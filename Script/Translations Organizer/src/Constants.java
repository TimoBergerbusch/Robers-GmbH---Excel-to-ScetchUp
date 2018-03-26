import org.ini4j.Ini;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
class Constants {

    public static final String Version = "0.2";
    public static final Color goodColor = new Color(0, 155, 0);
    public static final Color editingColor = Color.red;
    public static final Color partNotLoaded = Color.orange;
    public static final Color notLoadedColor = Color.red;
    public static final Color neutral = Color.lightGray;
    public static final String defaultPath = System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins";

    public static final String excelConstantSectionName = "RobersExcelConstants";
    public static final String excelConstantsPath = defaultPath + "\\su_RobersExcelConvert\\classes\\constants.ini";
    public static final String[] excelConstants = new String[]{"headerRow", "Lfd", "SageArt", "Bezeichnung", "Bauteil", "Materialgruppe", "Werkstoff", "Anzahl", "Laenge", "Breite", "Hoehe"};

    public static final String materialsPath = defaultPath + "\\su_RobersExcelConvert\\classes\\materials.ini";

    public static Material errorMaterial;

    public static final Requirement requirement = initRequirement();

    private static Requirement initRequirement() {

        ArrayList<String> names = new ArrayList<>();

        try {
            Ini ini = new Ini(new File(materialsPath));
            Set<String> keys = ini.keySet();
            String[] sideNames = new String[]{"vorne", "hinten", "links", "rechts", "oben", "unten"};
            String name;
            for (String section : keys) {
                for (String side : sideNames) {
                    name = ini.get(section, side);
                    if (!names.contains(name)) {
                        names.add(name);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Requirement> texturen = getMaterialsAsRequirement(names);

        return new Requirement("SketchUp", "", new ArrayList<Requirement>() {{
            // Pfad: SketchUp/Plugins
            add(new Requirement("Plugins", "Plugins", new ArrayList<Requirement>() {{
                add(new Requirement("Base Ruby", "su_RobersExcelConvert.rb", null));
                // Pfad: SketchUp/Plugins/su_RobersExcelConvert
                add(new Requirement("Folder", "su_RobersExcelConvert", new ArrayList<Requirement>() {{
                    // Pfad: SketchUp/Plugins/su_RobersExcelConvert/Icons
                    add(new Requirement("Icons", "icons", new ArrayList<Requirement>() {{
                        add(new Requirement("Garbage", "garbage.png", null));
                        add(new Requirement("ExcelIcon", "icon.png", null));
                        add(new Requirement("Paintbrush", "paintbrush.png", null));
                    }}));
                    // Pfad: SketchUp/Plugins/su_RobersExcelConvert/classes
                    add(new Requirement("Klassen", "classes", new ArrayList<Requirement>() {{
                        add(new Requirement("element_identifier", "element_identifier.rb", null));
                        add(new Requirement("entityHandler", "entityHandler.rb", null));
                        add(new Requirement("excelReader", "excelReader.rb", null));
                        add(new Requirement("materialHandler", "materialHandler.rb", null));
                        add(new Requirement("rectangle", "rectangle.rb", null));
                        add(new Requirement("Translations", "translations.ini", null));
                    }}));
                    // Pfad: SketchUp/Plugins/su_RobersExcelConvert/textures
                    add(new Requirement("Texturen", "textures", //new ArrayList<Requirement>() {{
                            texturen
//                        add(new Requirement("errorTexture", "errorTexture.jpg", null));
//                        add(new Requirement("texture1", "texture1.jpg", null));
//                        add(new Requirement("texture2", "texture2.jpg", null));
//                        add(new Requirement("texture3", "texture3.jpg", null));
//                        add(new Requirement("texture4", "texture4.jpg", null));
//                        add(new Requirement("texture5", "texture5.jpg", null));
//                        add(new Requirement("texture6", "texture6.jpg", null));
//                    }}));
                    ));
                }}));
            }}
            ));
        }});
    }

    private static ArrayList<Requirement> getMaterialsAsRequirement(ArrayList<String> names) {
        ArrayList<Requirement> texturen = new ArrayList<>();

        texturen.add(new Requirement("errorTexture", "errorTexture.jpg", null));
        for (String name : names) {
            texturen.add(getMaterialAsRequirement(name));
        }

        return texturen;
    }

    private static Requirement getMaterialAsRequirement(String name) {

        return new Requirement(name, name + ".jpg", null);
    }
}
