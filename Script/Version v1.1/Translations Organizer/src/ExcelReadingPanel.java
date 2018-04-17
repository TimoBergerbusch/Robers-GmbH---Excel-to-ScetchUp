import org.ini4j.Ini;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.MetalIconFactory;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    final int radioCheckBockColumn = 8;

    public JProgressBar progressBar = new JProgressBar(SwingConstants.VERTICAL);

    ExcelReader excelReader = ExcelReader.getExcelReader(progressBar);

    public JComboBox drawBox = new JComboBox(new Object[]{"Mit Koordinaten zeichnen", "Als Stapel zeichnen"});

    Element[] elements;

    private final String[] columnNames = new String[]{"Name", "Bez.", "Bauteil", "Mg", "Werkstoff", "TKey", "MKey", "Offset", "Daneben?"};
    private ArrayList<Integer> forbiddenRows = new ArrayList<>();

    public static int danebenXKoord, danebenYKoord, danebenZKoord;

    public ExcelReadingPanel() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 6;
        gbc.fill = GridBagConstraints.BOTH;

        this.add(progressBar, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        this.openExcelFile = new JButton("Excel-Datei einlesen", MetalIconFactory.getTreeFolderIcon());
        this.openExcelFile.setPreferredSize(new Dimension(900, 30));
        this.add(openExcelFile, gbc);
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
                    save.setEnabled(true);
                }
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
                    case radioCheckBockColumn:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                System.out.println("Is the column named " + columnNames[column] + " editable? ");
                if (forbiddenRows.contains(row))
                    return false;

//                if (column <= 5)
                if (column <= 6)
                    return false;
                else if (column == radioCheckBockColumn)
                    return true;
                else if (column == radioCheckBockColumn - 1)
                    return !(boolean) getValueAt(row, radioCheckBockColumn);
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

                if (row >= 0 && column >= 0 && row == e.getLastRow())
//                    if (columnNames[column].toString().equals("Daneben?")) {    //If CheckBox changed
////                    System.out.print("Es wurde für Zeile " + row + " entschieden es");
//                        if (!(boolean) model.getValueAt(row, column)) {
////                        System.out.print(" nicht");
//                            model.setValueAt(elements[row].getOffset(), row, column - 1); //Restore old value
//                        }else
////                            model.setValueAt("(0,0,0)", row, column - 1); //TODO: modify the default daneben-value
////                    System.out.println(" daneben zu zeichnen.");
//                    } else
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
                    }
            }
        });

        this.table = new JTable(model);
        this.table.setRowHeight(25);

        gbc.gridy++;
        gbc.gridheight = 5;
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 600));
        this.add(scrollPane, gbc);


        gbc.gridheight = 1;
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 6;

        ((JLabel) drawBox.getRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        this.add(drawBox, gbc);

        gbc.gridy++;
        this.save = new JButton("Speichern", MetalIconFactory.getTreeFloppyDriveIcon());
        this.save.setPreferredSize(new Dimension(900, 30));
        this.add(save, gbc);
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
        this.add(saveAndDraw, gbc);
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
                boolean bool = (boolean) model.getValueAt(i, radioCheckBockColumn);
                if (drawBox.getSelectedIndex() == 1)
                    bool = true;
                if (i > 0)
                    this.recalcDanebenValues(elements[i - 1], elements[i]);
                elements[i].printIntoIniFile(section, ini, bool);
            }
            ini.store(file);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void recalcDanebenValues(Element element, Element element1) {
        if (element.compareTo(element1) == 0) {
            danebenYKoord += element.getYAxisValue() + View.constantsPanel.constants.get("danebenVersatz");
        } else {
            danebenYKoord = View.constantsPanel.constants.get("danebenYValue");
            danebenZKoord += element.getZAxisValue() + View.constantsPanel.constants.get("danebenVersatz");
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
                System.out.println("Row number " + i + " is forbidden");
                forbiddenRows.add(i);
                model.setValueAt(true, i, radioCheckBockColumn);
            } else if (model.getValueAt(i, radioCheckBockColumn - 1).equals("(-7811,-7811,-7811)")) {
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

