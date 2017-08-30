/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
    @ManyToMany
    private List<UserDetails> owners;
    @ManyToMany
    private List<UserDetails> approvers;
    @ManyToMany
    private List<Compliance> compliances;
    @OneToOne
    private LegalGroup legalGroup;
}
