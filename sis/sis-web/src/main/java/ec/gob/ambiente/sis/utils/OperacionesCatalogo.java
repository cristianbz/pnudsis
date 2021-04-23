/**
@autor proamazonia [Christian Báez]  13 abr. 2021

**/
package ec.gob.ambiente.sis.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;

import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.CatalogsType;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoCatalogoEnum;
import ec.gob.ambiente.sis.utils.enumeraciones.TipoParticipanteEnum;

public class OperacionesCatalogo {
	/**
	 * Busca la descripcion de un catalogo por codigo y lista de catalogo
	 * @param codigo
	 * @param listaCatalogo
	 * @return
	 */
	public static String ubicaDescripcionCatalogo(int codigo,List<Catalogs> listaCatalogo){
		String valor="";
		Optional<Catalogs> resultado= listaCatalogo.stream().filter((p)->p.getCataId().equals(codigo)).findFirst();
		if(resultado.isPresent()){
			Catalogs catalogo= resultado.get();
			valor=catalogo.getCataText1();
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

		listaCatalogo = listaCatalogo.stream().sorted((cl1,cl2)->cl1.getCataOrder().compareTo(cl2.getCataOrder())).collect(Collectors.toList());
		if(listaCatalogo.size()==0)
			catalogo.setCataOrder(1);
		else
			catalogo.setCataOrder(listaCatalogo.get(listaCatalogo.size()-1).getCataOrder()+1);

		catalogo.setCataNumber(catalogo.getCataOrder());
		catalogo.setCataText1(catalogo.getCataText1());
		catalogo.setCataText2(catalogo.getCataText1());
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

}

