package ec.gob.ambiente.sis.services;

import java.math.BigDecimal;
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
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.model.ValueAnswers;

@Stateless
@LocalBean
public class TableResponsesFacade extends AbstractFacade<TableResponses, Integer> {
	@EJB
	private ValueAnswersFacade valueAnswersFacade;



	public TableResponsesFacade() {
		super(TableResponses.class,Integer.class);
	}
	
	/**
	 * Carga los valores de respuesta tipo tabla por avance de ejecucion
	 * @param codigoAvanceEjecucion
	 * @return
	 */
	public List<TableResponses> findByAdvanceExecution(int codigoAvanceEjecucion) throws Exception{
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Eliminar registro de TableResponses
	 * @param tableResponses
	 * @throws Exception
	 */
	public void eliminarRespuestasTabla(TableResponses tableResponses) throws Exception{
		tableResponses.setTareStatus(false);
		edit(tableResponses);
	}
	/**
	 * Elimina los datos de la tabla (coloca estado de elimancion a false)
	 * @param tablaDatos
	 * @throws Exception
	 */
	public void eliminarDatosTabla(List<TableResponses> tablaDatos) throws Exception{
		for (TableResponses tableResponses : tablaDatos) {
			tableResponses.setTareStatus(false);
			edit(tableResponses);
		}
	}
	public void eliminarDatosTablaActualizaValueAnswer(List<TableResponses> tablaDatos,ValueAnswers valueAnswer) throws Exception{
		for (TableResponses tableResponses : tablaDatos) {
			tableResponses.setTareStatus(false);
			edit(tableResponses);			
		}
		valueAnswer.setVaanYesnoAnswerValue(false);
		valueAnswersFacade.edit(valueAnswer);
	}
	public List<TableResponses> buscarPorAvanceEjecucionYSalvaguarda(int codigoAvanceEjecucion, int codigoSalvaguarda) throws Exception{
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.safeguards.safeId=:codigoSalvaguarda ORDER BY TR.questions.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		camposCondicion.put("codigoSalvaguarda", codigoSalvaguarda);
		List<TableResponses> tabla=findByCreateQuery(sql, camposCondicion);
		for (TableResponses tableResponses : tabla) {
			Hibernate.initialize(tableResponses.getQuestions());
		}
//		return findByCreateQuery(sql, camposCondicion);
		return tabla;
	}

	public TableResponses buscaLeyPolitica(int codigoLeyPolitica,int codigoAvanceEjecucion,int codigoPregunta) throws DaoException{
		try{
			String sql="SELECT TR FROM TableResponses TR WHERE TR.tareColumnNumberOne  =:codigoLeyPolitica AND TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.quesId =:codigoPregunta";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);		
			camposCondicion.put("codigoLeyPolitica", codigoLeyPolitica);
			camposCondicion.put("codigoPregunta", codigoPregunta);			
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			e.printStackTrace();
			throw new DaoException();
		}
	}
	public void agregarEditarNuevaTableResponse(TableResponses tableResponse) throws Exception{
		if(tableResponse.getTareId()==null)
			create(tableResponse);
		else
			edit(tableResponse);
		
	}
	public boolean agregaTablaResponseValueAnswers(TableResponses tableResponse,ValueAnswers valueAnswers,List<ValueAnswers> listaValoresRespuestas) throws Exception{
		boolean seAgregaronValoresRespuestas=false;
		if(tableResponse.getTareId()==null)
			create(tableResponse);
		else
			edit(tableResponse);
		if (valueAnswers!=null && valueAnswers.getVaanId()==null){
			for (ValueAnswers valores : listaValoresRespuestas) {
				if(valores.getQuestions().getQuesId() == valueAnswers.getQuestions().getQuesId())
					valores = valueAnswers;
				valueAnswersFacade.create(valores);
			}
			seAgregaronValoresRespuestas = true;
		}else{
			valueAnswersFacade.edit(valueAnswers);
		}
		return seAgregaronValoresRespuestas;	
	}
	public void agregaRespuestaTabla(TableResponses respuesta,ValueAnswers valorRespuesta) throws Exception{
		if(respuesta.getTareId()==null){
			create(respuesta);
			valueAnswersFacade.edit(valorRespuesta);
		}
			
	}
	/**
	 * Busca por avanceEjecucion y Genero
	 * @param codigoAvanceEjecucion
	 * @return
	 * @throws Exception
	 */
	public List<TableResponses> buscarPorAvanceEjecucionYGenero(int codigoAvanceEjecucion) throws Exception{
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.quesIsGender = TRUE ORDER BY TR.questions.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		List<TableResponses> tabla=findByCreateQuery(sql, camposCondicion);
		for (TableResponses tableResponses : tabla) {
			Hibernate.initialize(tableResponses.getQuestions());
		}
		return tabla;
	}
	
