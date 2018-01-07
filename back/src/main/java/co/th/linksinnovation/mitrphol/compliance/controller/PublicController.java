/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jirawong
 */
@RestController
public class PublicController {
    
    @Autowired
    private JobService jobService;
    
    @GetMapping("/public/resetmail")
    public void resetMail(){
        jobService.resetAccordIsExpire();
    }
    
    @GetMapping("/public/resetall")
    public void resetAllLast() {
        jobService.resetAccordAllExpire();
    }
}
