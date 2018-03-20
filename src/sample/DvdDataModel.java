package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Chris on 3/16/2018.
 */
public class DvdDataModel {

    private StringProperty id;
    private StringProperty name;
    private StringProperty genre;
    private StringProperty price;
    private StringProperty description;
    private StringProperty time;

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    private StringProperty status;
    private ObjectProperty albumArt;

    public DvdDataModel(String id, String name, String genre, String price, String description, String status, Album album, String time) {
        this.id =  new SimpleStringProperty(id);
        this.name =  new SimpleStringProperty(name);
        this.genre =  new SimpleStringProperty(genre);
        this.price =  new SimpleStringProperty(price);
        this.description =  new SimpleStringProperty(description);
        this.albumArt =  new SimpleObjectProperty(album);
        this.time =  new SimpleStringProperty(time);
        this.status =  new SimpleStringProperty(status);
    }


    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public Object getAlbumArt() {
        return albumArt.get();
    }

    public ObjectProperty albumArtProperty() {
        return albumArt;
    }

    public void setAlbumArt(Object albumArt) {
        this.albumArt.set(albumArt);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }

    public void setPrice(String price) {
        this.price.set(price);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }


    public String getTime() {
        return time.get();
    }

    public StringProperty timeProperty() {
        return time;
    }

    public void setTime(String time) {
        this.time.set(time);
    }
}
