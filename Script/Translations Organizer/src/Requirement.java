import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 * A Requirement-class to represent a folder, script or a texture which should be contained within the installation folder
 */
public class Requirement {

    /**
     * the path of the {@link Requirement} relative to it's parent {@link Requirement}
     */
    private final String path;
    /**
     * the name of the {@link Requirement}
     */
    private final String name;
    /**
     * {@link Requirement Requirements} that are part of this {@link Requirement}
     * Example: textures folder and textures
     */
    private final ArrayList<Requirement> subRequirements;

    /**
     * the {@link JLabel} representing the {@link Requirement}
     */
    private JLabel lbl;

    /**
     * the constructor of the {@link Requirement}
     *
     * @param name            the {@link #name}
     * @param path            the {@link #path}
     * @param subRequirements the {@link #subRequirements}
     */
    public Requirement(String name, String path, ArrayList<Requirement> subRequirements) {
        this.name = name;
        this.path = path;
        this.subRequirements = subRequirements;
    }

    /**
     * test, weather the {@link Requirement} exists at the expected path
     *
     * @param parentPath the path of the parent-{@link Requirement}
     * @return a boolean to indicate the existence
     */
    private boolean exists(String parentPath) {
        if (path.equals(""))
            return true;
        File f = new File(parentPath + "\\" + path);
        return f.exists();
    }

    /**
     * test weather the {@link Requirement} and <u>all</u> {@link #subRequirements} are met via {@link #exists(String)}
     * This also sets the color for the {@link #lbl}
     *
     * @param parentPath the path of the parent-{@link Requirement}
     * @return a boolean indicating if everything could be loaded as it should
     */
    public boolean test(String parentPath) {
        System.out.println("Testing:" + name);
        if (subRequirements != null) {
            for (Requirement requirement : subRequirements) {
                requirement.reset();
                if (!requirement.test(parentPath + "\\" + path)) {
                    System.out.println("+++++++++ Problem mit: " + requirement.toString());
                    this.lbl.setBackground(Constants.partNotLoaded);
                }
            }
        }

        boolean isThisLoaded = exists(parentPath);
        if (!isThisLoaded)
            this.lbl.setBackground(Constants.notLoadedColor);
        else if (this.lbl.getBackground() != Constants.partNotLoaded)
            this.lbl.setBackground(Constants.goodColor);

        return isThisLoaded && this.lbl.getBackground() != Constants.partNotLoaded;
    }

    /**
     * resets the backgroundcolor of {@link #lbl} using {@link Constants#neutral}
     */
    private void reset() {
        this.lbl.setBackground(Constants.neutral);
    }

    /**
     * creates the structure to represent the Requirements as an intuitive order
     *
     * @param list   the current list of {@link Tuple Tuples}
     * @param indent the current indent level
     * @return the list of {@link Tuple Tuples} after adding the current and all it's sub-components
     */
    public ArrayList<Tuple> getStructure(ArrayList<Tuple> list, int indent) {
        Tuple pair = new Tuple(indent, this);
        list.add(pair);

        if (subRequirements != null)
            for (Requirement requirement : subRequirements)
                list = requirement.getStructure(list, indent + 1);

        return list;
    }

    //GETTER AND SETTER
    public void setLabel(JLabel label) {
        this.lbl = label;
    }

    public String getName() {
        return name;
    }

    //PRINT METHODS

    /**
     * the String representation of the {@link Requirement}
     *
     * @return the {@link Requirement} as String
     */
    public String toString() {
        return "Name: " + name +
                "\t" +
                "Path: " + path;
    }

}
