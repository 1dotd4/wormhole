import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientLauncher extends Application {
    
    public static void main(String[] args) {
        Application.launch(ClientLauncher.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("res/insertFile.fxml"));
        
        stage.setTitle("wormhole");
	//        stage.setScene(new Scene(root, Settings.maxPixel, Settings.maxPixel));
        stage.setScene(new Scene(root,512,256));
        stage.show();
	
    }
}
