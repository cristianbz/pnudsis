/**
@autor proamazonia [Christian Báez]  26 mar. 2021

**/
package ec.gob.ambiente.sis.controller;


import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

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

	private static final Logger log = Logger.getLogger(AplicacionController.class);

    @Getter
    @Setter
    @Inject
    private AplicacionBean aplicacionBean;
    
    @EJB
   	@Getter
   	private CatalogsFacade catalogsFacade;
    
    @EJB
   	@Getter
   	private GeographicalLocationsFacade geographicalLocationsFacade;
    
    @Getter
    @Setter
    @Inject
    private MensajesController mensajesController;
    
    @PostConstruct
    public void ini(){
    	cargarProvinciaCantonParroquia();    	
    }
    
    public void cargarProvinciaCantonParroquia(){
    	try{
    		getAplicacionBean().setListaProvincias(getGeographicalLocationsFacade().listarProvincias());
    		getAplicacionBean().setListaTodosCantones(getGeographicalLocationsFacade().listarCantones());
    		getAplicacionBean().setListaTodasParroquias(getGeographicalLocationsFacade().listarParroquias());
    		getAplicacionBean().setListaPueblosNacionalidades(getCatalogsFacade().buscaCatalogosPorTipo(TipoCatalogoEnum.PUEBLOSNACIONALIDADES.getCodigo()));
    	}catch(Exception e ){
    		e.printStackTrace();
    	}
    }
    
    
}

