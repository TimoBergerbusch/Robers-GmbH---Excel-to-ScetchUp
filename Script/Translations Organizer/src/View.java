import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class View {

    private JFrame frame;

    private String version;

    public PluginsPanel pluginsPanel;
    public static TranslationsPanel translationsPanel;
    public MaterialsPanel materialsPanel;
    public ConstantsPanel constantsPanel;

    public JTabbedPane tabbedPane = new JTabbedPane();

    public View(String version) {
        this.version = version;

        this.createView();
    }

    private void createView() {
        frame = new JFrame("Translation Organiser - Version " + version);
        frame.getContentPane().setLayout(new BorderLayout());

        pluginsPanel = new PluginsPanel();
        tabbedPane.addTab("Plugins", pluginsPanel);

        translationsPanel = new TranslationsPanel();
        tabbedPane.addTab("Translations", translationsPanel);

        materialsPanel = new MaterialsPanel();
        tabbedPane.addTab("Materialien", materialsPanel);

        constantsPanel = new ConstantsPanel();
        tabbedPane.addTab("Excel-Einstellungen", constantsPanel);

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
