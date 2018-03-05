import javax.swing.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Timo Bergerbusch on 17.02.2018.
 */
public class Requirement {

    private final String path;
    private final String name;
    private final ArrayList<Requirement> subRequirements;
    private JLabel lbl;

    public Requirement(String name, String path, ArrayList<Requirement> subRequirements) {
        this.name = name;
        this.path = path;
        this.subRequirements = subRequirements;
    }

    private boolean exists(String parentPath) {
        if (path.equals(""))
            return true;
        File f = new File(parentPath + "\\" + path);
        return f.exists();
    }

    public boolean test(String parentPath) {
        System.out.println("Testing:" + name);
        if (subRequirements != null) {
            for (Requirement requirement : subRequirements) {
                requirement.reset();
                if (!requirement.test(parentPath + "\\" + path)) {
                    System.out.println("+++++++++ Problem mit: " + requirement.toString());
                    this.lbl.setBackground(Constants.partNotLoaded);
//                    return false;
                }
            }
//            this.lbl.setBackground(Constants.goodColor);
        }

        boolean isThisLoaded = exists(parentPath);
        if (!isThisLoaded)
            this.lbl.setBackground(Constants.notLoadedColor);
        else if (this.lbl.getBackground() != Constants.partNotLoaded)
            this.lbl.setBackground(Constants.goodColor);

        return isThisLoaded && this.lbl.getBackground() != Constants.partNotLoaded;
    }

    private void reset() {
        this.lbl.setBackground(Constants.neutral);
    }

    public String toString() {
        return "Name: " + name +
                "\t" +
                "Path: " + path;
    }

    public ArrayList<Tuple> getStructure(ArrayList<Tuple> list, int indent) {
        Tuple pair = new Tuple(indent, this);
        list.add(pair);

        if (subRequirements != null)
            for (Requirement requirement : subRequirements)
                list = requirement.getStructure(list, indent + 1);

        return list;
    }

    public void setLabel(JLabel label) {
        this.lbl = label;
    }

    public String getName() {
        return name;
    }

}
