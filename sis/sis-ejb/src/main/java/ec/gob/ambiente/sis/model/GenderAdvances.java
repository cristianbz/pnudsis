/**
@autor proamazonia [Christian Báez]  31 may. 2021

**/
package ec.gob.ambiente.sis.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
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

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gender_advances", schema = "sis")
@NamedQueries({
    @NamedQuery(name = "GenderAdvances.findAll", query = "SELECT GA FROM GenderAdvances GA WHERE GA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion AND GA.advanceExecutionSafeguards.adexIsReported=false AND GA.advanceExecutionSafeguards.adexIsGender=true AND GA.advanceExecutionSafeguards.adexStatus=true "),
    @NamedQuery(name = "GenderAdvances.findProyecto", query = "SELECT GA FROM GenderAdvances GA WHERE GA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion AND GA.advanceExecutionSafeguards.adexIsReported=false AND GA.advanceExecutionSafeguards.adexIsGender=true AND GA.advanceExecutionSafeguards.adexStatus=true "),
    @NamedQuery(name = "GenderAdvances.AvancesSinReportar", query = "SELECT GA FROM GenderAdvances GA WHERE GA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion AND GA.advanceExecutionSafeguards.adexIsReported=false AND GA.advanceExecutionSafeguards.adexIsGender=true AND GA.advanceExecutionSafeguards.adexStatus=true ")
})
public class GenderAdvances {
	
	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "gead_id")
    private Integer geadId;
		

	@Getter
	@Setter
	@Column(name = "gead_observations")
	private String geadObservations;
	
		
	@Getter
	@Setter
	@Column(name = "gead_executed_budget")
	private double geadExecutedBudget; 
	
	@Getter
	@Setter
	@Column(name = "gead_advance_value_goal")
	private String geadAdvanceValueGoal;
	

	
	@Getter
	@Setter
	@Column(name = "gead_budget_actions")
	private String geadBudgetActions;

		
	@Getter
	@Setter
	@Column(name = "gead_status")
	private boolean geadStatus;
		
	@Getter
	@Setter
	@Column(name = "gead_creator_user")
	private String geadCreatorUser;
	
	@Getter
	@Setter
	@Column(name = "gead_creation_date")
	private Date geadCreationDate;
	
	@Getter
	@Setter
	@Column(name = "gead_update_user")
	private String geadUpdateUser;
	
	@Getter
	@Setter
	@Column(name = "gead_update_date")
	private Date geadUpdateDate;
	
	@Getter
	@Setter
	@Column(name = "gead_dba_observation")
	private String geadDbaObservation;
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private AdvanceExecutionSafeguards advanceExecutionSafeguards;
	
	@Getter
	@Setter
	@JoinColumn(name = "pgin_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private ProjectsGenderInfo projectsGenderInfo;
	
	
	@Getter
	@Setter
	@OneToMany(mappedBy = "genderAdvances", fetch = FetchType.EAGER)
	private List<DetailAdvanceGender> detailAdvanceGenderList;
}

