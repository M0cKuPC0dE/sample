/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.EvidenceFile;
import co.th.linksinnovation.mitrphol.compliance.model.LicenseFile;
import co.th.linksinnovation.mitrphol.compliance.repository.AccordRepository;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author jirawong
 */
@Service
public class JobService {
    @Autowired
    private AccordRepository accordRepository;
    
    @Scheduled(cron = "0 0 9 * * *")
    public void resetAccordIsExpire(){
        List<Accord> findAll = accordRepository.findAll();
        for (Accord accord : findAll) {
            boolean flag = false;
            for (LicenseFile lf : accord.getLicenseFiles()) {
                if (lf != null && lf.getExpireDate().equals(new Date())) {
                    flag = true;
                    break;
                }
            }
            for (EvidenceFile ef : accord.getEvidenceFiles()) {
                if (ef != null && ef.getExpireDate().equals(new Date())) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                accord.setAccorded(null);
                accord.setAccept(null);
                accord.setApprove(null);
                accordRepository.save(accord);
            }
        }
    }
}
