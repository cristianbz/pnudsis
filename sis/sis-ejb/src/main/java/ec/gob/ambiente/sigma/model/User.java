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
@Table(name = "public.users")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM User u")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer userId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_name")
    private String userName;
    @Size(max = 255)
    @Column(name = "user_password")
    private String userPassword;
    @Size(max = 255)
    @Column(name = "user_docu_id")
    private String userDocuId;
    @Size(max = 255)
    @Column(name = "user_temp_password")
    private String userTempPassword;
    @Column(name = "user_token")
    private Boolean userToken;
    @Size(max = 45)
    @Column(name = "user_creation_user")
    private String userCreationUser;
    @Column(name = "user_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userCreationDate;
    @Size(max = 45)
    @Column(name = "user_user_update")
    private String userUserUpdate;
    @Column(name = "user_date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userDateUpdate;
    @Size(max = 255)
    @Column(name = "justification_access")
    private String justificationAccess;
    @Size(max = 255)
    @Column(name = "temp_password")
    private String tempPassword;
    @Column(name = "user_status")
    private Boolean userStatus;
    @Column(name = "user_subrogante")
    private Boolean userSubrogante;
    @Size(max = 255)
    @Column(name = "user_creator_user")
    private String userCreatorUser;
    @Column(name = "user_data_complete")
    private Boolean userDataComplete;
    @Column(name = "user_date_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userDateExpiration;
    @Column(name = "user_functionary")
    private Boolean userFunctionary;
    @Column(name = "user_edif_id")
    private Integer userEdifId;
    @Size(max = 255)
    @Column(name = "user_observations")
    private String userObservations;
    @Size(max = 255)
    @Column(name = "user_pin")
    private String userPin;
    @Size(max = 10000)
    @Column(name = "user_justification_access")
    private String userJustificationAccess;
    @Column(name = "user_central_functionary")
    private Boolean userCentralFunctionary;
    @Column(name = "user_is_area_boss")
    private Boolean userIsAreaBoss;
    @Column(name = "user_active_as_facilitator")
    private Boolean userActiveAsFacilitator;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "user_work_performance_ratio")
    private Double userWorkPerformanceRatio;
    @Column(name = "user_renew_user_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userRenewUserDate;
    @Column(name = "user_renew_pass_user")
    private Boolean userRenewPassUser;
    @Size(max = 255)
    @Column(name = "user_code_capcha")
    private String userCodeCapcha;
    @Column(name = "user_status_capcha")
    private Boolean userStatusCapcha;
    @Column(name = "user_date_expiration_link")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userDateExpirationLink;
   /* @OneToMany(mappedBy = "userId")
    private List<People> peopleList;*/
    @JoinColumn(name = "peop_id", referencedColumnName = "peop_id")
    @ManyToOne
    private People peopId;
    /*@OneToMany(mappedBy = "userImmediateSuperior")
    private List<Users> usersList;
    @JoinColumn(name = "user_immediate_superior", referencedColumnName = "user_id")
    @ManyToOne
    private Users userImmediateSuperior;*/

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
    }

    public User(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserDocuId() {
        return userDocuId;
    }

    public void setUserDocuId(String userDocuId) {
        this.userDocuId = userDocuId;
    }

    public String getUserTempPassword() {
        return userTempPassword;
    }

    public void setUserTempPassword(String userTempPassword) {
        this.userTempPassword = userTempPassword;
    }

    public Boolean getUserToken() {
        return userToken;
    }

    public void setUserToken(Boolean userToken) {
        this.userToken = userToken;
    }

    public String getUserCreationUser() {
        return userCreationUser;
    }

    public void setUserCreationUser(String userCreationUser) {
        this.userCreationUser = userCreationUser;
    }

    public Date getUserCreationDate() {
        return userCreationDate;
    }

    public void setUserCreationDate(Date userCreationDate) {
        this.userCreationDate = userCreationDate;
    }

    public String getUserUserUpdate() {
        return userUserUpdate;
    }

    public void setUserUserUpdate(String userUserUpdate) {
        this.userUserUpdate = userUserUpdate;
    }

    public Date getUserDateUpdate() {
        return userDateUpdate;
    }

    public void setUserDateUpdate(Date userDateUpdate) {
        this.userDateUpdate = userDateUpdate;
    }

    public String getJustificationAccess() {
        return justificationAccess;
    }

    public void setJustificationAccess(String justificationAccess) {
        this.justificationAccess = justificationAccess;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public Boolean getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Boolean userStatus) {
        this.userStatus = userStatus;
    }

    public Boolean getUserSubrogante() {
        return userSubrogante;
    }

    public void setUserSubrogante(Boolean userSubrogante) {
        this.userSubrogante = userSubrogante;
    }

    public String getUserCreatorUser() {
        return userCreatorUser;
    }

    public void setUserCreatorUser(String userCreatorUser) {
        this.userCreatorUser = userCreatorUser;
    }

    public Boolean getUserDataComplete() {
        return userDataComplete;
    }

    public void setUserDataComplete(Boolean userDataComplete) {
        this.userDataComplete = userDataComplete;
    }

    public Date getUserDateExpiration() {
        return userDateExpiration;
    }

    public void setUserDateExpiration(Date userDateExpiration) {
        this.userDateExpiration = userDateExpiration;
    }

    public Boolean getUserFunctionary() {
        return userFunctionary;
    }

    public void setUserFunctionary(Boolean userFunctionary) {
        this.userFunctionary = userFunctionary;
    }

    public Integer getUserEdifId() {
        return userEdifId;
    }

    public void setUserEdifId(Integer userEdifId) {
        this.userEdifId = userEdifId;
    }

    public String getUserObservations() {
        return userObservations;
    }

    public void setUserObservations(String userObservations) {
        this.userObservations = userObservations;
    }

    public String getUserPin() {
        return userPin;
    }

    public void setUserPin(String userPin) {
        this.userPin = userPin;
    }

    public String getUserJustificationAccess() {
        return userJustificationAccess;
    }

    public void setUserJustificationAccess(String userJustificationAccess) {
        this.userJustificationAccess = userJustificationAccess;
    }

    public Boolean getUserCentralFunctionary() {
        return userCentralFunctionary;
    }

    public void setUserCentralFunctionary(Boolean userCentralFunctionary) {
        this.userCentralFunctionary = userCentralFunctionary;
    }

    public Boolean getUserIsAreaBoss() {
        return userIsAreaBoss;
    }

    public void setUserIsAreaBoss(Boolean userIsAreaBoss) {
        this.userIsAreaBoss = userIsAreaBoss;
    }

    public Boolean getUserActiveAsFacilitator() {
        return userActiveAsFacilitator;
    }

    public void setUserActiveAsFacilitator(Boolean userActiveAsFacilitator) {
        this.userActiveAsFacilitator = userActiveAsFacilitator;
    }

    public Double getUserWorkPerformanceRatio() {
        return userWorkPerformanceRatio;
    }

    public void setUserWorkPerformanceRatio(Double userWorkPerformanceRatio) {
        this.userWorkPerformanceRatio = userWorkPerformanceRatio;
    }

    public Date getUserRenewUserDate() {
        return userRenewUserDate;
    }

    public void setUserRenewUserDate(Date userRenewUserDate) {
        this.userRenewUserDate = userRenewUserDate;
    }

    public Boolean getUserRenewPassUser() {
        return userRenewPassUser;
    }

    public void setUserRenewPassUser(Boolean userRenewPassUser) {
        this.userRenewPassUser = userRenewPassUser;
    }

    public String getUserCodeCapcha() {
        return userCodeCapcha;
    }

    public void setUserCodeCapcha(String userCodeCapcha) {
        this.userCodeCapcha = userCodeCapcha;
    }

    public Boolean getUserStatusCapcha() {
        return userStatusCapcha;
    }

    public void setUserStatusCapcha(Boolean userStatusCapcha) {
        this.userStatusCapcha = userStatusCapcha;
    }

    public Date getUserDateExpirationLink() {
        return userDateExpirationLink;
    }

    public void setUserDateExpirationLink(Date userDateExpirationLink) {
        this.userDateExpirationLink = userDateExpirationLink;
    }

    /*public List<People> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(List<People> peopleList) {
        this.peopleList = peopleList;
    }*/

    public People getPeopId() {
        return peopId;
    }

    public void setPeopId(People peopId) {
        this.peopId = peopId;
    }
/*
    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public Users getUserImmediateSuperior() {
        return userImmediateSuperior;
    }

    public void setUserImmediateSuperior(Users userImmediateSuperior) {
        this.userImmediateSuperior = userImmediateSuperior;
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ec.gob.ambiente.sigma.ejb.entidades.Users[ userId=" + userId + " ]";
    }
    
}
