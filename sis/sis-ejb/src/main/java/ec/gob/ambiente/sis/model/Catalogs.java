package ec.gob.ambiente.sis.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: Catalogs
 *
 */
@Entity
@Table(name = "catalogs", schema = "sis")

public class Catalogs {

	
	

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cata_id")
    private Integer cataId;
   
	@Getter
	@Setter
    @Column(name = "cata_order")
    private Integer cataOrder;

	@Getter
	@Setter
//    @Basic(optional = false)
//    @NotNull
//    @Size(min = 1, max = 100)
    @Column(name = "cata_text1")
    private String cataText1;
    
	@Getter
	@Setter
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "cata_text2")
    private String cataText2;
    
	@Getter
	@Setter
    @Column(name = "cata_number")
    private Integer cataNumber;
    
	@Getter
	@Setter
    @Column(name = "cata_level")
    private Integer cataLevel;
    
	@Getter
	@Setter
    @Column(name = "cata_status")
    private Boolean cataStatus;
    
	@Getter
	@Setter
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "cata_creator_user")
    private String cataCreatorUser;
    
	@Getter
	@Setter
    @Basic(optional = false)
    @NotNull
    @Column(name = "cata_creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cataCreationDate;
    
	@Getter
	@Setter
    @Size(max = 255)
    @Column(name = "cata_user_update")
    private String cataUserUpdate;
    
	@Getter
	@Setter
    @Column(name = "cata_date_update")
    @Temporal(TemporalType.TIMESTAMP)
    private Date catalDateUpdate;
    
	@Getter
	@Setter
    @Size(max = 1024)
    @Column(name = "cata_dba_observation")
    private String cataDbaObservation;
    
    @OneToMany(mappedBy = "cataParentId")
    private List<Catalogs> catalogsList;
    
    @JoinColumn(name = "cata_parent_id", referencedColumnName = "cata_id")
    @ManyToOne
    private Catalogs cataParentId;
    
	@Getter
	@Setter
    @JoinColumn(name = "caty_id", referencedColumnName = "caty_id")
    @ManyToOne(optional = false)
    private CatalogsType catalogsType;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "catalogs")
    private List<Questions> questionsList;

}
