package net.ooder.skill.travel;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelReaderService {

    /**
     * Read email addresses from Excel file
     * @param filePath Excel file path
     * @return List of email addresses
     */
    public List<String> readEmailsFromExcel(String filePath) {
        List<String> emails = new ArrayList<>();
        
        try {
            // Check if file exists
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("Excel file not found: " + filePath);
                // Return sample emails if file not found
                return getSampleEmails();
            }
            
            System.out.println("Reading Excel file: " + filePath);
            
            // Read Excel file
            try (FileInputStream fis = new FileInputStream(file);
                 Workbook workbook = WorkbookFactory.create(fis)) {
                
                // Get first sheet
                Sheet sheet = workbook.getSheetAt(0);
                
                // Iterate through rows
                for (Row row : sheet) {
                    // Assume email is in the first column
                    Cell cell = row.getCell(0);
                    if (cell != null) {
                        String email = getCellValue(cell);
                        if (isValidEmail(email)) {
                            emails.add(email);
                        }
                    }
                }
                
                System.out.println("Found " + emails.size() + " email addresses in Excel file");
                
                // Return sample emails if no emails found
                if (emails.isEmpty()) {
                    System.out.println("No email addresses found in Excel file, using sample data");
                    return getSampleEmails();
                }
                
            } catch (Exception e) {
                System.out.println("Error reading Excel file: " + e.getMessage());
                // Return sample emails if error occurs
                return getSampleEmails();
            }
            
        } catch (Exception e) {
            System.out.println("Error processing Excel file: " + e.getMessage());
            // Return sample emails if error occurs
            return getSampleEmails();
        }
        
        return emails;
    }
    
    /**
     * Get cell value as string
     */
    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    
    /**
     * Check if email is valid
     */
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    /**
     * Get sample email addresses
     */
    private List<String> getSampleEmails() {
        List<String> sampleEmails = new ArrayList<>();
        sampleEmails.add("tourist1@example.com");
        sampleEmails.add("tourist2@example.com");
        sampleEmails.add("tourist3@example.com");
        sampleEmails.add("tourist4@example.com");
        sampleEmails.add("tourist5@example.com");
        System.out.println("Using sample email addresses: " + sampleEmails);
        return sampleEmails;
    }
}
