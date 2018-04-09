import java.util.*;

/**
 * The assignment of 6 materials to an element
 */
public class MaterialAssignment {

    /**
     * the name of the material assignment
     */
    private String name;
    /**
     * the key of the material assignment <br>
     * the key is used within the ini-file as the section name. Therefore it is very important, that these keys are unique
     * Example:
     * <table>
     * <tr>
     * <th>name</th>
     * <th>key</th>
     * </tr>
     * <tr>
     * <td>OSB3 Platten</td>
     * <td>OSB3PL</td>
     * </tr>
     * <tr>
     * <td>Bretter</td>
     * <td>HOBretter</td>
     * </tr>
     * </table>
     */
    private String key;

    /**
     * the werkstoff is the substring that is necessary to be contained within the equally named column within the excel.
     */
    private final String werkstoff;
    /**
     * the materialgruppe defines the shortened name.
     * the materialgruppe is the first step of identifying
     * Example: PL -> OSB3 Platten
     */
    private final String materialgruppe;

    /**
     * the map that contains all the materials as values with the side of the element as the key.
     * <p>
     * <table>
     * <tr>
     * <th>key</th>
     * <th>value</th>
     * </tr>
     * <tr>
     * <td>vorne</td>
     * <td>material1</td>
     * </tr>
     * <tr>
     * <td>hinten</td>
     * <td>material2</td>
     * </tr>
     * <tr>
     * <td>...</td>
     * <td>...</td>
     * </tr>
     * </table>
     * Overall there are the keys: vorne, hinten, links, rechts, oben, unten
     */
    private final HashMap<String, Material> hashMap;

    /**
     * a constructor of a {@link MaterialAssignment}, with only one material.
     * This uses the constructor {@link #MaterialAssignment(String, String, String, String, Material, Material, Material,
     * Material, Material, Material)}, where the given material is used as the material of every side
     *
     * @param name           the {@link #name}
     * @param key            the {@link #key}
     * @param werkstoff      the {@link #werkstoff}
     * @param materialgruppe the {@link #materialgruppe}
     * @param material       the <u>only</u> material
     */
    public MaterialAssignment(String name, String key, String werkstoff, String materialgruppe, Material material) {
        this(name, key, werkstoff, materialgruppe, material, material, material, material, material, material);
    }

    /**
     * a constructor of a {@link MaterialAssignment} with a material for every side. Those get set into the
     * {@link #hashMap} and the constructor {@link #MaterialAssignment(String, String, String, String, HashMap)}
     *
     * @param name           the {@link #name}
     * @param key            the {@link #key}
     * @param werkstoff      the {@link #werkstoff}
     * @param materialgruppe the {@link #materialgruppe}
     * @param vorne          the {@link Material} of the side called "vorne"
     * @param hinten         the {@link Material} of the side called "hinten"
     * @param links          the {@link Material} of the side called "links"
     * @param rechts         the {@link Material} of the side called "rechts"
     * @param oben           the {@link Material} of the side called "oben"
     * @param unten          the {@link Material} of the side called "unten"
     */
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

    /**
     * a private constructor of a {@link MaterialAssignment} with a precomputed {@link HashMap}
     *
     * @param name           the {@link #name}
     * @param key            the {@link #key}
     * @param werkstoff      the {@link #werkstoff}
     * @param materialgruppe the {@link #materialgruppe}
     * @param hashMap        the {@link #hashMap}
     */
    private MaterialAssignment(String name, String key, String werkstoff, String materialgruppe, HashMap<String, Material> hashMap) {
        this.name = name;
        this.key = key;
        this.werkstoff = werkstoff;
        this.materialgruppe = materialgruppe;
        this.hashMap = hashMap;
    }

    /**
     * retriev the {@link Material} corresponding to the entered key
     *
     * @param key the side one wants the  {@link Material} from
     * @return a {@link Material} if key is valid. Otherwise the {@link Constants#errorMaterial}
     */
    public Material get(String key) {
        Material m = hashMap.get(key.toLowerCase());
        if (m != null)
            return m;
        else
            return Constants.errorMaterial;
    }

    /**
     * update the {@link Material} within the {@link #hashMap}
     *
     * @param key      the key of the side
     * @param material the new {@link Material}
     */
    public void updateMaterial(String key, Material material) {
        key = key.toLowerCase();
        if (Constants.sideNames.contains(key)) {
            hashMap.put(key, material);
            System.out.println("New Material for " + name + " at key " + key + " is " + hashMap.get(key));
        } else
            System.out.println("The key=" + key + " is not a valid side.");
    }

    /**
     * returns all the columns a table should have in order to represent a {@link MaterialAssignment}
     *
     * @return
     */
    public static String[] getTableHeader() {
        return new String[]{"Name", "Key", "Materialgruppe", "Werkstoff", "Vorne", "Hinten", "Links", "Rechts", "Oben", "Unten"};
    }

    public boolean fits(String materialgruppe, String werkstoff) {
        if (materialgruppe.equals(this.materialgruppe))
            if (this.werkstoff.equals("") || werkstoff.indexOf(this.werkstoff) > -1)
                return true;
        return false;
    }

    //GETTER AND SETTER

    /**
     * get the {@link #name},{@link #key}, {@link #materialgruppe},{@link #werkstoff} and {@link Material Materials}
     * as an array of Objects in order to add them to a table
     * NOTE: change order to the {@link #getTableHeader()}
     *
     * @return an Object-array in the mentioned order
     */
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

    // PRINT-METHODS

    /**
     * represents the {@link MaterialAssignment} as a String with the order:
     * {@link #name}, {@link #key},{@link #werkstoff},{@link #materialgruppe} and then {@link Material} per side
     * The representation is: <code>(Key:value)</code>
     *
     * @return the String in the mentioned format
     */
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

    public Set<Material> getDistinctMaterials() {
        Set<Material> s = new TreeSet<>();
        s.add(this.get("vorne"));
        s.add(this.get("hinten"));
        s.add(this.get("links"));
        s.add(this.get("rechts"));
        s.add(this.get("oben"));
        s.add(this.get("unten"));
        return s;
    }
}
