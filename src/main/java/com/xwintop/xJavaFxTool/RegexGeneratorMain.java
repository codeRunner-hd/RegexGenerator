package com.xwintop.xJavaFxTool;

import com.xwintop.xcore.util.javafx.JavaFxSystemUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author admin
 */
@Slf4j
public class RegexGeneratorMain extends Application {
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = RegexGeneratorMain.getFxmlLoader();
        ResourceBundle resourceBundle = fxmlLoader.getResources();
        Parent root = fxmlLoader.load();
        primaryStage.setResizable(true);
        primaryStage.setTitle(resourceBundle.getString("Title"));
        primaryStage.getIcons().add(new Image("/images/RegexGeneratorIcon.png"));
        double[] screenSize = JavaFxSystemUtil.getScreenSizeByScale(0.74D, 0.8D);
        primaryStage.setScene(new Scene(root, screenSize[0], screenSize[1]));
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    public static FXMLLoader getFxmlLoader() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("locale.RegexGenerator");
        URL url = Object.class.getResource("/com/xwintop/xJavaFxTool/fxmlView/codeTools/RegexGenerator.fxml");
        return new FXMLLoader(url, resourceBundle);
    }
}