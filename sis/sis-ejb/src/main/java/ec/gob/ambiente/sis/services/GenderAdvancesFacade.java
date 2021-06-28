/**
@autor proamazonia [Christian Báez]  1 jun. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.hibernate.Hibernate;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.sis.model.GenderAdvances;
import ec.gob.ambiente.sis.model.TableResponses;

@Stateless
@LocalBean
public class GenderAdvancesFacade extends AbstractFacade<GenderAdvances,Integer>{
	public GenderAdvancesFacade() {
		super(GenderAdvances.class, Integer.class);
	}
	/**
	 * Busca avance de genero por codigo de proyectoGeneroInfo
	 * @param codigoProjectGenderInfo
	 * @return
	 * @throws DaoException
	 */
	public GenderAdvances buscaPorProjectGenderInfoAvanceExecution(int codigoProjectGenderInfo, int codigoAvanceEjecucion) throws DaoException{
		try{
			String sql="SELECT GA FROM GenderAdvances GA WHERE GA.projectsGenderInfo.pginId=:codigoProjectGenderInfo AND GA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("codigoProjectGenderInfo", codigoProjectGenderInfo);
			camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);			
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}
	}
	
//	public List<GenderAdvances> buscaAvancesGeneroPorLineaEstrategica(int codigoLinea,int codigoAvanceEjecucion)throws DaoException{		
//			String sql="SELECT GA FROM GenderAdvances GA WHERE GA.advanceExecutionSafeguards.adexId=:codigoAvanceEjecucion AND GA.advanceExecutionSafeguards.adexIsReported=false AND GA.advanceExecutionSafeguards.adexIsGender=true AND GA.advanceExecutionSafeguards.adexStatus=true ";
//			Map<String, Object> camposCondicion=new HashMap<String, Object>();
//			camposCondicion.put("codigoLinea", codigoLinea);
//			camposCondicion.put("codigoAvanceEjecucion", codigoAvanceEjecucion);
//			return findByCreateQuery(sql, camposCondicion);		
//	}
}

