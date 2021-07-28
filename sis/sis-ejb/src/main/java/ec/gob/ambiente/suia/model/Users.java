package ec.gob.ambiente.suia.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.ProjectUsers;
import ec.gob.ambiente.sis.model.Questions;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Users
 *
 */
@Entity
@Table(name = "users", schema = "public")
@NamedQueries({
	@NamedQuery(name = "q",query = "SELECT U FROM Users U WHERE U.userName=:usuario AND U.userPassword=:clave AND U.userStatus=TRUE")
	
})
public class Users {

	@Getter
	@Setter
	@Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "user_id")
    private Integer userId;
	
	@Getter
	@Setter
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "user_name")
    private String userName;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "user_password")
    private String userPassword;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "user_docu_id")
    private String userDocuId;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "user_temp_password")
    private String userTempPassword;
	
	@Getter
	@Setter
    @Column(name = "user_token")
    private Boolean userToken;
	
	@Getter
	@Setter
    @Size(max = 45)
    @Column(name = "user_creation_user")
    private String userCreationUser;
	
	@Getter
	@Setter
    @Column(name = "user_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userCreationDate;
	
	@Getter
	@Setter
    @Size(max = 45)
    @Column(name = "user_user_update")
    private String userUserUpdate;
	
	@Getter
	@Setter
    @Column(name = "user_date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userDateUpdate;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "justification_access")
    private String justificationAccess;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "temp_password")
    private String tempPassword;
	
	@Getter
	@Setter
    @Column(name = "user_status")
    private Boolean userStatus;
	
	@Getter
	@Setter
    @Column(name = "user_subrogante")
    private Boolean userSubrogante;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "user_creator_user")
    private String userCreatorUser;
	
	@Getter
	@Setter
    @Column(name = "user_data_complete")
    private Boolean userDataComplete;
	
	@Getter
	@Setter
    @Column(name = "user_date_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userDateExpiration;
	
	@Getter
	@Setter
    @Column(name = "user_functionary")
    private Boolean userFunctionary;
	
	@Getter
	@Setter
    @Column(name = "user_edif_id")
    private Integer userEdifId;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "user_observations")
    private String userObservations;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "user_pin")
    private String userPin;
	
	@Getter
	@Setter
    @Size(max = 10000)
    @Column(name = "user_justification_access")
    private String userJustificationAccess;
	
	@Getter
	@Setter
    @Column(name = "user_central_functionary")
    private Boolean userCentralFunctionary;
	
	@Getter
	@Setter
    @Column(name = "user_is_area_boss")
    private Boolean userIsAreaBoss;
	
	@Getter
	@Setter
    @Column(name = "user_active_as_facilitator")
    private Boolean userActiveAsFacilitator;
	
	@Getter
	@Setter
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "user_work_performance_ratio")
    private Double userWorkPerformanceRatio;
	
	@Getter
	@Setter
    @Column(name = "user_renew_user_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userRenewUserDate;
	
	@Getter
	@Setter
    @Column(name = "user_renew_pass_user")
    private Boolean userRenewPassUser;
	
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "user_code_capcha")
    private String userCodeCapcha;
	
	@Getter
	@Setter
    @Column(name = "user_status_capcha")
    private Boolean userStatusCapcha;
	
	@Getter
	@Setter
    @Column(name = "user_date_expiration_link")
    @Temporal(TemporalType.TIMESTAMP)
    private Date userDateExpirationLink;


    public Users() {
    }

    public Users(Integer userId) {
        this.userId = userId;
    }

    public Users(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

	
	@Getter
	@Setter
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private List<AdvanceExecutionSafeguards> advanceExecutionSaveguardsList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private List<ProjectUsers> projectUsersList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private List<RolesUser> rolesUserList;
   
}
