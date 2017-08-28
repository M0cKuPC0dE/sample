/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Category;
import co.th.linksinnovation.mitrphol.compliance.model.Compliance;
import co.th.linksinnovation.mitrphol.compliance.model.Status;
import co.th.linksinnovation.mitrphol.compliance.repository.CategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.ComplianceRepository;
import co.th.linksinnovation.mitrphol.compliance.service.LocaleService;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@RestController
@RequestMapping("/api")
public class ProgressUploadController {

    private static final int BUFFER_SIZE = 1024 * 100;

    @Autowired
    private LocaleService localeService;

    @Autowired
    private ComplianceRepository complianceRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    private final static SimpleDateFormat fd = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    
    @RequestMapping(value = "/csvupload", method = RequestMethod.PUT)
    public void examUpload(@RequestBody byte[] file, HttpServletRequest request) throws 
        UnsupportedEncodingException, FileNotFoundException, IOException, ParseException {

        InputStream chunk = new ByteArrayInputStream(file);
        String filename = URLDecoder.decode(request.getHeader("Content-Name"), "UTF-8");
        appendFile(request.getHeader("Content-Start"), chunk, new File("/mnt/data/files/" +       
                request.getHeader("Content-Name") + "-" + filename));
        
        if (request.getHeader("Content-End") != null
                && request.getHeader("Content-End").equals(request.getHeader("Content-FileSize"))) {
            try (FileInputStream inputStream = new FileInputStream(
                    new File("/mnt/data/files/" + request.getHeader("Content-Name") + "-" + filename))) {
                    
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet firstSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = firstSheet.iterator();
                
                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                    System.out.println("RowNum: " + nextRow.getRowNum());
                    if(nextRow.getRowNum() == 0){
                        continue;
                    } else if(getCellValue(nextRow.getCell(15)) == null
                            || getCellValue(nextRow.getCell(15)).equals("")){
                        break;
                    }
                    
                    Category category = null;
                    for(int i = 0; i <= 6; i++) {
                        category = saveCategory(nextRow, i, category);
                    }
                    
                    // get compliance
                    final String legalname = getCellValue(nextRow.getCell(7));
                    final Double year =  Double.parseDouble(getCellValue(nextRow.getCell(8)));
                    
                    final Date publicDate = fd.parse(getCellValue(nextRow.getCell(9)));
                    final Date effectiveDate = fd.parse(getCellValue(nextRow.getCell(10)));
                    
                    final Status status = Status.valueOf(getCellValue(nextRow.getCell(11)).toUpperCase());
                    
                    final String department = getCellValue(nextRow.getCell(12));
                    final String ministry = getCellValue(nextRow.getCell(13));
                    final String important = getCellValue(nextRow.getCell(14));
                    final String legalDuty = getCellValue(nextRow.getCell(15));
                    
                    final Compliance compliance = new Compliance();
                    compliance.setLegalName(legalname);
                    compliance.setYear(year.intValue());
                    compliance.setPublicDate(publicDate);
                    compliance.setEffectiveDate(effectiveDate);
                    compliance.setStatus(status);
                    compliance.setDepartment(department);
                    compliance.setMinistry(ministry);
                    compliance.setImportant(important);
                    compliance.setLegalDuty(legalDuty);
                    
                    compliance.setCategory(category);
                    
                    complianceRepository.save(compliance);
                    
                }
                workbook.close();
            }
            
        }
    }

    @RequestMapping(value = "/pdfupload", method = RequestMethod.PUT)
    public void pdfUpload(@RequestBody byte[] file, HttpServletRequest request) throws 
        UnsupportedEncodingException {
        
        InputStream chunk = new ByteArrayInputStream(file);
        String filename = URLDecoder.decode(request.getHeader("Content-Name"), "UTF-8");
        appendFile(request.getHeader("Content-Start"), chunk, new File("/mnt/data/files/" + request.getHeader("Content-Name") + "-" + filename));
        if (request.getHeader("Content-End") != null 
                && request.getHeader("Content-End").equals(request.getHeader("Content-FileSize"))) {
           
        }
    }
    
    @RequestMapping(value = "/localeupload/{name}", method = RequestMethod.PUT)
    public void localeUpload(@RequestBody byte[] file, @PathVariable String name, HttpServletRequest request)
            throws Exception {
        InputStream chunk = new ByteArrayInputStream(file);
        appendFile(request.getHeader("Content-Start"), chunk,
                new File("/mnt/locales/" + name + ".json"));
        
        localeService.save(name);
    }

    private void appendFile(String start, InputStream in, File dest) {
        OutputStream out = null;

        try {
            if (dest.exists()) {
                if (start.equals("0")) {
                    if (dest.delete()) {
                        out = new BufferedOutputStream(new FileOutputStream(dest), BUFFER_SIZE);
                    }
                }
                out = new BufferedOutputStream(new FileOutputStream(dest, true), BUFFER_SIZE);
            } else {
                out = new BufferedOutputStream(new FileOutputStream(dest), BUFFER_SIZE);
            }
            in = new BufferedInputStream(in, BUFFER_SIZE);

            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
    
    private Category saveCategory(Row nextRow, int col, Category parent) {
        String categoryName = nextRow.getCell(col).getStringCellValue();
        if (categoryName == null || "".equals(categoryName)) {    
            return parent;
        }
        Category category = null;
        if (parent == null) {
            category = categoryRepository.findByNameAndParentIsNull(categoryName);
        } else {
            category = categoryRepository.findByNameAndParent(categoryName, parent);
        }
        
        if (category != null) {
            return category;
        }
        
        category = new Category();
        category.setName(categoryName);
        category.setParent(parent);
        categoryRepository.save(category);
        return category;
        
    }
    
    private String getCellValue(Cell cell) {
        if(cell == null) {
            return null;
        }
        
        final CellType type = cell.getCellTypeEnum();
        switch(type) {
            case BLANK :
                return "";
            case BOOLEAN :
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC :
                if(HSSFDateUtil.isCellDateFormatted(cell)) {
                     return String.valueOf(cell.getDateCellValue());
                }
                return String.valueOf(cell.getNumericCellValue());          
            default:
                // String
                return cell.getStringCellValue();
        }
       
    }
}