	public List<TableResponses> buscarPorAvanceEjecucionPreguntaProCanParr(int codigoAvanceEjecucion, int codigoPregunta,int codigoProv,int codigoCan,int codigoParr) throws Exception{
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.quesId = :codigoPregunta AND TR.tareColumnNumberOne=:codigoProv AND TR.tareColumnNumberTwo=:codigoCan AND TR.tareColumnNumberThree=:codigoParr  ORDER BY TR.tareId";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		camposCondicion.put("codigoPregunta", codigoPregunta);
		camposCondicion.put("codigoProv", codigoProv);
		camposCondicion.put("codigoCan", codigoCan);
		camposCondicion.put("codigoParr", codigoParr);
		List<TableResponses> tabla=findByCreateQuery(sql, camposCondicion);
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<TableResponses> listaAvanceEjecucionPregunta(int codigoAvanceEjecucion, int codigoPregunta) throws Exception{
		String sql="SELECT TR FROM TableResponses TR WHERE TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.quesId=:codigoPregunta ORDER BY TR.tareId";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
		camposCondicion.put("codigoPregunta", codigoPregunta);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Devuelve los proyectos registrados en la salvaguarda A
	 * @return
	 * @throws Exception
	 */
	public List<TableResponses> listaProyectosValoresSalvaguardaA() throws Exception{
		List<Object[]> resultado= null;
		List<TableResponses> listaResultado = new ArrayList<TableResponses>();
		String sql ="SELECT tare_column_decimal_one,tare_column_number_six FROM sis.table_responses WHERE ques_id=5 AND tare_status= TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				TableResponses tr= new TableResponses();
				tr.setTareColumnDecimalOne((BigDecimal)dataObj[0]);
				tr.setTareColumnNumberSix((Integer)dataObj[1]);
				listaResultado.add(tr);
			}
		}
		return listaResultado;
	}
	/**
	 * Lista comunidades salvaguarda B_C
	 * @param codigoPregunta
	 * @return
	 * @throws Exception
	 */
	public List<TableResponses> listaComunidadesSalvaguardaB_C_G(int codigoPregunta) throws Exception{
		List<Object[]> resultado= null;
		List<TableResponses> listaResultado = new ArrayList<TableResponses>();
		String sql ="SELECT lower(tare_column_one),ques_id FROM sis.table_responses WHERE tare_status=TRUE AND ques_id=" + codigoPregunta;
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				TableResponses tr= new TableResponses();
				tr.setTareColumnOne((String)dataObj[0]);
				tr.setTareColumnNumberOne((Integer)dataObj[1]);
				listaResultado.add(tr);
			}
		}
		return listaResultado;
	}
	/**
	 * Maximo valor de hombres y mujeres salvaguarda B
	 * @return
	 * @throws Exception
	 */
	public List<TableResponses> listaMaximoHombresMujeresSalvaguardaB() throws Exception{
		List<Object[]> resultado= null;
		List<TableResponses> listaResultado = new ArrayList<TableResponses>();
		String sql ="SELECT MAX(tare_column_number_seven)AS hombres,MAX(tare_column_number_eight)AS mujeres FROM sis.table_responses WHERE ques_id=16 AND tare_status= TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				TableResponses tr= new TableResponses();
				tr.setTareColumnNumberOne((Integer)dataObj[0]);
				tr.setTareColumnNumberTwo((Integer)dataObj[1]);
				listaResultado.add(tr);
			}
		}
		return listaResultado;
	}
	/**
	 * Numero de practicas o saberes ancestrales
	 * @return
	 * @throws Exception
	 */
	public int listaSaberesAncestralesSalvaguardaC() throws Exception{
		Integer valor= new Integer(0);
		List<Object[]> resultado= null;		
		String sql ="SELECT COUNT(tare_id) FROM sis.table_responses WHERE ques_id=45 AND tare_status= TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado)
				valor = Integer.valueOf(obj.toString());
		}
		return valor;
	}
	/**
	 * Provincias con gestion comunitaria
	 * @return
	 * @throws Exception
	 */
	public List<TableResponses> listaFomentoGestionComunitariaE() throws Exception{
		List<Object[]> resultado= null;
		List<TableResponses> listaResultado = new ArrayList<TableResponses>();
		String sql ="SELECT DISTINCT tare_column_number_one FROM sis.table_responses WHERE ques_id = 166 AND tare_status= TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				TableResponses tr= new TableResponses();
				tr.setTareColumnNumberOne(Integer.valueOf(obj.toString()));				
				listaResultado.add(tr);
			}
		}
		return listaResultado;
	}
	/**
	 * Total de hectareas de cobertura vegetal
	 * @return
	 * @throws Exception
	 */
	public BigDecimal totalHectareasCoberturaE() throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);		
		String sql ="SELECT SUM(tare_column_number_eight) FROM sis.table_responses WHERE ques_id= 169 AND tare_status= TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	public BigDecimal listaRecursosInvertidosF() throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);		
		String sql ="SELECT SUM(tare_column_decimal_one) FROM sis.table_responses WHERE ques_id=117 AND tare_status= TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	
	public int listaAccionesGeneradasSalvaguardaG() throws Exception{
		Integer valor= new Integer(0);
		List<Object[]> resultado= null;		
		String sql ="SELECT COUNT(tare_id) FROM sis.table_responses WHERE ques_id=131 AND tare_status= TRUE";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = Integer.valueOf(obj.toString());
			}
		}
		return valor;
	}
	
	public Integer listaBeneficiariosSalvaguardaG() throws Exception{
		Integer total=new Integer(0);
		List<Object[]> resultado= null;
		String sql ="SELECT SUM(tare_column_number_seven) AS hombres,SUM(tare_column_number_eight) AS mujeres FROM sis.table_responses WHERE ques_id=131 AND tare_status= TRUE";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				TableResponses tr= new TableResponses();
				if(dataObj[0]!=null)
					tr.setTareColumnNumberOne(Integer.valueOf( dataObj[0].toString()));
				else
					tr.setTareColumnNumberOne(0);
				if(dataObj[1]!=null)
					tr.setTareColumnNumberTwo(Integer.valueOf(dataObj[1].toString()));
				else
					tr.setTareColumnNumberTwo(0);
				total = tr.getTareColumnNumberOne() + tr.getTareColumnNumberTwo();
			}
		}
		return total;
	}
	/**
	 * Listado de proyectos registrados
	 * @return
	 * @throws Exception
	 */
	public List<String> listadoProyectos() throws Exception{
		List<Object[]> resultado= null;
		List<String> listaResultado = new ArrayList<String>();
		String sql ="SELECT DISTINCT tr.tare_column_number_six ,  ca.cata_text2 FROM sis.questions q, sis.table_responses tr, sis.catalogs ca " +
				" WHERE q.ques_id = tr.ques_id AND tr.ques_id=5 AND ca.cata_id=tr.tare_column_number_six AND tr.tare_status = true "+
				" ORDER BY ca.cata_text2";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				listaResultado.add(dataObj[1].toString());
			}
		}
		return listaResultado;
	}
}
