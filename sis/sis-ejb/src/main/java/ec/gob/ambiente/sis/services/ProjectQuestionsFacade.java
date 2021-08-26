/**
@autor proamazonia [Christian Báez]  13 jul. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.ProjectQuestions;
@Stateless
@LocalBean
public class ProjectQuestionsFacade extends AbstractFacade<ProjectQuestions, Integer>{
	
	
	
	public ProjectQuestionsFacade(){
		super(ProjectQuestions.class,Integer.class);
	}
	/**
	 * Lista de preguntas seleccionadas por proyecto
	 * @param codigoProyecto
	 * @return
	 * @throws Exception
	 */
	public List<ProjectQuestions> listaPreguntasProyectoSeleccionadas(Integer codigoProyecto)throws Exception{
		String sql="SELECT PQ FROM ProjectQuestions PQ WHERE PQ.projects.projId=:codigoProyecto AND PQ.prquStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Lista de preguntas seleccionadas por strategic_partner
	 * @param codigoPartner
	 * @return
	 * @throws Exception
	 */
	public List<ProjectQuestions> listaPreguntasPartnerSeleccionadas(Integer codigoPartner)throws Exception{
		String sql="SELECT PQ FROM ProjectQuestions PQ WHERE PQ.projectsStrategicPartners.pspaId=:codigoPartner AND PQ.prquStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoPartner", codigoPartner);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Agrega las preguntas y las salvaguardas asignadas al Partner.
	 * @param listaProjectQuestions
	 * @param listaSafeguarsAssigned
	 * @throws Exception
	 */
	public void agregaPreguntasProyecto(List<ProjectQuestions> listaProjectQuestions,boolean esProyecto,int tipo,boolean nuevoIngreso)throws Exception{
		if(!nuevoIngreso)
			controlPreguntasProyecto(listaProjectQuestions, esProyecto, tipo);
		for (ProjectQuestions preguntas : listaProjectQuestions) {
			if(preguntas.getPrquId() == null)
				create(preguntas);	
			else
				edit(preguntas);
		}

	}
	/**
	 * Controla el movimiento de las preguntas asignadas o removidas
	 * @param listaProjectQuestions
	 * @param esProyecto
	 * @param tipo
	 * @throws Exception
	 */
	public void controlPreguntasProyecto(List<ProjectQuestions> listaProjectQuestions,boolean esProyecto,int tipo)throws Exception{
		List<ProjectQuestions> listaTemp;
		if(esProyecto)
			listaTemp=listaPreguntasProyectoSeleccionadas(tipo);
		else
			listaTemp=listaPreguntasPartnerSeleccionadas(tipo);
		if(listaTemp.size()== listaProjectQuestions.size()){
			boolean encontrado=false;
			for (ProjectQuestions pqtemp : listaTemp) {
				encontrado = false;
				for (ProjectQuestions pq : listaProjectQuestions) {
					if(pqtemp.getCatalogs().getCataId().equals(pq.getCatalogs().getCataId())){
						pq.setPrquId(pqtemp.getPrquId());
						encontrado=true;
						break;
					}
				}
				if(encontrado==false){
					pqtemp.setPrquStatus(false);
					edit(pqtemp);
				}
			}
		}else if(listaTemp.size()>listaProjectQuestions.size() || listaTemp.size()<listaProjectQuestions.size()){
			for (ProjectQuestions pqtemp : listaTemp) {
				pqtemp.setPrquStatus(false);
				edit(pqtemp);
			}
		}
	}
	/**
	 * Busca las salvaguardas asignadas por proyecto o por socio estrategico
	 * @param codigoProyecto  Es el codigo del proyecto seleccionado
	 * @param codigoSocioEstrategico Es el codigo del socio estratégico seleccionado
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> listaSalvaguardasComponentes(Integer codigoProyecto,Integer codigoSocioEstrategico)throws Exception{
		String sql="";		
		if(codigoProyecto!=null && codigoSocioEstrategico == null)
			sql="SELECT DISTINCT safe_id, prqu_components FROM sis.project_questions WHERE prqu_status=true AND proj_id="+codigoProyecto +" ORDER BY safe_id";			
		else if(codigoSocioEstrategico != null)
			sql="SELECT DISTINCT safe_id, prqu_components FROM sis.project_questions WHERE prqu_status=true AND pspa_id="+codigoSocioEstrategico +" ORDER BY safe_id";							
		return consultaNativa(sql);
	}
}

