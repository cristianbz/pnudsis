/**
@autor proamazonia [Christian Báez]  20 may. 2021

 **/
package ec.gob.ambiente.sis.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import ec.gob.ambiente.sis.bean.SeguimientoGeneroBean;
import ec.gob.ambiente.sis.bean.SeguimientoSalvaguardaBean;
import ec.gob.ambiente.sis.model.AdvanceExecutionProjectGender;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.sis.model.TableResponses;

public class ResumenPDF {

	public static void reporteSalvaguardaVacio(String directorioArchivoPDF, SeguimientoSalvaguardaBean seguimientoSalvaguardas){
		try{
			Document document = new Document();
			document.setPageSize(PageSize.A4.rotate());
			document.setMargins(35, 35, 65, 88);//iz dere arriba abajo
			document.setMarginMirroring(true);

			PdfWriter writer =PdfWriter.getInstance(document, new FileOutputStream(directorioArchivoPDF));
			Rectangle rect = new Rectangle(100, 30, 500, 800);
			writer.setBoxSize("art", rect);
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
//			event.valoresPiePagina("", "Pag: ");
			writer.setPageEvent(event);
			document.open();

			Font fontContenido = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
			Font fontTitulos = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 7);
			Font fontTitulosSalvaguardas = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 12);
			fontTitulosSalvaguardas.setColor(13, 165, 196);

