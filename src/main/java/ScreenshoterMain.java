import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.util.Duration;

import java.util.function.Consumer;

public class ScreenshoterMain extends Application {

    public static final int MAIN_WINDOW_WIDTH = 300;
    public static final int MAIN_WINDOW_HEIGHT = 200;

    public void start(Stage stage) {
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

        Button takeScreenShotButton = new Button("Take screenshot!");

        VBox vBox = new VBox(label, delaySlider, takeScreenShotButton, checkBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(15);

        takeScreenShotButton.setOnAction(event -> {
            vBox.getChildren().clear();
            int delay = (int) delaySlider.getValue();
            Label counter = new Label(Integer.toString(delay));
            counter.setFont(new Font(30));
            vBox.getChildren().add(counter);
            startDelayTimer(delay, currentSecond -> counter.setText(Integer.toString(currentSecond)), () -> {
                if (checkBox.isSelected()) {
                    Platform.runLater(() -> {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        stage.hide();
                        takeScreenshot();
                    });
                }
            });
        });

        BorderPane root = new BorderPane(vBox);
        Scene scene = new Scene(root, MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT);
        stage.setTitle("Screenshoter");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void startDelayTimer(int delay, Consumer<Integer> onUpdate, Runnable onFinish) {
        if (delay == 0) {
            onFinish.run();
            return;
        }
        final int[] k = { delay };
        Timeline delayTimer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            k[0]--;
            onUpdate.accept(k[0]);
        }));
        delayTimer.setCycleCount(delay);
        delayTimer.setOnFinished(event -> {
            onFinish.run();
        });
        delayTimer.play();
    }

    private void takeScreenshot() {

    }

    public static void main(String[] args) {
        launch(args);
    }

}
