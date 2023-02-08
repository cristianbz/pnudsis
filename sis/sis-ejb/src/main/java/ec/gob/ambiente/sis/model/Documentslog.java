/**
@autor proamazonia [Christian Báez]  1 dic. 2022

**/
package ec.gob.ambiente.sis.model;

import java.io.Serializable;
import java.util.Date;

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

import ec.gob.ambiente.sigma.model.Projects;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sis.documentslog")
public class Documentslog implements Serializable{

	private static final long serialVersionUID = 1L;
	public static String BUSCATODOS="buscaTodos";
	public static String BUSCAPORFECHA="buscaPorFecha";
	
	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dclo_id")
    private Integer dcloId;
	
	@Getter
	@Setter
	@Column(name ="dclo_document_number")
	private String dcloDocumentNumber;
	
	@Getter
	@Setter
	@Column(name ="dclo_send_date")
	private Date dcloSendDate;
	
	@Getter
	@Setter
	@Column(name ="dclo_subject")
	private String dcloSubject;
		
	@Getter
	@Setter
	@Column(name ="dclo_sender")
	private String dcloSender;
	
	@Getter
	@Setter
	@Column(name ="dclo_status")
	private boolean dcloStatus;
	
	@Getter
	@Setter
	@Column(name ="dclo_addressee")
	private String dcloAddressee;
	
	@Getter
	@Setter
	@Column(name ="dclo_institution")
	private String dcloInstitution;
	
	@Getter
	@Setter
	@Column(name ="dclo_creation_date")
	private Date dcloCreationDate;
	
	@Getter
	@Setter
	@Column(name ="dclo_creator_user")
	private String dcloCreatorUser;
	
	@Getter
	@Setter
	@Column(name ="dclo_update_user")
	private String dcloUpdateUser;
	
	@Getter
	@Setter
	@Column(name ="dclo_update_date")
	private Date dcloUpdateDate;
	
	@Getter
	@Setter
	@Column(name ="dclo_dba_observation")
	private String dcloDbaObservation;
	
	
	@Getter
	@Setter
	@JoinColumn(name = "proj_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private Projects projects;
}

