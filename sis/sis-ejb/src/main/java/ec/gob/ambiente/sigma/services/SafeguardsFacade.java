package ec.gob.ambiente.sigma.services;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
public class SafeguardsFacade extends AbstractFacade<Safeguards, Integer> implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(SafeguardsFacade.class);

	public SafeguardsFacade() {
		super(Safeguards.class,Integer.class);
	}

	/**
	 * Carga todas las salvaguardas registradas
	 */
	@SuppressWarnings("unchecked")
	public List<Safeguards> findAll(){
		try{
			Query query=getEntityManager().createNamedQuery(Safeguards.CARGAR_TODAS_SALVAGUARDAS);
			return query.getResultList();
		}catch(NoResultException e){
			return null;
		}
	}
}