			Font fontCabeceraTabla = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
			Font fontContenidoTablas = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);


			Paragraph salvaguardaA = new Paragraph();
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaA.add(new Phrase("SALVAGUARDA A", fontTitulosSalvaguardas));
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));
			salvaguardaA.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasA().get(1).getQuesContentQuestion(), fontTitulos));
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));
			salvaguardaA.add(new Phrase("Marco Jurídico Internacional", fontTitulos));
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaA);

			salvaguardaA = new Paragraph();
			salvaguardaA.add(new Phrase("Marco Jurídico Nacional", fontTitulos));
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaA);


			salvaguardaA = new Paragraph();
			salvaguardaA.add(new Phrase("Normativa Secundaria Nacional", fontTitulos));
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaA);


			Paragraph politica = new Paragraph();
			politica.setIndentationLeft(20);
			salvaguardaA = new Paragraph();
			salvaguardaA.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasA().get(3).getQuesContentQuestion(), fontTitulos));
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaA);

			salvaguardaA = new Paragraph();
			salvaguardaA.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasA().get(4).getQuesContentQuestion(), fontTitulos));
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));
			salvaguardaA.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaA);

			PdfPTable tabla1A = new PdfPTable(new float[] { 7, 3 });
			tabla1A.setWidthPercentage(100);
			tabla1A.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabla1A.getDefaultCell().setPadding(3);
			tabla1A.getDefaultCell().setUseAscender(true);
			tabla1A.getDefaultCell().setUseDescender(true);
			tabla1A.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tabla1A.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			Paragraph encabezadoTablaA=new Paragraph();				
			encabezadoTablaA.add(new Phrase("Plan/Proyecto del Gobierno Nacional",fontCabeceraTabla));
			tabla1A.addCell(encabezadoTablaA);
			encabezadoTablaA=new Paragraph();	
			encabezadoTablaA.add(new Phrase("Presupuesto asignado",fontCabeceraTabla));
			tabla1A.addCell(encabezadoTablaA);


			document.add(tabla1A);

			Paragraph informacionOpcional = new Paragraph();
			informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			document.add(informacionOpcional);

			PdfPTable tabla2A = new PdfPTable(new float[] { 7, 7,7 });
			tabla2A.setWidthPercentage(100);
			tabla2A.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabla2A.getDefaultCell().setPadding(3);
			tabla2A.getDefaultCell().setUseAscender(true);
			tabla2A.getDefaultCell().setUseDescender(true);
			tabla2A.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tabla2A.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaA=new Paragraph();				
			encabezadoTablaA.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
			tabla2A.addCell(encabezadoTablaA);
			encabezadoTablaA=new Paragraph();	
			encabezadoTablaA.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
			tabla2A.addCell(encabezadoTablaA);
			encabezadoTablaA=new Paragraph();	
			encabezadoTablaA.add(new Phrase("Link verificador",fontCabeceraTabla));
			tabla2A.addCell(encabezadoTablaA);

			document.add(tabla2A);

			//SALVAGUARDA B

			Paragraph salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaB.add(new Phrase("SALVAGUARDA B", fontTitulosSalvaguardas));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(0).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(0).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(1).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB41 = new PdfPTable(new float[] { 3, 3, 3, 3, 3, 3, 3, 3 ,3 ,3  });
			tablaB41.setWidthPercentage(100);
			tablaB41.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB41.getDefaultCell().setPadding(3);
			tablaB41.getDefaultCell().setUseAscender(true);
			tablaB41.getDefaultCell().setUseDescender(true);
			tablaB41.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB41.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			Paragraph encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Modalidad",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Autoidentificación étnica",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nro hombres",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nro mujeres",fontCabeceraTabla));
			tablaB41.addCell(encabezadoTablaB);

			document.add(tablaB41);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(2).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(1).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(3).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB51 = new PdfPTable(new float[] { 3, 3, 3 ,3, 3, 3  });
			tablaB51.setWidthPercentage(100);
			tablaB51.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB51.getDefaultCell().setPadding(3);
			tablaB51.getDefaultCell().setUseAscender(true);
			tablaB51.getDefaultCell().setUseDescender(true);
			tablaB51.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB51.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaB51.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaB51.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaB51.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Institución",fontCabeceraTabla));
			tablaB51.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Actividades de coordinación",fontCabeceraTabla));
			tablaB51.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Link verificador del convenio",fontCabeceraTabla));
			tablaB51.addCell(encabezadoTablaB);

			document.add(tablaB51);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(4).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(2).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(5).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB61 = new PdfPTable(new float[] { 3, 3, 3 ,3,3 });
			tablaB61.setWidthPercentage(100);
			tablaB61.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB61.getDefaultCell().setPadding(3);
			tablaB61.getDefaultCell().setUseAscender(true);
			tablaB61.getDefaultCell().setUseDescender(true);
			tablaB61.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB61.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaB61.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaB61.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Institución",fontCabeceraTabla));
			tablaB61.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Objetivo",fontCabeceraTabla));
			tablaB61.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaB61.addCell(encabezadoTablaB);


			document.add(tablaB61);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(7).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(3).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(8).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB71 = new PdfPTable(new float[] { 3, 3, 3,3  });
			tablaB71.setWidthPercentage(100);
			tablaB71.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB71.getDefaultCell().setPadding(3);
			tablaB71.getDefaultCell().setUseAscender(true);
			tablaB71.getDefaultCell().setUseDescender(true);
			tablaB71.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB71.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Instrumento",fontCabeceraTabla));
			tablaB71.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Relevancia o importancia del instrumento",fontCabeceraTabla));
			tablaB71.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Mecanismo de Institucionalización",fontCabeceraTabla));
			tablaB71.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaB71.addCell(encabezadoTablaB);

			document.add(tablaB71);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(9).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(4).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(10).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB81 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3  });
			tablaB81.setWidthPercentage(100);
			tablaB81.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB81.getDefaultCell().setPadding(3);
			tablaB81.getDefaultCell().setUseAscender(true);
			tablaB81.getDefaultCell().setUseDescender(true);
			tablaB81.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaB81.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaB81.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaB81.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaB81.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaB81.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Actividad",fontCabeceraTabla));
			tablaB81.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Hectáreas",fontCabeceraTabla));
			tablaB81.addCell(encabezadoTablaB);
			document.add(tablaB81);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(11).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(5).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));

			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB9 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3  });
			tablaB9.setWidthPercentage(100);
			tablaB9.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB9.getDefaultCell().setPadding(3);
			tablaB9.getDefaultCell().setUseAscender(true);
			tablaB9.getDefaultCell().setUseDescender(true);
			tablaB9.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaB9.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaB9.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaB9.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaB9.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaB9.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaB9.addCell(encabezadoTablaB);

			document.add(tablaB9);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(12).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(6).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(13).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB102 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3, 3, 3,3  });
			tablaB102.setWidthPercentage(100);
			tablaB102.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB102.getDefaultCell().setPadding(3);
			tablaB102.getDefaultCell().setUseAscender(true);
			tablaB102.getDefaultCell().setUseDescender(true);
			tablaB102.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB102.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Hectáreas",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Estado",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nro Mujeres Beneficiarias",fontCabeceraTabla));
			tablaB102.addCell(encabezadoTablaB);

			document.add(tablaB102);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(15).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(16).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB11 = new PdfPTable(new float[] { 3, 3, 3 ,3 });
			tablaB11.setWidthPercentage(100);
			tablaB11.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB11.getDefaultCell().setPadding(3);
			tablaB11.getDefaultCell().setUseAscender(true);
			tablaB11.getDefaultCell().setUseDescender(true);
			tablaB11.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Cómo funciona",fontCabeceraTabla));
			tablaB11.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Casos que se han reportado durante el período de reporte",fontCabeceraTabla));
			tablaB11.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Casos que se han atendido durante el período de reporte",fontCabeceraTabla));
			tablaB11.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaB11.addCell(encabezadoTablaB);

			document.add(tablaB11);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(17).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(18).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB121 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3, 3 });
			tablaB121.setWidthPercentage(100);
			tablaB121.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB121.getDefaultCell().setPadding(3);
			tablaB121.getDefaultCell().setUseAscender(true);
			tablaB121.getDefaultCell().setUseDescender(true);
			tablaB121.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB121.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nombre de la organización Beneficiaria",fontCabeceraTabla));
			tablaB121.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Acciones implementadas",fontCabeceraTabla));
			tablaB121.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaB121.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaB121.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Conformación de la organización",fontCabeceraTabla));
			tablaB121.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nro hombres",fontCabeceraTabla));
			tablaB121.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nro mujeres",fontCabeceraTabla));
			tablaB121.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaB121.addCell(encabezadoTablaB);


			document.add(tablaB121);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(19).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(9).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(20).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB131 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3});
			tablaB131.setWidthPercentage(100);
			tablaB131.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB131.getDefaultCell().setPadding(3);
			tablaB131.getDefaultCell().setUseAscender(true);
			tablaB131.getDefaultCell().setUseDescender(true);
			tablaB131.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB131.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaB131.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Actividad",fontCabeceraTabla));
			tablaB131.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Temática tratada",fontCabeceraTabla));
			tablaB131.addCell(encabezadoTablaB);				
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Nro de personas que acceden a la info",fontCabeceraTabla));
			tablaB131.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaB131.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Componente",fontCabeceraTabla));
			tablaB131.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaB131.addCell(encabezadoTablaB);

			document.add(tablaB131);

			salvaguardaB = new Paragraph();
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(21).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(10).isVaanYesnoAnswerValue())
				salvaguardaB.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaB.add(new Phrase("NO", fontContenido));					
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(22).getQuesContentQuestion(), fontTitulos));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			salvaguardaB.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaB);

			PdfPTable tablaB143 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3,3});
			tablaB143.setWidthPercentage(100);
			tablaB143.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaB143.getDefaultCell().setPadding(3);
			tablaB143.getDefaultCell().setUseAscender(true);
			tablaB143.getDefaultCell().setUseDescender(true);
			tablaB143.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaB143.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaB143.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaB143.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Número de personas",fontCabeceraTabla));
			tablaB143.addCell(encabezadoTablaB);				
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("¿Que información se comunica a los beneficiarios?",fontCabeceraTabla));
			tablaB143.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("¿Como se informa a la gente sobre la ejecución del proyecto/programa?",fontCabeceraTabla));
			tablaB143.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaB143.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Componente",fontCabeceraTabla));
			tablaB143.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaB143.addCell(encabezadoTablaB);

			document.add(tablaB143);

			informacionOpcional = new Paragraph();
			informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			document.add(informacionOpcional);

			PdfPTable tablaOPB = new PdfPTable(new float[] { 7, 7,7 });
			tablaOPB.setWidthPercentage(100);
			tablaOPB.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaOPB.getDefaultCell().setPadding(3);
			tablaOPB.getDefaultCell().setUseAscender(true);
			tablaOPB.getDefaultCell().setUseDescender(true);
			tablaOPB.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaOPB.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaB=new Paragraph();				
			encabezadoTablaB.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
			tablaOPB.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
			tablaOPB.addCell(encabezadoTablaB);
			encabezadoTablaB=new Paragraph();	
			encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaOPB.addCell(encabezadoTablaB);

			document.add(tablaOPB);

			//SALVAGUARDA C

			Paragraph salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaC.add(new Phrase("SALVAGUARDA C", fontTitulosSalvaguardas));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(0).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(0).isVaanYesnoAnswerValue())
				salvaguardaC.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaC.add(new Phrase("NO", fontContenido));					
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(1).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC201 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3 ,3,3,3});
			tablaC201.setWidthPercentage(100);
			tablaC201.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC201.getDefaultCell().setPadding(3);
			tablaC201.getDefaultCell().setUseAscender(true);
			tablaC201.getDefaultCell().setUseDescender(true);
			tablaC201.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC201.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			Paragraph encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Nro Mujeres beneficiarias",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Hectáreas",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Inversión realizada",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Tipo de acceso",fontCabeceraTabla));
			tablaC201.addCell(encabezadoTablaC);
			document.add(tablaC201);

			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(2).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(1).isVaanYesnoAnswerValue())
				salvaguardaC.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaC.add(new Phrase("NO", fontContenido));					
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(3).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC211 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3,3});
			tablaC211.setWidthPercentage(100);
			tablaC211.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC211.getDefaultCell().setPadding(3);
			tablaC211.getDefaultCell().setUseAscender(true);
			tablaC211.getDefaultCell().setUseDescender(true);
			tablaC211.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC211.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Práctica o Saber ancestral",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("¿Cómo se reconocieron estas prácticas?",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("¿Cómo se han promovido estas prácticas?",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaC211.addCell(encabezadoTablaC);
			
			document.add(tablaC211);

			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(4).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(2).isVaanYesnoAnswerValue())
				salvaguardaC.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaC.add(new Phrase("NO", fontContenido));					
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(5).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC241 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3});
			tablaC241.setWidthPercentage(100);
			tablaC241.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC241.getDefaultCell().setPadding(3);
			tablaC241.getDefaultCell().setUseAscender(true);
			tablaC241.getDefaultCell().setUseDescender(true);
			tablaC241.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC241.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaC241.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Organizaciones que participan",fontCabeceraTabla));
			tablaC241.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Num hombres",fontCabeceraTabla));
			tablaC241.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Num mujeres",fontCabeceraTabla));
			tablaC241.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Temática",fontCabeceraTabla));
			tablaC241.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaC241.addCell(encabezadoTablaC);

			document.add(tablaC241);

			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					

			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(6).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC242 = new PdfPTable(new float[] { 3, 3});
			tablaC242.setWidthPercentage(100);
			tablaC242.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC242.getDefaultCell().setPadding(3);
			tablaC242.getDefaultCell().setUseAscender(true);
			tablaC242.getDefaultCell().setUseDescender(true);
			tablaC242.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC242.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Medidas que se han tomado para motivar la participación de mujeres",fontCabeceraTabla));
			tablaC242.addCell(encabezadoTablaC);								
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaC242.addCell(encabezadoTablaC);

			document.add(tablaC242);


			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(7).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(3).isVaanYesnoAnswerValue())
				salvaguardaC.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaC.add(new Phrase("NO", fontContenido));					
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(8).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC26 = new PdfPTable(new float[] { 3, 3, 3,3,3,3});
			tablaC26.setWidthPercentage(100);
			tablaC26.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC26.getDefaultCell().setPadding(3);
			tablaC26.getDefaultCell().setUseAscender(true);
			tablaC26.getDefaultCell().setUseDescender(true);
			tablaC26.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaC26.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaC26.addCell(encabezadoTablaC);					
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Representantes (nombres completos)",fontCabeceraTabla));
			tablaC26.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Cargo",fontCabeceraTabla));
			tablaC26.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
			tablaC26.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaC26.addCell(encabezadoTablaC);

			document.add(tablaC26);

			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(9).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(4).isVaanYesnoAnswerValue())
				salvaguardaC.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaC.add(new Phrase("NO", fontContenido));					
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(10).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC271 = new PdfPTable(new float[] {3, 3, 3, 3,3,3,3,3});
			tablaC271.setWidthPercentage(100);
			tablaC271.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC271.getDefaultCell().setPadding(3);
			tablaC271.getDefaultCell().setUseAscender(true);
			tablaC271.getDefaultCell().setUseDescender(true);
			tablaC271.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC271.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaC271.addCell(encabezadoTablaC);									
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaC271.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaC271.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaC271.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Objeto del convenio",fontCabeceraTabla));
			tablaC271.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Hectáreas bajo actividad REDD+",fontCabeceraTabla));
			tablaC271.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
			tablaC271.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaC271.addCell(encabezadoTablaC);
			
			document.add(tablaC271);

			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(11).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC28 = new PdfPTable(new float[] { 3, 3, 3, 3, 3,3,3});
			tablaC28.setWidthPercentage(100);
			tablaC28.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC28.getDefaultCell().setPadding(3);
			tablaC28.getDefaultCell().setUseAscender(true);
			tablaC28.getDefaultCell().setUseDescender(true);
			tablaC28.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();		
			encabezadoTablaC.add(new Phrase("Entina",fontCabeceraTabla));
			tablaC28.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaC28.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Objeto de contratación",fontCabeceraTabla));
			tablaC28.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Figura de contratación",fontCabeceraTabla));
			tablaC28.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Presupuesto",fontCabeceraTabla));
			tablaC28.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Cómo se están promoviendo y fomentando el derecho laboral en pueblos indígenas a través del PDI, programa y/ proyecto",fontCabeceraTabla));
			tablaC28.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
			tablaC28.addCell(encabezadoTablaC);
			
			document.add(tablaC28);

			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(12).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(5).isVaanYesnoAnswerValue())
				salvaguardaC.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaC.add(new Phrase("NO", fontContenido));					
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(13).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC291 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3,3});
			tablaC291.setWidthPercentage(100);
			tablaC291.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC291.getDefaultCell().setPadding(3);
			tablaC291.getDefaultCell().setUseAscender(true);
			tablaC291.getDefaultCell().setUseDescender(true);
			tablaC291.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC291.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);					
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Resultado",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link documento respaldo",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Objeto del acuerdo",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
			tablaC291.addCell(encabezadoTablaC);

			document.add(tablaC291);

			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(14).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(6).isVaanYesnoAnswerValue())
				salvaguardaC.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaC.add(new Phrase("NO", fontContenido));					
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(15).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC301 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3,3});
			tablaC301.setWidthPercentage(100);
			tablaC301.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC301.getDefaultCell().setPadding(3);
			tablaC301.getDefaultCell().setUseAscender(true);
			tablaC301.getDefaultCell().setUseDescender(true);
			tablaC301.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC301.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);					
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Acciones reportadas que causan daños",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Medidas de remediación",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Medidas de compensación",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link de la Remediación",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link de la Compensación",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
			tablaC301.addCell(encabezadoTablaC);

			document.add(tablaC301);

			salvaguardaC = new Paragraph();
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(16).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(7).isVaanYesnoAnswerValue())
				salvaguardaC.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaC.add(new Phrase("NO", fontContenido));					
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(17).getQuesContentQuestion(), fontTitulos));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			salvaguardaC.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaC);

			PdfPTable tablaC311 = new PdfPTable(new float[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,3});
			tablaC311.setWidthPercentage(100);
			tablaC311.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaC311.getDefaultCell().setPadding(3);
			tablaC311.getDefaultCell().setUseAscender(true);
			tablaC311.getDefaultCell().setUseDescender(true);
			tablaC311.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaC311.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);					
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Asistentes hombres",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Asistentes mujeres",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link verificador participantes",fontCabeceraTabla));
			tablaC311.addCell(encabezadoTablaC);
			
			document.add(tablaC311);

			informacionOpcional = new Paragraph();
			informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			document.add(informacionOpcional);

			PdfPTable tablaOPC = new PdfPTable(new float[] { 7, 7,7 });
			tablaOPC.setWidthPercentage(100);
			tablaOPC.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaOPC.getDefaultCell().setPadding(3);
			tablaOPC.getDefaultCell().setUseAscender(true);
			tablaOPC.getDefaultCell().setUseDescender(true);
			tablaOPC.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaOPC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaC=new Paragraph();				
			encabezadoTablaC.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
			tablaOPC.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
			tablaOPC.addCell(encabezadoTablaC);
			encabezadoTablaC=new Paragraph();	
			encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaOPC.addCell(encabezadoTablaC);

			document.add(tablaOPC);


			//SALVAGUARDA D

			Paragraph salvaguardaD = new Paragraph();
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaD.add(new Phrase("SALVAGUARDA D", fontTitulosSalvaguardas));
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaD.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasD().get(0).getQuesContentQuestion(), fontTitulos));
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasD().get(0).isVaanYesnoAnswerValue())
				salvaguardaD.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaD.add(new Phrase("NO", fontContenido));					
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));
			salvaguardaD.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasD().get(1).getQuesContentQuestion(), fontTitulos));
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaD);

			PdfPTable tablaD321 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3 ,3,3});
			tablaD321.setWidthPercentage(100);
			tablaD321.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaD321.getDefaultCell().setPadding(3);
			tablaD321.getDefaultCell().setUseAscender(true);
			tablaD321.getDefaultCell().setUseDescender(true);
			tablaD321.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaD321.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			Paragraph encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Espacio de difusión de la información",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Actores Asistentes que hombres participan en el proceso de acceso a la información",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Asistentes hombres",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Asistentes mujeres",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Componente",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaD321.addCell(encabezadoTablaD);

			document.add(tablaD321);

			salvaguardaD = new Paragraph();
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaD.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasD().get(2).getQuesContentQuestion(), fontTitulos));
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasD().get(1).isVaanYesnoAnswerValue())
				salvaguardaD.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaD.add(new Phrase("NO", fontContenido));					
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));
			salvaguardaD.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasD().get(3).getQuesContentQuestion(), fontTitulos));
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));
			salvaguardaD.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaD);

			PdfPTable tablaD331 = new PdfPTable(new float[] {3,3, 3, 3, 3,3 ,3,3,3});
			tablaD331.setWidthPercentage(100);
			tablaD331.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaD331.getDefaultCell().setPadding(3);
			tablaD331.getDefaultCell().setUseAscender(true);
			tablaD331.getDefaultCell().setUseDescender(true);
			tablaD331.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaD331.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Actor / Organización",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Actividad que realiza la comunidad",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Presupuesto asignado a la actividad",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Nivel de involucramiento",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Componente",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaD331.addCell(encabezadoTablaD);
			
			document.add(tablaD331);

			informacionOpcional = new Paragraph();
			informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			document.add(informacionOpcional);

			PdfPTable tablaOPD = new PdfPTable(new float[] { 7, 7,7 });
			tablaOPD.setWidthPercentage(100);
			tablaOPD.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaOPD.getDefaultCell().setPadding(3);
			tablaOPD.getDefaultCell().setUseAscender(true);
			tablaOPD.getDefaultCell().setUseDescender(true);
			tablaOPD.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaOPD.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaD=new Paragraph();				
			encabezadoTablaD.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
			tablaOPD.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
			tablaOPD.addCell(encabezadoTablaD);
			encabezadoTablaD=new Paragraph();	
			encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaOPD.addCell(encabezadoTablaD);

			document.add(tablaOPD);



			//SALVAGUARDA E

			Paragraph salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaE.add(new Phrase("SALVAGUARDA E", fontTitulosSalvaguardas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(0).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(0).isVaanYesnoAnswerValue())
				salvaguardaE.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaE.add(new Phrase("NO", fontContenido));					
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(1).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaE);

			PdfPTable tablaE341 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3,3,3 });
			tablaE341.setWidthPercentage(100);
			tablaE341.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaE341.getDefaultCell().setPadding(3);
			tablaE341.getDefaultCell().setUseAscender(true);
			tablaE341.getDefaultCell().setUseDescender(true);
			tablaE341.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaE341.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			Paragraph encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaE341.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaE341.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaE341.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaE341.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Actores involucrados clave",fontCabeceraTabla));
			tablaE341.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Hectáreas",fontCabeceraTabla));
			tablaE341.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Tipo de área consolidada",fontCabeceraTabla));
			tablaE341.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
			tablaE341.addCell(encabezadoTablaE);
			
			document.add(tablaE341);

			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(2).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(1).isVaanYesnoAnswerValue())
				salvaguardaE.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaE.add(new Phrase("NO", fontContenido));					
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(3).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaE);

			PdfPTable tablaE351 = new PdfPTable(new float[] { 3, 3, 3,3});
			tablaE351.setWidthPercentage(100);
			tablaE351.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaE351.getDefaultCell().setPadding(3);
			tablaE351.getDefaultCell().setUseAscender(true);
			tablaE351.getDefaultCell().setUseDescender(true);
			tablaE351.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaE351.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nivel",fontCabeceraTabla));
			tablaE351.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Herramienta (PDOT, ACUS, Ordenanza, etc)",fontCabeceraTabla));
			tablaE351.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
			tablaE351.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaE351.addCell(encabezadoTablaE);

			document.add(tablaE351);

			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(4).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(2).isVaanYesnoAnswerValue())
				salvaguardaE.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaE.add(new Phrase("NO", fontContenido));					
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(5).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaE);

			PdfPTable tablaE361 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3});
			tablaE361.setWidthPercentage(100);
			tablaE361.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaE361.getDefaultCell().setPadding(3);
			tablaE361.getDefaultCell().setUseAscender(true);
			tablaE361.getDefaultCell().setUseDescender(true);
			tablaE361.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaE361.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);				
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Actores clave",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Hectareas",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
			tablaE361.addCell(encabezadoTablaE);
			
			document.add(tablaE361);

			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(6).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(3).isVaanYesnoAnswerValue())
				salvaguardaE.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaE.add(new Phrase("NO", fontContenido));					
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(7).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaE);

			PdfPTable tablaE371 = new PdfPTable(new float[] {3, 3, 3, 3,3,3,3,3,3,3,3,3});
			tablaE371.setWidthPercentage(100);
			tablaE371.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaE371.getDefaultCell().setPadding(3);
			tablaE371.getDefaultCell().setUseAscender(true);
			tablaE371.getDefaultCell().setUseDescender(true);
			tablaE371.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaE371.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Temas",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Método",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Público",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro hombres",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro mujeres",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaE371.addCell(encabezadoTablaE);

			document.add(tablaE371);

			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(8).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaValoresRespuestasE().get(4).getVaanTextAnswerValue(),fontContenido));	
			document.add(salvaguardaE);

			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(9).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(5).isVaanYesnoAnswerValue())
				salvaguardaE.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaE.add(new Phrase("NO", fontContenido));					
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(10).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaE);

			PdfPTable tablaE381 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3});
			tablaE381.setWidthPercentage(100);
			tablaE381.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaE381.getDefaultCell().setPadding(3);
			tablaE381.getDefaultCell().setUseAscender(true);
			tablaE381.getDefaultCell().setUseDescender(true);
			tablaE381.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaE381.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaE381.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaE381.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaE381.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaE381.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Servicio",fontCabeceraTabla));
			tablaE381.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
			tablaE381.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaE381.addCell(encabezadoTablaE);
			
			document.add(tablaE381);

			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(11).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(6).isVaanYesnoAnswerValue())
				salvaguardaE.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaE.add(new Phrase("NO", fontContenido));					
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(12).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaE);

			PdfPTable tablaE391 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3});
			tablaE391.setWidthPercentage(100);
			tablaE391.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaE391.getDefaultCell().setPadding(3);
			tablaE391.getDefaultCell().setUseAscender(true);
			tablaE391.getDefaultCell().setUseDescender(true);
			tablaE391.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaE391.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaE391.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaE391.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaE391.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaE391.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Recurso",fontCabeceraTabla));
			tablaE391.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Periodicidad",fontCabeceraTabla));
			tablaE391.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
			tablaE391.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaE391.addCell(encabezadoTablaE);

			document.add(tablaE391);

			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(13).getQuesContentQuestion(), fontTitulos));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaE.add(new Phrase("COBENEFICIOS POLÍTICAS Y GESTIÓN INSTITUCIONAL", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			salvaguardaE = new Paragraph();												
			salvaguardaE.add(new Phrase("SOCIALES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(14).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);

			PdfPTable valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3,3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Comunidad",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nacionalidad",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(15).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(16).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(17).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Espacio de gobernanza",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();												
			salvaguardaE.add(new Phrase("AMBIENTALES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(18).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(19).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Riesgo abordado",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(20).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Capacidades fortalecidas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase("TRANSICIÓN A SISTEMAS PRODUCTIVOS SOSTENIBLES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			salvaguardaE = new Paragraph();												
			salvaguardaE.add(new Phrase("SOCIALES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(21).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(22).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(23).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Productos incentivados",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(24).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Organización fortalecida",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(25).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Asociación apoyada",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(26).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("% Incremento ingresos",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();												
			salvaguardaE.add(new Phrase("AMBIENTALES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(27).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(28).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(29).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(30).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase("MANEJO FORESTAL SOSTENIBLE", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			salvaguardaE = new Paragraph();												
			salvaguardaE.add(new Phrase("SOCIALES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(31).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Fuentes de ingreso apoyadas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(32).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Comunidad",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(33).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Capacidades fortalecidas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(34).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Iniciativa turismo apoyada",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(35).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Instrumento/mecanismo que mejora la gobernanza forestal",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(36).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nombre de las Organizaciones Apoyadas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(37).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nombre de las Organizaciones Apoyadas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();												
			salvaguardaE.add(new Phrase("AMBIENTALES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(38).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(39).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(40).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Cantón",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(41).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Especies protegidas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(42).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Especies protegidas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(43).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase("CONSERVACION Y RESTAURACION", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			salvaguardaE = new Paragraph();												
			salvaguardaE.add(new Phrase("SOCIALES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(44).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Usos alternativos",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Servicios valorados",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(45).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nombre de las Organizaciones Apoyadas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();												
			salvaguardaE.add(new Phrase("AMBIENTALES", fontTitulos));	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(46).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(47).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(48).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(49).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Canton",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(50).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			salvaguardaE = new Paragraph();																	
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(51).getQuesContentQuestion(), fontContenidoTablas));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));
			salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
			document.add(salvaguardaE);
			
			valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
			valoresCobeneficios.setWidthPercentage(100);
			valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
			valoresCobeneficios.getDefaultCell().setPadding(3);
			valoresCobeneficios.getDefaultCell().setUseAscender(true);
			valoresCobeneficios.getDefaultCell().setUseDescender(true);
			valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
			valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
			valoresCobeneficios.addCell(encabezadoTablaE);
			document.add(valoresCobeneficios);
			
			
			
			informacionOpcional = new Paragraph();
			informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			document.add(informacionOpcional);

			PdfPTable tablaOPE = new PdfPTable(new float[] { 7, 7,7 });
			tablaOPE.setWidthPercentage(100);
			tablaOPE.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaOPE.getDefaultCell().setPadding(3);
			tablaOPE.getDefaultCell().setUseAscender(true);
			tablaOPE.getDefaultCell().setUseDescender(true);
			tablaOPE.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaOPE.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaE=new Paragraph();				
			encabezadoTablaE.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
			tablaOPE.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
			tablaOPE.addCell(encabezadoTablaE);
			encabezadoTablaE=new Paragraph();	
			encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaOPE.addCell(encabezadoTablaE);


			document.add(tablaOPE);

			//SALVAGUARDA F


			Paragraph salvaguardaF = new Paragraph();
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaF.add(new Phrase("SALVAGUARDA F", fontTitulosSalvaguardas));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(0).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(0).isVaanYesnoAnswerValue())
				salvaguardaF.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaF.add(new Phrase("NO", fontContenido));					
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(1).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaF);

			PdfPTable tablaF411 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3 ,3,3});
			tablaF411.setWidthPercentage(100);
			tablaF411.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaF411.getDefaultCell().setPadding(3);
			tablaF411.getDefaultCell().setUseAscender(true);
			tablaF411.getDefaultCell().setUseDescender(true);
			tablaF411.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaF411.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			Paragraph encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaF411.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaF411.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaF411.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaF411.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaF411.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaF411.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Riesgo",fontCabeceraTabla));
			tablaF411.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaF411.addCell(encabezadoTablaF);


			document.add(tablaF411);

			salvaguardaF = new Paragraph();
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(2).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(1).isVaanYesnoAnswerValue())
				salvaguardaF.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaF.add(new Phrase("NO", fontContenido));					
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(3).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaF);

			PdfPTable tablaF421 = new PdfPTable(new float[] {3, 3, 3, 3,3 ,3, 3 ,3,3,3});
			tablaF421.setWidthPercentage(100);
			tablaF421.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaF421.getDefaultCell().setPadding(3);
			tablaF421.getDefaultCell().setUseAscender(true);
			tablaF421.getDefaultCell().setUseDescender(true);
			tablaF421.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaF421.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Riesgo asociado",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Medida tomada",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Componente",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaF421.addCell(encabezadoTablaF);

			document.add(tablaF421);

			salvaguardaF = new Paragraph();
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(4).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(2).isVaanYesnoAnswerValue())
				salvaguardaF.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaF.add(new Phrase("NO", fontContenido));					
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(5).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaF);

			PdfPTable tablaF431 = new PdfPTable(new float[] { 5, 3, 3,3 ,3, 3,3});
			tablaF431.setWidthPercentage(100);
			tablaF431.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaF431.getDefaultCell().setPadding(3);
			tablaF431.getDefaultCell().setUseAscender(true);
			tablaF431.getDefaultCell().setUseDescender(true);
			tablaF431.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaF431.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Sistemas REDD+",fontCabeceraTabla));
			tablaF431.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Acciones tomadas",fontCabeceraTabla));
			tablaF431.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Recursos invertidos",fontCabeceraTabla));
			tablaF431.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Actores clave",fontCabeceraTabla));
			tablaF431.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Resultado",fontCabeceraTabla));
			tablaF431.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Componente",fontCabeceraTabla));
			tablaF431.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaF431.addCell(encabezadoTablaF);

			document.add(tablaF431);

			salvaguardaF = new Paragraph();
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(6).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(3).isVaanYesnoAnswerValue())
				salvaguardaF.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaF.add(new Phrase("NO", fontContenido));					
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(7).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaF);

			PdfPTable tablaF441 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3 ,3,3,3,3});
			tablaF441.setWidthPercentage(100);
			tablaF441.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaF441.getDefaultCell().setPadding(3);
			tablaF441.getDefaultCell().setUseAscender(true);
			tablaF441.getDefaultCell().setUseDescender(true);
			tablaF441.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaF441.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Riesgo",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Actividad para mitigar",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Componente",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaF441.addCell(encabezadoTablaF);
			document.add(tablaF441);

			salvaguardaF = new Paragraph();
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(8).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(4).isVaanYesnoAnswerValue())
				salvaguardaF.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaF.add(new Phrase("NO", fontContenido));					
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(9).getQuesContentQuestion(), fontTitulos));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			salvaguardaF.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaF);

			PdfPTable tablaF452 = new PdfPTable(new float[] { 5, 3, 3,3,3 });
			tablaF452.setWidthPercentage(100);
			tablaF452.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaF452.getDefaultCell().setPadding(3);
			tablaF452.getDefaultCell().setUseAscender(true);
			tablaF452.getDefaultCell().setUseDescender(true);
			tablaF452.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaF452.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaF452.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Sistemas",fontCabeceraTabla));
			tablaF452.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Nro. Casos reportados",fontCabeceraTabla));
			tablaF452.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Nro. Casos atendidos",fontCabeceraTabla));
			tablaF452.addCell(encabezadoTablaF);										
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaF452.addCell(encabezadoTablaF);
			document.add(tablaF452);

			informacionOpcional = new Paragraph();
			informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			document.add(informacionOpcional);

			PdfPTable tablaOPF = new PdfPTable(new float[] { 7, 7,7 });
			tablaOPF.setWidthPercentage(100);
			tablaOPF.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaOPF.getDefaultCell().setPadding(3);
			tablaOPF.getDefaultCell().setUseAscender(true);
			tablaOPF.getDefaultCell().setUseDescender(true);
			tablaOPF.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaOPF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaF=new Paragraph();				
			encabezadoTablaF.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
			tablaOPF.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
			tablaOPF.addCell(encabezadoTablaF);
			encabezadoTablaF=new Paragraph();	
			encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaOPF.addCell(encabezadoTablaF);

			document.add(tablaOPF);

			//SALVAGUARDA G


			Paragraph salvaguardaG = new Paragraph();
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));	
			salvaguardaG.add(new Phrase("SALVAGUARDA G", fontTitulosSalvaguardas));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(0).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(0).isVaanYesnoAnswerValue())
				salvaguardaG.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaG.add(new Phrase("NO", fontContenido));					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(1).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaG);

			PdfPTable tablaG461 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3});
			tablaG461.setWidthPercentage(100);
			tablaG461.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaG461.getDefaultCell().setPadding(3);
			tablaG461.getDefaultCell().setUseAscender(true);
			tablaG461.getDefaultCell().setUseDescender(true);
			tablaG461.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaG461.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			Paragraph encabezadoTablaG=new Paragraph();
			encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaG461.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Responsable del estudio",fontCabeceraTabla));
			tablaG461.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Autores de la investigación",fontCabeceraTabla));
			tablaG461.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Riesgo Principal Identificado",fontCabeceraTabla));
			tablaG461.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nombre del estudio",fontCabeceraTabla));
			tablaG461.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Componente",fontCabeceraTabla));
			tablaG461.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Verificador",fontCabeceraTabla));
			tablaG461.addCell(encabezadoTablaG);

			document.add(tablaG461);

			salvaguardaG = new Paragraph();					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(2).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(1).isVaanYesnoAnswerValue())
				salvaguardaG.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaG.add(new Phrase("NO", fontContenido));					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(3).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaG);

			PdfPTable tablaG471 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3});
			tablaG471.setWidthPercentage(100);
			tablaG471.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaG471.getDefaultCell().setPadding(3);
			tablaG471.getDefaultCell().setUseAscender(true);
			tablaG471.getDefaultCell().setUseDescender(true);
			tablaG471.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaG471.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Actividad",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nro hombres",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nro mujeres",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaG471.addCell(encabezadoTablaG);
			document.add(tablaG471);

			salvaguardaG = new Paragraph();					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(5).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(2).isVaanYesnoAnswerValue())
				salvaguardaG.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaG.add(new Phrase("NO", fontContenido));					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(6).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaG);

			PdfPTable tablaG481 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3});
			tablaG481.setWidthPercentage(100);
			tablaG481.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaG481.getDefaultCell().setPadding(3);
			tablaG481.getDefaultCell().setUseAscender(true);
			tablaG481.getDefaultCell().setUseDescender(true);
			tablaG481.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaG481.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Institución acompaña",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Actividad ilicita reportada",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Resultado",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Fecha",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaG481.addCell(encabezadoTablaG);
			document.add(tablaG481);

			salvaguardaG = new Paragraph();					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(7).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(3).isVaanYesnoAnswerValue())
				salvaguardaG.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaG.add(new Phrase("NO", fontContenido));					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(8).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaG);

			PdfPTable tablaG491 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3,3,3});
			tablaG491.setWidthPercentage(100);
			tablaG491.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaG491.getDefaultCell().setPadding(3);
			tablaG491.getDefaultCell().setUseAscender(true);
			tablaG491.getDefaultCell().setUseDescender(true);
			tablaG491.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaG491.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Organización Beneficiaria",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Tipo de incentivo",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nro Mujeres Beneficiarias",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Support value chain",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Componente",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaG491.addCell(encabezadoTablaG);
			document.add(tablaG491);

			salvaguardaG = new Paragraph();					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(9).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(4).isVaanYesnoAnswerValue())
				salvaguardaG.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaG.add(new Phrase("NO", fontContenido));					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(10).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaG);

			PdfPTable tablaG501 = new PdfPTable(new float[] { 3, 3, 3,3,3});
			tablaG501.setWidthPercentage(100);
			tablaG501.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaG501.getDefaultCell().setPadding(3);
			tablaG501.getDefaultCell().setUseAscender(true);
			tablaG501.getDefaultCell().setUseDescender(true);
			tablaG501.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaG501.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Actividad",fontCabeceraTabla));
			tablaG501.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Presupuesto",fontCabeceraTabla));
			tablaG501.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Resultado",fontCabeceraTabla));
			tablaG501.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Componente",fontCabeceraTabla));
			tablaG501.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaG501.addCell(encabezadoTablaG);
			document.add(tablaG501);

			salvaguardaG = new Paragraph();					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(11).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(5).isVaanYesnoAnswerValue())
				salvaguardaG.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaG.add(new Phrase("NO", fontContenido));					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(12).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			document.add(salvaguardaG);

			PdfPTable tablaG511 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3,3});
			tablaG511.setWidthPercentage(100);
			tablaG511.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaG511.getDefaultCell().setPadding(3);
			tablaG511.getDefaultCell().setUseAscender(true);
			tablaG511.getDefaultCell().setUseDescender(true);
			tablaG511.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaG511.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Etnia",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nro hombres",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Nro mujeres",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Monitoreo remoto",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Monitoreo in situ",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Periodicidad",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Componente",fontCabeceraTabla));
			tablaG511.addCell(encabezadoTablaG);


			document.add(tablaG511);

			salvaguardaG = new Paragraph();					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(13).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(6).isVaanYesnoAnswerValue())
				salvaguardaG.add(new Phrase("SI", fontTitulos));
			else
				salvaguardaG.add(new Phrase("NO", fontContenido));					
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(14).getQuesContentQuestion(), fontTitulos));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(Chunk.NEWLINE));
			salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaValoresRespuestasG().get(7).getVaanTextAnswerValue(),fontContenido));
			document.add(salvaguardaG);

			informacionOpcional = new Paragraph();
			informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			informacionOpcional.add(new Phrase(Chunk.NEWLINE));
			document.add(informacionOpcional);

			PdfPTable tablaOPG = new PdfPTable(new float[] { 7, 7,7 });
			tablaOPG.setWidthPercentage(100);
			tablaOPG.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaOPG.getDefaultCell().setPadding(3);
			tablaOPG.getDefaultCell().setUseAscender(true);
			tablaOPG.getDefaultCell().setUseDescender(true);
			tablaOPG.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tablaOPG.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			encabezadoTablaG=new Paragraph();				
			encabezadoTablaG.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
			tablaOPG.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
			tablaOPG.addCell(encabezadoTablaG);
			encabezadoTablaG=new Paragraph();	
			encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
			tablaOPG.addCell(encabezadoTablaG);

			document.add(tablaOPG);

			Paragraph saltoLinea=new Paragraph();
			saltoLinea.add(new Phrase(Chunk.NEWLINE));

			document.add(saltoLinea);

			document.close();
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			response.setContentType("application/pdf");				
			response.setHeader("Content-Disposition","attachment; filename=" + new StringBuilder().append("resumenSalvaguardas").append(".pdf").toString());				
			response.getOutputStream().write(Archivos.getBytesFromFile(new File(directorioArchivoPDF)));
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();

		}catch(IOException e){
			e.printStackTrace();
		} catch (DocumentException e) {			
			e.printStackTrace();									
		} 

	}
	/**
	 * Reporte pdf de las salvaguardas del proyecto seleccionado
	 * @param directorioArchivoPDF
	 * @param seguimientoSalvaguardas
	 */
	public static void reporteSalvaguardaConDatos(String directorioArchivoPDF, SeguimientoSalvaguardaBean seguimientoSalvaguardas){
		try{
			String pattern = "MMM yy HH:mm";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date = simpleDateFormat.format(new Date());
			
			Document document = new Document();
			document.setPageSize(PageSize.A4.rotate());
			document.setMargins(35, 35, 78, 88);//iz dere arriba abajo
			document.setMarginMirroring(true);

			PdfWriter writer =PdfWriter.getInstance(document, new FileOutputStream(directorioArchivoPDF));
			Rectangle rect = new Rectangle(100, 30, 500, 800);
			writer.setBoxSize("art", rect);
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
			writer.setPageEvent(event);
			document.open();

			Font fontCabecera = new Font(FontFamily.HELVETICA, 9, Font.NORMAL, BaseColor.BLACK);
			Font fontContenido = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
			Font fontTitulos = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 7);
			Font fontTitulosSalvaguardas = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 12);
			fontTitulosSalvaguardas.setColor(13, 165, 196);

			Font fontCabeceraTabla = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
			Font fontContenidoTablas = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);

			PdfPTable tablaTituloSistema = new PdfPTable(3);
			
			tablaTituloSistema.setWidths(new int[]{50, 50,50});
			PdfPCell celda1 = new PdfPCell(new Phrase("Sistema de Información de Salvaguardas SIS", fontCabecera));
			celda1.setBackgroundColor(new BaseColor(13, 165, 196));
			celda1.setBorderColor(new BaseColor(13, 165, 196));
			celda1.setFixedHeight(15);
			
			PdfPCell celda2 = new PdfPCell();
			celda2.setBackgroundColor(new BaseColor(255, 255, 255));
			celda2.setBorderColor(new BaseColor(255, 255, 255));
			
			PdfPCell celda3 = new PdfPCell(new Phrase("Resumen de las salvaguardas reportadas", fontCabecera));
			celda3.setBackgroundColor(new BaseColor(13, 165, 196));
			celda3.setBorderColor(new BaseColor(13, 165, 196));
			
			PdfPCell celda4 = new PdfPCell();
			celda4.setBackgroundColor(new BaseColor(255, 255, 255));
			celda4.setBorderColor(new BaseColor(255, 255, 255));
			
			PdfPCell celda5 = new PdfPCell();
			celda5.setBackgroundColor(new BaseColor(255, 255, 255));
			celda5.setBorderColor(new BaseColor(255, 255, 255));
			
			PdfPCell celda6 = new PdfPCell(new Phrase(date, fontCabecera));
			celda6.setBackgroundColor(new BaseColor(255, 255, 255));
			celda6.setBorderColor(new BaseColor(255, 255, 255));
			
			
			tablaTituloSistema.addCell(celda1);
			tablaTituloSistema.addCell(celda2);
			tablaTituloSistema.addCell(celda3);
			tablaTituloSistema.addCell(celda4);
			tablaTituloSistema.addCell(celda5);
			tablaTituloSistema.addCell(celda6);
			document.add(tablaTituloSistema);
			
			Paragraph salto = new Paragraph();
			salto.add(new Phrase(Chunk.NEWLINE));
			document.add(salto);

			PdfPTable tablaCabecera = new PdfPTable(2);

			PdfPCell celda = new PdfPCell(new Phrase("Título del Plan de implementación, Programa o Proyecto:", fontTitulos));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setBackgroundColor(new BaseColor(13, 165, 196));
			celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablaCabecera.addCell(celda);


			celda = new PdfPCell(new Phrase(seguimientoSalvaguardas.getProyectoSeleccionado().getProjTitle(), fontContenido));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setBackgroundColor(new BaseColor(13, 165, 196));
			celda.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaCabecera.addCell(celda);

			celda = new PdfPCell(new Phrase("Socio implementador: ", fontTitulos));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setBackgroundColor(new BaseColor(13, 165, 196));
			celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablaCabecera.addCell(celda);

			celda = new PdfPCell(new Phrase(seguimientoSalvaguardas.getSocioImplementador().getPartName(), fontContenido));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setBackgroundColor(new BaseColor(13, 165, 196));
			celda.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaCabecera.addCell(celda);

			celda = new PdfPCell(new Phrase("Con qué sector se identifica:  ", fontTitulos));
			celda.setBackgroundColor(new BaseColor(13, 165, 196));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablaCabecera.addCell(celda);

			Paragraph sectores = new Paragraph();
			for(Sectors cata: seguimientoSalvaguardas.getListaSectoresDisponibles()){				
				Iterator itera = seguimientoSalvaguardas.getListaSectoresSeleccionados().iterator();
				while(itera.hasNext()){
					int key = Integer.valueOf(itera.next().toString());
					if(key == cata.getSectId()){
						sectores.add(new Phrase(cata.getSectName(), fontContenido));
						sectores.add(new Phrase(Chunk.NEWLINE));
					}
				}
			}
			celda = new PdfPCell(sectores);
			celda.setBackgroundColor(new BaseColor(13, 165, 196));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaCabecera.addCell(celda);


			document.add(tablaCabecera);

			if(seguimientoSalvaguardas.isSalvaguardaA()){				
				Paragraph salvaguardaA = new Paragraph();
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaA.add(new Phrase("SALVAGUARDA A", fontTitulosSalvaguardas));
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));
				salvaguardaA.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasA().get(1).getQuesContentQuestion(), fontTitulos));
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));
				salvaguardaA.add(new Phrase("Marco Jurídico Internacional", fontTitulos));
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaA);

				Paragraph marcoJuridico = new Paragraph();
				marcoJuridico.setIndentationLeft(20);
				for(Catalogs cata: seguimientoSalvaguardas.getCatalogoLeyes()){				
					Iterator itera = seguimientoSalvaguardas.getCatalogoLeyesSeleccionado().iterator();
					while(itera.hasNext()){
						int key = Integer.valueOf(itera.next().toString());
						if(key == cata.getCataId()){
							marcoJuridico.add(new Phrase(cata.getCataText2(), fontContenido));
							marcoJuridico.add(new Phrase(Chunk.NEWLINE));
						}
					}
				}
				document.add(marcoJuridico);
				salvaguardaA = new Paragraph();
				salvaguardaA.add(new Phrase("Marco Jurídico Nacional", fontTitulos));
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaA);

				Paragraph marcoJuridicoNacional = new Paragraph();
				marcoJuridicoNacional.setIndentationLeft(20);
				for(Catalogs cata: seguimientoSalvaguardas.getCatalogoMarcoJuridicoNacional()){				
					Iterator itera = seguimientoSalvaguardas.getCatalogoMarcoJuridicoNacionalSeleccionado().iterator();
					while(itera.hasNext()){
						int key = Integer.valueOf(itera.next().toString());
						if(key == cata.getCataId()){
							marcoJuridicoNacional.add(new Phrase(cata.getCataText2(), fontContenido));
							marcoJuridicoNacional.add(new Phrase(Chunk.NEWLINE));
						}
					}
				}
				document.add(marcoJuridicoNacional);
				salvaguardaA = new Paragraph();
				salvaguardaA.add(new Phrase("Normativa Secundaria Nacional", fontTitulos));
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaA);

				Paragraph normativa = new Paragraph();
				normativa.setIndentationLeft(20);
				for(Catalogs cata: seguimientoSalvaguardas.getCatalogoNormativaSecundariaNacional()){				
					Iterator itera = seguimientoSalvaguardas.getCatalogoNormativaSecundariaNacionalSeleccionado().iterator();
					while(itera.hasNext()){
						int key = Integer.valueOf(itera.next().toString());
						if(key == cata.getCataId()){
							normativa.add(new Phrase(cata.getCataText2(), fontContenido));
							normativa.add(new Phrase(Chunk.NEWLINE));
						}
					}
				}
				document.add(normativa);
				Paragraph politica = new Paragraph();
				politica.setIndentationLeft(20);
				salvaguardaA = new Paragraph();
				salvaguardaA.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasA().get(3).getQuesContentQuestion(), fontTitulos));
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaA);
				for(Catalogs cata: seguimientoSalvaguardas.getCatalogoPoliticas()){				
					Iterator itera = seguimientoSalvaguardas.getCatalogoPoliticasSeleccionado().iterator();
					while(itera.hasNext()){
						int key = Integer.valueOf(itera.next().toString());
						if(key == cata.getCataId()){
							politica.add(new Phrase(cata.getCataText2(), fontContenido));
							politica.add(new Phrase(Chunk.NEWLINE));
						}
					}
				}
				document.add(politica);
				salvaguardaA = new Paragraph();
				salvaguardaA.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasA().get(4).getQuesContentQuestion(), fontTitulos));
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));
				salvaguardaA.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaA);

				PdfPTable tabla1A = new PdfPTable(new float[] { 7, 3 });
				tabla1A.setWidthPercentage(100);
				tabla1A.setHorizontalAlignment(Element.ALIGN_LEFT);
				tabla1A.getDefaultCell().setPadding(3);
				tabla1A.getDefaultCell().setUseAscender(true);
				tabla1A.getDefaultCell().setUseDescender(true);
				tabla1A.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tabla1A.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				Paragraph encabezadoTablaA=new Paragraph();				
				encabezadoTablaA.add(new Phrase("Plan/Proyecto del Gobierno Nacional",fontCabeceraTabla));
				tabla1A.addCell(encabezadoTablaA);
				encabezadoTablaA=new Paragraph();	
				encabezadoTablaA.add(new Phrase("Presupuesto asignado",fontCabeceraTabla));
				tabla1A.addCell(encabezadoTablaA);

				Paragraph datosTablaA;
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaA()){
					datosTablaA=new Paragraph();
					datosTablaA.add(new Phrase(tabla.getTareCatPlanGobierno(),fontContenidoTablas));
					tabla1A.addCell(datosTablaA);
					datosTablaA=new Paragraph();
					datosTablaA.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()) ,fontContenidoTablas));
					tabla1A.addCell(datosTablaA);
				}
				document.add(tabla1A);

				Paragraph informacionOpcional = new Paragraph();
				informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				document.add(informacionOpcional);

				PdfPTable tabla2A = new PdfPTable(new float[] { 7, 7,7 });
				tabla2A.setWidthPercentage(100);
				tabla2A.setHorizontalAlignment(Element.ALIGN_LEFT);
				tabla2A.getDefaultCell().setPadding(3);
				tabla2A.getDefaultCell().setUseAscender(true);
				tabla2A.getDefaultCell().setUseDescender(true);
				tabla2A.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tabla2A.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaA=new Paragraph();				
				encabezadoTablaA.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
				tabla2A.addCell(encabezadoTablaA);
				encabezadoTablaA=new Paragraph();	
				encabezadoTablaA.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
				tabla2A.addCell(encabezadoTablaA);
				encabezadoTablaA=new Paragraph();	
				encabezadoTablaA.add(new Phrase("Link verificador",fontCabeceraTabla));
				tabla2A.addCell(encabezadoTablaA);

				datosTablaA=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaOPA()){
					datosTablaA.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tabla2A.addCell(datosTablaA);
					datosTablaA=new Paragraph();
					datosTablaA.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tabla2A.addCell(datosTablaA);
					datosTablaA=new Paragraph();
					datosTablaA.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tabla2A.addCell(datosTablaA);
				}
				document.add(tabla2A);
			}
			//SALVAGUARDA B
			if(seguimientoSalvaguardas.isSalvaguardaB()){	
				Paragraph salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaB.add(new Phrase("SALVAGUARDA B", fontTitulosSalvaguardas));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(0).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(0).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(1).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB41 = new PdfPTable(new float[] { 3, 3, 3, 3, 3, 3, 3, 3 ,3 ,3  });
				tablaB41.setWidthPercentage(100);
				tablaB41.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB41.getDefaultCell().setPadding(3);
				tablaB41.getDefaultCell().setUseAscender(true);
				tablaB41.getDefaultCell().setUseDescender(true);
				tablaB41.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB41.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				Paragraph encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Modalidad",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Autoidentificación étnica",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nro hombres",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nro mujeres",fontCabeceraTabla));
				tablaB41.addCell(encabezadoTablaB);

				Paragraph datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB41()){
					datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaB41.addCell(datosTablaB);
					datosTablaB=new Paragraph();
				}
				document.add(tablaB41);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(2).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(1).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(3).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB51 = new PdfPTable(new float[] { 3, 3, 3 ,3, 3, 3  });
				tablaB51.setWidthPercentage(100);
				tablaB51.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB51.getDefaultCell().setPadding(3);
				tablaB51.getDefaultCell().setUseAscender(true);
				tablaB51.getDefaultCell().setUseDescender(true);
				tablaB51.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB51.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaB51.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaB51.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaB51.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Institución",fontCabeceraTabla));
				tablaB51.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Actividades de coordinación",fontCabeceraTabla));
				tablaB51.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Link verificador del convenio",fontCabeceraTabla));
				tablaB51.addCell(encabezadoTablaB);


				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB51()){
					datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaB51.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaB51.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaB51.addCell(datosTablaB);						
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB51.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaB51.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaB51.addCell(datosTablaB);						
					datosTablaB=new Paragraph();
				}
				document.add(tablaB51);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(4).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(2).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(5).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB61 = new PdfPTable(new float[] {3,3, 3, 3, 3  });
				tablaB61.setWidthPercentage(100);
				tablaB61.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB61.getDefaultCell().setPadding(3);
				tablaB61.getDefaultCell().setUseAscender(true);
				tablaB61.getDefaultCell().setUseDescender(true);
				tablaB61.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB61.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaB61.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaB61.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Institución",fontCabeceraTabla));
				tablaB61.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Actividad que realiza la comunidad",fontCabeceraTabla));
				tablaB61.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaB61.addCell(encabezadoTablaB);


				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB61()){
					datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaB61.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaB61.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB61.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaB61.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaB61.addCell(datosTablaB);						
					datosTablaB=new Paragraph();
				}
				document.add(tablaB61);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(7).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(3).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(8).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB71 = new PdfPTable(new float[] { 3, 3, 3 ,3 });
				tablaB71.setWidthPercentage(100);
				tablaB71.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB71.getDefaultCell().setPadding(3);
				tablaB71.getDefaultCell().setUseAscender(true);
				tablaB71.getDefaultCell().setUseDescender(true);
				tablaB71.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB71.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Instrumento",fontCabeceraTabla));
				tablaB71.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Relevancia o importancia del instrumento",fontCabeceraTabla));
				tablaB71.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Mecanismo de Institucionalización",fontCabeceraTabla));
				tablaB71.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaB71.addCell(encabezadoTablaB);


				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB71()){
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB71.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaB71.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaB71.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaB71.addCell(datosTablaB);
					datosTablaB=new Paragraph();
				}
				document.add(tablaB71);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(9).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(4).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(10).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB81 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3  });
				tablaB81.setWidthPercentage(100);
				tablaB81.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB81.getDefaultCell().setPadding(3);
				tablaB81.getDefaultCell().setUseAscender(true);
				tablaB81.getDefaultCell().setUseDescender(true);
				tablaB81.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB81.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaB81.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaB81.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaB81.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaB81.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaB81.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Actividad",fontCabeceraTabla));
				tablaB81.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Hectáreas",fontCabeceraTabla));
				tablaB81.addCell(encabezadoTablaB);



				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB81()){
					
					datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaB81.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaB81.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaB81.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB81.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaB81.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaB81.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaB81.addCell(datosTablaB);											
					datosTablaB=new Paragraph();
				}
				document.add(tablaB81);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(11).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(5).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				//				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(10).getQuesContentQuestion(), fontTitulos));
				//				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB9 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3  });
				tablaB9.setWidthPercentage(100);
				tablaB9.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB9.getDefaultCell().setPadding(3);
				tablaB9.getDefaultCell().setUseAscender(true);
				tablaB9.getDefaultCell().setUseDescender(true);
				tablaB9.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB9.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaB9.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaB9.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaB9.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaB9.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaB9.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaB9.addCell(encabezadoTablaB);



				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB9()){

					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaB9.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaB9.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaB9.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaB9.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaB9.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB9.addCell(datosTablaB);												
					datosTablaB=new Paragraph();
				}
				document.add(tablaB9);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(12).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(6).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(13).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB102 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3, 3, 3,3  });
				tablaB102.setWidthPercentage(100);
				tablaB102.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB102.getDefaultCell().setPadding(3);
				tablaB102.getDefaultCell().setUseAscender(true);
				tablaB102.getDefaultCell().setUseDescender(true);
				tablaB102.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB102.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Hectáreas",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Estado",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nro Mujeres Beneficiarias",fontCabeceraTabla));
				tablaB102.addCell(encabezadoTablaB);



				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB102()){

					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaB102.addCell(datosTablaB);						
					datosTablaB=new Paragraph();
				}
				document.add(tablaB102);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(15).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(7).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(16).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB11 = new PdfPTable(new float[] { 3, 3, 3 ,3 });
				tablaB11.setWidthPercentage(100);
				tablaB11.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB11.getDefaultCell().setPadding(3);
				tablaB11.getDefaultCell().setUseAscender(true);
				tablaB11.getDefaultCell().setUseDescender(true);
				tablaB11.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB11.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Cómo funciona",fontCabeceraTabla));
				tablaB11.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Casos que se han reportado durante el período de reporte",fontCabeceraTabla));
				tablaB11.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Casos que se han atendido durante el período de reporte",fontCabeceraTabla));
				tablaB11.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaB11.addCell(encabezadoTablaB);

				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB11()){

					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB11.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnGenericOne()),fontContenidoTablas));
					tablaB11.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnGenericTwo()),fontContenidoTablas));
					tablaB11.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaB11.addCell(datosTablaB);
					datosTablaB=new Paragraph();
				}
				document.add(tablaB11);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(17).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(8).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(18).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB121 = new PdfPTable(new float[] { 3, 3, 3,3, 3, 3,3, 3 });
				tablaB121.setWidthPercentage(100);
				tablaB121.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB121.getDefaultCell().setPadding(3);
				tablaB121.getDefaultCell().setUseAscender(true);
				tablaB121.getDefaultCell().setUseDescender(true);
				tablaB121.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB121.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nombre de la organización Beneficiaria",fontCabeceraTabla));
				tablaB121.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Acciones implementadas",fontCabeceraTabla));
				tablaB121.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaB121.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaB121.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Conformación de la organización",fontCabeceraTabla));
				tablaB121.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nro hombres",fontCabeceraTabla));
				tablaB121.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nro mujeres",fontCabeceraTabla));
				tablaB121.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaB121.addCell(encabezadoTablaB);

				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB121()){

					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB121.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaB121.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaB121.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenericoCuatro(),fontContenidoTablas));
					tablaB121.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaB121.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaB121.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaB121.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaB121.addCell(datosTablaB);												
					datosTablaB=new Paragraph();
				}
				document.add(tablaB121);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(19).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(9).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(20).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB131 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3});
				tablaB131.setWidthPercentage(100);
				tablaB131.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB131.getDefaultCell().setPadding(3);
				tablaB131.getDefaultCell().setUseAscender(true);
				tablaB131.getDefaultCell().setUseDescender(true);
				tablaB131.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB131.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaB131.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Actividad",fontCabeceraTabla));
				tablaB131.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Temática tratada",fontCabeceraTabla));
				tablaB131.addCell(encabezadoTablaB);				
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Nro de personas que acceden a la info",fontCabeceraTabla));
				tablaB131.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaB131.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Componente",fontCabeceraTabla));
				tablaB131.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaB131.addCell(encabezadoTablaB);

				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB131()){

					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaB131.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaB131.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB131.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnGenericOne()),fontContenidoTablas));
					tablaB131.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaB131.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaB131.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaB131.addCell(datosTablaB);																		
					datosTablaB=new Paragraph();
				}
				document.add(tablaB131);

				salvaguardaB = new Paragraph();
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(21).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasB().get(10).isVaanYesnoAnswerValue())
					salvaguardaB.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaB.add(new Phrase("NO", fontContenido));					
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasB().get(22).getQuesContentQuestion(), fontTitulos));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				salvaguardaB.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaB);

				PdfPTable tablaB143 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3,3});
				tablaB143.setWidthPercentage(100);
				tablaB143.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaB143.getDefaultCell().setPadding(3);
				tablaB143.getDefaultCell().setUseAscender(true);
				tablaB143.getDefaultCell().setUseDescender(true);
				tablaB143.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaB143.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaB143.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaB143.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Número de personas",fontCabeceraTabla));
				tablaB143.addCell(encabezadoTablaB);				
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("¿Que información se comunica a los beneficiarios?",fontCabeceraTabla));
				tablaB143.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("¿Como se informa a la gente sobre la ejecución del proyecto/programa?",fontCabeceraTabla));
				tablaB143.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaB143.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Componente",fontCabeceraTabla));
				tablaB143.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaB143.addCell(encabezadoTablaB);

				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaB143()){

					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaB143.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaB143.addCell(datosTablaB);					
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(String.valueOf(tabla.getTareColumnGenericOne()),fontContenidoTablas));
					tablaB143.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaB143.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaB143.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaB143.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaB143.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaB143.addCell(datosTablaB);
					datosTablaB=new Paragraph();
				}
				document.add(tablaB143);

				Paragraph informacionOpcional = new Paragraph();
				informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				document.add(informacionOpcional);

				PdfPTable tablaOPB = new PdfPTable(new float[] { 7, 7,7 });
				tablaOPB.setWidthPercentage(100);
				tablaOPB.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaOPB.getDefaultCell().setPadding(3);
				tablaOPB.getDefaultCell().setUseAscender(true);
				tablaOPB.getDefaultCell().setUseDescender(true);
				tablaOPB.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaOPB.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaB=new Paragraph();				
				encabezadoTablaB.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
				tablaOPB.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
				tablaOPB.addCell(encabezadoTablaB);
				encabezadoTablaB=new Paragraph();	
				encabezadoTablaB.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaOPB.addCell(encabezadoTablaB);

				datosTablaB=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaOPB()){
					datosTablaB.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaOPB.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaOPB.addCell(datosTablaB);
					datosTablaB=new Paragraph();
					datosTablaB.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaOPB.addCell(datosTablaB);
				}
				document.add(tablaOPB);
			}
			//SALVAGUARDA C
			if(seguimientoSalvaguardas.isSalvaguardaC()){
				Paragraph salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaC.add(new Phrase("SALVAGUARDA C", fontTitulosSalvaguardas));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(0).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(0).isVaanYesnoAnswerValue())
					salvaguardaC.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaC.add(new Phrase("NO", fontContenido));					
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(1).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC201 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3 ,3,3,3});
				tablaC201.setWidthPercentage(100);
				tablaC201.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC201.getDefaultCell().setPadding(3);
				tablaC201.getDefaultCell().setUseAscender(true);
				tablaC201.getDefaultCell().setUseDescender(true);
				tablaC201.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC201.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				Paragraph encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Nro Mujeres Beneficiarias",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Hectáreas",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Presupuesto asignado a la actividad",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Tipo de acceso",fontCabeceraTabla));
				tablaC201.addCell(encabezadoTablaC);



				Paragraph datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC201()){

					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalTwo()),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaC201.addCell(datosTablaC);
					datosTablaC=new Paragraph();
				}
				document.add(tablaC201);

				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(2).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(1).isVaanYesnoAnswerValue())
					salvaguardaC.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaC.add(new Phrase("NO", fontContenido));					
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(3).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC211 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3,3});
				tablaC211.setWidthPercentage(100);
				tablaC211.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC211.getDefaultCell().setPadding(3);
				tablaC211.getDefaultCell().setUseAscender(true);
				tablaC211.getDefaultCell().setUseDescender(true);
				tablaC211.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC211.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Práctica o Saber ancestral",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("¿Cómo se reconocieron estas prácticas?",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("¿Cómo se han promovido estas prácticas?",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaC211.addCell(encabezadoTablaC);


				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC211()){

					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
					tablaC211.addCell(datosTablaC);						
					datosTablaC=new Paragraph();
				}
				document.add(tablaC211);

				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(4).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(2).isVaanYesnoAnswerValue())
					salvaguardaC.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaC.add(new Phrase("NO", fontContenido));					
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(5).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC241 = new PdfPTable(new float[] { 3,3, 3, 3,3 ,3});
				tablaC241.setWidthPercentage(100);
				tablaC241.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC241.getDefaultCell().setPadding(3);
				tablaC241.getDefaultCell().setUseAscender(true);
				tablaC241.getDefaultCell().setUseDescender(true);
				tablaC241.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC241.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaC241.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Organizaciones que participan",fontCabeceraTabla));
				tablaC241.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Num hombres",fontCabeceraTabla));
				tablaC241.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Num mujeres",fontCabeceraTabla));
				tablaC241.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Temática",fontCabeceraTabla));
				tablaC241.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaC241.addCell(encabezadoTablaC);



				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC241()){

					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaC241.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC241.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaC241.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaC241.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC241.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaC241.addCell(datosTablaC);												
					datosTablaC=new Paragraph();
				}
				document.add(tablaC241);

				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					

				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(6).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC242 = new PdfPTable(new float[] { 3, 3});
				tablaC242.setWidthPercentage(100);
				tablaC242.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC242.getDefaultCell().setPadding(3);
				tablaC242.getDefaultCell().setUseAscender(true);
				tablaC242.getDefaultCell().setUseDescender(true);
				tablaC242.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC242.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Medidas que se han tomado para motivar la participación de mujeres",fontCabeceraTabla));
				tablaC242.addCell(encabezadoTablaC);									
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaC242.addCell(encabezadoTablaC);

				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC242()){
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC242.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnNumberOne()),fontContenidoTablas));
					tablaC242.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC242.addCell(datosTablaC);
					datosTablaC=new Paragraph();
				}
				document.add(tablaC242);


				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(7).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(3).isVaanYesnoAnswerValue())
					salvaguardaC.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaC.add(new Phrase("NO", fontContenido));					
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(8).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC26 = new PdfPTable(new float[] { 3, 3, 3,3,3,3});
				tablaC26.setWidthPercentage(100);
				tablaC26.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC26.getDefaultCell().setPadding(3);
				tablaC26.getDefaultCell().setUseAscender(true);
				tablaC26.getDefaultCell().setUseDescender(true);
				tablaC26.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC26.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaC26.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaC26.addCell(encabezadoTablaC);					
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Representantes (nombres completos)",fontCabeceraTabla));
				tablaC26.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Cargo",fontCabeceraTabla));
				tablaC26.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
				tablaC26.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaC26.addCell(encabezadoTablaC);


				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC26()){
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaC26.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC26.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC26.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaC26.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaC26.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaC26.addCell(datosTablaC);
					datosTablaC=new Paragraph();
				}
				document.add(tablaC26);

				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(9).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(4).isVaanYesnoAnswerValue())
					salvaguardaC.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaC.add(new Phrase("NO", fontContenido));					
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(10).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC271 = new PdfPTable(new float[] {3, 3, 3, 3,3,3,3,3});
				tablaC271.setWidthPercentage(100);
				tablaC271.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC271.getDefaultCell().setPadding(3);
				tablaC271.getDefaultCell().setUseAscender(true);
				tablaC271.getDefaultCell().setUseDescender(true);
				tablaC271.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC271.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaC271.addCell(encabezadoTablaC);									
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaC271.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaC271.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaC271.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Objeto del convenio",fontCabeceraTabla));
				tablaC271.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Hectáreas bajo actividad REDD+",fontCabeceraTabla));
				tablaC271.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
				tablaC271.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaC271.addCell(encabezadoTablaC);

				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC271()){
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaC271.addCell(datosTablaC);										
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaC271.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaC271.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC271.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC271.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaC271.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaC271.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaC271.addCell(datosTablaC);
					datosTablaC=new Paragraph();
				}
				document.add(tablaC271);

				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(11).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC28 = new PdfPTable(new float[] { 3, 3, 3, 3, 3,3,3});
				tablaC28.setWidthPercentage(100);
				tablaC28.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC28.getDefaultCell().setPadding(3);
				tablaC28.getDefaultCell().setUseAscender(true);
				tablaC28.getDefaultCell().setUseDescender(true);
				tablaC28.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC28.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();		
				encabezadoTablaC.add(new Phrase("Entina",fontCabeceraTabla));
				tablaC28.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaC28.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Objeto de contratación",fontCabeceraTabla));
				tablaC28.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Figura de contratación",fontCabeceraTabla));
				tablaC28.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Presupuesto",fontCabeceraTabla));
				tablaC28.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Cómo se están promoviendo y fomentando el derecho laboral en pueblos indígenas a través del PDI, programa y/ proyecto",fontCabeceraTabla));
				tablaC28.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
				tablaC28.addCell(encabezadoTablaC);

				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC28()){
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaC28.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaC28.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC28.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo().equals("PERSONALPLANTA")?"Personal planta":"Personal consultoria",fontContenidoTablas));
					tablaC28.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaC28.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaC28.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaC28.addCell(datosTablaC);
					datosTablaC=new Paragraph();
				}
				document.add(tablaC28);

				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(12).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(5).isVaanYesnoAnswerValue())
					salvaguardaC.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaC.add(new Phrase("NO", fontContenido));					
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(13).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC291 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3,3});
				tablaC291.setWidthPercentage(100);
				tablaC291.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC291.getDefaultCell().setPadding(3);
				tablaC291.getDefaultCell().setUseAscender(true);
				tablaC291.getDefaultCell().setUseDescender(true);
				tablaC291.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC291.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);					
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Resultado",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link documento respaldo",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Objeto del acuerdo",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
				tablaC291.addCell(encabezadoTablaC);

				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC291()){
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaC291.addCell(datosTablaC);
					datosTablaC=new Paragraph();
				}
				document.add(tablaC291);

				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(14).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(6).isVaanYesnoAnswerValue())
					salvaguardaC.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaC.add(new Phrase("NO", fontContenido));					
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(15).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC301 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3,3});
				tablaC301.setWidthPercentage(100);
				tablaC301.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC301.getDefaultCell().setPadding(3);
				tablaC301.getDefaultCell().setUseAscender(true);
				tablaC301.getDefaultCell().setUseDescender(true);
				tablaC301.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC301.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);					
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Acciones reportadas que causan daños",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Medidas de remediación",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Medidas de compensación",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link de la Remediación",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link de la Compensación",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
				tablaC301.addCell(encabezadoTablaC);

				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC301()){
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnFive(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnSix(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaC301.addCell(datosTablaC);
					datosTablaC=new Paragraph();
				}
				document.add(tablaC301);

				salvaguardaC = new Paragraph();
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(16).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasC().get(7).isVaanYesnoAnswerValue())
					salvaguardaC.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaC.add(new Phrase("NO", fontContenido));					
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasC().get(17).getQuesContentQuestion(), fontTitulos));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				salvaguardaC.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaC);

				PdfPTable tablaC311 = new PdfPTable(new float[] { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,3});
				tablaC311.setWidthPercentage(100);
				tablaC311.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaC311.getDefaultCell().setPadding(3);
				tablaC311.getDefaultCell().setUseAscender(true);
				tablaC311.getDefaultCell().setUseDescender(true);
				tablaC311.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaC311.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);					
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Asistentes hombres",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Asistentes mujeres",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Componente",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link verificador participantes",fontCabeceraTabla));
				tablaC311.addCell(encabezadoTablaC);

				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaC311()){
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);					
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaC311.addCell(datosTablaC);
					datosTablaC=new Paragraph();
				}
				document.add(tablaC311);

				Paragraph informacionOpcional = new Paragraph();
				informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				document.add(informacionOpcional);

				PdfPTable tablaOPC = new PdfPTable(new float[] { 7, 7,7 });
				tablaOPC.setWidthPercentage(100);
				tablaOPC.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaOPC.getDefaultCell().setPadding(3);
				tablaOPC.getDefaultCell().setUseAscender(true);
				tablaOPC.getDefaultCell().setUseDescender(true);
				tablaOPC.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaOPC.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaC=new Paragraph();				
				encabezadoTablaC.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
				tablaOPC.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
				tablaOPC.addCell(encabezadoTablaC);
				encabezadoTablaC=new Paragraph();	
				encabezadoTablaC.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaOPC.addCell(encabezadoTablaC);

				datosTablaC=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaOPC()){
					datosTablaC.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaOPC.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaOPC.addCell(datosTablaC);
					datosTablaC=new Paragraph();
					datosTablaC.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaOPC.addCell(datosTablaC);
				}
				document.add(tablaOPC);

			}
			//SALVAGUARDA D
			if(seguimientoSalvaguardas.isSalvaguardaD()){
				Paragraph salvaguardaD = new Paragraph();
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaD.add(new Phrase("SALVAGUARDA D", fontTitulosSalvaguardas));
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaD.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasD().get(0).getQuesContentQuestion(), fontTitulos));
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasD().get(0).isVaanYesnoAnswerValue())
					salvaguardaD.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaD.add(new Phrase("NO", fontContenido));					
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));
				salvaguardaD.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasD().get(1).getQuesContentQuestion(), fontTitulos));
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaD);

				PdfPTable tablaD321 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3, 3, 3,3 ,3,3});
				tablaD321.setWidthPercentage(100);
				tablaD321.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaD321.getDefaultCell().setPadding(3);
				tablaD321.getDefaultCell().setUseAscender(true);
				tablaD321.getDefaultCell().setUseDescender(true);
				tablaD321.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaD321.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				Paragraph encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Espacio de difusión de la información",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Actores que participan en el proceso de acceso a la información",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Asistentes hombres",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Asistentes mujeres",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Componente",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaD321.addCell(encabezadoTablaD);




				Paragraph datosTablaD=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaD321()){

					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);					
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaD321.addCell(datosTablaD);						
					datosTablaD=new Paragraph();
				}
				document.add(tablaD321);

				salvaguardaD = new Paragraph();
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaD.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasD().get(2).getQuesContentQuestion(), fontTitulos));
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasD().get(1).isVaanYesnoAnswerValue())
					salvaguardaD.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaD.add(new Phrase("NO", fontContenido));					
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));
				salvaguardaD.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasD().get(3).getQuesContentQuestion(), fontTitulos));
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));
				salvaguardaD.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaD);

				PdfPTable tablaD331 = new PdfPTable(new float[] {3,3, 3, 3, 3,3 ,3,3,3});
				tablaD331.setWidthPercentage(100);
				tablaD331.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaD331.getDefaultCell().setPadding(3);
				tablaD331.getDefaultCell().setUseAscender(true);
				tablaD331.getDefaultCell().setUseDescender(true);
				tablaD331.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaD331.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Actor / Organización",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Actividad que realiza la comunidad",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Presupuesto asignado a la actividad",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Nivel de involucramiento",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Componente",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaD331.addCell(encabezadoTablaD);

				datosTablaD=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaD331()){

					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);						
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaD331.addCell(datosTablaD);											
					datosTablaD=new Paragraph();
				}
				document.add(tablaD331);

				Paragraph informacionOpcional = new Paragraph();
				informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				document.add(informacionOpcional);

				PdfPTable tablaOPD = new PdfPTable(new float[] { 7, 7,7 });
				tablaOPD.setWidthPercentage(100);
				tablaOPD.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaOPD.getDefaultCell().setPadding(3);
				tablaOPD.getDefaultCell().setUseAscender(true);
				tablaOPD.getDefaultCell().setUseDescender(true);
				tablaOPD.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaOPD.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaD=new Paragraph();				
				encabezadoTablaD.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
				tablaOPD.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
				tablaOPD.addCell(encabezadoTablaD);
				encabezadoTablaD=new Paragraph();	
				encabezadoTablaD.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaOPD.addCell(encabezadoTablaD);

				datosTablaD=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaOPD()){
					datosTablaD.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaOPD.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaOPD.addCell(datosTablaD);
					datosTablaD=new Paragraph();
					datosTablaD.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaOPD.addCell(datosTablaD);
				}
				document.add(tablaOPD);

			}	

			//SALVAGUARDA E
			if(seguimientoSalvaguardas.isSalvaguardaE()){
				Paragraph salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaE.add(new Phrase("SALVAGUARDA E", fontTitulosSalvaguardas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(0).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(0).isVaanYesnoAnswerValue())
					salvaguardaE.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaE.add(new Phrase("NO", fontContenido));					
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(1).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaE);

				PdfPTable tablaE341 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3,3,3 });
				tablaE341.setWidthPercentage(100);
				tablaE341.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaE341.getDefaultCell().setPadding(3);
				tablaE341.getDefaultCell().setUseAscender(true);
				tablaE341.getDefaultCell().setUseDescender(true);
				tablaE341.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaE341.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				Paragraph encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaE341.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaE341.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaE341.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaE341.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Actores involucrados clave",fontCabeceraTabla));
				tablaE341.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Hectáreas",fontCabeceraTabla));
				tablaE341.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Tipo de área consolidada",fontCabeceraTabla));
				tablaE341.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
				tablaE341.addCell(encabezadoTablaE);

				Paragraph datosTablaE=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE341()){

					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaE341.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaE341.addCell(datosTablaE);					
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaE341.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaE341.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaE341.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaE341.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaE341.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaE341.addCell(datosTablaE);
					datosTablaE=new Paragraph();
				}
				document.add(tablaE341);

				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(2).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(1).isVaanYesnoAnswerValue())
					salvaguardaE.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaE.add(new Phrase("NO", fontContenido));					
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(3).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaE);

				PdfPTable tablaE351 = new PdfPTable(new float[] { 3, 3, 3,3});
				tablaE351.setWidthPercentage(100);
				tablaE351.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaE351.getDefaultCell().setPadding(3);
				tablaE351.getDefaultCell().setUseAscender(true);
				tablaE351.getDefaultCell().setUseDescender(true);
				tablaE351.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaE351.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nivel",fontCabeceraTabla));
				tablaE351.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Herramienta (PDOT, ACUS, Ordenanza, etc)",fontCabeceraTabla));
				tablaE351.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
				tablaE351.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaE351.addCell(encabezadoTablaE);

				datosTablaE=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE351()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaE351.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaE351.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaE351.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaE351.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
				}
				document.add(tablaE351);

				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(4).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(2).isVaanYesnoAnswerValue())
					salvaguardaE.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaE.add(new Phrase("NO", fontContenido));					
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(5).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaE);

				PdfPTable tablaE361 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3,3});
				tablaE361.setWidthPercentage(100);
				tablaE361.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaE361.getDefaultCell().setPadding(3);
				tablaE361.getDefaultCell().setUseAscender(true);
				tablaE361.getDefaultCell().setUseDescender(true);
				tablaE361.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaE361.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);				
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Actores clave",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Hectareas",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
				tablaE361.addCell(encabezadoTablaE);

				datosTablaE=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE361()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);																											
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaE361.addCell(datosTablaE);
					datosTablaE=new Paragraph();
				}
				document.add(tablaE361);

				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(6).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(3).isVaanYesnoAnswerValue())
					salvaguardaE.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaE.add(new Phrase("NO", fontContenido));					
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(7).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaE);

				PdfPTable tablaE371 = new PdfPTable(new float[] {3, 3, 3, 3,3,3,3,3,3,3,3,3});
				tablaE371.setWidthPercentage(100);
				tablaE371.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaE371.getDefaultCell().setPadding(3);
				tablaE371.getDefaultCell().setUseAscender(true);
				tablaE371.getDefaultCell().setUseDescender(true);
				tablaE371.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaE371.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Temas",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Método",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Público",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro hombres",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro mujeres",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaE371.addCell(encabezadoTablaE);

				datosTablaE=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE371()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);																							
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);																							
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaE371.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
				}
				document.add(tablaE371);

				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(8).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaValoresRespuestasE().get(4).getVaanTextAnswerValue(),fontContenido));	
				document.add(salvaguardaE);

				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(9).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(5).isVaanYesnoAnswerValue())
					salvaguardaE.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaE.add(new Phrase("NO", fontContenido));					
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(10).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaE);

				PdfPTable tablaE381 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3});
				tablaE381.setWidthPercentage(100);
				tablaE381.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaE381.getDefaultCell().setPadding(3);
				tablaE381.getDefaultCell().setUseAscender(true);
				tablaE381.getDefaultCell().setUseDescender(true);
				tablaE381.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaE381.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaE381.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaE381.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaE381.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaE381.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Servicio",fontCabeceraTabla));
				tablaE381.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
				tablaE381.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaE381.addCell(encabezadoTablaE);

				datosTablaE=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE381()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaE381.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaE381.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaE381.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaE381.addCell(datosTablaE);																					
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaE381.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaE381.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaE381.addCell(datosTablaE);																																								
					datosTablaE=new Paragraph();
				}
				document.add(tablaE381);

				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(11).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasE().get(6).isVaanYesnoAnswerValue())
					salvaguardaE.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaE.add(new Phrase("NO", fontContenido));					
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(12).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaE);

				PdfPTable tablaE391 = new PdfPTable(new float[] { 3, 3, 3,3,3,3,3,3});
				tablaE391.setWidthPercentage(100);
				tablaE391.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaE391.getDefaultCell().setPadding(3);
				tablaE391.getDefaultCell().setUseAscender(true);
				tablaE391.getDefaultCell().setUseDescender(true);
				tablaE391.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaE391.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaE391.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaE391.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaE391.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaE391.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Recurso",fontCabeceraTabla));
				tablaE391.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Periodicidad",fontCabeceraTabla));
				tablaE391.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Componente",fontCabeceraTabla));
				tablaE391.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaE391.addCell(encabezadoTablaE);

				datosTablaE=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE391()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaE391.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaE391.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaE391.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaE391.addCell(datosTablaE);																					
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaE391.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaE391.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaE391.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaE391.addCell(datosTablaE);
					datosTablaE=new Paragraph();
				}
				document.add(tablaE391);

				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(13).getQuesContentQuestion(), fontTitulos));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaE.add(new Phrase("COBENEFICIOS POLÍTICAS Y GESTIÓN INSTITUCIONAL", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				salvaguardaE = new Paragraph();												
				salvaguardaE.add(new Phrase("SOCIALES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(14).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				PdfPTable valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3,3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Comunidad",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nacionalidad",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_1()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																						
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase( tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(15).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_2()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
																										
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(16).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_3()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();					
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);

				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(17).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Espacio de gobernanza",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_4()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();					
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();												
				salvaguardaE.add(new Phrase("AMBIENTALES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(18).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_5()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();					
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(19).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Riesgo abordado",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_6()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();					
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(20).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Capacidades fortalecidas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_7()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase("TRANSICIÓN A SISTEMAS PRODUCTIVOS SOSTENIBLES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				salvaguardaE = new Paragraph();												
				salvaguardaE.add(new Phrase("SOCIALES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(21).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_8()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																																										
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(22).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_9()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																																																
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(23).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Productos incentivados",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_10()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(24).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Organización fortalecida",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_11()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(25).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Asociación apoyada",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_12()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(26).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("% Incremento ingresos",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_13()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();												
				salvaguardaE.add(new Phrase("AMBIENTALES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(27).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_14()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(28).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_15()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(29).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_16()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(30).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_17()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase("MANEJO FORESTAL SOSTENIBLE", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				salvaguardaE = new Paragraph();												
				salvaguardaE.add(new Phrase("SOCIALES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(31).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Fuentes de ingreso apoyadas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_18()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(32).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Comunidad",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_19()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(33).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Capacidades fortalecidas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_20()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(34).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Iniciativa turismo apoyada",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_21()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(35).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Instrumento/mecanismo que mejora la gobernanza forestal",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_22()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(36).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nombre de las Organizaciones Apoyadas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_23()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(37).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nombre de las Organizaciones Apoyadas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_24()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();												
				salvaguardaE.add(new Phrase("AMBIENTALES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(38).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_25()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(39).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_26()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(40).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Cantón",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_27()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(41).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Especies protegidas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_28()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(42).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Especies protegidas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_29()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(43).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_30()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase("CONSERVACION Y RESTAURACION", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				salvaguardaE = new Paragraph();												
				salvaguardaE.add(new Phrase("SOCIALES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(44).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Usos alternativos",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Servicios valorados",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_31()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
																										
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(45).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nombre de las Organizaciones Apoyadas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_32()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();												
				salvaguardaE.add(new Phrase("AMBIENTALES", fontTitulos));	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(46).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_33()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																											
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(47).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_34()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(48).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_35()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(49).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Canton",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_36()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(50).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_37()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				
				salvaguardaE = new Paragraph();																	
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaE.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasE().get(51).getQuesContentQuestion(), fontContenidoTablas));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));
				salvaguardaE.add(new Phrase(Chunk.NEWLINE));	
				document.add(salvaguardaE);
				
				valoresCobeneficios = new PdfPTable(new float[] { 3, 3, 3, 3});
				valoresCobeneficios.setWidthPercentage(100);
				valoresCobeneficios.setHorizontalAlignment(Element.ALIGN_LEFT);
				valoresCobeneficios.getDefaultCell().setPadding(3);
				valoresCobeneficios.getDefaultCell().setUseAscender(true);
				valoresCobeneficios.getDefaultCell().setUseDescender(true);
				valoresCobeneficios.getDefaultCell().setBorderColor(BaseColor.WHITE);				
				valoresCobeneficios.getDefaultCell().setHorizontalAlignment(Element.ALIGN_MIDDLE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("PROVINCIA",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarios",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Beneficiarias",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Nro Hectareas",fontContenidoTablas));
				valoresCobeneficios.addCell(encabezadoTablaE);
				
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaE40_38()){
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberMens()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																	
					datosTablaE=new Paragraph();
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(String.valueOf( tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					valoresCobeneficios.addCell(datosTablaE);																					
					
					datosTablaE=new Paragraph();
				}
				document.add(valoresCobeneficios);
				

				Paragraph informacionOpcional = new Paragraph();
				informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				document.add(informacionOpcional);

				PdfPTable tablaOPE = new PdfPTable(new float[] { 7, 7,7 });
				tablaOPE.setWidthPercentage(100);
				tablaOPE.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaOPE.getDefaultCell().setPadding(3);
				tablaOPE.getDefaultCell().setUseAscender(true);
				tablaOPE.getDefaultCell().setUseDescender(true);
				tablaOPE.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaOPE.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaE=new Paragraph();				
				encabezadoTablaE.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
				tablaOPE.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
				tablaOPE.addCell(encabezadoTablaE);
				encabezadoTablaE=new Paragraph();	
				encabezadoTablaE.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaOPE.addCell(encabezadoTablaE);

				datosTablaE=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaOPE()){
					datosTablaE.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaOPE.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaOPE.addCell(datosTablaE);
					datosTablaE=new Paragraph();
					datosTablaE.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaOPE.addCell(datosTablaE);
				}
				document.add(tablaOPE);
			}	
			//SALVAGUARDA F

			if(seguimientoSalvaguardas.isSalvaguardaF()){
				Paragraph salvaguardaF = new Paragraph();
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaF.add(new Phrase("SALVAGUARDA F", fontTitulosSalvaguardas));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(0).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(0).isVaanYesnoAnswerValue())
					salvaguardaF.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaF.add(new Phrase("NO", fontContenido));					
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(1).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaF);

				PdfPTable tablaF411 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3 ,3,3});
				tablaF411.setWidthPercentage(100);
				tablaF411.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaF411.getDefaultCell().setPadding(3);
				tablaF411.getDefaultCell().setUseAscender(true);
				tablaF411.getDefaultCell().setUseDescender(true);
				tablaF411.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaF411.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				Paragraph encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaF411.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaF411.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaF411.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaF411.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaF411.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaF411.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Riesgo",fontCabeceraTabla));
				tablaF411.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaF411.addCell(encabezadoTablaF);

				Paragraph datosTablaF=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaF411()){

					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaF411.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaF411.addCell(datosTablaF);					
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaF411.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaF411.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaF411.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaF411.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaF411.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaF411.addCell(datosTablaF);
					datosTablaF=new Paragraph();
				}
				document.add(tablaF411);

				salvaguardaF = new Paragraph();
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(2).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(1).isVaanYesnoAnswerValue())
					salvaguardaF.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaF.add(new Phrase("NO", fontContenido));					
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(3).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaF);

				PdfPTable tablaF421 = new PdfPTable(new float[] {3, 3, 3, 3,3 ,3, 3 ,3,3,3});
				tablaF421.setWidthPercentage(100);
				tablaF421.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaF421.getDefaultCell().setPadding(3);
				tablaF421.getDefaultCell().setUseAscender(true);
				tablaF421.getDefaultCell().setUseDescender(true);
				tablaF421.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaF421.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Riesgo asociado",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Medida tomada",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Componente",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaF421.addCell(encabezadoTablaF);

				datosTablaF=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaF421()){

					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);					
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaF421.addCell(datosTablaF);
					datosTablaF=new Paragraph();
				}
				document.add(tablaF421);

				salvaguardaF = new Paragraph();
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(4).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(2).isVaanYesnoAnswerValue())
					salvaguardaF.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaF.add(new Phrase("NO", fontContenido));					
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(5).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaF);

				PdfPTable tablaF431 = new PdfPTable(new float[] { 5, 3, 3,3 ,3, 3,3});
				tablaF431.setWidthPercentage(100);
				tablaF431.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaF431.getDefaultCell().setPadding(3);
				tablaF431.getDefaultCell().setUseAscender(true);
				tablaF431.getDefaultCell().setUseDescender(true);
				tablaF431.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaF431.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Sistemas REDD+",fontCabeceraTabla));
				tablaF431.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Acciones tomadas",fontCabeceraTabla));
				tablaF431.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Recursos invertidos",fontCabeceraTabla));
				tablaF431.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Actores clave",fontCabeceraTabla));
				tablaF431.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Resultado",fontCabeceraTabla));
				tablaF431.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Componente",fontCabeceraTabla));
				tablaF431.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaF431.addCell(encabezadoTablaF);

				datosTablaF=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaF431()){

					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaF431.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaF431.addCell(datosTablaF);					
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaF431.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaF431.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaF431.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaF431.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaF431.addCell(datosTablaF);						
					datosTablaF=new Paragraph();
				}
				document.add(tablaF431);

				salvaguardaF = new Paragraph();
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(6).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(3).isVaanYesnoAnswerValue())
					salvaguardaF.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaF.add(new Phrase("NO", fontContenido));					
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(7).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaF);

				PdfPTable tablaF441 = new PdfPTable(new float[] { 3, 3, 3,3 ,3, 3 ,3,3,3,3});
				tablaF441.setWidthPercentage(100);
				tablaF441.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaF441.getDefaultCell().setPadding(3);
				tablaF441.getDefaultCell().setUseAscender(true);
				tablaF441.getDefaultCell().setUseDescender(true);
				tablaF441.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaF441.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Riesgo",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Actividad para mitigar",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Componente",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaF441.addCell(encabezadoTablaF);

				datosTablaF=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaF441()){

					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);					
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaF441.addCell(datosTablaF);
					datosTablaF=new Paragraph();
				}
				document.add(tablaF441);

				salvaguardaF = new Paragraph();
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(8).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasF().get(4).isVaanYesnoAnswerValue())
					salvaguardaF.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaF.add(new Phrase("NO", fontContenido));					
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasF().get(9).getQuesContentQuestion(), fontTitulos));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				salvaguardaF.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaF);

				PdfPTable tablaF452 = new PdfPTable(new float[] { 5, 3, 3,3,3 });
				tablaF452.setWidthPercentage(100);
				tablaF452.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaF452.getDefaultCell().setPadding(3);
				tablaF452.getDefaultCell().setUseAscender(true);
				tablaF452.getDefaultCell().setUseDescender(true);
				tablaF452.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaF452.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaF452.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Sistemas",fontCabeceraTabla));
				tablaF452.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Nro. Casos reportados",fontCabeceraTabla));
				tablaF452.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Nro. Casos atendidos",fontCabeceraTabla));
				tablaF452.addCell(encabezadoTablaF);										
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaF452.addCell(encabezadoTablaF);

				datosTablaF=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaF452()){

					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaF452.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaF452.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(String.valueOf(tabla.getTareColumnGenericOne()),fontContenidoTablas));
					tablaF452.addCell(datosTablaF);					
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(String.valueOf(tabla.getTareColumnGenericTwo()),fontContenidoTablas));
					tablaF452.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaF452.addCell(datosTablaF);												
					datosTablaF=new Paragraph();
				}
				document.add(tablaF452);

				Paragraph informacionOpcional = new Paragraph();
				informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				document.add(informacionOpcional);

				PdfPTable tablaOPF = new PdfPTable(new float[] { 7, 7,7 });
				tablaOPF.setWidthPercentage(100);
				tablaOPF.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaOPF.getDefaultCell().setPadding(3);
				tablaOPF.getDefaultCell().setUseAscender(true);
				tablaOPF.getDefaultCell().setUseDescender(true);
				tablaOPF.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaOPF.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaF=new Paragraph();				
				encabezadoTablaF.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
				tablaOPF.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
				tablaOPF.addCell(encabezadoTablaF);
				encabezadoTablaF=new Paragraph();	
				encabezadoTablaF.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaOPF.addCell(encabezadoTablaF);

				datosTablaF=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaOPF()){
					datosTablaF.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaOPF.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaOPF.addCell(datosTablaF);
					datosTablaF=new Paragraph();
					datosTablaF.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaOPF.addCell(datosTablaF);
				}
				document.add(tablaOPF);
			}
			//SALVAGUARDA G

			if(seguimientoSalvaguardas.isSalvaguardaG()){
				Paragraph salvaguardaG = new Paragraph();
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));	
				salvaguardaG.add(new Phrase("SALVAGUARDA G", fontTitulosSalvaguardas));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(0).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(0).isVaanYesnoAnswerValue())
					salvaguardaG.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaG.add(new Phrase("NO", fontContenido));					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(1).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaG);

				PdfPTable tablaG461 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3,3});
				tablaG461.setWidthPercentage(100);
				tablaG461.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaG461.getDefaultCell().setPadding(3);
				tablaG461.getDefaultCell().setUseAscender(true);
				tablaG461.getDefaultCell().setUseDescender(true);
				tablaG461.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaG461.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				Paragraph encabezadoTablaG=new Paragraph();
				encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaG461.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Responsable del estudio",fontCabeceraTabla));
				tablaG461.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Autores de la investigación",fontCabeceraTabla));
				tablaG461.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Riesgo Principal Identificado",fontCabeceraTabla));
				tablaG461.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nombre del estudio",fontCabeceraTabla));
				tablaG461.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Componente",fontCabeceraTabla));
				tablaG461.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Verificador",fontCabeceraTabla));
				tablaG461.addCell(encabezadoTablaG);


				Paragraph datosTablaG=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaG461()){

					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaG461.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaG461.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaG461.addCell(datosTablaG);					
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaG461.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaG461.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaG461.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaG461.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
				}
				document.add(tablaG461);

				salvaguardaG = new Paragraph();					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(2).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(1).isVaanYesnoAnswerValue())
					salvaguardaG.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaG.add(new Phrase("NO", fontContenido));					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(3).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaG);

				PdfPTable tablaG471 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3});
				tablaG471.setWidthPercentage(100);
				tablaG471.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaG471.getDefaultCell().setPadding(3);
				tablaG471.getDefaultCell().setUseAscender(true);
				tablaG471.getDefaultCell().setUseDescender(true);
				tablaG471.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaG471.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Actividad",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nro hombres",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nro mujeres",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaG471.addCell(encabezadoTablaG);

				datosTablaG=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaG471()){

					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);					
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaG471.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
				}
				document.add(tablaG471);

				salvaguardaG = new Paragraph();					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(5).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(2).isVaanYesnoAnswerValue())
					salvaguardaG.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaG.add(new Phrase("NO", fontContenido));					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(6).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaG);

				PdfPTable tablaG481 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3});
				tablaG481.setWidthPercentage(100);
				tablaG481.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaG481.getDefaultCell().setPadding(3);
				tablaG481.getDefaultCell().setUseAscender(true);
				tablaG481.getDefaultCell().setUseDescender(true);
				tablaG481.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaG481.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Institución acompaña",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Actividad ilicita reportada",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Resultado",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Fecha",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaG481.addCell(encabezadoTablaG);

				datosTablaG=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaG481()){

					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);					
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(Fechas.cambiarFormato(tabla.getTareColumnNine(),"yyyy-MM-dd"),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaG481.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
				}
				document.add(tablaG481);

				salvaguardaG = new Paragraph();					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(7).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(3).isVaanYesnoAnswerValue())
					salvaguardaG.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaG.add(new Phrase("NO", fontContenido));					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(8).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaG);

				PdfPTable tablaG491 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3,3,3});
				tablaG491.setWidthPercentage(100);
				tablaG491.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaG491.getDefaultCell().setPadding(3);
				tablaG491.getDefaultCell().setUseAscender(true);
				tablaG491.getDefaultCell().setUseDescender(true);
				tablaG491.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaG491.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Organización Beneficiaria",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Tipo de incentivo",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nro Mujeres Beneficiarias",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Support value chain",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Componente",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaG491.addCell(encabezadoTablaG);

				datosTablaG=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaG491()){

					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);					
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenericoTres(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);
					datosTablaG=new Paragraph();					
					datosTablaG.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);
					datosTablaG=new Paragraph();					
					datosTablaG.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaG491.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
				}
				document.add(tablaG491);

				salvaguardaG = new Paragraph();					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(9).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(4).isVaanYesnoAnswerValue())
					salvaguardaG.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaG.add(new Phrase("NO", fontContenido));					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(10).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaG);

				PdfPTable tablaG501 = new PdfPTable(new float[] { 3, 3, 3,3,3});
				tablaG501.setWidthPercentage(100);
				tablaG501.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaG501.getDefaultCell().setPadding(3);
				tablaG501.getDefaultCell().setUseAscender(true);
				tablaG501.getDefaultCell().setUseDescender(true);
				tablaG501.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaG501.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Actividad",fontCabeceraTabla));
				tablaG501.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Presupuesto",fontCabeceraTabla));
				tablaG501.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Resultado",fontCabeceraTabla));
				tablaG501.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Componente",fontCabeceraTabla));
				tablaG501.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaG501.addCell(encabezadoTablaG);

				datosTablaG=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaG501()){
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaG501.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(String.valueOf(tabla.getTareColumnDecimalOne()),fontContenidoTablas));
					tablaG501.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaG501.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaG501.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaG501.addCell(datosTablaG);																		
					datosTablaG=new Paragraph();
				}
				document.add(tablaG501);

				salvaguardaG = new Paragraph();					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(11).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(5).isVaanYesnoAnswerValue())
					salvaguardaG.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaG.add(new Phrase("NO", fontContenido));					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(12).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				document.add(salvaguardaG);

				PdfPTable tablaG511 = new PdfPTable(new float[] { 3, 3, 3,3 ,3,3, 3, 3,3 ,3,3,3});
				tablaG511.setWidthPercentage(100);
				tablaG511.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaG511.getDefaultCell().setPadding(3);
				tablaG511.getDefaultCell().setUseAscender(true);
				tablaG511.getDefaultCell().setUseDescender(true);
				tablaG511.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaG511.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Provincia",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Cantón",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Parroquia",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Etnia",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nacionalidad",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Comunidad",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nro hombres",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Nro mujeres",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Monitoreo remoto",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Monitoreo in situ",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Periodicidad",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Componente",fontCabeceraTabla));
				tablaG511.addCell(encabezadoTablaG);


				datosTablaG=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaG512()){

					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareProvincia(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareCanton(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);					
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareParroquia(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenerico(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareGenericoDos(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(String.valueOf(tabla.getTareColumnNumberMens()),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(String.valueOf(tabla.getTareColumnNumberWomen()),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);						
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareComponente(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnFour(),fontContenidoTablas));
					tablaG511.addCell(datosTablaG);						
					datosTablaG=new Paragraph();

				}
				document.add(tablaG511);

				salvaguardaG = new Paragraph();					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));					
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(13).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				if(seguimientoSalvaguardas.getListaValoresRespuestasG().get(6).isVaanYesnoAnswerValue())
					salvaguardaG.add(new Phrase("SI", fontTitulos));
				else
					salvaguardaG.add(new Phrase("NO", fontContenido));					
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaPreguntasG().get(14).getQuesContentQuestion(), fontTitulos));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(Chunk.NEWLINE));
				salvaguardaG.add(new Phrase(seguimientoSalvaguardas.getListaValoresRespuestasG().get(7).getVaanTextAnswerValue(),fontContenido));
				document.add(salvaguardaG);

				Paragraph informacionOpcional = new Paragraph();
				informacionOpcional.add(new Phrase("INFORMACION ADICIONAL OPCIONAL", fontTitulos));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				informacionOpcional.add(new Phrase(Chunk.NEWLINE));
				document.add(informacionOpcional);

				PdfPTable tablaOPG = new PdfPTable(new float[] { 7, 7,7 });
				tablaOPG.setWidthPercentage(100);
				tablaOPG.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaOPG.getDefaultCell().setPadding(3);
				tablaOPG.getDefaultCell().setUseAscender(true);
				tablaOPG.getDefaultCell().setUseDescender(true);
				tablaOPG.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaOPG.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

				encabezadoTablaG=new Paragraph();				
				encabezadoTablaG.add(new Phrase("Actividad que aporta a la salvaguarda",fontCabeceraTabla));
				tablaOPG.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Logro alcanzado que se reporta",fontCabeceraTabla));
				tablaOPG.addCell(encabezadoTablaG);
				encabezadoTablaG=new Paragraph();	
				encabezadoTablaG.add(new Phrase("Link verificador",fontCabeceraTabla));
				tablaOPG.addCell(encabezadoTablaG);

				datosTablaG=new Paragraph();
				for(TableResponses tabla: seguimientoSalvaguardas.getTablaSalvaguardaOPG()){
					datosTablaG.add(new Phrase(tabla.getTareColumnOne(),fontContenidoTablas));
					tablaOPG.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTwo(),fontContenidoTablas));
					tablaOPG.addCell(datosTablaG);
					datosTablaG=new Paragraph();
					datosTablaG.add(new Phrase(tabla.getTareColumnTree(),fontContenidoTablas));
					tablaOPG.addCell(datosTablaG);
				}
				document.add(tablaOPG);

				Paragraph resumenEjecutivo = new Paragraph();
				resumenEjecutivo.add(new Phrase("RESUMEN EJECUTIVO", fontTitulos));
				resumenEjecutivo.add(new Phrase(Chunk.NEWLINE));
				resumenEjecutivo.add(new Phrase(Chunk.NEWLINE));
				document.add(resumenEjecutivo);

				PdfPTable tablaRE = new PdfPTable(new float[] { 10});
				tablaRE.setWidthPercentage(100);
				tablaRE.setHorizontalAlignment(Element.ALIGN_LEFT);
				tablaRE.getDefaultCell().setPadding(3);
				tablaRE.getDefaultCell().setUseAscender(true);
				tablaRE.getDefaultCell().setUseDescender(true);
				tablaRE.getDefaultCell().setBorderColor(BaseColor.BLACK);				
				tablaRE.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				
				datosTablaG=new Paragraph();
				datosTablaG.add(new Phrase(seguimientoSalvaguardas.getAdvanceExecutionSafeguards().getAdexExecutiveSummary(),fontContenidoTablas ));
				tablaRE.addCell(datosTablaG);
				document.add(tablaRE);

			}

			Paragraph saltoLinea=new Paragraph();
			saltoLinea.add(new Phrase(Chunk.NEWLINE));

			document.add(saltoLinea);


			document.close();
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			response.setContentType("application/pdf");				
			response.setHeader("Content-Disposition","attachment; filename=" + new StringBuilder().append("resumenSalvaguardas").append(".pdf").toString());				
			response.getOutputStream().write(Archivos.getBytesFromFile(new File(directorioArchivoPDF)));
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();

		}catch(IOException e){
			e.printStackTrace();
		} catch (DocumentException e) {			
			e.printStackTrace();
		} 

	}
	/**
	 * Reporte PDF de genero del proyecto seleccionado
	 * @param directorioArchivoPDF
	 * @param seguimientoSalvaguardas
	 */
	public static void reporteGenero(String directorioArchivoPDF, SeguimientoGeneroBean beanGenero){
		try{
			String tema="";			
			Document document = new Document();
			document.setPageSize(PageSize.A4);
			document.setMargins(35, 35, 35, 35);
			document.setMarginMirroring(true);
			PdfWriter writer =PdfWriter.getInstance(document, new FileOutputStream(directorioArchivoPDF));
			Rectangle rect = new Rectangle(100, 30, 500, 800);
			writer.setBoxSize("art", rect);
			HeaderFooterPageEvent event = new HeaderFooterPageEvent();
//			event.valoresPiePagina("", "Pag: ");
			writer.setPageEvent(event);
			document.open();

			Font fontContenido = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
			Font fontTitulos = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 7);
			Font fontTitulosSalvaguardas = FontFactory.getFont(FontFactory.HELVETICA_BOLD.toString(), 12);
			Font fontCabeceraTabla = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
			Font fontContenidoTablas = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
			Paragraph parrafoHoja = new Paragraph();
			parrafoHoja.add(new Phrase("RESUMEN DE GENERO", fontTitulosSalvaguardas));
			parrafoHoja.add(new Phrase(Chunk.NEWLINE));
			parrafoHoja.setAlignment(Element.ALIGN_CENTER);
			parrafoHoja.add(new Phrase(Chunk.NEWLINE));			
			document.add(parrafoHoja);

			PdfPTable tablaCabecera = new PdfPTable(2);
			PdfPCell celda = new PdfPCell(new Phrase("Título del Plan de implementación, Programa o Proyecto:", fontTitulos));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablaCabecera.addCell(celda);
			celda = new PdfPCell(new Phrase(beanGenero.getProyectoSeleccionado().getProjTitle(), fontContenido));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaCabecera.addCell(celda);

			celda = new PdfPCell(new Phrase("Socio implementador: ", fontTitulos));			
			celda.setBorderColor(BaseColor.WHITE);
			celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablaCabecera.addCell(celda);
			
			if(beanGenero.getSocioImplementador()!=null)
				celda = new PdfPCell(new Phrase(beanGenero.getSocioImplementador().getPartName(), fontContenido));
			else
				celda = new PdfPCell(new Phrase("", fontContenido));

			celda.setBorderColor(BaseColor.WHITE);
			celda.setHorizontalAlignment(Element.ALIGN_LEFT);			
			tablaCabecera.addCell(celda);
			
			celda = new PdfPCell(new Phrase("Periodo a reportar: ", fontTitulos));			
			celda.setBorderColor(BaseColor.WHITE);
			celda.setHorizontalAlignment(Element.ALIGN_RIGHT);
			tablaCabecera.addCell(celda);
			
			celda = new PdfPCell(new Phrase(beanGenero.getAdvanceExecutionSafeguards().getAdexTermFrom().substring(0, 4).concat(" Enero-Diciembre"), fontContenido));
			celda.setBorderColor(BaseColor.WHITE);
			celda.setHorizontalAlignment(Element.ALIGN_LEFT);
			tablaCabecera.addCell(celda);
			
			document.add(tablaCabecera);

			Paragraph temaAbarcado = new Paragraph();
			temaAbarcado.add(new Phrase(Chunk.NEWLINE));
			temaAbarcado.add(new Phrase("Seguimiento de acciones relacionadas a genero: ", fontTitulos));
			temaAbarcado.add(new Phrase(tema,fontContenido));
			temaAbarcado.add(new Phrase(Chunk.NEWLINE));
			temaAbarcado.add(new Phrase(Chunk.NEWLINE));
			document.add(temaAbarcado);
			
			PdfPTable tabla1 = new PdfPTable(new float[] { 5, 5, 5, 5 ,5 });
			tabla1.setWidthPercentage(100);
			tabla1.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabla1.getDefaultCell().setPadding(3);
			tabla1.getDefaultCell().setUseAscender(true);
			tabla1.getDefaultCell().setUseDescender(true);
			tabla1.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tabla1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

			Paragraph encabezadoTabla1=new Paragraph();
			encabezadoTabla1.add(new Phrase("Temas",fontCabeceraTabla));
			tabla1.addCell(encabezadoTabla1);
			encabezadoTabla1=new Paragraph();
			encabezadoTabla1.add(new Phrase("Línea de acción",fontCabeceraTabla));
			tabla1.addCell(encabezadoTabla1);
			encabezadoTabla1=new Paragraph();	
			encabezadoTabla1.add(new Phrase("Indicador",fontCabeceraTabla));
			tabla1.addCell(encabezadoTabla1);			
			encabezadoTabla1=new Paragraph();	
			encabezadoTabla1.add(new Phrase("Valor alcanzado",fontCabeceraTabla));
			tabla1.addCell(encabezadoTabla1);
			encabezadoTabla1=new Paragraph();	
			encabezadoTabla1.add(new Phrase("Acciones implementadas",fontCabeceraTabla));
			tabla1.addCell(encabezadoTabla1);
			
						
			Paragraph datosTabla1;
			for (AdvanceExecutionProjectGender ga : beanGenero.getListaLineasGenero()) {						
				datosTabla1=new Paragraph();
				datosTabla1.add(new Phrase(ga.getProjectGenderIndicator().getProjectsGenderInfo().getCataId().getCatalogsType().getCatyDescription(),fontContenidoTablas));
				tabla1.addCell(datosTabla1);

				datosTabla1=new Paragraph();
				datosTabla1.add(new Phrase(ga.getProjectGenderIndicator().getProjectsGenderInfo().getCataId().getCataText2().equals("Otro")?ga.getProjectGenderIndicator().getProjectsGenderInfo().getPginOtherLine() :ga.getProjectGenderIndicator().getProjectsGenderInfo().getCataId().getCataText2() ,fontContenidoTablas));
				tabla1.addCell(datosTabla1);
				
				datosTabla1=new Paragraph();				
				datosTabla1.add(new Phrase(ga.getProjectGenderIndicator().getIndicators().getIndiId()==0?ga.getProjectGenderIndicator().getPgigAnotherIndicator():ga.getProjectGenderIndicator().getIndicators().getIndiDescription() ,fontContenidoTablas));
				tabla1.addCell(datosTabla1);

				datosTabla1=new Paragraph();
				StringBuilder valorIndicador=new StringBuilder();
				if(ga.getProjectGenderIndicator().getIndicators().getIndiType().equals("G")){
					if(ga.getProjectGenderIndicator().getIndicators().getIndiId() == 9 || ga.getProjectGenderIndicator().getIndicators().getIndiId() == 10)
						valorIndicador.append("Valor Hombres: ").append(ga.getAepgValueReachedOne()).append(" Valor Mujeres: ").append(ga.getAepgValueReachedTwo());
					else
						valorIndicador.append("Nro Hombres: ").append(ga.getAepgValueReachedOne()).append(" Nro Mujeres: ").append(ga.getAepgValueReachedTwo());
				}else if(ga.getProjectGenderIndicator().getIndicators().getIndiType().equals("N")){
					valorIndicador.append("Valor: ").append(ga.getAepgValueReachedOne());
				}else if(ga.getProjectGenderIndicator().getIndicators().getIndiType().equals("B")){
					if(ga.getAepgValueReachedOne()!=null)
						valorIndicador.append("Valor: ").append(ga.getAepgValueReachedOne()==1?"Si":"No");
					else
						valorIndicador.append("Valor: ");
				}else if(ga.getProjectGenderIndicator().getIndicators().getIndiType().equals("O")){
					valorIndicador.append("Valor: ").append(ga.getAepgValueReachedAnotherIndicator());
				}
				datosTabla1.add(new Phrase(valorIndicador.toString(),fontContenidoTablas));
				tabla1.addCell(datosTabla1);
				datosTabla1=new Paragraph();
				datosTabla1.add(new Phrase(ga.getAepgActionsDone() ,fontContenidoTablas));
				tabla1.addCell(datosTabla1);
			}
			document.add(tabla1);
			

			
			Paragraph preguntas = new Paragraph();
			preguntas.add(new Phrase(Chunk.NEWLINE));
			preguntas.add(new Phrase(beanGenero.getListaPreguntas().get(0) .getQuesContentQuestion(), fontTitulos));
			preguntas.add(new Phrase(Chunk.NEWLINE));
			if(beanGenero.getListaValoresRespuestas().get(0).isVaanYesnoAnswerValue())
				preguntas.add(new Phrase("SI", fontTitulos));
			else
				preguntas.add(new Phrase("NO", fontContenido));					
			preguntas.add(new Phrase(Chunk.NEWLINE));			
			preguntas.add(new Phrase(Chunk.NEWLINE));
			document.add(preguntas);
			
			PdfPTable tabla3 = new PdfPTable(new float[] { 3, 3, 3 , 3, 3, 3, 3, 3 , 3,3, 3, 3, 3  });
			tabla3.setWidthPercentage(100);
			tabla3.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabla3.getDefaultCell().setPadding(3);
			tabla3.getDefaultCell().setUseAscender(true);
			tabla3.getDefaultCell().setUseDescender(true);
			tabla3.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tabla3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
			Paragraph encabezadoTabla=new Paragraph();					
			encabezadoTabla.add(new Phrase("Provincia",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Cantón",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Parroquia",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Etnia",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Comunidad",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Espacio identificado",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Tipo de organización",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Fin / rol de la organización",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Acciones implementadas para fortalecer a la organización",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Nro Mujeres beneficiarias",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Link verificador",fontCabeceraTabla));
			tabla3.addCell(encabezadoTabla);
			
			Paragraph datosTabla3;
			for(TableResponses genero:beanGenero.getTablaRespuestas3()){
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareProvincia(),fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareCanton() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareParroquia() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareGenerico(),fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareGenericoDos() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareColumnOne() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareColumnTwo() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareColumnTree() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareColumnFour() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareColumnFive() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(String.valueOf(genero.getTareColumnNumberSix()) ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(String.valueOf(genero.getTareColumnNumberSeven()) ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
				
				datosTabla3=new Paragraph();
				datosTabla3.add(new Phrase(genero.getTareColumnSix() ,fontContenidoTablas));
				tabla3.addCell(datosTabla3);
			}
			document.add(tabla3);
			
			
			preguntas = new Paragraph();
			preguntas.add(new Phrase(Chunk.NEWLINE));
			preguntas.add(new Phrase(Chunk.NEWLINE));
			preguntas.add(new Phrase(beanGenero.getListaPreguntas().get(1) .getQuesContentQuestion(), fontTitulos));
			preguntas.add(new Phrase(Chunk.NEWLINE));						
			preguntas.add(new Phrase(Chunk.NEWLINE));
			document.add(preguntas);
			
			PdfPTable tabla6 = new PdfPTable(new float[] { 3, 3, 3 , 3, 3, 3, 3, 3 , 3,3, 3 });
			tabla6.setWidthPercentage(100);
			tabla6.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabla6.getDefaultCell().setPadding(3);
			tabla6.getDefaultCell().setUseAscender(true);
			tabla6.getDefaultCell().setUseDescender(true);
			tabla6.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tabla6.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
			encabezadoTabla=new Paragraph();					
			encabezadoTabla.add(new Phrase("Provincia",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Cantón",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Parroquia",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Etnia",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Comunidad",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Nombre de la lideresa identificada",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Espacio de Diálogo/Participación",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Rol que cumple en el espacio de diálogo/participación",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Acciones de fortalecimiento de la lideresa en el espacio de diálogo/participación",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);			
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Link verificador",fontCabeceraTabla));
			tabla6.addCell(encabezadoTabla);
			
			Paragraph datosTabla6;
			for(TableResponses genero:beanGenero.getTablaRespuestas6()){
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareProvincia(),fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareCanton() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareParroquia() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareGenerico(),fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareGenericoDos() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareColumnOne() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareColumnTwo() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);								
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareColumnTree() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareColumnFour() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareColumnFive() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);
				datosTabla6=new Paragraph();
				datosTabla6.add(new Phrase(genero.getTareColumnSix() ,fontContenidoTablas));
				tabla6.addCell(datosTabla6);
			}
			document.add(tabla6);
			
			preguntas = new Paragraph();
			preguntas.add(new Phrase(Chunk.NEWLINE));
			preguntas.add(new Phrase(Chunk.NEWLINE));
			preguntas.add(new Phrase(beanGenero.getListaPreguntas().get(2) .getQuesContentQuestion(), fontTitulos));
			preguntas.add(new Phrase(Chunk.NEWLINE));						
			preguntas.add(new Phrase(Chunk.NEWLINE));
			document.add(preguntas);
			
			PdfPTable tabla7 = new PdfPTable(new float[] { 3, 3, 3 , 3, 3, 3, 3, 3 , 3,3, 3 });
			tabla7.setWidthPercentage(100);
			tabla7.setHorizontalAlignment(Element.ALIGN_LEFT);
			tabla7.getDefaultCell().setPadding(3);
			tabla7.getDefaultCell().setUseAscender(true);
			tabla7.getDefaultCell().setUseDescender(true);
			tabla7.getDefaultCell().setBorderColor(BaseColor.BLACK);				
			tabla7.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			
			encabezadoTabla=new Paragraph();					
			encabezadoTabla.add(new Phrase("Provincia",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Cantón",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Parroquia",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();	
			encabezadoTabla.add(new Phrase("Etnia",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Nacionalidad",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Comunidad",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Acción",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Resultado",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Nro Mujeres beneficiarias",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Nro Hombres beneficiarios",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);			
			encabezadoTabla=new Paragraph();
			encabezadoTabla.add(new Phrase("Link verificador",fontCabeceraTabla));
			tabla7.addCell(encabezadoTabla);
			
			Paragraph datosTabla7;
			for(TableResponses genero:beanGenero.getTablaRespuestas7()){
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareProvincia(),fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareCanton() ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareParroquia() ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareGenerico(),fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareGenericoDos() ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareColumnOne() ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareColumnTwo() ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);								
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareColumnTree() ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(String.valueOf(genero.getTareColumnNumberSix()) ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(String.valueOf(genero.getTareColumnNumberSeven()) ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				datosTabla7=new Paragraph();
				datosTabla7.add(new Phrase(genero.getTareColumnFour() ,fontContenidoTablas));
				tabla7.addCell(datosTabla7);
				
			}
			document.add(tabla7);
			
			
			
			Paragraph saltoLinea=new Paragraph();
			saltoLinea.add(new Phrase(Chunk.NEWLINE));

			document.add(saltoLinea);


			document.close();
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
			response.setContentType("application/pdf");				
			response.setHeader("Content-Disposition","attachment; filename=" + new StringBuilder().append("seguimientoGenero").append(".pdf").toString());				
			response.getOutputStream().write(Archivos.getBytesFromFile(new File(directorioArchivoPDF)));
			response.getOutputStream().flush();
			response.getOutputStream().close();
			context.responseComplete();

		}catch(IOException e){
			e.printStackTrace();
		} catch (DocumentException e) {			
			e.printStackTrace();
		} 

	}

	
		
}

