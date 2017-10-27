/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.BusinessUnit;
import co.th.linksinnovation.mitrphol.compliance.model.Company;
import co.th.linksinnovation.mitrphol.compliance.model.Department;
import co.th.linksinnovation.mitrphol.compliance.model.Location;
import co.th.linksinnovation.mitrphol.compliance.repository.BusinessUnitRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.CompanyRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.DepartmentRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.LocationRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author jirawong
 */
@RestController
@RequestMapping("/api/lookup")
public class LookupController {
    
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private BusinessUnitRepository businessUnitRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @GetMapping("/company")
    public List<Company> getCompany(){
        return companyRepository.findAll();
    }
    
    @GetMapping("/location")
    public List<Location> getLocation(){
        return locationRepository.findAll();
    }
    
    @GetMapping("/businessunit")
    public List<BusinessUnit> getBusinessUnit(){
        return businessUnitRepository.findAll();
    }
    
    @GetMapping("/department")
    public List<Department> getDepartment(){
        return departmentRepository.findAll();
    }
}
