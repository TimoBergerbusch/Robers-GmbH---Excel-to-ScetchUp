import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class MaterialsPanel extends JPanel {

    private JTable table;
    private MaterialsSaveAndAddPanel additionalPanel;
    private MaterialsMovePanel translationsMovePanel;
    private DefaultTableModel model;

    private Material[] materials;
    public MaterialAssignment[] materialAssignments;

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
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
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
            }
        });
        this.loadColumns(table);
        this.loadMaterialAssignments();
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        additionalPanel = new MaterialsSaveAndAddPanel(this);
        this.add(additionalPanel, BorderLayout.SOUTH);

        translationsMovePanel = new MaterialsMovePanel(this);
        this.add(translationsMovePanel, BorderLayout.WEST);
    }

    private void loadMaterials() {
        materials = new Material[6];
        materials[0] = new Material("texture1");
        materials[1] = new Material("texture2");
        materials[2] = new Material("texture3");
        materials[3] = new Material("texture4");
        materials[4] = new Material("texture5");
        materials[5] = new Material("texture6");
    }

    private Material findMaterial(ImageIcon icon) {
        for (Material material : materials) {
            if (material.getIcon() == icon) {
                return material;
            }
        }
        return null;
    }

    private void loadColumns(JTable table) {
        JComboBox materialsComboBox = new JComboBox(this.getMaterialIcons());

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 3; i < table.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setCellEditor(new DefaultCellEditor(materialsComboBox));
        }
    }

    private ImageIcon[] getMaterialIcons() {
        ImageIcon[] images = new ImageIcon[materials.length];

        for (int i = 0; i < materials.length; i++)
            images[i] = materials[i].getIcon();

        return images;
    }

    public void removeMaterialAssignment() {
        int i = table.getSelectedRow();
        if (i != -1)
            this.removeMaterial(materialAssignments[i]);
    }

    public void removeMaterial(MaterialAssignment current) {
        ArrayList<MaterialAssignment> translationsList = new ArrayList<>(Arrays.asList(materialAssignments));
        translationsList.remove(current);
        materialAssignments = translationsList.toArray(new MaterialAssignment[]{});
        this.refresh();
    }

    public void addMaterialAssignment(MaterialAssignment materialAssignment) {
        ArrayList<MaterialAssignment> materialsList = new ArrayList<>(Arrays.asList(materialAssignments));
        materialsList.add(materialAssignment);
        materialAssignments = materialsList.toArray(materialAssignments);
        this.refresh();
    }

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

    public void refresh() {
        while (table.getRowCount() > 0) {
            model.removeRow(0);
        }

        for (int i = 0; i < materialAssignments.length; i++) {
            model.addRow(materialAssignments[i].getData());
        }
    }

    private void loadMaterialAssignments() {
//        materialAssignments = new MaterialAssignment[6];
//        materialAssignments[0] = new MaterialAssignment("Test1", "T1", "W1", "M1", materials[0]);
//        materialAssignments[1] = new MaterialAssignment("Test2", "T2", "W2", "M2", materials[0], materials[1]);
//        materialAssignments[2] = new MaterialAssignment("Test3", "T3", "W3", "M3", materials[2], materials[3]);
//        materialAssignments[3] = new MaterialAssignment("Test4", "T4", "W4", "M4", materials[4]);
//        materialAssignments[4] = new MaterialAssignment("Test5", "T5", "W5", "M5", materials[0], materials[1], materials[2], materials[3], materials[4], materials[5]);
//        materialAssignments[5] = new MaterialAssignment("OSB3 Platten", "OSB3PL", "OSB3 Platten", "PL", materials[3]);
//
//        for (int i = 0; i < materialAssignments.length; i++) {
//            model.addRow(materialAssignments[i].getData());
//        }
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

            for (int i = 0; i < materialAssignments.length; i++) {
                model.addRow(materialAssignments[i].getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    private Material getMaterial(String materialName) {
        for (Material material : materials)
            if (material.getName().equals(materialName))
                return material;

        return null;
    }

    public boolean isKeyUnique(MaterialAssignment current, String key) {
        for (MaterialAssignment materialAssignment : materialAssignments)
            if (materialAssignment != current && materialAssignment.getKey().equals(key))
                return false;

        return true;
    }

    public Material getDefaultMaterial() {
        return materials[0];
    }
}
