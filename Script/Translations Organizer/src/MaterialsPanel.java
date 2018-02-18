import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Timo Bergerbusch on 12.02.2018.
 */
public class MaterialsPanel extends JPanel {

    private String[] columnNames = {"Name", "Key", "Oben", "Unten", "Links", "Rechts", "Front", "Hinten"};
    private JTable table;
    private MaterialsSaveAndAddPanel additionalPanel;
    private MaterialsMovePanel translationsMovePanel;
    private DefaultTableModel model;

    private Material[] materials;
    public MaterialAssignment[] materialAssignments;

    public MaterialsPanel() {
        this.setLayout(new BorderLayout());

        materials = new Material[6];
        materials[0] = new Material("texture1");
        materials[1] = new Material("texture2");
        materials[2] = new Material("texture3");
        materials[3] = new Material("texture4");
        materials[4] = new Material("texture5");
        materials[5] = new Material("texture6");

        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int colum) {
                return true;
            }

            public Class getColumnClass(int column) {
                return getValueAt(0, column).getClass();
            }
        };
        model.setColumnIdentifiers(columnNames);
        table = new JTable(model);
        table.setRowHeight(25);
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2)
                    System.out.println("Double click row: " + row);
            }
        });
        this.loadColumns(table);
        this.loadTranslations();
        this.add(new JScrollPane(table), BorderLayout.CENTER);

        additionalPanel = new MaterialsSaveAndAddPanel(this);
        this.add(additionalPanel, BorderLayout.SOUTH);

        translationsMovePanel = new MaterialsMovePanel(this);
        this.add(translationsMovePanel, BorderLayout.WEST);
    }

    private void loadColumns(JTable table) {
        JComboBox materialsComboBox = new JComboBox(this.getMaterialIcons());
//        materialsComboBox.setRenderer(new ComboBoxListRenderer());

        TableColumnModel columnModel = table.getColumnModel();
        for (int i = 2; i < table.getColumnCount(); i++) {
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

    public void addTranslation(MaterialAssignment materialAssignment) {
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

    private void loadTranslations() {
        materialAssignments = new MaterialAssignment[5];
        materialAssignments[0] = new MaterialAssignment("Test1", "T1", materials[0]);
        materialAssignments[1] = new MaterialAssignment("Test2", "T2", materials[0], materials[1]);
        materialAssignments[2] = new MaterialAssignment("Test3", "T3", materials[2], materials[3]);
        materialAssignments[3] = new MaterialAssignment("Test4", "T4", materials[4]);
        materialAssignments[4] = new MaterialAssignment("Test5", "T5", materials[0], materials[1], materials[2], materials[3], materials[4], materials[5]);

        for (int i = 0; i < materialAssignments.length; i++) {
            model.addRow(materialAssignments[i].getData());
        }
    }

    private Translation loadUniqueMaterial(Ini ini, String key) {
        return null;
    }

    public boolean contains(MaterialAssignment current) {
        for (int i = 0; i < materialAssignments.length; i++)
            if (materialAssignments[i] == current)
                return true;

        return false;
    }


    public boolean isKeyUnique(Material current, String key) {
//        for (Material translation : materialAssignments)
//            if (translation != current && translation.get("Key").equals(key))
//                return false;

        return true;
    }
}
