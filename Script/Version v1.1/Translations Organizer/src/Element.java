import org.ini4j.Ini;

/**
 * Created by Timo Bergerbusch on 09.04.2018.
 */
public class Element {

    private String name, bezeichnung, bauteil, materialgruppe, werkstoff;
    private int anzahl, laenge, breite, hoehe;
    private int offsetX, offsetY, offsetZ;

    private Translation matchingTranslation;
    private MaterialAssignment matchingMaterialAssignment;

    public Element(String bezeichnung, String bauteil, String materialgruppe, String werkstoff, int anzahl, int laenge, int breite, int hoehe, int offsetX, int offsetY, int offsetZ) {
        this.name = bezeichnung + ": " + bauteil;
        this.bezeichnung = bezeichnung;
        this.bauteil = bauteil;
        this.materialgruppe = materialgruppe;
        this.werkstoff = werkstoff;
        this.anzahl = anzahl;
        this.laenge = laenge;
        this.breite = breite;
        this.hoehe = hoehe;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;

        this.identifyTranslation();
        this.identifyMaterialAssignment();
    }

    public void identifyTranslation() {
        matchingTranslation = Identifier.getIdentifier().findMatchingTranslation(bezeichnung, bauteil);
    }

    public void identifyMaterialAssignment() {
        matchingMaterialAssignment = Identifier.getIdentifier().findMatchingMaterialAssignment(materialgruppe, werkstoff);
//        System.out.println("the new matching MA is " + matchingMaterialAssignment);
    }

    public Object[] getDataAsRow() {
        Object[] obj = new Object[]{
                name, bezeichnung, bauteil, materialgruppe, werkstoff, matchingTranslation.get("Key"), matchingMaterialAssignment.getKey(), this.getOffset(), new Boolean(false)
        };
        return obj;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bezeichnung:").append(bezeichnung).append("\t");
        sb.append("Bauteil:").append(bauteil).append("\t");
        sb.append("Materialgruppe:").append(materialgruppe).append("\t");
        sb.append("Werkstoff:").append(werkstoff).append("\t");
        sb.append("Anzahl:").append(anzahl).append("\t");
        sb.append("Laenge:").append(laenge).append("\t");
        sb.append("Breite:").append(breite).append("\t");
        sb.append("Hoehe:").append(hoehe).append("\t");
        sb.append("Translation:").append(matchingTranslation).append("\t");
        sb.append("MaterialAssignment:").append(matchingMaterialAssignment).append("\t");
        sb.append("Offset:").append("(").append(offsetX).append(",").append(offsetY).append(",").append(offsetZ).append(")");

        return sb.toString();
    }

    //GETTER AND SETTER
    public int getLaenge() {
        return laenge;
    }

    public int getBreite() {
        return breite;
    }

    public int getHoehe() {
        return hoehe;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public int getOffsetZ() {
        return offsetZ;
    }

    public Translation getMatchingTranslation() {
        return matchingTranslation;
    }

    public MaterialAssignment getMatchingMaterialAssignment() {
        return matchingMaterialAssignment;
    }

    public String getName() {
        return name;
    }

    public Object getOffset() {
        return "(" + offsetX + "," + offsetY + "," + offsetZ + ")";
    }

    public void setOffset(String offset) {
        offset = offset.split("[(]")[1];
        offset = offset.split("[)]")[0];
        String[] split = offset.split("[,]");
        this.offsetX = Integer.parseInt(split[0]);
        this.offsetY = Integer.parseInt(split[1]);
        this.offsetZ = Integer.parseInt(split[2]);
        System.out.println(offsetX);
        System.out.println(offsetY);
        System.out.println(offsetZ);
    }

    public void printIntoIniFile(String sectionName, Ini ini, boolean daneben) {
        ini.put(sectionName, "name", this.getName());
        ini.put(sectionName, "vorne", this.getMatchingMaterialAssignment().get("vorne").getName());
        ini.put(sectionName, "hinten", this.getMatchingMaterialAssignment().get("hinten").getName());
        ini.put(sectionName, "links", this.getMatchingMaterialAssignment().get("links").getName());
        ini.put(sectionName, "rechts", this.getMatchingMaterialAssignment().get("rechts").getName());
        ini.put(sectionName, "oben", this.getMatchingMaterialAssignment().get("oben").getName());
        ini.put(sectionName, "unten", this.getMatchingMaterialAssignment().get("unten").getName());
        ini.put(sectionName, "x-achse", (Integer) this.getMatchingTranslation().transformedValue("X-Achse", this.getLaenge(), this.getBreite(), this.getHoehe()));
        Integer yAxis = (Integer) this.getMatchingTranslation().transformedValue("Y-Achse", this.getLaenge(), this.getBreite(), this.getHoehe());
        ini.put(sectionName, "y-achse", yAxis);
        ini.put(sectionName, "z-achse", (Integer) this.getMatchingTranslation().transformedValue("Z-Achse", this.getLaenge(), this.getBreite(), this.getHoehe()));
        if (!daneben) {
            ini.put(sectionName, "offX", (Integer) this.getOffsetX());
            ini.put(sectionName, "offY", (Integer) this.getOffsetY());
            ini.put(sectionName, "offZ", (Integer) this.getOffsetZ());
        } else {
            System.out.println(View.constantsPanel.constants.get("danebenXValue"));
            ini.put(sectionName, "offX", View.constantsPanel.constants.get("danebenXValue") + "");
            ini.put(sectionName, "offY", ExcelReadingPanel.danebenYKoord + "");
            ini.put(sectionName, "offZ", View.constantsPanel.constants.get("danebenZValue") + "");

            ExcelReadingPanel.danebenYKoord += 25 + yAxis + 25;
        }

    }

    public int getAnzahl() {
        return anzahl;
    }
}
