/**
@autor proamazonia [Christian Báez]  13 jul. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class ProjectsStrategicPartnersFacade extends AbstractFacade<ProjectsStrategicPartners, Integer> {
	public ProjectsStrategicPartnersFacade(){
		super(ProjectsStrategicPartners.class,Integer.class);
	}
	/**
	 * Consulta los partners del proyecto
	 * @param codigoProyecto
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsStrategicPartners> listaPartnersActivos(Integer codigoProyecto)throws Exception{
		String sql="SELECT PSP FROM ProjectsStrategicPartners PSP WHERE  PSP.projects.projId=:codigoProyecto AND PSP.pspaStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuery(sql, camposCondicion);
	}
}

