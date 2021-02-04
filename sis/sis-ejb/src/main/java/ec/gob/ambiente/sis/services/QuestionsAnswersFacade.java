package ec.gob.ambiente.sis.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.validator.HibernateValidator;


import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.QuestionsAnswers;

@Stateless
public class QuestionsAnswersFacade extends AbstractFacade<QuestionsAnswers, Integer> implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(QuestionsAnswersFacade.class);

	public QuestionsAnswersFacade() {
		super(QuestionsAnswers.class,Integer.class);
	}
	/**
	 * Consulta preguntas_respuestas por salvaguardas
	 * @param codigoSalvaguarda
	 * @return
	 */
	public List<QuestionsAnswers> findBySafegaurd(List<Integer> codigoSalvaguarda){
		try{
			Query query=getEntityManager().createNamedQuery(QuestionsAnswers.CARGA_PREGUNTAS_RESPUESTAS_POR_SALVAGUARDA);
			query.setParameter("codigoSalvaguarda", codigoSalvaguarda);
			return query.getResultList();
			
		}catch(NoResultException e){
			return null;
		}
	}

}
