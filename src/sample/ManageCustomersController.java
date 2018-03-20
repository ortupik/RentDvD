package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Chris on 3/16/2018.
 */
public class ManageCustomersController implements Initializable {

    //Variable Declarations
    @FXML private TableView customersTable;
    @FXML private TableColumn fnameColumn;
    @FXML private TableColumn lnameColumn;
    @FXML private TableColumn phoneColumn;
    @FXML private TableColumn addressColumn;
    @FXML private TableColumn timeColumn;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn updateColumn;
    @FXML private TableColumn deleteColumn;


    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchCombo;
    @FXML private ImageView imageView;
    @FXML private Button addButton;

    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField phoneTF;
    @FXML private TextArea addressTA;

    @FXML private Button mainPageBtn;
    @FXML private VBox mainVBox;

    private static Connection con;
    private ObservableList<CustomerDataModel> data;
    private static  int count = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        searchCombo.getItems().setAll("Drama","Action","Adult");
        searchCombo.getSelectionModel().selectFirst();

        try {

            con = new DbConnect().getDbConnect();
            data = FXCollections.observableArrayList();

            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM `customers`;");

            while (rs1.next()) {
                data.add(new CustomerDataModel(rs1.getString("customer_id"),rs1.getString("firstname"),rs1.getString("lastname"),rs1.getString("phone"), rs1.getString("address"),rs1.getString("created_at"),count));
                count++;
            }

            idColumn.setCellValueFactory(new PropertyValueFactory("id"));
            lnameColumn.setCellValueFactory(new PropertyValueFactory("lname"));
            addressColumn.setCellValueFactory(new PropertyValueFactory("address"));
            phoneColumn.setCellValueFactory(new PropertyValueFactory("phone"));
            timeColumn.setCellValueFactory(new PropertyValueFactory("time"));
            fnameColumn.setCellValueFactory(new PropertyValueFactory("fname"));
            updateColumn.setCellValueFactory(new PropertyValueFactory("id"));
            deleteColumn.setCellValueFactory(new PropertyValueFactory("id"));
            updateColumn.setCellFactory(new Callback<TableColumn<DvdDataModel,String>,TableCell<DvdDataModel,String>>(){
                @Override
                public TableCell<DvdDataModel, String> call(TableColumn<DvdDataModel, String> param) {
                    TableCell<DvdDataModel, String> cell = new TableCell<DvdDataModel, String>(){
                        @Override
                        public void updateItem(String item, boolean empty) {
                            if(item!=null){
                                HBox box= new HBox();
                                box.setSpacing(10) ;
                                VBox vbox = new VBox();
                                Button b = new Button("UPDATE");
                                b.setDefaultButton(true);
                                vbox.getChildren().add(b);
                                box.getChildren().addAll(vbox);
                                //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                                setGraphic(box);

                                b.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        try {
                                            Stage mainStage = (Stage) mainVBox.getScene().getWindow();
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/updateDetails.fxml"));
                                            Parent root = loader.load();
                                            Stage stage = new Stage();
                                            SelectCustomerController secondWindowController = loader.getController();
                                            secondWindowController.setDvdObject(dvdObject);
                                            // stage.initOwner(mainStage);
                                            secondWindowController.setParentStage(stage);
                                            stage.initModality(Modality.WINDOW_MODAL);
                                            stage.setScene(new Scene(root,585, 419));
                                            stage.show();
                                            mainStage.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            Parent root = FXMLLoader.load(getClass().getResource("fxml/updateDetails.fxml"));
                                            Stage primaryStage = new Stage();
                                            primaryStage.setScene(new Scene(root, 585, 419));
                                            primaryStage.show();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    };
                    return cell;
                }

            });
            deleteColumn.setCellFactory(new Callback<TableColumn<DvdDataModel,String>,TableCell<DvdDataModel,String>>(){
                @Override
                public TableCell<DvdDataModel, String> call(TableColumn<DvdDataModel, String> param) {
                    TableCell<DvdDataModel, String> cell = new TableCell<DvdDataModel, String>(){
                        @Override
                        public void  updateItem(String item, boolean empty) {
                            if(item!=null){
                                System.out.println(item);
                                HBox box= new HBox();
                                box.setSpacing(10) ;
                                VBox vbox = new VBox();
                                Button b = new Button("DELETE");
                                b.setCancelButton(true);
                                vbox.getChildren().add(b);
                                box.getChildren().addAll(vbox);
                                //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                                b.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        int option = JOptionPane.showConfirmDialog(null," Are you sure you want to Delete the Customer ?");

                                        if(option == 0) {
                                            String SQL1 = "DELETE FROM `customers`  WHERE `customer_id` = ? ";
                                            try {

                                                con = new DbConnect().getDbConnect();
                                                con.setAutoCommit(false); //
                                                PreparedStatement preparedStatement1 = new DbConnect().getDbConnect().prepareStatement(SQL1);
                                                preparedStatement1.setString(1, item);
                                                preparedStatement1.executeUpdate();
                                                con.commit(); //Commit the Changes

                                                JOptionPane.showMessageDialog(null, "Successfully DELETED Customer !", "Rent A DvD", JOptionPane.PLAIN_MESSAGE);


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
                                        }
                                    }
                                });
                                setGraphic(box);
                            }
                        }
                    };
                    return cell;
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

        mainPageBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root = null;
                Stage prevStage = (Stage) mainVBox.getScene().getWindow();
                try {
                    root = FXMLLoader.load(getClass().getResource("fxml/mainPage.fxml"));
                    Stage mainStage = Main.getMainStage();
                    mainStage.setTitle("Rent A DvD");
                    mainStage.setScene(new Scene(root, 549, 429));
                    mainStage.show();
                    prevStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    @FXML void insertDetails(ActionEvent event) {

        String SQL1 = "INSERT INTO `customers`( `firstname`, `lastname`, `phone`, `address`) VALUES(?,?,?,?)";

        try {

            con = new DbConnect().getDbConnect();
            con.setAutoCommit(false); //
            PreparedStatement preparedStatement1 = new DbConnect().getDbConnect().prepareStatement(SQL1);
            preparedStatement1.setString(1, firstName.getText());
            preparedStatement1.setString(2, lastName.getText());
            preparedStatement1.setString(3, phoneTF.getText());
            preparedStatement1.setString(4, addressTA.getText());
            preparedStatement1.executeUpdate();
            con.commit(); //Commit the Changes

            JOptionPane.showMessageDialog(null, "Successfully Inserted Customer !","Rent A DvD",JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            try {
                con.rollback(); //Rollback
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }finally {
            try {
                con.setAutoCommit(true); //Set Autocomit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
