import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class AdditionalPanel extends JPanel {

    GridBagConstraints gbc;
    JButton speichern, hinzufuegen;


    public AdditionalPanel() {
        super(new GridBagLayout());
        this.setBorder(new LineBorder(Color.darkGray, 1, true));

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 8;

        speichern = new JButton("Alles Speichern");
        speichern.setPreferredSize(new Dimension(500, 25));
        speichern.addActionListener(new SpeichernActionListener());
        this.add(speichern, gbc);

        gbc.gridy++;
        hinzufuegen = new JButton("Neue Translation Hinzufügen");
        hinzufuegen.setPreferredSize(new Dimension(500, 25));
        hinzufuegen.addActionListener(new HinzufuegenActionListener());
        this.add(hinzufuegen, gbc);
    }


    private class SpeichernActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                File f = new File(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins\\su_RobersExcelConvert\\classes\\translations_new.ini");
                f.createNewFile();
                Ini ini = new Ini(f);
                ini.clear();

                for (Translation translation : TranslationsPanel.translations)
                    this.printTranslation(ini, translation);

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

            ini.put(key, "key", key);
            ini.put(key, "name", name);
            ini.put(key, "kuerzel", kuerzel);
            ini.put(key, "bauteil", bauteil);
            ini.put(key, "x-achse", x_achse);
            ini.put(key, "y-achse", y_achse);
            ini.put(key, "z-achse", z_achse);
        }
    }

    private class HinzufuegenActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }
}
