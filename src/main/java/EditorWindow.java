import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

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
        toolsBar.setPrefSize(screenshotWidth, 50);
        toolsBar.setMaxSize(screenshotWidth, 50);
        toolsBar.setMinSize(screenshotWidth, 50);
        toolsBar.setAlignment(Pos.CENTER);
        toolsBar.setSpacing(15);

        VBox vBox = new VBox(toolsBar, canvas);
        Scene scene = new Scene(vBox);

        setScene(scene);
        show();
    }

}
