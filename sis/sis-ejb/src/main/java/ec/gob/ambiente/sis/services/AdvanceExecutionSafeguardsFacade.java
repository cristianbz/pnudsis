package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

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
	 * Devuelve el avance de ejecucion por proyecto
	 * @param codigoProyecto
	 * @return
	 */	
	public AdvanceExecutionSafeguards buscarPorProyecto(int codigoProyecto) throws DaoException{
		try{
			String sql="SELECT AP FROM AdvanceExecutionSafeguards AP WHERE AP.projects.projId=:codigoProyecto AND AP.adexIsGender = FALSE";
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
	 * Devuelve el anvance de ejecucion para genero por proyecto 
	 * @param codigoProyecto
	 * @return
	 * @throws DaoException
	 */
	public AdvanceExecutionSafeguards buscarAvanceGeneroPorProyecto(int codigoProyecto) throws DaoException{
		try{
//			String sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projects.projId=:codigoProyecto AND AE.adexIsReported=false AND AE.adexIsGender=true AND AE.adexStatus=true";
			String sql="SELECT AE FROM AdvanceExecutionSafeguards AE WHERE AE.projects.projId=:codigoProyecto AND AE.adexIsGender=true AND AE.adexStatus=true";
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
			resumenEjecutivo.setAdvanceExecutionSafeguards(avanceEjecucion);
			resumenEjecutivo.setExsuSummaryContent("");			
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
}
