/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.repository;

import co.th.linksinnovation.mitrphol.compliance.model.Category;
import co.th.linksinnovation.mitrphol.compliance.model.Compliance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author jirawong
 */
public interface ComplianceRepository extends JpaRepository<Compliance, Long>{

    public List<Compliance> findByDeletedIsFalse();

    public List<Compliance> findByDeletedIsFalseAndCategory(Category findOne);

    @Query(value = "SELECT c FROM Compliance c WHERE (c.legalName LIKE ?1 OR c.legalDuty LIKE ?2) AND c.category=?3 AND c.deleted = false")
    public List<Compliance> searchWithCategory(String get, String get0, Category findOne);

    @Query(value = "SELECT c FROM Compliance c WHERE (c.legalName LIKE ?1 OR c.legalDuty LIKE ?2) AND c.deleted = false")
    public List<Compliance> searchWithoutCategory(String get, String get0);
    
}
