import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Timo Bergerbusch on 09.04.2018.
 */
public class ExcelReadingPanel extends JPanel {

    JTable table;
    DefaultTableModel model;

    JButton openExcelFile;

    JButton save, saveAndDraw;

    final int danebenColumn = 8;
    final int bretterColumn = 9;

    public JProgressBar progressBar = new JProgressBar(SwingConstants.VERTICAL);

    ExcelReader excelReader = ExcelReader.getExcelReader(progressBar);

    public JComboBox drawBox = new JComboBox(new Object[]{"Mit Koordinaten zeichnen", "Als Stapel zeichnen"});

//    private ElementPanel elementPanel;

    Element[] elements;

    private final String[] columnNames = new String[]{"Name", "Bez.", "Bauteil", "Mg", "Werkstoff", "TKey", "MKey", "Offset", "Daneben?", "Bretter?", "Brett-Breite"};
    private ArrayList<Integer> forbiddenRows = new ArrayList<>();

    public static int danebenXKoord, danebenYKoord, danebenZKoord;

    public ExcelReadingPanel() {
        this.setLayout(new BorderLayout());


        this.openExcelFile = new JButton("Excel-Datei einlesen", MetalIconFactory.getTreeFolderIcon());
        this.openExcelFile.setPreferredSize(new Dimension(900, 30));
        this.add(openExcelFile, BorderLayout.NORTH);
        this.openExcelFile.addActionListener(e -> {
            JFileChooser fc = new JFileChooser("D:\\Dokumente\\GitHub\\Robers-GmbH---Excel-to-ScetchUp\\Testdaten");
            fc.setFileFilter(new FileNameExtensionFilter("(Only Excel Files)", "xlsm"));
            int returnVal = fc.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                elements = excelReader.readFile(file);
                loadElements(elements);
                saveAndDraw.setEnabled(true);
                save.setEnabled(true);
            }
        });
        Action openAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openExcelFile.doClick();
            }
        };
        this.openExcelFile.getActionMap().put("Open", openAction);
        this.openExcelFile.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK), "Open");


        this.model = new DefaultTableModel() {
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
//                    case 5:
//                        return Translation.class;
//                    case 8:
//                        return MaterialAssignment.class;
                    case danebenColumn:
                        return Boolean.class;
                    case bretterColumn:
                        return String.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
//                System.out.println("Is the column named " + columnNames[column] + " editable? ");
//                if (forbiddenRows.contains(row))
//                    return false;

//                if (column <= 5)
                if (column <= 6)
                    return false;
                else if (column == danebenColumn)
                    return true;
                else if (column == danebenColumn - 1)
                    return !(boolean) getValueAt(row, danebenColumn);
                else if (column == bretterColumn)
                    return true;
//                    return !(boolean) getValueAt(row, danebenColumn);
                else
                    return true; // DARF NIE AUFTRETEN
            }
        };
        this.model.setColumnIdentifiers(columnNames);
        this.model.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();

            if (row >= 0 && column >= 0 && row == e.getLastRow())
                if (columnNames[column].toString().equals("Offset")) {  //Offset
//                    System.out.println("changed value in " + model.getColumnName(column) + "-column to " + model.getValueAt(row, column));
                    String offset = model.getValueAt(row, column).toString();

                    if (!offset.matches("[(][-]?[0-9]*[,][-]?[0-9]*[,][-]?[0-9]*[)]")) {    //check for format
//                            System.out.println("Offside has not the expected format");
                        model.setValueAt(elements[row].getOffset(), row, column);
                    } else {
//                            System.out.println("Changed Offset in right format");
                        elements[row].setOffset(offset);
                    }
                } else if (columnNames[column].toString().equals("Daneben?")) {
                    if ((boolean) model.getValueAt(row, column))
                        model.setValueAt("-", row, column + 1);
                } else if (columnNames[column].toString().equals("Bretter?")) {
                    if (model.getValueAt(row, column).equals("-"))
                        model.setValueAt("-", row, column + 1);
                    else
                        model.setValueAt(View.constantsPanel.constants.get("brettBreite"), row, column + 1);
                }
        });

        this.table = new JTable(model);
        this.table.setRowHeight(25);
//        table.addMouseListener(new MouseAdapter() {
//            public void mousePressed(MouseEvent mouseEvent) {
//                JTable table = (JTable) mouseEvent.getSource();
//                Point point = mouseEvent.getPoint();
//                int row = table.rowAtPoint(point);
//                if (mouseEvent.getClickCount() == 2) {
//                    System.out.println(elements[row].toString());
//                    elementPanel.loadElement(elements[row]);
//                    elementPanel.setVisible(true);
//                }
//            }
//        });

        this.loadColumns();

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 600));
        scrollPane.setMinimumSize(new Dimension(600, 600));
        this.add(scrollPane, BorderLayout.CENTER);

