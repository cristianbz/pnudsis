/**
@autor proamazonia [Christian Báez]  10 nov. 2021

 **/
package ec.gob.ambiente.sis.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

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
import ec.gob.ambiente.sis.dto.DtoDatosSitioPublicoGenero;
import ec.gob.ambiente.sis.dto.DtoGenero;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.services.AdvanceExecutionProjectGenderFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import lombok.Getter;
import lombok.Setter;

@Named()
@ViewScoped
public class SitioPublicoController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(SitioPublicoController.class);

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

	@EJB
	@Getter
	private AdvanceExecutionProjectGenderFacade avanceExecutionFacade;

	@Getter
	@Setter
	@Inject
	private MensajesController mensajesController;

	private ResourceBundle rb;

	@Getter
	@Setter
	private PieChartModel pieModel;

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
			rb = ResourceBundle.getBundle("resources.salvaguardas");
			getSitioPublicoBean().setPosicionSalvaguardas(1);
			//			informacionSalvaguardaA();			
			Mensaje.actualizarComponente(":frm:pnlSalvaguardas");
			pieModel = new PieChartModel();
			iniciaColores();
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "init " + ": ").append(e.getMessage()));
		}
	}

	/**
	 * Resumen de informacion salvaguarda A
	 */
	public void informacionSalvaguardaA(){
		try{

			leerJson();
			obtieneSalvaguardas(rb.getString("SALVAGUARDA_A"), "A");
			getSitioPublicoBean().setResumenSalvaguarda(rb.getString("DESCRIPCION_A"));
			getSitioPublicoBean().setCodigoSalvaguarda("A");
			getSitioPublicoBean().setPosicionSalvaguardas(1);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaA " + ": ").append(e.getMessage()));
		}
	}

	public void informacionSalvaguardaB(){
		try{
			leerJson();
			obtieneSalvaguardas(rb.getString("SALVAGUARDA_B"), "B");
			getSitioPublicoBean().setResumenSalvaguarda(rb.getString("DESCRIPCION_B"));
			getSitioPublicoBean().setCodigoSalvaguarda("B");
			getSitioPublicoBean().setPosicionSalvaguardas(2);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaB " + ": ").append(e.getMessage()));
		}
	}

	public void informacionSalvaguardaC(){
		try{			
			leerJson();
			obtieneSalvaguardas(rb.getString("SALVAGUARDA_C"), "C");
			getSitioPublicoBean().setResumenSalvaguarda(rb.getString("DESCRIPCION_C"));
			getSitioPublicoBean().setCodigoSalvaguarda("C");
			getSitioPublicoBean().setPosicionSalvaguardas(3);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaC " + ": ").append(e.getMessage()));
		}
	}

	public void informacionSalvaguardaD(){

		try{			
			leerJson();
			obtieneSalvaguardas(rb.getString("SALVAGUARDA_D"), "D");
			getSitioPublicoBean().setResumenSalvaguarda(rb.getString("DESCRIPCION_D"));
			getSitioPublicoBean().setCodigoSalvaguarda("D");
			getSitioPublicoBean().setPosicionSalvaguardas(4);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaD " + ": ").append(e.getMessage()));
		}
	}

	public void informacionSalvaguardaE(){
		try{			
			leerJson();
			obtieneSalvaguardas(rb.getString("SALVAGUARDA_E"), "E");
			getSitioPublicoBean().setResumenSalvaguarda(rb.getString("DESCRIPCION_E"));
			getSitioPublicoBean().setCodigoSalvaguarda("E");
			getSitioPublicoBean().setPosicionSalvaguardas(5);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaE " + ": ").append(e.getMessage()));
		}
	}

	public void informacionSalvaguardaF(){
		try{			
			leerJson();
			obtieneSalvaguardas(rb.getString("SALVAGUARDA_F"), "F");
			getSitioPublicoBean().setResumenSalvaguarda(rb.getString("DESCRIPCION_F"));
			getSitioPublicoBean().setCodigoSalvaguarda("F");
			getSitioPublicoBean().setPosicionSalvaguardas(6);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaF " + ": ").append(e.getMessage()));
		}
	}

	public void informacionSalvaguardaG(){
		try{			
			leerJson();			
			obtieneSalvaguardas(rb.getString("SALVAGUARDA_G"), "G");
			getSitioPublicoBean().setResumenSalvaguarda(rb.getString("DESCRIPCION_G"));
			getSitioPublicoBean().setCodigoSalvaguarda("G");
			getSitioPublicoBean().setPosicionSalvaguardas(7);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "informacionSalvaguardaG " + ": ").append(e.getMessage()));
		}
	}
	public void informacionGenero(){
		try{			
			leerJson();			
			getSitioPublicoBean().setResumenSalvaguarda(rb.getString("DESCRIPCION_GENERO"));
			getSitioPublicoBean().setCodigoSalvaguarda("GE");
			getSitioPublicoBean().setPosicionSalvaguardas(8);
			getSitioPublicoBean().setDescripcionSalvaguarda(rb.getString("RESUMEN_GENERO"));
			getSitioPublicoBean().setSalvaguarda(rb.getString("GENERO"));
			getSitioPublicoBean().setTituloSalvaguarda(rb.getString("TITULO_GENERO"));
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
		if(getSitioPublicoBean().getPosicionSalvaguardas() == 8){
			getSitioPublicoBean().setPosicionSalvaguardas(8);
			informacionGenero();
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
			case 8:
				informacionGenero();
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
			case 8:
				informacionGenero();
				break;	
			}
		}
	}



	public void leerJson(){

		try{
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String archivoJSON = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("archivo").append(".json").toString();
			try (FileReader fileReader = new FileReader((archivoJSON))) {
				JsonArray objects = Jsoner.deserializeMany(fileReader);
				JsonArray o = (JsonArray) objects.get(0);
				for(int i=0; i<o.size();i++){	 
					JsonObject obj = (JsonObject) Jsoner.deserialize(o.get(i).toString());
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
						getSitioPublicoBean().setTotalAccionesReversionF(Integer.valueOf(obj.get("totalAccionesReversionF").toString()));
						getSitioPublicoBean().setListadoMedidasTomadasF(new ArrayList<>());
						JsonArray vectorF =(JsonArray) obj.get("medidasTomadas");
						if (vectorF != null) { 
							for (int n=0;n<vectorF.size();n++){ 	            				    
								getSitioPublicoBean().getListadoMedidasTomadasF().add(vectorF.getString(n));
							} 
						}
						break;
					case 6:
						getSitioPublicoBean().setNumeroComunidadesSalvaguardaG(Integer.valueOf(obj.get("numeroComunidadesG").toString()));
						getSitioPublicoBean().setNumeroAccionesGeneradasG(Integer.valueOf(obj.get("numeroAccionesG").toString()));
						break;
					case 7:
						getSitioPublicoBean().setNumeroTemasGenero(Integer.valueOf(obj.get("totalTemasAplicados").toString()));
						getSitioPublicoBean().setNumeroAccionesGenero(Integer.valueOf(obj.get("totalAccionesImplementadas").toString()));
						getSitioPublicoBean().setTotalPresupuestoGenero(new BigDecimal(obj.get("totalPresupuesto").toString()));
						getSitioPublicoBean().setListadoAccionesGenero(new ArrayList<>());
						getSitioPublicoBean().setListaTemasGenero(new ArrayList<>());
						JsonArray vectorGEN =(JsonArray) obj.get("accionesImplementadas");
						if (vectorGEN != null) { 
							for (int n=0;n<vectorGEN.size();n++){ 	            				    
								getSitioPublicoBean().getListadoAccionesGenero().add(vectorGEN.getString(n));
							} 
						}
						JsonArray vectorTemas =(JsonArray) obj.get("listaTemasGenero");
						if (vectorTemas != null) { 
							for (int n=0;n<vectorTemas.size();n++){
								DtoGenero objGenero = new DtoGenero();
								JsonObject objeto= (JsonObject) vectorTemas.get(n);
								objGenero.setTema(objeto.get("tema").toString());
								objGenero.setNumero(Integer.valueOf(objeto.get("numero").toString()));
								getSitioPublicoBean().getListaTemasGenero().add(objGenero);
							} 
						}
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
	/**
	 * Permite generar el resumen de salvaguardas para el sitio publico
	 */
	public void generarResumen(){

		List<TableResponses> listaTemp= new ArrayList<>();
		List<TableResponses> listaTempComunidades= new ArrayList<>();		
		List<String> listaComunidades=new ArrayList<>();
		List<TableResponses> listaTempProvincias= new ArrayList<>();
		try{
			List<Integer> listaProyectos=new ArrayList<>();
			BigDecimal totalInversion = new BigDecimal(0);
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
			dtoSalvaguardaA.setListadoProyectos(getTableResponsesFacade().listadoProyectos());
			////B
			DtoDatosSitioPublicoB dtoSalvaguardaB = new DtoDatosSitioPublicoB("B");
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
			dtoSalvaguardaF.setTotalAccionesReversion(getTableResponsesFacade().numeroAccionesEvitarRiesgos_F());
			dtoSalvaguardaF.setListadoMedidasTomadas(getTableResponsesFacade().listaMedidasTomadas_F());

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

			DtoDatosSitioPublicoGenero dtoGenero = new DtoDatosSitioPublicoGenero("GENERO");
			List<DtoGenero> listTempTemas = getAvanceExecutionFacade().listaTemasGenero();
			List<String> listTempAcciones = getAvanceExecutionFacade().listadoAccionesGenero();
			dtoGenero.setTotalPresupuesto(getAvanceExecutionFacade().presupuestoGenero());
			int totalTemas = 0;			
			if(listTempTemas!=null && listTempTemas.size()>0){
				dtoGenero.setListaTemasGenero(listTempTemas);
				for (DtoGenero genero : listTempTemas) {
					totalTemas = totalTemas + genero.getNumero();
				}
				dtoGenero.setTotalTemasAplicados(totalTemas);
			}else{
				dtoGenero.setTotalTemasAplicados(0);
				dtoGenero.setListaTemasGenero(null);
			}

			if(listTempAcciones!=null && listTempAcciones.size()>0){
				dtoGenero.setListaAccionesGenero(listTempAcciones);
				dtoGenero.setTotalAccionesImplementadas(listTempAcciones.size());
			}else{
				dtoGenero.setListaAccionesGenero(null);
				dtoGenero.setTotalAccionesImplementadas(0);
			}

			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String archivoJSON = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("archivo").append(".json").toString();
			List<Object> lista = Arrays.asList(dtoSalvaguardaA.toJson(), dtoSalvaguardaB.toJson(),dtoSalvaguardaC.toJson(),dtoSalvaguardaD.toJson(), dtoSalvaguardaE.toJson() , dtoSalvaguardaF.toJson() , dtoSalvaguardaG.toJson(),dtoGenero.toJson());
			try (FileWriter fileWriter = new FileWriter(archivoJSON)) {
				Jsoner.serialize(lista, fileWriter);
			}

		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "generarResumen " + ": ").append(e.getMessage()));
		}
	}

	public void dialogoA1(){
		Mensaje.verDialogo("dlgA1");		
	}
	public void dialogoF1(){
		Mensaje.verDialogo("dlgF1");		
	}

	public void dialogoAccionesGenero(){
		Mensaje.verDialogo("dlgAccionesGenero");		
	}
	public void dialogoTemasGenero(){
		int color=0;
		pieModel = new PieChartModel();
		ChartData data = new ChartData();
		PieChartDataSet dataSet = new PieChartDataSet();
		dataSet.setData(new ArrayList<>());

		List<String> bgColors = new ArrayList<>();
		bgColors.add("rgb(255,0,0)");
		bgColors.add("rgb(0,255,0)");
		bgColors.add("rgb(0,0,255)");
		bgColors.add("rgb(255,255,0)");
		bgColors.add("rgb(0,255,255)");
		bgColors.add("rgb(255,0,255)");
		bgColors.add("rgb(192,192,192)");
		bgColors.add("rgb(128,128,128)");
		bgColors.add("rgb(128,0,0)");
		for (DtoGenero g : getSitioPublicoBean().getListaTemasGenero()) {
			dataSet.getData().add(g.getNumero());
			data.setLabels(g.getTema());
		}
		dataSet.setBackgroundColor(bgColors);
		data.addChartDataSet(dataSet);
		pieModel.setData(data);
		Mensaje.verDialogo("dlgTemasGenero");		
	}

	public void descargarBD(){
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		//		String archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BaseDatosSIS").append(".xls").toString();
		String archivo="";
		switch (getSitioPublicoBean().getPosicionSalvaguardas()){
		case 1:
			archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_A").append(".xls").toString();
			break;
		case 2:
			archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_B").append(".xls").toString();
			break;
		case 3:
			archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_C").append(".xls").toString();
			break;
		case 4:
			archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_D").append(".xls").toString();
			break;
		case 5:
			archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_E").append(".xls").toString();
			break;
		case 6:
			archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_F").append(".xls").toString();
			break;
		case 7:
			archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_G").append(".xls").toString();
			break;
		}

		try{
			if(archivo.length()>0){
				File ficheroXLS = new File(archivo);
				if(ficheroXLS.exists()){
					FacesContext fctx = FacesContext.getCurrentInstance();
					FileInputStream fis = new FileInputStream(ficheroXLS);
					byte[] bytes = new byte[1000];
					int read = 0;

					if (!fctx.getResponseComplete()) {
						String fileName = ficheroXLS.getName();
						String contentType = "application/vnd.ms-excel";
						HttpServletResponse response =(HttpServletResponse) fctx.getExternalContext().getResponse();
						response.setContentType(contentType);
						response.setHeader("Content-Disposition","attachment;filename=\"" + fileName + "\"");
						ServletOutputStream out = response.getOutputStream();
						while ((read = fis.read(bytes)) != -1) {
							out.write(bytes, 0, read);
						}
						out.flush();
						out.close();
						System.out.println("\nDescargado\n");
						fctx.responseComplete();
					}
				}else{
					Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "",getMensajesController().getPropiedad("error.archivo"));
					Mensaje.actualizarComponente(":frm:growl");	
				}
			}
		} catch (IOException err) {  
			err.printStackTrace();  
		} 
	}

	public void createPieModel() {
		pieModel = new PieChartModel();
		ChartData data = new ChartData();
		PieChartDataSet dataSet = new PieChartDataSet();
		List<Number> values = new ArrayList<>();
		List<String> labels = new ArrayList<>();
		List<String> bgColors = new ArrayList<>();
		int cont = 0;
		for (DtoGenero g : getSitioPublicoBean().getListaTemasGenero()) {
			values.add(g.getNumero());
			labels.add(g.getTema());
			bgColors.add(getSitioPublicoBean().getColores().get(cont));
			cont++;
		}        
		dataSet.setData(values);

		dataSet.setBackgroundColor(bgColors);

		data.addChartDataSet(dataSet);
		data.setLabels(labels);

		pieModel.setData(data);        
		Mensaje.verDialogo("dlgTemasGenero");		
	}
	public void iniciaColores(){
		getSitioPublicoBean().setColores(new ArrayList<>());
		getSitioPublicoBean().getColores().add("rgb(255,0,0)");
		getSitioPublicoBean().getColores().add("rgb(0,255,0)");
		getSitioPublicoBean().getColores().add("rgb(0,0,255)");
		getSitioPublicoBean().getColores().add("rgb(255,255,0)");
		getSitioPublicoBean().getColores().add("rgb(0,255,255)");
		getSitioPublicoBean().getColores().add("rgb(255,0,255)");
		getSitioPublicoBean().getColores().add("rgb(192,192,192)");
		getSitioPublicoBean().getColores().add("rgb(128,128,128)");
		getSitioPublicoBean().getColores().add("rgb(128,0,0)");
		getSitioPublicoBean().getColores().add("rgb(128,128,0)");
		getSitioPublicoBean().getColores().add("rgb(0,128,0)");
		getSitioPublicoBean().getColores().add("rgb(128,0,128)");
		getSitioPublicoBean().getColores().add("rgb(0,128,128)");
		getSitioPublicoBean().getColores().add("rgb(0,0,128)");
	}
}

