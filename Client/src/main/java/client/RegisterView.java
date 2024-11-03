package client;

import client.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;
import org.example.*;

/**
 * Graphical user interface for user registration. Allows users to fill out a registration form with personal information,
 * including name, last name, tax code, email, user ID, and password, and register as operators.
 */
public class RegisterView {
    private JFrame frame;
    private JTextField nomeField;
    private JTextField cFField;
    private JTextField mailField;
    private JTextField uIDField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JPanel RegisterView;
    private JTextField cognomeField;
    private JTextField mCField;

    /**
     * Constructs the RegisterView and initializes the graphical user interface.
     */
    public RegisterView(){
        createView();
    }

    /**
     * Creates and configures the graphical user interface components for user registration.
     */
    public void createView(){
        //UI
        frame = new JFrame("Climate Monitoring");
        frame.setIconImage(new ImageIcon((getClass().getResource("/Images/icon.png"))).getImage());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(RegisterView);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                clearInput();
            }
        });

        registerButton.addActionListener(e -> {
            if (inputIsValid()) {
                MonitoringCenter mc = new MCManager().getMonitoringCenter(mCField.getText());
                Operator operator = new Operator(nomeField.getText(), cognomeField.getText(), cFField.getText(), mailField.getText(), uIDField.getText(), String.valueOf(passwordField.getPassword()), mc);
                OperatorManager om = new OperatorManager();
                if (om.saveOperator(operator)) {
                    new Dialog(Dialog.type.INFO, "You have successfully registered, please log in");
                    clearInput();
                    frame.setVisible(false);
                }
            }
        });

        registerButton.requestFocusInWindow();
    }

    private void clearInput(){
        nomeField.setText("");
        cognomeField.setText("");
        cFField.setText("");
        mailField.setText("");
        uIDField.setText("");
        passwordField.setText("");
        mCField.setText("");
    }

    public JFrame getFrame(){
        return this.frame;
    }

    /**
     * Checks if the registration form is filled correctly.
     *
     * @return True if all fields are valid, false otherwise.
     */
    public boolean inputIsValid(){
        if (Utils.hasDigit(nomeField.getText())){
            new Dialog(Dialog.type.ERR, "Name shouldn't include any digits");
            return false;
        }
        else if (nomeField.getText().isEmpty()){
            new Dialog(Dialog.type.ERR, "Name field shouldn't be empty");
            return false;
        }
        else if (Utils.hasDigit(cognomeField.getText())){
            new Dialog(Dialog.type.ERR, "Cognome shouldn't include any digits");
            return false;
        }
        else if (cognomeField.getText().isEmpty()){
            new Dialog(Dialog.type.ERR, "Cognome field shouldn't be empty");
            return false;
        }
        else if (cFField.getText().isEmpty()){
            new Dialog(Dialog.type.ERR, "Codice Fiscale field shouldn't be empty");
            return false;
        }
        else if (!Pattern.compile("^(.+)@(\\S+)$")
                .matcher(mailField.getText()).matches()){
            new Dialog(Dialog.type.ERR, "Email not valid");
            return false;
        }
        else if (mailField.getText().isEmpty()){
            new Dialog(Dialog.type.ERR, "Email field shouldn't be empty");
            return false;
        }
        else if (uIDField.getText().isEmpty()){
            new Dialog(Dialog.type.ERR, "User Id field shouldn't be empty");
            return false;
        }
        HashMap<String, Operator> operators = new OperatorManager().getOperators();
        if (operators.get(uIDField.getText()) != null){
            new Dialog(Dialog.type.ERR, "This User Id already exists");
            return false;
        }
        else if (String.valueOf(passwordField.getPassword()).isEmpty()){
            new Dialog(Dialog.type.ERR, "Password shouldn't be empty");
            return false;
        }
        else if (!mCField.getText().isEmpty()){
            MonitoringCenter mc = new MCManager().getMonitoringCenter(mCField.getText());
            if (mc == null){
                new Dialog(Dialog.type.ERR, "No monitoring center with that name exists");
                return false;
            }
        }
        return true;
    }
}
