package com.prepper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //gets the width and height of the screen - enables setting to full screen - hooray
        javafx.geometry.Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),1024, 768);
        scene.getStylesheets().add(getClass().getResource("globalStyle.css").toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(1024);
        stage.setHeight(768);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}