package client.views;

import client.*;
import client.Dialog;
import client.date_picker.DateLabelFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.Properties;
import org.example.*;

/**
 * Graphical user interface for inserting a new parameter observation. Allows users to select the category
 * and score of the parameter, provide a date, add notes, and associate the parameter with a specific area
 * and monitoring center. The view includes event listeners for interacting with the UI components.
 */
public class InsertParameterView {
    private JComboBox<String> categoryBox;
    private JComboBox<String> scoreBox;
    private JPanel paramView;
    private JButton addParameterButton;
    private JTextArea notesField;
    private JPanel datePickerPanel;
    private final JFrame frame;

    /**
     * Constructs the InsertParameterView and sets up event listeners for UI interactions.
     *
     * @param area The area associated with the parameter observation.
     * @param mc   The monitoring center associated with the parameter observation.
     */
    public InsertParameterView(Area area, MonitoringCenter mc){

        frame = new JFrame("Climate Monitoring");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(550, 200);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(paramView);
        frame.setIconImage(new ImageIcon("Images/icon.png").getImage());

        //date picker
        UtilDateModel model = new UtilDateModel();
        /*
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");*/
        JDatePanelImpl datePanel = new JDatePanelImpl(model, new Properties());
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        datePicker.setPreferredSize(new Dimension(120,25));
        datePickerPanel.add(datePicker);

        Utils.populateComboBox(categoryBox, Parameter.getCategoryNames());
        Utils.populateComboBox(scoreBox, new String[]{"1", "2", "3", "4", "5"});

        //Events
        addParameterButton.addActionListener(e -> {
            Parameter.Category category = Parameter.Category.valueOf(categoryBox.getSelectedItem().toString());
            int score = Integer.parseInt((String) scoreBox.getSelectedItem());
            Date date = (Date) datePicker.getModel().getValue();
            String notes = notesField.getText();
            if(date != null){
                Parameter param = new Parameter(category, score, notes, date, area, mc);
                new ParameterManager().addParameter(param);
                new client.Dialog(Dialog.type.INFO, "parameter added");
                frame.dispose();
            }
        });

        frame.revalidate();

        frame.setVisible(true);
    }

    public JFrame getFrame(){
        return frame;
    }
}
