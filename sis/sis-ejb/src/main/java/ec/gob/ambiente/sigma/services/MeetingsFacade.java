/**
@autor proamazonia [Christian Báez]  11 nov. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sigma.model.Meetings;
import ec.gob.ambiente.sis.dao.AbstractFacade;

@Stateless
@LocalBean
public class MeetingsFacade extends AbstractFacade<Meetings, Integer>{
	public MeetingsFacade(){
		super(Meetings.class,Integer.class);
	}
	/**
	 * Lista de eventos de fortalecimiento hombres
	 * @return
	 * @throws Exception
	 */
	public int listaEventosFortalecimientoHombres() throws Exception{
		Integer valor= new Integer(0);
		List<Object[]> resultado= null;		
		String sql ="SELECT COUNT(meto_id) as total FROM sigma.meetings_totals  where meto_number1 >0 AND meto_status=TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado)
				valor = Integer.valueOf(obj.toString());
		}
		return valor;
	}
	/**
	 * Lista de eventos de fortalecimiento mujeres
	 * @return
	 * @throws Exception
	 */
	public int listaEventosFortalecimientoMujeres() throws Exception{
		Integer valor= new Integer(0);
		List<Object[]> resultado= null;		
		String sql ="SELECT COUNT(meto_id) as total FROM sigma.meetings_totals where meto_number2 >0 AND meto_status=TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado)
				valor = Integer.valueOf(obj.toString());
		}
		return valor;
	}
}

