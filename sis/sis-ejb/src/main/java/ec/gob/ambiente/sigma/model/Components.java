/**
@autor proamazonia [Christian Báez]  20 sep. 2021

**/
package ec.gob.ambiente.sigma.model;

import java.io.Serializable;

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
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "components", schema = "sigma")
@NamedQueries({
	
	@NamedQuery(name = "a",query = "SELECT C FROM Projects PR, ActionPlans AP, Components C WHERE PR.actionPlans.acplId = AP.acplId AND AP.acplId = C.actionPlans.acplId AND PR.projId=:codigoProyecto AND c.compCode LIKE 'CE%'"),
})
public class Components implements Serializable{

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "comp_id")
    private Integer compId;
	
	@Getter
	@Setter
	@Column(name = "comp_code")
	private String compCode;
	
	@Getter
	@Setter
	@Column(name = "comp_name")
	private String compName;
	
	@Getter
	@Setter
	@Column(name = "comp_status")
	private boolean compStatus;
	
	@Getter
	@Setter
	@JoinColumn(name = "acpl_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private ActionPlans actionPlans;
}

