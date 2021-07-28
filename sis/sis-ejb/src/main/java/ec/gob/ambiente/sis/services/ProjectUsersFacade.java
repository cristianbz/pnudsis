/**
@autor proamazonia [Christian Báez]  19 jul. 2021

**/
package ec.gob.ambiente.sis.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
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
		String sql="SELECT PU FROM ProjectUsers PU WHERE PU.prusStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();		
		return findByCreateQuery(sql, camposCondicion);
	}
}

