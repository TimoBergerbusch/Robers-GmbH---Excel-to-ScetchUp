import org.ini4j.Ini;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Timo Bergerbusch on 09.04.2018.
 */
public class Element implements Comparable {

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
        String bretter = "-";
        String plankWidth = "-";
        if (werkstoff.contains("Bretter")) {
            bretter = matchingTranslation.get("defaultBrettAusrichtung");
            plankWidth = String.valueOf(View.constantsPanel.constants.get("brettBreite"));
        }
        Object[] obj = new Object[]{
                name, bezeichnung, bauteil, materialgruppe, werkstoff, matchingTranslation.get("Key"), matchingMaterialAssignment.getKey(), this.getOffset(), new Boolean(false), bretter, plankWidth
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

    private static void printAsBretter(Element element, int sectionIndex, Ini ini, String axis, int brettWidth) {
        int numberOfBretter = element.getNumberOfPlanks(axis, brettWidth);

        Element[] bretter = new Element[numberOfBretter];
        int[] sectionNames = new int[numberOfBretter];

        for (int i = 0; i < numberOfBretter; i++) {
//            int breite = plankWidth;
            int breite = brettWidth;

            // calc the width of the missing element before the switch

            int soll = element.matchingTranslation.transformedValue(axis, element.getLaenge(), element.getBreite(), element.getHoehe());
            int ist = (numberOfBretter - 1) * breite;
            if (i == Math.floorDiv(numberOfBretter, 2)) {
                breite = soll - ist;
            }

            Element brett = null;

            int additionalOffset = i * brettWidth;
            if (i > Math.floorDiv(numberOfBretter, 2))
                additionalOffset = (i - 1) * brettWidth + (soll - ist);

            if (axis.equals("X-Achse")) {
                brett = new Element(element.getBezeichnung(), element.getBauteil(), element.getMaterialgruppe(), element.getWerkstoff(), 1,
                        element.getLaenge(), element.getBreite(), element.getHoehe(), element.getOffsetX() + additionalOffset, element.getOffsetY(), element.getOffsetZ());
            } else if (axis.equals("Y-Achse")) {
                brett = new Element(element.getBezeichnung(), element.getBauteil(), element.getMaterialgruppe(), element.getWerkstoff(), 1,
                        element.getLaenge(), element.getBreite(), element.getHoehe(), element.getOffsetX(), element.getOffsetY() + additionalOffset, element.getOffsetZ());
            } else if (axis.equals("Z-Achse")) {
                brett = new Element(element.getBezeichnung(), element.getBauteil(), element.getMaterialgruppe(), element.getWerkstoff(), 1,
                        element.getLaenge(), element.getBreite(), element.getHoehe(), element.getOffsetX(), element.getOffsetY(), element.getOffsetZ() + additionalOffset);
            }
            brett.adjustValue(element.matchingTranslation.get(axis), breite);

            bretter[i] = brett;
            sectionNames[i] = sectionIndex + i;
        }

        bretter = checkForMinModulo(bretter, element.matchingTranslation.get(axis), brettWidth);

        for (int i = 0; i < bretter.length; i++) {
            Element brett = bretter[i];
            int sectionName = sectionNames[i];
            brett.printIntoIniFile(sectionName, ini, false, "-", -1);
        }
    }

    public void printIntoIniFile(int sectionIndex, Ini ini, boolean daneben, String asBretter, int brettWidth) {
        if (asBretter.equals("-")) {
            String sectionName = "Element" + sectionIndex;
            ini.put(sectionName, "name", this.getName());

            Integer xAxisValue = this.getXAxisValue();
            ini.put(sectionName, "x-achse", xAxisValue);
            Integer yAxisValue = this.getYAxisValue();
            ini.put(sectionName, "y-achse", yAxisValue);
            Integer zAxisValue = this.getZAxisValue();
            ini.put(sectionName, "z-achse", zAxisValue);

            MaterialAssignment adjustedAssignment = this.matchingMaterialAssignment.adjustToTranslation(matchingTranslation.get("X-Achse"), matchingTranslation.get("Y-Achse"), matchingTranslation.get("Z-Achse"));

            ini.put(sectionName, "vorne", adjustedAssignment.get("vorne").getName());
            ini.put(sectionName, "hinten", adjustedAssignment.get("hinten").getName());
            ini.put(sectionName, "links", adjustedAssignment.get("links").getName());
            ini.put(sectionName, "rechts", adjustedAssignment.get("rechts").getName());
            ini.put(sectionName, "oben", adjustedAssignment.get("oben").getName());
            ini.put(sectionName, "unten", adjustedAssignment.get("unten").getName());

            if (!daneben) {
                ini.put(sectionName, "offX", (Integer) this.getOffsetX());
                ini.put(sectionName, "offY", (Integer) this.getOffsetY());
                ini.put(sectionName, "offZ", (Integer) this.getOffsetZ());
            } else {
                System.out.println(View.constantsPanel.constants.get("danebenXValue"));
                ini.put(sectionName, "offX", ExcelReadingPanel.danebenXKoord + "");
                ini.put(sectionName, "offY", ExcelReadingPanel.danebenYKoord + "");
                ini.put(sectionName, "offZ", ExcelReadingPanel.danebenZKoord + "");
            }
        } else {
            Element clone = this.clone();

            if (daneben) {
                System.out.println("Bretter daneben");
                clone.offsetX = ExcelReadingPanel.danebenXKoord;
                clone.offsetY = ExcelReadingPanel.danebenYKoord;
                clone.offsetZ = ExcelReadingPanel.danebenZKoord;
            }

            Element.printAsBretter(clone, sectionIndex, ini, asBretter, brettWidth);
        }

    }

    private static Element[] checkForMinModulo(Element[] bretter, String axis, int brettWidth) {
        int minModuloWidth = ConstantsPanel.constants.get("minBrettBreite");
        int m = Math.floorDiv(bretter.length, 2);
        Element elementM = bretter[m];
        if (elementM.getAxisValue(axis) < minModuloWidth) {
//            System.out.println("!!!!!to small!!!!!");
            int missingWidth = minModuloWidth - elementM.getAxisValue(axis);
//            int stealWidth = Math.floorDiv(missingWidth, 2);
            int stealWidth = missingWidth;
            System.out.println("missing width: " + missingWidth);
            System.out.println("steal width: " + stealWidth);
            if (m > 0 && m < bretter.length) {
                bretter[m - 1].adjustValue(axis, brettWidth - stealWidth);
//                bretter[m + 1].adjustValue(axis, brettWidth - stealWidth);
                bretter[m].adjustValue(axis, minModuloWidth);
                bretter[m].adjustOffset(axis, -stealWidth);
//                bretter[m + 1].adjustOffset(axis, stealWidth);
            }
        }
        return bretter;
    }

    public void adjustValue(String key, int value) {
        if (key.equals("Laenge"))
            this.laenge = value;
        else if (key.equals("Hoehe"))
            this.hoehe = value;
        else if (key.equals("Breite"))
            this.breite = value;
        else
            assert false; //adjusting a value not defined
    }

    private void adjustOffset(String axis, int i) {
        String key = this.matchingTranslation.getAxisKeyToValue(axis);
        if (key.equals("X-Achse"))
            this.offsetX += i;
        else if (key.equals("Y-Achse"))
            this.offsetY += i;
        else if (key.equals("Z-Achse"))
            this.offsetZ += i;
        else
            System.out.println("ERROR: ignored offset adjustment");
    }

    public int getNumberOfPlanks(String axisName, int brettWidth) {
        if (axisName.equals("X-Achse")) {
//            System.out.println(this.getXAxisValue() + "/" + 100 + "=" + Math.ceil((double) this.getXAxisValue() / (double) brettWidth));
            return (int) Math.ceil((double) this.getXAxisValue() / (double) brettWidth);
        } else if (axisName.equals("Y-Achse")) {
//            System.out.println(this.getYAxisValue() + "/" + 100 + "=" + Math.ceil((double) this.getYAxisValue() / (double) brettWidth));
            return (int) Math.ceil((double) this.getYAxisValue() / (double) brettWidth);
        } else if (axisName.equals("Z-Achse")) {
            return (int) Math.ceil((double) this.getZAxisValue() / (double) brettWidth);
        }

        return 0;
    }

    public Integer getAxisValue(String axis) {
        if (matchingTranslation.getAxisKeyToValue(axis).equals("X-Achse"))
            return this.getXAxisValue();
        if (matchingTranslation.getAxisKeyToValue(axis).equals("Y-Achse"))
            return this.getYAxisValue();
        if (matchingTranslation.getAxisKeyToValue(axis).equals("Z-Achse"))
            return this.getZAxisValue();

        System.out.println("Problem with axis = " + axis);
        return null;
    }

    public Integer getXAxisValue() {
        return (Integer) this.getMatchingTranslation().transformedValue("X-Achse", this.getLaenge(), this.getBreite(), this.getHoehe());
    }

    public Integer getYAxisValue() {
        return (Integer) this.getMatchingTranslation().transformedValue("Y-Achse", this.getLaenge(), this.getBreite(), this.getHoehe());
    }

    public Integer getZAxisValue() {
        return (Integer) this.getMatchingTranslation().transformedValue("Z-Achse", this.getLaenge(), this.getBreite(), this.getHoehe());
    }

    public Element clone() {
        Element clone = new Element(this.bezeichnung, this.bauteil, this.materialgruppe, this.werkstoff, this.anzahl, this.laenge, this.breite, this.hoehe, this.offsetX, this.offsetY, this.offsetZ);
        return clone;
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

    public String getBezeichnung() {
        return bezeichnung;
    }

    public String getBauteil() {
        return bauteil;
    }

    public String getMaterialgruppe() {
        return materialgruppe;
    }

    public String getWerkstoff() {
        return werkstoff;
    }

    @Override
    public int compareTo(Object o) {
        Element other = (Element) o;


        if (other.name.equals(this.name)
                && other.bezeichnung.equals(this.bezeichnung)
                && other.bauteil.equals(this.bauteil)
                && other.materialgruppe.equals(this.materialgruppe)
                && other.werkstoff.equals(werkstoff))
            return 0;
        else
            return 1;
    }


}
