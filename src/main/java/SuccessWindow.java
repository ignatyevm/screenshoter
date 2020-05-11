import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;

public class SuccessWindow extends Stage {

    public SuccessWindow(File file) {
        Label appName = new Label("Screenshoter");
        appName.setFont(new Font(20));
        Label text = new Label(String.format("Saved to %s", file.getAbsolutePath()));
        text.setFont(new Font(15));
        ImageView appIcon = new ImageView(getClass().getClassLoader().getResource("app_icon.png").toString());
        appIcon.setFitWidth(162);
        appIcon.setFitHeight(128);
        VBox vBox = new VBox(appName, text);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);
        HBox hBox = new HBox(appIcon, vBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(15);
        hBox.setPadding(new Insets(15, 15, 15, 15));
        Scene scene = new Scene(hBox);
        setScene(scene);
        setResizable(false);
        centerOnScreen();
        setTitle("Screenshot saved successfully");
        show();
    }

}
