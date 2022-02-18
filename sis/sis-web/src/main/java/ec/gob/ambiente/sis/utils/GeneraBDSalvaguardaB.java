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

import ec.gob.ambiente.sigma.model.Components;
import ec.gob.ambiente.sis.dto.DtoTableResponses;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.Questions;
import ec.gob.ambiente.sis.services.QuestionsFacade;
import ec.gob.ambiente.sis.services.TableResponsesFacade;

public class GeneraBDSalvaguardaB {

	private static final Logger LOG = Logger.getLogger(GeneraBDSalvaguardaB.class);
	public static void generaArchivoSalvaguardaB(TableResponsesFacade servicio, QuestionsFacade servicioPreguntas, List<Catalogs> listaCatalogos,List<Object[]> listaProvincias,List<Object[]> listaCanton,List<Object[]> listaParroquia,List<Components> listaComponentes){
		try{
			ResourceBundle rb;
			rb = ResourceBundle.getBundle("resources.indicadores");
			List<Questions> listaPreguntas = servicioPreguntas.buscaPreguntaPorSalvaguarda(2);
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
			cell.setCellValue(rb.getString("B_6"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(7));
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_8"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(11));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_10"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(servicio.sumaDecimalUno(20).toString()));
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_12"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(20));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(17);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_14"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(69));
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(21);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_18"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

//			row = sheet.createRow(25);
//			cell = row.createCell(3);
//			cell = row.createCell(0);
//			cell.setCellValue(rb.getString("B_19"));
//			cell.setCellStyle(styleBold);
//
//			cell = row.createCell(1);
//			cell.setCellValue(0);
//			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(25);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_23"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(27));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(29);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_25"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.totalActores());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(33);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_26"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(29));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(37);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_27"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroCanalesHabilitados());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(41);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_30"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(31));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(45);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_32"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(servicio.totalPersonasInfoRedd().toString()));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(49);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_34"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
		
			row = sheet.createRow(53);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("B_37"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			
			sheet = workbook.createSheet("PREGUNTA 4.1");
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
			cell.setCellValue("Modalidad");
			cell.setCellStyle(styleBold);

			cell = row.createCell(5);
			cell.setCellValue("Fecha");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Provincia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(7);
			cell.setCellValue("Cantón");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(8);
			cell.setCellValue("Parroquia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Etnia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Nacionalidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Comunidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Nro hombres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Nro mujeres");
			cell.setCellStyle(styleBold);
			
			List<DtoTableResponses> lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_4(listaPreguntas.get(1).getQuesId());
			cuentaFila = 6;
			for (DtoTableResponses dt : lista) {
				Catalogs cata = new Catalogs();
				row = sheet.createRow(cuentaFila);			
				cell = row.createCell(0);
				cell.setCellValue(dt.getProyecto());
				
				cell = row.createCell(1);
				cell.setCellValue(dt.getPeriodo());
				
				cell = row.createCell(2);
				cell.setCellValue(dt.getSocioImplementador());
				
				cell = row.createCell(3);
				cell.setCellValue(dt.getSocioEstrategico());
				
//				for (Catalogs ca : listaCatalogos) {
//					if(ca.getCataId() == dt.getNumeroSeis()){
//						cata = ca;
//						break;
//					}
//				}				
//				cell = row.createCell(4);
//				cell.setCellValue(cata.getCataText2());
				
				String catalogo ="";
				if(dt.getNumeroSeis()>0){				
					for (Catalogs cat : listaCatalogos) {	
						if(cat.getCataId() == dt.getNumeroSeis()){
							catalogo = cat.getCataText2();				
							break;
						}
					}
					cell = row.createCell(4);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(4);
					cell.setCellValue(dt.getTextoDos());
				}
				
				cell = row.createCell(5);
				cell.setCellValue(dt.getFecha());
				catalogo ="";
				for (Object[] objects : listaProvincias) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroUno()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(6);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaCanton) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroDos()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(7);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Object[] objects : listaParroquia) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroTres()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(8);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCuatro()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(9);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroCinco()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(10);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getNumeroSiete());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getNumeroOcho());
				cuentaFila++;
			}
			/////PREGUNTA 5
			sheet = workbook.createSheet("PREGUNTA 5.1");
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
			cell.setCellValue("Institución");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(8);
			cell.setCellValue("Actividades de coordinación");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Link verificador del evento");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Nro hombres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Nro mujeres");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_5(listaPreguntas.get(3).getQuesId());
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
				
								
				cell = row.createCell(7);
				cell.setCellValue(dt.getTextoUno());
				
								
				cell = row.createCell(8);
				cell.setCellValue(dt.getTextoDos());
				
				cell = row.createCell(9);
				cell.setCellValue(dt.getTextoTres());
				
				catalogo ="";
				for (Components c : listaComponentes) {	
					if(c.getCompId() == dt.getNumeroSeis() ){
						catalogo = c.getCompName();				
						break;
					}
				}
				cell = row.createCell(10);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getNumeroCuatro());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getNumeroCinco());
				cuentaFila++;
			}
			
			//// PREGUNTA 6
			sheet = workbook.createSheet("PREGUNTA 6.1");
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
			cell.setCellValue(listaPreguntas.get(4).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(5).getQuesContentQuestion());
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
			cell.setCellValue("Institución");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(7);
			cell.setCellValue("Actividad que realiza la comunidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(8);
			cell.setCellValue("Link verificador del evento");
			cell.setCellStyle(styleBold);
			
			
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_6(listaPreguntas.get(5).getQuesId());
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
														
				cell = row.createCell(6);
				cell.setCellValue(dt.getTextoUno());
												
				cell = row.createCell(7);
				cell.setCellValue(dt.getTextoDos());
				
				cell = row.createCell(8);
				cell.setCellValue(dt.getTextoTres());
			
				cuentaFila++;
			}
			///PREGUNTA 7.1
			sheet = workbook.createSheet("PREGUNTA 7.1");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 10000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10200);
						

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
			cell.setCellValue("Instrumento");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Relevancia o importancia del instrumento");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Mecanismo de Institucionalización");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(7);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
			
			
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_7(listaPreguntas.get(8).getQuesId());
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
				cell.setCellValue(dt.getTextoCuatro());				
				
				cuentaFila++;
			}
			/// pregunta 8.1
			sheet = workbook.createSheet("PREGUNTA 8.1");
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
			sheet.setColumnWidth((short) 10,(short) 10200);
			sheet.setColumnWidth((short) 11,(short) 10200);
			sheet.setColumnWidth((short) 12,(short) 10200);
			sheet.setColumnWidth((short) 13,(short) 10200);
			sheet.setColumnWidth((short) 14,(short) 10200);

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
			cell.setCellValue("Nro Hombres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Nro Mujeres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Fecha");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Actividades");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(14);
			cell.setCellValue("Hectáreas");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_8(listaPreguntas.get(10).getQuesId());
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
				cell.setCellValue(dt.getNumeroSiete());
														
				cell = row.createCell(11);
				cell.setCellValue(dt.getNumeroOcho());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getFecha());
				
				catalogo ="";
				if(dt.getNumeroSeis()>0){				
					for (Catalogs cat : listaCatalogos) {	
						if(cat.getCataId() == dt.getNumeroSeis()){
							catalogo = cat.getCataText2();				
							break;
						}
					}
					cell = row.createCell(13);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(13);
					cell.setCellValue(dt.getTextoDos());
				}
				
				cell = row.createCell(14);
				cell.setCellValue( dt.getDecimalUno().toString());												
				cuentaFila++;
			}
			/// pregunta 9.1
			sheet = workbook.createSheet("PREGUNTA 9.1");
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
			
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(11).getQuesContentQuestion());
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
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_9(listaPreguntas.get(11).getQuesId());
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
				cuentaFila++;
			}
			/// pregunta 10
			sheet = workbook.createSheet("PREGUNTA 10");
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
			sheet.setColumnWidth((short) 10,(short) 10200);
			sheet.setColumnWidth((short) 11,(short) 10200);
			sheet.setColumnWidth((short) 12,(short) 10200);
			sheet.setColumnWidth((short) 13,(short) 10200);
			
			row = sheet.createRow(1);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(12).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(13).getQuesContentQuestion());
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
			cell.setCellValue("Hectareas");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Estado");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Nro Hombres beneficiario");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Nro Mujeres beneficiarias");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_10(listaPreguntas.get(13).getQuesId());
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
				cell.setCellValue(dt.getDecimalUno().toString());
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getNumeroSeis()==1?"Iniciado":dt.getNumeroSeis()==1?"En proceso":"Finalizado");
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getNumeroSiete());

				cell = row.createCell(13);
				cell.setCellValue(dt.getNumeroOcho());
				
				cuentaFila++;
			}
			//pregunta 11
			sheet = workbook.createSheet("PREGUNTA 11");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10200);
			
			row = sheet.createRow(1);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(15).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(16).getQuesContentQuestion());
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
			cell.setCellValue("Cómo funciona ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Casos que se han reportado durante el período de reporte");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Casos que se han atendido durante el período de reporte ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(7);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
						
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_11(listaPreguntas.get(16).getQuesId());
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
				cell.setCellValue(dt.getNumeroUno());
				
				cell = row.createCell(6);
				cell.setCellValue(dt.getNumeroDos());
				
				cell = row.createCell(7);
				cell.setCellValue(dt.getTextoDos());
				
				cuentaFila++;
			}
			///pregunta 12
			sheet = workbook.createSheet("PREGUNTA 12");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10000);
			sheet.setColumnWidth((short) 8,(short) 3200);
			sheet.setColumnWidth((short) 9,(short) 10200);
			sheet.setColumnWidth((short) 10,(short) 10200);
			sheet.setColumnWidth((short) 11,(short) 10200);
			
			row = sheet.createRow(1);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(17).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(18).getQuesContentQuestion());
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
			cell.setCellValue("Nombre de la Organización Beneficiaria. ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Etnia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Nacionalidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(7);
			cell.setCellValue("Acciones implementadas");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(8);
			cell.setCellValue("Conformación de la organización");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Nro hombres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Nro mujeres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_12(listaPreguntas.get(18).getQuesId());
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
				
				String catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroUno()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(5);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroDos()){
						catalogo = cat.getCataText2();				
						break;
					}
				}
				cell = row.createCell(6);
				cell.setCellValue(catalogo);
				
