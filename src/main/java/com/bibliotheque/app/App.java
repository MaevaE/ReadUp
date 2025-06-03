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
    stage.setMaximized(true);
    stage.show();

    PauseTransition delay = new PauseTransition(Duration.seconds(3));
    delay.setOnFinished(event -> {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainLayout.fxml"));
            Parent mainRoot = loader.load();
            Scene mainScene = new Scene(mainRoot);
            stage.setScene(mainScene);
            stage.setMaximized(true); // Pour qu’il prenne tout l’écran
        } catch (Exception e) {
            e.printStackTrace();
        }
    });
    delay.play();
}


    public static void main(String[] args) {
        launch(args);
    }
}
