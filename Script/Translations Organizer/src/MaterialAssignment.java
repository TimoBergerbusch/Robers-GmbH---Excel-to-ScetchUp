import java.util.HashMap;

/**
 * Created by Timo Bergerbusch on 18.02.2018.
 */
public class MaterialAssignment {

    private String name, key, werkstoff, materialgruppe;
    private HashMap<String, Material> hashMap;

    public MaterialAssignment(String name, String key, String werkstoff, String materialgruppe, Material material) {
        this(name, key, werkstoff, materialgruppe, material, material, material, material, material, material);
    }

    public MaterialAssignment(String name, String key, String werkstoff, String materialgruppe, Material vorne, Material hinten, Material links, Material rechts, Material oben, Material unten) {
        this(name, key, werkstoff, materialgruppe, new HashMap<String, Material>() {{
            put("Vorne", vorne);
            put("Hinten", hinten);
            put("Links", links);
            put("Rechts", rechts);
            put("Oben", oben);
            put("Unten", unten);
        }});
    }

    public MaterialAssignment(String name, String key, String werkstoff, String materialgruppe, HashMap<String, Material> hashMap) {
        this.name = name;
        this.key = key;
        this.werkstoff = werkstoff;
        this.materialgruppe = materialgruppe;
        this.hashMap = hashMap;
    }

    public Material get(String key) {
        return hashMap.get(key.toLowerCase());
    }

    public void updateMaterial(String key, Material material) {
        hashMap.replace(key, material);
        System.out.println("New Material for " + name + " at key " + key + " is " + hashMap.get(key));
    }

    public Object[] getData() {
        return new Object[]{name, key, werkstoff, materialgruppe,
                hashMap.get("Oben").getIcon(),
                hashMap.get("Unten").getIcon(),
                hashMap.get("Links").getIcon(),
                hashMap.get("Rechts").getIcon(),
                hashMap.get("Vorne").getIcon(),
                hashMap.get("Hinten").getIcon()
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getWerkstoff() {
        return werkstoff;
    }

    public HashMap<String, Material> getHashMap() {
        return hashMap;
    }

    public String getMaterialgruppe() {
        return materialgruppe;
    }

    public static String[] getTableHeader() {
        return new String[]{"Name", "Key", "Werkstoff", "Materialgruppe", "Vorne", "Hinten", "Links", "Rechts", "Oben", "Unten"};
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(Name:" + name + ")");
        sb.append("(Key:" + key + ")");
        sb.append("(Werkstoff:" + werkstoff + ")");
        sb.append("(Materialgruppe:" + materialgruppe + ")");
        for (String s : hashMap.keySet())
            sb.append("(Name:" + hashMap.get(s).toString() + ")");

        return sb.toString();
    }
}
