/**
@autor proamazonia [Christian Báez]  7 oct. 2021

**/
package ec.gob.ambiente.sis.model;

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
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "advance_execution_project_gender", schema = "sis")
@NamedQueries({
	@NamedQuery(name = "c1",query = "SELECT AEPG FROM AdvanceExecutionProjectGender AEPG, ProjectGenderIndicator PGIN, ProjectsGenderInfo PGINF WHERE AEPG.projectGenderIndicator.pgigId = PGIN.pgigId AND PGINF.pginId = PGIN.projectsGenderInfo.pginId AND AEPG.aepgStatus= TRUE AND AEPG.advanceExecutionSafeguards.adexId=:codigoAvance"),
	@NamedQuery(name = "c2",query = "SELECT AEPG FROM AdvanceExecutionProjectGender AEPG, ProjectGenderIndicator PGIN, ProjectsGenderInfo PGINF WHERE AEPG.projectGenderIndicator.pgigId = PGIN.pgigId AND PGINF.pginId = PGIN.projectsGenderInfo.pginId AND AEPG.aepgStatus= TRUE AND AEPG.advanceExecutionSafeguards.projects.projId=:codigoProyecto AND PGIN.indicators.indiId=:codigoIndicador ORDER BY AEPG.advanceExecutionSafeguards.adexTermFrom")
		
})
public class AdvanceExecutionProjectGender {
	
	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "aepg_id")
    private Integer aepgId;
	
	@Getter
	@Setter
	@Column(name = "aepg_status")
	private boolean aepgStatus;
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private AdvanceExecutionSafeguards advanceExecutionSafeguards;
	
	@Getter
	@Setter
	@JoinColumn(name = "pgig_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private ProjectGenderIndicator projectGenderIndicator;
	
    @Getter
	@Setter
	@OneToMany(mappedBy = "advanceExecutionProjectGender", fetch = FetchType.EAGER)
	private List<DetailAdvanceGender> detailAdvanceGenderList;
    
    @Getter
    @Setter
    @Column(name = "aepg_value_reached_one")
    private Integer aepgValueReachedOne;
    
    @Getter
    @Setter
    @Column(name = "aepg_value_reached_two")
    private Integer aepgValueReachedTwo;
    
    @Getter
    @Setter
    @Column(name = "aepg_value_reached_another_indicator")
    private String aepgValueReachedAnotherIndicator;
    
        
    @Getter
    @Setter
    @Column(name = "aepg_actions_done")
    private String aepgActionsDone;
    
    @Transient
    @Getter
    @Setter
    private boolean aepgBooleanValue;
    
    @Transient
    @Getter
    @Setter
    private List<AdvanceExecutionProjectGender> listaReportesAnteriores;
}

