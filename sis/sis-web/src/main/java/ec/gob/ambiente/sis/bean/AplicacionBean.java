/**
@autor proamazonia [Christian Báez]  26 mar. 2021

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import ec.gob.ambiente.sigma.model.Partners;
import ec.gob.ambiente.sis.model.Catalogs;
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
	
	@Getter
	@Setter
	private List<Catalogs> listaAutoIdentificacion;
	

	
	@Getter
	@Setter
	private List<Catalogs> listaMetodo;
	
	@Getter
	@Setter
	private List<Catalogs> listaPublico;
	

	
	@Getter
	@Setter
	private List<Catalogs> listaRecursos;
	
	@Getter
	@Setter
	private List<Catalogs> listaCatalogoRiesgo;
	
	@Getter
	@Setter
	private List<Catalogs> listaPeriodicidad;
	
	@Getter
	@Setter
	private List<Catalogs> listaSistemas;
	
	@Getter
	@Setter
	private List<Catalogs> listaAccion;
	
	@Getter
	@Setter
	private List<Catalogs> listaAlternativaEconomica;
	
	@Getter
	@Setter
	private List<Catalogs> listaMonitoreoRemoto;
	
	@Getter
	@Setter
	private List<Catalogs> listaMonitoreoInSitu;
	
	@Getter
	@Setter
	private List<Catalogs> listaLineaAccion;

	@Getter
	@Setter
	private List<Catalogs> listaControlVigilancia;

	
	@Getter
	@Setter
	private List<Object[]> listaSalvaguardas;
	


}

