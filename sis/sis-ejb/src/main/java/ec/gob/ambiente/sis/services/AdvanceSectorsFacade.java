package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.AdvanceSectors;

@Stateless
@LocalBean
public class AdvanceSectorsFacade extends AbstractFacade<AdvanceSectors,Integer> {

	
	

	public AdvanceSectorsFacade() {
		super(AdvanceSectors.class,Integer.class);
	}
	/**
	 * Elimina un AvanceSector
	 * @param advanceSectors
	 * @throws Exception
	 */
	public void eliminarAvanceSectores(AdvanceSectors advanceSectors)throws Exception{
		AdvanceSectors avanceSectors =getEntityManager().find(AdvanceSectors.class, advanceSectors.getAdseId());
		remove(avanceSectors);
	}
	public List<AdvanceSectors> listaAvanceSectoresPorAvanceEjecucion(int codigoAvanceEjecucion) throws Exception{
		String sql="SELECT A FROM AdvanceSectors A WHERE A.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		return findByCreateQuery(sql, camposCondicion);
	}
}
