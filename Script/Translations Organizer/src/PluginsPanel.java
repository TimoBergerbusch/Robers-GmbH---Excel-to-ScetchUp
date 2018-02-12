import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class PluginsPanel extends JPanel {


    private Color approved = new Color(0, 204, 0);
    private static JTextField path;
    private JButton openDir, testButton;
    private JLabel indicatorPluginsFolder, indicatorGems, indicatorBasicRuby, indicatorInifile, indicatorRubyXL, indicatorRubyZip, indicatorNokogiri, indicatorMiniPortile, indicatorIcons, indicatorTranslations;

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

        // test result indicatorPluginsFolder
        gbc.gridx = 0;
        gbc.gridy++;
        this.indicatorPluginsFolder = new JLabel("Plugins Folder", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorPluginsFolder, gbc);

        //test result Gems
        //test Gems
        gbc.gridx++;
        this.indicatorGems = new JLabel("Gems", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorGems, gbc);
        //test inifile
        gbc.gridy++;
        this.indicatorInifile = new JLabel("Inifile", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorInifile, gbc);
        //test rubyzip
        gbc.gridy++;
        this.indicatorRubyZip = new JLabel("rubyzip", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorRubyZip, gbc);
        //test mini_portile2
        gbc.gridy++;
        this.indicatorMiniPortile = new JLabel("mini_portile2", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorMiniPortile, gbc);
        //test nokogiri
        gbc.gridy++;
        this.indicatorNokogiri = new JLabel("Nokogiri", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorNokogiri, gbc);
        //test rubyXL
        gbc.gridy++;
        this.indicatorRubyXL = new JLabel("RubyXL", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorRubyXL, gbc);

        //test result su_RobersExcelConvert.rb
        gbc.gridx++;
        gbc.gridy = 1;
        this.indicatorBasicRuby = new JLabel("Basic Ruby File", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorBasicRuby, gbc);

        //test result Icons
        gbc.gridy++;
        this.indicatorIcons = new JLabel("Icons", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorIcons, gbc);

        //test result Translations
        gbc.gridy++;
        this.indicatorTranslations = new JLabel("INI-File", SwingConstants.CENTER);
        this.addIndicatorLabel(this.indicatorTranslations, gbc);

    }

    private void addIndicatorLabel(JLabel label, GridBagConstraints gbc) {
        label.setBorder(new LineBorder(Color.black, 2, true));
        label.setPreferredSize(new Dimension(100, 25));
        label.setBackground(Color.lightGray);
        label.setOpaque(true);
        this.add(label, gbc);
    }

    protected void setTest(HashMap<String, Boolean> map) {
        this.setTest(map, indicatorPluginsFolder, "Plugins Folder");
        this.setTest(map, indicatorInifile, "Inifile");
        this.setTest(map, indicatorRubyZip, "rubyzip");

        this.setTest(map, indicatorMiniPortile, "miniportile");
        //this.setTest(map, indicatorNokogiri, "nokogiri");
        if (map.get("nokogiri").booleanValue() && indicatorMiniPortile.getBackground() == approved)
            indicatorNokogiri.setBackground(approved);
        else
            indicatorNokogiri.setBackground(Color.red);

        //this.setTest(map, indicatorRubyXL, "rubyXL");
        if (map.get("rubyXL").booleanValue() && indicatorNokogiri.getBackground() == approved && indicatorRubyZip.getBackground() == approved)
            indicatorRubyXL.setBackground(approved);
        else
            indicatorRubyXL.setBackground(Color.red);

        if (indicatorInifile.getBackground() == approved && indicatorRubyXL.getBackground() == approved)
            indicatorGems.setBackground(approved);
        else
            indicatorGems.setBackground(Color.red);

        this.setTest(map, indicatorBasicRuby, "BasicRubyFile");
        this.setTest(map, indicatorIcons, "Icons");
        this.setTest(map, indicatorTranslations, "INI-File");


    }

    private void setTest(HashMap<String, Boolean> map, JLabel button, String key) {
        if (map.get(key).booleanValue())
            button.setBackground(approved);
        else
            button.setBackground(Color.red);
    }

    private class TestPathActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            File file = new File(path.getText());
            setTest(this.performTest(file));
        }

        private HashMap<String, Boolean> performTest(File file) {
            HashMap<String, Boolean> map = new HashMap<String, Boolean>();

            map.put("Plugins Folder", file.exists() && file.getName().equals("Plugins"));
            String sketchupFolder = file.getParentFile().getAbsolutePath();
            //System.out.println(sketchupFolder);
            //System.out.println(sketchupFolder + "\\Gems64\\gems\\inifile-3.0.0");
            map.put("Inifile", new File(sketchupFolder + "\\Gems64\\gems\\inifile-3.0.0").exists());
            map.put("rubyzip", new File(sketchupFolder + "\\Gems64\\gems\\rubyzip-1.2.1").exists());
            map.put("miniportile", new File(sketchupFolder + "\\Gems64\\gems\\mini_portile2-2.3.0").exists());
            map.put("nokogiri", new File(sketchupFolder + "\\Gems64\\gems\\nokogiri-1.8.1-x64-mingw32").exists());
            map.put("rubyXL", new File(sketchupFolder + "\\Gems64\\gems\\rubyXL-3.3.27").exists());

            map.put("BasicRubyFile", new File(sketchupFolder + "\\Plugins\\su_RobersExcelConvert.rb").exists());
            map.put("Icons", new File(sketchupFolder + "\\Plugins\\su_RobersExcelConvert\\Icons\\garbage.png").exists() &&
                    new File(sketchupFolder + "\\Plugins\\su_RobersExcelConvert\\Icons\\icon.png").exists() &&
                    new File(sketchupFolder + "\\Plugins\\su_RobersExcelConvert\\Icons\\paintbrush.png").exists());
            map.put("INI-File", new File(sketchupFolder + "\\Plugins\\su_RobersExcelConvert\\classes\\translations.ini").exists());
            return map;
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
