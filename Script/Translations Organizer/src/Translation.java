import java.util.HashMap;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class Translation {

    private HashMap<String, String> hashmap;

    public Translation(String name, String key, String kuerzel, String bauteil, String x_achse, String y_achse, String z_achse) {
        this.hashmap = new HashMap<String, String>();
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
            sb.append("(" + s + ": " + hashmap.get(s) + ")").append("\t");
        }

        return sb.toString();
    }

}
