package ec.gob.ambiente.sis.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: TableResponses
 *
 */
@Entity
@Table(name = "table_responses", schema = "sis")
@NamedQueries({
	@NamedQuery(name = TableResponses.CARGA_RESPUESTAS_POR_AVANCE_EJECUCION,query = "SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.advanceExecutionSaveguards.adexIsReported=TRUE AND TR.tareStatus=TRUE")
	
})
public class TableResponses  {

	
	public static final String CARGA_RESPUESTAS_POR_AVANCE_EJECUCION="cargaRespuestasPorAvanceEjecucion";
	
	public TableResponses(){
		
	}
	
	public TableResponses(Questions pregunta,AdvanceExecutionSafeguards avanceEjecucion) {
		this.questions=pregunta;
		this.advanceExecutionSaveguards = avanceEjecucion;
	}

	@Getter
	@Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tare_id")
    private Integer tareId;
	
	@Getter
	@Setter
	@Column(name = "tare_column_one")
	private String tareColumnOne;
	
	@Getter
	@Setter
	@Column(name = "tare_column_two")
	private String tareColumnTwo;
	
	@Getter
	@Setter
	@Column(name = "tare_column_three")
	private String tareColumnTree;
	
	@Getter
	@Setter
	@Column(name = "tare_column_four")
	private String tareColumnFour;
	
	@Getter
	@Setter
	@Column(name = "tare_column_five")
	private String tareColumnFive;
	
	@Getter
	@Setter
	@Column(name = "tare_column_six")
	private String tareColumnSix;
	
	@Getter
	@Setter
	@Column(name = "tare_column_seven")
	private String tareColumnSeven;
	
	@Getter
	@Setter
	@Column(name = "tare_column_eight")
	private String tareColumnEight;
	
	@Getter
	@Setter
	@Column(name = "tare_column_nine")
	private Date tareColumnNine;
	
	@Getter
	@Setter
	@Column(name = "tare_column_ten")
	private String tareColumnTen;
	
	@Getter
	@Setter
	@Column(name = "tare_column_eleven")
	private String tareColumnEleven;
	
	@Getter
	@Setter
	@Column(name = "tare_column_twelve")
	private String tareColumnTwelve;
	
	@Getter
	@Setter
	@Column(name = "tare_column_thirteen")
	private String tareColumnThirteen;
	
	@Getter
	@Setter
	@Column(name = "tare_column_decimal_one")
	private double tareColumnDecimalOne;
	
	@Getter
	@Setter
	@Column(name = "tare_column_decimal_two")
	private double tareColumnDecimalTwo;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_one")
	private Integer tareColumnNumberOne;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_two")
	private Integer tareColumnNumberTwo;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_three")
	private Integer tareColumnNumberThree;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_four")
	private Integer tareColumnNumberFour;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_five")
	private Integer tareColumnNumberFive;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_six")
	private Integer tareColumnNumberSix;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_seven")
	private Integer tareColumnNumberSeven;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_eight")
	private Integer tareColumnNumberEight;
	
	@Getter
	@Setter
	@Column(name = "tare_column_number_nine")
	private Integer tareColumnNumberNine;
	
	@Getter
	@Setter
	@Column(name = "tare_code_component")
	private Integer tareCodeComponent;
	
	@Getter
	@Setter
	@Column(name = "tare_law_political")
	private Integer tareLawPolitical;
	
	@Getter
	@Setter
	@Column(name = "tare_another_catalog")
	private String tareAnotherCatalog;
	
	@Getter
	@Setter
	@Transient
	private String tareProvincia;
	
	@Getter
	@Setter
	@Transient
	private String tareCanton;
	
	@Getter
	@Setter
	@Transient
	private String tareParroquia;
	
	@Getter
	@Setter
	@Transient
	private String tareCatPlanGobierno;
	
	@Getter
	@Setter
	@Transient
	private String tareComponente;
		
	@Getter
	@Setter
	@Transient
	@Column(name = "tare_Generico")
	private String tareGenerico;
	
	@Getter
	@Setter
	@Transient
	@Column(name = "tare_Generico_dos")
	private String tareGenericoDos;
	
	@Getter
	@Setter
	@Transient
	@Column(name = "tare_Generico_tres")
	private String tareGenericoTres;
	
	@Getter
	@Setter
	@Transient
	@Column(name = "tare_Generico_cuatro")
	private String tareGenericoCuatro;
	
	@Getter
	@Setter
	@Transient
	@Column(name = "tare_GenericoNumerico")
	private double tareGenericoNumerico;
	
	@Getter
	@Setter
	@Column(name = "tare_status")
	private boolean tareStatus;
	
	@Getter
	@Setter
	@JoinColumn(name = "ques_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private Questions questions;
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.EAGER)	
	private AdvanceExecutionSafeguards advanceExecutionSaveguards;
}
