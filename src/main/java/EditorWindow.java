import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EditorWindow extends Stage {

    public EditorWindow(Stage mainWindow, Image screenshot) {
        super();

        setTitle("Screenshoter: Editor");
        setOnCloseRequest(event -> {
            mainWindow.show();
        });
        setResizable(false);
        centerOnScreen();

        int screenshotWidth = (int) screenshot.getWidth();
        int screenshotHeight = (int) screenshot.getHeight();

        System.out.println(screenshotWidth);
        System.out.println(screenshotHeight);

        Canvas canvas = new Canvas(screenshotWidth, screenshotHeight);
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.drawImage(screenshot, 0, 0, screenshotWidth, screenshotHeight);

        ColorPicker colorPicker = new ColorPicker(Color.RED);
        context.setStroke(colorPicker.getValue());

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

        HBox toolsBar = new HBox(colorPicker);
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
