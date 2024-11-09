package client;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Objects;
import org.example.*;

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
        ServerResponse resp = null;
        try {
            resp = ConnectionManager.getCmServer().login(uid, pwd);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (resp.type == ServerResponse.Type.DATA){
            Operator op = (Operator) resp.data;
            if (op.getCentroMonitoraggio() == null){
                new Dialog(Dialog.type.INFO, "Please create a monitoring center first");
                new CreateCMView(op);
                frame.dispose();
                return;
            }
            new OperatoreView(op);
            frame.dispose();
        }
        else
            new Dialog(Dialog.type.valueOf(resp.type.toString()), resp.message);
    }
}
