import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;



public class Principal extends Application {
	private Scene scene;
	@Override
	public void start(Stage primaryStage){
		Bascula bascula = new Bascula();
		scene = new Scene(bascula);
		initComp();
		primaryStage.setScene(scene);
		primaryStage.setTitle("Bascula");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void initComp(){
		try{
			scene.getStylesheets().add(
				getClass().getResource("estilo.css").toExternalForm());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch();



	}
}