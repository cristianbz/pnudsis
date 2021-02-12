package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.Answers;

@Stateless
@LocalBean
public class AnswersFacade extends AbstractFacade<Answers, Integer> {


	public AnswersFacade() {
		super(Answers.class, Integer.class);

	}
	/**
	 * Carga todas las respuestas registradas
	 */	
	public List<Answers> buscarTodosLosProyectos() throws Exception{
		String sql="SELECT A FROM Answers A";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}

}
