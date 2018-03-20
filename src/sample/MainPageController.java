package sample;

/**
 * Created by CHRISX-K on 11/29/2015.
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;


public class MainPageController {

    private Parent root;
    private Stage primaryStage;

        @FXML
        void buttonClicked(ActionEvent event) {
            primaryStage = new Stage();

            Object source = event.getSource();
            if (source instanceof Button) {
                Button clickedBtn = (Button) source;
                   // new Main().close();
                switch (clickedBtn.getId()){
                    case "dvdBtn":
                        try {
                            Parent root = FXMLLoader.load(getClass().getResource("fxml/ManageDvds.fxml"));
                            primaryStage.setScene(new Scene(root));
                            Screen screen = Screen.getPrimary();
                            Rectangle2D bounds = screen.getVisualBounds();
                            primaryStage.setX(bounds.getMinX());
                            primaryStage.setY(bounds.getMinY());
                            primaryStage.setWidth(bounds.getWidth());
                            primaryStage.setHeight(bounds.getHeight());
                            //primaryStage.setFullScreen(true);
                            new Main().close();
                            primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "customersBtn":
                        try {
                            root = FXMLLoader.load(getClass().getResource("fxml/ManageCustomers.fxml"));
                            primaryStage.setScene(new Scene(root));
                            Screen screen = Screen.getPrimary();
                            Rectangle2D bounds = screen.getVisualBounds();
                            primaryStage.setX(bounds.getMinX());
                            primaryStage.setY(bounds.getMinY());
                            primaryStage.setWidth(bounds.getWidth());
                            primaryStage.setHeight(bounds.getHeight());
                            //primaryStage.setFullScreen(true);
                            new Main().close();
                            primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;

                    case "rentBtn":
                        try {
                            root = FXMLLoader.load(getClass().getResource("fxml/RentDvd.fxml"));
                            primaryStage.setScene(new Scene(root));
                            Screen screen = Screen.getPrimary();
                            Rectangle2D bounds = screen.getVisualBounds();
                            primaryStage.setX(bounds.getMinX());
                            primaryStage.setY(bounds.getMinY());
                            primaryStage.setWidth(bounds.getWidth());
                            primaryStage.setHeight(bounds.getHeight());
                            //primaryStage.setFullScreen(true);
                            new Main().close();
                            primaryStage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        break;

                }

            }

        }
    public void close(){
        primaryStage.close();
    }
}
