/**
@autor proamazonia [Christian Báez]  26 mar. 2021

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.suia.model.GeographicalLocations;
import lombok.Getter;
import lombok.Setter;

@Named
@ApplicationScoped
public class AplicacionBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private List<Object[]> listaProvincias;
	
	@Getter
	@Setter
	private List<Object[]> listaTodosCantones;
	
	@Getter
	@Setter
	private List<Object[]> listaTodasParroquias;
	
	@Getter
	@Setter
	private List<Catalogs> listaPueblosNacionalidades;

}

