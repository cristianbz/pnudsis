package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: ProjectGenderindicator
 *
 */
@Entity
@Table(name = "sis.project_gender_indicator")

public class ProjectGenderIndicator implements Serializable {

	
	private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pgig_id")
    private Integer pgigId;
    
    @Getter
    @Setter
    @JoinColumn(name = "indi_id", referencedColumnName = "indi_id")
    @ManyToOne    
    private Indicators indicators;
    
    @Getter
    @Setter
    @JoinColumn(name = "pgin_id", referencedColumnName = "pgin_id")
    @ManyToOne    
    private ProjectsGenderInfo projectsGenderInfo;
    
	@Getter
	@Setter
	@OneToMany(mappedBy = "projectGenderIndicator", fetch = FetchType.LAZY)
	private List<AdvanceExecutionProjectGender> advanceExecutionProjectGenderList;
    
    @Getter
    @Setter
    @Column(name = "pgig_base_line")
    private String pgigBaseLine;
    
    @Getter
    @Setter
    @Column(name = "pgig_goals")
    private String pgigGoals;
    
    @Getter
    @Setter
    @Column(name = "pgig_goal_value_one")
    private Integer pgigGoalValueOne;
    
    @Getter
    @Setter
    @Column(name = "pgig_goal_value_two")
    private Integer pgigGoalValueTwo;
    
    @Getter
    @Setter
    @Column(name = "pgig_another_indicator")
    private String pgigAnotherIndicator;
    
    @Getter
    @Setter
    @Column(name = "pgig_value_another_indicator")
    private String pgigValueAnotherIndicator;
    
    @Getter
    @Setter
    @Column(name = "pgig_value_reached_one")
    private Integer pgigValueReachedOne;
    
    @Getter
    @Setter
    @Column(name = "pgig_value_reached_two")
    private Integer pgigValueReachedTwo;
    
    @Getter
    @Setter
    @Column(name = "pgig_value_reached_another_indicator")
    private String pgigValueReachedAnotherIndicator;
    
        
    @Getter
    @Setter
    @Column(name = "pgig_actions_done")
    private String pgigActionsDone;
    
    @Getter
    @Setter
    @Column(name = "pgig_status")
    private boolean pgigStatus;
    
    @Transient
    @Getter
    @Setter
    private boolean pgigBooleanValue;
}
