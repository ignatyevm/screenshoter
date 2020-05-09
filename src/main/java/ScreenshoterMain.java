import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ScreenshoterMain extends Application {

    public static final int MAIN_WINDOW_WIDTH = 300;
    public static final int MAIN_WINDOW_HEIGHT = 200;

    public void start(Stage stage) {
        Button takeScreenShotButton = new Button("Take screenshot!");
        CheckBox checkBox = new CheckBox("Hide this window?");
        checkBox.setAllowIndeterminate(false);
        Slider delaySlider = new Slider(0.0, 30.0, 0.0);
        delaySlider.setPrefWidth(MAIN_WINDOW_WIDTH - 20);
        delaySlider.setMaxWidth(MAIN_WINDOW_WIDTH - 20);
        delaySlider.setMinWidth(MAIN_WINDOW_WIDTH - 20);
        delaySlider.setMinorTickCount(4);
        delaySlider.setMajorTickUnit(5.0);
        delaySlider.setBlockIncrement(1.0);
        delaySlider.setShowTickMarks(true);
        delaySlider.setShowTickLabels(true);
        delaySlider.setSnapToTicks(true);

        Label label = new Label("Screenshot delay");
        label.setFont(new Font(14));

        VBox vBox = new VBox(label, delaySlider, takeScreenShotButton, checkBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);
        BorderPane root = new BorderPane(vBox);
        Scene scene = new Scene(root, MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT);
        stage.setTitle("Screenshoter");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
