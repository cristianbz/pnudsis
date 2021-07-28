package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.Safeguards;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class SafeguardsFacade extends AbstractFacade<Safeguards, Integer> {


	public SafeguardsFacade() {
		super(Safeguards.class,Integer.class);
	}

	/**
	 * Carga todas las salvaguardas registradas
	 */
	public List<Safeguards> buscarTodosLosProyectos() throws Exception{
		String sql="SELECT S FROM Safeguards S WHERE S.safeStatus=true AND S.actionPlans.acplIscurrent=TRUE AND S.actionPlans.acplStatus=TRUE ORDER BY S.safeId";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();		
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<Object[]> listarSalvaguardas() throws Exception {
		String sql = "select safe_id,safe_description,safe_order,safe_code,safe_title from sigma.safeguards WHERE safe_level=1 order by safe_order";
		return consultaNativa(sql);
	}
}
