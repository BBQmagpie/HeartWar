package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {
    private static Stage guiStage;

    public static Stage getStage() {
        return guiStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = new HomePageGUI();
        primaryStage.setTitle("HEART WAR");
        guiStage=primaryStage;
        primaryStage.setScene(new Scene(root, 960, 600));
        primaryStage.show();

        guiStage.setOnCloseRequest(e->{
            Alert closingAlert=new Alert(Alert.AlertType.CONFIRMATION);
            closingAlert.setTitle("Exit Confirm");
            closingAlert.setHeaderText("Are you sure to exit? Data only saved after game done.");
            Optional<ButtonType> result=closingAlert.showAndWait();
            if(result.get()==ButtonType.OK){
                try {
                    MainProgram.end();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                guiStage.close();
            }else{
                e.consume();
            }
        });
    }


    public static void main(String[] args) throws Exception {
        MainProgram.initiation();
        launch(args);
    }
}
