import org.ini4j.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

/**
 * the {@link JPanel} managing all the {@link Translation Translations}
 */
class TranslationsPanel extends JPanel {

    /**
     * the {@link JTable} listing all the {@link Translation Translations}
     */
    private final JTable table;
    /**
     * the {@link TranslationEditPanel}, which is used to edit parameters of the Translation
     */
    private TranslationEditPanel translationEditPanel;
    /**
     * the {@link TableModel} of {@link #table}
     */
    private final DefaultTableModel model;

    /**
     * the {@link Translation Transaltions}
     */
    public Translation[] translations;

    /**
     * the constructor creating a new {@link TranslationsPanel}
     */
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

    /**
     * removes a sekected {@link Translation} using {@link #removeTranslation(Translation)}
     */
    public void removeTranslation() {
        int i = table.getSelectedRow();
        if (i != -1)
            this.removeTranslation(translations[i]);
    }

    /**
     * removes a given {@link Translation} from {@link #translations}
     *
     * @param current the {@link Translation} that should be removed
     */
    private void removeTranslation(Translation current) {
        ArrayList<Translation> translationsList = new ArrayList<>(Arrays.asList(translations));
        translationsList.remove(current);
        translations = translationsList.toArray(new Translation[]{});
        this.refresh();
    }

    /**
     * adds a new {@link Translation} to {@link #translations}
     *
     * @param translation the new {@link Translation}
     */
    public void addTranslation(Translation translation) {
        ArrayList<Translation> translationsList = new ArrayList<>(Arrays.asList(translations));
        translationsList.add(translation);
        translations = translationsList.toArray(translations);
        this.refresh();
    }

    /**
     * changes the index of a selected row based on the given attribute.
     * If the is invalid, or the change operation would be invalid based on the length og the array the method call is ignored
     *
     * @param upDown the direction of index changing. Typically it's 1 or -1 to push it up/down by 1
     */
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

    public void reload() {
        this.clearTable();
        this.loadTranslations();
    }

    /**
     * reloads the {@link #table} by first removing all rows and afterwards reentering the (changed) {@link Translation Translations}
     */
    public void refresh() {
        this.clearTable();

        for (Translation translation : translations) {
            model.addRow(translation.getData());
        }
    }

    private void clearTable() {
        while (table.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    /**
     * loads all the translations of the determined {@link Constants#translationsPath} and enters them into the
     * {@link #table} using {@link #loadUniqueTranslation(Ini, String)}
     */
    private void loadTranslations() {
//        File file = new File(Constants.translationsPath);
        this.clearTable();
//        translations = null;

        File file = Constants.translationsFile;
        if (file.exists())
            try {
                Ini ini = new Ini(file);

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
        else
            System.out.println("Die Translations-ini kann nicht geöffnet werden");
    }

    /**
     * loads a {@link Translation} within in a given ini and a section called key
     *
     * @param ini the ini-file containing the {@link Translation}
     * @param key the section-key
     * @return the read {@link Translation}
     */
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

    /**
     * a test, whether the current {@link #translations} array contains a given {@link Translation}
     *
     * @param current the {@link Translation} that should be tested
     * @return the boolean indicating the containing
     */
    public boolean contains(Translation current) {
        for (Translation translation : translations)
            if (translation == current)
                return true;

        return false;
    }

    /**
     * tests whether a key is unique within the current set of {@link Translation Translations}
     *
     * @param current the currently {@link Translation}. If an existing {@link Translation} is edited it is valid to keep the key as it is
     * @param key     the key that should not already be taken except of the same {@link Translation}
     * @return a boolean indicating if the key is unique
     */
    public boolean isKeyUnique(Translation current, String key) {
        for (Translation translation : translations)
            if (translation != current && translation.get("Key").equals(key))
                return false;

        return true;

    }

    //GETTER AND SETTER
    public TranslationEditPanel getEditPanel() {
        return translationEditPanel;
    }
}
