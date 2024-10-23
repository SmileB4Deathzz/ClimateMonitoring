package client;

import javax.swing.*;
import java.util.Date;
import java.util.Objects;

/**
 * Graphical user interface for inserting a new parameter observation. Allows users to select the category
 * and score of the parameter, provide a date, add notes, and associate the parameter with a specific area
 * and monitoring center. The view includes event listeners for interacting with the UI components.
 */
public class InsertParameterView {
    private JComboBox<String> categoryBox;
    private JComboBox<String> scoreBox;
    private JPanel paramView;
    private JTextField dateField;
    private JButton addParameterButton;
    private JTextArea notesField;

    /**
     * Constructs the InsertParameterView and sets up event listeners for UI interactions.
     *
     * @param area The area associated with the parameter observation.
     * @param mc   The monitoring center associated with the parameter observation.
     */
    public InsertParameterView(Area area, MonitoringCenter mc){

        JFrame frame = new JFrame("Climate Monitoring");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(550, 200);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(paramView);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon.png"))).getImage());

        Utils.populateComboBox(categoryBox, Parameter.getCategoryNames());
        Utils.populateComboBox(scoreBox, new String[]{"1", "2", "3", "4", "5"});

        //Events
        addParameterButton.addActionListener(e -> {
            Parameter.Category category = Parameter.Category.valueOf(categoryBox.getSelectedItem().toString());
            int score = Integer.parseInt((String) scoreBox.getSelectedItem());
            Date date = Utils.parseDate(dateField.getText());
            String notes = notesField.getText();
            if(date != null){
                Parameter param = new Parameter(category, score, notes, date, area, mc);
                new ParameterManager().addParameter(param);
                new Dialog(Dialog.type.INFO, "parameter added");
                frame.dispose();
            }
        });

        frame.revalidate();

        frame.setVisible(true);
    }
}
