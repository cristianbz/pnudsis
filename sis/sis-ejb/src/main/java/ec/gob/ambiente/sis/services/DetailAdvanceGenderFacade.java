/**
@autor proamazonia [Christian Báez]  3 jun. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.DetailAdvanceGender;

@Stateless
@LocalBean
public class DetailAdvanceGenderFacade extends AbstractFacade<DetailAdvanceGender, Integer>{
	public DetailAdvanceGenderFacade(){
		super(DetailAdvanceGender.class,Integer.class);
	}
	/**
	 * Agrega o edita un registro de detalle avance genero
	 * @param detalleAvance
	 * @throws Exception
	 */
	public void agregarEditarRegistroDetalleAvanceGenero(DetailAdvanceGender detalleAvance)throws Exception{
		if(detalleAvance.getDtagId() == null)
			create(detalleAvance);
		else
			edit(detalleAvance);
	}
	public List<DetailAdvanceGender> buscaDetalleAvanceGenero(int codigoAvanceGenero)throws Exception{
		String sql="SELECT DA FROM DetailAdvanceGender DA WHERE DA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion AND GA.advanceExecutionSafeguards.adexIsReported=false AND GA.advanceExecutionSafeguards.adexIsGender=true AND GA.advanceExecutionSafeguards.adexStatus=true ";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceGenero", codigoAvanceGenero);
		return findByCreateQuery(sql, camposCondicion);	
	}
	/**
	 * Eliminacion logica del registro
	 * @param detalle
	 * @throws Exception
	 */
	public void eliminaRegistroDetalleAvanceGenero(DetailAdvanceGender detalle)throws Exception{
		detalle.setDtagStatus(false);
		edit(detalle);
	}
	
	public List<DetailAdvanceGender> listadoDetalleAvanceGenero(int codigoAvanceGenero)throws Exception{
		String sql="SELECT DA FROM DetailAdvanceGender DA WHERE DA.advanceExecutionProjectGender.aepgId =:codigoAvanceGenero AND DA.dtagStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoAvanceGenero", codigoAvanceGenero);
		return findByCreateQuery(sql, camposCondicion);	
	}
	
	public String lineaDeAccion(int codigoDetalle)throws Exception{
		String valor="";
		String sql="SELECT c.cata_text2 FROM sis.detail_advance_gender dag, sis.gender_advances ga, sis.projects_gender_info pgi, sis.catalogs c "				
					+ " WHERE dag.gead_id = ga.gead_id AND pgi.pgin_id = ga.pgin_id AND c.cata_id = pgi.cata_id AND dag.dtag_id=" + codigoDetalle;
		List<Object[]> resultado = (List<Object[]>)consultaNativa(sql);		
		for(Object obj:resultado){
			valor = obj.toString();
		}
		return valor;
	}
	public String lineaDeAccionOtrosTemas(int codigoDetalle)throws Exception{
		String valor="";
		String sql="SELECT pgi.pgin_other_line FROM sis.detail_advance_gender dag, sis.gender_advances ga, sis.projects_gender_info pgi "				
					+ " WHERE dag.gead_id = ga.gead_id AND pgi.pgin_id = ga.pgin_id AND dag.dtag_id=" + codigoDetalle;
		List<Object[]> resultado = (List<Object[]>)consultaNativa(sql);		
		for(Object obj:resultado){
			valor = obj.toString();
		}
		return valor;
	}
}

