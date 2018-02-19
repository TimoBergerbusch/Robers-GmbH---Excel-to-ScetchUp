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
public class MaterialsSaveAndAddPanel extends JPanel {

    GridBagConstraints gbc;
    JButton speichern, hinzufuegen, loeschen;

    private MaterialsPanel parentPanel;

    public MaterialsSaveAndAddPanel(MaterialsPanel parentPanel) {
        this.parentPanel = parentPanel;


        this.setLayout(new GridBagLayout());
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
        hinzufuegen = new JButton("Neues Material hinzufügen");
        hinzufuegen.setPreferredSize(new Dimension(500, 25));
        hinzufuegen.addActionListener(new HinzufuegenActionListener());
        this.add(hinzufuegen, gbc);

        gbc.gridy++;
        loeschen = new JButton("Material löschen");
        loeschen.setPreferredSize(new Dimension(500, 25));
        loeschen.addActionListener(new LoeschenActionListener());
        this.add(loeschen, gbc);
    }


    private class SpeichernActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("save");

//            try {
//                File f = new File(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins\\su_RobersExcelConvert\\classes\\translations_new.ini");
//                f.createNewFile();
//                Ini ini = new Ini(f);
//                ini.clear();
//
//                for (MaterialAssignment materialAssignment : parentPanel.materialAssignments) {
//                    System.out.println(materialAssignment.toString());
//                    printTranslation(ini, materialAssignment);
//                }
//
//                ini.store(f);
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
        }

        private void printTranslation(Ini ini, MaterialAssignment materialAssignment) {
//            String key = translation.get("Key");
//            String name = translation.get("Name");
//            String kuerzel = translation.get("Kürzel");
//            String bauteil = translation.get("Bauteil");
//            String x_achse = translation.get("X-Achse");
//            String y_achse = translation.get("Y-Achse");
//            String z_achse = translation.get("Z-Achse");
//
//            ini.put(key, "key", key);
//            ini.put(key, "name", name);
//            ini.put(key, "kuerzel", kuerzel);
//            ini.put(key, "bauteil", bauteil);
//            ini.put(key, "x-achse", x_achse);
//            ini.put(key, "y-achse", y_achse);
//            ini.put(key, "z-achse", z_achse);
        }
    }

    private class HinzufuegenActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("add");
            Material material = parentPanel.getDefaultMaterial();
            JTextField nameTF = new JTextField();
            JTextField keyTF = new JTextField();
            JTextField bauteilTF = new JTextField();

            Object[] message = new Object[]{
                    "Name:", nameTF,
                    "Key:", keyTF,
                    "Bauteil:", bauteilTF
            };

            int option = JOptionPane.OK_OPTION;
            int allRight = JOptionPane.OK_OPTION;

            while (allRight == JOptionPane.OK_OPTION && option == JOptionPane.OK_OPTION) {
                option = JOptionPane.showConfirmDialog(null, message, "Neues Material", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String name = nameTF.getText();
                    String key = keyTF.getText();
                    String bauteil = bauteilTF.getText();
                    MaterialAssignment materialAssignment = new MaterialAssignment(name, key, bauteil, material);

                    if (parentPanel.isKeyUnique(materialAssignment, key)) {
                        parentPanel.addMaterialAssignment(materialAssignment);
                    } else {
                        allRight = JOptionPane.showConfirmDialog(null, "Der Key existiert bereits.", "Fehler: Kein unqiue Key", JOptionPane.OK_CANCEL_OPTION);
                    }
                }
            }
        }
    }

    private class LoeschenActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            parentPanel.removeMaterialAssignment();
        }
    }
}
