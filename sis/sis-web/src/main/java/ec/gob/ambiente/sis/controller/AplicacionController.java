/**
@autor proamazonia [Christian Báez]  26 mar. 2021

**/
package ec.gob.ambiente.sis.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sigma.services.CatalogTypeFacade;
import ec.gob.ambiente.sigma.services.ComponentsFacade;
import ec.gob.ambiente.sigma.services.PartnersFacade;
import ec.gob.ambiente.sigma.services.SafeguardsFacade;
import ec.gob.ambiente.sis.bean.AplicacionBean;
import ec.gob.ambiente.sis.services.CatalogsFacade;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
import ec.gob.ambiente.suia.service.GeographicalLocationsFacade;
import lombok.Getter;
import lombok.Setter;

@Named
@ApplicationScoped
public class AplicacionController implements Serializable{


	private static final long serialVersionUID = 1L;

	private static final Logger LOG = Logger.getLogger(AplicacionController.class);
	
	@Getter
    @Setter
	private ResourceBundle bundle;

    @Getter
    @Setter
    @Inject
    private AplicacionBean aplicacionBean;
    
    @EJB
   	@Getter
   	private PartnersFacade partnersFacade;
    
    @EJB
   	@Getter
   	private ComponentsFacade componentsFacade;

   	
    @EJB
   	@Getter
   	private CatalogsFacade catalogsFacade;
    
    @EJB
   	@Getter
   	private CatalogTypeFacade catalogTypeFacade;
    
    @EJB
   	@Getter
   	private SafeguardsFacade safeguardsFacade;
    
    @EJB
   	@Getter
   	private GeographicalLocationsFacade geographicalLocationsFacade;
    
    @Getter
    @Setter
    @Inject
    private MensajesController mensajesController;
    
    @PostConstruct
    public void init(){
    	cargarCatalogos();   
		FacesContext context = FacesContext.getCurrentInstance();
		bundle = context.getApplication().evaluateExpressionGet(context, "#{txt}", ResourceBundle.class);
    }
    
    public void cargarCatalogos(){
    	try{
    		getAplicacionBean().setListaProvincias(getGeographicalLocationsFacade().listarProvincias());
    		getAplicacionBean().setListaTodosCantones(getGeographicalLocationsFacade().listarCantones());
    		getAplicacionBean().setListaTodasParroquias(getGeographicalLocationsFacade().listarParroquias());
    		getAplicacionBean().setListaPueblosNacionalidades(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PUEBLOSNACIONALIDADES.getCodigo()));
    		getAplicacionBean().setListaPueblosNacionalidades(getAplicacionBean().getListaPueblosNacionalidades().stream().sorted((pn1,pn2)->pn1.getCataText1().compareTo(pn2.getCataText1())).collect(Collectors.toList()));
    		getAplicacionBean().setListaAutoIdentificacion(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.AUTOIDENTIFICACION.getCodigo()));
    		getAplicacionBean().setListaAutoIdentificacion(getAplicacionBean().getListaAutoIdentificacion().stream().sorted((a1,a2)->a1.getCataText1().compareTo(a2.getCataText1())).collect(Collectors.toList()));
    		
    		getAplicacionBean().setListaPublico(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PUBLICO.getCodigo()));
    		getAplicacionBean().setListaPublico(getAplicacionBean().getListaPublico().stream().sorted((p1,p2)->p1.getCataText1().compareTo(p2.getCataText1())).collect(Collectors.toList()));
    		getAplicacionBean().setListaMetodo(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.METODO.getCodigo()));
    		getAplicacionBean().setListaMetodo(getAplicacionBean().getListaMetodo().stream().sorted((m1,m2)->m1.getCataText1().compareTo(m2.getCataText1())).collect(Collectors.toList()));
    		getAplicacionBean().setListaCatalogoRiesgo(new ArrayList<>());
    		getAplicacionBean().setListaCatalogoRiesgo(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.RIESGO.getCodigo()));
    		getAplicacionBean().setListaRecursos(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.RECURSOS.getCodigo()));
    		getAplicacionBean().setListaRecursos(getAplicacionBean().getListaRecursos().stream().sorted((r1,r2)->r1.getCataText1().compareTo(r2.getCataText1())).collect(Collectors.toList()));
    		getAplicacionBean().setListaPeriodicidad(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PERIODICIDAD.getCodigo()));
    		getAplicacionBean().setListaPeriodicidad(getAplicacionBean().getListaPeriodicidad().stream().sorted((p1,p2)->p1.getCataText1().compareTo(p2.getCataText1())).collect(Collectors.toList()));
    		getAplicacionBean().setListaSistemas(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.SISTEMAS.getCodigo()));
    		getAplicacionBean().setListaSistemas(getAplicacionBean().getListaSistemas().stream().sorted((s1,s2)->s1.getCataText1().compareTo(s2.getCataText1())).collect(Collectors.toList()));
    		getAplicacionBean().setListaAccion(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.ACCION.getCodigo()));
    		getAplicacionBean().setListaAccion(getAplicacionBean().getListaAccion().stream().sorted((a1,a2)->a1.getCataText1().compareTo(a2.getCataText1())).collect(Collectors.toList()));
    		getAplicacionBean().setListaSalvaguardas(getSafeguardsFacade().listarSalvaguardas());
    		getAplicacionBean().setListaAlternativaEconomica(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.ALTERNATIVAECONOMICA.getCodigo()));
    		getAplicacionBean().setListaAlternativaEconomica(getAplicacionBean().getListaAlternativaEconomica().stream().sorted((ae1,ae2)->ae1.getCataText1().compareTo(ae2.getCataText1())).collect(Collectors.toList()));
    		getAplicacionBean().setListaMonitoreoRemoto(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOREMOTO.getCodigo()));
    		getAplicacionBean().setListaMonitoreoInSitu(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.MONITOREOINSITU.getCodigo()));
    		getAplicacionBean().setListaLineaAccion(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.LINEAACCION.getCodigo()));
    		getAplicacionBean().setListaControlVigilancia(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.CONTROLVIGILANCIA.getCodigo()));
    		getAplicacionBean().setListaTipoOrganizacion(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.TIPOORGANIZACION.getCodigo()));
    		getAplicacionBean().setListaTipoIncentivo(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.TIPOINCENTIVO.getCodigo()));
    		getAplicacionBean().setListaInstitucionAcompania(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.TIPOINSTITUCION.getCodigo()));
    		getAplicacionBean().setCodigoIndigena(getCatalogsFacade().codigoIndigena());

    	}catch(Exception e ){
    		LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarCatalogos " + ": ").append(e.getMessage()));
    	}
    }  
    
    public void cargarSalvaguardas(){
    	try{
    		if(getAplicacionBean().getListaSalvaguardas() == null){
    			getAplicacionBean().setListaSalvaguardas(getSafeguardsFacade().listarSalvaguardas());
    		}
    	}catch(Exception e){
    		LOG.error(new StringBuilder().append(this.getClass().getName() + "." + "cargarSalvaguardas " + ": ").append(e.getMessage()));
    	}
    }
}

