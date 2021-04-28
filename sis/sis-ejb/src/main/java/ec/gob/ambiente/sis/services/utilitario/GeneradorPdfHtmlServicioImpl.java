/**
@autor proamazonia [Christian Báez]  26 abr. 2021

**/
package ec.gob.ambiente.sis.services.utilitario;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.ejb.Stateless;

import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.layout.HtmlPageBreak;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;

@Stateless
public class GeneradorPdfHtmlServicioImpl implements Serializable{


	private static final long serialVersionUID = 1L;

	public String procesar(String html, Object dto) {
		Class<?> noparams[] = {};
		Field[] atributos = dto.getClass().getDeclaredFields();
		for (int i = 0; i < atributos.length; i++) {
			String nombre = atributos[i].getName();
			Method method;
			try {
				Object[] parametros = {};
				if (!"serialVersionUID".equals(nombre)) {
					method = dto.getClass().getDeclaredMethod("get" + nombre.substring(0, 1).toUpperCase() + nombre.substring(1, nombre.length()), noparams);
					Object resultado = method.invoke(dto, parametros);
					if (resultado != null) {
						html = html.replace("{P:" + nombre + "}", (String) resultado);
					} else {
						html = html.replace("{P:" + nombre + "}", "&nbsp;");
					}
				}
			} catch (NoSuchMethodException e) {
				System.out.println("No existe el campo: " + nombre);
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}
		return html;
	}

	/**
	 * Metodo para escribir el archivo en fisico
	 * 
	 * @param bFile
	 *            archivo de reporte
	 * @param fileDest
	 *            url destino del reporte
	 */
	public void escribirArchivo(byte[] bFile, String fileDest) {

		try (FileOutputStream fileOuputStream = new FileOutputStream(fileDest)) {
			fileOuputStream.write(bFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

//	/**
//	 * Metodo para crear el pdf
//	 * 
//	 * @param html
//	 *            archivo ahtml
//	 * @exception IOException
//	 *                excepcion
//	 */
//	public byte[] crearDocumentoPdf(String html) throws IOException {
//		ConverterProperties properties = new ConverterProperties();
//		ByteArrayOutputStream file = new ByteArrayOutputStream();
//		PdfWriter writer = PdfWriter.getInstance(null, file);
////		PdfWriter writer = new PdfWriter(file);
//		
//		PdfDocument pdf = new PdfDocument(writer);
//		pdf.setDefaultPageSize(PageSize.A4);
//		Document document = HtmlConverter.convertToDocument(html, pdf, properties);
//		document.setStrokeWidth(80);
//		document.close();
//
//		return file.toByteArray();
//	}
//
//	/**
//	 * metodo para crear el reporte pdf con margenes
//	 * 
//	 * @param html
//	 *            archivo html
//	 * @param margenSuperior
//	 *            margen superior
//	 * @param margenDerecho
//	 *            margen derecho
//	 * @param margenInferior
//	 *            margen inferior
//	 * @param margenIzquierdo
//	 *            margen izquierdo
//	 * @exception IOException
//	 *                excepcion
//	 */
//	public byte[] crearPdf(String html, int margenSuperior, int margenDerecho, int margenInferior, int margenIzquierdo, String mensajeQR) throws IOException {
//		html = html.replace("-webkit-xxx-large", "50").replace("o:p>", "p>");
//		List<IElement> elements = HtmlConverter.convertToElements(html);
//		ByteArrayOutputStream file = new ByteArrayOutputStream();
//		PdfWriter writer = new PdfWriter(file, new WriterProperties().setStandardEncryption(null, null, ~(EncryptionConstants.ALLOW_COPY) & ~(EncryptionConstants.ALLOW_MODIFY_CONTENTS), EncryptionConstants.ENCRYPTION_AES_128));
//
//		PdfDocument pdf = new PdfDocument(writer);
//
//		CabeceraPiePaginaPdf footerHandler = new CabeceraPiePaginaPdf(pdf, mensajeQR);
//		pdf.setDefaultPageSize(PageSize.A4);
//
//		Document document = new Document(pdf);
//		document.setMargins(margenSuperior, margenDerecho, margenInferior, margenIzquierdo);
//		PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
//		document.setFont(font);
//		for (IElement element : elements) {
//			if (element instanceof IBlockElement) {
//				document.add((IBlockElement) element);
//			} else if (element instanceof HtmlPageBreak) {
//				document.add((HtmlPageBreak) element);
//			} else {
//				document.add((AreaBreak) element);
//			}
//		}
//		if (mensajeQR != null) {
//			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);
//			footerHandler.writeTotal(pdf);
//		}
//		document.close();
//		return file.toByteArray();
//	}

}

