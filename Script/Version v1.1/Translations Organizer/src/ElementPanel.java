import javax.swing.*;
import java.awt.*;

/**
 * Created by Timo Bergerbusch on 09.05.2018.
 */
public class ElementPanel extends JPanel {

    private JLabel name, bezeichnung, bauteil, materialgruppe, werkstoff;
    private JComboBox<Translation> translationKey;
    private JComboBox<MaterialAssignment> materialKey;
    private JTextField offset, plankWidth;
    private JCheckBox daneben, bretter;

    private GridBagConstraints gbc;

    public ElementPanel() {
        this.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;

        // Name
        this.add(new JLabel("Name"), gbc);
        gbc.gridx++;
        this.add(name = new JLabel(""), gbc);
        name.setPreferredSize(new Dimension(150,25));
        gbc.gridx = 0;
        gbc.gridy++;

        // Bezeichnung
        this.add(new JLabel("Bezeichnung"), gbc);
        gbc.gridx++;
        this.add(bezeichnung = new JLabel(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Bauteil
        this.add(new JLabel("Bauteil"), gbc);
        gbc.gridx++;
        this.add(bauteil = new JLabel(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Materialgruppe
        this.add(new JLabel("Materialgruppe"), gbc);
        gbc.gridx++;
        this.add(materialgruppe = new JLabel(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Werkstoff
        this.add(new JLabel("Werkstoff"), gbc);
        gbc.gridx++;
        this.add(werkstoff = new JLabel(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Translation
        this.add(new JLabel("Translation"), gbc);
        gbc.gridx++;
        this.add(translationKey = new JComboBox<>(View.translationsPanel.translations), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // MaterialAssignment
        this.add(new JLabel("Material"), gbc);
        gbc.gridx++;
        this.add(materialKey = new JComboBox<>(View.materialsPanel.materialAssignments), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // Translation
        this.add(new JLabel("Offset"), gbc);
        gbc.gridx++;
        this.add(offset = new JTextField(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;

        // daneben
        gbc.gridwidth = 2;
        this.add(daneben = new JCheckBox("Daneben?"), gbc);
        gbc.gridy++;

        // Bretter
        this.add(bretter = new JCheckBox("Bretter?"), gbc);
        gbc.gridy++;

        // Translation
        this.add(new JLabel("Brett-Breite"), gbc);
        gbc.gridx++;
        this.add(plankWidth = new JTextField(""), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    public void loadElement(Element e) {
        name.setText(e.getName());
        bezeichnung.setText(e.getBezeichnung());
        bauteil.setText(e.getBauteil());
        materialgruppe.setText(e.getMaterialgruppe());
        werkstoff.setText(e.getWerkstoff());

        translationKey.setSelectedItem(e.getMatchingTranslation().get("Name"));
        materialKey.setSelectedItem(e.getMatchingMaterialAssignment().getName());

        offset.setText(String.valueOf(e.getOffset()));

        daneben.setSelected(true);
        bretter.setSelected(false);
        plankWidth.setText(String.valueOf(View.constantsPanel.constants.get("brettBreite")));

        this.revalidate();
    }
}
