import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
class TranslationsPanel extends JPanel {

    private final JTable table;
    private TranslationEditPanel translationEditPanel;
    private final DefaultTableModel model;

    public Translation[] translations;

    public TranslationsPanel() {
        this.setLayout(new BorderLayout());

//        JComboBox<String> comboBox = new JComboBox<>(new String[]{"Länge", "Breite", "Höhe"});

        model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int colum) {
                return false;
            }
        };
        String[] columnNames = {"Name", "Key", "Kürzel", "Bauteil", "X-Achse", "Y-Achse", "Z-Achse"};
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);
        table.setRowHeight(25);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2)
                    translationEditPanel.loadTranslation(translations[row]);
            }
        });
        this.loadTranslations();
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        translationEditPanel = new TranslationEditPanel(this);
        this.add(translationEditPanel, BorderLayout.EAST);

        TranslationsSaveAndAddPanel additionalPanel = new TranslationsSaveAndAddPanel(this);
        this.add(additionalPanel, BorderLayout.SOUTH);

        TranslationsMovePanel translationsMovePanel = new TranslationsMovePanel(this);
        this.add(translationsMovePanel, BorderLayout.WEST);
    }

    public void removeTranslation() {
        int i = table.getSelectedRow();
        if (i != -1)
            this.removeTranslation(translations[i]);
    }

    private void removeTranslation(Translation current) {
        ArrayList<Translation> translationsList = new ArrayList<>(Arrays.asList(translations));
        translationsList.remove(current);
        translations = translationsList.toArray(new Translation[]{});
        this.refresh();
    }

    public void addTranslation(Translation translation) {
        ArrayList<Translation> translationsList = new ArrayList<>(Arrays.asList(translations));
        translationsList.add(translation);
        translations = translationsList.toArray(translations);
        this.refresh();
    }

    public void changeIndices(int upDown) {
        int i = table.getSelectedRow();
        if (i + upDown < translations.length && i + upDown >= 0 && i != -1) {
            Translation t = translations[i];
            translations[i] = translations[i + upDown];
            translations[i + upDown] = t;
            this.refresh();
            table.setRowSelectionInterval(i + upDown, i + upDown);
        }
    }

    public void refresh() {
        while (table.getRowCount() > 0) {
            model.removeRow(0);
        }

        for (Translation translation : translations) {
            model.addRow(translation.getData());
        }
    }

    private void loadTranslations() {
        try {
            Ini ini = new Ini(new File(Constants.defaultPath+"\\su_RobersExcelConvert\\classes\\translations.ini"));

            Set<String> keys = ini.keySet();

            ArrayList<Translation> translationsList = new ArrayList<>();
            int index = 0;
            for (String key : keys) {
                translationsList.add(this.loadUniqueTranslation(ini, key));
                index++;
            }
            translations = translationsList.toArray(new Translation[]{});

            for (Translation translation : translations) {
                model.addRow(translation.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TranslationEditPanel getEditPanel() {
        return translationEditPanel;
    }

    private Translation loadUniqueTranslation(Ini ini, String key) {
        return new Translation(
                ini.get(key, "name"),
                ini.get(key, "key"),
                ini.get(key, "kuerzel"),
                ini.get(key, "bauteil"),
                ini.get(key, "x-achse"),
                ini.get(key, "y-achse"),
                ini.get(key, "z-achse"));
    }

    public boolean contains(Translation current) {
        for (Translation translation : translations)
            if (translation == current)
                return true;

        return false;
    }


    public boolean isKeyUnique(Translation current, String key) {
        for (Translation translation : translations)
            if (translation != current && translation.get("Key").equals(key))
                return false;

        return true;

    }
}
