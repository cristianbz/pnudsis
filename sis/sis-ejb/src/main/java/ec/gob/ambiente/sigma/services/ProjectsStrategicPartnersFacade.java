/**
@autor proamazonia [Christian Báez]  13 jul. 2021

**/
package ec.gob.ambiente.sigma.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import ec.gob.ambiente.sigma.model.ProjectsStrategicPartners;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;

@Stateless
@LocalBean
public class ProjectsStrategicPartnersFacade extends AbstractFacade<ProjectsStrategicPartners, Integer> {
	public ProjectsStrategicPartnersFacade(){
		super(ProjectsStrategicPartners.class,Integer.class);
	}
	/**
	 * Consulta los partners del proyecto
	 * @param codigoProyecto
	 * @return
	 * @throws Exception
	 */
	public List<ProjectsStrategicPartners> listaPartnersActivos(Integer codigoProyecto)throws Exception{
		String sql="SELECT PSP FROM ProjectsStrategicPartners PSP WHERE  PSP.projects.projId=:codigoProyecto AND PSP.pspaStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoProyecto", codigoProyecto);
		return findByCreateQuery(sql, camposCondicion);
	}
	/**
	 * Obtiene el partner del proyecto
	 * @param codigoProyecto
	 * @param codigoPartner
	 * @return
	 * @throws DaoException
	 */
	public ProjectsStrategicPartners partnerDelProyecto(Integer codigoProyecto,Integer codigoPartner)throws DaoException{
		try{
			String sql="SELECT PSP FROM ProjectsStrategicPartners PSP WHERE  PSP.projects.projId=:codigoProyecto AND PSP.pspaId=:codigoPartner AND PSP.pspaStatus=TRUE";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoProyecto", codigoProyecto);
			camposCondicion.put("codigoPartner", codigoPartner);
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	/**
	 * Devuelve el partner estrategico
	 * @param codigoPartner El codigo del partner estrategico
	 * @return
	 * @throws DaoException
	 */
	public ProjectsStrategicPartners partnerEstrategico(Integer codigoPartner)throws DaoException{
		try{
			String sql="SELECT PSP FROM ProjectsStrategicPartners PSP WHERE  PSP.pspaId=:codigoPartner AND PSP.pspaStatus=TRUE";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();			
			camposCondicion.put("codigoPartner", codigoPartner);
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	/**
	 * Devuelve los proyectos del socio estrategico
	 * @param rucSocio  RUC del socio estrategico
	 * @return Lista de proyectos
	 * @throws Exception
	 */
	public List<ProjectsStrategicPartners> listaProyectosSocioEstrategico(String rucSocio)throws Exception{
		String sql="SELECT PSP FROM ProjectsStrategicPartners PSP,Projects P, Partners PA WHERE PSP.projects.projId = P.projId  AND PSP.partners.partId = PA.partId AND PA.partIdNumber=:rucSocio AND PSP.pspaStatus=TRUE AND P.projRegisterStatus='V' ORDER BY P.projTitle";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("rucSocio", rucSocio);
		return findByCreateQuery(sql, camposCondicion);
	}
}

