/**
@autor proamazonia [Christian Báez]  17 may. 2022

**/
package ec.gob.ambiente.sis.utils;

import java.io.IOException;

import com.google.zxing.WriterException;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;


public class CabeceraPiePaginaPdf {
	private static final long serialVersionUID = 1L;
	protected PdfFormXObject placeholder;
	protected float side = 20;
	protected float x = 300;
	protected float y = 10;
	protected float space = 4.5f;
	protected float descent = 3;
	protected String mensajeQR;

	/**
	 * Metodo constructor que genera cabecera y pie de pagina.
	 * @param pdf pdf
	 * @param mensajeQR mensaje qr
	 */
	public CabeceraPiePaginaPdf(PdfDocument pdf, String mensajeQR) {
		this.mensajeQR = mensajeQR;
		placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
	}
	
	public CabeceraPiePaginaPdf(PdfDocument pdf) {		
		placeholder = new PdfFormXObject(new Rectangle(0, 0, side, side));
	}

	/**
	 * Metodo que genera pdf con qr.
	 * @param event evento
	 */
	public void handleEvent(Event event) {
		try {
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfDocument pdf = docEvent.getDocument();
			PdfPage page = docEvent.getPage();
			int pageNumber = pdf.getPageNumber(page);
			Rectangle pageSize = page.getPageSize();
			PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdf);
			Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
			Paragraph p = new Paragraph().add("Página ").add(String.valueOf(pageNumber)).add(" de");
			PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
			canvas.showTextAligned(p, x, y, TextAlignment.RIGHT);
			canvas.setFont(font);
			canvas.setFontSize(10);
//			if (mensajeQR != null) {
//				byte[] imagen = GeneracionQRCode.getQRCodeImage(mensajeQR+"\nNúmero de páginas: "+canvas.getPdfDocument().getNumberOfPages(), 10, 10, null);
//				ImageData imageData = ImageDataFactory.create(imagen);
//				pdfCanvas.addImage(imageData, space, descent, true);
//			}
			pdfCanvas.addXObject(placeholder, x + space, y - descent);
			pdfCanvas.release();
			canvas.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que escribe el pdf.
	 * @param pdf pdf
	 */
	public void writeTotal(PdfDocument pdf) {
		Canvas canvas = new Canvas(placeholder, pdf);
		canvas.showTextAligned(String.valueOf(pdf.getNumberOfPages()), 0, descent, TextAlignment.LEFT);
		canvas.close();
	}
}

