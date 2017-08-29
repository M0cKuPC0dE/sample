/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.Where;

/**
 *
 * @author jirawong
 */
@Data
@Entity
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @ManyToOne
    @JsonBackReference
    private Category parent;
    @OneToMany(mappedBy = "parent")
    @JsonManagedReference
    @Where(clause = "deleted = 0")
    private List<Category> childs;
    private Boolean deleted = false;
    @OneToMany(mappedBy = "category")
    @JsonView(JsonViewer.CategoryWithCompliance.class)
    private List<Compliance> compliances;
    
    public void addChild(Category category){
        if(this.childs == null){
            this.childs = new ArrayList<>();
        }
        category.setParent(this);
        this.childs.add(category);
    }
}
