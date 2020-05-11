import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditorWindow extends Stage {

    private int screenshotWidth;
    private int screenshotHeight;
    private boolean isCropping = false;
    private int cropX1, cropY1, cropX2, cropY2;

    private Canvas mainLayer;
    private Canvas cropLayer;

    private GraphicsContext mainLayerContext;
    private GraphicsContext cropLayerContext;

    public EditorWindow(Stage mainWindow, Image screenshot) {
        super();

        setTitle("Screenshoter: Editor");
        setResizable(false);
        centerOnScreen();
        setOnCloseRequest(event -> {
            mainWindow.show();
        });

        screenshotWidth = (int) screenshot.getWidth();
        screenshotHeight = (int) screenshot.getHeight();

        cropX1 = 0;
        cropY1 = 0;
        cropX2 = screenshotWidth;
        cropY2 = screenshotHeight;

        mainLayer = new Canvas(screenshotWidth, screenshotHeight);
        mainLayerContext = mainLayer.getGraphicsContext2D();
        mainLayerContext.setLineCap(StrokeLineCap.ROUND);
        mainLayerContext.drawImage(screenshot, 0, 0, screenshotWidth, screenshotHeight);

        cropLayer = new Canvas(screenshotWidth, screenshotHeight);
        cropLayerContext = cropLayer.getGraphicsContext2D();
        cropLayerContext.setLineWidth(1);
        cropLayerContext.setStroke(Color.BLACK);

        Pane layers = new Pane(mainLayer, cropLayer);
        registerDrawingListeners();

        HBox toolsBar = setupToolsBar();

        HBox buttonsBar = setupButtonsBar();

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(toolsBar);
        borderPane.setRight(buttonsBar);
        borderPane.setPrefSize(screenshotWidth, 50);
        borderPane.setMaxSize(screenshotWidth, 50);
        borderPane.setMinSize(screenshotWidth, 50);

        borderPane.setPadding(new Insets(0, 20, 0, 20));

        VBox vBox = new VBox(borderPane, layers);
        Scene scene = new Scene(vBox);
        registerHotKeys(scene);
        setScene(scene);
        show();
    }

    void registerHotKeys(Scene scene) {
        scene.setOnKeyPressed(event -> {
            File file = null;
            if (event.getCode() == KeyCode.S && event.isShiftDown() && event.isControlDown()) {
                file = openSaveFileDialog();
            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                file = new File(String.format("%s/screenshoter/%s.png",
                        System.getProperty("user.home"),
                        new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date())));
            }
            if (file != null) {
                saveScreenshot(file);
                new SuccessWindow(file);
            }
        });
    }

    File openSaveFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG File", "*.png"));
        fileChooser.setTitle("Save screenshot");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        return fileChooser.showSaveDialog(this);
    }

    void registerDrawingListeners() {
        cropLayer.setOnMousePressed(event -> {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (isCropping) {
                cropLayerContext.clearRect(0, 0, screenshotWidth, screenshotHeight);
                cropX1 = x;
                cropY1 = y;
            } else {
                mainLayerContext.beginPath();
                mainLayerContext.moveTo(x, y);
            }
        });

        cropLayer.setOnMouseDragged(event -> {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (isCropping) {
                cropX2 = x;
                cropY2 = y;
                cropLayerContext.clearRect(0, 0, screenshotWidth, screenshotHeight);
                drawSelection(cropLayerContext, cropX1, cropY1, cropX2, cropY2);
            } else {
                mainLayerContext.lineTo(x, y);
                mainLayerContext.stroke();
            }
        });

        cropLayer.setOnMouseReleased(event -> {
            isCropping = false;
        });
    }

    HBox setupButtonsBar() {
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            File file = openSaveFileDialog();
            saveScreenshot(file);
        });
        HBox buttonsBar = new HBox(saveButton);
        buttonsBar.setAlignment(Pos.CENTER);
        buttonsBar.setSpacing(15);
        return buttonsBar;
    }

    HBox setupToolsBar() {
        Button cropButton = new Button();
        ImageView cropButtonIcon = new ImageView(getClass().getClassLoader().getResource("crop.png").toString());
        cropButtonIcon.setFitWidth(20);
        cropButtonIcon.setFitHeight(20);
        cropButton.setGraphic(cropButtonIcon);
        cropButton.setOnAction(event -> {
            isCropping = true;
        });

        ColorPicker colorPicker = new ColorPicker(Color.RED);
        colorPicker.setOnAction(event -> {
            mainLayerContext.setStroke(colorPicker.getValue());
        });

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
            mainLayerContext.setLineWidth(lineWidthSlider.getValue());
        });

        mainLayerContext.setStroke(colorPicker.getValue());
        mainLayerContext.setLineWidth(lineWidthSlider.getValue());

        HBox toolsBar = new HBox(cropButton, colorPicker, lineWidthSlider);
        toolsBar.setAlignment(Pos.CENTER);
        toolsBar.setSpacing(15);
        return toolsBar;
    }

    void saveScreenshot(File file) {
        validateCrop();
        int cropX = cropX1;
        int cropY = cropY1;
        int cropWidth = cropX2 - cropX1;
        int cropHeight = cropY2 - cropY1;

        WritableImage writableImage = new WritableImage(cropWidth, cropHeight);
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setViewport(new Rectangle2D(cropX, cropY, cropWidth, cropHeight));
        mainLayer.snapshot(snapshotParameters, writableImage);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void validateCrop() {
        if (cropX2 < cropX1) { int t = cropX1; cropX1 = cropX2; cropX2 = t; }
        if (cropY2 < cropY1) { int t = cropY1; cropY1 = cropY2; cropY2 = t; }
    }

    void drawSelection(GraphicsContext context, int x1, int y1, int x2, int y2) {
        if (x2 < x1) { int t = x1; x1 = x2; x2 = t; }
        if (y2 < y1) { int t = y1; y1 = y2; y2 = t; }
        Paint currentStroke = context.getStroke();
        int currentLineWidth = (int) context.getLineWidth();
        context.setStroke(Color.BLACK);
        context.setLineWidth(1);
        context.strokeRect(x1, y1, x2 - x1, y2 - y1);
        context.setStroke(currentStroke);
        context.setLineWidth(currentLineWidth);
    }

}
