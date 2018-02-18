import java.util.HashMap;

/**
 * Created by Timo Bergerbusch on 18.02.2018.
 */
public class MaterialAssignment {

    private String name, key;
    private HashMap<String, Material> hashMap;

    public MaterialAssignment(String name, String key, Material material) {
        this(name, key, material, material, material, material, material, material);
    }

    public MaterialAssignment(String name, String key, Material topBottom, Material rest) {
        this(name, key, rest, rest, rest, rest, topBottom, topBottom);
    }

    public MaterialAssignment(String name, String key, Material vorne, Material hinten, Material links, Material rechts, Material oben, Material unten) {
        this(name, key, new HashMap<String, Material>() {{
            put("vorne", vorne);
            put("hinten", hinten);
            put("links", links);
            put("rechts", rechts);
            put("oben", oben);
            put("unten", unten);
        }});
    }

    public MaterialAssignment(String name, String key, HashMap<String, Material> hashMap) {
        this.name = name;
        this.key = key;
        this.hashMap = hashMap;
    }

    public Material get(String key) {
        return hashMap.get(key);
    }

    public Object[] getData() {
        return new Object[]{name, key,
                hashMap.get("vorne").getIcon(),
                hashMap.get("hinten").getIcon(),
                hashMap.get("links").getIcon(),
                hashMap.get("rechts").getIcon(),
                hashMap.get("oben").getIcon(),
                hashMap.get("unten").getIcon(),
        };
    }
}
