/**
@autor proamazonia [Christian Báez]  20 sep. 2021

**/
package ec.gob.ambiente.sigma.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "components", schema = "sigma")
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
}

