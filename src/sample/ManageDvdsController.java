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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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
public class ManageDvdsController implements Initializable {

    //Variable Declarations
    @FXML private TableView dvdTable;
    @FXML private TableColumn artColumn;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn priceColumn;
    @FXML private TableColumn descrColumn;
    @FXML private TableColumn timeColumn;
    @FXML private TableColumn genreColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchCombo;
    @FXML private ComboBox<String> genreCombo;
    @FXML private ImageView imageView;
    @FXML private Button addButton;
    @FXML private Button chooseFileButton;

    @FXML private TextField priceTF;
    @FXML private TextField dvdName;
    @FXML private TextArea descriptionTA;

    @FXML private Button mainPageBtn;
    @FXML private VBox mainVBox;

    private static Connection con;
    private ObservableList<DvdDataModel> data;
    private final FileChooser fileChooser = new FileChooser();
    private Desktop desktop = Desktop.getDesktop();
    private static String imageName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        searchCombo.getItems().setAll("Drama","Action","Adult");
        searchCombo.getSelectionModel().selectFirst();
        genreCombo.getItems().setAll("Drama","Action","Adult");
        genreCombo.getSelectionModel().selectFirst();

        try {

            con = new DbConnect().getDbConnect();
            data = FXCollections.observableArrayList();

            ResultSet rs1 = con.createStatement().executeQuery("SELECT * FROM `dvd_table`;");

            while (rs1.next()) {
                Album album = new Album(rs1.getString("image_path"),rs1.getString("dvd_name"),rs1.getString("genre"));
                data.add(new DvdDataModel(rs1.getString("dvd_id"),rs1.getString("dvd_name"),rs1.getString("genre"),rs1.getString("rental_price"), rs1.getString("dvd_description"), "", album,rs1.getString("time_created")));
            }

            nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
            genreColumn.setCellValueFactory(new PropertyValueFactory("genre"));
            descrColumn.setCellValueFactory(new PropertyValueFactory("description"));
            priceColumn.setCellValueFactory(new PropertyValueFactory("price"));
            timeColumn.setCellValueFactory(new PropertyValueFactory("time"));
            artColumn.setCellValueFactory(new PropertyValueFactory("albumArt"));
            artColumn.setCellFactory(new Callback<TableColumn<DvdDataModel,Album>,TableCell<DvdDataModel,Album>>(){
                @Override
                public TableCell<DvdDataModel, Album> call(TableColumn<DvdDataModel, Album> param) {
                    TableCell<DvdDataModel, Album> cell = new TableCell<DvdDataModel, Album>(){
                        @Override
                        public void updateItem(Album item, boolean empty) {
                            if(item!=null){
                                HBox box= new HBox();
                                box.setSpacing(10) ;
                                VBox vbox = new VBox();
                                //vbox.getChildren().add(new Label(item.getAlbum()));
                                //vbox.getChildren().add(new Label(item.getAlbum()));
                                ImageView imageview = new ImageView();
                                imageview.setFitHeight(70);
                                imageview.setFitWidth(70);
                                imageview.setImage(new Image(ManageDvdsController.class.getResource("img").toString()+"/"+item.getFilename()));
                                box.getChildren().addAll(imageview,vbox);
                                //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                                setGraphic(box);
                            }
                        }
                    };
                    return cell;
                }

            });

            chooseFileButton.setOnAction(
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(final ActionEvent e) {
                            File file = fileChooser.showOpenDialog(Main.getMainStage());
                            fileChooser.setInitialDirectory(new File("C:\\Users\\Chris\\IdeaProjects\\RentDvD\\src\\sample\\img"));
                            if (file != null) {

                                imageName = file.getName();

                                String path = System.getProperty("user.dir")+"/src/sample/img/"+imageName;
                                try {
                                    File dest = new File(path);
                                    System.out.println(dest.getPath());
                                    Files.copy(file.toPath(), dest.toPath());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                imageView.setImage(new Image("file:"+file.getAbsolutePath()));
                            }
                        }
                    });



            // 1. Wrap the ObservableList in a FilteredList (initially display all data).
            FilteredList<DvdDataModel> filteredData = new FilteredList<>(data, p -> true);

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
                        case "BY NAME":
                            if (dvdData.nameProperty().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                return true; // Filter matches first name.
                            }
                            break;
                        case "BY Description":
                            if (dvdData.descriptionProperty().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                return true; // Filter matches first name.
                            }
                            break;
                        case "BY ID":
                            if (dvdData.idProperty().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                return true; // Filter matches first name.
                            }
                            break;
                        default:
                            if (dvdData.nameProperty().toString().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                return true; // Filter matches first name.
                            }
                            break;

                    }

                    return false; // Does not match.
                });
            });


            // 3. Wrap the FilteredList in a SortedList.
            SortedList<DvdDataModel> sortedData = new SortedList<>(filteredData);

            // 4. Bind the SortedList comparator to the TableView comparator.
            // 	  Otherwise, sorting the TableView would have no effect.
            sortedData.comparatorProperty().bind(dvdTable.comparatorProperty());

            // 5. Add sorted (and filtered) data to the table.
            dvdTable.setItems(sortedData);



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
                    Stage mainStage = new Stage();
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

        String SQL1 = "INSERT INTO `dvd_table`( `dvd_name`, `genre`, `dvd_description`, `image_path`, `rental_price`) VALUES(?,?,?,?,?)";

        try {

            con = new DbConnect().getDbConnect();
            con.setAutoCommit(false); //
            PreparedStatement preparedStatement1 = new DbConnect().getDbConnect().prepareStatement(SQL1);
            preparedStatement1.setString(1, dvdName.getText());
            preparedStatement1.setString(2, genreCombo.getSelectionModel().getSelectedItem().toString());
            preparedStatement1.setString(3, descriptionTA.getText());
            preparedStatement1.setString(4, imageName);
            preparedStatement1.setString(5, priceTF.getText());
            preparedStatement1.executeUpdate();
            con.commit(); //Commit the Changes

            JOptionPane.showMessageDialog(null, "Successfully Inserted DvD !","Rent A DvD",JOptionPane.PLAIN_MESSAGE);

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
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
