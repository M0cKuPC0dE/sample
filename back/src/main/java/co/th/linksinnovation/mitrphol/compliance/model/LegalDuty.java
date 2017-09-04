/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 *
 * @author jirawong
 */
@Data
@Entity
public class LegalDuty {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 4000)
    private String name;
    @ManyToOne
    @JsonView(JsonViewer.LegalDutyWithCompliance.class)
    private Compliance compliance;
}
