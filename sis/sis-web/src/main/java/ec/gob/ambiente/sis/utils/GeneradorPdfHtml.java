/**
@autor proamazonia [Christian Báez]  17 may. 2022

**/
package ec.gob.ambiente.sis.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.attach.impl.layout.HtmlPageBreak;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
@Stateless
public class GeneradorPdfHtml {
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
	
	public void escribirArchivo(byte[] bFile, String fileDest) {

		try (FileOutputStream fileOuputStream = new FileOutputStream(fileDest)) {
			fileOuputStream.write(bFile);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public byte[] crearDocumentoPdf(String html,int posicion) throws IOException {
		html = html.replace("-webkit-xxx-large", "50").replace("o:p>", "p>");
		ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String url = ctx.getRealPath("/") + File.separator +  "resources"+ File.separator +  "images"+ File.separator;
		ConverterProperties properties = new ConverterProperties();
		properties.setBaseUri(url);
		ByteArrayOutputStream file = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(file);		
		PdfDocument pdf = new PdfDocument(writer);
		if(posicion==1)
			pdf.setDefaultPageSize(PageSize.A4);
		else
			pdf.setDefaultPageSize(PageSize.A4.rotate());
		CabeceraPiePaginaPdf piePagina = new CabeceraPiePaginaPdf(pdf);
		pdf.addEventHandler(PdfDocumentEvent.END_PAGE, piePagina);
		Document document = HtmlConverter.convertToDocument(html, pdf, properties);		
		document.setStrokeWidth(80);
		document.close();

		return file.toByteArray();
	}
	
	public byte[] crearPdf(String html, int margenSuperior, int margenDerecho, int margenInferior, int margenIzquierdo, String mensajeQR) throws IOException {
		html = html.replace("-webkit-xxx-large", "50").replace("o:p>", "p>");
		List<IElement> elements = HtmlConverter.convertToElements(html);
		ByteArrayOutputStream file = new ByteArrayOutputStream();
//		PdfWriter writer = new PdfWriter(file, new WriterProperties().setStandardEncryption(null, null, ~(EncryptionConstants.ALLOW_COPY) & ~(EncryptionConstants.ALLOW_MODIFY_CONTENTS), EncryptionConstants.ENCRYPTION_AES_128));
		PdfWriter writer = new PdfWriter(file);
		
		PdfDocument pdf = new PdfDocument(writer);

		
		pdf.setDefaultPageSize(PageSize.A4.rotate());

		Document document = new Document(pdf);
		document.setMargins(margenSuperior, margenDerecho, margenInferior, margenIzquierdo);
		PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
		document.setFont(font);
		for (IElement element : elements) {
			if (element instanceof IBlockElement) {
				document.add((IBlockElement) element);
			} else if (element instanceof HtmlPageBreak) {
				document.add((HtmlPageBreak) element);
			} else {
				document.add((AreaBreak) element);
			}
		}
//		if (mensajeQR != null) {
//			pdf.addEventHandler(PdfDocumentEvent.END_PAGE, (IEventHandler) footerHandler);
//			footerHandler.writeTotal(pdf);
//		}
		
		document.close();
		return file.toByteArray();
	}
	public String insertarQR(String html, String texto, int ancho, int alto, int anchoHtml, String logoQR) throws Exception {
		if (texto != null) {
			return html.replace("<qr>", "<img src='data:image/jpeg;base64," + Base64.getEncoder().encodeToString(GeneracionQRCode.getQRCodeImage(texto, ancho, alto, logoQR)) + "' alt='' width='" + anchoHtml + "px'/>");
		}
		return html.replace("<qr>", "");
	}
}

