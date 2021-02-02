package ec.gob.ambiente.sis.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.TableResponses;

@Stateless
public class TableResponsesFacade extends AbstractFacade<TableResponses, Integer> implements Serializable {


	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(TableResponsesFacade.class);

	public TableResponsesFacade() {
		super(TableResponses.class,Integer.class);
	}
	
	/**
	 * Carga los valores de respuesta tipo tabla por avance de ejecucion
	 * @param codigoAvanceEjecucion
	 * @return
	 */
	public List<TableResponses> findByAdvanceExecution(int codigoAvanceEjecucion){
		try{
			Query query=getEntityManager().createNamedQuery(TableResponses.CARGA_RESPUESTAS_POR_AVANCE_EJECUCION);
			query.setParameter("codigoAvanceEjecucion", codigoAvanceEjecucion);
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}

}
