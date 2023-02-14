/**
@autor proamazonia [Christian Báez]  2 dic. 2021

**/
package ec.gob.ambiente.sis.services;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import org.apache.log4j.Logger;

import com.github.cliftonlabs.json_simple.Jsoner;
import com.opencsv.CSVWriter;

import ec.gob.ambiente.sigma.services.MeetingsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
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
import lombok.Getter;

@Singleton
public class GenerarDatosSitioPublico implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(GenerarDatosSitioPublico.class);
	
	public static final String FOLDER_NAME = "/reportes";
	
	public static String webPath;
	@EJB
	@Getter
	private TableResponsesFacade tableResponsesFacade;
	
	@EJB
	@Getter
	private ProjectsGenderInfoFacade projectsGenderFacade;
	
	@EJB
	@Getter
	private AdvanceExecutionProjectGenderFacade avanceExecutionFacade;
	
	@EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;
	
	@EJB
	@Getter
	private MeetingsFacade meetingsFacade;
	
	@Schedule(second = "0", minute = "01", hour = "23",dayOfWeek="Sun" , persistent = false)
//	@Schedule(minute = "*/1", hour="*", persistent = false)
//	@Schedule(hour="23",minute = "0", second="0", persistent = false)
	public void generarResumen(){

		List<TableResponses> listaTemp= new ArrayList<>();
		List<TableResponses> listaTempComunidades= new ArrayList<>();		
		List<String> listaComunidades=new ArrayList<>();
		List<TableResponses> listaTempProvincias= new ArrayList<>();
		try{
			List<String> listaProyectos=new ArrayList<>();
			BigDecimal totalInversion = new BigDecimal(0);
			listaTemp = getTableResponsesFacade().listaProyectosValoresSalvaguardaA();
			Map<String,BigDecimal> mapaTemp=new HashMap<String,BigDecimal>();
			for(TableResponses tr: listaTemp){
				mapaTemp.put(tr.getTareColumnOne(), tr.getTareColumnDecimalOne());
				totalInversion = totalInversion.add(tr.getTareColumnDecimalOne());
			}
			for(Entry<String,BigDecimal> proy: mapaTemp.entrySet()){
				listaProyectos.add(proy.getKey());
			}			
			DtoDatosSitioPublicoA dtoSalvaguardaA = new DtoDatosSitioPublicoA("A");
			dtoSalvaguardaA.setNumeroProyectos(listaProyectos.size());
			dtoSalvaguardaA.setTotalInversionProyectos(totalInversion);
//			dtoSalvaguardaA.setListadoProyectos(getTableResponsesFacade().listadoProyectos());
			dtoSalvaguardaA.setListadoProyectos(getTableResponsesFacade().listadoProyectosConservacion());
			////B
			DtoDatosSitioPublicoB dtoSalvaguardaB = new DtoDatosSitioPublicoB("B");

			listaTempComunidades = new ArrayList<>();
			listaTempComunidades = getTableResponsesFacade().listaMaximoHombresMujeresSalvaguardaB();
			dtoSalvaguardaB.setTotalPersonasAccesoInfo(getTableResponsesFacade().totalPersonasAccesoInfoB());
			dtoSalvaguardaB.setNumeroAlianzas(getTableResponsesFacade().numeroAlianzasB());
			dtoSalvaguardaB.setNumeroEventosRendicion(getTableResponsesFacade().numeroEventosRendicionB());			
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
			dtoSalvaguardaC.setNumeroOrganizacionIndigenaC(getTableResponsesFacade().numeroOrganizacionIndigenaC());
			dtoSalvaguardaC.setNumeroEventosCPLIC(getTableResponsesFacade().numeroEventosCPLIC());

			///// D
			DtoDatosSitioPublicoD dtoSalvaguardaD = new DtoDatosSitioPublicoD("D");

//			dtoSalvaguardaD.setTotalEventosHombres(getMeetingsFacade().listaEventosFortalecimientoHombres());
//			dtoSalvaguardaD.setTotalEventosMujeres(getMeetingsFacade().listaEventosFortalecimientoMujeres());
			dtoSalvaguardaD.setTotalEspaciosD(getTableResponsesFacade().numeroEspaciosCapacitacionD());
			dtoSalvaguardaD.setTotalPersonasParticipacionD(getTableResponsesFacade().totalPersonasParticipacionD());
			dtoSalvaguardaD.setNumeroOrganizacionesLocalesD(getTableResponsesFacade().numeroOrganizacionesLocalesD());

			// E
			DtoDatosSitioPublicoE dtoSalvaguardaE = new DtoDatosSitioPublicoE("E");
			listaTempProvincias = getTableResponsesFacade().listaFomentoGestionComunitariaE();

//			dtoSalvaguardaE.setNumeroFomentoGestionComunitaria(listaTempProvincias.size());
//			dtoSalvaguardaE.setTotalHectareasCobertura(getTableResponsesFacade().totalHectareasCoberturaE());
			dtoSalvaguardaE.setTotalHectareasConservadoE(getTableResponsesFacade().totalHectareasConservacionE());
			dtoSalvaguardaE.setTotalHectareasRestauradoE(getTableResponsesFacade().totalHectareasRestauracionE());
			dtoSalvaguardaE.setNumeroInvestigacionesE(getTableResponsesFacade().numeroInvestigacionesE());

			// F
			DtoDatosSitioPublicoF dtoSalvaguardaF = new DtoDatosSitioPublicoF("F");			
			dtoSalvaguardaF.setTotalAccionesReversion(getTableResponsesFacade().numeroAccionesEvitarRiesgos_F());
			dtoSalvaguardaF.setListadoMedidasTomadas(getTableResponsesFacade().listaMedidasTomadas_F());
			dtoSalvaguardaF.setNumeroInvestigacionesF(getTableResponsesFacade().numeroInvestigacionesF());
			dtoSalvaguardaF.setNumeroAccionesFortalecimientoF(getTableResponsesFacade().numeroAccionesFortalecimientoF());
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
			dtoSalvaguardaG.setNumeroActividadesControl(getTableResponsesFacade().numeroActividadesControlG());
			//GENERO
			
			DtoDatosSitioPublicoGenero dtoGenero = new DtoDatosSitioPublicoGenero("GENERO");
			List<DtoGenero> listTempTemas = getAvanceExecutionFacade().listaTemasGenero();
			List<DtoGenero> listProyectosTemas = getAvanceExecutionFacade().listaProyectosTemas();
			List<String> listTempAcciones = getAvanceExecutionFacade().listadoAccionesGenero();
			dtoGenero.setTotalPresupuesto(getAvanceExecutionFacade().presupuestoGenero());
			int totalTemas = 0;			
//			if(listTempTemas!=null && listTempTemas.size()>0){
//				dtoGenero.setListaTemasGenero(listTempTemas);
//				for (DtoGenero genero : listTempTemas) {
//					totalTemas = totalTemas + genero.getNumero();
//				}
//				dtoGenero.setTotalTemasAplicados(totalTemas);
//			}else{
//				dtoGenero.setTotalTemasAplicados(0);
//				dtoGenero.setListaTemasGenero(null);
//			}
			
			if(listTempAcciones!=null && listTempAcciones.size()>0){
				dtoGenero.setListaAccionesGenero(listTempAcciones);
				dtoGenero.setTotalAccionesImplementadas(listTempAcciones.size());
			}else{
				dtoGenero.setListaAccionesGenero(new ArrayList<>());
				dtoGenero.setTotalAccionesImplementadas(0);
			}
			dtoGenero.setListaLineasAccionProyecto(procesaLineasAccionProyectoGenero(getProjectsGenderFacade().listaLineaAccionProyecto()));
			dtoGenero.setListaAporteProyectoGenero(procesaAporteProyectoGenero(getProjectsGenderFacade().listaAporteProyectoGenero()));
			
			String archivoJSON = new StringBuilder().append(this.webPath).append(File.separator).append("archivo").append(".json").toString();
//			String archivoCSVA = new StringBuilder().append(this.webPath).append(File.separator).append("salvaguardaA").append(".csv").toString();
	
//			List<Object> lista = Arrays.asList(dtoSalvaguardaA.toJson(), dtoSalvaguardaB.toJson(),dtoSalvaguardaC.toJson(),dtoSalvaguardaD.toJson(), dtoSalvaguardaE.toJson() , dtoSalvaguardaF.toJson() , dtoSalvaguardaG.toJson(),dtoGenero.toJson());
			List<Object> lista = Arrays.asList(dtoSalvaguardaA.toJson(), dtoSalvaguardaB.toJson(),dtoSalvaguardaC.toJson(),dtoSalvaguardaD.toJson(), dtoSalvaguardaE.toJson(), dtoSalvaguardaF.toJson(), dtoSalvaguardaG.toJson(),dtoGenero.toJson());
			try (FileWriter fileWriter = new FileWriter(archivoJSON)) {
				Jsoner.serialize(lista, fileWriter);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "generarResumen " + ": ").append(e.getMessage()));
		}
	}
	
	public List<DtoGenero> procesaLineasAccionProyectoGenero(List<Object[]> lista){
		List<DtoGenero> listaTemp = new ArrayList<>();
		List<DtoGenero> listaFinal = new ArrayList<>();
		if(lista.size()>0){
			for(Object obj:lista){
				Object[] dataObj = (Object[]) obj;
				DtoGenero genero = new DtoGenero();
				genero.setLineaAccion(dataObj[0].toString());
				if(genero.getLineaAccion().equals("Otro"))
					genero.setLineaAccion(dataObj[1].toString());
				genero.setProyecto(dataObj[2].toString());
				listaTemp.add(genero);
			}
			String lineaAccion= listaTemp.get(0).lineaAccion;
			StringBuilder proyecto= new StringBuilder();
			DtoGenero genero = new DtoGenero();
			for (DtoGenero dtoGenero : listaTemp) {
				if(dtoGenero.getLineaAccion().equals(lineaAccion)){
					genero.setLineaAccion(dtoGenero.getLineaAccion());
					proyecto.append(dtoGenero.getProyecto()).append(",");
				}else{
					genero.setProyecto(proyecto.toString());
					listaFinal.add(genero);
					genero=new DtoGenero();
					lineaAccion = dtoGenero.getLineaAccion();
					genero.setLineaAccion(lineaAccion);
					proyecto = new StringBuilder();
					proyecto.append(dtoGenero.getProyecto()).append(",");
				}
					
			}
			return listaFinal;
		}else
			return null;
	}
	
	public  List<DtoGenero> procesaAporteProyectoGenero(List<Object[]> lista){
		List<DtoGenero> listaTemp = new ArrayList<>();
		List<DtoGenero> listaFinal = new ArrayList<>();
		if(lista.size()>0){
			for(Object obj:lista){
				Object[] dataObj = (Object[]) obj;
				DtoGenero genero = new DtoGenero();
				genero.setProyecto(dataObj[0].toString());				
				genero.setPresupuesto((BigDecimal)dataObj[1]);
				listaTemp.add(genero);
			}
			String proyecto= listaTemp.get(0).proyecto;
			BigDecimal monto= BigDecimal.ZERO;
			DtoGenero genero = new DtoGenero();
			int contador=0;
			for (DtoGenero dtoGenero : listaTemp) {
				if(dtoGenero.getProyecto().equals(proyecto)){
					genero.setProyecto(dtoGenero.getProyecto());
					monto = monto.add(dtoGenero.getPresupuesto());
				}else{
					genero.setPresupuesto(monto);
					listaFinal.add(genero);
					genero=new DtoGenero();
					proyecto = dtoGenero.getProyecto();
					genero.setProyecto(proyecto);
					monto = BigDecimal.ZERO;
					monto = monto.add( dtoGenero.getPresupuesto());
				}
				contador++;	
				if(listaTemp.size()==1 || listaTemp.size() == contador){
					genero.setPresupuesto(monto);
					listaFinal.add(genero);
				}
				
			}
			return listaFinal;
		}else
			return null;
		
	}
	
//	public void generarcsv(String path){
//		try{			 				
//			CSVWriter writer = new CSVWriter(new FileWriter(path));			        
//			//Create record
//			String [] record = "4,David,Miller,Australia,30".split(",");
//			//Write the record to file
//			writer.writeNext(record);				        
//			//close the writer
//			writer.close();            
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
}

