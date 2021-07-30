/**
@autor proamazonia [Christian Báez]  19 jul. 2021

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
import ec.gob.ambiente.sis.model.ProjectUsers;

@Stateless
@LocalBean
public class ProjectUsersFacade extends AbstractFacade<ProjectUsers, Integer>{
	public ProjectUsersFacade(){
		super(ProjectUsers.class,Integer.class);
	}
	/**
	 * Lista de proyectos del usuario
	 * @param codigoUsuario
	 * @return
	 * @throws Exception
	 */
	public List<ProjectUsers> listaProyectosDelUsuario(Integer codigoUsuario) throws Exception{
		String sql="SELECT PU FROM ProjectUsers PU WHERE PU.users.userId = :codigoUsuario AND PU.prusStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("codigoUsuario", codigoUsuario);
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public List<ProjectUsers> listaProyectoUsuarios() throws Exception{
		String sql="SELECT PU FROM ProjectUsers PU ";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();		
		return findByCreateQuery(sql, camposCondicion);
	}
	
	public ProjectUsers agregarEditarProyectousuario(ProjectUsers proyectoUsuario)throws Exception{
		if(proyectoUsuario.getPrusId() == null)
			create(proyectoUsuario);
		else
			edit(proyectoUsuario);
		return proyectoUsuario;
	}
	/**
	 * Valida si un usuario esta ya registrado
	 * @param codigoUsuario
	 * @param codigoProyecto
	 * @param codigoPartner
	 * @return
	 * @throws Exception
	 */
	public ProjectUsers validaUsuarioAsignado(Integer codigoUsuario,Integer codigoProyecto,Integer codigoPartner)throws DaoException{
		try{			
			String sql="";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			if(codigoPartner == null){
				sql="SELECT PU FROM ProjectUsers PU WHERE PU.users.userId = :codigoUsuario AND PU.projects.projId= :codigoProyecto";
				camposCondicion.put("codigoUsuario", codigoUsuario);
				camposCondicion.put("codigoProyecto", codigoProyecto);
			}else{
				sql="SELECT PU FROM ProjectUsers PU WHERE PU.users.userId = :codigoUsuario AND PU.projects.projId= :codigoProyecto AND PU.pspaId= :codigoPartner";
				camposCondicion.put("codigoUsuario", codigoUsuario);
				camposCondicion.put("codigoProyecto", codigoProyecto);
				camposCondicion.put("codigoPartner", codigoPartner);
			}
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}

	}
}

