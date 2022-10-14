/**
@autor proamazonia [Christian Báez]  6 jul. 2022

**/
package ec.gob.ambiente.sis.reporteria;

public class GenerarPdfConsolidado {
	public static final String REPORTE_RESUMEN_ENCABEZADO_PREGUNTAS="<!DOCTYPE html >\r\n" + 
			"<html xmlns='http://www.w3.org/1999/xhtml'>\r\n" + 
			"<head>\r\n" + 
			"<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />\r\n" + 
			"<title>Documento sin título</title>\r\n" + 
			"<style>\r\n" +
			"table td {\r\n" +
			"	  white-space: normal !important;\r\n" +
			"   word-wrap: break;\r\n" +
			"	}\r\n" +
			".preguntas {\r\n" +
			"font-family: sans-serif;\r\n" +
			"font-size: 11px;\r\n" +
			"font-weight: bold;\r\n" +
			"}\r\n" +
			".titulotabla {\r\n" +
			"font-family: sans-serif;\r\n" +
			"font-size: 10px;\r\n" +
			"font-weight: bold;\r\n" +
			"}\r\n" +
			".tablaborder {\r\n" +
			"border: 1px solid black;\r\n" +
			"border-collapse:collapse;\r\n" +
			"height: auto;\r\n" +						
			"}\r\n" +
			".tablasinborder {\r\n" +
			"border: 0px solid black;\r\n" +
			"border-collapse:separate;\r\n" +
			"border-spacing: 10px 15px;\r\n" +
			"height: auto;\r\n" +						
			"}\r\n" +
			".contenido {\r\n" +
			"font-family: sans-serif;\r\n" +
			"font-size: 11px;\r\n" +			
			"}\r\n" +
			"</style>\r\n" +
			"</head>\r\n" + 
			"\r\n" + 
			"<body>\r\n" +
			"<table  width='80%' style='margin: 0 auto;border-style: none;border-collapse: collapse;font-size:12px;font-family: sans-serif' border='0'>\r\n" +
			"<tr>\r\n" +
		    "    <td><img src='{P:logoEscudo}' width='100' height='45'></td>\r\n" +
		    "   <td style='background-color: #ffffff; width: 30%'> </td>\r\n" +
		     "   <td><img src='{P:logoMae}' width='250' height='35'> </td>\r\n" +    
		     "</tr>\r\n" +
			"<tr style='margin: 0 auto;border-style: none;border-collapse: collapse;font-size:10px;background-color: #0da5c4;border-color: #ffffff;font-family: sans-serif'>\r\n" +
		    "    <td>Sistema de Información de Salvaguardas SIS</td>\r\n" +
		     "   <td style='background-color: #ffffff; width: 30%'> </td>\r\n" +    
		     "   <td>Resumen del abordaje de salvaguardas reportadas</td>\r\n" +        
		    "</tr>\r\n" +
		    "<tr >\r\n" +
		     "   <td style='background-color: #ffffff'></td>\r\n" +
		      "  <td style='background-color: #ffffff'> </td>    \r\n" +
		      "  <td style='background-color: #ffffff'>{P:fecha}</td>\r\n" +        
		    "</tr>\r\n" +
		"</table>\r\n" +	
		"<br/>\r\n" +		
		"<table  width='80%' style='margin: 0 auto;border-style: solid;border-collapse: collapse;font-size:10px;font-family: sans-serif;background-color: #0da5c4;' border='0'>\r\n" +
		 "   <tr style='height:3px;border-color: #ffffff;'>\r\n" +
		  "      <td style='font-weight: bold;vertical-align: baseline;text-align:left;' width: 10%;'>Salvaguarda:</td>\r\n" +
		   "     <td width: 70%;'>{P:salvaguarda} </td>\r\n" +
		   " </tr>\r\n" +
		   " <tr style='height:3px;border-color: #ffffff;'>\r\n" +
		   "     <td style='font-weight: bold;vertical-align: baseline;text-align:left;width:10%;'>Año:</td>\r\n" +
		   "     <td style='width: 70%;'>{P:anio} </td>\r\n" +
		   " </tr>\r\n" +		   
		"</table>\r\n" +    
		"<br/>\r\n" +					
		"<p style='text-align:center;font-size:15px;font-family: sans-serif;'>REPORTE CONSOLIDADO DE SOCIOS QUE RESPONDIERON CADA PREGUNTA POR SALVAGUARDA</p>\r\n";
	
	public static final String REPORTE_CONTENIDO_PREGUNTAS="<p style='color: #0da5c4; font-family: sans-serif;font-size: 13;font-weight: bold;'>SALVAGUARDA {P:salvaguarda}</p>\r\n" +
	     "{P:tablaDatos}\r\n";
	     
	public static final String REPORTE_RESUMEN_PIE="<table  width='100%' style='margin: 0 auto;border-style: none;border-collapse: collapse;font-size:12px;font-family: sans-serif' border='0'>\r\n" +
			"<tr>\r\n" +
		    "    <td style='text-align:center;'><img src='{P:logoPie}' width='600' height='80' ></td>\r\n" +		        
		     "</tr>\r\n" +
		"</table>\r\n" +
		"</body>\r\n" +
			"</html>";



}

