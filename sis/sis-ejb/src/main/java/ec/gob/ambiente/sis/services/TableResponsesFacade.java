package ec.gob.ambiente.sis.services;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.hibernate.Hibernate;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.dto.DtoSalvaguardaA;
import ec.gob.ambiente.sis.dto.DtoSalvaguardaF;
import ec.gob.ambiente.sis.dto.DtoTableResponses;
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
			String sql="SELECT TR FROM TableResponses TR WHERE TR.tareLawPolitical  =:codigoLeyPolitica AND TR.advanceExecutionSaveguards.adexId=:codigoAvanceEjecucion AND TR.tareStatus=TRUE AND TR.questions.quesId =:codigoPregunta";
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
//		String sql ="SELECT tare_column_decimal_one,tare_column_number_six FROM sis.table_responses WHERE ques_id=5 AND tare_status= TRUE;";
		String sql ="SELECT tr.tare_column_decimal_one,CASE WHEN tr.tare_column_number_six > 0 THEN ca.cata_text2 ELSE tr.tare_another_catalog END " + 
					" FROM sis.table_responses tr, sis.catalogs ca, sis.advance_execution_safeguards aex,sigma.projects p  " +
					" WHERE tr.tare_column_number_six = ca.cata_id AND tr.ques_id=5 AND tr.tare_status= TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE" +
					" UNION " +
					" SELECT tr.tare_column_decimal_one,tr.tare_another_catalog " + 
					" FROM sis.table_responses tr, sis.advance_execution_safeguards aex,sigma.projects p   WHERE tr.ques_id=5 AND tr.tare_status= TRUE AND tr.tare_column_number_six = 0 AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE;"; 
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				TableResponses tr= new TableResponses();
				tr.setTareColumnDecimalOne((BigDecimal)dataObj[0]);
