/**
@autor proamazonia [Christian Báez]  17 ene. 2022

**/
package ec.gob.ambiente.sis.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ec.gob.ambiente.sis.dto.DtoTableResponses;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;

public class GenerarBDSalvaguardaA {
	private static final Logger LOG = Logger.getLogger(GenerarBDSalvaguardaA.class);
	@SuppressWarnings("resource")
	public static void generaArchivoSalvaguardaA(TableResponsesFacade servicio, QuestionsFacade servicioPreguntas){
		try{
			ResourceBundle rb;
			rb = ResourceBundle.getBundle("resources.indicadores");
			List<Questions> listaPreguntas = servicioPreguntas.buscaPreguntaPorSalvaguarda(1);
			int cuentaFila = 0;
			HSSFWorkbook workbook = new HSSFWorkbook();			
			HSSFFont bold = workbook.createFont();
			bold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle styleBold = workbook.createCellStyle();
			styleBold.setFont( bold );
			
			HSSFSheet sheet = workbook.createSheet("INDICADORES");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 4000);

			HSSFRow row = sheet.createRow(1);
			HSSFCell cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("A_3"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroInstrumentosPolitica_A());
			cell.setCellStyle(styleBold);

			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("A_4"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(servicio.cantidadProyectosInvertidos_A().toString()));
			cell.setCellStyle(styleBold);

			
			sheet = workbook.createSheet("PREGUNTA 1.1");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 30000);
			sheet.setColumnWidth((short) 4,(short) 30000);

