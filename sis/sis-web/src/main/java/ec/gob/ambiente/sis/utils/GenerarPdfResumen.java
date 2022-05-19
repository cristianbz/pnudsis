/**
@autor proamazonia [Christian Báez]  18 may. 2022

**/
package ec.gob.ambiente.sis.utils;

public class GenerarPdfResumen {
	public static final String REPORTE_RESUMEN_SALVAGUARDAS="<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>\r\n" + 
			"<html xmlns='http://www.w3.org/1999/xhtml'>\r\n" + 
			"<head>\r\n" + 
			"<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />\r\n" + 
			"<title>Documento sin título</title>\r\n" + 
			"<style>\r\n" +
			".preguntas {\r\n" +
			"font-family: sans-serif;\r\n" +
			"font-size: 11px;\r\n" +
			"font-weight: bold;\r\n" +
			"}\r\n" +
			".contenido {\r\n" +
			"font-family: sans-serif;\r\n" +
			"font-size: 11px;\r\n" +			
			"}\r\n" +
			"</style>\r\n" +
			"</head>\r\n" + 
			"\r\n" + 
			"<body>\r\n" + 			
			"<table  width='70%' style='margin: 0 auto;border-style: solid;border-collapse: collapse;font-size:12px;background-color: #0da5c4;border-color: #ffffff;font-family: sans-serif' border='0'>\r\n" +
		    "<tr>\r\n" +
		    "    <td>Sistema de Información de Salvaguardas SIS</td>\r\n" +
		     "   <td style='background-color: #ffffff; width: 30%'> </td>\r\n" +    
		     "   <td>Resumen de las salvaguardas reportadas</td>\r\n" +        
		    "</tr>\r\n" +
		    "<tr >\r\n" +
		     "   <td style='background-color: #ffffff'></td>\r\n" +
		      "  <td style='background-color: #ffffff'> </td>    \r\n" +
		      "  <td style='background-color: #ffffff'>{P:fecha}</td>\r\n" +        
		    "</tr>\r\n" +
		"</table>\r\n" +		
		"<br/>\r\n" +		
		"<table  width='60%' style='margin: 0 auto;border-style: solid;border-collapse: collapse;font-size:10px;font-family: sans-serif;background-color: #0da5c4;' border='0'>\r\n" +
		 "   <tr style='height:3px;border-color: #ffffff;'>\r\n" +
		  "      <td style='font-weight: bold;vertical-align: baseline;text-align:right;'>Título del Plan de implementación, Programa o Proyecto:</td>\r\n" +
		   "     <td>{P:proyecto} </td>\r\n" +
		   " </tr>\r\n" +
		   " <tr style='height:3px;border-color: #ffffff;'>\r\n" +
		   "     <td style='font-weight: bold;vertical-align: baseline;text-align:right;'>Socio implementador:</td>\r\n" +
		   "     <td>{P:socioImplementador} </td>\r\n" +
		   " </tr>\r\n" +
		   " <tr style='height:3px;border-color: #ffffff;'>\r\n" +
		   "     <td style='font-weight: bold;vertical-align: baseline;text-align:right;'>Con qué sector se identifica:</td>\r\n" +
		   "     <td>{P:sectores} </td>\r\n" +
		   " </tr>\r\n" +
		"</table>\r\n" +
		"<br/>\r\n" +
	     "<br/>\r\n" +
	     "<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA A</p>\r\n" +
	     "<p class='preguntas'>{P:pregunta1}</p>\r\n" +
	     "<p class='preguntas'>Marco Jurídico Internacional</p>\r\n" +
	     "{P:tablaJuridicoInternacional}\r\n" + 
			
			"</body>\r\n" +
			"</html>";
}

