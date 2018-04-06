import java.awt.*;

import javax.swing.*;

/**
 * a class to represent a material within the RobersExcelConvert-Plugin
 */
public class Material {

    /**
     * the name of the material
     */
    private final String name;
    /**
     * the corresponding icon of the material, which will be shown in the plugin as a texture
     */
    private final ImageIcon icon;

    /**
     * The constructor with a name to the texture. The path of the texture is already determined by the structure.
     * Also the {@link #name} is the {@link #icon} without the file-extension
     *
     * @param name the name of the texture
     */
    public Material(String name) {
        this.name = name;
//        ImageIcon icon = new ImageIcon(Constants.defaultPath + "\\Plugins\\su_RobersExcelConvert\\textures\\" + name + ".jpg");
        ImageIcon icon = new ImageIcon(Constants.texturesPath + "\\" + name + ".jpg");
        Image scaledInstance = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        this.icon = new ImageIcon(scaledInstance);
    }

    /**
     * Getter of the {@link #name}
     *
     * @return the {@link #name} of the material
     */
    public String getName() {
        return name;
    }

    /**
     * Getter of the {@link #icon}
     *
     * @return the {@link #icon} of the material
     */
    public ImageIcon getIcon() {
        return icon;
    }

    /**
     * Returns the {@link #name} as the default toString-method
     *
     * @return the {@link #name} of the material
     */
    public String toString() {
        return name;
    }
}
