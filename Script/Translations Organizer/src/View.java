import javax.swing.*;
import java.awt.*;

/**
 * The view, which in Version 0.3 contains <u>all</u> functionality
 */
class View {

    /**
     * the {@link JTabbedPane} containing the different parts that could be edited:
     * <ol>
     * <li>{@link PluginsPanel Requirement-Semantic-Check}</li>
     * <li>{@link TranslationsPanel Translations}</li>
     * <li>{@link MaterialsPanel MaterialAssignments}</li>
     * <li>{@link ConstantsPanel Constants}</li>
     * </ol>
     */
    private final JTabbedPane tabbedPane = new JTabbedPane();

    public View() {
        this.createView();
    }

    /**
     * draws the GUI with all the sub-elements
     */
    private void createView() {
        JFrame frame = new JFrame("Translation Organiser - Version " + Constants.Version);
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
