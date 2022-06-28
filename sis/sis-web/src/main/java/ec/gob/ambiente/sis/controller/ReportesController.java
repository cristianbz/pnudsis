/**
@autor proamazonia [Christian Báez]  16 jun. 2022

**/
package ec.gob.ambiente.sis.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.model.Projects;
import ec.gob.ambiente.sigma.services.ProjectsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.ReportesBean;
import ec.gob.ambiente.sis.dto.DtoResumenAbordajeSalvaguarda;
import ec.gob.ambiente.sis.model.AdvanceExecutionSafeguards;
import ec.gob.ambiente.sis.model.AdvanceSummary;
import ec.gob.ambiente.sis.services.AdvanceSummaryFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.utils.GeneradorPdfHtml;
import ec.gob.ambiente.sis.utils.GenerarPdfResumenAbordaje;
import lombok.Getter;

@Named()
@ViewScoped
public class ReportesController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(ReportesController.class);
	
	@Inject
	@Getter
	private ReportesBean reportesBean;
	
	@EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;
	
	@EJB
	@Getter
	private TableResponsesFacade tableResponsesFacade;
	@EJB
	@Getter
	private AdvanceSummaryFacade advanceSummaryFacade;
	
	@EJB
	@Getter
	private ProjectsFacade projectsFacade;
	
	@Inject
	@Getter
	private GeneradorPdfHtml generadorPdfHtml;
	
	@PostConstruct
	public void init(){
		try{
			getReportesBean().setTipoCargaInformacion(1);
			getReportesBean().setListaSalvaguardas(new ArrayList<>());
			getReportesBean().setListaSalvaguardas(getSafeguardsFacade().cargaSalvaguardasActivas());
			getReportesBean().setListaProyectos(new ArrayList<>());
			getReportesBean().setListaProyectos(getProjectsFacade().buscarTodosLosProyectos());
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "init " + ": ").append(e.getMessage()));
		}
	}
	
	public void llenaDatosPorPeriodo(DtoResumenAbordajeSalvaguarda dto ){
		try{
			String anio = getReportesBean().getAnio().toString().concat("-01");	
			List<AdvanceSummary> listaAvances = new ArrayList<>();
			listaAvances = getAdvanceSummaryFacade().listaAvancesProyecto(getReportesBean().getProyectoSeleccionado().getProjId(), anio);
			dto.setAnio(getReportesBean().getAnio().toString());			
			String resumen="";			
			resumen="<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA A</p>\r\n" ;
			resumen = llenaInformacionSocioImplementador(listaAvances, "A", resumen);
			resumen += "<p style='color: #0da5c4;margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;font-weight: bold;'>RESUMEN SOCIOS ESTRATÉGICOS </p>\r\n" ;
			resumen += "<table class='tablaborder' width='100%' style='margin-left: 3em;font-size:11px;font-family: sans-serif;table-layout: fixed;'>\r\n";
			resumen += "<tr class='titulotabla'>\r\n" + " <td class='tablaborder' bgcolor='#FFFFFF' width='25%' >Socio</td> <td class='tablaborder' bgcolor='#FFFFFF' width='75%;'>Resumen</td></tr>\r\n";
			resumen =llenaInformacionSocioEstrategico(listaAvances, "A", resumen);				
			dto.setTablaResumenSociosEstrategicos(resumen);
			resumen+="<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA B</p>\r\n" ;
			resumen = llenaInformacionSocioImplementador(listaAvances, "B", resumen);
			resumen += "<p style='color: #0da5c4;margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;font-weight: bold;'>RESUMEN SOCIOS ESTRATÉGICOS </p>\r\n" ;
			resumen += "<table class='tablaborder' width='100%' style='margin-left: 3em;font-size:11px;font-family: sans-serif;table-layout: fixed;'>\r\n";
			resumen += "<tr class='titulotabla'>\r\n" + " <td class='tablaborder' bgcolor='#FFFFFF' width='25%' >Socio</td> <td class='tablaborder' bgcolor='#FFFFFF' width='75%'>Resumen</td></tr>\r\n";
			resumen =llenaInformacionSocioEstrategico(listaAvances, "B", resumen);				
			dto.setTablaResumenSociosEstrategicos(resumen);
			resumen+="<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA C</p>\r\n" ;
			resumen = llenaInformacionSocioImplementador(listaAvances, "C", resumen);
			resumen += "<p style='color: #0da5c4;margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;font-weight: bold;'>RESUMEN SOCIOS ESTRATÉGICOS </p>\r\n" ;
			resumen += "<table class='tablaborder' width='100%' style='margin-left: 3em;font-size:11px;font-family: sans-serif;table-layout: fixed;'>\r\n";
			resumen += "<tr class='titulotabla'>\r\n" + " <td class='tablaborder' bgcolor='#FFFFFF' width='25%' >Socio</td> <td class='tablaborder' bgcolor='#FFFFFF' width='75%'>Resumen</td></tr>\r\n";
			resumen =llenaInformacionSocioEstrategico(listaAvances, "C", resumen);				
			dto.setTablaResumenSociosEstrategicos(resumen);
			resumen+="<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA D</p>\r\n" ;
			resumen = llenaInformacionSocioImplementador(listaAvances, "D", resumen);
			resumen += "<p style='color: #0da5c4;margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;font-weight: bold;'>RESUMEN SOCIOS ESTRATÉGICOS </p>\r\n" ;
			resumen += "<table class='tablaborder' width='100%' style='margin-left: 3em;font-size:11px;font-family: sans-serif;table-layout: fixed;'>\r\n";
			resumen += "<tr class='titulotabla'>\r\n" + " <td class='tablaborder' bgcolor='#FFFFFF' width='25%' >Socio</td> <td class='tablaborder' bgcolor='#FFFFFF' width='75%'>Resumen</td></tr>\r\n";
			resumen =llenaInformacionSocioEstrategico(listaAvances, "D", resumen);
			resumen+="<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA E</p>\r\n" ;
			resumen = llenaInformacionSocioImplementador(listaAvances, "E", resumen);
			resumen += "<p style='color: #0da5c4;margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;font-weight: bold;'>RESUMEN SOCIOS ESTRATÉGICOS </p>\r\n" ;
			resumen += "<table class='tablaborder' width='100%' style='margin-left: 3em;font-size:11px;font-family: sans-serif;table-layout: fixed;'>\r\n";
			resumen += "<tr class='titulotabla'>\r\n" + " <td class='tablaborder' bgcolor='#FFFFFF' width='25%' >Socio</td> <td class='tablaborder' bgcolor='#FFFFFF' width='75%'>Resumen</td></tr>\r\n";
			resumen =llenaInformacionSocioEstrategico(listaAvances, "E", resumen);				
			dto.setTablaResumenSociosEstrategicos(resumen);
			resumen+="<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA F</p>\r\n" ;
			resumen = llenaInformacionSocioImplementador(listaAvances, "F", resumen);
			resumen += "<p style='color: #0da5c4;margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;font-weight: bold;'>RESUMEN SOCIOS ESTRATÉGICOS </p>\r\n" ;
			resumen += "<table class='tablaborder' width='100%' style='margin-left: 3em;font-size:11px;font-family: sans-serif;table-layout: fixed;'>\r\n";
			resumen += "<tr class='titulotabla'>\r\n" + " <td class='tablaborder' bgcolor='#FFFFFF' width='25%' >Socio</td> <td class='tablaborder' bgcolor='#FFFFFF' width='75%'>Resumen</td></tr>\r\n";
			resumen =llenaInformacionSocioEstrategico(listaAvances, "F", resumen);				
			dto.setTablaResumenSociosEstrategicos(resumen);
			resumen+="<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA G</p>\r\n" ;
			resumen = llenaInformacionSocioImplementador(listaAvances, "G", resumen);
			resumen += "<p style='color: #0da5c4;margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;font-weight: bold;'>RESUMEN SOCIOS ESTRATÉGICOS </p>\r\n" ;
			resumen += "<table class='tablaborder' width='100%' style='margin-left: 3em;font-size:11px;font-family: sans-serif;table-layout: fixed;'>\r\n";
			resumen += "<tr class='titulotabla'>\r\n" + " <td class='tablaborder' bgcolor='#FFFFFF' width='25%' >Socio</td> <td class='tablaborder' bgcolor='#FFFFFF' width='75%'>Resumen</td></tr>\r\n";
			resumen =llenaInformacionSocioEstrategico(listaAvances, "G", resumen);				
			dto.setTablaResumenSociosEstrategicos(resumen);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String llenaInformacionSocioImplementador(List<AdvanceSummary> lista, String salvaguarda,String resumen){
		String datos="";
		for (AdvanceSummary as : lista) {				
			if(as.getSafeguards().getSafeCode().equals(salvaguarda) && as.getAdvanceExecutionSafeguards().getProjectsStrategicPartners()==null){	
				String avance=as.getAdsuAdvance();
				if(avance==null)
					avance="";
				datos += "<p style='color: #0da5c4; font-family: sans-serif;font-size: 11;font-weight: bold;margin-left: 3em;border-style:none;border-collapse: collapse;'> RESUMEN SOCIO IMPLEMENTADOR</p>\r\n"; 
				datos += "<p style='margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;'> " + avance +"</p>\r\n" ;				
				break;
			}			
		}
		resumen += datos;
		return resumen;
	}
	public String llenaInformacionSocioEstrategico(List<AdvanceSummary> lista, String salvaguarda,String resumen){
		String filas="";
		for (AdvanceSummary as : lista) {				
			if(as.getSafeguards().getSafeCode().equals(salvaguarda) && as.getAdvanceExecutionSafeguards().getProjectsStrategicPartners()!=null){
				String avance=as.getAdsuAdvance();
				if(avance==null)
					avance="";
				filas += "<tr><td class='tablaborder' bgcolor='#FFFFFF' >" + as.getAdvanceExecutionSafeguards().getProjectsStrategicPartners().getPartners().getPartName() + "</td> <td class='tablaborder' bgcolor='#FFFFFF' >" + avance + "</td> </tr>\r\n";							
			}			
		}
		filas += "</table>\r\n";
		resumen+=filas;
		return resumen;
	}
	
	
	public void generarPdfResumenAbordajePorProyecto(){
		try{
			
			String htmlReporte = GenerarPdfResumenAbordaje.REPORTE_RESUMEN_ENCABEZADO_POR_PROYECTO;
			String pattern = "MMM yy HH:mm";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(new Date());
			String periodo="";
				
					
			DtoResumenAbordajeSalvaguarda dto = new DtoResumenAbordajeSalvaguarda();
			dto.setLogoEscudo("escudoE.png");
			dto.setLogoMae("mae.png");
			dto.setLogoPie("pieAmbiente.png");
			dto.setFecha(date);
			dto.setProyecto(getReportesBean().getProyectoSeleccionado().getProjTitle());
			
			dto.setSocioImplementador(getReportesBean().getProyectoSeleccionado().getPartners().getPartName());
			periodo = getReportesBean().getAnio().toString().concat(" Enero - Diciembre");			
			
			llenaDatosPorPeriodo(dto);
			htmlReporte += GenerarPdfResumenAbordaje.REPORTE_CONTENIDO_RESUMEN__POR_PROYECTO;
			htmlReporte += GenerarPdfResumenAbordaje.REPORTE_RESUMEN_PIE;			
			String html = getGeneradorPdfHtml().procesar(htmlReporte, dto);
			byte[] array = getGeneradorPdfHtml().crearDocumentoPdf(html,1);
			descargarReporte("application/pdf", "resumenAbordaje" , array);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "generarPdfResumenAbordajePorProyecto" + ": ").append(e.getMessage()));
//			e.printStackTrace();
		}
	}
	
	public void generarPdfResumenAbordajePorSalvaguarda(){
		try{
			Map<Integer,Projects> mapaTemp=new HashMap<Integer,Projects>();
			String htmlReporte = GenerarPdfResumenAbordaje.REPORTE_RESUMEN_ENCABEZADO_POR_SALVAGUARDA;
			String pattern = "MMM yy HH:mm";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(new Date());
			String periodo="";
			DtoResumenAbordajeSalvaguarda dto = new DtoResumenAbordajeSalvaguarda();
			dto.setLogoEscudo("escudoE.png");
			dto.setLogoMae("mae.png");
			dto.setLogoPie("pieAmbiente.png");
			dto.setFecha(date);
			dto.setSalvaguarda(getReportesBean().getSalvaguardaSeleccionada().getSafeCode());
			periodo = getReportesBean().getAnio().toString().concat(" Enero - Diciembre");				
			dto.setAnio(periodo);		
			String anio = getReportesBean().getAnio().toString().concat("-01");	
			List<AdvanceSummary> listaAvance = getAdvanceSummaryFacade().listaAvancesSalvaguarda_Anio(anio, getReportesBean().getSalvaguardaSeleccionada().getSafeId());
			for (AdvanceSummary asu : listaAvance) {
				mapaTemp.put(asu.getAdvanceExecutionSafeguards().getProjects().getProjId(), asu.getAdvanceExecutionSafeguards().getProjects());
			}			
			llenarInformacionPorSalvaguarda(mapaTemp, listaAvance,dto);
			htmlReporte += GenerarPdfResumenAbordaje.REPORTE_CONTENIDO_RESUMEN__POR_SALVAGUARDA;			
			htmlReporte += GenerarPdfResumenAbordaje.REPORTE_RESUMEN_PIE;			
			String html = getGeneradorPdfHtml().procesar(htmlReporte, dto);
			byte[] array = getGeneradorPdfHtml().crearDocumentoPdf(html,1);
			descargarReporte("application/pdf", "resumenAbordaje" , array);
		}catch(Exception e){
			LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "generarPdfResumenAbordajePorSalvaguarda" + ": ").append(e.getMessage()));			
		}
	}
	
	public void llenarInformacionPorSalvaguarda(Map<Integer, Projects> mapa,List<AdvanceSummary> listaAvance,DtoResumenAbordajeSalvaguarda dto){
		String html="";
		for (Entry<Integer,Projects> m : mapa.entrySet()){			
			html+="<p style='color: #0da5c4; font-family: sans-serif;font-size: 11;font-weight: bold;border-style:none;border-collapse: collapse;'>PROYECTO: </p>\r\n" +
				 "<p style='margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;'>" + m.getValue().getProjTitle() +"</p>\r\n" + 
				 "<p style='color: #0da5c4; font-family: sans-serif;font-size: 11;font-weight: bold;border-style:none;border-collapse: collapse;'>RESUMEN SOCIO IMPLEMENTADOR: </p>\r\n";
			for (AdvanceSummary asu : listaAvance) {
				if(asu.getAdvanceExecutionSafeguards().getProjects().getProjId() == m.getKey()){
					if(asu.getAdvanceExecutionSafeguards().getProjectsStrategicPartners() == null){
						String avance=asu.getAdsuAdvance();
						if(avance==null)
							avance="";
						html+= "<p style='margin-left: 3em;border-style:none;border-collapse: collapse;font-size:11px;font-family: sans-serif;'>" + avance  +"</p>\r\n" ;
						break;
					}
				}
			}
			html+="<p style='color: #0da5c4; font-family: sans-serif;font-size: 11;font-weight: bold;border-style:none;border-collapse: collapse;'>RESUMEN SOCIOS ESTRATEGICOS: </p>\r\n";
			
			html += "<table class='tablaborder' width='100%' style='margin-left: 3em;font-size:11px;font-family: sans-serif;table-layout: fixed;'>\r\n";
			html += "<tr class='titulotabla'>\r\n" + " <td class='tablaborder' bgcolor='#FFFFFF' width='25%' >Socio</td> <td class='tablaborder' bgcolor='#FFFFFF' width='75%'>Resumen</td></tr>\r\n";
			for (AdvanceSummary asu : listaAvance) {
				if(asu.getAdvanceExecutionSafeguards().getProjects().getProjId() == m.getKey()){
					if(asu.getAdvanceExecutionSafeguards().getProjectsStrategicPartners() != null){
						String avance=asu.getAdsuAdvance();
						if(avance==null)
							avance="";
						html += "<tr><td class='tablaborder' bgcolor='#FFFFFF' >" + asu.getAdvanceExecutionSafeguards().getProjectsStrategicPartners().getPartners().getPartName() + "</td> <td class='tablaborder' bgcolor='#FFFFFF' >" + avance + "</td> </tr>\r\n";
					}
				}
			}
			html+="</table><br/>\r\n";
			
		}
		dto.setTablaResumenSalvaguardas(html);
//		return html;
	}
	
	private void descargarReporte(String mimeType, String nombreArchivo, byte[] bs) throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		response.reset();
		response.setContentType(mimeType);
		response.setHeader("Content-disposition", "attachment; filename=\"" + nombreArchivo + ".pdf");

		OutputStream output = response.getOutputStream();
		output.write(bs);
		output.flush();
		output.close();

		facesContext.responseComplete();
	}
}

