import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Timo Bergerbusch on 25.02.2018.
 */
public class ConstantsPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JButton speichern, refresh;

    private Ini ini;

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
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
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

            }

        });

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        speichern = new JButton("Speichern");
        speichern.setIcon(MetalIconFactory.getTreeFloppyDriveIcon());
        speichern.addActionListener(new SpeichernActionListener());
        bottomPanel.add(speichern, BorderLayout.CENTER);

        refresh = new JButton();
        refresh.setIcon(MetalIconFactory.getTreeHardDriveIcon());
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadConstants();
            }
        });
        bottomPanel.add(refresh, BorderLayout.EAST);

        this.add(bottomPanel, BorderLayout.SOUTH);
        this.loadConstants();
    }

    public void loadConstants() {
        this.clear();
        File file = new File(Constants.excelConstantsPath);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "There exists no 'constants.ini' within " + Constants.excelConstantsPath, "Error: constants.ini", JOptionPane.ERROR_MESSAGE);
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

    private void clear() {
        while (table.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

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
