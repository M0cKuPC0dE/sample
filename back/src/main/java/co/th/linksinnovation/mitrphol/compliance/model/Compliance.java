/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import javax.persistence.Column;
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
    @Column(length = 4000)
    private String legalDuty;
    @ManyToOne
    @JsonView(JsonViewer.ComplianceWithCategory.class)
    private Category category;
    private Boolean deleted = false;
}
