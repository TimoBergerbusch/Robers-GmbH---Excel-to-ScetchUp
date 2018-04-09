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
 * the {@link JPanel} to save, add and delete a selected {@link MaterialAssignment}
 */
class MaterialsSaveAndAddPanel extends JPanel {

    /**
     * the hierarchically lowest {@link JPanel} inheriting this panel
     */
    private final MaterialsPanel parentPanel;

    /**
     * creates a new {@link MaterialsSaveAndAddPanel} with the save, add and delete {@link JButton}
     *
     * @param parentPanel tge {@link #parentPanel}
     */
    public MaterialsSaveAndAddPanel(MaterialsPanel parentPanel) {
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
        JButton hinzufuegen = new JButton("Neues Material hinzufügen");
        hinzufuegen.setPreferredSize(new Dimension(500, 25));
        hinzufuegen.addActionListener(new HinzufuegenActionListener());
        hinzufuegen.setIcon(MetalIconFactory.getTreeLeafIcon());
        this.add(hinzufuegen, gbc);

        gbc.gridy++;
        JButton loeschen = new JButton("Material löschen");
        loeschen.setPreferredSize(new Dimension(500, 25));
        loeschen.addActionListener(new LoeschenActionListener());
        this.add(loeschen, gbc);
    }

    /**
     * the {@link ActionListener} of the save-Button.
     * It is used to save the currently listed {@link MaterialAssignment MaterialAssignments} within the materials.ini
     */
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

                ini.store();
//                ini.store(f);
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

            System.out.println(name + " " + key + " " + werkstoff + " " + materialGruppe + " " + vorne.getName() + " " + hinten.getName() + " " + links.getName() + " " + rechts.getName() + " " + oben + " " + unten);
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

    /**
     * the {@link ActionListener} of the add-Button.
     * It uses a {@link JOptionPane} to create a new {@link MaterialAssignment}. This only uses the {@link MaterialAssignment#name}, {@link MaterialAssignment#key},
     * {@link MaterialAssignment#werkstoff} and {@link MaterialAssignment#materialgruppe}.
     * The materials can be edited within the list
     */
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

    /**
     * the {@link ActionListener} of the delete-Button.
     * It deletes the selected {@link MaterialAssignment} <u>without</u> any further acknowledge or confirmation
     */
    private class LoeschenActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            parentPanel.removeMaterialAssignment();
        }
    }
}
