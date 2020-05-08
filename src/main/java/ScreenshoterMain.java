import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ScreenshoterMain extends Application {

    public void start(Stage stage) {
        Group group = new Group();
        Scene scene = new Scene(group, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
