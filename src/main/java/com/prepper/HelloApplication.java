package com.prepper;

import ai.djl.engine.Engine;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import eu.hansolo.fx.charts.*;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Engine engine = Engine.getEngine("PyTorch");
        System.out.println("Verison " + engine.getVersion());
        //gets the width and height of the screen - enables setting to full screen - hooray
        javafx.geometry.Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1330, 768);
        scene.getStylesheets().add(getClass().getResource("globalStyle.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(1330);
        stage.setHeight(768);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}