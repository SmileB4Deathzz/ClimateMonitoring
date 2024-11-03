package client;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.example.*;

/**
 * Utility class containing various static methods for common tasks such as closing frames,
 * displaying data in tables, retrieving selected areas, populating combo boxes, and checking data files.
 */
public class Utils {

    /**
     * Default constructor
     */
    public Utils(){

    }

    /**
     * Closes all frames created by the application.
     */
    public static void closeAllFrames(){
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            frame.dispose();
        }
    }

    /**
     * Displays a list of areas in a table format within the specified panel.
     *
     * @param al          The AreaList containing the areas to display.
     * @param tablePanel  The panel where the table will be displayed.
     * @return A reference to the created JTable.
     */
    public static JTable displayAreasToTable(AreaList al, JPanel tablePanel){
        String[][] data = al.toStringArray();
        String[] col = {"NAME", "COUNTRY", "LATITUDE", "LONGITUDE"};
        JTable table = new JTable(data, col){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane);
        tablePanel.revalidate();
        return table;
    }

    /**
     * Retrieves the selected area from the provided JTable.
     *
     * @param table The JTable containing the selected area.
     * @return The selected Area object.
     */
    public static Area getSelectedArea(JTable table){
        int selectedRow = table.getSelectedRow();
        String denominazione = table.getValueAt(selectedRow, 0).toString();
        String stato = table.getValueAt(selectedRow, 1).toString();
        String latitudine = table.getValueAt(selectedRow, 2).toString();
        String longitudine = table.getValueAt(selectedRow, 3).toString();
        return new Area(denominazione, stato, Double.parseDouble(latitudine), Double.parseDouble(longitudine));
    }

    /**
     * Populates a JComboBox with the provided values.
     *
     * @param box    The JComboBox to populate.
     * @param values An array of values to populate the combo box with.
     */
    public static void populateComboBox(JComboBox<String> box, String[] values){
        for (String value : values) {
            box.addItem(value);
        }
    }

    /**
     * Checks if data files exist and creates them if necessary.
     * Data files include "CentriMonitoraggio.dati", "CoordinateMonitoraggio.dati",
     * "OperatoriRegistrati.dati", and "ParametriClimatici.dati".
     */
    public static void checkDataFiles(){
        String[] files = {"CentriMonitoraggio", "CoordinateMonitoraggio", "OperatoriRegistrati", "ParametriClimatici"};
        try {
            Files.createDirectories(Paths.get("../data/"));
            for (int i = 0; i < 4; i++) {
                File f = new File("../data/" + files[i] + ".dati");
                if (!f.exists()) {
                    if (!f.createNewFile())
                        new Dialog(Dialog.type.ERR, "failed to create file " + f.getName());
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Tries to parse a string containing a date in the format dd-MM-yyyy.
     *
     * @param str The string containing the date to be parsed.
     * @return The parsed Date object if successful, {@code null} otherwise.
     */
    public static Date parseDate(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        Date parsedDate;
        try {
            parsedDate = sdf.parse(str);
        } catch (ParseException e){
            new Dialog(Dialog.type.ERR, "Failed to parse date");
            return null;
        }
        return parsedDate;
    }

    /**
     * Checks whether the provided string contains any digits.
     *
     * @param s The provided string.
     * @return True if the provided string has any digits, false otherwise.
     */
    public static boolean hasDigit(String s){
        char[] chars = s.toCharArray();
        for(char c : chars) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves an ArrayList of parameters associated with this area.
     *
     * @return An ArrayList of parameters associated with the provided area.
     */
    public static ArrayList<Parameter> getAreaParameters(Area a){
        try {
            return ConnectionManager.getCmServer().getAreaParameters(a);
        } catch (RemoteException e) {
            new Dialog(Dialog.type.ERR, "Failed to get area parameters");
            throw new RuntimeException(e);
        }
    }

}
