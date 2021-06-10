/**
@autor proamazonia [Christian Báez]  31 may. 2021

**/
package ec.gob.ambiente.sigma.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity 
@Table(name = "sigma.catalogs_types")
@NamedQueries({
    @NamedQuery(name = "CatalogsType.findAll", query = "SELECT c FROM CatalogsType c")})
public class CatalogType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "caty_id")
    private Integer catyId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "caty_mnemonic")
    private String catyMnemonic;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "caty_description")
    private String catyDescription;
    @Column(name = "caty_status")
    private Boolean catyStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catyId")
    private List<Catalog> catalogList;

    public CatalogType() {
    }

    public CatalogType(Integer catyId) {
        this.catyId = catyId;
    }

    public CatalogType(Integer catyId, String catyMnemonic, String catyDescription) {
        this.catyId = catyId;
        this.catyMnemonic = catyMnemonic;
        this.catyDescription = catyDescription;
    }

    public Integer getCatyId() {
        return catyId;
    }

    public void setCatyId(Integer catyId) {
        this.catyId = catyId;
    }

    public String getCatyMnemonic() {
        return catyMnemonic;
    }

    public void setCatyMnemonic(String catyMnemonic) {
        this.catyMnemonic = catyMnemonic;
    }

    public String getCatyDescription() {
        return catyDescription;
    }

    public void setCatyDescription(String catyDescription) {
        this.catyDescription = catyDescription;
    }

    public Boolean getCatyStatus() {
        return catyStatus;
    }

    public void setCatyStatus(Boolean catyStatus) {
        this.catyStatus = catyStatus;
    }

    public List<Catalog> getCatalogList() {
        return catalogList;
    }

    public void setCatalogList(List<Catalog> catalogList) {
        this.catalogList = catalogList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (catyId != null ? catyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CatalogType)) {
            return false;
        }
        CatalogType other = (CatalogType) object;
        if ((this.catyId == null && other.catyId != null) || (this.catyId != null && !this.catyId.equals(other.catyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.ambiente.sigma.ejb.entidades.CatalogsType[ catyId=" + catyId + " ]";
    }
    
}

