/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.rutinas;

import ipccrono.stages.rutina.Rutina;
import ipccrono.Main;
import ipccrono.stages.ejercicios.Ejercicio;
import ipccrono.stages.rutina.FXMLRutinaController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author FMR
 */
public class FXMLRutinasController implements Initializable {
    
    @FXML
    private BorderPane mainPane;
    @FXML
    private ListView<Rutina> listView;
    
    private static ObservableList<Rutina> rutinas;
    @FXML
    private Button boton;
    @FXML
    private Button addRut;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boton.setOnMouseEntered((event) -> {
            boton.setStyle("-fx-background-color:#ffc849;");
        });
        boton.setOnMouseExited((event) -> {
            boton.setStyle("-fx-background-color:orange;");
        });
        addRut.setOnMouseEntered((event) -> {
            addRut.setStyle("-fx-background-color:#ffc849;-fx-background-radius:50;-fx-border-radius:50;");
        });
        addRut.setOnMouseExited((event) -> {
            addRut.setStyle("-fx-background-color:orange;-fx-background-radius:50;-fx-border-radius:50;");
        });
//        listView.setCellFactory(lv -> new ListCelda());
listView.setCellFactory(new Callback<ListView<Rutina>, ListCell<Rutina>>() {
    @Override
    public ListCell<Rutina> call(ListView<Rutina> param) {
        return new ListCelda();
    }
});
ObservableList<Ejercicio> ejemplo = FXCollections.observableArrayList();
ejemplo.add(new Ejercicio("Abdominales",5));
Rutina ejemploRutina = new Rutina("Rutina fitness", 3, 20, ejemplo , 5);

ObservableList<Ejercicio> ejemplo2 = FXCollections.observableArrayList();
ejemplo2.addAll(new Ejercicio("Sentadilas",3),new Ejercicio("Abdominales",5),new Ejercicio("Flexiones",10));
Rutina ejemploRutina2 = new Rutina("Rutina #2", 4, 10, ejemplo2 , 15);

rutinas = FXCollections.observableArrayList();
rutinas.addAll(ejemploRutina,ejemploRutina2);
listView.setItems(rutinas);
    }
    
    @FXML
    private void cronometro(ActionEvent event) {
        if(listView.getSelectionModel().getSelectedItem() != null){
            Main.getMainController().init(listView.getSelectionModel().getSelectedItem());
        }else{
            if(Main.getMainController().getRutina() != null){
                Main.getMainController().init(null);
            }
        }
        Main.switchScene(Main.MAIN_STAGE);
    }
    
    @FXML
    private void addRutina(ActionEvent event) {
        Main.getRutinaController().clearData();
        FXMLRutinaController.editingRutina = false;
        Main.switchScene(Main.ADD_EDIT_RUTINA_STAGE);
    }
    
    public Button getBoton(){
        return boton;
    }
    
    public ObservableList<Rutina> getRutinas() {
        return rutinas;
    }
    
    public ListView<Rutina> getListView() {
        return listView;
    }
    
}
class ListCelda extends ListCell<Rutina> {
    private Button btn,btn2,btn3;
    private Rutina rutina;
    @Override
    protected void updateItem(Rutina item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null || empty) {
            setGraphic(null);
            setStyle("");
            setText(null);
        } else {
            setGraphic(null);
            setText(null);
            rutina = item;
            btn = new Button("ELIMINAR");
            Image img = new Image("/ipccrono/resources/cancel.png");
            ImageView imgView = new ImageView(img);
            imgView.setFitHeight(28);
            imgView.setFitWidth(28);
            btn.setGraphic(imgView);
            btn.setOnAction((event) -> {
                Main.getRutinasController().getRutinas().remove(rutina);
            });
            
            btn2 = new Button("EDITAR");
            Image img2 = new Image("/ipccrono/resources/pencil.png");
            ImageView imgView2 = new ImageView(img2);
            imgView2.setFitHeight(28);
            imgView2.setFitWidth(28);
            btn2.setGraphic(imgView2);
            btn2.setOnAction((ActionEvent event) -> {
                FXMLRutinaController.editingRutina = true;
                FXMLRutinaController.index = Main.getRutinasController().getRutinas().indexOf(rutina);
                System.out.println("index of rutina: "+Main.getRutinasController().getRutinas().indexOf(rutina));
                System.out.println("RUtinas; "+Main.getRutinasController().getRutinas());
                System.out.println("rutina actual: "+rutina);
                Main.getRutinaController().setData(rutina.getName(), rutina.getRepeticiones(), rutina.getDescansoRepet(), rutina.getDescansoEjs(), rutina.getEjercicios());
                Main.switchScene(Main.ADD_EDIT_RUTINA_STAGE);
            });
            
            btn3 = new Button("SELECCIONAR");
            Image img3 = new Image("/ipccrono/resources/check.png");
            ImageView imgView3 = new ImageView(img3);
            imgView3.setFitHeight(28);
            imgView3.setFitWidth(28);
            btn3.setGraphic(imgView3);
            btn3.setOnAction((event) -> {
                Main.getRutinasController().getListView().getSelectionModel().clearSelection();
                Main.getRutinasController().getListView().getSelectionModel().select(item);
                Main.getRutinasController().getBoton().fire();
            });
            
            btn.setStyle("-fx-background-color:orange;");
            btn2.setStyle("-fx-background-color:orange;");
            btn3.setStyle("-fx-background-color:orange;");
            
            btn.setOnMouseEntered((event) -> {
                btn.setStyle("-fx-background-color:#ffc849;");
            });
            btn.setOnMouseExited((event) -> {
                btn.setStyle("-fx-background-color:orange;");
            });
            btn2.setOnMouseEntered((event) -> {
                btn2.setStyle("-fx-background-color:#ffc849;");
            });
            btn2.setOnMouseExited((event) -> {
                btn2.setStyle("-fx-background-color:orange;");
            });
            btn3.setOnMouseEntered((event) -> {
                btn3.setStyle("-fx-background-color:#ffc849;");
            });
            btn3.setOnMouseExited((event) -> {
                btn3.setStyle("-fx-background-color:orange;");
            });
            
            GridPane gp = new GridPane();
            ColumnConstraints col1 = new ColumnConstraints();
            col1.setPercentWidth(46);
            ColumnConstraints col2 = new ColumnConstraints();
            col2.setPercentWidth(20);
            ColumnConstraints col3 = new ColumnConstraints();
            col3.setPercentWidth(14);
            ColumnConstraints col4 = new ColumnConstraints();
            col4.setPercentWidth(20);
            gp.getColumnConstraints().addAll(col1,col2,col3,col4);
            gp.add(new Label(item.getName()), 0, 0);
            gp.add(btn3, 1, 0);
            gp.add(btn2, 2, 0);
            gp.add(btn, 3, 0);
            
//            HBox h = new HBox(new Label(item.getName()),imgView,imgView2);
//            h.setSpacing(5);
//            setGraphic(h);
setGraphic(gp);
        }
    }
}