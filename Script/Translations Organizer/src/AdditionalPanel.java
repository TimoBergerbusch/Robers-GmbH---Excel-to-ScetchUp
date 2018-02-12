import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

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
        this.add(speichern, gbc);

        gbc.gridy++;
        hinzufuegen = new JButton("Neue Translation Hinzufügen");
        hinzufuegen.setPreferredSize(new Dimension(500, 25));
        this.add(hinzufuegen, gbc);
    }

}
