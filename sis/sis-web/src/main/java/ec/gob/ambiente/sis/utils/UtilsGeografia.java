/**
@autor proamazonia [Christian Báez]  2 jun. 2021

**/
package ec.gob.ambiente.sis.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ec.gob.ambiente.suia.model.GeographicalLocations;

public class UtilsGeografia {

	public static List<GeographicalLocations> filtraCantones(List<Object[]> listaCantones,int codigoProvincia){
		List<GeographicalLocations> listaAux= new ArrayList<>();
		List<Object[]> listaTemporal = listaCantones.stream().filter(canton->Integer.valueOf(canton[3].toString()).equals(codigoProvincia)).collect(Collectors.toList());		
		for (Object[] objects : listaTemporal) {
			GeographicalLocations geo=new GeographicalLocations();
			geo.setGeloName(objects[0].toString());
			geo.setGeloId(Integer.valueOf(objects[1].toString()));
			geo.setGeloCodificationInec(objects[2].toString());
			listaAux.add(geo);
		}
		return listaAux;
	}
	
	public static List<GeographicalLocations> filtraParroquias(List<Object[]> listaParroquias,int codigoCanton){
		List<GeographicalLocations> listaAux= new ArrayList<>();
		List<Object[]> listaTemporal = listaParroquias.stream().filter(canton->Integer.valueOf(canton[3].toString()).equals(codigoCanton)).collect(Collectors.toList());		
		for (Object[] objects : listaTemporal) {
			GeographicalLocations geo=new GeographicalLocations();
			geo.setGeloName(objects[0].toString());
			geo.setGeloId(Integer.valueOf(objects[1].toString()));
			geo.setGeloCodificationInec(objects[2].toString());
			listaAux.add(geo);
		}
		return listaAux;
	}
}

