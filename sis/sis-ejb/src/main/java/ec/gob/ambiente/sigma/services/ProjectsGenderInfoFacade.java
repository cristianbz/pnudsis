/**
@autor proamazonia [Christian Báez]  31 may. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.ProjectsGenderInfo;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class ProjectsGenderInfoFacade extends AbstractFacade<ProjectsGenderInfo,Integer >{
	
	public ProjectsGenderInfoFacade() {
		super(ProjectsGenderInfo.class,Integer.class);
	}
	/**
	 * Obtiene las lineas estrategica del proyecto
	 * @param codigoProyecto
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsGenderInfo> listaProjectsGenderInfo(int codigoProyecto) throws Exception{
		String sql="SELECT PGI from ProjectsGenderInfo PGI WHERE PGI.pginStatus=true AND PGI.projects.projId=:codigoProyecto";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		
		return findByCreateQuery(sql, camposCondicion);

	}
	/**
	 * Busca por linea de genero y codigo de proyecto
	 * @param codigoProyecto
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsGenderInfo> listaPorProyectoLineaGenero(int codigoProyecto,int codigoLineaGenero) throws Exception{
		String sql="SELECT PGI from ProjectsGenderInfo PGI WHERE PGI.pginStatus=true AND PGI.projects.projId=:codigoProyecto AND PGI.cataId.catyId.catyId=:codigoLineaGenero";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		camposCondicion.put("codigoLineaGenero", codigoLineaGenero);
		return findByCreateQuery(sql, camposCondicion);

	}
}

