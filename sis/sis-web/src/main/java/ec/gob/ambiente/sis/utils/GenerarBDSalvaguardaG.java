/**
@autor proamazonia [Christian Báez]  28 ene. 2022

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

import ec.gob.ambiente.sigma.model.Components;
import ec.gob.ambiente.sis.dto.DtoTableResponses;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;

public class GenerarBDSalvaguardaG {
	private static final Logger LOG = Logger.getLogger(GenerarBDSalvaguardaG.class);
	public static void generaArchivoSalvaguardaG(TableResponsesFacade servicio, QuestionsFacade servicioPreguntas, List<Catalogs> listaCatalogos,List<Object[]> listaProvincias,List<Object[]> listaCanton,List<Object[]> listaParroquia,List<Components> listaComponentes){
		try{
			ResourceBundle rb;
			rb = ResourceBundle.getBundle("resources.indicadores");
			List<Questions> listaPreguntas = servicioPreguntas.buscaPreguntaPorSalvaguarda(29);
			int cuentaFila = 0;
			
			@SuppressWarnings("resource")
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
			cell.setCellValue(rb.getString("G_93"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.totalActoresMonitoreo());
			cell.setCellStyle(styleBold);

			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_94"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_97"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.totalBeneficiariosAlternativasEconomicas());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_100"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(servicio.totalPresupuestoSNMB().toString()));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("G_102"));
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(136));
			cell.setCellStyle(styleBold);
			
			///PREGUNTA 46.1
			sheet = workbook.createSheet("PREGUNTA 46.1");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10200);
			sheet.setColumnWidth((short) 8,(short) 10200);
			sheet.setColumnWidth((short) 9,(short) 10200);
			sheet.setColumnWidth((short) 10,(short) 5200);
			
			row = sheet.createRow(1);
			cell = row.createCell(0);			
			cell.setCellValue(listaPreguntas.get(0).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(1).getQuesContentQuestion());
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
			cell.setCellValue("Provincia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Institución responsable ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Autores de la investigación");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(7);
			cell.setCellValue("Nombre del estudio");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(8);
			cell.setCellValue("Riesgo Principal Identificado");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(9);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
												
			List<DtoTableResponses> lista = new ArrayList<>();
			lista = servicio.listaPreguntas_F_41_1(listaPreguntas.get(1).getQuesId());
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
				
				String catalogo ="";
				for (Object[] objects : listaProvincias) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroUno()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(4);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(5);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(6);
				cell.setCellValue(dt.getTextoDos());
				
				cell = row.createCell(7);
				cell.setCellValue(dt.getTextoTres());
				
//				catalogo ="";
//				for (Catalogs cat : listaCatalogos) {	
//					if(cat.getCataId() == dt.getNumeroSeis()){
//						catalogo = cat.getCataText2();				
//						break;
//					}
//				}				
//				cell = row.createCell(8);
//				cell.setCellValue(catalogo);
				
				catalogo ="";
				if(dt.getNumeroSeis()>0){				
					for (Catalogs cat : listaCatalogos) {	
						if(cat.getCataId() == dt.getNumeroSeis()){
							catalogo = cat.getCataText2();				
							break;
						}
					}
					cell = row.createCell(8);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(8);
					cell.setCellValue(dt.getTextoCinco());
				}
				
				if(dt.getNumeroSiete() != 1000){
					catalogo ="";
					for (Components c : listaComponentes) {	
						if(c.getCompId() == dt.getNumeroSiete() ){
							catalogo = c.getCompName();				
							break;
						}
					}
					cell = row.createCell(9);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(9);
					cell.setCellValue("Componentes operativos");
				}
				
				cell = row.createCell(10);
				cell.setCellValue(dt.getTextoCuatro());								
				cuentaFila++;
			}
			///PREGUNTA 47.1
			sheet = workbook.createSheet("PREGUNTA 47.1");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10200);
			sheet.setColumnWidth((short) 8,(short) 10200);
			sheet.setColumnWidth((short) 9,(short) 10200);
			sheet.setColumnWidth((short) 10,(short) 5200);
			sheet.setColumnWidth((short) 11,(short) 5200);
			sheet.setColumnWidth((short) 12,(short) 5200);
			sheet.setColumnWidth((short) 13,(short) 5200);
			sheet.setColumnWidth((short) 14,(short) 5200);
			
			row = sheet.createRow(1);
			cell = row.createCell(0);			
			cell.setCellValue(listaPreguntas.get(2).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(3).getQuesContentQuestion());
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
			cell.setCellValue("Provincia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Cantón");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Parroquia");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(7);
			cell.setCellValue("Etnia");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(8);
			cell.setCellValue("Nacionalidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Comunidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Actividad ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Nro hombres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Nro mujeres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Fecha");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
			
												
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_F_41_1(listaPreguntas.get(3).getQuesId());
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
				
				String catalogo ="";
				for (Object[] objects : listaProvincias) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroUno()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(4);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaCanton) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroDos()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(5);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaParroquia) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroTres()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(6);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCuatro()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(7);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCinco()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(8);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(9);
				cell.setCellValue(dt.getTextoUno());
				
//				catalogo ="";
//				for (Catalogs cat : listaCatalogos) {	
//					if(cat.getCataId() == dt.getNumeroSeis()){
//						catalogo = cat.getCataText2();				
//						break;
//					}
//				}				
//				cell = row.createCell(10);
//				cell.setCellValue(catalogo);
				
				catalogo ="";
				if(dt.getNumeroSeis()>0){				
					for (Catalogs cat : listaCatalogos) {	
						if(cat.getCataId() == dt.getNumeroSeis()){
							catalogo = cat.getCataText2();				
							break;
						}
					}
					cell = row.createCell(10);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(10);
					cell.setCellValue(dt.getTextoCinco());
				}
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getNumeroOcho());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getNumeroNueve());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getFecha());
				
				
				if(dt.getNumeroSiete() != 1000){
					catalogo ="";
					for (Components c : listaComponentes) {	
						if(c.getCompId() == dt.getNumeroSiete() ){
							catalogo = c.getCompName();				
							break;
						}
					}
					cell = row.createCell(14);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(14);
					cell.setCellValue("Componentes operativos");
				}
											
				cuentaFila++;
			}
			///PREGUNTA 48.1
			sheet = workbook.createSheet("PREGUNTA 48.1");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10200);
			sheet.setColumnWidth((short) 8,(short) 10200);
			sheet.setColumnWidth((short) 9,(short) 10200);
			sheet.setColumnWidth((short) 10,(short) 5200);
			sheet.setColumnWidth((short) 11,(short) 5200);
			sheet.setColumnWidth((short) 12,(short) 5200);
			sheet.setColumnWidth((short) 13,(short) 5200);
			sheet.setColumnWidth((short) 14,(short) 5200);
			
			row = sheet.createRow(1);
			cell = row.createCell(0);			
			cell.setCellValue(listaPreguntas.get(5).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(6).getQuesContentQuestion());
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
			cell.setCellValue("Provincia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Cantón");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Parroquia");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(7);
			cell.setCellValue("Etnia");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(8);
			cell.setCellValue("Nacionalidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Comunidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Institución acompaña ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Actividad ilicita reportada");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Fecha");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Resultado");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
			
												
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_F_41_1(listaPreguntas.get(6).getQuesId());
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
				
				String catalogo ="";
				for (Object[] objects : listaProvincias) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroUno()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(4);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaCanton) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroDos()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(5);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaParroquia) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroTres()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(6);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCuatro()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(7);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCinco()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(8);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(9);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(10);
				cell.setCellValue(dt.getTextoDos());
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroSeis()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(11);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getFecha());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getTextoTres());
				
				cell = row.createCell(14);
				cell.setCellValue(dt.getTextoCuatro());
				
												
				cuentaFila++;
			}
			///PREGUNTA 49.1
			sheet = workbook.createSheet("PREGUNTA 49.1");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10200);
			sheet.setColumnWidth((short) 8,(short) 10200);
			sheet.setColumnWidth((short) 9,(short) 10200);
			sheet.setColumnWidth((short) 10,(short) 5200);
			sheet.setColumnWidth((short) 11,(short) 5200);
			sheet.setColumnWidth((short) 12,(short) 5200);
			sheet.setColumnWidth((short) 13,(short) 5200);
			sheet.setColumnWidth((short) 14,(short) 5200);
			sheet.setColumnWidth((short) 15,(short) 5200);
			sheet.setColumnWidth((short) 16,(short) 5200);
			
			row = sheet.createRow(1);
			cell = row.createCell(0);			
			cell.setCellValue(listaPreguntas.get(7).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(8).getQuesContentQuestion());
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
			cell.setCellValue("Provincia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Cantón");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Parroquia");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(7);
			cell.setCellValue("Etnia");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(8);
			cell.setCellValue("Nacionalidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Comunidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Organización Beneficiaria ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Tipo de incentivo");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Nro Hombres beneficiarios");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Nro Mujeres beneficiarias");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Support value chain ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(15);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(16);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
												
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_F_41_1(listaPreguntas.get(8).getQuesId());
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
				
				String catalogo ="";
				for (Object[] objects : listaProvincias) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroUno()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(4);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaCanton) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroDos()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(5);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaParroquia) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroTres()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(6);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCuatro()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(7);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCinco()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(8);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(9);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(10);
				cell.setCellValue(dt.getTextoDos());
				
//				catalogo ="";
//				for (Catalogs cat : listaCatalogos) {	
//					if(cat.getCataId() == dt.getNumeroSeis()){
//						catalogo = cat.getCataText2();				
//						break;
//					}
//				}				
//				cell = row.createCell(11);
//				cell.setCellValue(catalogo);
				
				catalogo ="";
				if(dt.getNumeroSeis()>0){				
					for (Catalogs cat : listaCatalogos) {	
						if(cat.getCataId() == dt.getNumeroSeis()){
							catalogo = cat.getCataText2();				
							break;
						}
					}
					cell = row.createCell(11);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(11);
					cell.setCellValue(dt.getTextoCinco());
				}
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getNumeroOcho());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getNumeroNueve());
				
				cell = row.createCell(14);
				cell.setCellValue(dt.getTextoTres());
				
				
				if(dt.getNumeroSiete() != 1000){
					catalogo ="";
					for (Components c : listaComponentes) {	
						if(c.getCompId() == dt.getNumeroSiete() ){
							catalogo = c.getCompName();				
							break;
						}
					}
					cell = row.createCell(15);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(15);
					cell.setCellValue("Componentes operativos");
				}
				cell = row.createCell(16);
				cell.setCellValue(dt.getTextoCuatro());							
				cuentaFila++;
			}
			///PREGUNTA 50.1
			sheet = workbook.createSheet("PREGUNTA 50.1");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10200);
			sheet.setColumnWidth((short) 8,(short) 10200);
			
			row = sheet.createRow(1);
			cell = row.createCell(0);			
			cell.setCellValue(listaPreguntas.get(9).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(10).getQuesContentQuestion());
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
			cell.setCellValue("Actividad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Presupuesto");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Resultado");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(7);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(8);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
			
												
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_F_41_1(listaPreguntas.get(10).getQuesId());
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
				
				cell = row.createCell(5);
				cell.setCellValue(dt.getDecimalUno().toString());
				
				cell = row.createCell(6);
				cell.setCellValue(dt.getTextoDos());
				
				String catalogo ="";
				if(dt.getNumeroSiete() != 1000){
					catalogo ="";
					for (Components c : listaComponentes) {	
						if(c.getCompId() == dt.getNumeroSiete() ){
							catalogo = c.getCompName();				
							break;
						}
					}
					cell = row.createCell(7);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(7);
					cell.setCellValue("Componentes operativos");
				}
				cell = row.createCell(8);
				cell.setCellValue(dt.getTextoTres());							
				cuentaFila++;
			}
			///PREGUNTA 51.1
			sheet = workbook.createSheet("PREGUNTA 51.1");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10200);
			sheet.setColumnWidth((short) 8,(short) 10200);
			sheet.setColumnWidth((short) 9,(short) 10200);
			sheet.setColumnWidth((short) 10,(short) 5200);
			sheet.setColumnWidth((short) 11,(short) 5200);
			sheet.setColumnWidth((short) 12,(short) 5200);
			sheet.setColumnWidth((short) 13,(short) 5200);
			sheet.setColumnWidth((short) 14,(short) 5200);
			sheet.setColumnWidth((short) 15,(short) 5200);

			row = sheet.createRow(1);
			cell = row.createCell(0);			
			cell.setCellValue(listaPreguntas.get(11).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(12).getQuesContentQuestion());
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
			cell.setCellValue("Provincia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Cantón");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Parroquia");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(7);
			cell.setCellValue("Etnia");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(8);
			cell.setCellValue("Nacionalidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Comunidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Nro hombres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Nro mujeres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Monitoreo remoto");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Monitoreo in situ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Periodicidad ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(15);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
												
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_F_41_1(listaPreguntas.get(12).getQuesId());
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
				
				String catalogo ="";
				for (Object[] objects : listaProvincias) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroUno()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(4);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaCanton) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroDos()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(5);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaParroquia) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroTres()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(6);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCuatro()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(7);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCinco()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(8);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(9);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(10);
				cell.setCellValue(dt.getNumeroSeis());
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getNumeroOcho());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getTextoDos());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getTextoTres());
				
				cell = row.createCell(14);
				cell.setCellValue(dt.getTextoCuatro());
				
				if(dt.getNumeroSiete() != 1000){
					catalogo ="";
					for (Components c : listaComponentes) {	
						if(c.getCompId() == dt.getNumeroSiete() ){
							catalogo = c.getCompName();				
							break;
						}
					}
					cell = row.createCell(15);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(15);
					cell.setCellValue("Componentes operativos");
				}
											
				cuentaFila++;
			}
			
			///PREGUNTA 51.3
			sheet = workbook.createSheet("PREGUNTA 51.3");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 10000);
						
			row = sheet.createRow(1);
			cell = row.createCell(0);			
			cell.setCellValue(listaPreguntas.get(13).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(14).getQuesContentQuestion());
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
			cell.setCellValue("Link de acceso");
			cell.setCellStyle(styleBold);
			
												
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_E_37_2(listaPreguntas.get(14).getQuesId());
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
			
			//INFORMACION ADICIONAL
			sheet = workbook.createSheet("INFORMACION_ADICIONAL");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 10000);
			sheet.setColumnWidth((short) 5,(short) 10000);
			sheet.setColumnWidth((short) 6,(short) 10000);
			
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
			lista = servicio.listaInformacionAdicional(165);
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
			String archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_G").append(".xls").toString();
			FileOutputStream file = new FileOutputStream(archivo);
	        workbook.write(file);
	        file.close();
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "","Ocurrio un error al generar el archivo");
			LOG.error(new StringBuilder().append("GenerarBDSalvaguardaG " + "." + "generaArchivoSalvaguardaG" + ": ").append(e.getMessage()));
		}
	}	
}





