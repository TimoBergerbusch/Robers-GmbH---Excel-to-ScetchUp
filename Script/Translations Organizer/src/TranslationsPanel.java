import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class TranslationsPanel extends JPanel {

    private String[] columnNames = {"Index", "Name", "Key", "Kürzel", "Bauteil", "X-Achse", "Y-Achse", "Z-Achse"};
    private JTable table;
    private JComboBox<String> comboBox;
    DefaultTableModel model;
    public static ArrayList<Translation> translations;

    public TranslationsPanel() {
        this.setBorder(new LineBorder(Color.darkGray, 3, true));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 8;

        comboBox = new JComboBox<>(new String[]{"Länge", "Breite", "Höhe"});

        model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int colum) {
                return false;
            }
        };
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);
        table.setRowHeight(25);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2) {
                    View.translationEditPanel.loadTranslation(translations.get(row));
                }
                System.out.println(table.getSize());
            }
        });
        this.loadTranslations();
        this.add(new JScrollPane(table), gbc);
    }

    public void refresh() {
        while (table.getRowCount() > 0) {
            model.removeRow(0);
        }

        for (int i = 0; i < translations.size(); i++) {
            model.addRow(translations.get(i).getData());
        }
    }

    private void loadTranslations() {
        try {
            Ini ini = new Ini(new File(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins\\su_RobersExcelConvert\\classes\\translations.ini"));

            Set<String> keys = ini.keySet();

            translations = new ArrayList<>();
            int index = 0;
            for (String key : keys) {
                translations.add(this.loadUniqueTranslation(index, ini, key));
                index++;
            }

            for (int i = 0; i < translations.size(); i++) {
                model.addRow(translations.get(i).getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Translation loadUniqueTranslation(int index, Ini ini, String key) {
        return new Translation(
                index,
                ini.get(key, "name"),
                ini.get(key, "key"),
                ini.get(key, "kuerzel"),
                ini.get(key, "bauteil"),
                ini.get(key, "x-achse"),
                ini.get(key, "y-achse"),
                ini.get(key, "z-achse"));
    }
}
