import javax.swing.*;
import java.awt.*;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class View {

    private JFrame frame;

    private String version;

    public PluginsPanel pluginsPanel;
    public static TranslationsPanel translationsPanel;
    public static TranslationEditPanel translationEditPanel;
    public AdditionalPanel additionalPanel;

    public View(String version) {
        this.version = version;
        this.createView();
    }

    private void createView() {
        frame = new JFrame("Translation Organiver - Version " + version);
        frame.setLayout(new BorderLayout());

        frame.add(pluginsPanel = new PluginsPanel(), BorderLayout.NORTH);

        frame.add(translationsPanel = new TranslationsPanel(), BorderLayout.CENTER);

        frame.add(translationEditPanel = new TranslationEditPanel(), BorderLayout.EAST);

        frame.add(additionalPanel = new AdditionalPanel(), BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
