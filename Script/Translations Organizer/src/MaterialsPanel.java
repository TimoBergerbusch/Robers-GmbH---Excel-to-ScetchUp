import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * the {@link MaterialsPanel} is used to list the different, create new and change the order of {@link MaterialAssignment MaterialAssignments}.
 */
class MaterialsPanel extends JPanel {

    /**
     * the {@link JTable} listing all the {@link MaterialAssignment MaterialAssignments}
     */
    private final JTable table;
    /**
     * the {@link javax.swing.table.TableModel} of the {@link #table}
     */
    private final DefaultTableModel model;

    /**
     * the {@link Material Materials} used within the {@link MaterialAssignment MaterialAssignments}
     */
    private Material[] materials;
    /**
     * the {@link MaterialAssignment MaterialAssignments} which are managed wihtin this panel
     */
    public MaterialAssignment[] materialAssignments;

    /**
     * the constructor to create a new {@link MaterialsPanel}.
     * It creates and loads the table, creates the {@link MaterialsMovePanel} and {@link MaterialsSaveAndAddPanel}
     */
    public MaterialsPanel() {
        this.setLayout(new BorderLayout());

        this.loadMaterials();

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return true;
            }

            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        model.setColumnIdentifiers(MaterialAssignment.getTableHeader());
        table = new JTable(model);
        table.setRowHeight(50);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2)
                    System.out.println("Double click row: " + row);
            }
        });
        model.addTableModelListener(e -> {
            if (e.getFirstRow() != e.getLastRow() || e.getColumn() == -1)
                return;

            int row = e.getFirstRow();
            int column = e.getColumn();
            String columnName = table.getColumnName(column);

            if (table.getValueAt(row, column).getClass() == ImageIcon.class) {
                ImageIcon icon = (ImageIcon) table.getValueAt(row, column);

                materialAssignments[row].updateMaterial(columnName, findMaterial(icon));
            } else {
                String value = table.getValueAt(row, column).toString();
                if (columnName.equals("Name"))
                    materialAssignments[row].setName(value);
                else if (columnName.equals("Key"))
                    materialAssignments[row].setKey(value);
            }
        });
        this.loadColumns();
        this.loadMaterialAssignments();
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        MaterialsSaveAndAddPanel additionalPanel = new MaterialsSaveAndAddPanel(this);
        this.add(additionalPanel, BorderLayout.SOUTH);

        MaterialsMovePanel materialsMovePanel = new MaterialsMovePanel(this);
        this.add(materialsMovePanel, BorderLayout.WEST);
    }

    /**
     * this method looks up all the materials within the designated folder set in the {@link Constants}
     */
    private void loadMaterials() {
        ArrayList<Material> materialList = new ArrayList<>();
        File f = new File(Constants.texturesPath);
        if (f.exists())
            for (File file : f.listFiles()) {
                String fileName = file.getName().split("\\.")[0];
                Material M = new Material(fileName);
                if (fileName.equals("errorTexture"))
                    Constants.errorMaterial = M;
                else
                    materialList.add(M);
            }
        else
            System.out.println("Die Texturen können nicht aufgelistet werden");

        materials = materialList.toArray(new Material[]{});
    }

    /**
     * tries to retrieve a {@link Material} by the {@link ImageIcon}.
     * This is used because of the ComboBox for choosing
     *
     * @param icon the {@link ImageIcon} of the wanted {@link Material}
     * @return the {@link Material}
     */
    private Material findMaterial(ImageIcon icon) {
        for (Material material : materials) {
            if (material.getIcon() == icon) {
                return material;
            }
        }
        System.out.println("NO material found!");
        return null;
    }

    /**
     * loads the tables column-names and the ComboBox to choose a {@link Material} based on {@link Material#icon}
     */
    private void loadColumns() {
        JComboBox materialsComboBox = new JComboBox(this.getMaterialIcons());
        materialsComboBox.setMaximumRowCount(5);

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 3; i < table.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setCellEditor(new DefaultCellEditor(materialsComboBox));
        }
    }

    /**
     * Creates an array of all Icons of the {@link #materials}
     *
     * @return the image of ImageIcons
     */
    private ImageIcon[] getMaterialIcons() {
        ImageIcon[] images = new ImageIcon[materials.length + 1];

        for (int i = 0; i < materials.length; i++)
            images[i] = materials[i].getIcon();

        if (Constants.errorMaterial != null)
            images[images.length - 1] = Constants.errorMaterial.getIcon();

        return images;
    }

    /**
     * deletes a selected {@link MaterialAssignment} form the table.
     * If no row is selected the call is ignored.
     * It uses the method {@link #removeMaterialAssignment} to remove it from {@link #materialAssignments}
     */
    public void removeMaterialAssignment() {
        int i = table.getSelectedRow();
        if (i != -1)
            this.removeMaterialAssignment(materialAssignments[i]);
    }

    /**
     * deletes a given {@link MaterialAssignment} from {@link #materialAssignments}
     *
     * @param current the {@link MaterialAssignment} that should be deleted
     */
    private void removeMaterialAssignment(MaterialAssignment current) {
        ArrayList<MaterialAssignment> translationsList = new ArrayList<>(Arrays.asList(materialAssignments));
        translationsList.remove(current);
        materialAssignments = translationsList.toArray(new MaterialAssignment[]{});
        this.refresh();
    }

    /**
     * adds a new {@link MaterialAssignment} to {@link #materialAssignments} and uses {@link #refresh()} to reload the {@link #table}
     *
     * @param materialAssignment the new {@link MaterialAssignment}
     */
    public void addMaterialAssignment(MaterialAssignment materialAssignment) {
        ArrayList<MaterialAssignment> materialsList = new ArrayList<>(Arrays.asList(materialAssignments));
        materialsList.add(materialAssignment);
        materialAssignments = materialsList.toArray(materialAssignments);
        this.refresh();
    }

    /**
     * changes the index of a selected row based on the given attribute.
     * If the is invalid, or the change operation would be invalid based on the length og the array the method call is ignored
     *
     * @param upDown the direction of index changing. Typically it's 1 or -1 to push it up/down by 1
     */
    public void changeIndices(int upDown) {
        int i = table.getSelectedRow();
        if (i + upDown < materialAssignments.length && i + upDown >= 0 && i != -1) {
            MaterialAssignment t = materialAssignments[i];
            materialAssignments[i] = materialAssignments[i + upDown];
            materialAssignments[i + upDown] = t;
            this.refresh();
            table.setRowSelectionInterval(i + upDown, i + upDown);
        }
    }

    /**
     * reloads the {@link #table} by first removing all rows and afterwards reentering the (changed) {@link MaterialAssignment MaterialAssignments}
     */
    private void refresh() {
        while (table.getRowCount() > 0) {
            model.removeRow(0);
        }

        for (MaterialAssignment materialAssignment : materialAssignments) {
            model.addRow(materialAssignment.getData());
        }
    }

    /**
     * loads all {@link MaterialAssignment MaterialAssignments} written in the materials.ini using {@link #loadUniqueMaterialAssignment(Ini, String)}
     */
    private void loadMaterialAssignments() {
        try {
            Ini ini = new Ini(new File(Constants.defaultPath + "\\su_RobersExcelConvert\\classes\\materials.ini"));

            Set<String> keys = ini.keySet();

            ArrayList<MaterialAssignment> materialAssignmentsList = new ArrayList<>();
            int index = 0;
            for (String key : keys) {
                materialAssignmentsList.add(this.loadUniqueMaterialAssignment(ini, key));
                index++;
            }
            materialAssignments = materialAssignmentsList.toArray(new MaterialAssignment[]{});

            for (MaterialAssignment materialAssignment : materialAssignments) {
                if (materialAssignment != null)
                    model.addRow(materialAssignment.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * loads a single {@link MaterialAssignment}
     *
     * @param ini
     * @param key
     * @return
     */
    private MaterialAssignment loadUniqueMaterialAssignment(Ini ini, String key) {
//        String key = ini.get(key, "key");
        String name = ini.get(key, "name");
        String werkstoff = ini.get(key, "werkstoff");
        String materialgruppe = ini.get(key, "materialgruppe");
        String vorneStr = ini.get(key, "vorne");
        String hintenStr = ini.get(key, "hinten");
        String linksStr = ini.get(key, "links");
        String rechtsStr = ini.get(key, "rechts");
        String obenStr = ini.get(key, "oben");
        String untenStr = ini.get(key, "unten");

        Material vorne = this.getMaterial(vorneStr);
        Material hinten = this.getMaterial(hintenStr);
        Material links = this.getMaterial(linksStr);
        Material rechts = this.getMaterial(rechtsStr);
        Material oben = this.getMaterial(obenStr);
        Material unten = this.getMaterial(untenStr);

        if (vorne != null && hinten != null && rechts != null && links != null && oben != null && unten != null)
            return new MaterialAssignment(name, key, werkstoff, materialgruppe, vorne, hinten, links, rechts, oben, unten);

        System.out.println("ERROR: NO MaterialAssignment possible for key=" + key);
        return null;
    }

    /**
     * retrieves the {@link Material} that has the given {@link Material}
     *
     * @param materialName the {@link Material#name}
     * @return the {@link Material} corresponding to the given materialName. If no {@link Material} with this name
     * could be found the {@link Constants#errorMaterial} is returned
     */
    private Material getMaterial(String materialName) {
        for (Material material : materials)
            if (material.getName().equals(materialName))
                return material;

        System.out.println("Cannot find name=" + materialName + ". Give it errorTexture");
        return Constants.errorMaterial;
    }

    /**
     * tests whether a key is unique within the current set of {@link MaterialAssignment MaterialAssignments}
     *
     * @param current the currently {@link MaterialAssignment}. If an existing {@link MaterialAssignment} is edited it is valid to keep the key as it is
     * @param key     the key that should not already be taken except of the same {@link MaterialAssignment}
     * @return a boolean indicating if the key is unique
     */
    public boolean isKeyUnique(MaterialAssignment current, String key) {
        for (MaterialAssignment materialAssignment : materialAssignments)
            if (materialAssignment != current && materialAssignment.getKey().equals(key))
                return false;

        return true;
    }


    /**
     * getting the default material as an example {@link Material}
     * @return the material with index 0
     */
    public Material getDefaultMaterial() {
        return materials[0];
    }
}
