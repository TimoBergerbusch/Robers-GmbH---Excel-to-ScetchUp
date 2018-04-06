import org.ini4j.*;

import java.awt.*;
import java.io.*;
import java.util.*;

/**
 * The constants used within the whole Project
 */
class Constants {

    /**
     * current version of the programm
     */
    public static final String Version = "0.3";

    // Colors
    /**
     * default color of a positive result
     */
    public static final Color goodColor = new Color(0, 155, 0);
    /**
     * default color of a value, which is currently edited
     */
    public static final Color editingColor = Color.red;
    /**
     * default color of a result which has a inherited element having the {@link #partNotLoaded}
     */
    public static final Color partNotLoaded = Color.orange;
    /**
     * default color of a Requirement that could not be loaded
     */
    public static final Color notLoadedColor = Color.red;
    /**
     * default color of a Requirement that has not been tested yet
     */
    public static final Color neutral = Color.lightGray;

    //Paths
    /**
     * the default path the program tries to locate the Plugins-folder
     */
    public static String defaultPath = System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins";
    /**
     * the path to the designated path of the textures relative of the defaultPath
     */
    public static String texturesPath = defaultPath + "\\su_RobersExcelConvert\\textures";
    /**
     * the name of the section within the constants.ini-file
     */
    public static String excelConstantSectionName = "RobersExcelConstants";
    /**
     * the path of the constants.ini-file realtive to the {@link #defaultPath}
     */
    public static String excelConstantsPath = defaultPath + "\\su_RobersExcelConvert\\classes\\constants.ini";
    /**
     * the default elements within the constants.ini-file, which should be editable
     */
    public static String[] excelConstants = new String[]{"headerRow", "Lfd", "SageArt", "Bezeichnung", "Bauteil", "Materialgruppe", "Werkstoff", "Anzahl", "Laenge", "Breite", "Hoehe"};
    /**
     * the path of the materials.ini relative to the {@link #defaultPath}
     */
    public static String materialsPath = defaultPath + "\\su_RobersExcelConvert\\classes\\materials.ini";

    public static String translationsPath = defaultPath + "\\su_RobersExcelConvert\\classes\\translations.ini";

    //Files
    public static File materialFile;
    public static File translationsFile;
    public static File constantsFile;

    //Others
    /**
     * the error material. This material is used within the program if a mentioned material could not be found
     */
    public static Material errorMaterial;

    /**
     * the names of the sides of an element
     */
    public static ArrayList<String> sideNames = new ArrayList<String>() {{
        add("vorne");
        add("hinten");
        add("links");
        add("rechts");
        add("oben");
        add("unten");
    }};

    /**
     * the base requirement, which inherits the subfolders, inifiles and materials
     */
    public static Requirement requirement = initRequirement();

    /**
     * creates all the requirements the plugins-path should contain
     *
     * @return a Requirement with inherited further Requirements
     */
    private static Requirement initRequirement() {

        ArrayList<String> names = getMaterialNames();
        final ArrayList<Requirement> texturen = getMaterialsAsRequirement(names);

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
                    add(new Requirement("Texturen", "textures",
                            texturen
                    ));
                }}));
            }}
            ));
        }});
    }

    /**
     * Reads all the materials mentioned in the "materials.ini"
     *
     * @return an ArrayList of all the materialnames
     */
    private static ArrayList<String> getMaterialNames() {
        ArrayList<String> names = new ArrayList<String>();
        File file = new File(materialsPath);
        if (file.exists())
            try {
                Ini ini = new Ini(file);
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
        else
            System.out.println("Die Materials-Ini kann nicht gelesen werden");

        return names;
    }

    /**
     * creates an ArrayList of all materials mentioned in names as a requirement using {#getMaterialAsRequirement}
     *
     * @param names the names of all materials
     * @return an ArrayList of Materials
     */
    private static ArrayList<Requirement> getMaterialsAsRequirement(ArrayList<String> names) {
        ArrayList<Requirement> texturen = new ArrayList<>();

        texturen.add(new Requirement("errorTexture", "errorTexture.jpg", null));
        for (String name : names) {
            texturen.add(getMaterialAsRequirement(name));
        }

        return texturen;
    }

    /**
     * creates a Requirement of a single material
     *
     * @param name the name of the material
     * @return the material as a requirement
     */
    private static Requirement getMaterialAsRequirement(String name) {

        return new Requirement(name, name + ".jpg", null);
    }

    public static void reloadPaths(){
        texturesPath = defaultPath + "\\su_RobersExcelConvert\\textures";

        excelConstantSectionName = "RobersExcelConstants";

        excelConstantsPath = defaultPath + "\\su_RobersExcelConvert\\classes\\constants.ini";

        materialsPath = defaultPath + "\\su_RobersExcelConvert\\classes\\materials.ini";

        translationsPath = defaultPath + "\\su_RobersExcelConvert\\classes\\translations.ini";

        Constants.reloadFiles();
    }

    public static void reloadFiles() {
        materialFile = new File(materialsPath);
        translationsFile = new File(translationsPath);
        constantsFile = new File(excelConstantsPath);
    }
}
