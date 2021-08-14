/**
@autor proamazonia [Christian Báez]  18 feb. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.Catalogs;

@LocalBean
@Stateless
public class CatalogsFacade extends AbstractFacade<Catalogs,Integer>{

	public CatalogsFacade() {
		super(Catalogs.class, Integer.class);
	}
	/**
	 * Busca los catalogos por tipo
	 * @param tipoCatalogo
	 * @return
	 * @throws Exception
	 */
	public List<Catalogs> buscaCatalogosPorTipo(int tipoCatalogo)throws Exception{
		String sql="SELECT C FROM Catalogs C WHERE C.catalogsType.catyId=:tipoCatalogo ORDER BY C.cataText1, C.cataOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("tipoCatalogo", tipoCatalogo);
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<Object[]> buscarDescripcionCatalogoPorTipo(int tipoCatalogo) throws Exception{
		String sql = "select cata_text1 from sis.catalogs where caty_id= " + tipoCatalogo
					 + " and cata_status=true order by cata_text1";		
		return consultaNativa(sql);
	}
	
	public void agregaEditaCatalogo(Catalogs catalogo) throws Exception{
		if(catalogo.getCataId() == null)
			create(catalogo);
		else
			edit(catalogo);
	}
	/**
	 * Carga todos los catalogos
	 * @return
	 * @throws Exception
	 */
	public List<Catalogs> buscaTodosCatalogos() throws Exception{
		String sql="SELECT C FROM Catalogs C ORDER BY C.catalogsType.catyId,C.cataOrder";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Retorna las lineas de accion de genero
	 * @return
	 * @throws Exception
	 */
	public List<Object[]>  buscaCatalogoLineasAccionGenero()throws Exception{
		String sql = "SELECT cata_id,caty_id,cata_order,cata_text2 FROM sis.catalogs WHERE caty_id IN(SELECT caty_id FROM sis.catalogs_types WHERE caty_mnemonic IN ('LINEA DE GENERO 1','LINEA DE GENERO 2','LINEA DE GENERO 3')) ORDER BY cata_id";		
	return consultaNativa(sql);
	}
	
	public Catalogs catalogoOtrosDeLineaAccion(String catalogo,int posicion)throws DaoException{
		try{
			String sql="SELECT C FROM Catalogs C WHERE C.catalogsType.catyMnemonic=:catalogo AND C.cataNumber=:posicion AND C.cataStatus = TRUE";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();		
			camposCondicion.put("catalogo", catalogo);
			camposCondicion.put("posicion", posicion);
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();			
		}
		
	}
}

