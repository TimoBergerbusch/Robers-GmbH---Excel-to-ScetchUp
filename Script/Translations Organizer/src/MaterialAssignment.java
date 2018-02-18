import java.util.ArrayList;
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
            put("Vorne", vorne);
            put("Hinten", hinten);
            put("Links", links);
            put("Rechts", rechts);
            put("Oben", oben);
            put("Unten", unten);
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

    public void updateMaterial(String key, Material material) {
        hashMap.replace(key, material);
        System.out.println("New Material for " + name + " at key " + key + " is " + hashMap.get(key));
    }

    public Object[] getData() {
//        ArrayList list = new ArrayList();
//        list.add(name);
//        list.add(key);
//        for(String s: hashMap.keySet())
//            list.add(hashMap.get(s).getIcon());
//
//        return list.toArray();

        return new Object[]{name, key,
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

    public HashMap<String, Material> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Material> hashMap) {
        this.hashMap = hashMap;
    }
}
