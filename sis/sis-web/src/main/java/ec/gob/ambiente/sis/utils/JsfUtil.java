package ec.gob.ambiente.sis.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

//import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import ec.gob.ambiente.sigma.model.User;
import ec.gob.ambiente.sis.bean.LoginBean;




public class JsfUtil {
	static final String GROWL_MESSAGES = "growlMessages";
	private static final  String temporalTemp = System.getProperty("java.io.tmpdir")+File.separator;
	
    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();        
        
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "Seleccione...");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }
 
    /**
     * Añade un mensaje de <code>error</code> a la pila de mensajes del FacesContext.
     * @param ex Excepción capturada, para mostrar el stacktrace, por ejemplo.
     * @param defaultMsg Mensaje general.
     */
    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
		if (msg != null && msg.length() > 0) {
			addErrorMessage(msg);
		} else {
			addErrorMessage(defaultMsg);
		}
    }

    /**
     * Añade mensajes de error en manera de bucle. 
     * @param messages
     */
    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }
       
    /**
     * Añade un mensaje de <code>error</code> a la pila de mensajes del FacesContext.
     * @param msg	El mensaje en general.
     */
    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);                
    }

    /**
     * Añade un mensaje de <code>información</code> a la pila de mensajes del FacesContext.
     * @param msg	El mensaje en general.
     */    
    public static void addInfoMessage(String msg){
    	FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    public static void addInfoMessage(String msg, Boolean addInGrowl){
    	FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
    	if (addInGrowl){    	
    		FacesContext.getCurrentInstance().addMessage(GROWL_MESSAGES, facesMsg);
    	}else{
    		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    	}
    }
    
    
    /**
     * Añade un mensaje de info tipo: <code>FATAL</code> a la pila de mensajes del FacesContext.
     * @param msg
     */
    public static void addFatalMessage(String msg){
    	FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_FATAL, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    }
    
    /**
     * Añade un mensaje de info tipo: <code>WARNING</code> a la pila de mensajes del FacesContext.
     * @param msg
     */
    public static void addWarningMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);                
    } 
    
    /**
     * Añade un mensaje de info tipo: <code>SUCCESS</code> a la pila de mensajes del FacesContext.
     * @param msg
     */
    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage("successInfo", facesMsg);
    }

    /**
     * Obtiene un parámetro vía request por GET.
     * @param key
     * @return
     */
    public static String getRequestParameter(String key) {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(key) == null ? "" : FacesContext
				.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(key);
    }

    /**
     * Obtiene un objeto desde la request, por GET supongo, no lo pruebo aún!
     * @param requestParameterName
     * @param converter
     * @param component
     * @return
     */
    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }
    
    
    /** Inicia utilerías */
    
	/**
	 * Añade un error técnico al request y se muestra por pantalla.
	 * @param error Descripción del error.
	 */
    /*public static void addErrorTecnico(String error) {
		ErrorTecnico errorTecnico = new ErrorTecnico();
		errorTecnico.setError(error);
		errorTecnico.setExisteError(true);
		System.out.println("Se fué por error técnico<--");
		HttpServletRequest request = (HttpServletRequest) FacesContext
		 .getCurrentInstance().getExternalContext().getRequest();		
		request.setAttribute("errorTecnico", errorTecnico);
	} */      
     
    /**
     * Añade parámetros a la solicitud, que serán leídos por el cliente mediante 
     * un manejador javascript en, por ejemplo: el <code>oncomplete.</code>
     * @param key String Clave
     * @param value String Valor.
     */
    /*public static void addCallbackParamToRC(String key, String value){
    	RequestContext context = RequestContext.getCurrentInstance();
		context.addCallbackParam(key, value);
    }*/
    
    
    /**
     * Añade un objeto al contexto Flash, ojo que solo existirá en una "ida y vuelta".
     * @param key
     * @param object
     */
    public static void setFlashContextObject(String key, Object object){    	
    	FacesContext.getCurrentInstance().getExternalContext().getFlash().put(key, object);
    }    
    
    /**
     * Retorna un objeto del contexto Flash, ojo que solo existirá en una "ida y vuelta".
     * @param key
     * @return
     */
    public static Object getFlashContextObject(String key){
    	return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(key);
    }
    
    /**
     * Redirect a request to the specified URL, and cause the responseComplete(), esa descripción
     * lo dice todo! 
     * @param url
     * @throws RuntimeException, IOException 
     */
    public static void redirect(String url) throws RuntimeException, IOException{
    	ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		externalContext.redirect(externalContext.getRequestContextPath() + url);
    	
//    	FacesContext faces = FacesContext.getCurrentInstance();
//		faces.getExternalContext().redirect(faces.getExternalContext().getRequestContextPath() + url);
    }

    
    /**
     * Evalúa la URL del <b>Referente </b> para saber si vienen variables por el <code>GET</code>.
     * @return
     */
    public static Boolean isPropertiesPresentByHttpGet(){
    	Boolean ban = true;
		String ss = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer");
		if (null == ss || ss.indexOf('?') == -1){
			ban = false;				
		}
		return ban;
    }

    /**
     * Obtiene la URL desde dónde viene la petición.
     * @return
     */
    public static String getHttpReferer(){    	
		return FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("referer");
    }
    
    /**
     * Devuelve una variable que viene por get, pero del <code>referer</code>. 
     * @param property La propiedad que se desea consultar.
     * @return
     */
    public static String getHttpPropertyReferer(String property){

		String ss = getHttpReferer();

		java.util.regex.Pattern namePtrn = java.util.regex.Pattern
				.compile(property + "=(\\w+)");
		
		java.util.regex.Matcher nameMtchr = namePtrn.matcher(ss);

		String devolver = "";
		
		while (nameMtchr.find()) {
			devolver = nameMtchr.group(1);
		}
		
		return devolver;
    }
    
    /**
     * Ejecuta un javascript pasado por parámetro luego de que regrese del servidor.
     * @param stringJavascript
     */
    /*public static void executeJavascriptWhenPostIsComplete(String stringJavascript){
    	RequestContext context = RequestContext.getCurrentInstance();    	 
    	context.execute(stringJavascript);
    }*/
    /** Fin utilerías */
 
    
    /**
     * Get un objeto de la sesión del contexto.
     * @param theObject
     * @return
     */
    public static Object getSessionObject(String theObject){
    	HttpServletRequest request = (HttpServletRequest) FacesContext
    			.getCurrentInstance().getExternalContext()
    			.getRequest();
    	
    	return request.getSession(false).getAttribute(theObject);
    }
    
    /**
     * Set un objeto en la sesión del contexto web.
     * @param nameOfTheObject
     * @param theObject
     */
    public static void setSessionObject(String nameOfTheObject, Object theObject){
    	HttpServletRequest request = (HttpServletRequest) FacesContext
    			.getCurrentInstance().getExternalContext()
    			.getRequest();    	
    	
		request.getSession(false).setAttribute(nameOfTheObject, theObject);
    }
 
    /**
     * Diferencias entre dos fechas
     * @param fechaInicial La fecha de inicio
     * @param fechaFinal
     * @return Retorna el numero de dias entre dos fechas
     */
    public static synchronized int diferenciasDeFechas(Date fechaInicial, Date fechaFinal) {

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String fechaInicioString = df.format(fechaInicial);
        try {
            fechaInicial = df.parse(fechaInicioString);
        } catch (ParseException ex) {
        }

        String fechaFinalString = df.format(fechaFinal);
        try {
            fechaFinal = df.parse(fechaFinalString);
        } catch (ParseException ex) {
        }

        long fechaInicialMs = fechaInicial.getTime();
        long fechaFinalMs = fechaFinal.getTime();
        long diferencia = fechaFinalMs - fechaInicialMs;
        double dias = Math.floor(diferencia / (1000 * 60 * 60 * 24));
        return ((int) dias);
    }
    
    /*public static File upload(FileUploadEvent event) {		
		File folder = new File(System.getProperty("java.io.tmpdir"));
		UploadedFile arq= event.getFile();
		String fileName=validateFileName(arq.getFileName());
		try {
			InputStream input = arq.getInputstream();
			File file = new File(folder, fileName);
			OutputStream out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024 * 1024 * 130];
			while ((read = input.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			input.close();
			out.flush();
			out.close();
			return file;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}*/
    
    public static String validateFileName(String fileName) {
		if (fileName.indexOf(":") > -1) {
			return fileName.replace(":", "");
		}
		return fileName;
	}
    
    public static void removeFileTemp(File file, List<File> fileList) 
	{	
		try
		{
			for (File fileExist : fileList) 
			{
				if(!file.equals(fileExist))
				{				    
					File pathFile = new File(temporalTemp + fileExist.getName());
					FileOutputStream fout = new FileOutputStream(pathFile);
					fout.close();
					pathFile.delete(); 
					fileList.remove(fileExist);		
			    }
			}
		}
		catch(IOException ex)
		{

		}
	}
	
	public static StreamedContent getFileDownload(File file) 
	{
		File tmp = file;
		FileInputStream stream;
		
		try {
			if(file != null)
			{
				stream = new FileInputStream(tmp);
			
				String name[] = tmp.getAbsoluteFile().getName().split("\\.");
				String extension = "";
				if(name.length > 0)
				extension = name[name.length - 1];
				
				String contentType = "application/pdf";
				
				if(extension.equals("gif"))
					contentType = "application/gif";
				
				if(extension.equals("jpeg"))
					contentType = "image/jpeg";
				
				if(extension.equals("jpg"))
					contentType = "image/jpg";
				
				if(extension.equals("png"))
					contentType = "image/png";
				
				if(extension.equals("wmv"))
					contentType = "application/wmv";
				
				if(extension.equals("doc"))
					contentType = "application/doc";
				
				if(extension.equals("xls"))
					contentType = "application/xls";
			
					StreamedContent download = new DefaultStreamedContent(stream, contentType,
					tmp.getName());
				return download;
			}
			else
			{
				return null;
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getExtensionFile(File file) 
	{
		if(file != null)
		{				
			String name[] = file.getAbsoluteFile().getName().split("\\.");
			String extension = "";
			if(name.length > 0)
			extension = name[name.length - 1];
			
			return extension;
		}
		else
		{
			return "";
		}
	}	
	
	public static String implode(String[] ary, String delim) {
	    String out = "";
	    for(int i=0; i<ary.length; i++) {
	        if(i!=0) { out += delim; }
	        out += ary[i];
	    }
	    return out;
	}
	
	/**
	 * Genera una cadena de caracteres aleatorea
	 * 
	 * @param onlyChars
	 * @return
	 */
	public static synchronized String generatePassword(boolean... onlyChars) {
		String passwd = "";
		Random random= new Random();
		int TAMANIO_PASSWORD=8;
		boolean isChar = onlyChars == null ? false : onlyChars.length > 0 ? onlyChars[0] : false;
		for (char c : complete("" + (int) (random.nextDouble() * 99999999), TAMANIO_PASSWORD, '0', true).toCharArray()) {
			int value = (int) (Integer.parseInt("" + c) + Math.round(Math.random() * 120));
			char cc = (char) value;
			if (Character.isLetter(cc) & Character.isDefined(cc) & !Character.isWhitespace(cc)) {
				passwd += cc;
			} else {
				value = (int) (isChar ? Math.round(Math.random() * 25) + 65 : value);
				passwd += isChar ? (char) value : c;
			}
		}
		return passwd;
	}

	/**
	 * Permite complementar una determinada cadena de texto con un caracter
	 * especificado
	 * 
	 * @param data
	 *            Cadena de texto original
	 * @param length
	 *            longitud deseada
	 * @param complete
	 *            caracter con el cual se completara la cadena
	 * @param reverse
	 *            indica si la cadena se complementara al fina(false) o al
	 *            inicio(true)
	 * @return cadena complementada, si la longitid es menor a la cadena
	 *         original se retornara la original sin ccambios
	 */
	public static synchronized String complete(String data, final int length, final char complete, final boolean reverse) {
		final int size = data.length();
		StringBuilder build = new StringBuilder();
		if (reverse) {
			for (int i = size; i < length; i++) {
				build.append(complete);
			}
			build.append(data);
		} else {
			build.append(data);
			for (int i = size; i < length; i++) {
				build.append(complete);
			}
		}
		return build.toString();
	}
	
	/**
	 * Devuelve una cadena encriptada en sha1
	 * 
	 * @param password
	 * @return
	 */
	public static String claveEncriptadaSHA1(String password) {
		try {
			byte[] buffer = password.getBytes();
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(buffer);
			byte[] digest = md.digest();
			String valorHash = "";
			for (byte aux : digest) {
				int b = aux & 0xff;
				if (Integer.toHexString(b).length() == 1) {
					valorHash += "0";
				}
				valorHash += Integer.toHexString(b);
			}
			return valorHash;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 
	 * <b> Compara 2 cadenas ignorando mayusculas y tildes. </b>
	 * 
	 * @author Carlos Pupo
	 * @version Revision: 1.0
	 *          <p>
	 *          [Autor: Carlos Pupo, Fecha: 23/01/2015]
	 *          </p>
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean comparePrimaryStrings(String s1, String s2) {
		Collator collator = Collator.getInstance();
		collator.setStrength(Collator.PRIMARY);
		return collator.equals(s1, s2);
	}
	
	/**
	 * 
	 * <b> Convierte una cadena a una de la opciones, compara ignorando
	 * mayusculas y tildes, si no es encontrada, retorna la misma cadena </b>
	 * 
	 * @author Carlos Pupo
	 * @version Revision: 1.0
	 *          <p>
	 *          [Autor: Carlos Pupo, Fecha: 23/01/2015]
	 *          </p>
	 * @param string
	 * @param strings
	 * @return
	 */
	public static String getStringAsAnyPrimaryStrings(String string, String[] strings) {
		for (String s : strings) {
			if (comparePrimaryStrings(string, s)) {
				return s;
			}
		}
		return string;
	}
	
	/**
	 * 
	 * <b> Verifica si una cadena esta entre una de las opciones ignorando
	 * mayusculas y tildes. </b>
	 * 
	 * @author Carlos Pupo
	 * @version Revision: 1.0
	 *          <p>
	 *          [Autor: Carlos Pupo, Fecha: 23/01/2015]
	 *          </p>
	 * @param string
	 * @param strings
	 * @return
	 */
	public static boolean isStringInPrimaryStrings(String string, String[] strings) {
		for (String s : strings) {
			if (comparePrimaryStrings(string, s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Muestra un mensaje de informaci�n
	 * 
	 * @param message
	 *            texto del mensaje
	 */
	public static void addMessageInfo(String message) {
		addMessage(message, FacesMessage.SEVERITY_INFO);
	}

	/**
	 * Muestra un mensaje de advertencia
	 * 
	 * @param message
	 *            texto del mensaje
	 */
	public static void addMessageWarning(String message) {
		addMessage(message, FacesMessage.SEVERITY_WARN);
	}

	/**
	 * Muestra un mensaje de error
	 * 
	 * @param message
	 *            texto del mensaje
	 */
	public static void addMessageError(String message) {
		addMessage(message, FacesMessage.SEVERITY_ERROR);
	}

	/**
	 * Muestra un mensaje de error en un hilo diferente
	 * 
	 * @param message
	 *            texto del mensaje
	 * @param delay
	 *            sleep en milisegundos
	 */
	public static void addMessageError(final String message, long delay) {
		try {
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					addMessageError(message);
				}
			}, delay);
		} catch (Exception e) {
		}
	}

	/**
	 * Muestra una lista de mensajes de error
	 * 
	 * @param messages
	 *            lista de mensajes
	 */
	public static void addMessageError(List<String> messages) {
		for (String message : messages) {
			addMessage(message, FacesMessage.SEVERITY_ERROR);
		}
	}

	/**
	 * Adiciona un mensaje al contexto
	 * 
	 * @param message
	 *            el texto a mostrar
	 * @param severity
	 *            el tipo de mensaje
	 */
	private static void addMessage(String message, Severity severity) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
	}

	/**
	 * Adiciona un mensaje al contexto
	 * 
	 * @param message
	 *            el texto a mostrar
	 */
	public static void addMessageErrorForComponent(String component, String message) {
		FacesContext.getCurrentInstance().addMessage(component,
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
	}

	/**
	 * Adiciona un mensaje al contexto desde un bundle
	 */
	public static String getMessageFromBundle(String bundleName, String key, Object... params) {
		String messageBundleName = bundleName;
		if (messageBundleName == null) {
			messageBundleName = FacesContext.getCurrentInstance().getApplication().getMessageBundle();
		}
		Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
		ResourceBundle bundle = null;
		try {
			bundle = ResourceBundle.getBundle(messageBundleName, locale);
		} catch (Exception e) {
			bundle = FacesContext.getCurrentInstance().getApplication()
					.getResourceBundle(FacesContext.getCurrentInstance(), messageBundleName);
		}
		try {
			return MessageFormat.format(bundle.getString(key), params);
		} catch (Exception e) {
			return '!' + key + '!';
		}
	}
	
	public static HttpServletRequest getRequest() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		return request;
	}
	
	public static String devolverPathReportesHtml(final String reporteHtml) {
		return getRequest().getSession().getServletContext().getRealPath("pages/reportesHtml/" + reporteHtml);
	}
	
	public static String devolverContexto(final String pathArchivo) {
		return getRequest().getContextPath() + pathArchivo;
	}
	
	/**
	 * Agrega o modifica una variable de sesion request
	 * @param attributeName
	 * @param valor
	 */
	public static void setSessionAttribute(final String attributeName,final Object valor) {		
		getRequest().getSession().setAttribute(attributeName, valor);
	}
	
	/**
	 * Obtener una variable de sesion request
	 * @param attributeName
	 * @return
	 */
	public static Object getSessionAttribute(final String attributeName) {		
		return getRequest().getSession().getAttribute(attributeName);
	}
	
	public static boolean validarMail(String email) {
		boolean valido = false;
		Pattern patronEmail = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		Matcher mEmail = patronEmail.matcher(email);
		if (mEmail.matches()) {
			valido = true;
		}
		return valido;
	}
	
	/**
	 * Valida RUC o Cedula
	 * 
	 * @param numero
	 * @return
	 */
	public static boolean validarCedulaORUC(String numero) {
		int suma = 0;
		int residuo = 0;
		boolean privada = false;
		boolean publica = false;
		boolean natural = false;
		int numeroProvincias = 24;
		int digitoVerificador = 0;
		int modulo = 11;

		int d1, d2, d3, d4, d5, d6, d7, d8, d9, d10;
		int p1, p2, p3, p4, p5, p6, p7, p8, p9;

		d1 = d2 = d3 = d4 = d5 = d6 = d7 = d8 = d9 = d10 = 0;
		p1 = p2 = p3 = p4 = p5 = p6 = p7 = p8 = p9 = 0;

		if (numero.length() < 10) {
			return false;
		}

		// Los primeros dos digitos corresponden al codigo de la provincia
		int provincia = Integer.parseInt(numero.substring(0, 2));

		if (provincia <= 0 || provincia > numeroProvincias) {
			return false;
		}

		// Almacena los digitos de la cedula en variables.
		d1 = Integer.parseInt(numero.substring(0, 1));
		d2 = Integer.parseInt(numero.substring(1, 2));
		d3 = Integer.parseInt(numero.substring(2, 3));
		d4 = Integer.parseInt(numero.substring(3, 4));
		d5 = Integer.parseInt(numero.substring(4, 5));
		d6 = Integer.parseInt(numero.substring(5, 6));
		d7 = Integer.parseInt(numero.substring(6, 7));
		d8 = Integer.parseInt(numero.substring(7, 8));
		d9 = Integer.parseInt(numero.substring(8, 9));
		d10 = Integer.parseInt(numero.substring(9, 10));

		// El tercer digito es:
		// 9 para sociedades privadas y extranjeros
		// 6 para sociedades publicas
		// menor que 6 (0,1,2,3,4,5) para personas naturales
		if (d3 == 7 || d3 == 8) {
			return false;
		}

		// Solo para personas naturales (modulo 10)
		if (d3 < 6) {
			natural = true;
			modulo = 10;
			p1 = d1 * 2;
			if (p1 >= 10) {
				p1 -= 9;
			}
			p2 = d2 * 1;
			if (p2 >= 10) {
				p2 -= 9;
			}
			p3 = d3 * 2;
			if (p3 >= 10) {
				p3 -= 9;
			}
			p4 = d4 * 1;
			if (p4 >= 10) {
				p4 -= 9;
			}
			p5 = d5 * 2;
			if (p5 >= 10) {
				p5 -= 9;
			}
			p6 = d6 * 1;
			if (p6 >= 10) {
				p6 -= 9;
			}
			p7 = d7 * 2;
			if (p7 >= 10) {
				p7 -= 9;
			}
			p8 = d8 * 1;
			if (p8 >= 10) {
				p8 -= 9;
			}
			p9 = d9 * 2;
			if (p9 >= 10) {
				p9 -= 9;
			}
		}

		// Solo para sociedades publicas (modulo 11)
		// Aqui el digito verficador esta en la posicion 9, en las otras 2
		// en la pos. 10
		if (d3 == 6) {
			publica = true;
			p1 = d1 * 3;
			p2 = d2 * 2;
			p3 = d3 * 7;
			p4 = d4 * 6;
			p5 = d5 * 5;
			p6 = d6 * 4;
			p7 = d7 * 3;
			p8 = d8 * 2;
			p9 = 0;
		}

		/*
		 * Solo para entidades privadas (modulo 11)
		 */
		if (d3 == 9) {
			privada = true;
			p1 = d1 * 4;
			p2 = d2 * 3;
			p3 = d3 * 2;
			p4 = d4 * 7;
			p5 = d5 * 6;
			p6 = d6 * 5;
			p7 = d7 * 4;
			p8 = d8 * 3;
			p9 = d9 * 2;
		}

		suma = p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
		residuo = suma % modulo;

		// Si residuo=0, dig.ver.=0, caso contrario 10 - residuo
		digitoVerificador = residuo == 0 ? 0 : modulo - residuo;
		int longitud = numero.length();
		// ahora comparamos el elemento de la posicion 10 con el dig. ver.
		if (publica) {
			if (digitoVerificador != d9) {
				return false;
			}
			/*
			 * El ruc de las empresas del sector publico terminan con 0001
			 */
			if (!numero.substring(9, longitud).equals("0001")) {
				return false;
			}
		}

		if (privada) {
			if (digitoVerificador != d10) {
				return false;
			}
			if (!numero.substring(10, longitud).equals("001")) {
				return false;
			}
		}

		if (natural) {
			if (digitoVerificador != d10) {
				return false;
			}
			if (numero.length() > 10 && !numero.substring(10, longitud).equals("001")) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Muestra una lista de mensajes de error
	 * 
	 * @param messages
	 *            lista de mensajes
	 */
	public static void addErrorMessage(List<String> messages) {
		
		for (String message : messages) {
			
			FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
	        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
		}
	}
	
	public static String getDateFormat(Date date) {
		Calendar fecha = Calendar.getInstance();
		fecha.setTime(date);
		return devuelveDiaSemana(fecha.get(Calendar.DAY_OF_WEEK)) + " " + fecha.get(Calendar.DAY_OF_MONTH) + " de "
				+ devuelveMes(fecha.get(Calendar.MONTH)) + " " + fecha.get(Calendar.YEAR);
	}
	
	private static String devuelveDiaSemana(int dia) {
		switch (dia) {
		case 1:
			return "Domingo";
		case 2:
			return "Lunes";
		case 3:
			return "Martes";
		case 4:
			return "Miércoles";
		case 5:
			return "Jueves";
		case 6:
			return "Viernes";
		case 7:
			return "Sábado";
		default:
			return "";

		}
	}

	public static String devuelveMes(int mes) {
		switch (mes) {
		case 0:
			return "enero";
		case 1:
			return "febrero";
		case 2:
			return "marzo";
		case 3:
			return "abril";
		case 4:
			return "mayo";
		case 5:
			return "junio";
		case 6:
			return "julio";
		case 7:
			return "agosto";
		case 8:
			return "septiembre";
		case 9:
			return "octubre";
		case 10:
			return "noviembre";
		case 11:
			return "diciembre";
		default:
			return "";
		}
	}
	
	public static String getUserLogin()
	{
		HttpServletRequest request = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		String user = (String) request.getSession(false).getAttribute("user");
		return user;
	}
	
	
	/**
	 * Obtener valor de un archivo .properties de la aplicacion
	 * @param key
	 * @param isConfig Si es configuracion o Mensaje
	 * @return
	 */
	public static String getProperty(String key,boolean isConfig)
	{
		FacesContext context = FacesContext.getCurrentInstance();
		String fileProperties=isConfig?"#{survey}":"#{msg}";		
		ResourceBundle bundle = context.getApplication().evaluateExpressionGet(context, fileProperties, ResourceBundle.class);
		String value = bundle.getString(key);		
		return value;
	}	
	
	
	/*public static void uploadApdoDocument(UploadedFile file,ApplicationDocument appdoDocument){		
		appdoDocument.setContent(file.getContents());
		appdoDocument.setApdoMimeType(file.getContentType());
		appdoDocument.setApdoName(file.getFileName());
		String[] split=appdoDocument.getApdoName().split("\\.");
		appdoDocument.setApdoExtension("."+split[split.length-1]);
	}*/
	
	/*public static void uploadApdoDocument(File file,ApplicationDocument document){
		Path path = Paths.get(file.getAbsolutePath());
		try {
			document.setContent(Files.readAllBytes(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		document.setApdoMimeType(mimeTypesMap.getContentType(file));
		document.setApdoName(file.getName());
		String[] split=document.getApdoName().split("\\.");
		document.setApdoExtension("."+split[split.length-1]);
	}*/
	
	/**
	 * Colocar marca de agua en pdf
	 * @param file Archivo
	 * @param texto Marca de Agua
	 * @return File
	 */


	/*public static File fileMarcaAgua(File file,String texto,BaseColor color) {
        try {
            String nombre = file.getAbsolutePath();
            PdfReader reader = new PdfReader(nombre);
            int n = reader.getNumberOfPages();

            // Create a stamper that will copy the document to a new file
            PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(
                    file.getAbsolutePath() + ".tmp"));
            int i = 1;
            PdfContentByte under;
            PdfContentByte over;

            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
                    BaseFont.WINANSI, BaseFont.EMBEDDED);

            while (i <= n) {
                // Watermark under the existing page
                under = stamp.getUnderContent(i);
                
                // Text over the existing page
                over = stamp.getOverContent(i);           
                under.beginText();
                under.setFontAndSize(bf, 48);
                under.setColorFill(color);
                // under.showText("Borrador ");
                under.addImage(Image.getInstance(getRecursoImage("180px-Coat_of_arms_of_Ecuador.svg.png")));
                under.showTextAligned(1, texto, 300, 500, 45);
                
                under.endText();                
                i++;
            }

            stamp.close();
            File borrador = new File(file.getAbsolutePath() + ".tmp");
            borrador.renameTo(file);
            return new File(nombre);
        } catch (Exception de) {

            return file;
        }

    }*/
	
	private static URL getRecursoImage(String nombreImagen) {
		ServletContext servletContext = (ServletContext) FacesContext
				.getCurrentInstance().getExternalContext().getContext();
		try {
			return servletContext.getResource("/resources/images/"
					+ nombreImagen);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Obtener java.io.File desde un org.primefaces.event.FileUploadEvent
	 * @param event
	 * @return
	 */
	/*
	public static File fileUploadEventToFile(FileUploadEvent event) {
		File folder = new File(System.getProperty("java.io.tmpdir"));
		UploadedFile arq = event.getFile();
		String fileName = arq.getFileName();
		try {
			InputStream input = arq.getInputstream();
			File file = new File(folder, fileName);
			OutputStream out = new FileOutputStream(file);
			int read = 0;
			byte[] bytes = new byte[1024 * 1024 * 2];
			while ((read = input.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			input.close();
			out.flush();
			out.close();
			return file;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}*/
	
	public static User getLoggedUser() {
		LoginBean instance = getBean(LoginBean.class);
		if (instance.getUser().getUserId()!=null) {
			//instance.getUser().setPassword(instance.getPassword());
			return instance.getUser();
		}
		return null;
	}
	
	/**
	 * Obtiene un managed bean del contexto
	 * 
	 * @return managed bean registrado con name
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> beanType) {
		String customName = null;
		try {
			customName = beanType.getAnnotation(ManagedBean.class).annotationType().getDeclaredMethod("name")
					.invoke(beanType.getAnnotation(ManagedBean.class)).toString();
		} catch (Exception e) {

		}
		String standardBeanName = (beanType.getSimpleName().charAt(0) + "").toLowerCase()
				+ beanType.getSimpleName().substring(1);

		if (customName != null && !customName.isEmpty())
			standardBeanName = customName;

		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		return (T) FacesContext.getCurrentInstance().getApplication().getELResolver().getValue(elContext, null,
				standardBeanName);
	}
	
	public static String getDireccion(){
		HttpServletRequest request  = (HttpServletRequest) FacesContext
				.getCurrentInstance().getExternalContext().getRequest();
		
		if(request==null) return "";
		String port  = ""+request.getServerPort();
		if(port.equals("80")){
			port="";
		}else{
			port =":"+ port;
		}
		
		String contextPath = request.getContextPath();
		if(contextPath.equals("/")){
			contextPath="";
		}
		
		String domain ="http://"+ request.getServerName()+port;
		domain+=contextPath + "/resources/images/firma_etiqueta_nueva.png";
		return domain;	
		
	}
	

}
