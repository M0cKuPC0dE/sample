/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.repository;

import co.th.linksinnovation.mitrphol.compliance.model.LegalGroup;
import co.th.linksinnovation.mitrphol.compliance.model.UserDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jirawong
 */
public interface LegalgroupRepository extends JpaRepository<LegalGroup, Long>{

    public List<LegalGroup> findByCoordinatesIn(UserDetails findOne);
    
}
