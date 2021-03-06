/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.main;

import ipccrono.Main;
import ipccrono.stages.ejercicios.Ejercicio;
import ipccrono.stages.rutina.Rutina;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 *
 * @author FMR
 */
public class FXMLMainController implements Initializable {
    
    private static Rutina rutina = null;
    private int i,t,totalTime;
    private IntervalTimerS timer;
    
    @FXML
    private Pane pane;
    @FXML
    private Arc progressCircle;
    @FXML
    private Circle innerCircle;
    @FXML
    private Button btn;
    private ImageView menuButton;
    
    private boolean menuShown = false;
    private Pane menu;
    @FXML
    private BorderPane mainPane;
    @FXML
    private HBox hBar;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView imageBtn;
    @FXML
    private Label ejercicioTime;
    @FXML
    private Button restart;
    @FXML
    private Button next;
    
    private BooleanProperty paused = new SimpleBooleanProperty(true);
    
    private Image playImg,pauseImg;
    @FXML
    private Label nRepeticion;
    @FXML
    private Label rutinaYEjercicio;
    @FXML
    private Label rutinaTime;
    
    @FXML
    private Button statsButton;
    
    private long ejTime = 0;
    private long descTime = 0;
    long lastParado=0;
    @FXML
    private Button rutinasBtn;
    @FXML
    private BorderPane borderPane;
    
