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
import javax.persistence.Lob;
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
public class AccordRemark {
    @Id
    @GeneratedValue
    private Long id;
    @Lob
    private String remark;
    @ManyToOne
    private UserDetails user;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    public AccordRemark() {
    }

    public AccordRemark(String remark, UserDetails user) {
        this.remark = remark;
        this.user = user;
    }
    
    
}
