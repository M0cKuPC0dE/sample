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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void deleteCategory(Category category) {
        if (!category.getChilds().isEmpty()) {
            CopyOnWriteArrayList<Category> copyOnWriteArrayList = new CopyOnWriteArrayList<>(category.getChilds());
            for(Category cat : copyOnWriteArrayList){
                Category findOne = categoryRepository.findOne(cat.getId());
                deleteCategory(findOne);
            }
        }

        List<Compliance> compliances = complianceRepository.findByDeletedIsFalseAndCategory(category);
        for (Compliance compliance : compliances) {
            compliance.setDeleted(Boolean.TRUE);
        }
        category.setDeleted(Boolean.TRUE);
        complianceRepository.save(compliances);
        categoryRepository.save(category);
    }
}
