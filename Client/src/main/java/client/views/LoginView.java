package client.views;

import client.Dialog;
import client.Operator;
import client.OperatorManager;
import client.Utils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

/**
 * Graphical user interface for user login. Allows users to enter their user ID and password for authentication.
 * The view includes event listeners for interacting with the login components and validating credentials.
 */
public class LoginView {
    private JTextField userId;
    private JTextField password;
    private JButton loginButton;
    private JPanel LoginView;
    private JFrame frame;

    /**
     * Constructs the LoginView and initializes the user interface.
     */
    public LoginView(){createView();}

    /**
     * Creates and configures the graphical user interface components for user login.
     */
    private void createView(){
        frame = new JFrame("Climate Monitoring");
        frame.setIconImage(new ImageIcon((getClass().getResource("/Images/icon.png"))).getImage());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setSize(260, 150);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(LoginView);
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                clearInput();
            }
        });


        //Events
        userId.addActionListener(e -> password.grabFocus());

        password.addActionListener(e -> loginButton.doClick());

        loginButton.addActionListener(e -> Login(userId.getText(), password.getText()));

    }

    private void clearInput(){
        userId.setText("");
        password.setText("");
    }

    public JFrame getFrame(){
        return this.frame;
    }

    /**
     * Validates the provided login credentials.
     *
     * @param uid User ID to be validated.
     * @param pwd Password to be validated.
     */
    private void Login(String uid, String pwd){
        HashMap<String, Operator> operators = new OperatorManager().getOperators();
        Operator op = operators.get(uid);
        if (op != null){
            if (pwd.equals(op.getPassword())) {
                Utils.closeAllFrames();

                if (op.getCentroMonitoraggio() == null){
                    new Dialog(Dialog.type.INFO, "Please create a monitoring center first");
                    new CreateCMView(op);
                    return;
                }
                new OperatoreView(op);
            }
            else new Dialog(Dialog.type.ERR, "Wrong username or password");
        }
        else new Dialog(Dialog.type.ERR, "Wrong username or password");
    }
}
