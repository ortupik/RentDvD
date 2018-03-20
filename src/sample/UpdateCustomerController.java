package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.DbConnect;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;


public class UpdateCustomerController  implements Initializable {

    private static Connection con;
    private String id;

    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField phoneTF;
    @FXML private TextArea addressTA;

    private Stage primaryStage;

    protected void setId(String id){
        this.id = id;
    }

    @FXML
    void buttonClicked(ActionEvent event) throws SQLException {


        Object source = event.getSource();
        if (source instanceof Button) {
            Button clickedBtn = (Button) source;

            switch (clickedBtn.getId()) {
                case "updateButton":

                    con = new DbConnect().getDbConnect();
                    con.setAutoCommit(false);

                    String address = addressTA.getText();
                    String fnameS = firstName.getText();
                    String lnameS = lastName.getText();
                    String phonen = phoneTF.getText();


                    String sq11 = "UPDATE  customers SET `phone`='" + phonen + "',`firstname`='" + fnameS + "',`lastname`='" + lnameS + "',`address`='" + address + "' WHERE `id` ='" + id + "'";

                    try {
                        PreparedStatement pst1 = con.prepareStatement(sq11);
                        pst1.executeUpdate();
                        con.commit();//Commit the changes
                        JOptionPane.showMessageDialog(null, "Record Updated successfully", "Rent A Dvd", JOptionPane.PLAIN_MESSAGE);

                    } catch (SQLException e) {
                        e.printStackTrace();
                        con.rollback(); //Rollback any changes made
                    }
                    break;

            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

