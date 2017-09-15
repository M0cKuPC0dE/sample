/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.Where;

/**
 *
 * @author jirawong
 */
@Data
@Entity
public class Compliance {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 4000)
    private String legalName;
    private Integer year;
    @Temporal(TemporalType.TIMESTAMP)
    private Date publicDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(length = 1000)
    private String department;
    @Column(length = 1000)
    private String ministry;
    @Column(length = 4000)
    private String important;
    @OneToMany(mappedBy = "compliance")
    @Where(clause = "deleted = 0")
    @JsonView(JsonViewer.ComplianceWithLegalDuty.class)
    private List<LegalDuty> legalDuties;
    @Column(length = 4000)
    private String tags;
    @OneToMany(mappedBy = "compliance")
    @JsonManagedReference
    public List<LegalFile> legalFiles;
    @ManyToOne
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    private Category category;
    private Boolean deleted = false;
    
    public List<LegalDuty> addLegalDuties(LegalDuty legalDuty){
        if(this.legalDuties == null){
            this.legalDuties = new ArrayList<>();
        }
        this.legalDuties.add(legalDuty);
        return this.legalDuties;
    }
}
