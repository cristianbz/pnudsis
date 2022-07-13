package ec.gob.ambiente.sis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.dto.DtoPreguntasPartners;
import ec.gob.ambiente.sis.dto.DtoRespuestasSalvaguardas;
import ec.gob.ambiente.sis.model.Questions;

@Stateless
@LocalBean
public class QuestionsFacade extends AbstractFacade<Questions, Integer>  {


	public QuestionsFacade() {
		super(Questions.class, Integer.class);
	}
	/**
	 * Preguntas de salvaguardas ingresadas
	 * @return
	 * @throws Exception
	 */
	public List<Questions> listaPreguntasIngresadas() throws Exception{
		String sql="SELECT Q FROM Questions Q ORDER BY Q.safeguards.safeCode,Q.quesId";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<Questions> listaPreguntasGeneroIngresadas() throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.quesIsGender=TRUE ORDER BY Q.safeguards.safeCode,Q.quesId";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Lista de todas las preguntas ingresadas
	 */
	
	public List<Questions> buscarTodasLasPreguntas() throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.quesStatus=True AND Q.quesIsGender = FALSE ORDER BY Q.quesId";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Consulta pregunta por codigo salvaguarda
	 * @return
	 */
	public List<Questions> buscaPreguntaPorSalvaguarda(int codigoSalvaguarda) throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.safeguards.safeId=:codigoSalvaguarda AND Q.quesIsGender = False AND Q.quesStatus=True order by Q.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoSalvaguarda", codigoSalvaguarda);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Consulta preguntas por codigos de salvaguardas
	 * @param codigosSalvaguardas
	 * @return
	 * @throws Exception
	 */
	public List<Questions> buscarPreguntasPorSalvaguardas(List<Integer> codigosSalvaguardas) throws Exception{		
		String sql="SELECT Q FROM Questions Q WHERE Q.safeguards.safeId IN (:codigoSalvaguarda) AND Q.quesIsGender = False AND Q.quesStatus=True ORDER BY Q.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoSalvaguarda", codigosSalvaguardas);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Busca las preguntas de genero
	 * @return
	 * @throws Exception
	 */
	public List<Questions> buscaPreguntasGenero()throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.quesStatus=True AND Q.quesIsGender = TRUE ORDER BY Q.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();		
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<Questions> buscaTodasPreguntasGenero()throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.quesIsGender = TRUE ORDER BY Q.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();		
		return findByCreateQuery(sql, camposCondicion);
	}
	
	/**
	 * Crea o edita una pregunta
	 * @param pregunta
	 * @return
	 * @throws Exception
	 */
	public Questions crearEditarPregunta(Questions pregunta) throws Exception{
		if(pregunta.getQuesId() == null)
			create(pregunta);
		else
			edit(pregunta);
		return pregunta;
	}
	/**
	 * Obtiene el numero de orden mas alto de las preguntas de una salvaguarda
	 * @param codigoSalvaguarda Codigo de la salvaguarda a buscar
	 * @return
	 * @throws Exception
	 */
	public int campoOrdenPregunta(int codigoSalvaguarda) throws Exception{
		int orden=0;
		String sql="SELECT MAX(ques_question_order) FROM sis.questions WHERE safe_id=" + codigoSalvaguarda;		
		List<Object[]>  resultList = (List<Object[]>) consultaNativa(sql);
		for (Object object : resultList) {			
			int dato = (Integer) object;
			orden=dato;			
		}
		return orden;
	}
	/**
	 * Obtiene el numero de orden mas alto de las preguntas de genero 
	 * @return
	 * @throws Exception
	 */
	public int campoOrdenPreguntaGenero() throws Exception{
		int orden=0;
		String sql="SELECT MAX(ques_question_order) FROM sis.questions WHERE safe_id IS NULL";		
		List<Object[]>  resultList = (List<Object[]>) consultaNativa(sql);
		for (Object object : resultList) {			
			int dato = (Integer) object;
			orden=dato;			
		}
		return orden;
	}
	/**
	 * Busca las preguntas activas
	 * @return
	 * @throws Exception
	 */
	public List<Questions> listaPreguntasActivas() throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.quesStatus= TRUE ORDER BY Q.safeguards.safeCode,Q.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Busca las preguntas por el codigo de la salvaguarda
	 * @param codigoSalvaguarda
	 * @return
	 * @throws Exception
	 */
	public List<Questions> buscaPreguntaPorCodigoSalvaguarda(String codigoSalvaguarda) throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.safeguards.safeCode=:codigoSalvaguarda AND Q.quesIsGender = False AND Q.quesStatus=True ORDER BY Q.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoSalvaguarda", codigoSalvaguarda);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Devuelve las preguntas principales
	 * 
	 */
	public List<Questions> buscaPreguntaPrincipalPorCodigoSalvaguarda(String codigoSalvaguarda) throws Exception{
		String sql="SELECT Q FROM Questions Q WHERE Q.safeguards.safeCode=:codigoSalvaguarda AND Q.quesIsGender = False AND Q.quesStatus=True AND Q.quesPrincipalQuestion = TRUE  ORDER BY Q.quesQuestionOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoSalvaguarda", codigoSalvaguarda);
		return findByCreateQuery(sql, camposCondicion);
	}
	/***
	 * Obtiene las preguntas registradas por los partners
	 * @return
	 * @throws Exception
	 */
	public List<DtoPreguntasPartners> preguntasPartners(String salvaguarda, String periodo) throws Exception{
		List<DtoPreguntasPartners> lista = new ArrayList<>();
		List<Object[]> resultado= null;
		String sql ="SELECT * FROM sis.view_questions_partners WHERE safe_code='" + salvaguarda + "' AND adex_term_from = '" + periodo +"' ORDER BY ques_id,proj_title;";		
		resultado = (List<Object[]>)consultaNativa(sql);
		if(resultado.size()>0){
			for(Object obj:resultado){
				Object[] dataObj = (Object[]) obj;
				DtoPreguntasPartners dto = new DtoPreguntasPartners();				
				if(dataObj[0]!=null)
					dto.setId(Integer.valueOf(dataObj[0].toString()));					
				if(dataObj[1]!=null)
					dto.setPregunta(dataObj[1].toString());
				if(dataObj[2]!=null)
					dto.setPartner(dataObj[2].toString());
				if(dataObj[3]!=null)
					dto.setSalvaguarda(dataObj[3].toString());	
				if(dataObj[4]!=null)
					dto.setSocioEstrategico("SI");
				else
					dto.setSocioEstrategico("NO");
				if(dataObj[5]!=null)
					dto.setNombreProyecto(dataObj[5].toString());
				if(dataObj[6]!=null)
					dto.setNombreCorto(dataObj[6].toString());
				else
					dto.setNombreCorto("");
//				if(dataObj[7]!=null)
//					dto.setPeriodo(dataObj[7].toString());
				
				lista.add(dto);
			}
		}
		return lista;
	}
}
