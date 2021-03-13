/**
@autor proamazonia [Christian Báez]  12 mar. 2021

**/
package ec.gob.ambiente.sis.bean;
import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;
import lombok.Getter;
import lombok.Setter;
@Named
@ViewScoped
public class SalvaguardaDBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaD321;
	
	@Getter
	@Setter
	private List<TableResponses> tablaSalvaguardaD331;
	
	@Getter
	@Setter
	private List<ValueAnswers> listaValoresRespuestasD;
	
	@Getter
	@Setter
	private List<Questions> listaPreguntasD;
}

