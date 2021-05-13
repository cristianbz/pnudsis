/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.ambiente.suia.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dguano@proamazonia.org
 */
@Entity
@Table(name = "public.organizations")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "orga_id")
    private Integer orgaId;
    @Column(name = "gelo_id")
    private Integer geloId;
    @Column(name = "orga_status")
    private Boolean orgaStatus;
    @Size(max = 255)
    @Column(name = "orga_charge_legal_representative")
    private String orgaChargeLegalRepresentative;
    @Size(max = 255)
    @Column(name = "orga_name_organization")
    private String orgaNameOrganization;
    @Size(max = 255)
    @Column(name = "orga_state_participation")
    private String orgaStateParticipation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "orga_ruc")
    private String orgaRuc;
    @Column(name = "org_qualified")
    private Boolean orgQualified;
    @Size(max = 255)
    @Column(name = "org_name_comercial")
    private String orgNameComercial;
    @Size(max = 255)
    @Column(name = "orga_activity")
    private String orgaActivity;
    @Size(max = 255)
    @Column(name = "orga_user_create")
    private String orgaUserCreate;
    @Column(name = "orga_date_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orgaDateCreate;
    @Size(max = 255)
    @Column(name = "orga_user_update")
    private String orgaUserUpdate;
    @Column(name = "orga_date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date orgaDateUpdate;
    @Size(max = 255)
    @Column(name = "orga_ip_create")
    private String orgaIpCreate;
    @Size(max = 255)
    @Column(name = "orga_ip_update")
    private String orgaIpUpdate;
    /*@JoinColumn(name = "peop_id", referencedColumnName = "peop_id")
    @ManyToOne
    private People peopId;*/

    public Organization() {
    }

    public Organization(Integer orgaId) {
        this.orgaId = orgaId;
    }

    public Organization(Integer orgaId, String orgaRuc) {
        this.orgaId = orgaId;
        this.orgaRuc = orgaRuc;
    }

    public Integer getOrgaId() {
        return orgaId;
    }

    public void setOrgaId(Integer orgaId) {
        this.orgaId = orgaId;
    }

    public Integer getGeloId() {
        return geloId;
    }

    public void setGeloId(Integer geloId) {
        this.geloId = geloId;
    }

    public Boolean getOrgaStatus() {
        return orgaStatus;
    }

    public void setOrgaStatus(Boolean orgaStatus) {
        this.orgaStatus = orgaStatus;
    }

    public String getOrgaChargeLegalRepresentative() {
        return orgaChargeLegalRepresentative;
    }

    public void setOrgaChargeLegalRepresentative(String orgaChargeLegalRepresentative) {
        this.orgaChargeLegalRepresentative = orgaChargeLegalRepresentative;
    }

    public String getOrgaNameOrganization() {
        return orgaNameOrganization;
    }

    public void setOrgaNameOrganization(String orgaNameOrganization) {
        this.orgaNameOrganization = orgaNameOrganization;
    }

    public String getOrgaStateParticipation() {
        return orgaStateParticipation;
    }

    public void setOrgaStateParticipation(String orgaStateParticipation) {
        this.orgaStateParticipation = orgaStateParticipation;
    }

    public String getOrgaRuc() {
        return orgaRuc;
    }

    public void setOrgaRuc(String orgaRuc) {
        this.orgaRuc = orgaRuc;
    }

    public Boolean getOrgQualified() {
        return orgQualified;
    }

    public void setOrgQualified(Boolean orgQualified) {
        this.orgQualified = orgQualified;
    }

    public String getOrgNameComercial() {
        return orgNameComercial;
    }

    public void setOrgNameComercial(String orgNameComercial) {
        this.orgNameComercial = orgNameComercial;
    }

    public String getOrgaActivity() {
        return orgaActivity;
    }

    public void setOrgaActivity(String orgaActivity) {
        this.orgaActivity = orgaActivity;
    }

    public String getOrgaUserCreate() {
        return orgaUserCreate;
    }

    public void setOrgaUserCreate(String orgaUserCreate) {
        this.orgaUserCreate = orgaUserCreate;
    }

    public Date getOrgaDateCreate() {
        return orgaDateCreate;
    }

    public void setOrgaDateCreate(Date orgaDateCreate) {
        this.orgaDateCreate = orgaDateCreate;
    }

    public String getOrgaUserUpdate() {
        return orgaUserUpdate;
    }

    public void setOrgaUserUpdate(String orgaUserUpdate) {
        this.orgaUserUpdate = orgaUserUpdate;
    }

    public Date getOrgaDateUpdate() {
        return orgaDateUpdate;
    }

    public void setOrgaDateUpdate(Date orgaDateUpdate) {
        this.orgaDateUpdate = orgaDateUpdate;
    }

    public String getOrgaIpCreate() {
        return orgaIpCreate;
    }

    public void setOrgaIpCreate(String orgaIpCreate) {
        this.orgaIpCreate = orgaIpCreate;
    }

    public String getOrgaIpUpdate() {
        return orgaIpUpdate;
    }

    public void setOrgaIpUpdate(String orgaIpUpdate) {
        this.orgaIpUpdate = orgaIpUpdate;
    }

    /*public People getPeopId() {
        return peopId;
    }

    public void setPeopId(People peopId) {
        this.peopId = peopId;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (orgaId != null ? orgaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Organization)) {
            return false;
        }
        Organization other = (Organization) object;
        if ((this.orgaId == null && other.orgaId != null) || (this.orgaId != null && !this.orgaId.equals(other.orgaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.ambiente.sigma.ejb.entidades.Organizations[ orgaId=" + orgaId + " ]";
    }
    
}
