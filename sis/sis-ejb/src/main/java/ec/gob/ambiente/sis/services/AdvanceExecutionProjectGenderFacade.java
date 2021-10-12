/**
@autor proamazonia [Christian Báez]  7 oct. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.AdvanceExecutionProjectGender;

@Stateless
@LocalBean
public class AdvanceExecutionProjectGenderFacade extends AbstractFacade<AdvanceExecutionProjectGender, Integer>{
	
	public AdvanceExecutionProjectGenderFacade(){
		super(AdvanceExecutionProjectGender.class,Integer.class);
	}
	
	/**
	 * Consulta los reportes de genero del proyecto
	 * @param codigoAvance Codigo del avance de ejecucion a reportar
	 * @param codigoProyecto Codigo del proyecto a reportar
	 * @return
	 * @throws Exception
	 */
	public List<AdvanceExecutionProjectGender> listaReportados(int codigoAvance, int codigoProyecto)throws Exception{
		String sql="SELECT AEPG FROM AdvanceExecutionProjectGender AEPG WHERE AEPG.aepgStatus =true AND AEPG.advanceExecutionSafeguards.adexId=:codigoAvance AND AEPG.projectGenderIndicator.projectsGenderInfo.projects.projId=:codigoProyecto";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		camposCondicion.put("codigoAvance", codigoAvance);	
		List<AdvanceExecutionProjectGender> listaTemp=findByCreateQuery(sql, camposCondicion);
		for (AdvanceExecutionProjectGender aepg : listaTemp) {
			Hibernate.initialize(aepg.getProjectGenderIndicator());
		}
		return listaTemp;
	}
	/**
	 * Agrega o edita un avance de genero
	 * @param aepg
	 * @throws Exception
	 */
	public void agregarEditar(AdvanceExecutionProjectGender aepg)throws Exception{
		if(aepg.getAepgId()==null)
			create(aepg);
		else
			edit(aepg);
	}
	
	public List<AdvanceExecutionProjectGender> listaReportadosActivosPorProyectoGeneroInfo(int codigoProyectoGenderinfo)throws Exception{
		String sql="SELECT AEPG FROM AdvanceExecutionProjectGender AEPG WHERE AEPG.aepgStatus =true AND AEPG.projectsGenderInfo.pginId=:codigoProyectoGenderinfo AND AEPG.advanceExecutionSafeguards.adexIsReported= FALSE AND AEPG.aepgStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyectoGenderinfo", codigoProyectoGenderinfo);		
		List<AdvanceExecutionProjectGender> listaTemp=findByCreateQuery(sql, camposCondicion);
		for (AdvanceExecutionProjectGender aepg : listaTemp) {
			Hibernate.initialize(aepg.getAdvanceExecutionSafeguards());
		}
		return listaTemp;
	}

}

