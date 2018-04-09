import java.util.*;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class Translation {

    private final HashMap<String, String> hashmap;

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

    public String get(String key) {
        return this.hashmap.get(key);
    }

    public void set(String columnName, Object aValue) {
        hashmap.replace(columnName, aValue + "");
    }

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

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String s : hashmap.keySet()) {
            sb.append("(").append(s).append(": ").append(hashmap.get(s)).append(")").append("\t");
        }

        return sb.toString();
    }

    public boolean fits(String kuerzel, String bauteil) {

        if (kuerzel.equals(this.get("Kürzel")))
            if (this.get("Bauteil").equals(""))
                return true;
            else if (bauteil.indexOf(this.get("Bauteil")) > -1)
                return true;

//        System.out.println(kuerzel + "!=" + this.get("Kürzel") + " and '" + bauteil + "' != '' and index check " + (bauteil.indexOf(this.get("Bauteil")) > -1));
        return false;
    }

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
}
