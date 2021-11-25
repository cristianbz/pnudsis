/**
@autor proamazonia [Christian Báez]  10 nov. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sigma.services.MeetingsFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.SitioPublicoBean;
import ec.gob.ambiente.sis.model.TableResponses;
import ec.gob.ambiente.sis.services.TableResponsesFacade;
import ec.gob.ambiente.sis.utils.Mensaje;
import lombok.Getter;

@Named()
@ViewScoped
public class SitioPublicoController implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final String SALVAGUARDA_A="SALVAGUARDA A";
	private static final String SALVAGUARDA_B="SALVAGUARDA B";
	private static final String SALVAGUARDA_C="SALVAGUARDA C";
	private static final String SALVAGUARDA_D="SALVAGUARDA D";
	private static final String SALVAGUARDA_E="SALVAGUARDA E";
	private static final String SALVAGUARDA_F="SALVAGUARDA F";
	private static final String SALVAGUARDA_G="SALVAGUARDA G";
	private static final String DESCRIPCION_A="Programas o proyectos del gobierno nacional dedicados a la conservación de bosques";
	private static final String DESCRIPCION_B="Comunidades vinculadas con manejo forestal sostenible, hombres y mujeres beneficiarios";
	private static final String DESCRIPCION_C="Prácticas y saberes ancestrales ";
	private static final String DESCRIPCION_D="Eventos de fortalecimiento y capacidades para hombres y mujeres";
	private static final String DESCRIPCION_E="Fomento de la gestión comunitaria y apoyo al fortalecimiento de las organizaciones campesinas e indígenas";
	private static final String DESCRIPCION_F="Cantidad de recursos invertidos";
	private static final String DESCRIPCION_G="Acciones para generar alternativas económicas sostenibles a nivel local";
	
	@Inject
	@Getter
	private SitioPublicoBean sitioPublicoBean;
	
	@EJB
	@Getter
	private TableResponsesFacade tableResponsesFacade;
	
	@EJB
	@Getter
	private SafeguardsFacade safeguardsFacade;
	
	@EJB
	@Getter
	private MeetingsFacade meetingsFacade;
	
	@PostConstruct
	public void init(){
		try{
			List<Object[]> listSalvaguardas=new ArrayList<>();
			getSitioPublicoBean().setListaSalvaguardas(new ArrayList<>());
			listSalvaguardas = getSafeguardsFacade().listarSalvaguardas();
			for(Object obj:listSalvaguardas){
				Object[] dataObj = (Object[]) obj;
				Safeguards sf = new Safeguards();
				sf.setSafeDescription(dataObj[1].toString());
				sf.setSafeCode(dataObj[3].toString());				
				sf.setSafeTitle(dataObj[4].toString());
				getSitioPublicoBean().getListaSalvaguardas().add(sf);
			}
			getSitioPublicoBean().setPosicionSalvaguardas(1);
			informacionSalvaguardaA();
			Mensaje.actualizarComponente(":frm:pnlSalvaguardas");			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Resumen de informacion salvaguarda A
	 */
	public void informacionSalvaguardaA(){
		List<TableResponses> listaTemp= new ArrayList<>();
		try{

			List<Integer> listaProyectos=new ArrayList<>();
			BigDecimal totalInversion = new BigDecimal(0);
			getSitioPublicoBean().setTotalInversionProyectos(new BigDecimal(0));
			listaTemp = getTableResponsesFacade().listaProyectosValoresSalvaguardaA();
			Map<Integer,BigDecimal> mapaTemp=new HashMap<Integer,BigDecimal>();
			for(TableResponses tr: listaTemp){
				mapaTemp.put(tr.getTareColumnNumberSix(), tr.getTareColumnDecimalOne());
				totalInversion = totalInversion.add(tr.getTareColumnDecimalOne());
			}
			for(Entry<Integer,BigDecimal> proy: mapaTemp.entrySet()){
				listaProyectos.add(proy.getKey());
			}
			getSitioPublicoBean().setTotalInversionProyectos(totalInversion);
			getSitioPublicoBean().setNumeroProyectosSalvaguardaA(listaProyectos.size());
//			System.out.println(listaProyectos.size());
//			System.out.println(getSitioPublicoBean().getTotalInversionProyectos());
			obtieneSalvaguardas(SALVAGUARDA_A, "A");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_A);
			getSitioPublicoBean().setCodigoSalvaguarda("A");
			getSitioPublicoBean().setPosicionSalvaguardas(1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void informacionSalvaguardaB(){
		List<TableResponses> listaTempComunidades= new ArrayList<>();
		
		List<String> listaComunidades=new ArrayList<>();
		try{
			
			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(16);
			Map<String,Integer> mapaTemp=new HashMap<String,Integer>();
			for(TableResponses tr: listaTempComunidades){
				mapaTemp.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
			}
			for(Entry<String,Integer> proy: mapaTemp.entrySet()){
				listaComunidades.add(proy.getKey());
			}
			getSitioPublicoBean().setNumeroComunidadesSalvaguardaB(listaComunidades.size());
			
			listaTempComunidades = new ArrayList<>();
			listaTempComunidades = getTableResponsesFacade().listaMaximoHombresMujeresSalvaguardaB();
			
			for(TableResponses tr: listaTempComunidades){
				getSitioPublicoBean().setNumeroHombresSalvaguardaB(tr.getTareColumnNumberOne());
				getSitioPublicoBean().setNumeroMujeresSalvaguardaB(tr.getTareColumnNumberTwo());
			}
//			System.out.println("Numero comunidades");
//			System.out.println(getSitioPublicoBean().getNumeroComunidadesSalvaguardaB());
//			System.out.println("Numero hombres");
//			System.out.println(getSitioPublicoBean().getNumeroHombresSalvaguardaB());
//			System.out.println("Numero mujeres");
//			System.out.println(getSitioPublicoBean().getNumeroMujeresSalvaguardaB());
			obtieneSalvaguardas(SALVAGUARDA_B, "B");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_B);
			getSitioPublicoBean().setCodigoSalvaguarda("B");
			getSitioPublicoBean().setPosicionSalvaguardas(2);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void informacionSalvaguardaC(){
		List<TableResponses> listaTempComunidades= new ArrayList<>();
		List<String> listaComunidades=new ArrayList<>();
		try{			
			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(57);
			Map<String,Integer> mapaTemp=new HashMap<String,Integer>();
			for(TableResponses tr: listaTempComunidades){
				mapaTemp.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
			}
			for(Entry<String,Integer> proy: mapaTemp.entrySet()){
				listaComunidades.add(proy.getKey());
			}
			getSitioPublicoBean().setNumeroComunidadesSalvaguardaC(listaComunidades.size());			
			getSitioPublicoBean().setNumeroPracticasAncestralesC(getTableResponsesFacade().listaSaberesAncestralesSalvaguardaC());
//			System.out.println("Numero comunidades");
//			System.out.println(getSitioPublicoBean().getNumeroComunidadesSalvaguardaC());
//			System.out.println("Numero Saberes Ancestrales");
//			System.out.println(getSitioPublicoBean().getNumeroPracticasAncestralesC());
			obtieneSalvaguardas(SALVAGUARDA_C, "C");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_C);
			getSitioPublicoBean().setCodigoSalvaguarda("C");
			getSitioPublicoBean().setPosicionSalvaguardas(3);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void informacionSalvaguardaD(){
		
		try{			
			getSitioPublicoBean().setTotalEventosFortalecimientoD(getMeetingsFacade().listaEventosFortalecimiento());
			
//			System.out.println("Numero Eventos");
//			System.out.println(getSitioPublicoBean().getTotalEventosFortalecimientoD());
			obtieneSalvaguardas(SALVAGUARDA_D, "D");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_D);
			getSitioPublicoBean().setCodigoSalvaguarda("D");
			getSitioPublicoBean().setPosicionSalvaguardas(4);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void informacionSalvaguardaE(){
		List<TableResponses> listaTempProvincias= new ArrayList<>();
		try{			
			listaTempProvincias = getTableResponsesFacade().listaFomentoGestionComunitariaE();
			getSitioPublicoBean().setNumeroFomentoGestionComunitariaE(listaTempProvincias.size());
			getSitioPublicoBean().setTotalHectareasCoberturaE(getTableResponsesFacade().totalHectareasCoberturaE());
			obtieneSalvaguardas(SALVAGUARDA_E, "E");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_E);
			getSitioPublicoBean().setCodigoSalvaguarda("E");
			getSitioPublicoBean().setPosicionSalvaguardas(5);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void informacionSalvaguardaF(){
		try{			
			getSitioPublicoBean().setTotalRecursosInvertidos(getTableResponsesFacade().listaRecursosInvertidosF());			
			obtieneSalvaguardas(SALVAGUARDA_F, "F");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_F);
			getSitioPublicoBean().setCodigoSalvaguarda("F");
			getSitioPublicoBean().setPosicionSalvaguardas(6);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void informacionSalvaguardaG(){
		List<TableResponses> listaTempComunidades= new ArrayList<>();
		List<String> listaComunidades=new ArrayList<>();
		try{			
			listaTempComunidades = getTableResponsesFacade().listaComunidadesSalvaguardaB_C_G(136);
			Map<String,Integer> mapaTemp=new HashMap<String,Integer>();
			for(TableResponses tr: listaTempComunidades){
				mapaTemp.put(tr.getTareColumnOne().trim(), tr.getTareColumnNumberOne());				
			}
			for(Entry<String,Integer> proy: mapaTemp.entrySet()){
				listaComunidades.add(proy.getKey());
			}
			getSitioPublicoBean().setNumeroComunidadesSalvaguardaG(listaComunidades.size());
			getSitioPublicoBean().setTotalBeneficiariosG(getTableResponsesFacade().listaBeneficiariosSalvaguardaG());
			getSitioPublicoBean().setNumeroAccionesGeneradasG(getTableResponsesFacade().listaAccionesGeneradasSalvaguardaG());
			
//			System.out.println("Numero comunidades");
//			System.out.println(getSitioPublicoBean().getNumeroComunidadesSalvaguardaG());
//			System.out.println("Numero Acciones");
//			System.out.println(getSitioPublicoBean().getNumeroAccionesGeneradasG());
//			System.out.println("Numero Beneficiarios");
//			System.out.println(getSitioPublicoBean().getTotalBeneficiariosG());
			obtieneSalvaguardas(SALVAGUARDA_G, "G");
			getSitioPublicoBean().setResumenSalvaguarda(DESCRIPCION_G);
			getSitioPublicoBean().setCodigoSalvaguarda("G");
			getSitioPublicoBean().setPosicionSalvaguardas(7);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void obtieneSalvaguardas(String salvaguarda, String codigo){
		for(Safeguards sf:getSitioPublicoBean().getListaSalvaguardas()){
			if(sf.getSafeCode().equals(codigo)){
				getSitioPublicoBean().setDescripcionSalvaguarda(sf.getSafeDescription());
				getSitioPublicoBean().setSalvaguarda(salvaguarda);
				getSitioPublicoBean().setTituloSalvaguarda(sf.getSafeTitle());
			}
		}
	}
	public void avanzaSalvaguarda(){
		if(getSitioPublicoBean().getPosicionSalvaguardas() == 7){
			getSitioPublicoBean().setPosicionSalvaguardas(7);
			informacionSalvaguardaG();
		}else{
			getSitioPublicoBean().setPosicionSalvaguardas(getSitioPublicoBean().getPosicionSalvaguardas()+1);
			switch(getSitioPublicoBean().getPosicionSalvaguardas()){
				case 2:
					informacionSalvaguardaB();
					break;
				case 3:
					informacionSalvaguardaC();
					break;
				case 4:
					informacionSalvaguardaD();
					break;
				case 5:
					informacionSalvaguardaE();
					break;
				case 6:
					informacionSalvaguardaF();
					break;
				case 7:
					informacionSalvaguardaG();
					break;	
			}
		}
	}
	public void retrocedeSalvaguarda(){
		if(getSitioPublicoBean().getPosicionSalvaguardas() == 1){
			getSitioPublicoBean().setPosicionSalvaguardas(1);
			informacionSalvaguardaA();
		}else{
			getSitioPublicoBean().setPosicionSalvaguardas(getSitioPublicoBean().getPosicionSalvaguardas()-1);
			switch(getSitioPublicoBean().getPosicionSalvaguardas()){
				case 1:
					informacionSalvaguardaA();
					break;
				case 2:
					informacionSalvaguardaB();
					break;
				case 3:
					informacionSalvaguardaC();
					break;
				case 4:
					informacionSalvaguardaD();
					break;
				case 5:
					informacionSalvaguardaE();
					break;
				case 6:
					informacionSalvaguardaF();
					break;
				case 7:
					informacionSalvaguardaG();
					break;	
			}
		}
	}
}

