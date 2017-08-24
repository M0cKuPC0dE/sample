/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.service;

import co.th.linksinnovation.mitrphol.compliance.model.Category;
import co.th.linksinnovation.mitrphol.compliance.model.Compliance;
import co.th.linksinnovation.mitrphol.compliance.repository.CategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.repository.ComplianceRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jirawong
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ComplianceRepository complianceRepository;

    public void deleteCategory(Long id) {
        Category findOne = categoryRepository.findOne(id);
        if (!findOne.getChilds().isEmpty()) {
            for (Category category : findOne.getChilds()) {
                deleteCategory(category.getId());
            }
        }

        List<Compliance> compliances = complianceRepository.findByCategory(findOne);
        for (Compliance compliance : compliances) {
            compliance.setDeleted(Boolean.TRUE);
        }
        findOne.setDeleted(Boolean.TRUE);
        complianceRepository.save(compliances);
        categoryRepository.save(findOne);
    }
}
