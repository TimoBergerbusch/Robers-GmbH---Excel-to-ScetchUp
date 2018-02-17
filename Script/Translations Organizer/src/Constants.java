import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class Constants {

    public static final String Version = "0.2";
    public static final Color goodColor = new Color(0, 155, 0);
    public static final Color editingColor = Color.red;
    public static final Color partNotLoaded = Color.orange;
    public static final Color notLoadedColor = Color.red;

    public static Requirement requirement = initRequirement();

    private static Requirement initRequirement() {
        // Pfad: Plugins
        return new Requirement("Plugins", "", new ArrayList<Requirement>() {{
            add(new Requirement("Base Ruby", "su_RobersExcelConvert.rb", null));
            // Pfad: Plugins/su_RobersExcelConvert
            add(new Requirement("Folder", "su_RobersExcelConvert", new ArrayList<Requirement>() {{
                // Pfad: Plugins/su_RobersExcelConvert/Icons
                add(new Requirement("Icons", "icons", new ArrayList<Requirement>() {{
                    add(new Requirement("Garbage", "garbage.png", null));
                    add(new Requirement("ExcelIcon", "icon.png", null));
                    add(new Requirement("Paintbrush", "paintbrush.png", null));
                }}));
                // Pfad: Plugins/su_RobersExcelConvert/classes
                add(new Requirement("Klassen", "classes", new ArrayList<Requirement>() {{
                    add(new Requirement("element_identifier", "element_identifier.rb", null));
                    add(new Requirement("entityHandler", "entityHandler.rb", null));
                    add(new Requirement("excelReader", "excelReader.rb", null));
                    add(new Requirement("materialHandler", "materialHandler.rb", null));
                    add(new Requirement("rectangle", "rectangle.rb", null));
                    add(new Requirement("Translations", "translations.ini", null));
                }}));
                // Pfad: Plugins/su_RobersExcelConvert/textures
                add(new Requirement("Texturen", "textures", new ArrayList<Requirement>() {{
                    add(new Requirement("texture1", "texture1.jpg", null));
                    add(new Requirement("texture2", "texture2.jpg", null));
                    add(new Requirement("texture3", "texture3.jpg", null));
                    add(new Requirement("texture4", "texture4.jpg", null));
                    add(new Requirement("texture5", "texture5.jpg", null));
                    add(new Requirement("texture6", "texture6.jpg", null));
                }}));
            }}));
        }}
        );
    }
}
