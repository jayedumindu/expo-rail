import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage Login) throws IOException {
        Login.setScene(new Scene(FXMLLoader.load(getClass().getResource("view/login.fxml"))));
        Login.setResizable(false);
        Login.show();
    }

}
