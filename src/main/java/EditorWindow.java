import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditorWindow extends Stage {

    public EditorWindow(Stage mainWindow, Image screenshot) {
        super();

        setTitle("Screenshoter: Editor");
        setOnCloseRequest(event -> {
            mainWindow.show();
        });

        Canvas canvas = new Canvas(screenshot.getWidth(), screenshot.getHeight());
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.drawImage(screenshot, 0, 0, screenshot.getWidth(), screenshot.getHeight());

        HBox toolsBar = new HBox();
        VBox vBox = new VBox(toolsBar, canvas);
        Scene scene = new Scene(vBox);
        setScene(scene);
        show();
    }

}
