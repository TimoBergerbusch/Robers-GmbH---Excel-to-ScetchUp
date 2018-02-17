import sun.security.pkcs11.wrapper.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class PluginsPanel extends JPanel {

    private static JTextField path;
    private JButton openDir, testButton;

//    private HashMap<String, JLabel> labels;

    public PluginsPanel() {
        // Basic Panel Setup
        this.setLayout(new GridBagLayout());
        this.setBorder(new LineBorder(Color.darkGray, 3, true));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 6;


        // Path textfield
        this.path = new JTextField(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins");
        this.path.setEnabled(false);
        this.path.setMinimumSize(new Dimension(350, 25));
        this.add(path, gbc);

        //Opendir
        gbc.gridwidth = 1;
        gbc.gridx = 6;
        this.openDir = new JButton("O");
        this.openDir.addActionListener(new PathActionListener());
        this.add(openDir, gbc);

        // Test Button
        gbc.gridx++;
        this.testButton = new JButton("Test Integrity");
        testButton.addActionListener(new TestPathActionListener());
        this.add(testButton, gbc);

        // create Labels
        gbc.gridx = 0;
        gbc.gridy++;
//        labels = new HashMap<>();

//        System.out.println(Constants.requirement.test(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins"));
//        System.out.println(Constants.requirement.getStructure(new ArrayList<>(),0));
        for (Tuple t : Constants.requirement.getStructure(new ArrayList<>(), gbc.gridx)) {
            gbc.gridy++;
            JLabel lbl = new JLabel(t.getRequirementName(), SwingConstants.CENTER);
            gbc.gridx = t.getIndex();
//            labels.put(t.getRequirementName(),lbl);
            t.getRequirement().setLabel(lbl);
            this.addIndicatorLabel(lbl, gbc);
        }


    }

    private void addIndicatorLabel(JLabel label, GridBagConstraints gbc) {
        label.setBorder(new LineBorder(Color.black, 2, true));
        label.setPreferredSize(new Dimension(100, 25));
        label.setBackground(Color.lightGray);
        label.setOpaque(true);
        this.add(label, gbc);
    }

    private class TestPathActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Constants.requirement.test(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins");
        }
    }

    private class PathActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(PluginsPanel.path.getText()));
            chooser.setDialogTitle("Select the Plugins-Folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                PluginsPanel.path.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }
}
