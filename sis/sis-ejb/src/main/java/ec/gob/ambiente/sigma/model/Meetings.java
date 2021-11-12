/**
@autor proamazonia [Christian Báez]  11 nov. 2021

**/
package ec.gob.ambiente.sigma.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "sigma.meetings")
public class Meetings implements Serializable{

	private static final long serialVersionUID = 1L;
	
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "meet_id")
    private Integer meetId;
}

