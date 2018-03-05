import javax.swing.*;
import java.awt.*;

/**
 * Created by Timo Bergerbusch on 18.02.2018.
 */
public class Material {

    private final String name;
    private final ImageIcon icon;

    public Material(String name) {
        this.name = name;
        ImageIcon icon = new ImageIcon(Constants.defaultPath + "\\su_RobersExcelConvert\\textures\\" + name + ".jpg");
        Image scaledInstance = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        this.icon = new ImageIcon(scaledInstance);
    }

    public String getName() {
        return name;
    }


    public ImageIcon getIcon() {
        return icon;
    }


    public String toString() {
        return name;
    }
}
