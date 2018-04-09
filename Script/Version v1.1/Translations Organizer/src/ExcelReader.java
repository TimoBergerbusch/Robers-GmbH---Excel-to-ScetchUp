

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Timo Bergerbusch on 09.04.2018.
 */
public class ExcelReader {

    private static ExcelReader excelReader;
    DataFormatter formatter = new DataFormatter();
    FormulaEvaluator evaluator;
    File file;
    private JProgressBar progressBar;

    public static ExcelReader getExcelReader(JProgressBar progressBar) {
        if (excelReader == null)
            excelReader = new ExcelReader(progressBar);
        return excelReader;
    }

    public ExcelReader(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Element[] testReadExample() {
        file = new File("D:\\Dokumente\\GitHub\\Robers-GmbH---Excel-to-ScetchUp\\Testdaten\\real_test1.xlsm");
        return this.readFile(file);
    }

    public Element[] readFile(File file) {
        if (!file.exists() || !FilenameUtils.getExtension(String.valueOf(file.getAbsoluteFile())).equals("xlsm")) {
            System.out.println("The given file is not valid for this application");
            return new Element[0];
        }
        Element[] elements = new Element[0];
        progressBar.setValue(0); //VISUALS
        try {
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("Dimensionsware");
            evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            int numberOfElements = this.countDistinctElements(sheet);

            progressBar.setMaximum(numberOfElements - 1);//VISUALS

            elements = new Element[numberOfElements];
            HashMap<String, Integer> constants = View.constantsPanel.constants;
//            System.out.println(constants);
            int headerRow = constants.get("headerRow");
            for (int i = 0; i < numberOfElements; i++) {
                Row row = sheet.getRow(headerRow + 2 + 2 * i);
                elements[i] = this.readElementRow(row, constants);
                progressBar.setValue(i); //VISUALS

//                System.out.println("Element number " + i + ":" + elements[i] + System.currentTimeMillis());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        elements = this.blowUpNumber(elements);

        return elements;
    }

    private Element[] blowUpNumber(Element[] elements) {
        ArrayList<Element> newElements = new ArrayList<>();

        for (Element e : elements) {
            for (int count = 0; count < e.getAnzahl(); count++) {
                newElements.add(e);
            }
        }

        return newElements.toArray(new Element[0]);
    }

    private Element readElementRow(Row row, HashMap<String, Integer> constants) {
        String bezeichnung = formatter.formatCellValue(row.getCell(constants.get("Bezeichnung")));
        String bauteil = formatter.formatCellValue(row.getCell(constants.get("Bauteil")));
        String materialgruppe = formatter.formatCellValue(row.getCell(constants.get("Materialgruppe")));
        String werkstoff = formatter.formatCellValue(row.getCell(constants.get("Werkstoff")));
//        System.out.println(evaluator.evaluate(row.getCell(constants.get("Anzahl"))).getNumberValue());
        int anzahl = (int) evaluator.evaluate(row.getCell(constants.get("Anzahl"))).getNumberValue();
        int laenge = (int) evaluator.evaluate(row.getCell(constants.get("Laenge"))).getNumberValue();
        int breite = (int) evaluator.evaluate(row.getCell(constants.get("Breite"))).getNumberValue();
        int hoehe = (int) evaluator.evaluate(row.getCell(constants.get("Hoehe"))).getNumberValue();
        //TODO: OFFSETS
        return new Element(bezeichnung, bauteil, materialgruppe, werkstoff, anzahl, laenge, breite, hoehe, 0, 0, 0);
    }

    public int countDistinctElements(Sheet worksheet) {
        HashMap<String, Integer> constants = View.constantsPanel.constants;
        if (!constants.isEmpty()) {
            int numberOfElements = 0;
            Row row;
            String cellValue;
            do {
                numberOfElements++;
                row = worksheet.getRow(constants.get("headerRow") + 2 + numberOfElements * 2);
                cellValue = formatter.formatCellValue(row.getCell(constants.get("SageArt")));
//            System.out.println("Cellvalue at " + (18 + numberOfElements * 2) + ": " + cellValue);
//            System.out.println(!cellValue.equals(""));
            } while (cellValue != "");


            return numberOfElements;
        } else {
            System.out.println("ERROR: No constants to load Excel with");
            return 0;
        }
    }
}
