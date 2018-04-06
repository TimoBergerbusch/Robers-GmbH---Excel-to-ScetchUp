import java.awt.*;

import javax.swing.*;

/**
 * The view, which in Version 0.3 contains <u>all</u> functionality
 */
class View {

    private static View view = null;

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

    private PluginsPanel pluginsPanel;
    private TranslationsPanel translationsPanel;
    private MaterialsPanel materialsPanel;
    private ConstantsPanel constantsPanel;

    private View() {
        this.createView();
    }

    public void reload() {
        translationsPanel.reload();
        materialsPanel.reload();
        constantsPanel.reload();
    }

    public static View getView() {
        if (view == null)
            view = new View();
        return view;
    }

    /**
     * draws the GUI with all the sub-elements
     */
    private void createView() {
        JFrame frame = new JFrame("Translation Organiser - Version " + Constants.Version);
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
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
