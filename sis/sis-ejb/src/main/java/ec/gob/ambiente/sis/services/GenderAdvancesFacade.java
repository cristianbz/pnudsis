/**
@autor proamazonia [Christian Báez]  1 jun. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.GenderAdvances;

@Stateless
@LocalBean
public class GenderAdvancesFacade extends AbstractFacade<GenderAdvances,Integer>{
	public GenderAdvancesFacade() {
		super(GenderAdvances.class, Integer.class);
	}
	/**
	 * Busca avance de genero por codigo de proyectoGeneroInfo
	 * @param codigoProjectGenderInfo
	 * @return
	 * @throws DaoException
	 */
	public GenderAdvances buscaPorProjectGenderInfoAvanceExecution(int codigoProjectGenderInfo, int codigoAvanceEjecucion) throws DaoException{
		try{
			String sql="SELECT GA FROM GenderAdvances GA WHERE GA.projectsGenderInfo.pginId=:codigoProjectGenderInfo AND GA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoProjectGenderInfo", codigoProjectGenderInfo);
			camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);			
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	/**
	 * Busca los avances de genero en estado activo y que pertencen a un reporte de seguimiento activo
	 * @param codigoProjectGenderInfo
	 * @return
	 * @throws Exception
	 */
	public List<GenderAdvances> listaAvancesGeneroActivosPorProjectGender(int codigoProjectGenderInfo)throws Exception{
		String sql="SELECT GA FROM GenderAdvances GA WHERE GA.projectsGenderInfo.pginId=:codigoProjectGenderInfo AND GA.geadStatus = TRUE AND GA.advanceExecutionSafeguards.adexReportedStatus='I'";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProjectGenderInfo", codigoProjectGenderInfo);			
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Busca los avances de genero por avance de ejecucion
	 * @param codigoAvanceEjecucion
	 * @return
	 * @throws Exception
	 */
	public List<GenderAdvances> listadoAvancesGeneroPorAvanceEjecucion(int codigoAvanceEjecucion)throws Exception{
		List<GenderAdvances> listaTemp=new ArrayList<GenderAdvances>();
		String sql="SELECT GA FROM GenderAdvances GA WHERE GA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion AND GA.projectsGenderInfo.cataId IS NOT NULL AND GA.geadStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);					
		listaTemp = findByCreateQuery(sql, camposCondicion);

		return listaTemp;
	}
	/**
	 * Busca los avances de genero por avance de ejecucion de otros temas
	 * @param codigoAvanceEjecucion
	 * @return
	 * @throws Exception
	 */
	public List<GenderAdvances> listadoAvancesGeneroOtrosTemasPorAvanceEjecucion(int codigoAvanceEjecucion)throws Exception{
		List<GenderAdvances> listaTemp=new ArrayList<GenderAdvances>();
		String sql="SELECT GA FROM GenderAdvances GA WHERE GA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion AND GA.projectsGenderInfo.cataId IS NULL AND GA.geadStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);					
		listaTemp = findByCreateQuery(sql, camposCondicion);
		return listaTemp;
	}
	/**
	 * 
	 * @param avanceGenero
	 * @throws Exception
	 */
	public void actualizarCrearAvanceGenero(GenderAdvances avanceGenero)throws Exception{
		if(avanceGenero.getGeadId()==null)
			create(avanceGenero);
		else
			edit(avanceGenero);
	}

}

