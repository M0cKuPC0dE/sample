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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
    
    @RequestMapping(value = "/examupload", method = RequestMethod.PUT)
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
                    if(nextRow.getRowNum() == 0){
                        continue;
                    }
                    
                    // get category
                    Category parent = null;
                    String parentName = nextRow.getCell(0).getStringCellValue();
                    if(parentName != null && !"".equals(parentName)) {
                        parent = categoryRepository.findByNameAndParentIsNull(parentName);
                        if(parent == null) {
                            parent = new Category();
                            parent.setName(parentName);
                            categoryRepository.save(parent);
                        }
                    }
                    Category child1 = createChildCategory(nextRow, 1, parent);
                    Category child2 = createChildCategory(nextRow, 2, child1);
                    Category child3 = createChildCategory(nextRow, 3, child2);
                    Category child4 = createChildCategory(nextRow, 4, child3);
                    Category child5 = createChildCategory(nextRow, 5, child4);
                    Category child6 = createChildCategory(nextRow, 6, child5);
                    
                    // get compliance
                    String legalname = nextRow.getCell(7).getStringCellValue();
                    Double year = nextRow.getCell(8).getNumericCellValue();
                  
                    Date publicDate = nextRow.getCell(9).getDateCellValue();
                    Date effectiveDate = nextRow.getCell(10).getDateCellValue();
                    Status status = Status.valueOf(nextRow.getCell(11).getStringCellValue().toUpperCase());
                    String department = nextRow.getCell(12).getStringCellValue();
                    String ministry = nextRow.getCell(13).getStringCellValue();
                    String important = nextRow.getCell(14).getStringCellValue();
                    String legalDuty = nextRow.getCell(15).getStringCellValue();
                    
                    Compliance compliance = new Compliance();
                    compliance.setLegalName(legalname);
                    compliance.setYear(year.intValue());
                    compliance.setPublicDate(publicDate);
                    compliance.setEffectiveDate(effectiveDate);
                    compliance.setStatus(status);
                    compliance.setDepartment(department);
                    compliance.setMinistry(ministry);
                    compliance.setImportant(important);
                    compliance.setLegalDuty(legalDuty);
                    
                    compliance.setCategory(child6);
                    
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
    
    private Category createChildCategory(Row nextRow, int col, Category parent) {
        Category child = null;
        String childName = nextRow.getCell(col).getStringCellValue();
        if (childName != null && !"".equals(childName)) {
            child = categoryRepository.findByNameAndParent(childName, parent);
            if (child == null) {
                child = new Category();
                child.setName(childName);
                child.setParent(parent);
                categoryRepository.save(child);
            }
        } else {
            return parent;
        }
        return child;
    }
}
