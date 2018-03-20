/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

/**
 *
 * @author Narayan
 */
public class Album {
    
    
    //Attributes
    private String filename;
    private String artist;
    private String album;

    public Album(String filename, String artist, String album) {
        this.filename = filename;
        this.artist = artist;
        this.album = album;
    }
    
    //Accessor Methods
    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
    
    
    
}
