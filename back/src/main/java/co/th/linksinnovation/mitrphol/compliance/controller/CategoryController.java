/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.controller;

import co.th.linksinnovation.mitrphol.compliance.model.Category;
import co.th.linksinnovation.mitrphol.compliance.repository.CategoryRepository;
import co.th.linksinnovation.mitrphol.compliance.service.CategoryService;
import java.util.List;
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
 * @author jirawong
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> get() {
        return categoryRepository.findByParentIsNullAndDeletedIsFalse();
    }

    @PostMapping
    public List<Category> post(@RequestBody Category category) {
        if (category.getParent().getId() != null) {
            Category child = categoryRepository.save(category);
            Category parent = categoryRepository.findOne(child.getParent().getId());
            parent.addChild(child);
            categoryRepository.save(parent);
        }else{
            category.setParent(null);
            Category child = categoryRepository.save(category);
        }
        return categoryRepository.findByParentIsNullAndDeletedIsFalse();
    }
    
    @PostMapping("/update")
    public List<Category> postUpdate(@RequestBody Category category) {
        Category findOne = categoryRepository.findOne(category.getId());
        Category parent = findOne.getParent();
        category.setParent(parent);
        categoryRepository.save(category);
        return categoryRepository.findByParentIsNullAndDeletedIsFalse();
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id){
        categoryService.deleteCategory(id);
    }
}