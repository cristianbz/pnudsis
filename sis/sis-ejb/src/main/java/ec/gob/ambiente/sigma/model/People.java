/**
@autor proamazonia [Christian Báez]  5 may. 2021

**/
package ec.gob.ambiente.sigma.model;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "public.people")
public class People implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "peop_id")
    private Integer peopId;
    @Size(max = 255)
    @Column(name = "peop_name")
    private String peopName;
    @Size(max = 10)
    @Column(name = "peop_genre")
    private String peopGenre;
    @Size(max = 255)
    @Column(name = "peop_title")
    private String peopTitle;
    @Column(name = "peop_status")
    private Boolean peopStatus;
    @Size(max = 255)
    @Column(name = "peop_id_document")
    private String peopIdDocument;
    @Size(max = 255)
    @Column(name = "peop_position")
    private String peopPosition;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "peop_pin")
    private String peopPin;
    @Size(max = 20)
    @Column(name = "peop_cambio")
    private String peopCambio;
    @Size(max = 255)
    @Column(name = "peop_user_create")
    private String peopUserCreate;
    @Column(name = "peop_date_create")
    @Temporal(TemporalType.TIMESTAMP)
    private Date peopDateCreate;
    @Size(max = 255)
    @Column(name = "peop_user_update")
    private String peopUserUpdate;
    @Column(name = "peop_date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date peopDateUpdate;
    @Size(max = 255)
    @Column(name = "peop_ip_create")
    private String peopIpCreate;
    @Size(max = 255)
    @Column(name = "peop_ip_update")
    private String peopIpUpdate;
    @Size(max = 1024)
    @Column(name = "peop_observations")
    private String peopObservations;
    /*@OneToMany(mappedBy = "peopId")
    private List<Organizations> organizationsList;
    @JoinColumn(name = "gelo_id", referencedColumnName = "gelo_id")
    @ManyToOne
    private GeographicalLocations geloId;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users userId;
    @OneToMany(mappedBy = "peopId")
    private List<Users> usersList;*/

    public People() {
    }

    public People(Integer peopId) {
        this.peopId = peopId;
    }

    public People(Integer peopId, String peopPin) {
        this.peopId = peopId;
        this.peopPin = peopPin;
    }

    public Integer getPeopId() {
        return peopId;
    }

    public void setPeopId(Integer peopId) {
        this.peopId = peopId;
    }

    public String getPeopName() {
        return peopName;
    }

    public void setPeopName(String peopName) {
        this.peopName = peopName;
    }

    public String getPeopGenre() {
        return peopGenre;
    }

    public void setPeopGenre(String peopGenre) {
        this.peopGenre = peopGenre;
    }

    public String getPeopTitle() {
        return peopTitle;
    }

    public void setPeopTitle(String peopTitle) {
        this.peopTitle = peopTitle;
    }

    public Boolean getPeopStatus() {
        return peopStatus;
    }

    public void setPeopStatus(Boolean peopStatus) {
        this.peopStatus = peopStatus;
    }

    public String getPeopIdDocument() {
        return peopIdDocument;
    }

    public void setPeopIdDocument(String peopIdDocument) {
        this.peopIdDocument = peopIdDocument;
    }

    public String getPeopPosition() {
        return peopPosition;
    }

    public void setPeopPosition(String peopPosition) {
        this.peopPosition = peopPosition;
    }

    public String getPeopPin() {
        return peopPin;
    }

    public void setPeopPin(String peopPin) {
        this.peopPin = peopPin;
    }

    public String getPeopCambio() {
        return peopCambio;
    }

    public void setPeopCambio(String peopCambio) {
        this.peopCambio = peopCambio;
    }

    public String getPeopUserCreate() {
        return peopUserCreate;
    }

    public void setPeopUserCreate(String peopUserCreate) {
        this.peopUserCreate = peopUserCreate;
    }

    public Date getPeopDateCreate() {
        return peopDateCreate;
    }

    public void setPeopDateCreate(Date peopDateCreate) {
        this.peopDateCreate = peopDateCreate;
    }

    public String getPeopUserUpdate() {
        return peopUserUpdate;
    }

    public void setPeopUserUpdate(String peopUserUpdate) {
        this.peopUserUpdate = peopUserUpdate;
    }

    public Date getPeopDateUpdate() {
        return peopDateUpdate;
    }

    public void setPeopDateUpdate(Date peopDateUpdate) {
        this.peopDateUpdate = peopDateUpdate;
    }

    public String getPeopIpCreate() {
        return peopIpCreate;
    }

    public void setPeopIpCreate(String peopIpCreate) {
        this.peopIpCreate = peopIpCreate;
    }

    public String getPeopIpUpdate() {
        return peopIpUpdate;
    }

    public void setPeopIpUpdate(String peopIpUpdate) {
        this.peopIpUpdate = peopIpUpdate;
    }

    public String getPeopObservations() {
        return peopObservations;
    }

    public void setPeopObservations(String peopObservations) {
        this.peopObservations = peopObservations;
    }

    /*public List<Organizations> getOrganizationsList() {
        return organizationsList;
    }

    public void setOrganizationsList(List<Organizations> organizationsList) {
        this.organizationsList = organizationsList;
    }

    public GeographicalLocations getGeloId() {
        return geloId;
    }

    public void setGeloId(GeographicalLocations geloId) {
        this.geloId = geloId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (peopId != null ? peopId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof People)) {
            return false;
        }
        People other = (People) object;
        if ((this.peopId == null && other.peopId != null) || (this.peopId != null && !this.peopId.equals(other.peopId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.ambiente.sigma.ejb.entidades.People[ peopId=" + peopId + " ]";
    }
    
}
