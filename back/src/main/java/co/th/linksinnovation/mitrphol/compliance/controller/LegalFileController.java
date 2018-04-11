/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.EvidenceFile;
import co.th.linksinnovation.mitrphol.compliance.model.LegalFile;
import co.th.linksinnovation.mitrphol.compliance.model.LicenseFile;
import co.th.linksinnovation.mitrphol.compliance.repository.EvidenceFileRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LegalFileRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LicenseFileRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@RestController
public class LegalFileController {

    @Autowired
    private LegalFileRepository legalFileRepository;
    @Autowired
    private LicenseFileRepository licenseFileRepository;
    @Autowired
    private EvidenceFileRepository evidenceFileRepository;

    @GetMapping("/public/download/{id}")
    public void download(@PathVariable("id") Long id, HttpServletResponse response) {

        InputStream in = null;
        try {
            final LegalFile legalFile = legalFileRepository.findOne(id);
            final String dataDirectory = "/mnt/data/files/" + legalFile.getUuid() == null ?legalFile.getName() : legalFile.getUuid();
            final File file = new File(dataDirectory);
            in = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("language", "th-TH");
            response.setContentType("application/octet-stream");
            FileCopyUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            Logger.getLogger(LegalFileController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(LegalFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @GetMapping("/public/download/license/{id}")
    public void downloadLicense(@PathVariable("id") Long id, HttpServletResponse response) {

        InputStream in = null;
        try {
            final LicenseFile legalFile = licenseFileRepository.findOne(id);
            final String dataDirectory = "/mnt/data/files/" + legalFile.getUuid() == null ?legalFile.getName() : legalFile.getUuid();
            final File file = new File(dataDirectory);
            in = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("language", "th-TH");
            response.setContentType("application/octet-stream");
            FileCopyUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            Logger.getLogger(LegalFileController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(LegalFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @GetMapping("/public/download/evidence/{id}")
    public void downloadEvidence(@PathVariable("id") Long id, HttpServletResponse response) {

        InputStream in = null;
        try {
            final EvidenceFile legalFile = evidenceFileRepository.findOne(id);
            final String dataDirectory = "/mnt/data/files/" + legalFile.getUuid() == null ?legalFile.getName() : legalFile.getUuid();
            final File file = new File(dataDirectory);
            in = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("language", "th-TH");
            response.setContentType("application/octet-stream");
            FileCopyUtils.copy(in, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {
            Logger.getLogger(LegalFileController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(LegalFileController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @PostMapping("/api/file")
    public LegalFile post(@RequestBody LegalFile legalFile) {
        return legalFileRepository.save(legalFile);
    }
}
