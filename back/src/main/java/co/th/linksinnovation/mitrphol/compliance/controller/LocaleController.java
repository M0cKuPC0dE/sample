/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.JsonViewer;
import co.th.linksinnovation.mitrphol.compliance.model.Language;
import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.repository.LocaleRespository;
import com.fasterxml.jackson.annotation.JsonView;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@RestController
@RequestMapping("/api/locales")
public class LocaleController {
    
    @Autowired
    private LocaleRespository localeRespository;
    
    @GetMapping
    public List<Language> get(){
        return localeRespository.findAll();
    }
    
    @GetMapping("code/{lang}")
    public Language getEditLocale(@PathVariable String lang) {
        return localeRespository.findOne(lang);
    }
    
    @PostMapping
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    public Language post(@RequestBody Language language) {
         return localeRespository.save(language);
   
    }
    
    @GetMapping("/{lang}")
    public String getLocale(@PathVariable String lang) {
        final String jsonFile = "/mnt/locales/"+ lang + ".json";
        final File file = new File(jsonFile);

        final StringBuilder result = new StringBuilder("");
        try (Scanner scanner = new Scanner(file)) {

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			result.append(line).append("\n");
		}

		scanner.close();

	} catch (IOException e) {
		e.printStackTrace();
	}
        JSONObject jsonResult  = new JSONObject(result.toString());
	return jsonResult.toString();
    }
    
    @DeleteMapping("/{lang}")
    public void delete(@PathVariable("lang") String lang) {
        localeRespository.delete(lang);
    }
}
