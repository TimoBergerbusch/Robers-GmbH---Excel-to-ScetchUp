import java.util.*;

/**
 * A Translation is a mapping of the excel-values 'Länge','Breite' and 'Höhe' to the x, y and z-axis
 */
public class Translation {

    /**
     * the mapping of the excel values to the axis
     */
    private final HashMap<String, String> hashmap;

    /**
     * create a new {@link Translation} with <u>all</u> it's properties
     *
     * @param name    the name
     * @param key     the key
     * @param kuerzel the kuerzel mentioned in the excel file
     * @param bauteil the mentioned in the excel file
     * @param x_achse what gets mapped to the x-axis
     * @param y_achse what gets mapped to the y-axis
     * @param z_achse what gets mapped to the z-axis
     */
    public Translation(String name, String key, String kuerzel, String bauteil, String x_achse, String y_achse, String z_achse) {
        this.hashmap = new HashMap<>();
        this.hashmap.put("Name", name);
        this.hashmap.put("Key", key);
        this.hashmap.put("Kürzel", kuerzel);
        this.hashmap.put("Bauteil", bauteil);
        this.hashmap.put("X-Achse", x_achse);
        this.hashmap.put("Y-Achse", y_achse);
        this.hashmap.put("Z-Achse", z_achse);
    }

    /**
     * get the value of the {@link Translation} by their name
     *
     * @param key the key of the property
     * @return the property value, which might be null
     */
    public String get(String key) {
        return this.hashmap.get(key);
    }

    /**
     * replaces a value within the {@link #hashmap}
     *
     * @param columnName the values name
     * @param aValue     the new value
     */
    public void set(String columnName, Object aValue) {
        hashmap.replace(columnName, aValue + "");
    }


    /**
     * checks wether a given {@link Element Element's} kuerzel and bauteil tuple fit to the translation
     *
     * @param kuerzel the {@link Element Element's} kuerzel
     * @param bauteil the {@link Element Element's} bauteil
     * @return a boolean value
     */
    public boolean fits(String kuerzel, String bauteil) {

        if (kuerzel.equals(this.get("Kürzel")))
            if (this.get("Bauteil").equals(""))
                return true;
            else if (bauteil.indexOf(this.get("Bauteil")) > -1)
                return true;

//        System.out.println(kuerzel + "!=" + this.get("Kürzel") + " and '" + bauteil + "' != '' and index check " + (bauteil.indexOf(this.get("Bauteil")) > -1));
        return false;
    }

    /**
     * returns the value of interest defined by the key
     *
     * @param key    the key to the value of interest
     * @param laenge the laenge-value
     * @param breite the breite-value
     * @param hoehe  the hoehe-value
     * @return the value of interest
     */
    public int transformedValue(String key, int laenge, int breite, int hoehe) {
        String value = this.get(key);
        if (value.equals("Laenge"))
            return laenge;
        if (value.equals("Breite"))
            return breite;
        if (value.equals("Hoehe"))
            return hoehe;
        return -1;
    }



    //PRINT METHODS

    /**
     * creates an Object array of the {@link Translation}
     *
     * @return the data in {@link #hashmap}
     */
    public Object[] getData() {
        return new Object[]{
                this.hashmap.get("Name"),
                this.hashmap.get("Key"),
                this.hashmap.get("Kürzel"),
                this.hashmap.get("Bauteil"),
                this.hashmap.get("X-Achse"),
                this.hashmap.get("Y-Achse"),
                this.hashmap.get("Z-Achse")
        };
    }

    /**
     * the {@link Translation} formalized as a {@link String}
     *
     * @return a {@link String}
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String s : hashmap.keySet()) {
            sb.append("(").append(s).append(": ").append(hashmap.get(s)).append(")").append("\t");
        }

        return sb.toString();
    }

}