//        gbc.gridx = 7;
//        this.elementPanel = new ElementPanel();
//        this.elementPanel.setVisible(false);
//        this.add(elementPanel, gbc);

        // BOTTOM PANEL
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;

        ((JLabel) drawBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(drawBox, gbc);


        gbc.gridy++;
        this.save = new JButton("Speichern", MetalIconFactory.getTreeFloppyDriveIcon());
        this.save.setPreferredSize(new Dimension(900, 30));
        bottomPanel.add(save, gbc);
        this.save.setEnabled(false);
        this.save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speichern();
            }
        });
        this.save.setToolTipText("Speichern der Daten in der Datei für das SketchUp Plugin ohne Sketchup zu öffnen");
        Action saveAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save.doClick();
            }
        };
        this.save.getActionMap().put("Save", saveAction);
        this.save.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK), "Save");

        gbc.gridy++;
        this.saveAndDraw = new JButton("Speichern und Zeichnen", MetalIconFactory.getTreeFloppyDriveIcon());
        this.saveAndDraw.setPreferredSize(new Dimension(900, 30));
        bottomPanel.add(saveAndDraw, gbc);
        this.saveAndDraw.setEnabled(false);
        this.saveAndDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speichern();

                //SketchUp ausführen
                try {
                    JOptionPane.showConfirmDialog(null, "SketchUp wird gestartet...", "Information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    Process process = new ProcessBuilder("cmd", "/c", "start sketchup.exe").start();
                    System.out.println(process.waitFor());
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        });
        this.saveAndDraw.setToolTipText("Speichern die Daten und öffnet SketchUp");
        Action saveAndDrawAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveAndDraw.doClick();
            }
        };
        this.saveAndDraw.getActionMap().put("SaveAndDraw", saveAndDrawAction);
        this.saveAndDraw.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK + Event.SHIFT_MASK), "SaveAndDraw");

        this.add(bottomPanel, BorderLayout.SOUTH);
//        loadExampleFile();
    }

    private void loadColumns() {
        JComboBox materialsComboBox = new JComboBox(new String[]{"-", "X-Achse", "Y-Achse", "Z-Achse"});
        materialsComboBox.setMaximumRowCount(5);

        TableColumnModel columnModel = table.getColumnModel();
        TableColumn column = columnModel.getColumn(bretterColumn);
        column.setCellEditor(new DefaultCellEditor(materialsComboBox));
    }

    public void speichern() {
        try {
//            Ini file = new Ini(Constants.connectionFile);
//            File file = new File(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins\\su_RobersExcelConvert\\classes\\connection.ini");
            danebenXKoord = View.constantsPanel.constants.get("danebenXValue");
            danebenYKoord = View.constantsPanel.constants.get("danebenYValue");
            danebenZKoord = View.constantsPanel.constants.get("danebenZValue");
            File file = Constants.connectionFile;
            file.createNewFile();
//            System.out.println(file.exists());
            Ini ini = new Ini(file);
            ini.getConfig().setEscape(false);
            ini.clear();


            ini.put("General", "numberOfElements", computeNumberOfElements());
            ini.put("General", "usedMaterials", retrievedUsedMaterials());

            int section = 0;
            String bretterLayout;
            for (int i = 0; i < elements.length; i++) {
//                System.out.println("Save Element named: Element" + (i + 1));

                if (i == 0)
                    section += 1;
                else if (getPlankColumnValue(i - 1).equals("-"))
                    section += 1;
                else
                    section += elements[i - 1].getNumberOfPlanks(getPlankColumnValue(i - 1), this.getPlankWidthForElement(i - 1));

                boolean bool = (boolean) model.getValueAt(i, danebenColumn);
                if (drawBox.getSelectedIndex() == 1)
                    bool = true;
                if (i > 0)
                    this.recalcDanebenValues(elements[i - 1], elements[i]);
                bretterLayout = String.valueOf(model.getValueAt(i, bretterColumn));
                elements[i].printIntoIniFile(section, ini, bool, bretterLayout, this.getPlankWidthForElement(i));
            }
            ini.store(file);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public int getPlankWidthForElement(int i) {
        String value = String.valueOf(model.getValueAt(i, bretterColumn + 1));
        if (!value.equals("-"))
            return Integer.parseInt(value);
        else
            return Integer.MAX_VALUE;
    }

    private Integer computeNumberOfElements() {
        int number = 0;
        for (int i = 0; i < elements.length; i++) {
            String plankValue = this.getPlankColumnValue(i);
            if (!plankValue.equals("-"))
                number += elements[i].getNumberOfPlanks(plankValue, this.getPlankWidthForElement(i));
            else
                number++;
        }
        return number;
    }

    private String getPlankColumnValue(int row) {
        return String.valueOf(model.getValueAt(row, bretterColumn));
    }

    private void recalcDanebenValues(Element element, Element element1) {
        if (element.compareTo(element1) == 0) {
            danebenYKoord += element.getYAxisValue() + ConstantsPanel.constants.get("danebenVersatz");
        } else {
            danebenYKoord = ConstantsPanel.constants.get("danebenYValue");
            danebenZKoord += element.getZAxisValue() + ConstantsPanel.constants.get("danebenVersatz");
        }
    }

    private String retrievedUsedMaterials() {
        Set<Material> usedMaterials = new TreeSet<>();

        for (Element element : elements) {
            usedMaterials.addAll(element.getMatchingMaterialAssignment().getDistinctMaterials());
        }

        StringBuilder sb = new StringBuilder();
        Iterator<Material> iterator = usedMaterials.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext())
                sb.append("|");
        }

        return sb.toString();
    }

    private void loadElements(Element[] elements) {
        this.clearTable();
        forbiddenRows.clear();
        for (int i = 0; i < elements.length; i++) {
            Element element = elements[i];
            model.addRow(element.getDataAsRow());
            if (element.getMatchingTranslation() == Identifier.getIdentifier().getDefaultTranslation()) {
//                System.out.println("Row number " + i + " is forbidden");
                forbiddenRows.add(i);
                model.setValueAt(true, i, danebenColumn);
            } else if (model.getValueAt(i, danebenColumn - 1).equals("(-7811,-7811,-7811)")) {
                model.setValueAt(true, i, danebenColumn);
            }
        }
    }

    private void clearTable() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
    }

    private void loadExampleFile() {
        this.elements = excelReader.testReadExample();
        loadElements(elements);
        save.setEnabled(true);
        saveAndDraw.setEnabled(true);
    }

}

