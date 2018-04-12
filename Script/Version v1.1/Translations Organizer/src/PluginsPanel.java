import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.*;

/**
 * a {@link JPanel} to represent the Requirements that should bet met in order to be able to successfully run the
 * RobersExcelConvert-Plugin
 */
class PluginsPanel extends JPanel {

    /**
     * the {@link JTextField} containing the path to the folder
     */
    private JTextField path;

    /**
     * creates a new {@link PluginsPanel} with the default path:
     * System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp"
     * and the {@link Requirement Requirements} given in {@link Constants#requirement}
     * NOTE: as {@link Material Materials} there are only these mentioned, which are used within at-least one {@link MaterialAssignment}
     */
    public PluginsPanel() {
        // Basic Panel Setup
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 6;


        // Path textfield
        path = new JTextField(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp");
        path.setEnabled(false);
        this.add(path, gbc);

        //Opendir
        gbc.gridwidth = 1;
        gbc.gridx = 6;
        JButton openDir = new JButton();
        openDir.setIcon(MetalIconFactory.getTreeComputerIcon());
        openDir.addActionListener(new PathActionListener());
        this.add(openDir, gbc);

        // Test Button
        gbc.gridx++;
        JButton testButton = new JButton("Test Integrity");
        testButton.addActionListener(new TestPathActionListener());
        this.add(testButton, gbc);

        // create Labels
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 9;

        JPanel labelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = gbc2.gridy = 0;
        gbc2.gridwidth = gbc2.gridheight = 1;
        gbc2.weightx = gbc2.weighty = 0;

        for (Tuple t : Constants.requirement.getStructure(new ArrayList<>(), gbc.gridx)) {
            gbc2.gridy++;
            gbc2.gridx = t.getIndex();
            JLabel lbl = new JLabel(t.getRequirementName(), SwingConstants.CENTER);
            t.getRequirement().setLabel(lbl);
            this.addIndicatorLabel(lbl, gbc2, labelPanel);
        }

        JScrollPane scrollPane = new JScrollPane(labelPanel);
        scrollPane.setPreferredSize(new Dimension(900, 600));

        this.add(scrollPane, gbc);
    }

    /**
     * creates a new {@link JLabel} which represents a {@link Requirement}
     *
     * @param label the label of the {@link Requirement}
     * @param gbc   {@link GridBagConstraints}
     * @param panel the panel the label should be put onto
     */
    private void addIndicatorLabel(JLabel label, GridBagConstraints gbc, JPanel panel) {
        label.setBorder(new LineBorder(Color.black, 2, true));
        label.setPreferredSize(new Dimension(150, 25));
        label.setBackground(Color.lightGray);
        label.setOpaque(true);
        panel.add(label, gbc);
    }

    /**
     * the {@link ActionListener} to test weather the given path fulfils the requirements
     */
    private class TestPathActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            Constants.requirement.test(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins");
            Constants.requirement.test(path.getText());
        }
    }

    /**
     * the {@link ActionListener} to choose the path
     */
    private class PathActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(View.frame.getSize());
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(new File(path.getText()));
            chooser.setDialogTitle("Select the Plugins-Folder");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//                path.setText(chooser.getSelectedFile().getAbsolutePath());
                Constants.defaultPath = chooser.getSelectedFile().getAbsolutePath();
                path.setText(Constants.defaultPath);
                Constants.reload();
            }
        }
    }
}
