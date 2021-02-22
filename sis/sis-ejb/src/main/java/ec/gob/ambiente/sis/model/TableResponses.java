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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Entity implementation class for Entity: TableResponses
 *
 */
@Entity
@Table(name = "table_responses", schema = "sis")
@NamedQueries({
	@NamedQuery(name = TableResponses.CARGA_RESPUESTAS_POR_AVANCE_EJECUCION,query = "SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.advanceExecutionSaveguards.adexIsReported=TRUE")	
})
public class TableResponses  {

	
	public static final String CARGA_RESPUESTAS_POR_AVANCE_EJECUCION="cargaRespuestasPorAvanceEjecucion";

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
	@JoinColumn(name = "quan_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private QuestionsAnswers questionsAnswers;
	
	@Getter
	@Setter
	@JoinColumn(name = "adex_id")
	@ManyToOne(fetch = FetchType.LAZY)	
	private AdvanceExecutionSafeguards advanceExecutionSaveguards;
}
