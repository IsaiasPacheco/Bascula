import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.effect.*;
import javafx.scene.text.*;
import javafx.scene.*;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.control.*;
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
	//Textarea
	private HBox hboxText;
	private TextArea textArea;
	
	public Bascula(){
		stp = new StackPane();
		segundero = new Dial(200,50,5,Color.CRIMSON);
		group = new Group();
		group.getChildren().add(stp);
		group.getChildren().addAll(segundero);
		vb = new VBox();
		vb.getChildren().add(group);
		//TextArea
		hboxText = new HBox();
		textArea = new TextArea();

		
		textArea.setEditable(false);
		//textArea.setPadding(new Insets(0,0,0,40));
		
		datos = new Label(" Kilos");
		hboxText.setAlignment(Pos.CENTER);
		vb.getChildren().add(hboxText);
		addImage();
		hboxText.getChildren().add(textArea);
		hboxText.getChildren().add(datos);

		vb.setAlignment(Pos.CENTER);
		getChildren().add(vb);
		layoult();
		segundero.actualizar(0);
		textArea.setText(" 0.0 ");
	}
	private void addImage(){
		try{
    		InputStream is = getClass().getResourceAsStream("box.png");
    		Image img = new Image(is);
    		ImageView iv = new ImageView(img);
    		iv.setFitWidth(50);
			iv.setFitHeight(50);
			hboxText.getChildren().add(iv);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}	
	}

	private void addCircle(){
		try{
    		InputStream is = getClass().getResourceAsStream("arov2.jpg");
    		Image img = new Image(is);
    		ImageView iv = new ImageView(img);
    		iv.setFitWidth(600);
			iv.setFitHeight(600);
			stp.getChildren().add(iv);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
	}

	private void addMin(){
		Rectangle[] tickMinuto = new Rectangle[60];
		for(int i = 0; i < 60; i++){
			if((i >= 11 && i <= 19)){
				continue;
			}
			if( i%5 == 0 ){
				tickMinuto[i] = new Rectangle(20,2,Color.RED);	
			}else{
				tickMinuto[i] = new Rectangle(10,2,Color.BLACK);
			}
			tickMinuto[i].setTranslateX(190*Math.cos(-(Math.PI/30)*i));
			tickMinuto[i].setTranslateY(190*Math.sin(-(Math.PI/30)*i));
			tickMinuto[i].setRotate(-(180/30)*i);
			stp.getChildren().add(tickMinuto[i]);
		}
	}

	private void addHor(){
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
	}
	private void layoult(){
		addCircle();
		Circle luna = new Circle(220, Color.WHITESMOKE);
		luna.setEffect(new InnerShadow());
		stp.getChildren().addAll(luna);
		addMin();
		addHor();
		group.getChildren().add(new Circle(300,300,10,Color.CRIMSON));
		group.getChildren().add(new Circle(300,300,5,Color.BLACK));
		actualizar();
	}

	private void actualizar(){
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
						textArea.setText(valor+"");
                    }
                } catch (SerialPortException e) {
                    System.out.println("Error: " + e);
                } catch (ArduinoException e) {
                    System.out.println("Error: " + e);
                }
            }
		};
		try {
            ino.arduinoRX("COM4", 9600, listerner);
        } catch (ArduinoException e) {
            System.out.println("Error: " + e);
        } catch (SerialPortException e) {
            System.out.println("Error: " + e);
        }
	}
}