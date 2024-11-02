package client;

import java.io.*;
import java.util.HashMap;
import org.example.*;



/**
 * Manager class for handling Operator instances. Contains methods for serializing and deserializing
 * the list of operators and provides other helper functions.
 */
public class OperatorManager {
    /** The file path to save and retrieve operator data. */
    private final String filePath = "../data/OperatoriRegistrati.dati";

    /**
     * Default constructor
     */
    public OperatorManager(){

    }

    /**
     * Serializes the provided list of operators to the designated file.
     *
     * @param operators The HashMap of operators to be serialized.
     */
    private void saveOperators(HashMap<String, Operator> operators){
            try (FileOutputStream fos = new FileOutputStream(filePath);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {

                oos.writeObject(operators);

            } catch (FileNotFoundException e) {
                new Dialog(Dialog.type.ERR, "Failed to serialize data");
                throw new RuntimeException(e);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                System.exit(1);
            }
    }

    /**
     * Deserializes the list of operators from the designated file.
     *
     * @return A HashMap containing pairs of "operatorId/operator".
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Operator> getOperators(){
        HashMap<String, Operator> operators = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            operators =  (HashMap<String, Operator>) ois.readObject();

        } catch (IOException ioe) {
            if (ioe instanceof EOFException)
                return operators;
            ioe.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found");
            c.printStackTrace();
        }
        return operators;
    }

    /**
     * Adds the provided operator to the operators datafile
     *
     * @param op The operator to be added to the datafile.
     * @return {@code true} if the operator was added successfully, {@code false} otherwise.
     */
    public boolean saveOperator(Operator op){
        try {
            HashMap<String, Operator> operators = getOperators();
            operators.put(op.getUserId(), op);
            saveOperators(operators);
            return true;
        }
        catch (Exception e){
            new Dialog(Dialog.type.ERR, "Failed to add new Operator to the database");
            e.printStackTrace();
        }
        return false;
    }
}
