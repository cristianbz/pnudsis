/**
@autor proamazonia [Christian Báez]  4 may. 2021

**/
package ec.gob.ambiente.sis.utils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper{
	String valorPieUno;
	String valorPieDos;
	String tipoPie;
	public PdfPTable footer;
	  
	  public HeaderFooterPageEvent(PdfPTable footer, String tipoPie) {
          this.footer = footer;
          this.tipoPie = tipoPie;
      }
	  public HeaderFooterPageEvent(String tipoPie) {
          this.tipoPie = tipoPie;
      }
      /**
	 * 
	 */
	public HeaderFooterPageEvent() {
		// TODO Auto-generated constructor stub
	}
	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		if("PIE".endsWith(tipoPie)){
        	ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase(valorPieUno), rect.getLeft(), rect.getBottom(), 0);
        	ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_CENTER, new Phrase(valorPieDos+ document.getPageNumber()), rect.getRight(), rect.getBottom(), 0);
        }else if("COMUNICADO".equals(tipoPie)){
        	Font fontNormal = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
        	PdfContentByte canvas = writer.getDirectContent();
        	footer.writeSelectedRows(0, -1, document.left()+60, rect.getBottom()+30, canvas);
        	ColumnText.showTextAligned(writer.getDirectContent(),Element.ALIGN_BOTTOM, new Phrase("Pág.:"+document.getPageNumber(),fontNormal), rect.getRight(), rect.getBottom(), 0);
        }
      }
 
    public void valoresPiePagina(String valorUno,String valorDos){
    	this.valorPieUno=valorUno;
    	this.valorPieDos=valorDos;
    }
}