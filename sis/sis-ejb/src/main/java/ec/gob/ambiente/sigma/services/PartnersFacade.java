package ec.gob.ambiente.sigma.services;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sis.dao.AbstractFacade;
@Stateless
public class PartnersFacade extends AbstractFacade<Partners, Integer> implements Serializable{


	private static final long serialVersionUID = 1L;
	public PartnersFacade(){
		super(Partners.class,Integer.class);
	}
	
	/***
	 * Busca socio por codigo
	 */
	public Partners findByCode(Integer codigoSocio){
		try{
			Query query=getEntityManager().createNamedQuery(Partners.CARGAR_SOCIOS_POR_CODIGO);
			query.setParameter("codigoSocio", codigoSocio);
			return (Partners) query.getSingleResult();
		}catch(NoResultException e){
			return null;
		}
	}

}
