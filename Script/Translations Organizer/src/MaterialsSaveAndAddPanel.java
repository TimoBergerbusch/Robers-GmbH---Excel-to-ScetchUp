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
    private JButton speichern, hinzufuegen, loeschen;

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

            try {
                File f = new File(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins\\su_RobersExcelConvert\\classes\\materials.ini");
                f.createNewFile();
                Ini ini = new Ini(f);
                ini.getConfig().setEscape(false);
                ini.clear();

                for (MaterialAssignment materialAssignment : parentPanel.materialAssignments) {
                    System.out.println(materialAssignment.toString());
                    printTranslation(ini, materialAssignment);
                }

                ini.store(f);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        private void printTranslation(Ini ini, MaterialAssignment materialAssignment) {
            String key = materialAssignment.getKey();
            String name = materialAssignment.getName();
            String werkstoff = materialAssignment.getWerkstoff();
            String materialGruppe = materialAssignment.getMaterialgruppe();
            Material vorne = materialAssignment.get("Vorne");
            Material hinten = materialAssignment.get("Hinten");
            Material links = materialAssignment.get("Links");
            Material rechts = materialAssignment.get("Rechts");
            Material oben = materialAssignment.get("Oben");
            Material unten = materialAssignment.get("Unten");

            ini.put(key, "key", key);
            ini.put(key, "name", name);
            ini.put(key, "werkstoff", werkstoff);
            ini.put(key, "materialgruppe", materialGruppe);
            ini.put(key, "vorne", vorne.getName());
            ini.put(key, "hinten", hinten.getName());
            ini.put(key, "links", rechts.getName());
            ini.put(key, "rechts", links.getName());
            ini.put(key, "oben", oben.getName());
            ini.put(key, "unten", unten.getName());
        }
    }

    private class HinzufuegenActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("add");
            Material material = parentPanel.getDefaultMaterial();
            JTextField nameTF = new JTextField();
            JTextField keyTF = new JTextField();
            JTextField werkstoffTF = new JTextField();
            JTextField materialgruppeTF = new JTextField();

            Object[] message = new Object[]{
                    "Name:", nameTF,
                    "Key:", keyTF,
                    "Materialgruppe:", materialgruppeTF,
                    "Werkstoff:", werkstoffTF
            };

            int option = JOptionPane.OK_OPTION;
            int allRight = JOptionPane.OK_OPTION;

            while (allRight == JOptionPane.OK_OPTION && option == JOptionPane.OK_OPTION) {
                option = JOptionPane.showConfirmDialog(null, message, "Neues Material", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String name = nameTF.getText();
                    String key = keyTF.getText();
                    String werkstoff = werkstoffTF.getText();
                    String materialgruppe = materialgruppeTF.getText();
                    MaterialAssignment materialAssignment = new MaterialAssignment(name, key, werkstoff, materialgruppe, material);

                    if (parentPanel.isKeyUnique(materialAssignment, key)) {
                        parentPanel.addMaterialAssignment(materialAssignment);
                        option = JOptionPane.CANCEL_OPTION;
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
