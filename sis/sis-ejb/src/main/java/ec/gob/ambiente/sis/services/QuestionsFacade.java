package ec.gob.ambiente.sis.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.Questions;

@Stateless
public class QuestionsFacade extends AbstractFacade<Questions, Integer> implements Serializable {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(QuestionsFacade.class);

	public QuestionsFacade() {
		super(Questions.class, Integer.class);
	}
	/**
	 * Lista de todas las preguntas ingresadas
	 */
	@SuppressWarnings("unchecked")
	public List<Questions> findAll(){
		try{
			Query query= getEntityManager().createNamedQuery(Questions.CARGAR_PREGUNTAS);
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
	/**
	 * Consulta preguntas por salvaguarda
	 * @return
	 */
	public List<Questions> findBySafeguard(int codigoSalvaguarda){
		try{
			Query query= getEntityManager().createNamedQuery(Questions.CARGAR_PREGUNTAS_POR_SALVAGUARDA);
			query.setParameter("codigoSalvaguarda", codigoSalvaguarda);
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
}
