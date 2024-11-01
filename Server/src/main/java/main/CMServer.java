package main;

import client.AreaList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class CMServer implements CMServerInterface {
    public static void main( String[] args ) throws SQLException {
        Connection conn = new DBManager().connect("dbCM", "postgres", "buzica");
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM operatori");
        while (rs.next()) {
            System.out.print("Column 1 returned ");
            System.out.println(rs.getString(1));
        }
        rs.close();
        st.close();
        System.out.println( "Hello World!" );
    }

    public AreaList getAreas(){

    }
}