//				catalogo ="";
//				for (Catalogs cat : listaCatalogos) {	
//					if(cat.getCataId() == dt.getNumeroCuatro()){
//						catalogo = cat.getCataText2();				
//						break;
//					}
//				}
//				cell = row.createCell(7);
//				cell.setCellValue(catalogo);
				
				catalogo ="";
				if(dt.getNumeroCuatro()>0){				
					for (Catalogs cat : listaCatalogos) {	
						if(cat.getCataId() == dt.getNumeroCuatro()){
							catalogo = cat.getCataText2();				
							break;
						}
					}
					cell = row.createCell(7);
					cell.setCellValue(catalogo);
				}else{
					cell = row.createCell(7);
					cell.setCellValue(dt.getTextoTres());
				}
				
				cell = row.createCell(8);
				cell.setCellValue(dt.getNumeroTres()==1?"Hombres":dt.getNumeroTres()==2?"Mujeres":"Mixta");
				
				cell = row.createCell(9);
				cell.setCellValue(dt.getNumeroCinco());
				
				cell = row.createCell(10);
				cell.setCellValue(dt.getNumeroSeis());
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getTextoDos());
				
				cuentaFila++;
			}
			///pregunta 13
			sheet = workbook.createSheet("PREGUNTA 13");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10000);
			sheet.setColumnWidth((short) 8,(short) 3200);
			sheet.setColumnWidth((short) 9,(short) 3200);
			sheet.setColumnWidth((short) 10,(short) 3200);
			
			row = sheet.createRow(1);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(19).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(20).getQuesContentQuestion());
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
			cell.setCellValue("Fecha");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Actividad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Temática tratada");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(7);
			cell.setCellValue("Nro de personas que acceden a la información");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(8);
			cell.setCellValue("Provincia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_13(listaPreguntas.get(20).getQuesId());
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
				cell.setCellValue(dt.getFecha());
				
				String catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroDos()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(5);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(6);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(7);
				cell.setCellValue(dt.getNumeroTres());
				
				catalogo ="";
				for (Object[] objects : listaProvincias) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroUno()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(8);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Components c : listaComponentes) {	
					if(c.getCompId() == dt.getNumeroCuatro() ){
						catalogo = c.getCompName();				
						break;
					}
				}
				cell = row.createCell(9);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(10);
				cell.setCellValue(dt.getTextoDos());
				
				cuentaFila++;
			}

			///pregunta 14
			sheet = workbook.createSheet("PREGUNTA 14");
			sheet.setColumnWidth((short) 0,(short) 10200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 10000);
			sheet.setColumnWidth((short) 4,(short) 3200);
			sheet.setColumnWidth((short) 5,(short) 10200);
			sheet.setColumnWidth((short) 6,(short) 10200);
			sheet.setColumnWidth((short) 7,(short) 10000);
			sheet.setColumnWidth((short) 8,(short) 3200);
			sheet.setColumnWidth((short) 9,(short) 10000);
			sheet.setColumnWidth((short) 10,(short) 3200);
			sheet.setColumnWidth((short) 11,(short) 3200);
			
			row = sheet.createRow(1);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(21).getQuesContentQuestion());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(3);			
			cell = row.createCell(0);
			cell.setCellValue(listaPreguntas.get(22).getQuesContentQuestion());
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
			cell.setCellValue("Fecha");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(5);
			cell.setCellValue("Comunidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(6);
			cell.setCellValue("Número de personas");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(7);
			cell.setCellValue("¿Qué información se comunica a los beneficiarios?");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(8);
			cell.setCellValue("¿Cómo se informa a la gente sobre la ejecución del proyecto/programa?");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Provincia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
			
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_B_14(listaPreguntas.get(22).getQuesId());
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
				cell.setCellValue(dt.getFecha());
				
				cell = row.createCell(5);
				cell.setCellValue(dt.getTextoUno());
				
				cell = row.createCell(6);
				cell.setCellValue(dt.getNumeroDos());
				
				cell = row.createCell(7);
				cell.setCellValue(dt.getTextoDos());
				
				cell = row.createCell(8);
				cell.setCellValue(dt.getTextoTres());
				
				String catalogo ="";
				for (Object[] objects : listaProvincias) {	
					if(Integer.valueOf( objects[1].toString()) == dt.getNumeroUno()){
						catalogo = objects[0].toString();					
						break;
					}
				}				
				cell = row.createCell(9);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Components c : listaComponentes) {	
					if(c.getCompId() == dt.getNumeroTres() ){
						catalogo = c.getCompName();				
						break;
					}
				}
				cell = row.createCell(10);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(11);
				cell.setCellValue(dt.getTextoCuatro());
				
				cuentaFila++;
			}
			//INFORMACION ADICIONAL
			sheet = workbook.createSheet("INFORMACION_ADICIONAL");
			sheet.setColumnWidth((short) 0,(short) 30200);
			sheet.setColumnWidth((short) 1,(short) 5000);
			sheet.setColumnWidth((short) 2,(short) 10200);
			sheet.setColumnWidth((short) 3,(short) 30000);
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
			lista = servicio.listaInformacionAdicional(160);
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
			String archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_B").append(".xls").toString();
			FileOutputStream file = new FileOutputStream(archivo);
	        workbook.write(file);
	        file.close();
		}catch(Exception e){
			Mensaje.verMensaje(FacesMessage.SEVERITY_ERROR, "","Ocurrio un error al generar el archivo");
			LOG.error(new StringBuilder().append("GeneraBDSalvaguardaB " + "." + "generaArchivoSalvaguardaB" + ": ").append(e.getMessage()));
		}
	}	
}

