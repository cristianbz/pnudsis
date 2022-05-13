/**
@autor proamazonia [Christian Báez]  8 mar. 2021

**/
package ec.gob.ambiente.suia.service;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.suia.model.GeographicalLocations;

@Stateless
@LocalBean
public class GeographicalLocationsFacade extends AbstractFacade<GeographicalLocations, Integer> {
	/**
	 * Default constructor.
	 */
	public GeographicalLocationsFacade() {
		super(GeographicalLocations.class, Integer.class);
	}

	public void eliminarLogico(GeographicalLocations entidad) {
		entidad.setGeloStatus(false);
		edit(entidad);
	}

	public List<Object[]> listarProvincias() throws Exception {
		String sql = "select g.gelo_name,g.gelo_id,g.gelo_codification_inec from public.geographical_locations g where g.gelo_parent_id=1 and g.gelo_status=true order by g.gelo_codification_inec";
		return consultaNativa(sql);
	}

	public List<Object[]> listarCantones() throws Exception {
		String sql = "select g.gelo_name,g.gelo_id,g.gelo_codification_inec,g.gelo_parent_id from public.geographical_locations g "
				+ " where gelo_parent_id in (select g.gelo_id from public.geographical_locations g where g.gelo_parent_id=1 and g.gelo_status=true) "
				+ " and g.gelo_status=true order by g.gelo_codification_inec";
		return consultaNativa(sql);
	}

	public List<Object[]> listarParroquias() throws Exception {
		String sql = "select g.gelo_name,g.gelo_id,g.gelo_codification_inec,g.gelo_parent_id from public.geographical_locations g"
				+ " where  gelo_parent_id in ( select g.gelo_id from public.geographical_locations g "
				+ " where gelo_parent_id in (select g.gelo_id from public.geographical_locations g where g.gelo_parent_id=1 and g.gelo_status=true)"
				+ " and g.gelo_status=true order by g.gelo_codification_inec )" + " order by g.gelo_codification_inec";
		return consultaNativa(sql);
	}
	public List<Object[]> listarCantonesPorProvincia(String codigoProvincia) throws Exception {
		String sql = "select g.gelo_name,g.gelo_id,g.gelo_codification_inec,g.gelo_parent_id from public.geographical_locations g " 
				 + " where gelo_parent_id in (select g.gelo_id from public.geographical_locations g where g.gelo_parent_id=1 and g.gelo_status=true) " 
				 + " and SUBSTRING(gelo_codification_inec FROM 1 FOR 2)=  '"+ codigoProvincia + "'"
				 + "and g.gelo_status=true order by g.gelo_codification_inec";
		
		return consultaNativa(sql);
	}

	public List<Object[]> listarParroquiasPorCanton(String codigoCanton) throws Exception {
		String sql = "select g.gelo_name,g.gelo_id,g.gelo_codification_inec,g.gelo_parent_id from public.geographical_locations g"
				+ " where  gelo_parent_id in ( select g.gelo_id from public.geographical_locations g "
				+ " where gelo_parent_id in (select g.gelo_id from public.geographical_locations g where g.gelo_parent_id=1 and g.gelo_status=true)"
				+ " and SUBSTRING(gelo_codification_inec FROM 1 FOR 4)=  '"+ codigoCanton + "'"
				+ " and g.gelo_status=true order by g.gelo_codification_inec )" + " order by g.gelo_codification_inec";		
		return consultaNativa(sql);
	}
}

