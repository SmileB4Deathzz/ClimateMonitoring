package client;

import java.io.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.example.*;

/**
 * Manager class for handling Parameter instances. Contains methods for serializing and deserializing
 * the list of parameters, adding new parameters, and other helper methods.
 */
public class ParameterManager {

    /** The file path to save and retrieve parameter data. */
    private final String filePath = "../data/ParametriClimatici.dati";

    /**
     * Default constructor for the ParameterManager class.
     */
    public ParameterManager(){}

    /**
     * Deserializes the list of parameters from the designated file.
     *
     * @return An ArrayList containing parameter instances.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Parameter> getParameters(){
        ArrayList<Parameter> params = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            params =  (ArrayList<Parameter>)ois.readObject();

        } catch (IOException ioe) {
            if (ioe instanceof EOFException)
                return params;
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return params;
    }

    /**
     * Serializes the provided list of parameters to a file.
     *
     * @param params The ArrayList of parameters to be serialized.
     */
    private void saveParameters(ArrayList<Parameter> params){
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(params);

        } catch (FileNotFoundException e) {
            new Dialog(Dialog.type.ERR, "Failed to serialize data");
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Adds the provided parameter to the parameter database and saves it to the file.
     *
     * @param param The parameter to be added to the database.
     */
    public void addParameter(Parameter param){
        ServerResponse sResp = null;
        try {
            sResp = ConnectionManager.getCmServer().insertParameter(param);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        new Dialog(Dialog.type.valueOf(sResp.type.toString()), sResp.message);
    }

    /**
     * Extracts aggregated parameter information from a list of parameters.
     *
     * @param params The list of parameters from which to extract aggregated information.
     * @return A 2D string array containing aggregated parameter information.
     */
    public String[][] getAggregatedParams(ArrayList<Parameter> params){
        Parameter.Category[] categories = Parameter.Category.values();
        ArrayList<String[]> result = new ArrayList<>();
        ArrayList<Parameter> paramsCopy = new ArrayList<>(params);
        for (Parameter.Category category : categories) {
            int totalScore = 0;
            int count = 0;
            for (int j = 0; j < paramsCopy.size(); j++) {
                Parameter param = paramsCopy.get(j);
                if (param.getCategory() == category) {
                    count++;
                    totalScore += param.getScore();
                    paramsCopy.remove(j);   //No need to check this parameter again for other categories
                    j--;
                }
            }
            if (count != 0) {
                String[] row = {category.toString(), Integer.toString(count), Integer.toString(totalScore / count)};
                result.add(row);
            }
        }
        return result.toArray(new String[result.size()][]);
    }

    /**
     * Extracts the notes from a list of parameters and returns them in a 2D string array
     * in the format {"DATE", "NOTES"};
     *
     * @param params The list of parameters from which to extract notes.
     * @return A 2D string array containing notes left by operators, or null if no notes exist.
     */
    public String[][] getNotes(ArrayList<Parameter> params){
        ArrayList<String[]> result = new ArrayList<>();
        for (Parameter param : params) {
            if (!param.getNotes().isEmpty()) {
                String date = new SimpleDateFormat("dd-MM-yyyy").format(param.getDate());
                String notes = param.getNotes();
                result.add(new String[]{date, notes});
            }
        }
        return result.size() > 0 ? result.toArray(new String[result.size()][]) : null;
    }

    /**
     * Converts an ArrayList of parameters into a 2D string array representation.
     *
     * @param params The ArrayList of parameters to be converted.
     * @return A 2D string array containing the parameter attributes.
     */
    public String[][] toStringArray(ArrayList<Parameter> params){
        String[][] result = new String[params.size()][];

        for (int i = 0; i < params.size(); i++) {
            result[i] = params.get(i).toStringArray();
        }
        return result;
    }
}
