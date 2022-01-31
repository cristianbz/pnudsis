/**
@autor proamazonia [Christian Báez]  24 ene. 2022

**/
package ec.gob.ambiente.sis.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

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

public class GenerarBDSalvaguardaD {

	public static void generaArchivoSalvaguardaD(TableResponsesFacade servicio, QuestionsFacade servicioPreguntas, List<Catalogs> listaCatalogos,List<Object[]> listaProvincias,List<Object[]> listaCanton,List<Object[]> listaParroquia,List<Components> listaComponentes){
		try{
			ResourceBundle rb;
			rb = ResourceBundle.getBundle("resources.indicadores");
			List<Questions> listaPreguntas = servicioPreguntas.buscaPreguntaPorSalvaguarda(16);
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
			cell.setCellValue(rb.getString("D_58"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(65));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(5);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_61"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.totalActoresDialogo());
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(9);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_63"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);

			row = sheet.createRow(13);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_64"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(0);
			cell.setCellStyle(styleBold);
			
			row = sheet.createRow(17);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_67"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(Double.parseDouble(servicio.totalMujeresDialogo().toString()));
			cell.setCellStyle(styleBold);

			row = sheet.createRow(22);
			cell = row.createCell(3);
			cell = row.createCell(0);
			cell.setCellValue(rb.getString("D_69"));
			cell.setCellStyle(styleBold);

			cell = row.createCell(1);
			cell.setCellValue(servicio.numeroDeRegistros(67));
			cell.setCellStyle(styleBold);
			
			///PREGUNTA 32.1
			sheet = workbook.createSheet("PREGUNTA 32.1");
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
			cell.setCellValue("Espacio de difusión de la información ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Asistentes mujeres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Asistentes hombres");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(13);
			cell.setCellValue("Actores que participan en el proceso de acceso a la información ");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(14);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(15);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
									
			List<DtoTableResponses> lista = new ArrayList<>();
			lista = servicio.listaPreguntas_D_32(listaPreguntas.get(1).getQuesId());
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
				cell.setCellValue(dt.getNumeroSeis());
				
				cell = row.createCell(12);
				cell.setCellValue(dt.getNumeroSiete());
				
				cell = row.createCell(13);
				cell.setCellValue(dt.getTextoTres());
				
				
				catalogo ="";
				for (Components c : listaComponentes) {	
					if(c.getCompId() == dt.getNumeroOcho() ){
						catalogo = c.getCompName();				
						break;
					}
				}
				cell = row.createCell(14);
				cell.setCellValue(catalogo);
												
				cell = row.createCell(15);
				cell.setCellValue(dt.getTextoCuatro());
				
				cuentaFila++;
			}
			
			///PREGUNTA 33.1
			sheet = workbook.createSheet("PREGUNTA 33.1");
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
			cell.setCellValue("Actor/Organización");
			cell.setCellStyle(styleBold);
						
			cell = row.createCell(6);
			cell.setCellValue("Etnia");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(7);
			cell.setCellValue("Nacionalidad");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(8);
			cell.setCellValue("Presupuesto asignado a la actividad ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(9);
			cell.setCellValue("Actividad que realiza la comunidad ");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(10);
			cell.setCellValue("Nivel de involucramiento");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(11);
			cell.setCellValue("Componente");
			cell.setCellStyle(styleBold);
			
			cell = row.createCell(12);
			cell.setCellValue("Link verificador");
			cell.setCellStyle(styleBold);
									
			lista = new ArrayList<>();
			lista = servicio.listaPreguntas_D_33(listaPreguntas.get(3).getQuesId());
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

				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroDos()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(6);
				cell.setCellValue(catalogo);
				
				catalogo ="";
				for (Catalogs cat : listaCatalogos) {	
					if(cat.getCataId() == dt.getNumeroTres()){
						catalogo = cat.getCataText2();				
						break;
					}
				}				
				cell = row.createCell(7);
				cell.setCellValue(catalogo);
				
				cell = row.createCell(8);
				cell.setCellValue(dt.getDecimalUno().toString());
				
				cell = row.createCell(9);
				cell.setCellValue(dt.getTextoDos());
				
				cell = row.createCell(10);
				cell.setCellValue(dt.getTextoTres());
				
				
				catalogo ="";
				for (Components c : listaComponentes) {	
					if(c.getCompId() == dt.getNumeroCuatro() ){
						catalogo = c.getCompName();				
						break;
					}
				}
				cell = row.createCell(11);
				cell.setCellValue(catalogo);
												
				cell = row.createCell(12);
				cell.setCellValue(dt.getTextoCuatro());
				
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
			lista = servicio.listaInformacionAdicional(162);
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
			String archivo = new StringBuilder().append(ctx.getRealPath("")).append(File.separator).append("reportes").append(File.separator).append("BDSIS_SALV_D").append(".xls").toString();
			FileOutputStream file = new FileOutputStream(archivo);
	        workbook.write(file);
	        file.close();
//	        Mensaje.verMensaje(FacesMessage.SEVERITY_INFO, "",getMensajesController().getPropiedad("info.generaArchivo"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}	
}



