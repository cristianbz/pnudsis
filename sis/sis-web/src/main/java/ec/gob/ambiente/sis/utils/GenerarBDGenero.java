/**
@autor proamazonia [Christian Báez]  31 ene. 2022

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
import ec.gob.ambiente.sis.services.AdvanceExecutionProjectGenderFacade;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;

public class GenerarBDGenero {
	private static final Logger LOG = Logger.getLogger(GenerarBDGenero.class);
	public static void generaArchivoGenero(TableResponsesFacade servicioGenero, AdvanceExecutionProjectGenderFacade servicio, QuestionsFacade servicioPreguntas, List<Catalogs> listaCatalogos,List<Object[]> listaProvincias,List<Object[]> listaCanton,List<Object[]> listaParroquia,List<Components> listaComponentes){
		try{
			ResourceBundle rb;
			rb = ResourceBundle.getBundle("resources.indicadores");
			List<Questions> listaPreguntas = servicioPreguntas.buscaPreguntasGenero();
			int cuentaFila = 0;
			
			@SuppressWarnings("resource")
			HSSFWorkbook workbook = new HSSFWorkbook();			
			HSSFFont bold = workbook.createFont();
			bold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle styleBold = workbook.createCellStyle();
			styleBold.setFont( bold );
			
			HSSFSheet sheet = workbook.createSheet("INDICADORES");
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
			sheet.setColumnWidth((short) 11,(short) 10200);
			sheet.setColumnWidth((short) 12,(short) 10200);
			sheet.setColumnWidth((short) 13,(short) 10200);
			sheet.setColumnWidth((short) 14,(short) 10200);
			sheet.setColumnWidth((short) 15,(short) 5200);
			sheet.setColumnWidth((short) 16,(short) 5200);
			
			HSSFRow row = sheet.createRow(5);			
			HSSFCell cell = row.createCell(0);
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
			cell.setCellValue("Temas");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Línea de acción ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Indicador");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(7);
			cell.setCellValue("Meta");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(8);
			cell.setCellValue("Valor esperado(Nro mujeres)");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Valor esperado(Nro hombres)");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Valor esperado (SI/NO)");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Valor esperado");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Valor alcanzado (Nro mujeres)");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Valor alcanzado (Nro hombres)");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Valor alcanzado (SI/NO)");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(15);
			cell.setCellValue("Valor alcanzado");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(16);
			cell.setCellValue("Acciones implementadas");
			cell.setCellStyle(styleBold);
												
			List<DtoTableResponses> lista = new ArrayList<>();
			lista = servicio.listaResumenIndicadoresGenero();
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
				cell.setCellValue(dt.getTextoDos());
				
				cell = row.createCell(6);
				cell.setCellValue(dt.getTextoTres());
				
				cell = row.createCell(7);
				cell.setCellValue(dt.getTextoSiete());
				
				cell = row.createCell(8);
				if(dt.getTextoSeis().equals("B"))
					cell.setCellValue("N.A");
				else
					cell.setCellValue(dt.getNumeroTres());
				
				cell = row.createCell(9);
				if(dt.getTextoSeis().equals("B"))
					cell.setCellValue("N.A");
				else
					cell.setCellValue(dt.getNumeroCuatro());
				
				cell = row.createCell(10);
				if(dt.getTextoSeis().equals("B") && dt.getNumeroTres()== 1)
					cell.setCellValue("SI");
				else if(dt.getTextoSeis().equals("B") && dt.getNumeroTres()== 0)
					cell.setCellValue("NO");
				else 
					cell.setCellValue("N.A");
				
				cell = row.createCell(11);
				if(dt.getTextoSeis().equals("N"))					
					cell.setCellValue(dt.getNumeroTres());
				else
					cell.setCellValue("N.A");
								
				cell = row.createCell(12);
				if(dt.getTextoSeis().equals("B"))
					cell.setCellValue("N.A");
				else
					cell.setCellValue(dt.getNumeroUno());
				
				cell = row.createCell(13);
				if(dt.getTextoSeis().equals("B"))
					cell.setCellValue("N.A");
				else
					cell.setCellValue(dt.getNumeroDos());
				
				cell = row.createCell(14);
				if(dt.getTextoSeis().equals("B") && dt.getNumeroUno()== 1)
					cell.setCellValue("SI");
				else if(dt.getTextoSeis().equals("B") && dt.getNumeroUno()== 0)
					cell.setCellValue("NO");
				else 
					cell.setCellValue("N.A");
							
				cell = row.createCell(15);
				if(dt.getTextoSeis().equals("N"))					
					cell.setCellValue(dt.getNumeroUno());
				else
					cell.setCellValue("N.A");
				
				cell = row.createCell(16);
				cell.setCellValue(dt.getTextoCuatro());
				
				cuentaFila++;
			}
			///PREGUNTA 47.1
			sheet = workbook.createSheet("PREGUNTA 1");
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
			cell.setCellValue(listaPreguntas.get(0).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
//			row = sheet.createRow(3);			
//			cell = row.createCell(0);
//			cell.setCellValue(listaPreguntas.get(3).getQuesContentQuestion());
//			cell.setCellStyle(styleBold);
			
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
			cell.setCellValue("Espacio identificado ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Tipo de organización");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Fin / rol de la organización");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Acciones implementadas para fortalecer a la organización");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Nro Hombres beneficiarios");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(15);
			cell.setCellValue("Nro Mujeres beneficiarias");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(16);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
												
			lista = new ArrayList<>();
			lista = servicioGenero.listaPreguntas_Genero(listaPreguntas.get(0).getQuesId());
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
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getTextoTres());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getTextoCuatro());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getTextoCinco());
				
				cell = row.createCell(14);
				cell.setCellValue(dt.getNumeroUno());
				
				cell = row.createCell(15);
				cell.setCellValue(dt.getNumeroSeis());
				
				cell = row.createCell(16);
				cell.setCellValue(dt.getTextoSeis());
											
				cuentaFila++;
			}
			///PREGUNTA 2
			sheet = workbook.createSheet("PREGUNTA 2");
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
			cell.setCellValue(listaPreguntas.get(1).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
//			row = sheet.createRow(3);			
//			cell = row.createCell(0);
//			cell.setCellValue(listaPreguntas.get(3).getQuesContentQuestion());
//			cell.setCellStyle(styleBold);
			
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
			cell.setCellValue("Nombre de la lideresa identificada");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Espacio de Diálogo/Participación");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Rol que cumple en el espacio de diálogo/participación");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Acciones de fortalecimiento de la lideresa en el espacio de diálogo/participación");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
												
			lista = new ArrayList<>();
			lista = servicioGenero.listaPreguntas_Genero(listaPreguntas.get(1).getQuesId());
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
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getTextoTres());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getTextoCuatro());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getTextoCinco());
									
				cell = row.createCell(14);
				cell.setCellValue(dt.getTextoSeis());
											
				cuentaFila++;
			}
			///PREGUNTA 3
			sheet = workbook.createSheet("PREGUNTA 3");
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
			
			
//			row = sheet.createRow(3);			
//			cell = row.createCell(0);
//			cell.setCellValue(listaPreguntas.get(3).getQuesContentQuestion());
//			cell.setCellStyle(styleBold);
			
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
			cell.setCellValue("Acción");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Resultado");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Nro Mujeres beneficiarias");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Nro Hombres beneficiarios");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
												
			lista = new ArrayList<>();
			lista = servicioGenero.listaPreguntas_Genero(listaPreguntas.get(2).getQuesId());
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
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getTextoTres());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getNumeroSeis());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getNumeroSiete());
									
				cell = row.createCell(14);
				cell.setCellValue(dt.getTextoCuatro());
											
				cuentaFila++;
			}

			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			String archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_GENERO").append(".xls").toString();
			FileOutputStream file = new FileOutputStream(archivo);
	        workbook.write(file);
	        file.close();
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "","Ocurrio un error al generar el archivo");
			LOG.error(new StringBuilder().append("GenerarBDGenero " + "." + "generaArchivoGenero" + ": ").append(e.getMessage()));
		}
	}	

}

