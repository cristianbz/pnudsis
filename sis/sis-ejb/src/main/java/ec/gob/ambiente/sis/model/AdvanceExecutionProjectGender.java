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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "advance_execution_project_gender", schema = "sis")
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
}

