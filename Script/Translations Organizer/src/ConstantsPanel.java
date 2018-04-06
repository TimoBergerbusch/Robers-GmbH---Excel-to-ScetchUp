import org.ini4j.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.plaf.metal.*;
import javax.swing.table.*;

/**
 * The panel where the constants of the excel columns are listed and editable
 */
class ConstantsPanel extends JPanel {

    /**
     * the JTable, which lists the constants
     */
    private final JTable table;
    /**
     * the corresponding model to the {@link #table}
     */
    private final DefaultTableModel model;

    /**
     * the ini-file containing all the constants
     */
    private Ini ini;

    /**
     * the constructor to create the {@link ConstantsPanel} containing the {@link #table}
     */
    public ConstantsPanel() {

        this.setLayout(new BorderLayout());
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return colum > 0;
            }
        };

        model.setColumnIdentifiers(new String[]{"Konstante", "Spalte in der Excel"});
        table = new JTable(model);
        table.setRowHeight(50);
        model.addTableModelListener(e -> {
            if (e.getFirstRow() != e.getLastRow() || e.getColumn() != 1)
                return;

            int row = e.getFirstRow();
            int column = e.getColumn();

            try {
                Integer.parseInt(table.getValueAt(row, column).toString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Der Wert ist keine Zahl. Wird auf den Standart zurück gesetzt.", "Error: Keine Zahl", JOptionPane.ERROR_MESSAGE);
                model.setValueAt(ini.get(Constants.excelConstantSectionName, table.getValueAt(row, 0)), row, 1);
            }

        });

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        JButton speichern = new JButton("Speichern");
        speichern.setIcon(MetalIconFactory.getTreeFloppyDriveIcon());
        speichern.addActionListener(new SpeichernActionListener());
        bottomPanel.add(speichern, BorderLayout.CENTER);

        JButton refresh = new JButton();
        refresh.setIcon(MetalIconFactory.getTreeHardDriveIcon());
        refresh.addActionListener(e -> loadConstants());
        bottomPanel.add(refresh, BorderLayout.EAST);

        this.add(bottomPanel, BorderLayout.SOUTH);
        this.loadConstants();
    }

    /**
     * loads all the constants of the constants.ini
     */
    private void loadConstants() {
        this.clear();
//        File file = new File(Constants.excelConstantsPath);
        File file = Constants.constantsFile;
        if (!file.exists()) {
//            JOptionPane.showMessageDialog(null, "There exists no 'constants.ini' within " + Constants.excelConstantsPath, "Error: constants.ini", JOptionPane.ERROR_MESSAGE);
            System.out.println("Die Constants-ini kann nicht geöffnet werden");
            return;
        }
        try {
            ini = new Ini(file);

            for (String key : Constants.excelConstants) {
                model.addRow(new Object[]{key, ini.get(Constants.excelConstantSectionName, key)});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * removes all rows of the {@link #table}
     */
    private void clear() {
        while (table.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    public void reload() {
        this.clear();
        this.loadConstants();
    }

    /**
     * the {@link ActionListener} corresponding to the save-Button.
     * It (re-)writes the constant values into the constants.ini
     */
    private class SpeichernActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("save");
            for (int row = 0; row < table.getRowCount(); row++) {
                String key = table.getValueAt(row, 0).toString();
                String value = table.getValueAt(row, 1).toString();

                ini.put(Constants.excelConstantSectionName, key, value);
            }
            try {
                ini.store();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "Erfolgreich gespeichert.", "Speichern erfolgreich", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
