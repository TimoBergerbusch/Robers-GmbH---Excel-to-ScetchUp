import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    JButton saveAndDraw;

    final int radioCheckBockColumn = 8;

    public JProgressBar progressBar = new JProgressBar(SwingConstants.VERTICAL);

    ExcelReader excelReader = ExcelReader.getExcelReader(progressBar);

    Element[] elements;


    private final String[] columnNames = new String[]{"Name", "Bez.", "Bauteil", "Mg", "Werkstoff",  "TKey", "MKey", "Offset", "Daneben?"};
    private ArrayList<Integer> forbiddenRows = new ArrayList<>();


    public ExcelReadingPanel() {
        this.setLayout(new BorderLayout());

        this.openExcelFile = new JButton("Excel-Datei einlesen", MetalIconFactory.getTreeFolderIcon());
        this.add(openExcelFile, BorderLayout.NORTH);
        this.openExcelFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("D:\\Dokumente\\GitHub\\Robers-GmbH---Excel-to-ScetchUp\\Testdaten");
                fc.setFileFilter(new FileNameExtensionFilter("(Only Excel Files)", "xlsm"));
                int returnVal = fc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    elements = excelReader.readFile(file);
                    loadElements(elements);
                    saveAndDraw.setEnabled(true);
                }
            }
        });

        this.model = new DefaultTableModel() {
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
//                    case 5:
//                        return Translation.class;
//                    case 8:
//                        return MaterialAssignment.class;
                    case radioCheckBockColumn:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
//                System.out.println("Is the column named " + columnNames[column] + " editable? ");
                if (forbiddenRows.contains(row))
                    return false;

//                if (column <= 5)
                if (column <= 7)
                    return false;
//                else if ((6 <= column && column <= 9) || column == 11)
//                    return true;
                else if (column == radioCheckBockColumn)
                    return true;
                else if (column == radioCheckBockColumn - 1)
                    return !(boolean) getValueAt(row, column + 1);
                else
                    return true; // DARF NIE AUFTRETEN
            }
        };
        this.model.setColumnIdentifiers(columnNames);
        this.model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (row >= 0 && column >= 0 && row == e.getLastRow() && columnNames[column].toString().equals("Daneben?")) {
                    System.out.print("Es wurde für Zeile " + row + " entschieden es");
                    if (!(boolean) model.getValueAt(row, column)) {
                        System.out.print(" nicht");
                        model.setValueAt(elements[row].getOffset(), row, column - 1); //Restore old value
                    } else
                        model.setValueAt("(-100,0,0)", row, column - 1); //TODO: modify the default daneben-value
                    System.out.println(" daneben zu zeichnen.");
                } else if (row >= 0 && column >= 0 && row == e.getLastRow()) {

                    System.out.println("changed value in " + model.getColumnName(column) + "-column to " + model.getValueAt(row, column));
                    if (column == radioCheckBockColumn - 1) {
                        String offset = model.getValueAt(row, column).toString();

                        if (!offset.matches("[(][-]?[0-9]*[,][-]?[0-9]*[,][-]?[0-9]*[)]")) {
                            System.out.println("Offside has not the expected format");
                            model.setValueAt(elements[row].getOffset(), row, column);
                        } else {
                            System.out.println("Changed Offset in right format");
//                            if (!(boolean) model.getValueAt(row, column + 1))
                            elements[row].setOffset(offset);
                        }

                    }
                }
            }
        });

        this.table = new JTable(model);
        this.table.setRowHeight(25);

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        this.saveAndDraw = new JButton("Zeichnen", MetalIconFactory.getTreeFloppyDriveIcon());
        this.add(saveAndDraw, BorderLayout.SOUTH);
        this.saveAndDraw.setEnabled(false);
        this.saveAndDraw.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                this.speichern();

                //SketchUp ausführen
                try {
                    Process process = new ProcessBuilder("cmd", "/c", "start sketchup.exe").start();
                    System.out.println(process.waitFor());
//                    Runtime.getRuntime().exec("cmd","/d","start sketchup.exe");
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            private void speichern() {
                try {
//                    Ini inifile = new Ini(Constants.connectionFile);
                    File file = new File(System.getenv("APPDATA") + "\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins\\su_RobersExcelConvert\\classes\\connection_new.ini");
                    file.createNewFile();
                    System.out.println(file.exists());
                    Ini ini = new Ini(file);
                    ini.getConfig().setEscape(false);
                    ini.clear();

                    ini.put("General", "numberOfElements", new Integer(elements.length));
                    ini.put("General", "usedMaterials", retrievedUsedMaterials());

                    String section;
                    Element element;
                    for (int i = 0; i < elements.length; i++) {
                        System.out.println("Save Element named: Element" + (i + 1));
                        section = "Element" + (i + 1);
                        elements[i].printIntoIniFile(section, ini);
                    }
                    ini.store(file);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

//        progressBar = new JProgressBar(SwingConstants.VERTICAL);
        this.add(progressBar, BorderLayout.WEST);

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
                forbiddenRows.add(i);
                model.setValueAt(true, i, radioCheckBockColumn);
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
    }
}
