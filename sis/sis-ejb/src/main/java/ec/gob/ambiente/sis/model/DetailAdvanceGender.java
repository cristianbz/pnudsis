/**
@autor proamazonia [Christian Báez]  31 may. 2021

**/
package ec.gob.ambiente.sis.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "detail_advance_gender", schema = "sis")
public class DetailAdvanceGender {
	
	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dtag_id")
    private Integer dtagId;
	
	@Getter
	@Setter
	@Column(name = "dtag_number_of_women")
	private Integer dtagNumberOfWomen;
		
	@Getter
	@Setter
	@Column(name = "dtag_province")
	private Integer dtagProvince;
	
	@Getter
	@Setter
	@Column(name = "dtag_canton")
	private Integer dtagCanton;
	
	@Getter
	@Setter
	@Column(name = "dtag_parish")
	private Integer dtagParish;
	
	@Getter
	@Setter
	@Column(name = "dtag_number_of_mens")
	private Integer dtagNumberOfMens;
	
	@Getter
	@Setter
	@Column(name = "dtag_ethnicity")
	private Integer dtagEthnicity;
	
	@Getter
	@Setter
	@Column(name = "dtag_town")
	private Integer dtagTown;
		
	@Getter
	@Setter
	@Column(name = "dtag_comunity")
	private String dtagComunity;
	
	@Getter
	@Setter
	@Column(name = "dtag_status")
	private boolean dtagStatus;
	
	@Getter
	@Setter
	@Transient
	private String provincia;
	
	@Getter
	@Setter
	@Transient
	private String canton;
	
	@Getter
	@Setter
	@Transient
	private String parroquia;
	
	@Getter
	@Setter
	@Transient
	private String etnia;
	
	@Getter
	@Setter
	@Transient
	private String pueblo;
	
	@Getter
	@Setter
	@Transient
	private String lineaAccion;
	
	@Getter
	@Setter
	@JoinColumn(name = "gead_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private GenderAdvances genderAdvances;
}