    private static Label ejercicio;
    private static StackPane h;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playImg = new Image(getClass().getResourceAsStream("/ipccrono/resources/play.png"));
        pauseImg = new Image(getClass().getResourceAsStream("/ipccrono/resources/pausa.png"));
        init(null);
    }
    
    
    public Boolean getPaused() {
        return paused.get();
    }
    
    public void setPaused(BooleanProperty value) {
        paused = value;
    }
    
    public BooleanProperty pausedProperty() {
        return paused;
    }
    
    public Arc getProgressCircle() {
        return progressCircle;
    }
    
    public Circle getInnerCircle() {
        return innerCircle;
    }
    
    public Button getBtn() {
        return btn;
    }
    
    private void menu(ActionEvent event) {
        if(menuShown){
            AnchorPane.setTopAnchor(menu, 460d);
            AnchorPane.setBottomAnchor(menu, null);
            menuButton.setRotate(270);
        }else{
            AnchorPane.setBottomAnchor(menu, 0d);
            AnchorPane.setTopAnchor(menu, null);
            menuButton.setRotate(90);
        }
        menuShown = !menuShown;
    }
    
    @FXML
    private void rutinas(ActionEvent event) throws Exception{
        statsButton.setVisible(false);
        Main.getMainController().getProgressCircle().setFill(Color.DODGERBLUE);
        Main.switchScene(Main.RUTINAS_STAGE);
    }
    
    public void init(Rutina r){
        rutinasBtn.setOnMouseEntered((event) -> {
            rutinasBtn.setStyle("-fx-background-color:#ffc849;");
        });
        rutinasBtn.setOnMouseExited((event) -> {
            rutinasBtn.setStyle("-fx-background-color:orange;");
        });
        ejercicio = new Label("Ejercicio: - ");
        ejercicio.setStyle("-fx-text-fill:white;");
        ejercicio.setFont(Font.font("Berlin Sans FB", 35));
        h = new StackPane(ejercicio,statsButton);
        h.setAlignment(Pos.CENTER);
        borderPane.setBottom(h);
        BorderPane.setAlignment(borderPane, Pos.CENTER);
        
        ejercicio.visibleProperty().bind(statsButton.visibleProperty().not());
        
        rutinasBtn.disableProperty().bind(pausedProperty().not());
        ejTime = 0;
        descTime = 0;
        rutina = r;
        if(rutina == null) {
            btn.setDisable(true);
            restart.setDisable(true);
            next.setDisable(true);
            progressCircle.lengthProperty().unbind();
            progressCircle.setLength(0);
            
            nRepeticion.textProperty().unbind();
            nRepeticion.setText("REPETICION 0/0");
            rutinaYEjercicio.textProperty().unbind();
            rutinaYEjercicio.setText("Rutina: -");
            ejercicio.textProperty().unbind();
            ejercicio.setText("Ejercicio: -");
            ejercicioTime.textProperty().unbind();
            ejercicioTime.setText("00:00:00");
            rutinaTime.textProperty().unbind();
            rutinaTime.setText("De: 00:00:00");
            
        }else {
            timer = null;
            timer = new IntervalTimerS();
            timer.setSesionTipo(rutina);
            timer.setStoppedDuration(0L);
            timer.setStoppedDurationRutina(0L);
            timer.setOnFailed((event) -> {
                System.out.println("FAILEDDD!!");
                
                init(rutina);
                btn.fire();
            });
            
            ejercicioTime.textProperty().bind(timer.tiempoProperty());
            rutinaTime.textProperty().bind(timer.tiempoRemainingProperty());
            rutinaYEjercicio.textProperty().bind(timer.rutinaProperty());
            ejercicio.textProperty().bind(timer.ejercicioProperty());
            nRepeticion.textProperty().bind(timer.repeticionProperty());
            
            timer.setOnSucceeded(c -> {
                if (timer != null && timer.getValue() != null && timer.getValue()) {
                    System.out.println("FINISHED!");
                    
                    long descansos;
                    if(paused.get()){
                        descansos = System.currentTimeMillis() - lastParado;
                    }else{
                        descansos = 0;
                    }
                    timer.setStoppedTime(0L);
                    statsButton.setVisible(true);
                    Main.getGraficasController().clear();
                    System.out.println("rutina: "+rutina);
                    System.out.println("descansos: "+(descansos/1000));
                    Main.getGraficasController().setData(rutina.getEjercicioTime() - (ejTime/1000), rutina.getEjercicioTime(), timer.getStoppedDuration()+(descansos/1000) + rutina.getDescansoTime() - (descTime/1000), rutina.getDescansoTime());
                    timer.reset();
                    init(rutina);
                }
            });
            
            btn.setDisable(false);
            restart.setDisable(true);
            next.setDisable(true);
            paused.setValue(true);
            imageBtn.setImage(playImg);
            progressCircle.setStartAngle(90);
            progressCircle.lengthProperty().bind(timer.ejercicioProgressProperty());
        }
    }
    
    @FXML
    private void action(ActionEvent e) {
        if(e.getSource() == btn && paused.getValue()) {
            Main.click.play();
            play();
        } else if(e.getSource() == btn && !paused.getValue()) {
            Main.click.play();
            pause();
        }else if(e.getSource() == next){
            Main.click.play();
            switch(timer.getEstadoActual()){
                case TRABAJO:
                    ejTime += timer.getMillisTrans();
                    System.out.println("ejTime: "+ejTime);
                    break;
                case DESCANSO_CIRCUITO:
                case DESCANSO_EJERCICIO:
                    descTime += timer.getMillisTrans();
                    System.out.println("descTime: "+descTime);
                    break;
            }
            System.out.println("next");
            timer.setEjercicioProgress(0d);
            timer.setCambiarEstado(true);
            timer.restart();
            
        }else if(e.getSource() == restart){
            Main.getMainController().getProgressCircle().setFill(Color.DODGERBLUE);
            Main.click.play();
            System.out.println("restart");
            init(rutina);
        }else if(e.getSource() == statsButton) {
            Main.switchScene(Main.GRAFICAS_STAGE);
        }
        
    }
    
    public void play() {
        
        statsButton.setVisible(false);
        imageBtn.setImage(pauseImg);
        restart.setDisable(true);
        next.setDisable(true);
        paused.setValue(!paused.getValue());
        switch (timer.getState()/*getEstadoActual()*/) {
            case READY:
                timer.setStartTimings();
                System.out.println("start timings");
                timer.start();
                break;
            default:
                timer.restart();
                break;
        }
    }
    private void pause(){
        
        lastParado = System.currentTimeMillis();
        imageBtn.setImage(playImg);
        restart.setDisable(false);
        next.setDisable(false);
        paused.setValue(!paused.getValue());
        timer.cancel();
    }
    
    public Rutina getRutina(){
        return rutina;
    }
    
    public IntervalTimerS getTimer(){
        return timer;
    }
    public boolean isPaused(){
        return paused.getValue();
    }
    
    public String format(int n){
        return String.format("%02d", n);
    }
    
    public long getEjTimeStoped(){
        return ejTime;
    }
    
    public long getDescTimeStoped(){
        return descTime;
    }
}
