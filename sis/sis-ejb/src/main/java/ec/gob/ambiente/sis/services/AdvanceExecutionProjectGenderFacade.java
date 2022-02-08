/**
@autor proamazonia [Christian Báez]  7 oct. 2021

**/
package ec.gob.ambiente.sis.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.dto.DtoGenero;
import ec.gob.ambiente.sis.dto.DtoTableResponses;
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
		return listaTemp;
	}
	/**
	 * Temas tratados para genero 
	 * @return
	 * @throws Exception
	 */
	public List<DtoGenero> listaTemasGenero() throws Exception{
		List<Object[]> resultado= null;
		List<DtoGenero> listaResultado = new ArrayList<DtoGenero>();
		String sql ="SELECT COUNT(pgi.cata_id), c.cata_text2 FROM sis.advance_execution_safeguards aes, sis.advance_execution_project_gender aepg, " +
					"sis.project_gender_indicator pgig, sis.projects_gender_info pgi, sis.catalogs c " +
					" WHERE c.cata_id = pgi.cata_id AND pgig.pgin_id = pgi.pgin_id AND aepg.adex_id = aes.adex_id AND " + 
					" pgig.pgig_id = aepg.pgig_id AND aepg.aepg_status = TRUE " +
					" GROUP BY c.cata_text2";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				DtoGenero genero = new DtoGenero();
				genero.setNumero(Integer.valueOf(dataObj[0].toString()));
				genero.setTema(dataObj[1].toString());
				listaResultado.add(genero);
			}
		}
		return listaResultado;
	}
	/**
	 * Lista de acciones de genero
	 * @return
	 * @throws Exception
	 */
	public List<String> listadoAccionesGenero() throws Exception{
		List<Object[]> resultado= null;
		List<String> listaResultado = new ArrayList<String>();
		String sql ="SELECT aepg_id,aepg_actions_done FROM sis.advance_execution_project_gender WHERE LENGTH(aepg_actions_done)>0 AND aepg_status = TRUE";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				listaResultado.add(dataObj[1].toString());
			}
		}
		return listaResultado;
	}
	
	public BigDecimal presupuestoGenero() throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);		
		String sql ="SELECT SUM(pgi.pgin_budget) FROM sis.projects_gender_info pgi, sis.project_gender_indicator pgig, sis.advance_execution_safeguards aes, "
				+ " sis.advance_execution_project_gender aepg "
				+ " WHERE pgig.pgin_id = pgi.pgin_id AND aepg.adex_id = aes.adex_id AND " 
				+ "	pgig.pgig_id = aepg.pgig_id AND aepg.aepg_status = TRUE AND pgi.pgin_status = TRUE";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	public List<DtoTableResponses> listaResumenIndicadoresGenero() throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, " +
				" ct.caty_description,ca.cata_text2 ,indi.indi_description , aepg.aepg_value_reached_one, aepg.aepg_value_reached_two, aepg.aepg_value_reached_another_indicator,aepg_actions_done,indi.indi_type, " +
				" pgi.pgig_goals, pgi.pgig_goal_value_one, pgi.pgig_goal_value_two" +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" ,sis.catalogs ca, sigma.partners par,sis.advance_execution_project_gender aepg,sis.project_gender_indicator pgi,sis.projects_gender_info pginfo,sis.catalogs_types ct, " +
				" sis.indicators indi " +
				" WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE " + 
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND aes.adex_is_gender = TRUE " +
				" AND aes.adex_id = aepg.adex_id AND    aepg.pgig_id = pgi.pgig_id AND pgi.pgin_id = pginfo.pgin_id AND pginfo.cata_id =ca.cata_id AND p.proj_status = TRUE" +
				" AND ct.caty_id = ca.caty_id AND pgi.indi_id = indi.indi_id ";
 
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				DtoTableResponses dto = new DtoTableResponses();				
				if(dataObj[0]!=null)
					dto.setProyecto(dataObj[0].toString());					
				if(dataObj[1]!=null)
					dto.setPeriodo(dataObj[1].toString());
				if(dataObj[2]!=null)
					dto.setSocioImplementador(dataObj[2].toString());
				if(dataObj[3]!=null)
					dto.setSocioEstrategico(dataObj[3].toString());
				else
					dto.setSocioEstrategico("");
				if(dataObj[4]!=null)
					dto.setTextoUno(dataObj[4].toString());
				if(dataObj[5]!=null)
					dto.setTextoDos(dataObj[5].toString());	
				if(dataObj[6]!=null)
					dto.setTextoTres(dataObj[6].toString());
				if(dataObj[7]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setTextoCuatro(dataObj[9].toString());
				if(dataObj[10]!=null)
					dto.setTextoCinco(dataObj[10].toString());
				if(dataObj[11]!=null)
					dto.setTextoSeis(dataObj[11].toString());
				if(dataObj[12]!=null)
					dto.setTextoSiete(dataObj[12].toString());
				if(dataObj[13]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[13].toString()));
				if(dataObj[14]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[14].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

}

