package client.views;

import client.*;
import client.Dialog;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import org.example.*;

/**
 * Graphical user interface for the operator's view. Displays information about the monitoring center
 * associated with the operator, provides a table of areas under that monitoring center, and allows
 * the operator to insert parameters for selected areas.
 */
public class OperatoreView {
    private final Operator operator;
    private final MonitoringCenter mc;
    private JFrame frame;
    private JButton logoutButton;
    private JPanel OperatoreView;
    private JPanel tablePanel;
    private JLabel CMName;
    private JTable table;
    private JPanel paramPanel;
    private JButton insertParameterButton;
    private JTextField searchField;
    private JButton searchButton;
    private JButton addAreasButton;

    /**
     * Constructs the OperatoreView with the associated operator and monitoring center, then initializes the user interface.
     *
     * @param op The operator whose view is being displayed.
     */
    public OperatoreView(Operator op) {
        this.operator = op;
        this.mc = new MCManager().getMonitoringCenter(op.getCentroMonitoraggio().getNome());
        createView();
    }

    /**
     * Creates and configures the graphical user interface components for the operator's view.
     */
    private void createView() {
        frame = new JFrame("Climate Monitoring");
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon.png"))).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(OperatoreView);

        table = Utils.displayAreasToTable(new AreaList(mc.getAreas()), tablePanel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CMName.setText(mc.getNome());

        //Events
        logoutButton.addActionListener(e -> {
            Utils.closeAllFrames();
            new MainView();
        });

        insertParameterButton.addActionListener(e ->{
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                Area selectedArea = Utils.getSelectedArea(table);
                new InsertParameterView(selectedArea, mc);
            }
            else new client.Dialog(Dialog.type.ERR, "Select an area first");
        });

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            tablePanel.removeAll();
            AreaList hits = new AreaList(new AreaList(mc.getAreas()).search(searchText));
            table = Utils.displayAreasToTable(hits, tablePanel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            addTableListener(table);
        });

        addAreasButton.addActionListener(e -> {
            frame.dispose();
            new AddAreasView(operator);
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            Area selectedArea = Utils.getSelectedArea(table);
            displayParameters(selectedArea);
        });

        frame.setVisible(true);
    }

    /**
     * Displays all the parameters of the specified area. If the area has no parameters, a message stating
     * this will be displayed on the user interface.
     *
     * @param area The area whose parameters are to be displayed.
     */
    private void displayParameters(Area area){
        ArrayList<Parameter> params = Utils.getAreaParameters(area);
        if (params.size() ==0) {
            paramPanel.removeAll();
            JLabel noParamLabel = new JLabel("No parameters for this area");
            noParamLabel.setHorizontalAlignment(JLabel.CENTER);
            paramPanel.add(noParamLabel, BorderLayout.CENTER);
            paramPanel.revalidate();
            return;
        }
        paramPanel.removeAll();

        String[][] data = new ParameterManager().toStringArray(params);
        String[] col = {"CATEGORY", "SCORE", "DATE", "NOTES"};
        JTable table = new JTable(data, col);
        table.setCellSelectionEnabled(false);
        TableColumn column;
        for (int i = 0; i < 4; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 3) {
                column.setPreferredWidth(300);  //set notes column bigger
            } else {
                column.setPreferredWidth(20);
            }
        }

        JScrollPane scrollPane = new JScrollPane(table);
        paramPanel.add(scrollPane);
        paramPanel.revalidate();
    }

    /**
     * Adds a list selection listener to a table to trigger actions when a row is selected.
     *
     * @param table The JTable to which the listener should be added.
     */
    private void addTableListener(JTable table){
        table.getSelectionModel().addListSelectionListener(e -> {
            Area selectedArea = Utils.getSelectedArea(table);
            displayParameters(selectedArea);
        });
    }
}