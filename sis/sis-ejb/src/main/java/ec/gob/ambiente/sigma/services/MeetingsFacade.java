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
		String sql ="SELECT sum(meet_total_male) FROM sigma.meetings WHERE meet_status=TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado){
				if (obj!=null)
					valor = Integer.valueOf(obj.toString());
			}
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
		String sql ="SELECT sum(meet_total_female) FROM sigma.meetings WHERE meet_status=TRUE;";
		resultado = (List<Object[]>)consultaNativa(sql);		
		if(resultado.size()>0){
			for(Object obj:resultado){
				if (obj!=null)
					valor = Integer.valueOf(obj.toString());
			}
		}
		return valor;
	}
}

