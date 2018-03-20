package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Callback;

import javax.swing.*;
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
public class RentDvdsController implements Initializable {

    //Variable Declarations
    @FXML private TableView dvdTable;
    @FXML private TableColumn artColumn;
    @FXML private TableColumn nameColumn;
    @FXML private TableColumn priceColumn;
    @FXML private TableColumn descrColumn;
    @FXML private TableColumn timeColumn;
    @FXML private TableColumn genreColumn;
    @FXML private TableColumn idColumn;
    @FXML private TableColumn statusColumn;


    @FXML private TextField searchField;
    @FXML private ComboBox<String> searchCombo;
    @FXML private ImageView imageView;
    @FXML private Button rentButton;
    @FXML private Button mainPageBtn;
    @FXML private VBox mainVBox;


    private static Connection con;
    private ObservableList<DvdDataModel> data;

    @FXML private Text dvdName;
    @FXML private Text genreT;
    @FXML private Text statusText;
    @FXML private Text descrText;
    @FXML private Text priceText;
    @FXML private ImageView dvdImage;

    @FXML private Pane dvdPane;

    private static  String id = "";
    private static  String rentStatus = "";
    private static DvdObject dvdObject;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        searchCombo.getItems().setAll("Drama","Action","Adult");
        searchCombo.getSelectionModel().selectFirst();

        try {

            con = new DbConnect().getDbConnect();
            data = FXCollections.observableArrayList();

            ResultSet rs1 = con.createStatement().executeQuery("SELECT *,@dvd_id := `dvd_id`,(SELECT COUNT(*) FROM `rent` WHERE `dvd_id` = @dvd_id) as status  FROM `dvd_table`");

            while (rs1.next()) {
                int status = rs1.getInt("status");
                if(status >= 1){
                    rentStatus = "Rented";
                }else{
                    rentStatus = "Available";
                }
                Album album = new Album(rs1.getString("image_path"),rs1.getString("dvd_name"),rs1.getString("genre"));
                data.add(new DvdDataModel(rs1.getString("dvd_id"),rs1.getString("dvd_name"),rs1.getString("genre"),rs1.getString("rental_price"), rs1.getString("dvd_description"),rentStatus, album,rs1.getString("time_created")));
            }

            idColumn.setCellValueFactory(new PropertyValueFactory("id"));
            nameColumn.setCellValueFactory(new PropertyValueFactory("name"));
            statusColumn.setCellValueFactory(new PropertyValueFactory("status"));
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
                                imageview.setImage(new Image(RentDvdsController.class.getResource("img").toString()+"/"+item.getFilename()));
                                box.getChildren().addAll(imageview,vbox);
                                //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                                setGraphic(box);
                            }
                        }
                    };
                    return cell;
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



            dvdTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                    int pos = dvdTable.getSelectionModel().getSelectedIndex();

                        if (pos >= 0) {
                            id = idColumn.getCellData(pos).toString();
                            System.out.println(id);
                            String name = nameColumn.getCellData(pos).toString().toUpperCase();
                            String genre = genreColumn.getCellData(pos).toString().toUpperCase();
                            String description = descrColumn.getCellData(pos).toString().toUpperCase();
                            rentStatus = statusColumn.getCellData(pos).toString().toUpperCase();
                            String price = "$"+priceColumn.getCellData(pos).toString().toUpperCase();
                            dvdName.setText(name);
                            genreT.setText(genre);
                            descrText.setText(description);
                            priceText.setText(price);
                            statusText.setText(rentStatus);

                             dvdObject = new DvdObject(id,name,genre,price,description);

                            if(rentStatus.equals("RENTED")){
                                rentButton.setDisable(true);
                                statusText.setFill(Color.RED);
                            }else{
                                rentButton.setDisable(false);
                                statusText.setFill(Color.DARKGREEN);
                            }
                            Album album = (Album) artColumn.getCellData(pos);
                            dvdImage.setImage(new Image(RentDvdsController.class.getResource("img").toString()+"/"+album.getFilename()));
                            if(!dvdPane.isVisible()){
                                dvdPane.setVisible(true);
                            }
                        }
                    }

            });


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");

        }

        rentButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Stage mainStage = (Stage) mainVBox.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/SelectCustomer.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    SelectCustomerController secondWindowController = loader.getController();
                    secondWindowController.setDvdId(id);
                    secondWindowController.setDvdObject(dvdObject);
                   // stage.initOwner(mainStage);
                    secondWindowController.setParentStage(stage);
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.setScene(new Scene(root,950, 561));
                    stage.show();
                    mainStage.close();
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            try {
                               Parent root = FXMLLoader.load(getClass().getResource("fxml/RentDvd.fxml"));
                                mainStage.setScene(new Scene(root));
                                Screen screen = Screen.getPrimary();
                                Rectangle2D bounds = screen.getVisualBounds();
                                mainStage.setX(bounds.getMinX());
                                mainStage.setY(bounds.getMinY());
                                mainStage.setWidth(bounds.getWidth());
                                mainStage.setHeight(bounds.getHeight());
                                //primaryStage.setFullScreen(true);
                                new Main().close();
                                mainStage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

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

}
