package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Indicators
 *
 */
@Entity
@Table(name = "sis.indicators")

public class Indicators implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
    @Getter
    @Setter
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "indi_id")
    private Integer indiId;
    
    @Getter
    @Setter
    @Size(max = 250)
    @Column(name = "indi_description")
    private String indiDescription;
    
    @Getter
    @Setter    
    @Column(name = "indi_status")
    private boolean indiStatus;
    
    @Getter
    @Setter
    @Size(max = 250)
    @Column(name = "indi_creator_user")
    private String indiCreatorUser;
    
    @Getter
    @Setter
    @Column(name = "indi_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date indiCreationDate;
    
    @Getter
    @Setter
    @Size(max = 250)
    @Column(name = "indi_update_user")
    private String indiUpdateUser;
    
    @Getter
    @Setter
    @Column(name = "indi_update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date indiUpdateDate;
    
    @Getter
    @Setter    
    @Column(name = "indi_dba_observation")
    private String indiDbaObservation;
    
    @Getter
	@Setter
	@OneToMany(mappedBy = "indicators", fetch = FetchType.LAZY)
	private List<ProjectsGenderInfo> projectGenderInfoList;
}
