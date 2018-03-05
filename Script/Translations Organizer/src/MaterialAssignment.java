import java.util.HashMap;

/**
 * Created by Timo Bergerbusch on 18.02.2018.
 */
public class MaterialAssignment {

    private String name;
    private String key;
    private final String werkstoff;
    private final String materialgruppe;
    private final HashMap<String, Material> hashMap;

    public MaterialAssignment(String name, String key, String werkstoff, String materialgruppe, Material material) {
        this(name, key, werkstoff, materialgruppe, material, material, material, material, material, material);
    }

    public MaterialAssignment(String name, String key, String werkstoff, String materialgruppe, Material vorne, Material hinten, Material links, Material rechts, Material oben, Material unten) {
        this(name, key, werkstoff, materialgruppe, new HashMap<String, Material>() {{
            put("vorne", vorne);
            put("hinten", hinten);
            put("links", links);
            put("rechts", rechts);
            put("oben", oben);
            put("unten", unten);
        }});
    }

    private MaterialAssignment(String name, String key, String werkstoff, String materialgruppe, HashMap<String, Material> hashMap) {
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
//        hashMap.replace(key, material);
        hashMap.put(key.toLowerCase(), material);
        System.out.println("New Material for " + name + " at key " + key + " is " + hashMap.get(key.toLowerCase()));
    }

    public Object[] getData() {
        return new Object[]{name, key, materialgruppe, werkstoff,
                hashMap.get("vorne").getIcon(),
                hashMap.get("hinten").getIcon(),
                hashMap.get("links").getIcon(),
                hashMap.get("rechts").getIcon(),
                hashMap.get("oben").getIcon(),
                hashMap.get("unten").getIcon()
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

    public String getMaterialgruppe() {
        return materialgruppe;
    }

    public static String[] getTableHeader() {
        return new String[]{"Name", "Key", "Materialgruppe", "Werkstoff", "Vorne", "Hinten", "Links", "Rechts", "Oben", "Unten"};
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(Name:").append(name).append(")");
        sb.append("(Key:").append(key).append(")");
        sb.append("(Werkstoff:").append(werkstoff).append(")");
        sb.append("(Materialgruppe:").append(materialgruppe).append(")");
        for (String s : hashMap.keySet())
            sb.append("(").append(s).append(":").append(hashMap.get(s).toString()).append(")");

        return sb.toString();
    }
}
