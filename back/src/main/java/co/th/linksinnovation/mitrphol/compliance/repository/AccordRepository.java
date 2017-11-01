/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.repository;

import co.th.linksinnovation.mitrphol.compliance.model.Accord;
import co.th.linksinnovation.mitrphol.compliance.model.LegalCategory;
import co.th.linksinnovation.mitrphol.compliance.model.LegalDuty;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jirawong
 */
public interface AccordRepository extends JpaRepository<Accord, Long>{
    public Accord findByLegalCategoryAndLegalDuty(LegalCategory lc,LegalDuty c);

    public List<Accord> findByLegalCategory(LegalCategory le);
}
