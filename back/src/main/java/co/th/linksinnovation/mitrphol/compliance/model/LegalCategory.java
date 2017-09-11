/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;

/**
 *
 * @author jirawong
 */
@Data
@Entity
public class LegalCategory {
    @Id
    @GeneratedValue
    public Long id;
    public String party;
    public String department;
    public Boolean approved = false;
    @ManyToMany
    private Set<UserDetails> owners;
    @ManyToMany
    private Set<UserDetails> approvers;
    @ManyToMany
    private List<LegalDuty> legalDuties;
    @OneToOne
    private LegalGroup legalGroup;
    @OneToMany(mappedBy = "legalCategory",orphanRemoval = true)
    @JsonManagedReference
    private List<Accord> accords = new ArrayList<>();
}
