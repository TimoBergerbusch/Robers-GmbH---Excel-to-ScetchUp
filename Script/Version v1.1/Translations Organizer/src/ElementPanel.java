import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.swing.*;
import javax.swing.plaf.metal.MetalIconFactory;
import java.awt.*;

/**
 * Created by Timo Bergerbusch on 09.05.2018.
 */
public class ElementPanel extends JPanel {

    private JLabel name, bezeichnung, bauteil, materialgruppe, werkstoff;
    private JLabel translationKey;
    private JComboBox<MaterialAssignment> materialKey;
    private JTextField offset, plankWidth;
    private JCheckBox daneben;
    private JComboBox<String> bretter;
    private JButton save, reidentify, discard;

    public GridBagConstraints gbc;

    private Element current;

    public ElementPanel() {
        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;

        // Name
        this.add(new JLabel("Name:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        this.add(name = new JLabel(""), gbc);
        name.setPreferredSize(new Dimension(150, 25));
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // Bezeichnung
        this.add(new JLabel("Bezeichnung:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        this.add(bezeichnung = new JLabel(""), gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // Bauteil
        this.add(new JLabel("Bauteil:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        this.add(bauteil = new JLabel(""), gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // Materialgruppe
        this.add(new JLabel("Materialgruppe:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        this.add(materialgruppe = new JLabel(""), gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // Werkstoff
        this.add(new JLabel("Werkstoff:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        this.add(werkstoff = new JLabel(""), gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // Translation
        this.add(new JLabel("Translation:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        this.add(translationKey = new JLabel(""), gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // MaterialAssignment
        this.add(new JLabel("Material:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        this.add(materialKey = new JComboBox<>(View.materialsPanel.materialAssignments), gbc);
        gbc.gridx++;

        this.add(reidentify = new JButton(""), gbc);
        this.reidentify.setPreferredSize(new Dimension(25, 25));
        this.reidentify.setIcon(MetalIconFactory.getTreeHardDriveIcon());
        this.reidentify.addActionListener(e -> {
            if (current != null) {
                this.current.identifyMaterialAssignment();
                this.materialKey.setSelectedItem(this.current.getMatchingMaterialAssignment());
            }
        });

        gbc.gridx = 0;
        gbc.gridy++;


        // Translation
        this.add(new JLabel("Offset:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        this.add(offset = new JTextField(""), gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // daneben
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        this.add(daneben = new JCheckBox("Daneben?"), gbc);
        this.daneben.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.daneben.addActionListener(e -> {
            if (daneben.isSelected()) {
                this.offset.setEnabled(false);
            } else
                this.offset.setEnabled(true);
        });
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy++;

        // Bretter
        this.add(new JLabel("Bretter:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        gbc.gridwidth = 2;
        this.add(bretter = new JComboBox<>(new String[]{"-", "X-Achse", "Y-Achse", "Z-Achse"}), gbc);
        this.bretter.addActionListener(e -> {
            if (!this.bretter.getSelectedItem().equals("-"))
                materialKey.setSelectedItem(MaterialAssignment.brettMaterial);
            else
                this.reidentify.doClick();
        });
        gbc.gridx = 0;
        gbc.gridy++;

        // Translation
        gbc.gridwidth = 1;
        this.add(new JLabel("Brett-Breite:", SwingConstants.RIGHT), gbc);
        gbc.gridx++;
        this.add(plankWidth = new JTextField(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        gbc.gridwidth = 3;
        this.add(save = new JButton("Speichern"), gbc);
        gbc.gridy++;
        save.addActionListener(e -> {
            if (current != null) {
                if (offset.getText().matches("[(][-]?[0-9]*[,][-]?[0-9]*[,][-]?[0-9]*[)]")) {
                    current.setMatchingMaterialAssignment((MaterialAssignment) materialKey.getSelectedItem());
                    current.setDaneben(this.daneben.isSelected());
                    current.setBretter(this.bretter.getSelectedItem().toString());
                    current.setBrettWidth(Integer.parseInt(this.plankWidth.getText()));

                    this.setVisible(false);
                    this.clear();
                } else
                    JOptionPane.showMessageDialog(null, "Die Koordinaten des Offsets sind nicht im richtigen Format.\n " +
                            "Bitte verwenden Sie das Format '(x,y,z)', wobei x,y und z ganze Zahlen sind.", "Offset Koordinaten Fehlen", JOptionPane.ERROR_MESSAGE);

            }
        });

        // Verwerfen
        this.add(discard = new JButton("Verwerfen"), gbc);
        this.discard.addActionListener(e -> {
            this.setVisible(false);
            this.clear();
        });

    }

    public void loadElement(Element e) {
        current = e;
        name.setText(e.getName());
        bezeichnung.setText(e.getBezeichnung());
        bauteil.setText(e.getBauteil());
        materialgruppe.setText(e.getMaterialgruppe());
        werkstoff.setText(e.getWerkstoff());

        translationKey.setText(e.getMatchingTranslation().get("Name"));
        materialKey.setSelectedItem(e.getMatchingMaterialAssignment());

        offset.setText(String.valueOf(e.getOffset()));


        daneben.setSelected(e.isDaneben());
        bretter.setSelectedItem(current.getBretter());

        plankWidth.setText(String.valueOf(View.constantsPanel.constants.get("brettBreite")));

        this.revalidate();
    }

    private void clear() {
        current = null;
        name.setText("");
        bezeichnung.setText("");
        bauteil.setText("-");
        materialgruppe.setText("-");
        werkstoff.setText("-");

        translationKey.setText("-");
        materialKey.setSelectedItem("-");

        offset.setText("-");


        daneben.setSelected(false);
        bretter.setSelectedItem("-");

        plankWidth.setText("-");
    }
}