			row = sheet.createRow(1);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(0).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue("Marco Jurídico Internacional");
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(5);			
			cell = row.createCell(0);
			cell.setCellValue("PROYECTO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue("PERIODO");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(2);
			cell.setCellValue("SOCIO IMPLEMENTADOR");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(3);
			cell.setCellValue("SOCIO ESTRATEGICO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(4);
			cell.setCellValue("LEY");
			cell.setCellStyle(styleBold);

			
			List<DtoTableResponses> lista = new ArrayList<>();
			lista = servicio.listaPoliticasLeyes(listaPreguntas.get(0).getQuesId(),3);
			cuentaFila = 6;
			for (DtoTableResponses dt : lista) {
				row = sheet.createRow(cuentaFila);			
				cell = row.createCell(0);
				cell.setCellValue(dt.getProyecto());
				
				cell = row.createCell(1);
				cell.setCellValue(dt.getPeriodo());
				
				cell = row.createCell(2);
				cell.setCellValue(dt.getSocioImplementador());
				
				cell = row.createCell(3);
				cell.setCellValue(dt.getSocioEstrategico());
				
				cell = row.createCell(4);
				cell.setCellValue(dt.getTextoUno());
				cuentaFila++;
			}
			cuentaFila++;
			row = sheet.createRow(cuentaFila);			
			cell = row.createCell(0);
			cell.setCellValue("Marco Jurídico Nacional");
			cell.setCellStyle(styleBold);
			
			cuentaFila++;
			row = sheet.createRow(cuentaFila);			
			cell = row.createCell(0);
			cell.setCellValue("PROYECTO");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(1);
			cell.setCellValue("PERIODO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(2);
			cell.setCellValue("SOCIO IMPLEMENTADOR");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(3);
			cell.setCellValue("SOCIO ESTRATEGICO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(4);
			cell.setCellValue("LEY");
			cell.setCellStyle(styleBold);

			
			lista = new ArrayList<>();
			lista = servicio.listaPoliticasLeyes(listaPreguntas.get(0).getQuesId(),24);
			cuentaFila++;
			for (DtoTableResponses dt : lista) {
				row = sheet.createRow(cuentaFila);			
				cell = row.createCell(0);
				cell.setCellValue(dt.getProyecto());
				
				cell = row.createCell(1);
				cell.setCellValue(dt.getPeriodo());
				
				cell = row.createCell(2);
				cell.setCellValue(dt.getSocioImplementador());
				
				cell = row.createCell(3);
				cell.setCellValue(dt.getSocioEstrategico());
				
				cell = row.createCell(4);
				cell.setCellValue(dt.getTextoUno());
				cuentaFila++;
			}
			cuentaFila++;
			row = sheet.createRow(cuentaFila);			
			cell = row.createCell(0);
			cell.setCellValue("Normativa Secundaria Nacional");
			cell.setCellStyle(styleBold);
			
			cuentaFila++;
			row = sheet.createRow(cuentaFila);			
			cell = row.createCell(0);
			cell.setCellValue("PROYECTO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue("PERIODO");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(2);
			cell.setCellValue("SOCIO IMPLEMENTADOR");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(3);
			cell.setCellValue("SOCIO ESTRATEGICO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(4);
			cell.setCellValue("LEY");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaPoliticasLeyes(listaPreguntas.get(0).getQuesId(),25);
			cuentaFila++;
			for (DtoTableResponses dt : lista) {
				row = sheet.createRow(cuentaFila);			
				cell = row.createCell(0);
				cell.setCellValue(dt.getProyecto());
				
				cell = row.createCell(1);
				cell.setCellValue(dt.getPeriodo());
				
				cell = row.createCell(2);
				cell.setCellValue(dt.getSocioImplementador());
				
				cell = row.createCell(3);
				cell.setCellValue(dt.getSocioEstrategico());
				
				cell = row.createCell(4);
				cell.setCellValue(dt.getTextoUno());
				cuentaFila++;
			}
			
			sheet = workbook.createSheet("PREGUNTA 2.1");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 30000);
			sheet.setColumnWidth((short) 4,(short) 30000);
			
			cuentaFila=1;
			row = sheet.createRow(1);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(1).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			cuentaFila++;
			row = sheet.createRow(cuentaFila);			
			cell = row.createCell(0);
			cell.setCellValue("PROYECTO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue("PERIODO");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(2);
			cell.setCellValue("SOCIO IMPLEMENTADOR");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(3);
			cell.setCellValue("SOCIO ESTRATEGICO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(4);
			cell.setCellValue("POLITICA");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaPoliticasLeyes(listaPreguntas.get(1).getQuesId(),2);
			cuentaFila++;
			for (DtoTableResponses dt : lista) {
				row = sheet.createRow(cuentaFila);			
				cell = row.createCell(0);
				cell.setCellValue(dt.getProyecto());
				
				cell = row.createCell(1);
				cell.setCellValue(dt.getPeriodo());
				
				cell = row.createCell(2);
				cell.setCellValue(dt.getSocioImplementador());
				
				cell = row.createCell(3);
				cell.setCellValue(dt.getSocioEstrategico());
				
				cell = row.createCell(4);
				cell.setCellValue(dt.getTextoUno());
				cuentaFila++;
			}
			
			sheet = workbook.createSheet("PREGUNTA 3");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10000);
			sheet.setColumnWidth((short) 3,(short) 10200);
			sheet.setColumnWidth((short) 4,(short) 30000);
			sheet.setColumnWidth((short) 5,(short) 10000);
			
			cuentaFila=1;
			row = sheet.createRow(1);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(2).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			cuentaFila++;
			row = sheet.createRow(cuentaFila);			
			cell = row.createCell(0);
			cell.setCellValue("PROYECTO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue("PERIODO");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(2);
			cell.setCellValue("SOCIO IMPLEMENTADOR");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(3);
			cell.setCellValue("SOCIO ESTRATEGICO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(4);
			cell.setCellValue("Plan/Proyecto del Gobierno Nacional");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Presupuesto asignado");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaProgProyA_3();
			cuentaFila++;
			for (DtoTableResponses dt : lista) {
				row = sheet.createRow(cuentaFila);			
				cell = row.createCell(0);
				cell.setCellValue(dt.getProyecto());
				
				cell = row.createCell(1);
				cell.setCellValue(dt.getPeriodo());
				
				cell = row.createCell(2);
				cell.setCellValue(dt.getSocioImplementador());
				
				cell = row.createCell(3);
				cell.setCellValue(dt.getSocioEstrategico());
				
				cell = row.createCell(4);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(5);
				cell.setCellValue(dt.getDecimalUno().toString());
				cuentaFila++;
			}

			sheet = workbook.createSheet("INFORMACION_ADICIONAL");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10000);
			sheet.setColumnWidth((short) 3,(short) 10200);
			sheet.setColumnWidth((short) 4,(short) 30000);
			sheet.setColumnWidth((short) 5,(short) 30000);
			sheet.setColumnWidth((short) 6,(short) 30000);
			
			cuentaFila=1;
			row = sheet.createRow(1);			
			cell = row.createCell(0);
			cell.setCellValue("Si considera necesario, ingrese información adicional que aporte a la salvaguarda respectiva");
			cell.setCellStyle(styleBold);
			
			cuentaFila++;
			row = sheet.createRow(cuentaFila);			
			cell = row.createCell(0);
			cell.setCellValue("PROYECTO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue("PERIODO");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(2);
			cell.setCellValue("SOCIO IMPLEMENTADOR");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(3);
			cell.setCellValue("SOCIO ESTRATEGICO");
			cell.setCellStyle(styleBold);

			cell = row.createCell(4);
			cell.setCellValue("Actividad que aporta a la salvaguarda");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Logro alcanzado que se reporta");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaInformacionAdicional(159);
			cuentaFila++;
			for (DtoTableResponses dt : lista) {
				row = sheet.createRow(cuentaFila);			
				cell = row.createCell(0);
				cell.setCellValue(dt.getProyecto());
				
				cell = row.createCell(1);
				cell.setCellValue(dt.getPeriodo());
				
				cell = row.createCell(2);
				cell.setCellValue(dt.getSocioImplementador());
				
				cell = row.createCell(3);
				cell.setCellValue(dt.getSocioEstrategico());
				
				cell = row.createCell(4);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(5);
				cell.setCellValue(dt.getTextoDos());
				
				cell = row.createCell(6);
				cell.setCellValue(dt.getTextoTres());
				cuentaFila++;
			}

			
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_A").append(".xls").toString();
			FileOutputStream file = new FileOutputStream(archivo);
	        workbook.write(file);
	        file.close();
	        
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "","Ocurrio un error al generar el archivo");
			LOG.error(new StringBuilder().append("GenerarBDSalvaguardaA " + "." + "generaArchivoSalvaguardaA" + ": ").append(e.getMessage()));
			e.printStackTrace();
		}

	}
}

