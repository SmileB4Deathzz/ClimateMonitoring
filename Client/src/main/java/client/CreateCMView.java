package client;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Graphical user interface for creating a new monitoring center. Allows an operator to select areas
 * and associate them with a new monitoring center, providing the monitoring center's name and address.
 * The view also includes event listeners for interacting with the UI components.
 */
public class CreateCMView {
    private final ArrayList<Area> selectedAreas;
    private final JFrame frame;
    private JTable table;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel tablePanel;
    private JButton addAreaButton;
    private JPanel CreateCMView;
    private JButton createMonitoringCenterButton;
    private JPanel SATablePanel;

    /**
     * Constructs the CreateCMView and sets up event listeners for UI interactions.
     *
     * @param operator The operator who is creating the monitoring center.
     */
    public CreateCMView(Operator operator){
        selectedAreas = new ArrayList<>();
        AreaList areas = new AreaList();
        frame = new JFrame("Climate Monitoring");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 720);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(CreateCMView);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon.png"))).getImage());
        frame.setVisible(true);

        createMonitoringCenterButton.setEnabled(false);

        table = Utils.displayAreasToTable(areas, tablePanel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            tablePanel.removeAll();
            AreaList hits = new AreaList(areas.search(searchText));
            table = Utils.displayAreasToTable(hits, tablePanel);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        });

        addAreaButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1){
                Area selectedArea = Utils.getSelectedArea(table);

                if (areaAlreadyAdded(selectedArea)){
                    new Dialog(Dialog.type.ERR, "Area already in selected areas list");
                }
                else{
                    SATablePanel.removeAll();
                    selectedAreas.add(selectedArea);
                    JTable sAT = Utils.displayAreasToTable(new AreaList(selectedAreas), SATablePanel);
                    sAT.setFillsViewportHeight(true);
                    sAT.setCellSelectionEnabled(false);
                    createMonitoringCenterButton.setEnabled(true);
                }
            }
        });

        createMonitoringCenterButton.addActionListener(e -> {
            if (inputIsValid()){
                String name = nameField.getText();
                String address = addressField.getText();

                MonitoringCenter mc = new MonitoringCenter(name, address, selectedAreas);
                if (new MCManager().addMCenter(mc)) {
                    operator.setCentroMonitoraggio(mc);
                    new OperatorManager().saveOperator(operator);
                    new Dialog(Dialog.type.INFO, "Monitoring Center created successfully");
                    frame.dispose();
                    new OperatoreView(operator);
                }
            }
        });
    }

    /**
     * Checks if an area has already been added to the selected areas list for the monitoring center.
     *
     * @param a The area to check for existence in the selected areas.
     * @return {@code true} if the area is already added, {@code false} otherwise.
     */
    private boolean areaAlreadyAdded(Area a){
        if (selectedAreas == null)
            return false;
        for (Area area : selectedAreas) {
            if (a.getLatitudine() == area.getLatitudine() && a.getLongitudine() == area.getLongitudine())
                return true;
        }
        return false;
    }

    /**
     * Checks if the input in the name and address fields is valid (not empty).
     *
     * @return {@code true} if the input is valid, {@code false} otherwise.
     */
    private boolean inputIsValid(){
        if (nameField.getText().isEmpty()){
            new Dialog(Dialog.type.ERR, "Name can't be empty");
            return false;
        }
        else if (addressField.getText().isEmpty()){
            new Dialog(Dialog.type.ERR, "Address can't be empty");
            return false;
        }
        return true;
    }
}
