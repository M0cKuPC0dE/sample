/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

/**
 *
 * @author jirawong
 */
@Data
@Entity
public class EvidenceFile {
    @Id
    @GeneratedValue
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date warningDate;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expireDate;
    private Boolean expired = true;
    private String name;
    
    @PrePersist
    @PreUpdate
    public void pre(){
        if(!this.expired){
            this.warningDate = null;
            this.expireDate = null;
        }
    }
}
