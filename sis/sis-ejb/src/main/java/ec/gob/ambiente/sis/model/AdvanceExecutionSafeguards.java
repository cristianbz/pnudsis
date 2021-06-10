package ec.gob.ambiente.sis.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.suia.model.Users;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: AdvanceExecutionSaveguards
 *
 */
@Entity
@Table(name = "advance_execution_safeguards", schema = "sis")
@NamedQueries({
	@NamedQuery(name = AdvanceExecutionSafeguards.CARGAR_AVANCE_POR_PROYECTO,query = "SELECT AP FROM AdvanceExecutionSafeguards AP WHERE AP.projects.projId=:codigoProyecto AND AP.adexIsReported=false")	
})
public class AdvanceExecutionSafeguards {

	
	
	public static final String CARGAR_AVANCE_POR_PROYECTO="cargarAvancePorProyecto";

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "adex_id")
    private Integer adexId;
	
	@Getter
	@Setter
	@Column(name = "adex_register_date")
	private Date adexRegisterDate;
	
	@Getter
	@Setter
	@Column(name = "adex_is_reported")
	private boolean adexIsReported;
	
	@Getter
	@Setter
	@Column(name = "adex_status")
	private boolean adexStatus;
	
	@Getter
	@Setter
	@Column(name = "adex_is_gender")
	private boolean adexIsGender;
	
	@Getter
	@Setter
	@Column(name = "adex_creator_user")
	private String adexCreatorUser;
	
	@Getter
	@Setter
	@Column(name = "adex_creation_date")
	private Date adexCreationDate;
	
	@Getter
	@Setter
	@Column(name = "adex_update_user")
	private String adexUpdateUser;
	
	@Getter
	@Setter
	@Column(name = "adex_update_date")
	private Date adexUpdateDate;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSafeguards", fetch = FetchType.LAZY)
	private List<GenderAdvances> genderAdvancesList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSaveguards", fetch = FetchType.LAZY)
	private List<ValueAnswers> valueAnswersList;
   
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSaveguards", fetch = FetchType.LAZY)
	private List<TableResponses> tableResponsesList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSafeguards", fetch = FetchType.LAZY)
	private List<ExecutiveSummaries> executiveSummariesList;
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionSafeguards", fetch = FetchType.EAGER,cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE})
	private List<AdvanceSectors> advanceSectorsList;
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Projects projects;
	
	@Getter
	@Setter
	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Users users;
}
