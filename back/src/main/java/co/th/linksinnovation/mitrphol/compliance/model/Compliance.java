/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

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
    private String legalName;
    private Integer year;
    @Temporal(TemporalType.DATE)
    private Date publicDate;
    @Temporal(TemporalType.DATE)
    private Date effectiveDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String department;
    private String ministy;
    private String important;
    private String legalDuty;
    @ManyToOne
    @JsonBackReference
    private Category category;
}
