package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Chris on 3/16/2018.
 */
public class SelectCustomerController implements Initializable {

    //Variable Declarations
    @FXML private TableView customersTable;
    @FXML private TableColumn fnameColumn;
    @FXML private TableColumn lnameColumn;
    @FXML private TableColumn phoneColumn;
    @FXML private TableColumn addressColumn;
    @FXML private TableColumn idColumn;


    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchCombo;
    @FXML private ImageView imageView;
    @FXML private Button selectButton;

    @FXML private Text firstNameT;
    @FXML private Text lastNameT;
    @FXML private Text phoneT;
    @FXML private Text addressT;
    @FXML private Pane customerPane;

    private static Connection con;
    private ObservableList<CustomerDataModel> data;
    private final FileChooser fileChooser = new FileChooser();
    private Desktop desktop = Desktop.getDesktop();
    private static String imageName;
    private static  String id = "";
    private String dvdId = "";
    private static DvdObject dvdObject;
    private static CustomerObject customerObject;
    @FXML private VBox mainVBox;
    private Stage parentStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        searchCombo.getItems().setAll("Drama","Action","Adult");
        searchCombo.getSelectionModel().selectFirst();

        try {

            con = new DbConnect().getDbConnect();
            data = FXCollections.observableArrayList();

            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM `customers`;");

            while (rs1.next()) {
                data.add(new CustomerDataModel(rs1.getString("customer_id"),rs1.getString("firstname"),rs1.getString("lastname"),rs1.getString("phone"), rs1.getString("address"),rs1.getString("created_at"), count));
            }

            idColumn.setCellValueFactory(new PropertyValueFactory("id"));
            lnameColumn.setCellValueFactory(new PropertyValueFactory("lname"));
            addressColumn.setCellValueFactory(new PropertyValueFactory("address"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory("phone"));
            fnameColumn.setCellValueFactory(new PropertyValueFactory("fname"));


            customersTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                    int pos = customersTable.getSelectionModel().getSelectedIndex();

                    if (pos >= 0) {
                        id = idColumn.getCellData(pos).toString();
                        System.out.println(id);
                        String fname = fnameColumn.getCellData(pos).toString().toUpperCase();
                        String lname = lnameColumn.getCellData(pos).toString().toUpperCase();
                        String phone = phoneColumn.getCellData(pos).toString().toUpperCase();
                        String address = addressColumn.getCellData(pos).toString().toUpperCase();
                        firstNameT.setText(fname);
                        lastNameT.setText(lname);
                        phoneT.setText(phone);
                        addressT.setText(address);

                        customerObject = new CustomerObject(id,fname,lname,phone,address);
                    }

                    if(!customerPane.isVisible()){
                        customerPane.setVisible(true);
                    }
                }

            });



            // 1. Wrap the ObservableList in a FilteredList (initially display all data).
            FilteredList<CustomerDataModel> filteredData = new FilteredList<>(data, p -> true);

            // 2. Set the filter Predicate whenever the filter changes.
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(dvdData -> {
                    // If filter text is empty, display all persons.
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    // Compare first name and last name of every person with filter text.
                    String lowerCaseFilter = newValue.toLowerCase();

                    switch(searchCombo.getSelectionModel().getSelectedItem()) {
                        case "BY First Name":
                            if (dvdData.fnameProperty().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                return true; // Filter matches first name.
                            }
                            break;
                        case "BY Phone":
                            if (dvdData.phoneProperty().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                return true; // Filter matches first name.
                            }
                            break;
                        case "BY ID":
                            if (dvdData.idProperty().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                return true; // Filter matches first name.
                            }
                            break;
                        default:
                            if (dvdData.fnameProperty().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                return true; // Filter matches first name.
                            }
                            break;

                    }

                    return false; // Does not match.
                });
            });


            // 3. Wrap the FilteredList in a SortedList.
            SortedList<CustomerDataModel> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            // 	  Otherwise, sorting the TableView would have no effect.
            sortedData.comparatorProperty().bind(customersTable.comparatorProperty());

            // 5. Add sorted (and filtered) data to the table.
            customersTable.setItems(sortedData);



            /*employeeTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    int pos = employeeTable.getSelectionModel().getSelectedIndex();
                    firstTime ++;
                    if (firstTime > 1) {
                        if (pos >= 0) {
                            String data = idColumn.getCellData(pos).toString();

                            try {
                                Parent root = FXMLLoader.load(getClass().getResource("details.fxml"));
                                primaryStage = new Stage();
                                primaryStage.setScene(new Scene(root, 777, 532));
                                primaryStage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }
            });*/


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");

        }

    }
    @FXML void insertDetails(ActionEvent event) {

                if(dvdObject != null) {

                   int option = JOptionPane.showConfirmDialog(null,"Rent Dvd "+dvdObject.getName()+" at price "+dvdObject.getPrice() +" For "+customerObject.getFname()+ " "+customerObject.getLname() +" ?");

                    if(option == 0) {
                        String SQL1 = "INSERT INTO `rent`( `customer_id`, `dvd_id`) VALUES(?,?)";

                        try {

                            con = new DbConnect().getDbConnect();
                            con.setAutoCommit(false); //
                            PreparedStatement preparedStatement1 = new DbConnect().getDbConnect().prepareStatement(SQL1);
                            preparedStatement1.setString(1, id);
                            preparedStatement1.setString(2, dvdId);
                            preparedStatement1.executeUpdate();
                            con.commit(); //Commit the Changes

                            JOptionPane.showMessageDialog(null, "Successfully Rented Dvd !", "Rent A DvD", JOptionPane.PLAIN_MESSAGE);


                        } catch (Exception e) {
                            try {
                                con.rollback(); //Rollback
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        } finally {
                            try {
                                con.setAutoCommit(true); //Set Autocomit to true
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{

                    }
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("fxml/RentDvd.fxml"));
                        Stage mainStage = (Stage) mainVBox.getScene().getWindow();
                        mainStage.setScene(new Scene(root));
                        Screen screen = Screen.getPrimary();
                        Rectangle2D bounds = screen.getVisualBounds();
                        mainStage.setX(bounds.getMinX());
                        mainStage.setY(bounds.getMinY());
                        mainStage.setWidth(bounds.getWidth());
                        mainStage.setHeight(bounds.getHeight());
                        //primaryStage.setFullScreen(true);
                        //  parentStage.close();

                        mainStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
    }

    public void setDvdId(String dvdId) {
        this.dvdId = dvdId;
    }

    public void setDvdObject(DvdObject dvdObject) {
        this.dvdObject = dvdObject;
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }
}
