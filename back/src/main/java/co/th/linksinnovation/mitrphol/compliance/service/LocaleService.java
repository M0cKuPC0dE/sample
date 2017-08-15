/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.Language;
import co.th.linksinnovation.mitrphol.compliance.repository.LocaleRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@Service
public class LocaleService {
    
    @Autowired
    private LocaleRespository localeRespository;
    
    public Language save(String name) {
        Language language = localeRespository.findOne(name);
        if(language == null) {
            language = new Language();
            language.setCode(name);
            localeRespository.save(language);
        }
        return language;
    }
}
