import com.sun.org.apache.xpath.internal.SourceTree;

/**
 * Created by Timo Bergerbusch on 09.04.2018.
 */
public class Identifier {

    private static Identifier identifier;

    private View view;
    private Translation defaultTranslation;
    private MaterialAssignment defaultMaterialAssignment;

    public static Identifier getIdentifier() {
        if (identifier == null)
            identifier = new Identifier();
        return identifier;
    }

    public Identifier() {
        defaultTranslation = new Translation("Default Translation", "Default", "Irrelevant", "Irrelevant", "Laenge", "Breite", "Hoehe");
        defaultMaterialAssignment = new MaterialAssignment("Default MaterialAssignment", "Default", "Irrelevant", "Irrelevant", Constants.errorMaterial);
        this.view = view;
    }

    public Translation findMatchingTranslation(String kuerzel, String bauteil) {
        Translation[] translations = View.translationsPanel.translations;

        for (Translation translation : translations) {
            if (translation.fits(kuerzel, bauteil))
                return translation;
        }
        System.out.println("WARNING: No Translations fits for [Kuerzel: " + kuerzel + ", Bauteil: " + bauteil + "]. Take default Translation");
        return defaultTranslation;
    }

    public MaterialAssignment findMatchingMaterialAssignment(String materialgruppe, String werkstoff) {
        MaterialAssignment[] assignments = View.materialsPanel.materialAssignments;

        for (MaterialAssignment assignment : assignments) {
            if (assignment.fits(materialgruppe, werkstoff))
                return assignment;
        }

        System.out.println("WARNING: No MaterialAssignment fits. Take default MaterialAssignment");
        return defaultMaterialAssignment;
    }

    //GETTER AND SETTER

    public Translation getDefaultTranslation() {
        return defaultTranslation;
    }

    public MaterialAssignment getDefaultMaterialAssignment() {
        return defaultMaterialAssignment;
    }
}
