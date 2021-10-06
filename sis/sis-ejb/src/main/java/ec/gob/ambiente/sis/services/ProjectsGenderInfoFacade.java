/**
@autor proamazonia [Christian Báez]  4 ago. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.hibernate.Hibernate;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.ProjectGenderIndicator;
import ec.gob.ambiente.sis.model.ProjectsGenderInfo;
import lombok.Getter;

@Stateless
@LocalBean
public class ProjectsGenderInfoFacade extends AbstractFacade<ProjectsGenderInfo, Integer>{
//	
//	@EJB
//	private GenderAdvancesFacade genderAdvancesFacade;
	
	@EJB
	@Getter
	private ProjectGenderIndicatorFacade projectGenderIndicatorFacade;

	
	public ProjectsGenderInfoFacade(){
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
	/***
	 * Busca por otras lineas de genero.
	 * @param codigoProyecto
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsGenderInfo> listaPorProyectoOtrasLineaGenero(int codigoProyecto) throws Exception{
		String sql="SELECT PGI from ProjectsGenderInfo PGI WHERE PGI.pginStatus=true AND PGI.projects.projId=:codigoProyecto AND PGI.cataId.cataId IS NULL";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);		
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Lista las lineas de genero por proyecto o por partner
	 * @param codigoProyecto
	 * @param codigoPartner
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsGenderInfo> listaLineasGeneroProyectoPartner(Integer codigoProyecto,Integer codigoPartner) throws Exception{
		String sql="";
		List<ProjectsGenderInfo> listaTemp=new ArrayList<ProjectsGenderInfo>();
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		if(codigoProyecto>0 && codigoPartner>0){
			sql="SELECT PGI from ProjectsGenderInfo PGI WHERE PGI.pginStatus=TRUE AND PGI.projects.projId=:codigoProyecto AND PGI.pspaId=:codigoPartner ORDER BY PGI.cataId.cataNumber";
			camposCondicion.put("codigoProyecto", codigoProyecto);		
			camposCondicion.put("codigoPartner", codigoPartner);
		}else{
			sql="SELECT PGI from ProjectsGenderInfo PGI WHERE PGI.pginStatus=TRUE AND PGI.projects.projId=:codigoProyecto AND PGI.pspaId IS NULL ORDER BY PGI.cataId.cataNumber";
			camposCondicion.put("codigoProyecto", codigoProyecto);		
		}		
		listaTemp = findByCreateQuery(sql, camposCondicion);
		for (ProjectsGenderInfo pgi : listaTemp) {
			Hibernate.initialize(pgi.getProjectGenderIndicatorList());
		}
		return listaTemp;
//		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<ProjectsGenderInfo> listaOtrasLineasGeneroProyectoPartner(Integer codigoProyecto,Integer codigoPartner) throws Exception{
		String sql="";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		if(codigoProyecto>0 && codigoPartner>0){
			sql="SELECT PGI from ProjectsGenderInfo PGI WHERE PGI.pginStatus=TRUE AND PGI.projects.projId=:codigoProyecto AND PGI.pspaId=:codigoPartner AND PGI.cataId IS NULL ORDER BY PGI.pginId";
			camposCondicion.put("codigoProyecto", codigoProyecto);		
			camposCondicion.put("codigoPartner", codigoPartner);
		}else{
			sql="SELECT PGI from ProjectsGenderInfo PGI WHERE PGI.pginStatus=TRUE AND PGI.projects.projId=:codigoProyecto AND PGI.pspaId IS NULL AND PGI.cataId IS NULL ORDER BY PGI.pginId";
			camposCondicion.put("codigoProyecto", codigoProyecto);		
		}			
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Agrega o edita un ProjectGenderInfo
	 * @param projectGenderInfo
	 * @throws Exception
	 */
	public void agregarEditarProjectGenerInfo(ProjectsGenderInfo projectGenderInfo,List<ProjectGenderIndicator> indicadores)throws Exception{
		if(projectGenderInfo.getPginId() == null)
			create(projectGenderInfo);
		else
			edit(projectGenderInfo);
		for(ProjectGenderIndicator pgi:indicadores){
			getProjectGenderIndicatorFacade().agregarEditar(pgi);
		}
	}
	
