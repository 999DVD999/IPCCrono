/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package ipccrono.stages.rutina;

import ipccrono.stages.ejercicios.Ejercicio;
import javafx.collections.ObservableList;

/**
 *
 * @author FMR
 */
public class Rutina {
    private String name;
    private int repeticiones;
    private int tDescansoRepeticiones;
    private ObservableList<Ejercicio> ejercicios;
    private int tDescansoEjercicios;
    private int totalTime;
    
    
    public Rutina(String nombre, int repet, int descansoRepet, ObservableList<Ejercicio> ejs, int descansoEjs){
        update(nombre,repet,descansoRepet,ejs,descansoEjs);
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getRepeticiones(){
        return this.repeticiones;
    }
    
    public int getDescansoRepet(){
        return this.tDescansoRepeticiones;
    }
    
    public int getDescansoEjs(){
        return this.tDescansoEjercicios;
    }
    
    public ObservableList<Ejercicio> getEjercicios(){
        return this.ejercicios;
    }

    void update(String nombre, int repet, int descansoRepet, ObservableList<Ejercicio> ejs, int descansoEjs) {
        name = nombre;
        repeticiones = repet;
        tDescansoRepeticiones = descansoRepet;
        ejercicios = ejs;
        tDescansoEjercicios = descansoEjs;
        
        int t=0;
        for(Ejercicio e:ejs){
            t += e.getTime() + tDescansoEjercicios;
        }
        t -= tDescansoEjercicios;
        
        totalTime = repet*(t + tDescansoRepeticiones) - tDescansoRepeticiones;
    }
    
    @Override
    public String toString() {
        return name + ", "+repeticiones+" repes con descanso: "+tDescansoRepeticiones+" y entre ejs: "+tDescansoEjercicios+", ejs: "+ejercicios;
    }
    
    public static int getH(int time){
        return time / 3600;
    }
    
    public static int getM(int time){
        return time/60 - getH(time) * 60;
    }
    
    public static int getS(int time){
        return time % 60;
    }
    
    public int getTime(){
        return totalTime;
    }
    
    public int getEjercicioTime(){
        int t = 0;
        for(Ejercicio e:ejercicios){
//            System.out.println("ejercicio: "+e.getName()+" -> "+ e.getTime());
            t += e.getTime();
        }
        return t * repeticiones;
    }
    
    public int getDescansoTime(){
//        System.out.println("ejs size: "+ejercicios.size()+" tdescansoEjs: "+tDescansoEjercicios+" tdescansoRepet: "+tDescansoRepeticiones+" repeticiones: "+repeticiones);
//        System.out.println("size -1*descEJs: "+ ((ejercicios.size() - 1) * tDescansoEjercicios )+" repes-1*descRut "+((repeticiones - 1) * tDescansoRepeticiones) );
        return (((ejercicios.size() - 1) * tDescansoEjercicios )*repeticiones + ((repeticiones - 1) * tDescansoRepeticiones));
    }
}
