/**
@autor proamazonia [Christian Báez]  10 nov. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;

import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.MeetingsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.SitioPublicoBean;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoA;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoB;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoC;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoD;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoE;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoF;
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoG;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import lombok.Getter;

@Named()
@ViewScoped
public class SitioPublicoController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(SitioPublicoController.class);
	private static final String SALVAGUARDA_A="SALVAGUARDA A";
	private static final String SALVAGUARDA_B="SALVAGUARDA B";
	private static final String SALVAGUARDA_C="SALVAGUARDA C";
	private static final String SALVAGUARDA_D="SALVAGUARDA D";
	private static final String SALVAGUARDA_E="SALVAGUARDA E";
	private static final String SALVAGUARDA_F="SALVAGUARDA F";
	private static final String SALVAGUARDA_G="SALVAGUARDA G";
	private static final String DESCRIPCION_A="Programas o proyectos del gobierno nacional dedicados a la conservación de bosques";
	private static final String DESCRIPCION_B="Comunidades vinculadas con manejo forestal sostenible, hombres y mujeres beneficiarios";
	private static final String DESCRIPCION_C="Prácticas y saberes ancestrales ";
	private static final String DESCRIPCION_D="Eventos de fortalecimiento de capacidades para hombres y mujeres";
	private static final String DESCRIPCION_E="Fomento de la gestión comunitaria y apoyo al fortalecimiento de las organizaciones campesinas e indígenas";
	private static final String DESCRIPCION_F="Cantidad de recursos invertidos";
	private static final String DESCRIPCION_G="Acciones para generar alternativas económicas sostenibles a nivel local";
	
	@Inject
	@Getter
	private SitioPublicoBean sitioPublicoBean;
	
	@EJB
	@Getter
	private TableResponsesFacade tableResponsesFacade;
	
	@EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;
	
	@EJB
	@Getter
	private MeetingsFacade meetingsFacade;
	
	@PostConstruct
	public void init(){
		try{
			List<Object[]> listSalvaguardas=new ArrayList<>();
			getSitioPublicoBean().setListaSalvaguardas(new ArrayList<>());
			listSalvaguardas = getSafeguardsFacade().listarSalvaguardas();
			for(Object obj:listSalvaguardas){
				Object[] dataObj = (Object[]) obj;
				Safeguards sf = new Safeguards();
				sf.setSafeDescription(dataObj[1].toString());
				sf.setSafeCode(dataObj[3].toString());				
				sf.setSafeTitle(dataObj[4].toString());
				getSitioPublicoBean().getListaSalvaguardas().add(sf);
			}
//			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//			String archivoJSON = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("archivo").append(".json").toString();			
//			File file = new File(archivoJSON);
//			if(!file.exists())
//				generarResumen();
			getSitioPublicoBean().setPosicionSalvaguardas(1);
			informacionSalvaguardaA();
			Mensaje.actualizarComponente(":frm:pnlSalvaguardas");			
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "init " + ": ").append(e.getMessage()));
		}
	}
	
	/**
	 * Resumen de informacion salvaguarda A
	 */
	public void informacionSalvaguardaA(){
		List<TableResponses> listaTemp= new ArrayList<>();
		try{

//			List<Integer> listaProyectos=new ArrayList<>();
//			BigDecimal totalInversion = new BigDecimal(0);
//			getSitioPublicoBean().setTotalInversionProyectos(new BigDecimal(0));
//			listaTemp = getTableResponsesFacade().listaProyectosValoresSalvaguardaA();
//			Map<Integer,BigDecimal> mapaTemp=new HashMap<Integer,BigDecimal>();
//			for(TableResponses tr: listaTemp){
//				mapaTemp.put(tr.getTareColumnNumberSix(), tr.getTareColumnDecimalOne());
//				totalInversion = totalInversion.add(tr.getTareColumnDecimalOne());
//			}
//			for(Entry<Integer,BigDecimal> proy: mapaTemp.entrySet()){
//				listaProyectos.add(proy.getKey());
//			}
//			getSitioPublicoBean().setTotalInversionProyectos(totalInversion);
//			getSitioPublicoBean().setNumeroProyectosSalvaguardaA(listaProyectos.size());
			leerJson();
			obtieneSalvaguardas(SALVAGUARDA_A, "A");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_A);
			getSitioPublicoBean().setCodigoSalvaguarda("A");
			getSitioPublicoBean().setPosicionSalvaguardas(1);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaA " + ": ").append(e.getMessage()));
		}
	}

	public void informacionSalvaguardaB(){
//		List<TableResponses> listaTempComunidades= new ArrayList<>();		
//		List<String> listaComunidades=new ArrayList<>();
		try{
			
//			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(16);
//			Map<String,Integer> mapaTemp=new HashMap<String,Integer>();
//			for(TableResponses tr: listaTempComunidades){
//				mapaTemp.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
//			}
//			for(Entry<String,Integer> proy: mapaTemp.entrySet()){
//				listaComunidades.add(proy.getKey());
//			}
//			getSitioPublicoBean().setNumeroComunidadesSalvaguardaB(listaComunidades.size());
//			
//			listaTempComunidades = new ArrayList<>();
//			listaTempComunidades = getTableResponsesFacade().listaMaximoHombresMujeresSalvaguardaB();
//			
//			for(TableResponses tr: listaTempComunidades){
//				if(tr.getTareColumnNumberOne() != null)
//					getSitioPublicoBean().setNumeroHombresSalvaguardaB(tr.getTareColumnNumberOne());
//				else
//					getSitioPublicoBean().setNumeroHombresSalvaguardaB(0);
//				if(tr.getTareColumnNumberTwo() != null)
//					getSitioPublicoBean().setNumeroMujeresSalvaguardaB(tr.getTareColumnNumberTwo());
//				else
//					getSitioPublicoBean().setNumeroMujeresSalvaguardaB(0);
//			}
			leerJson();
			obtieneSalvaguardas(SALVAGUARDA_B, "B");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_B);
			getSitioPublicoBean().setCodigoSalvaguarda("B");
			getSitioPublicoBean().setPosicionSalvaguardas(2);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaB " + ": ").append(e.getMessage()));
		}
	}
	
	public void informacionSalvaguardaC(){
//		List<TableResponses> listaTempComunidades= new ArrayList<>();
//		List<String> listaComunidades=new ArrayList<>();
		try{			
//			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(57);
//			Map<String,Integer> mapaTemp=new HashMap<String,Integer>();
//			for(TableResponses tr: listaTempComunidades){
//				mapaTemp.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
//			}
//			for(Entry<String,Integer> proy: mapaTemp.entrySet()){
//				listaComunidades.add(proy.getKey());
//			}
//			getSitioPublicoBean().setNumeroComunidadesSalvaguardaC(listaComunidades.size());			
//			getSitioPublicoBean().setNumeroPracticasAncestralesC(getTableResponsesFacade().listaSaberesAncestralesSalvaguardaC());
			leerJson();
			obtieneSalvaguardas(SALVAGUARDA_C, "C");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_C);
			getSitioPublicoBean().setCodigoSalvaguarda("C");
			getSitioPublicoBean().setPosicionSalvaguardas(3);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaC " + ": ").append(e.getMessage()));
		}
	}
	
	public void informacionSalvaguardaD(){
		
		try{			
//			getSitioPublicoBean().setTotalEventosFortalecimientoHomD(getMeetingsFacade().listaEventosFortalecimientoHombres());
//			getSitioPublicoBean().setTotalEventosFortalecimientoMujD(getMeetingsFacade().listaEventosFortalecimientoMujeres());
			leerJson();
			obtieneSalvaguardas(SALVAGUARDA_D, "D");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_D);
			getSitioPublicoBean().setCodigoSalvaguarda("D");
			getSitioPublicoBean().setPosicionSalvaguardas(4);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaD " + ": ").append(e.getMessage()));
		}
	}
	
	public void informacionSalvaguardaE(){
//		List<TableResponses> listaTempProvincias= new ArrayList<>();
		try{			
//			listaTempProvincias = getTableResponsesFacade().listaFomentoGestionComunitariaE();
//			getSitioPublicoBean().setNumeroFomentoGestionComunitariaE(listaTempProvincias.size());
//			getSitioPublicoBean().setTotalHectareasCoberturaE(getTableResponsesFacade().totalHectareasCoberturaE());
			leerJson();
			obtieneSalvaguardas(SALVAGUARDA_E, "E");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_E);
			getSitioPublicoBean().setCodigoSalvaguarda("E");
			getSitioPublicoBean().setPosicionSalvaguardas(5);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaE " + ": ").append(e.getMessage()));
		}
	}
	
	public void informacionSalvaguardaF(){
		try{			
//			getSitioPublicoBean().setTotalRecursosInvertidos(getTableResponsesFacade().listaRecursosInvertidosF());
			leerJson();
			obtieneSalvaguardas(SALVAGUARDA_F, "F");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_F);
			getSitioPublicoBean().setCodigoSalvaguarda("F");
			getSitioPublicoBean().setPosicionSalvaguardas(6);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaF " + ": ").append(e.getMessage()));
		}
	}
	
	public void informacionSalvaguardaG(){
//		List<TableResponses> listaTempComunidades= new ArrayList<>();
//		List<String> listaComunidades=new ArrayList<>();
		try{			
//			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(136);
//			Map<String,Integer> mapaTemp=new HashMap<String,Integer>();
//			for(TableResponses tr: listaTempComunidades){
//				mapaTemp.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
//			}
//			for(Entry<String,Integer> proy: mapaTemp.entrySet()){
//				listaComunidades.add(proy.getKey());
//			}
//			getSitioPublicoBean().setNumeroComunidadesSalvaguardaG(listaComunidades.size());
//			getSitioPublicoBean().setTotalBeneficiariosG(getTableResponsesFacade().listaBeneficiariosSalvaguardaG());
//			getSitioPublicoBean().setNumeroAccionesGeneradasG(getTableResponsesFacade().listaAccionesGeneradasSalvaguardaG());
			leerJson();			
			obtieneSalvaguardas(SALVAGUARDA_G, "G");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_G);
			getSitioPublicoBean().setCodigoSalvaguarda("G");
			getSitioPublicoBean().setPosicionSalvaguardas(7);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaG " + ": ").append(e.getMessage()));
		}
	}
	
	public void obtieneSalvaguardas(String salvaguarda, String codigo){
		for(Safeguards sf:getSitioPublicoBean().getListaSalvaguardas()){
			if(sf.getSafeCode().equals(codigo)){
				getSitioPublicoBean().setDescripcionSalvaguarda(sf.getSafeDescription());
				getSitioPublicoBean().setSalvaguarda(salvaguarda);
				getSitioPublicoBean().setTituloSalvaguarda(sf.getSafeTitle());
			}
		}
	}
	public void avanzaSalvaguarda(){
		if(getSitioPublicoBean().getPosicionSalvaguardas() == 7){
			getSitioPublicoBean().setPosicionSalvaguardas(7);
			informacionSalvaguardaG();
		}else{
			getSitioPublicoBean().setPosicionSalvaguardas(getSitioPublicoBean().getPosicionSalvaguardas()+1);
			switch(getSitioPublicoBean().getPosicionSalvaguardas()){
				case 2:
					informacionSalvaguardaB();
					break;
				case 3:
					informacionSalvaguardaC();
					break;
				case 4:
					informacionSalvaguardaD();
					break;
				case 5:
					informacionSalvaguardaE();
					break;
				case 6:
					informacionSalvaguardaF();
					break;
				case 7:
					informacionSalvaguardaG();
					break;	
			}
		}
	}
	public void retrocedeSalvaguarda(){
		if(getSitioPublicoBean().getPosicionSalvaguardas() == 1){
			getSitioPublicoBean().setPosicionSalvaguardas(1);
			informacionSalvaguardaA();
		}else{
			getSitioPublicoBean().setPosicionSalvaguardas(getSitioPublicoBean().getPosicionSalvaguardas()-1);
			switch(getSitioPublicoBean().getPosicionSalvaguardas()){
				case 1:
					informacionSalvaguardaA();
					break;
				case 2:
					informacionSalvaguardaB();
					break;
				case 3:
					informacionSalvaguardaC();
					break;
				case 4:
					informacionSalvaguardaD();
					break;
				case 5:
					informacionSalvaguardaE();
					break;
				case 6:
					informacionSalvaguardaF();
					break;
				case 7:
					informacionSalvaguardaG();
					break;	
			}
		}
	}

	
	