//	public void eliminaLineaDeGenero(List<GenderAdvances> listaAvances,ProjectsGenderInfo projectGender,String usuario)throws Exception{
//		for (GenderAdvances ga : listaAvances) {
//			ga.setGeadStatus(false);
//			ga.setGeadUpdateUser(usuario);
//			ga.setGeadUpdateDate(new Date());
//			genderAdvancesFacade.edit(ga);
//		}
//		projectGender.setPginAssociatedResults(null);
//		projectGender.setPginBudget(0);
//		projectGender.setPginResultsType(null);
//
//		projectGender.setPginOtherLine(null);
//		projectGender.setPginUpdateDate(new Date());
//		projectGender.setPginUpdateUser(usuario);		
//		edit(projectGender);
//	}
	/**
	 * Consulta en base al codigo del projectGenderInfo
	 * @param codigo
	 * @return
	 * @throws DaoException
	 */
	public ProjectsGenderInfo buscaProjectGenderInfoPorCodigo(int codigo)throws DaoException{
		try{
		String sql="SELECT PGI from ProjectsGenderInfo PGI WHERE PGI.pginId=:codigo";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigo", codigo);		
		return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	
	public List<Object[]> listadoNuevosProyectosAgregados(int codigoProyecto,int codigoPartner,int codigoAvance) throws Exception{
		String sql="";		
		if(codigoProyecto>0 && codigoPartner == 0){
//			sql="SELECT ga.pgin_id as pgi1,pgi.pgin_id as pgi2,pgi.cata_id,pgi.indi_id,pgi.pgin_other_line,pgi.pgin_another_indicator "
//				+",pgi.pgin_results_type,pgi.pgin_associated_results, pgi.pgin_budget,pgi.pspa_id " 
//				+" FROM sis.gender_advances ga RIGHT JOIN sis.projects_gender_info pgi "
//				+" ON ga.pgin_id = pgi.pgin_id  WHERE pgi.pgin_status=TRUE AND pgi.pgin_associated_results is not null AND pgi.proj_id=" +codigoProyecto
//				+ " AND pgi.pspa_id IS NULL";
			sql = "SELECT ga.pgin_id as pgi1,pgi.pgin_id as pgi2,pgi.cata_id,pgi.indi_id,pgi.pgin_other_line,pgi.pgin_another_indicator " 
					+",pgi.pgin_results_type,pgi.pgin_associated_results, pgi.pgin_budget,pgi.pspa_id FROM sis.gender_advances ga INNER JOIN sis.advance_execution_safeguards aes "
					+" ON aes.adex_id= ga.adex_id AND ga.adex_id="+codigoAvance
					+" RIGHT JOIN sis.projects_gender_info pgi " 
					+" ON pgi.pgin_id=ga.pgin_id WHERE pgi.proj_id=" + codigoProyecto
					+ " AND pgi.pgin_status=TRUE";
		}else if(codigoProyecto>0 && codigoPartner > 0){
//			sql="SELECT ga.pgin_id as pgi1,pgi.pgin_id as pgi2,pgi.cata_id,pgi.indi_id,pgi.pgin_other_line,pgi.pgin_another_indicator "
//					+",pgi.pgin_results_type,pgi.pgin_associated_results, pgi.pgin_budget,pgi.pspa_id " 
//					+" FROM sis.gender_advances ga RIGHT JOIN sis.projects_gender_info pgi "
//					+" ON ga.pgin_id = pgi.pgin_id  WHERE pgi.pgin_status=TRUE AND pgi.pgin_associated_results is not null AND pgi.proj_id="+codigoProyecto
//					+ " AND pgi.pspa_id="+codigoPartner;
			sql = "SELECT ga.pgin_id as pgi1,pgi.pgin_id as pgi2,pgi.cata_id,pgi.indi_id,pgi.pgin_other_line,pgi.pgin_another_indicator " 
					+",pgi.pgin_results_type,pgi.pgin_associated_results, pgi.pgin_budget,pgi.pspa_id FROM sis.gender_advances ga INNER JOIN sis.advance_execution_safeguards aes "
					+" ON aes.adex_id= ga.adex_id AND ga.adex_id="+codigoAvance
					+" RIGHT JOIN sis.projects_gender_info pgi " 
					+" ON pgi.pgin_id=ga.pgin_id WHERE pgi.pspa_id=" + codigoPartner
					+ " AND pgi.proj_id=" + codigoProyecto
					+ " AND pgi.pgin_status=TRUE";
		}		
		return consultaNativa(sql);
	}
}

