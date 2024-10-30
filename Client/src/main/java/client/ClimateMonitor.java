package client;

import client.views.MainView;
import com.formdev.flatlaf.FlatIntelliJLaf;

import javax.swing.*;

/**
 * Progetto laboratorio A: "Climate Monitoring", anno 2022-2023
 * @author Vartic Cristian, Matricola 748689
 * @version 1.0
 */
public class ClimateMonitor {
    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatIntelliJLaf());
        Utils.checkDataFiles();

        new MainView();
    }
}