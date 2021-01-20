package ec.gob.ambiente.sis.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.Answers;

@Stateless
public class AnswersFacade extends AbstractFacade<Answers, Integer> implements Serializable{


	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(AnswersFacade.class);

	public AnswersFacade() {
		super(Answers.class, Integer.class);

	}
	/**
	 * Carga todas las respuestas registradas
	 */
	@SuppressWarnings("unchecked")
	public List<Answers> findAll(){
		try{
			Query query= getEntityManager().createNamedQuery(Answers.CARGAR_TODAS_PREGUNTAS);
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}

}
