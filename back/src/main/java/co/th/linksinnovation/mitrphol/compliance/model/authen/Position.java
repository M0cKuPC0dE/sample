/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.th.linksinnovation.mitrphol.compliance.model.authen;

/**
 *
 * @author Piyawut Chiradejnunt<pchiradejnunt@gmail.com>
 */
@lombok.Data
public class Position {
    private String id;
    private Name name;
    private String department;
    private Group group;
    private Level level;
    private String costCenter;
}