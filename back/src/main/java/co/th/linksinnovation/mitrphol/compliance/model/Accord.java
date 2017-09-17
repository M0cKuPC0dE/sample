/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

/**
 *
 * @author jirawong
 */
@Data
@Entity
public class Accord {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 4000)
    private String remark;
    @Column(length = 4000)
    private String remarkCoordinator;
    @Column(length = 4000)
    private String remarkApprover;
    @Enumerated(EnumType.STRING)
    private Accorded accorded;
    @Temporal(TemporalType.TIMESTAMP)
    private Date completeDate;
    @ManyToOne
    @JsonBackReference
    private LegalCategory legalCategory;
    @OneToOne
    private LegalDuty legalDuty;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<LicenseFile> licenseFiles;
    @OneToMany(cascade = CascadeType.MERGE)
    private List<EvidenceFile> evidenceFiles;
    private Boolean accept;
    private Boolean approve;
}
