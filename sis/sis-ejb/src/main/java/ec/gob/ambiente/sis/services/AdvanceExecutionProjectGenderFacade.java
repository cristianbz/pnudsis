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

	/**
	 * Carga los indicadores reportados por codigo de AvanceEjecucion
	 * @param codigoAvance  Codigo del avance ejecucion
	 * @return
	 * @throws Exception
	 */
	public List<AdvanceExecutionProjectGender> listaIndicadoresReportados(int codigoAvance)throws Exception{
		List<AdvanceExecutionProjectGender> listaTemp=null;
		String sql="SELECT AEPG FROM AdvanceExecutionProjectGender AEPG, ProjectGenderIndicator PGIN, ProjectsGenderInfo PGINF WHERE AEPG.projectGenderIndicator.pgigId = PGIN.pgigId AND PGINF.pginId = PGIN.projectsGenderInfo.pginId AND AEPG.aepgStatus= TRUE AND AEPG.advanceExecutionSafeguards.adexId=:codigoAvance";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvance", codigoAvance);		
		listaTemp=findByCreateQuery(sql, camposCondicion);
		for (AdvanceExecutionProjectGender aepg : listaTemp) {
			Hibernate.initialize(aepg.getProjectGenderIndicator().getIndicators());
		}
		return listaTemp;
	}
	
	public List<AdvanceExecutionProjectGender> ubicaIndicadoresActivosNoFinalizados(int codigoIndicador)throws Exception{
		String sql="SELECT AEPG FROM AdvanceExecutionProjectGender AEPG WHERE AEPG.aepgStatus =true AND AEPG.projectGenderIndicator.pgigId=:codigoIndicador AND AEPG.advanceExecutionSafeguards.adexIsReported= FALSE AND AEPG.aepgStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoIndicador", codigoIndicador);		
		List<AdvanceExecutionProjectGender> listaTemp=findByCreateQuery(sql, camposCondicion);
//		for (AdvanceExecutionProjectGender aepg : listaTemp) {
//			Hibernate.initialize(aepg.getAdvanceExecutionSafeguards());
//		}
		return listaTemp;
	}
}
