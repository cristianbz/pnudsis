package ec.gob.ambiente.sis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSectors;
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
			String sql="SELECT AP FROM AdvanceExecutionSafeguards AP WHERE AP.projects.projId=:codigoProyecto AND AP.adexIsReported=false";
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
			create(avanceEjecucion);
			for (TableResponses respuestaTabla : avanceEjecucion.getTableResponsesList()) {
				if(respuestaTabla.getTareId()==null)
					tableResponsesFacade.create(respuestaTabla);
				else
					tableResponsesFacade.edit(respuestaTabla);
			}
			for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) {
				if(respuestas.getVaanId()==null)
					valueAnswersFacade.create(respuestas);
				else
					valueAnswersFacade.edit(respuestas);
			}
		}else{
			List<AdvanceSectors> listaSectores=new ArrayList<>();
			List<TableResponses> listaAux=new ArrayList<>();			
			listaSectores = advanceSectorsFacade.listaAvanceSectoresPorAvanceEjecucion(avanceEjecucion.getAdexId());
			
			listaSectores.stream().forEach(s->{
				try {
					advanceSectorsFacade.eliminarAvanceSectores(s);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			if(salvaguarda==1){
				edit(avanceEjecucion);
				listaAux = tableResponsesFacade.findByAdvanceExecution(avanceEjecucion.getAdexId()).stream().filter(tr -> tr.getQuestions().getQuesQuestionOrder()==2 || tr.getQuestions().getQuesQuestionOrder()==4).collect(Collectors.toList());				

				System.out.println(listaAux.size() + " tammmm");
				listaAux.stream().forEach(tr->{
					try {
						tableResponsesFacade.eliminarRespuestasTabla(tr);						
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				for (TableResponses respuestaTabla : avanceEjecucion.getTableResponsesList()) {
					if(respuestaTabla.getTareId()==null)
						tableResponsesFacade.create(respuestaTabla);
					else
						tableResponsesFacade.edit(respuestaTabla);
				}
				for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) {
					if(respuestas.getVaanId()==null)
						valueAnswersFacade.create(respuestas);
					else
						valueAnswersFacade.edit(respuestas);
				}
			}else if(salvaguarda==2){
				edit(avanceEjecucion);
				listaAux = tableResponsesFacade.findByAdvanceExecution(avanceEjecucion.getAdexId()).stream().filter(tr -> tr.getQuestions().getQuesQuestionOrder() ==26 || tr.getQuestions().getQuesQuestionOrder() ==27).collect(Collectors.toList());				
				listaAux.stream().forEach(tr->{
					try {
						tableResponsesFacade.eliminarRespuestasTabla(tr);	
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
				
				for (TableResponses respuestaTabla : avanceEjecucion.getTableResponsesList()) {
					if(respuestaTabla.getTareId()==null)
						tableResponsesFacade.create(respuestaTabla);
					else
						tableResponsesFacade.edit(respuestaTabla);
				}
				for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) {
					if(respuestas.getVaanId()==null)
						valueAnswersFacade.create(respuestas);
					else
						valueAnswersFacade.edit(respuestas);
				}
				
			}else if(salvaguarda>2){
				edit(avanceEjecucion);
				for (TableResponses respuestaTabla : avanceEjecucion.getTableResponsesList()) {
					if(respuestaTabla.getTareId()==null)
						tableResponsesFacade.create(respuestaTabla);
					else
						tableResponsesFacade.edit(respuestaTabla);
				}
				for (ValueAnswers respuestas : avanceEjecucion.getValueAnswersList()) {
					if(respuestas.getVaanId()==null)
						valueAnswersFacade.create(respuestas);
					else
						valueAnswersFacade.edit(respuestas);
				}
			}
			
		}
		return avanceEjecucion;

	}

}
