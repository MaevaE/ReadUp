package com.Readup.app;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class App extends Application {
    
    @Override
public void start(Stage stage) throws Exception {
    Parent splash = FXMLLoader.load(getClass().getResource("/fxml/splash.fxml"));
    Scene splashScene = new Scene(splash);
    stage.setScene(splashScene);
    stage.show();

    PauseTransition delay = new PauseTransition(Duration.seconds(3));
   
}

    public static void main(String[] args) {
        launch(args);
    }
}
