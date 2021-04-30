package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sis.dao.AbstractFacade;
@Stateless
@LocalBean
public class PartnersFacade extends AbstractFacade<Partners, Integer>{


	
	public PartnersFacade(){
		super(Partners.class,Integer.class);
	}
	
	/***
	 * Busca socio por codigo
	 */
	public Partners buscarPartnerPorCodigo(Integer codigoSocio) throws Exception{
		String sql="SELECT PA FROM Partners PA WHERE PA.partId=:codigoSocio";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoSocio", codigoSocio);
		return findByCreateQuerySingleResult(sql, camposCondicion);
	}
	/**
	 * Carga los socios implementadores
	 * @return
	 * @throws Exception
	 */
	public List<Partners> listarSociosImplementadores() throws Exception {
		String sql="SELECT P FROM Partners P WHERE P.partStatus=true AND P.partType='I' ORDER BY P.partName ";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}

}
