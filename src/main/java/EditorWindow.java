import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class EditorWindow extends Stage {

    public EditorWindow(Stage mainWindow, Image screenshot) {
        super();

        setTitle("Screenshoter: Editor");
        setResizable(false);
        centerOnScreen();
        setOnCloseRequest(event -> {
            mainWindow.show();
        });

        int screenshotWidth = (int) screenshot.getWidth();
        int screenshotHeight = (int) screenshot.getHeight();

        Canvas canvas = new Canvas(screenshotWidth, screenshotHeight);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setLineCap(StrokeLineCap.ROUND);
        context.drawImage(screenshot, 0, 0, screenshotWidth, screenshotHeight);

        ColorPicker colorPicker = new ColorPicker(Color.RED);

        Slider lineWidthSlider = new Slider(1.0, 36.0, 5.0);
        lineWidthSlider.setPrefWidth(300);
        lineWidthSlider.setMaxWidth(300);
        lineWidthSlider.setMinWidth(300);
        lineWidthSlider.setMinorTickCount(4);
        lineWidthSlider.setMajorTickUnit(5.0);
        lineWidthSlider.setBlockIncrement(1.0);
        lineWidthSlider.setShowTickMarks(true);
        lineWidthSlider.setShowTickLabels(true);
        lineWidthSlider.setSnapToTicks(true);
        lineWidthSlider.valueProperty().addListener((obs) -> {
            context.setLineWidth(lineWidthSlider.getValue());
        });

        context.setStroke(colorPicker.getValue());
        context.setLineWidth(lineWidthSlider.getValue());

        colorPicker.setOnAction(event -> {
            context.setStroke(colorPicker.getValue());
        });

        canvas.setOnMousePressed(event -> {
            context.beginPath();
            context.moveTo(event.getX(), event.getY());
        });

        canvas.setOnMouseDragged(event -> {
            context.lineTo(event.getX(), event.getY());
            context.stroke();
        });

        HBox toolsBar = new HBox(colorPicker, lineWidthSlider);
        toolsBar.setAlignment(Pos.CENTER);
        toolsBar.setSpacing(15);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            WritableImage writableImage = new WritableImage(screenshotWidth, screenshotHeight);
            canvas.snapshot(null, writableImage);
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG File", "*.png"));
            fileChooser.setTitle("Select path to save");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File file = fileChooser.showSaveDialog(this);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        HBox buttonsBar = new HBox(saveButton);
        buttonsBar.setAlignment(Pos.CENTER);
        buttonsBar.setSpacing(15);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(toolsBar);
        borderPane.setRight(buttonsBar);
        borderPane.setPrefSize(screenshotWidth, 50);
        borderPane.setMaxSize(screenshotWidth, 50);
        borderPane.setMinSize(screenshotWidth, 50);

        borderPane.setPadding(new Insets(0, 20, 0, 20));

        VBox vBox = new VBox(borderPane, canvas);
        Scene scene = new Scene(vBox);

        setScene(scene);
        show();
    }

}