//	public void generarResumen(){
//		List<TableResponses> listaTemp= new ArrayList<>();
//		List<TableResponses> listaTempComunidades= new ArrayList<>();		
//		List<String> listaComunidades=new ArrayList<>();
//		List<TableResponses> listaTempProvincias= new ArrayList<>();
//		try{
//			List<Integer> listaProyectos=new ArrayList<>();
//			BigDecimal totalInversion = new BigDecimal(0);
//			getSitioPublicoBean().setTotalInversionProyectos(new BigDecimal(0));
//			listaTemp = getTableResponsesFacade().listaProyectosValoresSalvaguardaA();
//			Map<Integer,BigDecimal> mapaTemp=new HashMap<Integer,BigDecimal>();
//			for(TableResponses tr: listaTemp){
//				mapaTemp.put(tr.getTareColumnNumberSix(), tr.getTareColumnDecimalOne());
//				totalInversion = totalInversion.add(tr.getTareColumnDecimalOne());
//			}
//			for(Entry<Integer,BigDecimal> proy: mapaTemp.entrySet()){
//				listaProyectos.add(proy.getKey());
//			}			
//			DtoDatosSitioPublicoA dtoSalvaguardaA = new DtoDatosSitioPublicoA("A");
//			dtoSalvaguardaA.setNumeroProyectos(listaProyectos.size());
//			dtoSalvaguardaA.setTotalInversionProyectos(totalInversion);
//			////B
//			DtoDatosSitioPublicoB dtoSalvaguardaB = new DtoDatosSitioPublicoB("B");
//			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(16);
//			Map<String,Integer> mapaTempB=new HashMap<String,Integer>();
//			for(TableResponses tr: listaTempComunidades){
//				mapaTempB.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
//			}
//			for(Entry<String,Integer> proy: mapaTempB.entrySet()){
//				listaComunidades.add(proy.getKey());
//			}
//			dtoSalvaguardaB.setNumeroComunidadesB(listaComunidades.size());
//			
//			listaTempComunidades = new ArrayList<>();
//			listaTempComunidades = getTableResponsesFacade().listaMaximoHombresMujeresSalvaguardaB();
//			
//			for(TableResponses tr: listaTempComunidades){
//				if(tr.getTareColumnNumberOne() != null)
//					dtoSalvaguardaB.setNumeroHombresB(tr.getTareColumnNumberOne());
//				else
//					dtoSalvaguardaB.setNumeroHombresB(0);
//				if(tr.getTareColumnNumberTwo() != null)	
//					dtoSalvaguardaB.setNumeroMujeresB(tr.getTareColumnNumberTwo());
//				else
//					dtoSalvaguardaB.setNumeroMujeresB(0);
//			}
//			
//			///// C
//			DtoDatosSitioPublicoC dtoSalvaguardaC = new DtoDatosSitioPublicoC("C");
//			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(57);
//			Map<String,Integer> mapaTempC=new HashMap<String,Integer>();
//			for(TableResponses tr: listaTempComunidades){
//				mapaTempC.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
//			}
//			for(Entry<String,Integer> proy: mapaTempC.entrySet()){
//				listaComunidades.add(proy.getKey());
//			}			
//			dtoSalvaguardaC.setNumeroComunidadesC(listaComunidades.size());
//			dtoSalvaguardaC.setNumeroPracticas(getTableResponsesFacade().listaSaberesAncestralesSalvaguardaC());
//			
//			///// D
//			DtoDatosSitioPublicoD dtoSalvaguardaD = new DtoDatosSitioPublicoD("D");
//			
//			dtoSalvaguardaD.setTotalEventosHombres(getMeetingsFacade().listaEventosFortalecimientoHombres());
//			dtoSalvaguardaD.setTotalEventosMujeres(getMeetingsFacade().listaEventosFortalecimientoMujeres());
//			
//			// E
//			DtoDatosSitioPublicoE dtoSalvaguardaE = new DtoDatosSitioPublicoE("E");
//			listaTempProvincias = getTableResponsesFacade().listaFomentoGestionComunitariaE();
//			
//			dtoSalvaguardaE.setNumeroFomentoGestionComunitaria(listaTempProvincias.size());
//			dtoSalvaguardaE.setTotalHectareasCobertura(getTableResponsesFacade().totalHectareasCoberturaE());
//			
//			// F
//			DtoDatosSitioPublicoF dtoSalvaguardaF = new DtoDatosSitioPublicoF("F");			
//			dtoSalvaguardaF.setTotalRecursosInvertidos(getTableResponsesFacade().listaRecursosInvertidosF());
//			
//			// G
//			DtoDatosSitioPublicoG dtoSalvaguardaG = new DtoDatosSitioPublicoG("G");
//			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(136);
//			Map<String,Integer> mapaTempG=new HashMap<String,Integer>();
//			for(TableResponses tr: listaTempComunidades){
//				mapaTempG.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
//			}
//			for(Entry<String,Integer> proy: mapaTempG.entrySet()){
//				listaComunidades.add(proy.getKey());
//			}
//						
//			dtoSalvaguardaG.setNumeroAcciones(getTableResponsesFacade().listaAccionesGeneradasSalvaguardaG());
//			dtoSalvaguardaG.setNumeroComunidades(listaComunidades.size());
//			dtoSalvaguardaG.setTotalBeneficiarios(getTableResponsesFacade().listaBeneficiariosSalvaguardaG());
//			
//			
////			String json = Jsoner.serialize(dtoSalvaguardaA);
////			System.out.println(json);
////			json = Jsoner.prettyPrint(json);
////			System.out.println(json +"DATOSSSS");
//						
//			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//			String archivoJSON = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("archivo").append(".json").toString();
//	
//			List<Object> lista = Arrays.asList(dtoSalvaguardaA, dtoSalvaguardaB,dtoSalvaguardaC,dtoSalvaguardaD, dtoSalvaguardaE , dtoSalvaguardaF , dtoSalvaguardaG);
//			try (FileWriter fileWriter = new FileWriter(archivoJSON)) {
//	            Jsoner.serialize(lista, fileWriter);
//	        }
//			
//		}catch(Exception e){
//			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "generarResumen " + ": ").append(e.getMessage()));
//		}
//	}
	public void leerJson(){
		
		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String archivoJSON = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("archivo").append(".json").toString();
			
//			File file = new File(archivoJSON);
//			if(!file.exists())
//				generarResumen();
			
			try (FileReader fileReader = new FileReader((archivoJSON))) {

	            JsonArray objects = Jsoner.deserializeMany(fileReader);
	            
	            JsonArray o = (JsonArray) objects.get(0);
	            
	            for(int i=0; i<o.size();i++){	            	
	            	JsonObject obj = (JsonObject) o.get(i);
	            	switch(i){
	            		case 0:
	            			getSitioPublicoBean().setNumeroProyectosSalvaguardaA(Integer.valueOf(obj.get("numeroProyectosA").toString()));
	            			getSitioPublicoBean().setTotalInversionProyectos(new BigDecimal(obj.get("totalInversionProyectosA").toString()));
	            			getSitioPublicoBean().setListadoProyectosA(new ArrayList<>());
	            			JsonArray vector =(JsonArray) obj.get("proyectos");
	            			if (vector != null) { 
	            				for (int n=0;n<vector.size();n++){ 	            				    
	            					getSitioPublicoBean().getListadoProyectosA().add(vector.getString(n));
	            				} 
	            			}	            			
	            			break;
	            		case 1:
	            			getSitioPublicoBean().setNumeroComunidadesSalvaguardaB(Integer.valueOf(obj.get("numeroComunidadesB").toString()));
	            			getSitioPublicoBean().setNumeroHombresSalvaguardaB(Integer.valueOf(obj.get("numeroHombresB").toString()));
	            			getSitioPublicoBean().setNumeroMujeresSalvaguardaB(Integer.valueOf(obj.get("numeroMujeresB").toString()));
	            			break;
	            		case 2:
	            			getSitioPublicoBean().setNumeroComunidadesSalvaguardaC(Integer.valueOf(obj.get("numeroComunidadesC").toString()));			
	            			getSitioPublicoBean().setNumeroPracticasAncestralesC(Integer.valueOf(obj.get("numeroPracticasC").toString()));	
	            			break;
	            		case 3:
	            			getSitioPublicoBean().setTotalEventosFortalecimientoHomD(Integer.valueOf(obj.get("totalEventosHombresD").toString()));
	            			getSitioPublicoBean().setTotalEventosFortalecimientoMujD(Integer.valueOf(obj.get("totalEventosMujeresD").toString()));
	            			break;
	            		case 4:
	            			getSitioPublicoBean().setNumeroFomentoGestionComunitariaE(Integer.valueOf(obj.get("numeroFomentoGestionComunitariaE").toString()));
	            			getSitioPublicoBean().setTotalHectareasCoberturaE(new BigDecimal(obj.get("totalHectareasCoberturaE").toString()));
	            			break;
	            		case 5:
	            			getSitioPublicoBean().setTotalRecursosInvertidos(new BigDecimal(obj.get("totalRecursosInvertidosF").toString()));
	            			break;
	            		case 6:
	            			getSitioPublicoBean().setNumeroComunidadesSalvaguardaG(Integer.valueOf(obj.get("numeroComunidadesG").toString()));
	            			getSitioPublicoBean().setTotalBeneficiariosG(Integer.valueOf(obj.get("totalBeneficiariosG").toString()));
	            			getSitioPublicoBean().setNumeroAccionesGeneradasG(Integer.valueOf(obj.get("numeroAccionesG").toString()));
	            			break;
	            	}
	            	
	            }
			}
		}catch(FileNotFoundException e){
			System.out.println("Archivo JSON no encontrado");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void generarResumen(){

		List<TableResponses> listaTemp= new ArrayList<>();
		List<TableResponses> listaTempComunidades= new ArrayList<>();		
		List<String> listaComunidades=new ArrayList<>();
		List<TableResponses> listaTempProvincias= new ArrayList<>();
		try{
			List<Integer> listaProyectos=new ArrayList<>();
			BigDecimal totalInversion = new BigDecimal(0);
			//			getSitioPublicoBean().setTotalInversionProyectos(new BigDecimal(0));
			listaTemp = getTableResponsesFacade().listaProyectosValoresSalvaguardaA();
			Map<Integer,BigDecimal> mapaTemp=new HashMap<Integer,BigDecimal>();
			for(TableResponses tr: listaTemp){
				mapaTemp.put(tr.getTareColumnNumberSix(), tr.getTareColumnDecimalOne());
				totalInversion = totalInversion.add(tr.getTareColumnDecimalOne());
			}
			for(Entry<Integer,BigDecimal> proy: mapaTemp.entrySet()){
				listaProyectos.add(proy.getKey());
			}			
			DtoDatosSitioPublicoA dtoSalvaguardaA = new DtoDatosSitioPublicoA("A");
			dtoSalvaguardaA.setNumeroProyectos(listaProyectos.size());
			dtoSalvaguardaA.setTotalInversionProyectos(totalInversion);
			
			////B
			DtoDatosSitioPublicoB dtoSalvaguardaB = new DtoDatosSitioPublicoB("B");
			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(16);
			Map<String,Integer> mapaTempB=new HashMap<String,Integer>();
			for(TableResponses tr: listaTempComunidades){
				mapaTempB.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
			}
			for(Entry<String,Integer> proy: mapaTempB.entrySet()){
				listaComunidades.add(proy.getKey());
			}
			dtoSalvaguardaB.setNumeroComunidadesB(listaComunidades.size());

			listaTempComunidades = new ArrayList<>();
			listaTempComunidades = getTableResponsesFacade().listaMaximoHombresMujeresSalvaguardaB();

			for(TableResponses tr: listaTempComunidades){
				if(tr.getTareColumnNumberOne() != null)
					dtoSalvaguardaB.setNumeroHombresB(tr.getTareColumnNumberOne());
				else
					dtoSalvaguardaB.setNumeroHombresB(0);
				if(tr.getTareColumnNumberTwo() != null)	
					dtoSalvaguardaB.setNumeroMujeresB(tr.getTareColumnNumberTwo());
				else
					dtoSalvaguardaB.setNumeroMujeresB(0);
			}

			///// C
			DtoDatosSitioPublicoC dtoSalvaguardaC = new DtoDatosSitioPublicoC("C");
			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(57);
			Map<String,Integer> mapaTempC=new HashMap<String,Integer>();
			for(TableResponses tr: listaTempComunidades){
				mapaTempC.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
			}
			for(Entry<String,Integer> proy: mapaTempC.entrySet()){
				listaComunidades.add(proy.getKey());
			}			
			dtoSalvaguardaC.setNumeroComunidadesC(listaComunidades.size());
			dtoSalvaguardaC.setNumeroPracticas(getTableResponsesFacade().listaSaberesAncestralesSalvaguardaC());

			///// D
			DtoDatosSitioPublicoD dtoSalvaguardaD = new DtoDatosSitioPublicoD("D");

			dtoSalvaguardaD.setTotalEventosHombres(getMeetingsFacade().listaEventosFortalecimientoHombres());
			dtoSalvaguardaD.setTotalEventosMujeres(getMeetingsFacade().listaEventosFortalecimientoMujeres());

			// E
			DtoDatosSitioPublicoE dtoSalvaguardaE = new DtoDatosSitioPublicoE("E");
			listaTempProvincias = getTableResponsesFacade().listaFomentoGestionComunitariaE();

			dtoSalvaguardaE.setNumeroFomentoGestionComunitaria(listaTempProvincias.size());
			dtoSalvaguardaE.setTotalHectareasCobertura(getTableResponsesFacade().totalHectareasCoberturaE());

			// F
			DtoDatosSitioPublicoF dtoSalvaguardaF = new DtoDatosSitioPublicoF("F");			
			dtoSalvaguardaF.setTotalRecursosInvertidos(getTableResponsesFacade().listaRecursosInvertidosF());

			// G
			DtoDatosSitioPublicoG dtoSalvaguardaG = new DtoDatosSitioPublicoG("G");
			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(136);
			Map<String,Integer> mapaTempG=new HashMap<String,Integer>();
			for(TableResponses tr: listaTempComunidades){
				mapaTempG.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
			}
			for(Entry<String,Integer> proy: mapaTempG.entrySet()){
				listaComunidades.add(proy.getKey());
			}

			dtoSalvaguardaG.setNumeroAcciones(getTableResponsesFacade().listaAccionesGeneradasSalvaguardaG());
			dtoSalvaguardaG.setNumeroComunidades(listaComunidades.size());
			dtoSalvaguardaG.setTotalBeneficiarios(getTableResponsesFacade().listaBeneficiariosSalvaguardaG());


			//			String json = Jsoner.serialize(dtoSalvaguardaA);
			//			json = Jsoner.prettyPrint(json);
			//			System.out.println(json);
//			String ruta = this.getClass().getClassLoader().toString();
//			System.out.println(this.webPath);			
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//
			String archivoJSON = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("archivo").append(".json").toString();
//			String archivoJSON = new StringBuilder().append(this.webPath).append(File.separator).append("archivo").append(".json").toString();
			List<Object> lista = Arrays.asList(dtoSalvaguardaA,dtoSalvaguardaB,dtoSalvaguardaC,dtoSalvaguardaD, dtoSalvaguardaE , dtoSalvaguardaF , dtoSalvaguardaG);
									
			try (FileWriter fileWriter = new FileWriter(archivoJSON)) {
				
				Jsoner.serialize(lista, fileWriter);
//				System.out.println(Jsoner.serialize(lista));
			}			
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "generarResumen " + ": ").append(e.getMessage()));
		}
	}

	public void saludar(){
		System.out.println("Saludar" + getSitioPublicoBean().getListadoProyectosA().size());
		
		Mensaje.verDialogo("dlgA1");
		
	}
	public void salir(){
		System.out.println("Salir");
		Mensaje.ocultarDialogo("dlgA1");
	}
	
	
}

