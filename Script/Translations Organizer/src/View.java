import javax.swing.*;
import java.awt.*;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
class View {

    private final String version;

    private final JTabbedPane tabbedPane = new JTabbedPane();

    public View() {
        this.version = Constants.Version;

        this.createView();
    }

    private void createView() {
        JFrame frame = new JFrame("Translation Organiser - Version " + version);
        frame.getContentPane().setLayout(new BorderLayout());

        PluginsPanel pluginsPanel = new PluginsPanel();
        tabbedPane.addTab("Plugins", pluginsPanel);

        TranslationsPanel translationsPanel = new TranslationsPanel();
        tabbedPane.addTab("Translations", translationsPanel);

        MaterialsPanel materialsPanel = new MaterialsPanel();
        tabbedPane.addTab("Materialien", materialsPanel);

        ConstantsPanel constantsPanel = new ConstantsPanel();
        tabbedPane.addTab("Excel-Einstellungen", constantsPanel);

        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
