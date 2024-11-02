package client;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import org.example.*;

/**
 * Graphical user interface for the main application view. Displays a table of areas and provides
 * functionality to search for areas, register new operators, and log in. It also allows displaying
 * aggregated parameters and notes for selected areas.
 */
public class MainView {
    private JTable table;
    private JPanel tablePanel;
    private JPanel MainView;
    private JTextField searchField;
    private JButton searchButton;
    private JButton registerButton;
    private JButton loginButton;
    private JPanel noParamsPanel;
    private JPanel paramsPanel;
    private JPanel notesPanel;

    /**
     * Constructs the MainView and initializes the user interface.
     */
    public MainView() {
        createView();
    }

    /**
     * Creates and configures the graphical user interface components for the main view.
     */
    public void createView() {
        AreaList areas = new AreaList();
        JFrame regFrame = new RegisterView().getFrame();
        JFrame logFrame = new LoginView().getFrame();


        JFrame frame = new JFrame("Climate Monitoring");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(MainView);
        frame.setIconImage(new ImageIcon((getClass().getResource("/Images/icon.png"))).getImage());

        table = Utils.displayAreasToTable(areas, tablePanel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setColumnSelectionAllowed(false);
        table.setShowGrid(true);
        addTableListener(table);

        //Events
        searchField.addActionListener(e -> searchButton.doClick());

        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            tablePanel.removeAll();
            AreaList hits = new AreaList(areas.search(searchText));
            table = Utils.displayAreasToTable(hits, tablePanel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            addTableListener(table);
        });

        registerButton.addActionListener(e -> regFrame.setVisible(true));

        loginButton.addActionListener(e -> logFrame.setVisible(true));

        frame.setVisible(true);
    }

    /**
     * Displays aggregated parameters for the specified area and populates the parameters and notes panels.
     *
     * @param area The area for which to display aggregated parameters.
     */
    private void displayParameters(Area area){
        ArrayList<Parameter> params = Utils.getAreaParameters(area);
        if (params.size() ==0) {
            clearPanels();
            JLabel noParamLabel = new JLabel("No parameters for this area");
            noParamLabel.setHorizontalAlignment(JLabel.CENTER);
            noParamLabel.setVerticalAlignment(JLabel.CENTER);
            noParamsPanel.add(noParamLabel);
            noParamsPanel.revalidate();
            return;
        }
        clearPanels();

        String[][] data = new ParameterManager().getAggregatedParams(params);
        String[] col = {"CATEGORY", "DETECTIONS", "AVERAGE SCORE"};
        JTable paramTable = new JTable(data, col);
        paramTable.setCellSelectionEnabled(false);
        paramTable.setPreferredSize(new Dimension(200, 200));
        paramTable.setPreferredScrollableViewportSize(paramTable.getPreferredSize());
        paramTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(paramTable);
        paramsPanel.add(scrollPane);
        paramsPanel.revalidate();

        displayNotes(area);
    }

    /**
     * Displays notes left by operators for the specified area and populates the notes panel.
     *
     * @param area The area for which to display notes.
     */
    private void displayNotes(Area area){
        ArrayList<Parameter> params = Utils.getAreaParameters(area);
        String[][] notes = new ParameterManager().getNotes(params);
        if (notes == null){
            JLabel noNotesLabel = new JLabel("No notes for this area");
            noNotesLabel.setVerticalAlignment(JLabel.CENTER);
            noNotesLabel.setHorizontalAlignment(JLabel.CENTER);
            notesPanel.add(noNotesLabel);
            notesPanel.revalidate();
            return;
        }

        String[] col = {"DATE", "NOTES"};
        JTable notesTable = new JTable(notes, col);
        notesTable.setCellSelectionEnabled(false);
        notesTable.setPreferredSize(new Dimension(200, 200));
        notesTable.setPreferredScrollableViewportSize(notesTable.getPreferredSize());
        notesTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(notesTable);
        notesPanel.add(scrollPane);
        notesPanel.revalidate();
    }

    /**
     * Adds a list selection listener to a JTable to trigger actions when a row is selected.
     *
     * @param table The JTable to which the listener should be added.
     */
    private void addTableListener(JTable table){
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Area selectedArea = Utils.getSelectedArea(table);
                displayParameters(selectedArea);
            }
        });
    }

    /**
     * Clears the content of the notesPanel, noParamsPanel, and paramsPanel.
     */
    private void clearPanels(){
        noParamsPanel.removeAll();
        paramsPanel.removeAll();
        notesPanel.removeAll();
    }
}