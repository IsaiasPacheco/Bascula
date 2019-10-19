import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.*;
import javafx.scene.text.*;
import javafx.scene.*;
import java.util.*;
import javafx.animation.*;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.*;

import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import javafx.scene.image.*;
import java.io.*;

public class Bascula extends Parent{
	private VBox vb;
	private Label datos;
	private StackPane stp;
	private Dial segundero;
	private Group group;
	private Calendar calendar;
	public Bascula(){
		stp = new StackPane();
		segundero = new Dial(200,50,5,Color.CRIMSON);
		group = new Group();
		group.getChildren().add(stp);
		group.getChildren().addAll(segundero);
		vb = new VBox(10);
		vb.getChildren().add(group);
		datos = new Label("0.0 Kilos");
		vb.getChildren().add(datos);
		vb.setAlignment(Pos.CENTER);
		getChildren().add(vb);
		layoult();
		segundero.actualizar(0);
		addImage();
	}
	private void addImage(){
		try{
    		InputStream is = getClass().getResourceAsStream("box.png");
    		Image img = new Image(is);
    		ImageView iv = new ImageView(img);
    		iv.setFitWidth(70);
			iv.setFitHeight(70);
			datos.setGraphic(iv);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}	
	}

	private void addCircle(){
		try{
    		InputStream is = getClass().getResourceAsStream("aro.jpg");
    		Image img = new Image(is);
    		ImageView iv = new ImageView(img);
    		iv.setFitWidth(600);
			iv.setFitHeight(600);
			stp.getChildren().add(iv);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
	}

	private void layoult(){
		addCircle();
		Circle luna = new Circle(220, Color.WHITESMOKE);
		luna.setEffect(new InnerShadow());
		stp.getChildren().addAll(luna);

		Rectangle[] tickHora = new Rectangle[6];
		Text[] txt = new Text[6];
		for(int i = 0; i < 6; i++){
			txt[i] = new Text("");
			txt[i].setTranslateX(150*Math.cos(-(Math.PI/3)*i));
			txt[i].setTranslateY(150*Math.sin(-(Math.PI/3)*i));

			tickHora[i] = new Rectangle(20,5,Color.BLACK);
			tickHora[i].setTranslateX(190*Math.cos(-(Math.PI/3)*i));
			tickHora[i].setTranslateY(190*Math.sin(-(Math.PI/3)*i));
			tickHora[i].setRotate(-(180/3)*i);
		}
		txt[0].setText("1");
		txt[1].setText("0");
		txt[2].setText("5");
		txt[3].setText("4");
		txt[4].setText("3");
		txt[5].setText("2");

		stp.getChildren().addAll(txt);
		stp.getChildren().addAll(tickHora);

		Rectangle[] tickMinuto = new Rectangle[60];
		for(int i = 0; i < 60; i++){
			if((i >= 11 && i <= 19)){
				continue;
			}
			tickMinuto[i] = new Rectangle(10,2,Color.BLACK);
			tickMinuto[i].setTranslateX(190*Math.cos(-(Math.PI/30)*i));
			tickMinuto[i].setTranslateY(190*Math.sin(-(Math.PI/30)*i));
			tickMinuto[i].setRotate(-(180/30)*i);
			stp.getChildren().add(tickMinuto[i]);
		}

		group.getChildren().add(new Circle(300,300,10,Color.CRIMSON));
		group.getChildren().add(new Circle(300,300,5,Color.BLACK));
		actualizar();
	}

	private void actualizar(){
		calendar = Calendar.getInstance();
		int hor = calendar.get(Calendar.HOUR);
		int min = calendar.get(Calendar.MINUTE);
		int sec = calendar.get(Calendar.SECOND);
		double angulo = 360.0/6.0;
		PanamaHitek_Arduino ino = new PanamaHitek_Arduino();
		SerialPortEventListener listerner = new SerialPortEventListener() {
            @Override
            public void serialEvent(SerialPortEvent spe) {
                try {
                    if (ino.isMessageAvailable()) {
                        //Se toma el valor del puerto serial
                        String msj = ino.printMessage();
                        double valor = Double.parseDouble(msj);
                        if(valor > 5){
                        	valor = 5;
                        }
						segundero.actualizar(angulo*valor);
						datos.setText(valor+" Kilos");
                    }
                } catch (SerialPortException e) {
                    System.out.println("Error: " + e);
                } catch (ArduinoException e) {
                    System.out.println("Error: " + e);
                }
            }
		};
		
		try {
            ino.arduinoRX("COM6", 9600, listerner);
        } catch (ArduinoException e) {
            System.out.println("Error: " + e);
        } catch (SerialPortException e) {
            System.out.println("Error: " + e);
        }
	}
}