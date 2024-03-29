package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
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
		String sql="SELECT A FROM AdvanceSectors A WHERE A.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion AND A.adseSelectedSector = True";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public void agregaEditaAvanceSector(AdvanceSectors avanceSector)throws Exception{
		if (avanceSector.getAdseId()==null)
			create(avanceSector);
		else
			edit(avanceSector);
	}
	
	public AdvanceSectors buscaAvanceSector(int codigoAvanceEjecucion, int codigoSector)throws DaoException{
		try{
			String sql="SELECT A FROM AdvanceSectors A WHERE A.advanceExecutionSafeguards.adexId = :codigoAvanceEjecucion AND A.sectors.sectId = :codigoSector AND A.adseSelectedSector = True";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
			camposCondicion.put("codigoSector", codigoSector);
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
}
