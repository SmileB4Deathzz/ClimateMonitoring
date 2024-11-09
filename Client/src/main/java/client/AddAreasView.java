package client;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Objects;
import org.example.*;

public class AddAreasView {
    private final ArrayList<Area> selectedAreas;
    private final JFrame frame;
    private JTable table;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel tablePanel;
    private JButton addAreaButton;
    private JPanel addAreasView;
    private JButton confirmButton;
    private JPanel SATablePanel;


    public AddAreasView(Operator operator) {
        MonitoringCenter mc = new MCManager().getMonitoringCenter(operator.getCentroMonitoraggio().getNome());
        selectedAreas = new ArrayList<>();
        AreaList areas = new AreaList();
        frame = new JFrame("Climate Monitoring");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 720);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(addAreasView);
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/icon.png"))).getImage());
        frame.setVisible(true);

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
            if (selectedRow != -1) {
                Area selectedArea = Utils.getSelectedArea(table);

                if (areaAlreadySelected(selectedArea)) {
                    new Dialog(Dialog.type.ERR, "Area already selected");
                    return;
                }
                if (mc.containsArea(selectedArea)){
                    new Dialog(Dialog.type.ERR, "Area already present in the Monitoring Center");
                    return;
                }

                SATablePanel.removeAll();
                selectedAreas.add(selectedArea);
                JTable sAT = Utils.displayAreasToTable(new AreaList(selectedAreas), SATablePanel);
                sAT.setFillsViewportHeight(true);
                sAT.setCellSelectionEnabled(false);
                confirmButton.setEnabled(true);
            }
        });

        confirmButton.addActionListener(e -> {
                new MCManager().addAreas(mc, selectedAreas);
                new Dialog(Dialog.type.INFO, "Areas added successfully");
                frame.dispose();
                new OperatoreView(operator);
        });
    }

    /**
     * Checks if an area has already been added to the selected areas list for the monitoring center.
     *
     * @param a The area to check for existence in the selected areas.
     * @return {@code true} if the area is already added, {@code false} otherwise.
     */
    private boolean areaAlreadySelected(Area a) {
        if (selectedAreas == null)
            return false;
        for (Area area : selectedAreas) {
            if (a.getLatitudine() == area.getLatitudine() && a.getLongitudine() == area.getLongitudine())
                return true;
        }
        return false;
    }

}
