import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
class TranslationsSaveAndAddPanel extends JPanel {

    private final TranslationsPanel parentPanel;

    public TranslationsSaveAndAddPanel(TranslationsPanel parentPanel) {
        this.parentPanel = parentPanel;


        this.setLayout(new GridBagLayout());
        this.setBorder(new LineBorder(Color.darkGray, 1, true));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 8;

        JButton speichern = new JButton("Alles Speichern");
        speichern.setPreferredSize(new Dimension(500, 25));
        speichern.addActionListener(new SpeichernActionListener());
        speichern.setIcon(MetalIconFactory.getTreeFloppyDriveIcon());
        this.add(speichern, gbc);

        gbc.gridy++;
        JButton hinzufuegen = new JButton("Neue Translation Hinzufügen");
        hinzufuegen.setPreferredSize(new Dimension(500, 25));
        hinzufuegen.addActionListener(new HinzufuegenActionListener());
        hinzufuegen.setIcon(MetalIconFactory.getTreeLeafIcon());
        this.add(hinzufuegen, gbc);

        gbc.gridy++;
        JButton loeschen = new JButton("Translation löschen");
        loeschen.setPreferredSize(new Dimension(500, 25));
        loeschen.addActionListener(new LoeschenActionListener());
        this.add(loeschen, gbc);
    }


    private class SpeichernActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
//                File f = new File(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins\\su_RobersExcelConvert\\classes\\translations.ini");
                File f = Constants.translationsFile;
                f.createNewFile();
                Ini ini = new Ini(f);
                ini.getConfig().setEscape(false);
                ini.clear();

                for (Translation translation : parentPanel.translations) {
                    System.out.println(translation.toString());
                    printTranslation(ini, translation);
                }

                ini.store(f);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        private void printTranslation(Ini ini, Translation translation) {
            String key = translation.get("Key");
            String name = translation.get("Name");
            String kuerzel = translation.get("Kürzel");
            String bauteil = translation.get("Bauteil");
            String x_achse = translation.get("X-Achse");
            String y_achse = translation.get("Y-Achse");
            String z_achse = translation.get("Z-Achse");
            String ausrichtung = translation.get("defaultBrettAusrichtung");

            ini.put(key, "key", key);
            ini.put(key, "name", name);
            ini.put(key, "kuerzel", kuerzel);
            ini.put(key, "bauteil", bauteil);
            ini.put(key, "x-achse", x_achse);
            ini.put(key, "y-achse", y_achse);
            ini.put(key, "z-achse", z_achse);

            ini.put(key, "defaultBrettAusrichtung", ausrichtung);
        }
    }

    private class HinzufuegenActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            parentPanel.getEditPanel().loadTranslation(new Translation("TESTName", "TESTKey", "TESTKürzel", "TESTBauteil", "Länge", "Breite", "Höhe", "X-Achse"));
        }
    }

    private class LoeschenActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            parentPanel.removeTranslation();
        }
    }
}
