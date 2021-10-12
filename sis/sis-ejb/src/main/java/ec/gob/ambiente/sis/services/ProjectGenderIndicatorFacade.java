/**
@autor proamazonia [Christian Báez]  4 oct. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.hibernate.Hibernate;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.ProjectGenderIndicator;

@Stateless
@LocalBean
public class ProjectGenderIndicatorFacade extends AbstractFacade<ProjectGenderIndicator, Integer>{
	public ProjectGenderIndicatorFacade(){
		super(ProjectGenderIndicator.class,Integer.class);
	}
	/**
	 * Agrega o edita un ProjectGenderindicator
	 * @param pgi
	 * @throws Exception
	 */
	public void agregarEditar(ProjectGenderIndicator pgi)throws Exception{
		if(pgi.getPgigId()== null)
			create(pgi);
		else
			edit(pgi);
	}
	public List<ProjectGenderIndicator> listaPorProjectGender(int codigoProjectGenderInfo)throws Exception{		
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		String sql="SELECT PGI from ProjectGenderIndicator PGI WHERE PGI.pgigStatus=TRUE AND PGI.projectsGenderInfo.pginId=:codigoProjectGenderInfo";
		camposCondicion.put("codigoProjectGenderInfo", codigoProjectGenderInfo);				
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<ProjectGenderIndicator> listaLineasGeneroProyectoPartner(Integer codigoProyecto,Integer codigoPartner) throws Exception{
		String sql="";
		List<ProjectGenderIndicator> listaTemp=new ArrayList<ProjectGenderIndicator>();
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		if(codigoProyecto>0 && codigoPartner>0){
			sql="SELECT PGI from ProjectGenderIndicator PGI WHERE PGI.pgigStatus=TRUE AND PGI.projectsGenderInfo.projects.projId=:codigoProyecto AND PGI.projectsGenderInfo.pspaId=:codigoPartner ORDER BY PGI.projectsGenderInfo.cataId.cataNumber";
			camposCondicion.put("codigoProyecto", codigoProyecto);		
			camposCondicion.put("codigoPartner", codigoPartner);
		}else{
			sql="SELECT PGI from ProjectGenderIndicator PGI WHERE PGI.pgigStatus=TRUE AND PGI.projectsGenderInfo.projects.projId=:codigoProyecto AND PGI.projectsGenderInfo.pspaId IS NULL ORDER BY PGI.projectsGenderInfo.cataId.cataNumber";
			camposCondicion.put("codigoProyecto", codigoProyecto);		
		}		
		listaTemp = findByCreateQuery(sql, camposCondicion);
		for (ProjectGenderIndicator pgi : listaTemp) {
			Hibernate.initialize(pgi.getProjectsGenderInfo());
		}
		return listaTemp;
//		return findByCreateQuery(sql, camposCondicion);
	}
}


