/**
@autor proamazonia [Christian Báez]  13 abr. 2021

**/
package ec.gob.ambiente.sis.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ec.gob.ambiente.sigma.model.Components;
import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;

public class OperacionesCatalogo {
	/**
	 * Busca la descripcion de un catalogo por codigo y lista de catalogo
	 * @param codigo
	 * @param listaCatalogo
	 * @return
	 */
	public static String ubicaDescripcionCatalogo(int codigo,List<Catalogs> listaCatalogo){
		String valor="";
//		Optional<Catalogs> resultado= listaCatalogo.stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
//		if(resultado.isPresent()){
//			Catalogs catalogo= resultado.get();
//			valor=catalogo.getCataText1();
//		}
		for (Catalogs c : listaCatalogo) {
			if(c.getCataId() == codigo){
				valor = c.getCataText1();
				break;
			}
		}
		return valor;
	}
	
	public static String ubicaDescripcionCatalogoText2(int codigo,List<Catalogs> listaCatalogo){
		String valor="";
//		Optional<Catalogs> resultado= listaCatalogo.stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
//		if(resultado.isPresent()){
//			Catalogs catalogo= resultado.get();
//			valor=catalogo.getCataText2();
//		}
		for (Catalogs c : listaCatalogo) {
			if(c.getCataId() == codigo){
				valor = c.getCataText2();
				break;
			}
		}
		return valor;
	}
	
	/**
	 * Prepara datos para agregar un nuevo catalogo
	 * @param tipoCatalogo
	 * @param listaCatalogo
	 * @param catalogo
	 * @param usuario
	 * @return
	 */
	public static Catalogs agregarNuevoCatalogo(int tipoCatalogo,List<Catalogs> listaCatalogo,Catalogs catalogo,String usuario){
		CatalogsType tipo=new CatalogsType();
		
		tipo.setCatyId(tipoCatalogo);
		catalogo.setCatalogsType(tipo);

//		listaCatalogo = listaCatalogo.stream().sorted((cl1,cl2)->cl1.getCataOrder().compareTo(cl2.getCataOrder())).collect(Collectors.toList());
		Collections.sort(listaCatalogo, new Comparator<Catalogs>(){
			@Override
			public int compare(Catalogs o1, Catalogs o2) {
				return o1.getCataOrder().compareTo(o2.getCataOrder());
			}
		});
		if(listaCatalogo.size()==0)
			catalogo.setCataOrder(1);
		else
			catalogo.setCataOrder(listaCatalogo.get(listaCatalogo.size()-1).getCataOrder()+1);

		catalogo.setCataNumber(catalogo.getCataOrder());
		catalogo.setCataText1(".");
		catalogo.setCataText2(catalogo.getCataText2());
		catalogo.setCataCreationDate(new Date());
		catalogo.setCataStatus(true);
		catalogo.setCataCreatorUser(usuario);
		return catalogo;
	}
	
	/**
	 * Ubica un codigo de catalogo en un catalogo
	 * @param listaCatalogo
	 * @param codigo
	 * @return
	 */
	public static boolean ubicaCodigoEnCatalogo(List<Catalogs> listaCatalogo,int codigo){
		boolean encontrado=false;
		for (Catalogs catalogo : listaCatalogo) {			
			if(codigo == catalogo.getCataId()){
				encontrado=true;
				break;
			}
		}
		return encontrado;
	}
	/**
	 * Ubica el codigo del componente en base al nombre
	 * @param componente
	 * @param listaComponentes
	 * @return
	 */
	public static int ubicaComponente(String componente,List<Components> listaComponentes){
		int codigo=0;
		for (Components comp : listaComponentes) {
			if(comp.getCompName().equals(componente)){
				codigo=comp.getCompId();
				break;
			}
		}
		return codigo;
	}
	
	public static String ubicaComponentePorCodigo(int codigo,List<Components> listaComponentes){
		String componente="";
		for (Components comp : listaComponentes) {
			if(comp.getCompId()== codigo){
				componente=comp.getCompName();
				break;
			}
		}
		return componente;
	}
}

