package sample;

/**
 * Created by Chris on 3/19/2018.
 */
public class DvdObject {

    private String id;
    private String name;
    private String genre;
    private String price;
    private String description;
    private String time;

    public DvdObject(String id, String name, String genre, String price, String description) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.price = price;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
