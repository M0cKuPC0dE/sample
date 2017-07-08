/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@RestController
public class TestController {

    @GetMapping("/hello")
    public String getHello() {
        return "Hello, User";
    }
    
    @GetMapping("/api/test")
    public String callAPI() {
        return "Hello, Authencated User";
    }
    
}
