package client;

import javax.swing.*;

/**
 * A utility class for displaying dialog messages based on the provided type and message.
 * This class offers two types of dialogs: error and information.
 */
public class Dialog {
    /**
     * Enumeration of dialog types
     */
    public enum type{
        ERR, INFO
    }

    /**
     * Constructs a dialog instance based on the provided type and message.
     *
     * @param t       The type of dialog to display (error or information).
     * @param message The message content to be displayed in the dialog.
     */
    public Dialog(type t, String message) {
        switch(t){
            case ERR:
                JOptionPane.showMessageDialog(new JFrame(), message, "Error",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case INFO:
                JOptionPane.showMessageDialog(new JFrame(), message, "Info",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
        }
    }
}
