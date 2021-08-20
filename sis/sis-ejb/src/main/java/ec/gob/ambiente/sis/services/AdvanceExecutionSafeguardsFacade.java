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

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSectors;
import ec.gob.ambiente.sis.model.ExecutiveSummaries;
import ec.gob.ambiente.sis.model.GenderAdvances;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;

@Stateless
@LocalBean
public class AdvanceExecutionSafeguardsFacade extends AbstractFacade<AdvanceExecutionSafeguards, Integer>{

	@EJB
	private TableResponsesFacade tableResponsesFacade;
	
	@EJB
	private ValueAnswersFacade valueAnswersFacade;
	
	@EJB
	private AdvanceSectorsFacade advanceSectorsFacade;
	
	@EJB
	private GenderAdvancesFacade genderAdvancesFacade;
	
	@EJB
	private ExecutiveSummariesFacade executiveSummariesFacade; 

	public AdvanceExecutionSafeguardsFacade() {
		super(AdvanceExecutionSafeguards.class,Integer.class);
	}
	/**
	 * Busca si existe un avance de ejecucion para genero o proyecto en base a un periodo de reporte
	 * @param codigoProyecto
	 * @param codigoPartner
	 * @param generoSalvaguarda  1 salvaguarda  2 genero
	 * @param desde
	 * @param hasta
	 * @return
	 * @throws DaoException
	 */
	public AdvanceExecutionSafeguards buscarAvanceSalvaguardaGeneroReportado(int codigoProyecto, int codigoPartner,int generoSalvaguarda,String desde,String hasta) throws DaoException{
		try{
			String sql="";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			if (codigoProyecto>0 && codigoPartner == 0 && generoSalvaguarda == 1){
				sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projects.projId=:codigoProyecto AND AE.projectsStrategicPartners.pspaId=NULL AND AE.adexIsGender = FALSE AND AE.adexTermFrom =:desde AND AE.adexTermTo =:hasta ";
				camposCondicion.put("codigoProyecto", codigoProyecto);
				camposCondicion.put("desde", desde);
				camposCondicion.put("hasta", hasta);
			}else if (codigoProyecto>0 && codigoPartner > 0 && generoSalvaguarda == 1){
				sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projects.projId=:codigoProyecto AND AE.projectsStrategicPartners.pspaId=:codigoPartner AND AE.adexIsGender = FALSE AND AE.adexTermFrom =:desde AND AE.adexTermTo =:hasta";
				camposCondicion.put("codigoProyecto", codigoProyecto);
				camposCondicion.put("codigoPartner", codigoPartner);
				camposCondicion.put("desde", desde);
				camposCondicion.put("hasta", hasta);
			}else if (codigoProyecto>0 && codigoPartner == 0 && generoSalvaguarda == 2){
				sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projects.projId=:codigoProyecto AND AE.projectsStrategicPartners.pspaId=NULL AND AE.adexIsGender = TRUE AND AE.adexTermFrom =:desde AND AE.adexTermTo =:hasta ";
				camposCondicion.put("codigoProyecto", codigoProyecto);
				camposCondicion.put("desde", desde);
				camposCondicion.put("hasta", hasta);
			}else if(codigoProyecto>0 && codigoPartner > 0 && generoSalvaguarda == 2){
				sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projects.projId=:codigoProyecto AND AE.projectsStrategicPartners.pspaId=:codigoPartner AND AE.adexIsGender = TRUE AND AE.adexTermFrom =:desde AND AE.adexTermTo =:hasta ";
				camposCondicion.put("codigoProyecto", codigoProyecto);
				camposCondicion.put("codigoPartner", codigoPartner);
				camposCondicion.put("desde", desde);
				camposCondicion.put("hasta", hasta);
			}
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	
	/**
	 * Devuelve el avance de ejecucion por proyecto
	 * @param codigoProyecto
	 * @return
	 */	
	public AdvanceExecutionSafeguards buscarPorProyecto(int codigoProyecto) throws DaoException{
		try{
			String sql="SELECT AP FROM AdvanceExecutionSafeguards AP WHERE AP.projects.projId=:codigoProyecto AND AP.projectsStrategicPartners.pspaId=NULL AND AP.adexIsGender = FALSE AND AP.adexIsReported=FALSE";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoProyecto", codigoProyecto);
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	/**
	 * Devuelve el avance de ejecucion por strategicPartner
	 * @param codigoProyecto
	 * @return
	 */	
	public AdvanceExecutionSafeguards buscarPorStrategicPartner(int codigoPartner,int codigoProyecto) throws DaoException{
		try{
			String sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projectsStrategicPartners.pspaId=:codigoPartner AND AE.projects.projId=:codigoProyecto AND AE.adexIsGender = FALSE AND AE.projectsStrategicPartners.pspaStatus= TRUE AND AE.adexIsReported=FALSE";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoPartner", codigoPartner);
			camposCondicion.put("codigoProyecto", codigoProyecto);
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	/**
	 * Devuelve el anvance de ejecucion para genero por proyecto 
	 * @param codigoProyecto
	 * @return
	 * @throws DaoException
	 */
	public AdvanceExecutionSafeguards buscarAvanceGeneroPorProyecto(int codigoProyecto) throws DaoException{
		try{
//			String sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projects.projId=:codigoProyecto AND AE.adexIsReported=false AND AE.adexIsGender=true AND AE.adexStatus=true";
			String sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projects.projId=:codigoProyecto AND AE.adexIsGender=true AND AE.adexIsReported=false AND AE.adexStatus=true";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoProyecto", codigoProyecto);
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}

	/**
	 * Graba el AvanceEjecucionSalvaguarda
	 * @param avanceEjecucion
	 * @throws Exception
	 */
	public AdvanceExecutionSafeguards grabarAvanceEjecucionSalvaguarda(AdvanceExecutionSafeguards avanceEjecucion,int salvaguarda) throws Exception{
		if(avanceEjecucion.getAdexId()==null){
			avanceEjecucion = create(avanceEjecucion);
			for (TableResponses respuestaTabla : avanceEjecucion.getTableResponsesList()) {
				if(respuestaTabla.getTareId()==null)
					tableResponsesFacade.create(respuestaTabla);
				else
					tableResponsesFacade.edit(respuestaTabla);
			}
			for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) { 
				respuestas.setAdvanceExecutionSaveguards(avanceEjecucion);
				if(respuestas.getVaanId()==null)
					valueAnswersFacade.create(respuestas);
				else
					valueAnswersFacade.edit(respuestas);
			}
		}else{
			controlaAvencesSectores(avanceEjecucion.getAdvanceSectorsList(), avanceEjecucion.getAdexId());
			for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) {
					valueAnswersFacade.edit(respuestas);
			}
			edit(avanceEjecucion);			
		}
		return avanceEjecucion;

	}
	public void controlaAvencesSectores(List<AdvanceSectors> listaAvanceSectores,int codigoAvanceEjecucion) throws Exception{
		List<AdvanceSectors> listaTemp=advanceSectorsFacade.listaAvanceSectoresPorAvanceEjecucion(codigoAvanceEjecucion);
		if(listaTemp.size()==listaAvanceSectores.size()){
			boolean encontrado=false;
			for (AdvanceSectors asTemp : listaTemp) {
				encontrado=false;
				for (AdvanceSectors as : listaAvanceSectores) {
					if(asTemp.getSectors().getSectId().equals(as.getSectors().getSectId())){
						as.setAdseId(asTemp.getAdseId());
						encontrado=true;
						break;
					}
				}
				if(encontrado==false){
					break;
				}
			}
			if(encontrado==false){
				for (AdvanceSectors asTemp : listaTemp) {
					asTemp.setAdseSelectedSector(false);
					advanceSectorsFacade.eliminarAvanceSectores(asTemp);
				}
			}
			
		}else if(listaTemp.size()>=listaAvanceSectores.size() || listaTemp.size()<=listaAvanceSectores.size()){
			for (AdvanceSectors asTemp : listaTemp) {
				asTemp.setAdseSelectedSector(false);
				advanceSectorsFacade.edit(asTemp);
			}
		}
	}
	/**
	 * Registra el avance de ejecucion para genero
	 * @param avanceEjecucion
	 * @return
	 * @throws Exception
	 */
	public AdvanceExecutionSafeguards grabarEditarAvanceEjecucionGenero(AdvanceExecutionSafeguards avanceEjecucion, GenderAdvances avanceGenero,ExecutiveSummaries resumenEjecutivo)throws Exception{
		if (avanceEjecucion.getAdexId() == null){
			create(avanceEjecucion);
			for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) { 
				respuestas.setAdvanceExecutionSaveguards(avanceEjecucion);
				if(respuestas.getVaanId()==null)
					valueAnswersFacade.create(respuestas);
				else
					valueAnswersFacade.edit(respuestas);
			}
			avanceGenero.setAdvanceExecutionSafeguards(avanceEjecucion);
//			resumenEjecutivo.setAdvanceExecutionSafeguards(avanceEjecucion);
//			resumenEjecutivo.setExsuSummaryContent("");			
			genderAdvancesFacade.create(avanceGenero);
			executiveSummariesFacade.create(resumenEjecutivo);
		}else{
//			for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) {
//				valueAnswersFacade.edit(respuestas);
//			}
			edit(avanceEjecucion);
			avanceGenero.setAdvanceExecutionSafeguards(avanceEjecucion);
			genderAdvancesFacade.edit(avanceGenero);
			executiveSummariesFacade.edit(resumenEjecutivo);
		}
		return avanceEjecucion;
	}
	
	/**
	 * Registra el avance de ejecucion para genero
	 * @param avanceEjecucion
	 * @return
	 * @throws Exception
	 */
	public AdvanceExecutionSafeguards agregarEditarAvanceEjecucionGenero(AdvanceExecutionSafeguards avanceEjecucion, List<GenderAdvances> avancesGenero)throws Exception{
		if (avanceEjecucion.getAdexId() == null){
			create(avanceEjecucion);
			for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) { 
				respuestas.setAdvanceExecutionSaveguards(avanceEjecucion);
				if(respuestas.getVaanId()==null)
					valueAnswersFacade.create(respuestas);
				else
					valueAnswersFacade.edit(respuestas);
			}
			for(GenderAdvances ag:avancesGenero){
				ag.setAdvanceExecutionSafeguards(avanceEjecucion);
				genderAdvancesFacade.create(ag);
			}						
		}else{
			edit(avanceEjecucion);
			for(GenderAdvances ag:avancesGenero){
				ag.setAdvanceExecutionSafeguards(avanceEjecucion);
				genderAdvancesFacade.edit(ag);
			}
		}
		return avanceEjecucion;
	}
	/**
	 * Actualiza avance ejecucion genero
	 * @param avanceEjecucion
	 * @param avancesGenero
	 * @return
	 * @throws Exception
	 */
	public AdvanceExecutionSafeguards actualizaAvanceEjecucionGenero(AdvanceExecutionSafeguards avanceEjecucion)throws Exception{
		edit(avanceEjecucion);		
		return avanceEjecucion;
	} 

	
	/**
	 * Busca los avances de ejecucion reportados
	 * @param codigoProyecto
	 * @param tipo  1  salvaguardas  2 genero
	 * @return
	 * @throws Exception
	 */
	public List<AdvanceExecutionSafeguards> listarProyectosReportados(Integer codigoProyecto, Integer tipo) throws Exception{
		String sql="";
		List<AdvanceExecutionSafeguards> listaTemp=new ArrayList<AdvanceExecutionSafeguards>();
		if (tipo == 1)
			sql="SELECT AE FROM Projects P, AdvanceExecutionSafeguards AE WHERE AE.projects.projId = P.projId AND P.projStatus=TRUE AND P.projId= :codigoProyecto AND AE.adexIsGender = FALSE";
		else
			sql="SELECT AE FROM Projects P, AdvanceExecutionSafeguards AE WHERE AE.projects.projId = P.projId AND P.projStatus=TRUE AND P.projId= :codigoProyecto AND AE.adexIsGender = TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		listaTemp = findByCreateQuery(sql, camposCondicion);
		return listaTemp;
	}
	
	/**
	 * Busca los avances de ejecucion reportados del socio estrategico
	 * @param codigoProyecto
	 * @param tipo  1  salvaguardas  2 genero
	 * @return
	 * @throws Exception
	 */
	public List<AdvanceExecutionSafeguards> listarProyectosReportadosSocioEstrategico(Integer codigoProyecto, Integer tipo,Integer codigoSocio) throws Exception{
		String sql="";
		List<AdvanceExecutionSafeguards> listaTemp=new ArrayList<AdvanceExecutionSafeguards>();
		if (tipo == 1)
			sql="SELECT AE FROM Projects P, AdvanceExecutionSafeguards AE WHERE AE.projects.projId = P.projId AND P.projStatus=TRUE AND P.projId= :codigoProyecto AND AE.adexIsGender = FALSE AND AE.projectsStrategicPartners.pspaId=:codigoSocio";
		else
			sql="SELECT AE FROM Projects P, AdvanceExecutionSafeguards AE WHERE AE.projects.projId = P.projId AND P.projStatus=TRUE AND P.projId= :codigoProyecto AND AE.adexIsGender = TRUE AND AE.projectsStrategicPartners.pspaId=:codigoSocio";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		camposCondicion.put("codigoSocio", codigoSocio);
		listaTemp = findByCreateQuery(sql, camposCondicion);
		return listaTemp;
	}

}
