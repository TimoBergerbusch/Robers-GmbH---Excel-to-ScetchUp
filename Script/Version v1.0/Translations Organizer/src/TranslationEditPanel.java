import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class TranslationEditPanel extends JPanel {

    private static Translation current;
    private final JLabel informationLabel;
    private final JTextField nameField;
    private final JTextField keyField;
    private final JTextField kuerzelField;
    private final JTextField bauteilField;
    private final JComboBox<String> x_achseBox;
    private final JComboBox<String> y_achseBox;
    private final JComboBox<String> z_achseBox;
    private final TranslationsPanel parentPanel;

    public TranslationEditPanel(TranslationsPanel translationsPanel) {
        super(new GridBagLayout());

        this.parentPanel = translationsPanel;
//        this.setBorder(new LineBorder(Color.darkGray, 3, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;

        informationLabel = new JLabel("- no Translation selected -");
//        informationLabel.setSize(new Dimension(250,25));
        informationLabel.setForeground(Constants.goodColor);
        informationLabel.setFont(new Font("Arial", Font.BOLD, 18));
        informationLabel.setAlignmentX(CENTER_ALIGNMENT);
        this.add(informationLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        //name
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setToolTipText("Der Name des Bauteils");
        this.add(nameLabel, gbc);
        gbc.gridx++;
        nameField = new JTextField("");
        nameField.setPreferredSize(new Dimension(150, 25));
        this.add(nameField, gbc);

        //key
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel keyLabel = new JLabel("Key:");
        keyLabel.setToolTipText("Der eindeutige Key zum identifiziern");
        this.add(keyLabel, gbc);
        gbc.gridx++;
        keyField = new JTextField("");
        keyField.setPreferredSize(new Dimension(150, 25));
        this.add(keyField, gbc);

        //kuerzel
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel kuerzelLabel = new JLabel("Kürzel:");
        kuerzelLabel.setToolTipText("Das Kürzel in der Excel-Datei, welche dieses Bauteil identifiziert");
        this.add(kuerzelLabel, gbc);
        gbc.gridx++;
        kuerzelField = new JTextField("");
        kuerzelField.setPreferredSize(new Dimension(150, 25));
        this.add(kuerzelField, gbc);

        //bauteil
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel bauteilLabel = new JLabel("Bauteil:");
        bauteilLabel.setToolTipText("Schlagwort, welches NICHT in der Bauteil Spalte vorkommen darf");
        this.add(bauteilLabel, gbc);
        gbc.gridx++;
        bauteilField = new JTextField("");
        bauteilField.setPreferredSize(new Dimension(150, 25));
        this.add(bauteilField, gbc);

        //x-achse
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel x_achseLabel = new JLabel("X-Achse");
        x_achseLabel.setToolTipText("Das Maß, welches auf die X-Achse abgebildet werden soll");
        this.add(x_achseLabel, gbc);
        gbc.gridx++;
        x_achseBox = new JComboBox<>(new String[]{"-", "Laenge", "Breite", "Hoehe"});
        x_achseBox.setPreferredSize(new Dimension(150, 25));
        this.add(x_achseBox, gbc);

        //y-achse
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel y_achseLabel = new JLabel("Y-Achse");
        y_achseLabel.setToolTipText("Das Maß, welches auf die Y-Achse abgebildet werden soll");
        this.add(y_achseLabel, gbc);
        gbc.gridx++;
        y_achseBox = new JComboBox<>(new String[]{"-", "Laenge", "Breite", "Hoehe"});
        y_achseBox.setPreferredSize(new Dimension(150, 25));
        this.add(y_achseBox, gbc);

        //z-achse
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel z_achseLabel = new JLabel("Z-Achse");
        z_achseLabel.setToolTipText("Das Maß, welches auf die Z-Achse abgebildet werden soll");
        this.add(z_achseLabel, gbc);
        gbc.gridx++;
        z_achseBox = new JComboBox<>(new String[]{"-", "Laenge", "Breite", "Hoehe"});
        z_achseBox.setPreferredSize(new Dimension(150, 25));
        this.add(z_achseBox, gbc);

        //speichern
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton speichern = new JButton("Translation Speichern");
        speichern.setPreferredSize(new Dimension(150, 25));
        speichern.addActionListener(new SpeichernActionListener());
        this.add(speichern, gbc);

        //verwerfen
        gbc.gridy++;
        JButton verwerfen = new JButton("Änderungen verwerfen");
        verwerfen.setPreferredSize(new Dimension(150, 25));
        verwerfen.addActionListener(e -> clearPanel());
        this.add(verwerfen, gbc);
    }

    public void loadTranslation(Translation translation) {
        current = translation;
        this.refresh();

        nameField.setText(translation.get("Name"));
        keyField.setText(translation.get("Key"));
        bauteilField.setText(translation.get("Bauteil"));
        kuerzelField.setText(translation.get("Kürzel"));

        x_achseBox.setSelectedItem(translation.get("X-Achse"));
        y_achseBox.setSelectedItem(translation.get("Y-Achse"));
        z_achseBox.setSelectedItem(translation.get("Z-Achse"));

    }

    private void refresh() {
        informationLabel.setText("Editing: " + current.get("Kürzel"));
        informationLabel.setForeground(Constants.editingColor);
    }

    private void clearPanel() {
        informationLabel.setText("- no Translation selected -");
        informationLabel.setForeground(Constants.goodColor);
        nameField.setText("");
        keyField.setText("");
        kuerzelField.setText("");
        bauteilField.setText("");
        x_achseBox.setSelectedItem("-");
        y_achseBox.setSelectedItem("-");
        z_achseBox.setSelectedItem("-");
    }

    private class SpeichernActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (x_achseBox.getSelectedIndex() != y_achseBox.getSelectedIndex() &&
                    x_achseBox.getSelectedIndex() != z_achseBox.getSelectedIndex() &&
                    y_achseBox.getSelectedIndex() != z_achseBox.getSelectedIndex()) {
                if (current != null) {
                    if (parentPanel.isKeyUnique(current, keyField.getText())) {
                        current.set("Name", nameField.getText());
                        current.set("Key", keyField.getText());
                        current.set("Bauteil", bauteilField.getText());
                        current.set("Kürzel", kuerzelField.getText());

                        current.set("X-Achse", x_achseBox.getSelectedItem());
                        current.set("Y-Achse", y_achseBox.getSelectedItem());
                        current.set("Z-Achse", z_achseBox.getSelectedItem());

                        if (!parentPanel.contains(current))
                            parentPanel.addTranslation(current);

                        parentPanel.refresh();

                        clearPanel();
                    } else {
                        JOptionPane.showMessageDialog(null, "Der Key ist nicht eindeutig", "Fehler", JOptionPane.ERROR_MESSAGE);
                    }
                } else
                    JOptionPane.showMessageDialog(null,
                            "Es gibt kein aktuelles Element, welches gespeichert werden kann!",
                            "Fehler: Speichern fehlgeschlagen", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                        "Die Auf X, Y und Z abgebildeten Maße müssen verschieden sein!",
                        "Fehler: Achsenzuweisung", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
