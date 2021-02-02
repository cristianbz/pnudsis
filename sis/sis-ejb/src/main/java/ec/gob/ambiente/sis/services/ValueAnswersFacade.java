package ec.gob.ambiente.sis.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.ValueAnswers;

@Stateless
public class ValueAnswersFacade extends AbstractFacade<ValueAnswers, Integer> implements Serializable {


	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ValueAnswersFacade.class);

	public ValueAnswersFacade() {
		super(ValueAnswers.class,Integer.class);
	}
	/**
	 * Retorna los valores de respuestas por codigo de avance ejecucion
	 * @param codigoAvanceEjecucion
	 * @return
	 */
	public List<ValueAnswers> findByAdvanceExecution(int codigoAvanceEjecucion){
		try{
			Query query=getEntityManager().createNamedQuery(ValueAnswers.CARGA_POR_AVANCE_EJECUCION);
			query.setParameter("codigoAvanceEjecucion", codigoAvanceEjecucion);
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}

}
