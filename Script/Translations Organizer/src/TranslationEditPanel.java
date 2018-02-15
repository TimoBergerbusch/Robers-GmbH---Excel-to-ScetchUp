import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class TranslationEditPanel extends JPanel {

    GridBagConstraints gbc;
    static Translation current;
    JLabel nameLabel, keyLabel, kuerzelLabel, bauteilLabel, x_achseLabel, y_achseLabel, z_achseLabel;
    JTextField nameField, keyField, kuerzelField, bauteilField;
    JComboBox<String> x_achseBox, y_achseBox, z_achseBox;
    JButton speichern;

    public TranslationEditPanel() {
        super(new GridBagLayout());
        this.setBorder(new LineBorder(Color.darkGray, 3, true));

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;

        //name
        nameLabel = new JLabel("Name:");
        nameLabel.setToolTipText("Der Name des Bauteils");
        this.add(nameLabel, gbc);
        gbc.gridx++;
        nameField = new JTextField("");
        nameField.setPreferredSize(new Dimension(150, 25));
        this.add(nameField, gbc);

        //key
        gbc.gridy++;
        gbc.gridx = 0;
        keyLabel = new JLabel("Key:");
        keyLabel.setToolTipText("Der eindeutige Key zum identifiziern");
        this.add(keyLabel, gbc);
        gbc.gridx++;
        keyField = new JTextField("");
        keyField.setPreferredSize(new Dimension(150, 25));
        this.add(keyField, gbc);

        //kuerzel
        gbc.gridy++;
        gbc.gridx = 0;
        kuerzelLabel = new JLabel("Kürzel:");
        kuerzelLabel.setToolTipText("Das Kürzel in der Excel-Datei, welche dieses Bauteil identifiziert");
        this.add(kuerzelLabel, gbc);
        gbc.gridx++;
        kuerzelField = new JTextField("");
        kuerzelField.setPreferredSize(new Dimension(150, 25));
        this.add(kuerzelField, gbc);

        //bauteil
        gbc.gridy++;
        gbc.gridx = 0;
        bauteilLabel = new JLabel("Bauteil:");
        bauteilLabel.setToolTipText("Schlagwort, welches NICHT in der Bauteil Spalte vorkommen darf");
        this.add(bauteilLabel, gbc);
        gbc.gridx++;
        bauteilField = new JTextField("");
        bauteilField.setPreferredSize(new Dimension(150, 25));
        this.add(bauteilField, gbc);

        //x-achse
        gbc.gridy++;
        gbc.gridx = 0;
        x_achseLabel = new JLabel("X-Achse");
        x_achseLabel.setToolTipText("Das Maß, welches auf die X-Achse abgebildet werden soll");
        this.add(x_achseLabel, gbc);
        gbc.gridx++;
        x_achseBox = new JComboBox<>(new String[]{"-", "Länge", "Breite", "Höhe"});
        x_achseBox.setPreferredSize(new Dimension(150, 25));
        this.add(x_achseBox, gbc);

        //y-achse
        gbc.gridy++;
        gbc.gridx = 0;
        y_achseLabel = new JLabel("Y-Achse");
        y_achseLabel.setToolTipText("Das Maß, welches auf die Y-Achse abgebildet werden soll");
        this.add(y_achseLabel, gbc);
        gbc.gridx++;
        y_achseBox = new JComboBox<>(new String[]{"-", "Länge", "Breite", "Höhe"});
        y_achseBox.setPreferredSize(new Dimension(150, 25));
        this.add(y_achseBox, gbc);

        //z-achse
        gbc.gridy++;
        gbc.gridx = 0;
        z_achseLabel = new JLabel("Z-Achse");
        z_achseLabel.setToolTipText("Das Maß, welches auf die Z-Achse abgebildet werden soll");
        this.add(z_achseLabel, gbc);
        gbc.gridx++;
        z_achseBox = new JComboBox<>(new String[]{"-", "Länge", "Breite", "Höhe"});
        z_achseBox.setPreferredSize(new Dimension(150, 25));
        this.add(z_achseBox, gbc);

        //speichern
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        speichern = new JButton("Translation Speichern");
        speichern.setPreferredSize(new Dimension(150, 25));
        speichern.addActionListener(new SpeichernActionListener());
        this.add(speichern, gbc);
    }

    public void loadTranslation(Translation translation) {
        this.current = translation;

        nameField.setText(translation.get("Name"));
        keyField.setText(translation.get("Key"));
        bauteilField.setText(translation.get("Bauteil"));
        kuerzelField.setText(translation.get("Kürzel"));

        x_achseBox.setSelectedItem(translation.get("X-Achse"));
        y_achseBox.setSelectedItem(translation.get("Y-Achse"));
        z_achseBox.setSelectedItem(translation.get("Z-Achse"));

    }

    private class SpeichernActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (x_achseBox.getSelectedIndex() != y_achseBox.getSelectedIndex() &&
                    x_achseBox.getSelectedIndex() != z_achseBox.getSelectedIndex() &&
                    y_achseBox.getSelectedIndex() != z_achseBox.getSelectedIndex()) {
                if (current != null) {
                    current.set("Name", nameField.getText());
                    current.set("Key", keyField.getText());
                    current.set("Bauteil", bauteilField.getText());
                    current.set("Kürzel", kuerzelField.getText());

                    current.set("X-Achse", x_achseBox.getSelectedItem());
                    current.set("Y-Achse", y_achseBox.getSelectedItem());
                    current.set("Z-Achse", z_achseBox.getSelectedItem());
                    View.translationsPanel.refresh();
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