//				tr.setTareColumnNumberSix((Integer)dataObj[1]);
				tr.setTareColumnOne(dataObj[1].toString());
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
		String sql ="SELECT lower(tr.tare_column_one),tr.ques_id FROM sis.table_responses tr, sis.advance_execution_safeguards aex,sigma.projects p  WHERE tr.tare_status=TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE AND tr.ques_id=" + codigoPregunta;
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
		String sql ="SELECT MAX(tr.tare_column_number_seven)AS hombres,MAX(tr.tare_column_number_eight)AS mujeres FROM sis.table_responses tr, sis.advance_execution_safeguards aex,sigma.projects p  WHERE tr.ques_id=16 AND tr.tare_status= TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE";
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
		String sql ="SELECT COUNT(tr.tare_id) FROM sis.table_responses tr, sis.advance_execution_safeguards aex,sigma.projects p WHERE tr.ques_id=45 AND tr.tare_status= TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE;";
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
		String sql ="SELECT DISTINCT tr.tare_column_number_one FROM sis.table_responses tr, sis.advance_execution_safeguards aex,sigma.projects p WHERE ques_id = 166 AND tare_status= TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE;";
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
		String sql ="SELECT SUM(tr.tare_column_number_eight) FROM sis.table_responses tr, sis.advance_execution_safeguards aex,sigma.projects p WHERE tr.ques_id= 169 AND tr.tare_status= TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE;";
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
		String sql ="SELECT COUNT(tr.tare_id) FROM sis.table_responses tr, sis.advance_execution_safeguards aex,sigma.projects p WHERE tr.ques_id=131 AND tr.tare_status= TRUE AND p.proj_status = TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id";
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
	 * Lista de proyectos de conservacion
	 * @return
	 * @throws Exception
	 */
	
	public List<DtoSalvaguardaA> listadoProyectosConservacion() throws Exception{
		List<Object[]> resultado= null;
		List<DtoSalvaguardaA> listaResultado = new ArrayList<DtoSalvaguardaA>();
//		String sql ="SELECT  ca.cata_text2, sum(tr.tare_column_decimal_one) FROM sis.questions q, sis.table_responses tr, sis.catalogs ca " + 
//				 " WHERE q.ques_id = tr.ques_id AND tr.ques_id=5 AND ca.cata_id=tr.tare_column_number_six AND tr.tare_status = true " + 
//				 " GROUP BY ca.cata_text2";
		String sql ="SELECT  CASE WHEN LENGTH(ca.cata_text2)>40 THEN substring(ca.cata_text2 from 1 for 39) ELSE ca.cata_text2 END, sum(tr.tare_column_decimal_one) FROM sis.questions q, sis.table_responses tr, sis.catalogs ca , sis.advance_execution_safeguards aex,sigma.projects p " + 
					" WHERE q.ques_id = tr.ques_id AND tr.ques_id=5 AND ca.cata_id=tr.tare_column_number_six AND tr.tare_status = true AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE" + 
					" GROUP BY ca.cata_text2 " +
					" UNION " +
					" SELECT  CASE WHEN LENGTH(tr.tare_another_catalog) > 40 THEN substring(tr.tare_another_catalog from 1 for 39) ELSE tr.tare_another_catalog END,  sum(tr.tare_column_decimal_one) FROM sis.questions q, sis.table_responses tr , sis.advance_execution_safeguards aex,sigma.projects p " +
					" WHERE q.ques_id = tr.ques_id AND tr.ques_id=5 AND tr.tare_status = true AND tr.tare_column_number_six = 0 AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE" + 
					" GROUP BY tr.tare_another_catalog ";	
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				DtoSalvaguardaA dto= new DtoSalvaguardaA();
				Object[] dataObj = (Object[]) obj;
				dto.setProyecto(dataObj[0].toString());
				dto.setPresupuesto(Double.parseDouble(dataObj[1].toString()));				
				listaResultado.add(dto);
			}
		}
		return listaResultado;
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
	/**
	 * Numero de acciones para evitar riesgos de reversión
	 * @return
	 * @throws Exception
	 */
	public int numeroAccionesEvitarRiesgos_F() throws Exception{
		Integer valor= new Integer(0);
		List<Object[]> resultado= null;		
		String sql ="SELECT COUNT(tr.tare_id) as total FROM sis.table_responses tr, sis.advance_execution_safeguards aex,sigma.projects p  WHERE tr.ques_id=115 AND tr.tare_status = TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = Integer.valueOf(obj.toString());
			}
		}
		return valor;
	}

	/**
	 * Listado de medidas tomadas
	 * @return
	 * @throws Exception
	 */
	public List<DtoSalvaguardaF> listaMedidasTomadas_F() throws Exception{
		List<Object[]> resultado= null;
		List<DtoSalvaguardaF> listaResultado = new ArrayList<DtoSalvaguardaF>();
		String sql ="SELECT  tr.tare_column_two ,ca.cata_text2 FROM sis.table_responses  tr, sis.catalogs ca , sis.advance_execution_safeguards aex,sigma.projects p " +
					" WHERE tr.tare_column_number_six = ca.cata_id AND tr.ques_id=115 AND tr.tare_status = TRUE AND tr.adex_id = aex.adex_id AND p.proj_id = aex.proj_id AND p.proj_status = TRUE ORDER BY ca.cata_text2";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				DtoSalvaguardaF tr= new DtoSalvaguardaF();
				tr.setTextoUno(dataObj[0].toString());
				tr.setTextoDos(dataObj[1].toString());
				listaResultado.add(tr);
			}
		}
		return listaResultado;
	}
	/**
	 * Instrumentos de politica publica.
	 * @return
	 * @throws Exception
	 */
	public int numeroInstrumentosPolitica_A() throws Exception{
		Integer valor= new Integer(0);
		List<Object[]> resultado= null;		
		String sql ="SELECT COUNT(tare_id) as total FROM sis.table_responses WHERE ques_id=4 AND tare_status = TRUE";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = Integer.valueOf(obj.toString());
			}
		}
		return valor;
	}
	/**
	 * Lista de proyectos invertidos
	 * @return
	 * @throws Exception
	 */
	public BigDecimal cantidadProyectosInvertidos_A() throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);		
		String sql ="SELECT SUM(tare_column_decimal_one) FROM sis.table_responses WHERE ques_id=5 AND tare_status= TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	/**
	 * Permite consultar el numero de registros de acuerdo al tipo de pregunta
	 * @param pregunta
	 * @return
	 * @throws Exception
	 */
	public int numeroDeRegistros(Integer pregunta) throws Exception{
		Integer valor= new Integer(0);
		List<Object[]> resultado= null;		
		String sql ="SELECT COUNT(tare_id) as total FROM sis.table_responses WHERE tare_status = TRUE AND ques_id = " + pregunta;
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = Integer.valueOf(obj.toString());
			}
		}
		return valor;
	}
	
	public BigDecimal numeroDeHectareas(Integer pregunta) throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);		
		String sql ="SELECT SUM(tare_column_decimal_one) FROM sis.table_responses WHERE tare_status= TRUE  AND ques_id= "+pregunta;
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	
	public Integer totalActores() throws Exception{
		Integer total=new Integer(0);
		List<Object[]> resultado= null;
		String sql ="SELECT sum(tare_column_number_five) as h, sum(tare_column_number_six) as m FROM sis.table_responses WHERE tare_status= true AND ques_id=27";
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
	
	public int numeroCanalesHabilitados() throws Exception{
		Integer valor= new Integer(0);
		List<Object[]> resultado= null;		
		String sql ="SELECT COUNT(cata_id) FROM sis.catalogs WHERE caty_id=5 AND cata_status = TRUE";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = Integer.valueOf(obj.toString());
			}
		}
		return valor;
	}
	
	public BigDecimal numeroAccionesTenenciaTierra() throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);		
		String sql ="SELECT COUNT(tare_id) FROM sis.table_responses tr, sis.catalogs c " +
					" WHERE c.cata_id=tr.tare_column_number_eight AND tr.ques_id=43 AND tr.tare_status = TRUE AND cata_text1 like 'Acceso a propiedad de la tierra'" ;
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	
	public Integer totalActoresDialogo() throws Exception{
		Integer total=new Integer(0);
		List<Object[]> resultado= null;
		String sql ="SELECT sum(tare_column_number_six) as h, sum(tare_column_number_seven) as m FROM sis.table_responses WHERE tare_status= true AND ques_id=65";
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
	
	public BigDecimal totalMujeresDialogo() throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);
		String sql ="SELECT sum(tare_column_number_seven) as m FROM sis.table_responses WHERE tare_status= true AND ques_id=65";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	
	public BigDecimal numeroHectareasConsolidadas() throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);		
		String sql ="SELECT sum(tare_column_decimal_one) as h FROM sis.table_responses WHERE tare_status= true AND ques_id=69";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	
	public Integer totalActoresMonitoreo() throws Exception{
		Integer total=new Integer(0);
		List<Object[]> resultado= null;
		String sql ="SELECT sum(tare_column_number_seven) as h, sum(tare_column_number_eight) as m FROM sis.table_responses WHERE tare_status= true AND ques_id=126";
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
	
	public Integer totalBeneficiariosAlternativasEconomicas() throws Exception{
		Integer total=new Integer(0);
		List<Object[]> resultado= null;
		String sql ="SELECT sum(tare_column_number_seven) as h, sum(tare_column_number_eight) as m FROM sis.table_responses WHERE tare_status= true AND ques_id=131";
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
	
	public BigDecimal totalPresupuestoSNMB() throws Exception{
		List<Object[]> resultado= null;
		BigDecimal valor= new BigDecimal(0);		
		String sql ="SELECT sum(tare_column_decimal_one) as p FROM sis.table_responses WHERE tare_status= true AND ques_id=133";
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				if(obj != null)
					valor = new BigDecimal(obj.toString());
			}
		}
		return valor;
	}
	
	/**
	 * Lista de politicas leyes para archivo bd
	 * @param pregunta
	 * @param cataTipo
	 * @return
	 * @throws Exception
	 */
	public List<DtoTableResponses> listaPoliticasLeyes(Integer pregunta,Integer cataTipo) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador, CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END,ca.cata_text2 FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE " +
				" AND tr.tare_status = TRUE AND ca.cata_id = tr.tare_law_political AND tr.ques_id = " +pregunta 
				+ " AND ca.caty_id = " + cataTipo; 
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
				lista.add(dto);
			}
		}
		return lista;
	}
	/**
	 * Lista los programas, proyectos pregunta 3 salvaguarda A
	 * @return
	 * @throws Exception
	 */
	public List<DtoTableResponses> listaProgProyA_3() throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo, par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END,ca.cata_text2 , tr.tare_column_decimal_one " +
				"FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND ca.cata_id = tr.tare_column_number_six AND tr.ques_id = 5 " ;
 
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
					dto.setDecimalUno(new BigDecimal(dataObj[5].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	
	/**
	 * Informacion adicioanal para la salvaguarda
	 * @param pregunta
	 * @return
	 * @throws Exception
	 */
	public List<DtoTableResponses> listaInformacionAdicional(Integer pregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, tr.tare_column_two, tr.tare_column_three " + 
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id ="+ pregunta ;
 
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
				lista.add(dto);
			}
		}
		return lista;
	}
	/**
	 * Recupera la informacion de las preguntas B_4
	 * @param codigoPregunta
	 * @return
	 * @throws Exception
	 */
	public List<DtoTableResponses> listaPreguntas_B_4(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_nine, tr.tare_column_number_one , tr.tare_column_number_two , tr.tare_column_number_three, tr.tare_column_number_four , tr.tare_column_number_five , tr.tare_column_number_six , tr.tare_column_number_seven , tr.tare_column_number_eight " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id =" + codigoPregunta;
 
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
				if(dataObj[5]!=null){
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");					
					Date fecha = formato.parse(dataObj[5].toString());
					dto.setFecha(fecha);					
				}
				if(dataObj[6]!=null){
					dto.setNumeroUno(Integer.valueOf(dataObj[6].toString()));					
				}
				if(dataObj[7]!=null){
					dto.setNumeroDos(Integer.valueOf(dataObj[7].toString()));					
				}
				if(dataObj[8]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[13].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	/**
	 * Informacion pregunta B_5
	 * @param codigoPregunta
	 * @return
	 * @throws Exception
	 */
	public List<DtoTableResponses> listaPreguntas_B_5(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three, tr.tare_column_number_one , tr.tare_column_number_two , tr.tare_column_number_three, tr.tare_column_number_six , tr.tare_column_number_seven , tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE"+
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroTres(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[12].toString()));				
				lista.add(dto);
			}
		}
		return lista;
	}
	/**
	 * Información pregunta B_6
	 * @param codigoPregunta
	 * @return
	 * @throws Exception
	 */
	public List<DtoTableResponses> listaPreguntas_B_6(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one," + 
				" tr.tare_column_two,tr.tare_column_three, tr.tare_column_number_one , tr.tare_column_number_two  " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id =" + codigoPregunta;
 
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
								
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_B_7(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three, tr.tare_column_four FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id =" + codigoPregunta;
 
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
					dto.setTextoCuatro(dataObj[7].toString());										
												
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_B_8(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_nine,tr.tare_column_decimal_one, tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_column_number_six, tr.tare_column_number_seven, tr.tare_column_number_eight " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id AND p.proj_status = TRUE" +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id  AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
				if(dataObj[5]!=null){
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");					
					Date fecha = formato.parse(dataObj[5].toString());
					dto.setFecha(fecha);					
				}
				if(dataObj[6]!=null)
					dto.setDecimalUno(new BigDecimal(dataObj[6].toString()));				
				if(dataObj[7]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[7].toString()));									
				if(dataObj[8]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[8].toString()));										
				if(dataObj[9]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[13].toString()));
				if(dataObj[14]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[14].toString()));

				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_B_9(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				"tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_four, tr.tare_column_number_five " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id AND p.proj_status = TRUE" +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id " +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroUno(Integer.valueOf(dataObj[5].toString()));									
				if(dataObj[6]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[6].toString()));										
				if(dataObj[7]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[9].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_B_10(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				"tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_column_number_six, tr.tare_column_number_seven, tr.tare_column_number_eight , tr.tare_column_decimal_one" +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id AND p.proj_status = TRUE" +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id " +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroUno(Integer.valueOf(dataObj[5].toString()));									
				if(dataObj[6]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[6].toString()));										
				if(dataObj[7]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setDecimalUno(new BigDecimal(dataObj[13].toString()));		
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_B_11(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two, tr.tare_column_number_six, tr.tare_column_number_seven " +
				"FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroUno(Integer.valueOf(dataObj[6].toString()));									
				if(dataObj[7]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[7].toString()));										
						
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_B_12(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_column_number_six, tr.tare_column_number_seven, tr.tare_column_number_eight, tr.tare_column_number_nine " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroUno(Integer.valueOf(dataObj[6].toString()));									
				if(dataObj[7]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[7].toString()));										
				if(dataObj[8]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[8].toString()));									
				if(dataObj[9]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[10].toString()));									
				if(dataObj[11]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[11].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_B_13(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two, tr.tare_column_nine, tr.tare_column_number_one, tr.tare_column_number_six, tr.tare_column_number_seven, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id =" + codigoPregunta;
 
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
				if(dataObj[6]!=null){
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");					
					Date fecha = formato.parse(dataObj[6].toString());
					dto.setFecha(fecha);					
				}
				if(dataObj[7]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[7].toString()));									
				if(dataObj[8]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[8].toString()));										
				if(dataObj[9]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[9].toString()));									
				if(dataObj[10]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[10].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_B_14(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two ,tr.tare_column_three,tr.tare_column_four, tr.tare_column_nine, tr.tare_column_number_one, tr.tare_column_number_six, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " + 
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id =" + codigoPregunta;
 
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
					dto.setTextoCuatro(dataObj[7].toString());
				if(dataObj[8]!=null){
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");					
					Date fecha = formato.parse(dataObj[8].toString());
					dto.setFecha(fecha);					
				}
				if(dataObj[9]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[9].toString()));									
				if(dataObj[10]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[10].toString()));										
				if(dataObj[11]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[11].toString()));									
				
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_C_20(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two ,tr.tare_column_decimal_one, tr.tare_column_decimal_two,tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_column_number_six, tr.tare_column_number_seven, tr.tare_column_number_eight, tr.tare_code_component "+
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setDecimalUno(new BigDecimal(dataObj[6].toString()));
				if(dataObj[7]!=null)
					dto.setDecimalDos(new BigDecimal(dataObj[7].toString()));				
				if(dataObj[8]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[8].toString()));									
				if(dataObj[9]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[9].toString()));										
				if(dataObj[10]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[13].toString()));
				if(dataObj[14]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[14].toString()));
				if(dataObj[15]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[15].toString()));
				if(dataObj[16]!=null)
					dto.setNumeroNueve(Integer.valueOf(dataObj[16].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_C_21(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three,tr.tare_column_four,tr.tare_column_five, tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setTextoCuatro(dataObj[7].toString());				
				if(dataObj[8]!=null)
					dto.setTextoCinco(dataObj[8].toString());									
				if(dataObj[9]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[9].toString()));										
				if(dataObj[10]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[10].toString()));					
				if(dataObj[11]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[11].toString()));					
				if(dataObj[12]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[12].toString()));					
				if(dataObj[13]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[13].toString()));					
				if(dataObj[14]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[14].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_C_24(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				"tr.tare_column_two,tr.tare_column_three, tr.tare_column_nine, tr.tare_column_number_six, tr.tare_column_number_seven " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
				if(dataObj[7]!=null){
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");					
					Date fecha = formato.parse(dataObj[7].toString());
					dto.setFecha(fecha);					
				}
				if(dataObj[8]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[8].toString()));										
				if(dataObj[9]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[9].toString()));					
				
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_C_24_2(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				"tr.tare_column_two " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
				
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_C_26(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " +
		" tr.tare_column_two,tr.tare_column_three,tr.tare_column_four,tr.tare_column_number_one,tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setTextoCuatro(dataObj[7].toString());
				if(dataObj[8]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[8].toString()));										
				if(dataObj[9]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[9].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	public List<DtoTableResponses> listaPreguntas_C_27(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " +
				" tr.tare_column_two,tr.tare_column_three, tr.tare_column_decimal_one, tr.tare_column_number_one, tr.tare_column_number_four,tr.tare_column_number_five, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setDecimalUno(new BigDecimal(dataObj[7].toString()));				
				if(dataObj[8]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[8].toString()));										
				if(dataObj[9]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[11].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_C_28(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three, tr.tare_column_decimal_one, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setDecimalUno(new BigDecimal(dataObj[7].toString()));				
				if(dataObj[8]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[8].toString()));										
				if(dataObj[9]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[10].toString()));
				
				lista.add(dto);
			}
		}
		return lista;
	}
	public List<DtoTableResponses> listaPreguntas_C_29(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three, tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_column_number_six, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroTres(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[13].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_C_30(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three,tr.tare_column_four,tr.tare_column_five,tr.tare_column_six, tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setTextoCuatro(dataObj[7].toString());
				if(dataObj[8]!=null)
					dto.setTextoCinco(dataObj[8].toString());
				if(dataObj[9]!=null)
					dto.setTextoSeis(dataObj[9].toString());
				if(dataObj[10]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[10].toString()));										
				if(dataObj[11]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[13].toString()));
				
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_C_31(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_nine, tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_column_number_six, tr.tare_column_number_seven, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
				if(dataObj[6]!=null){
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");					
					Date fecha = formato.parse(dataObj[6].toString());
					dto.setFecha(fecha);					
				}				
				if(dataObj[7]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[7].toString()));										
				if(dataObj[8]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[13].toString()));
				if(dataObj[14]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[14].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_D_32(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three,tr.tare_column_four, tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_column_number_six, tr.tare_column_number_seven, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
				if(dataObj[6]!=null){
					dto.setTextoTres(dataObj[6].toString());						
				}				
				if(dataObj[7]!=null)
					dto.setTextoCuatro(dataObj[7].toString());										
				if(dataObj[8]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[13].toString()));
				if(dataObj[14]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[14].toString()));
				if(dataObj[15]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[15].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_D_33(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three,tr.tare_column_four,tr.tare_column_decimal_one, tr.tare_column_number_one, tr.tare_column_number_four, tr.tare_column_number_five, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
				if(dataObj[6]!=null){
					dto.setTextoTres(dataObj[6].toString());						
				}				
				if(dataObj[7]!=null)
					dto.setTextoCuatro(dataObj[7].toString());										
				if(dataObj[8]!=null)
					dto.setDecimalUno(new BigDecimal(dataObj[8].toString()));	
				if(dataObj[9]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[12].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_E_34(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_decimal_one, tr.tare_column_number_one, tr.tare_column_number_two, tr.tare_column_number_three, tr.tare_column_number_six, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa  " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setDecimalUno(new BigDecimal(dataObj[6].toString()));	
				if(dataObj[7]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[11].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_E_35(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two, tr.tare_column_number_six, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroUno(Integer.valueOf(dataObj[6].toString()));
				if(dataObj[7]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[7].toString()));				
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_E_36(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " +
				" tr.tare_column_two, tr.tare_column_decimal_one,tr.tare_column_number_one,tr.tare_column_number_two,tr.tare_column_number_three,tr.tare_column_number_four,tr.tare_column_number_five, tr.tare_code_component " + 
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setDecimalUno(new BigDecimal(dataObj[6].toString()));	
				if(dataObj[7]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[12].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_E_37(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three, tr.tare_column_nine,tr.tare_column_number_one,tr.tare_column_number_two,tr.tare_column_number_three,tr.tare_column_number_six,tr.tare_column_number_seven,tr.tare_column_number_eight,tr.tare_column_number_nine, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
				if(dataObj[7]!=null){
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");					
					Date fecha = formato.parse(dataObj[7].toString());
					dto.setFecha(fecha);					
				}					
				if(dataObj[8]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[13].toString()));
				if(dataObj[14]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[14].toString()));
				if(dataObj[15]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[15].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_E_37_2(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, va.vaan_text_answer_value " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.value_answers va, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND va.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND va.vaan_status = TRUE AND LENGTH (va.vaan_text_answer_value)>1 AND va.ques_id = " + codigoPregunta;
 
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
				lista.add(dto);
			}
		}
		return lista;
	}

	public List<DtoTableResponses> listaPreguntas_E_38(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_number_one,tr.tare_column_number_two,tr.tare_column_number_three,tr.tare_column_number_six, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroUno(Integer.valueOf(dataObj[6].toString()));
				if(dataObj[7]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[10].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_E_39(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_number_one,tr.tare_column_number_two,tr.tare_column_number_three,tr.tare_column_number_six,tr.tare_column_number_seven, tr.tare_code_component " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id =  " + codigoPregunta;
 
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
					dto.setNumeroUno(Integer.valueOf(dataObj[6].toString()));
				if(dataObj[7]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[11].toString()));
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_E_40(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_number_one,tr.tare_column_number_five,tr.tare_column_number_six,tr.tare_column_number_seven " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id =" + codigoPregunta;
 
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
					dto.setNumeroUno(Integer.valueOf(dataObj[5].toString()));
				if(dataObj[6]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[6].toString()));
				if(dataObj[7]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[8].toString()));				
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_E_40_X(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_decimal_one,tr.tare_column_number_one,tr.tare_column_number_five,tr.tare_column_number_six,tr.tare_column_number_seven,tr.tare_column_number_eight,tr.tare_column_number_two,tr.tare_column_two " +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				", sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setDecimalUno(new BigDecimal(dataObj[5].toString()));	
				if(dataObj[6]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[6].toString()));
				if(dataObj[7]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[7].toString()));
				if(dataObj[8]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[8].toString()));
				if(dataObj[9]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setTextoDos(dataObj[12].toString());
				lista.add(dto);
			}
		}
		return lista;
	}
	
	public List<DtoTableResponses> listaPreguntas_F_41_1(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three,tr.tare_column_number_one,tr.tare_column_number_two,tr.tare_column_number_three,tr.tare_column_number_four,tr.tare_column_number_five,tr.tare_column_number_six, tr.tare_code_component, " +
				" tr.tare_column_four,tr.tare_column_decimal_one ,tr.tare_column_number_seven,tr.tare_column_number_eight,tr.tare_column_nine" +
				" FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id AND p.proj_status = TRUE" +
				" AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setNumeroTres(Integer.valueOf(dataObj[9].toString()));
				if(dataObj[10]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[13].toString()));
				if(dataObj[14]!=null)
					dto.setTextoCuatro(dataObj[14].toString());
				if(dataObj[15]!=null)
					dto.setDecimalUno(new BigDecimal(dataObj[15].toString()));
				if(dataObj[16]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[16].toString()));
				if(dataObj[17]!=null)
					dto.setNumeroNueve(Integer.valueOf(dataObj[17].toString()));
				if(dataObj[18]!=null){
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");					
					Date fecha = formato.parse(dataObj[18].toString());
					dto.setFecha(fecha);					
				}	
				lista.add(dto);
			}
		}
		return lista;
	}
	/**
	 * Preguntas de Genero
	 * @param codigoPregunta
	 * @return
	 * @throws Exception
	 */
	public List<DtoTableResponses> listaPreguntas_Genero(int codigoPregunta) throws Exception{
		List<DtoTableResponses> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT DISTINCT  p.proj_title,CONCAT(aes.adex_term_from,' / ',aes.adex_term_to)as periodo,par.part_name as socioimplementador , CASE WHEN aes.pspa_id IS NULL THEN '' ELSE pa.part_name END, tr.tare_column_one, " + 
				" tr.tare_column_two,tr.tare_column_three,tr.tare_column_four,tr.tare_column_five,tr.tare_column_six,tr.tare_column_number_one,tr.tare_column_number_two,tr.tare_column_number_three,tr.tare_column_number_four,tr.tare_column_number_five,tr.tare_column_number_six, " +
				" tr.tare_column_number_seven,tr.tare_column_number_eight FROM sis.advance_execution_safeguards aes, sigma.projects p, sigma.projects_strategic_partners psp, sigma.partners pa " +
				" , sis.table_responses tr,sis.catalogs ca, sigma.partners par WHERE p.proj_id = aes.proj_id AND aes.adex_status = TRUE AND p.proj_status = TRUE AND tr.adex_id = aes.adex_id " +
				" AND (psp.pspa_id = aes.pspa_id OR aes.pspa_id IS NULL) AND pa.part_id =psp.part_id AND par.part_id=p.part_id " +
				" AND aes.adex_is_gender = TRUE AND tr.tare_status = TRUE AND tr.ques_id = " + codigoPregunta;
 
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
					dto.setTextoCuatro(dataObj[7].toString());
				if(dataObj[8]!=null)
					dto.setTextoCinco(dataObj[8].toString());
				if(dataObj[9]!=null)
					dto.setTextoSeis(dataObj[9].toString());
				if(dataObj[10]!=null)
					dto.setNumeroUno(Integer.valueOf(dataObj[10].toString()));
				if(dataObj[11]!=null)
					dto.setNumeroDos(Integer.valueOf(dataObj[11].toString()));
				if(dataObj[12]!=null)
					dto.setNumeroTres(Integer.valueOf(dataObj[12].toString()));
				if(dataObj[13]!=null)
					dto.setNumeroCuatro(Integer.valueOf(dataObj[13].toString()));
				if(dataObj[14]!=null)
					dto.setNumeroCinco(Integer.valueOf(dataObj[14].toString()));
				if(dataObj[15]!=null)
					dto.setNumeroSeis(Integer.valueOf(dataObj[15].toString()));
				if(dataObj[16]!=null)
					dto.setNumeroSiete(Integer.valueOf(dataObj[16].toString()));
				if(dataObj[17]!=null)
					dto.setNumeroOcho(Integer.valueOf(dataObj[17].toString()));					
				lista.add(dto);
			}
		}
		return lista;
	}

}
