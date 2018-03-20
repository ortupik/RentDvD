package sample;

/**
 * Created by CHRISX-K on 11/19/2015.
 */
import javax.swing.*;
import java.sql.*;

/* This Class serves to connect to Mysql*/

public class DbConnect {

    public static Connection getDbConnect() {

        String url = "jdbc:mysql://localhost:3306/";

        String user = "root";
        String password = "chowder";

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);

            conn.setAutoCommit(false);

            Statement stt=conn.createStatement();
            stt.executeUpdate("USE dvd_rental_db;");

            conn.commit(); //Commit the changes if everything is OK

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback(); //Roll back the changes
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null,e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e);
        } catch (InstantiationException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,e);

            }
        catch (IllegalAccessException e) {
        e.printStackTrace();
    }finally {
            try {
                conn.setAutoCommit(true); //Set Autocomit to true
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return conn;
    }
}