/**
@autor proamazonia [Christian Báez]  16 jul. 2021

 **/
package ec.gob.ambiente.suia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.excepciones.DaoException;
import ec.gob.ambiente.suia.model.Users;
@Stateless
@LocalBean
public class UsersFacade extends AbstractFacade<Users, Integer>{
	public UsersFacade(){
		super(Users.class,Integer.class);
	}
	/**
	 * Validacion del usuario se envia como parametro un objeto usuario
	 * @param usuario
	 * @return
	 * @throws DaoException
	 */
	public Users validarUsuario(String usuario,String clave)throws DaoException{
		try{
			String sql="SELECT U FROM Users U WHERE U.userName=:usuario AND U.userPassword=:clave AND U.userStatus=TRUE";
			Map<String, Object> camposCondicion=new HashMap<String, Object>();
			camposCondicion.put("usuario", usuario);
			camposCondicion.put("clave", clave);
			return findByCreateQuerySingleResult(sql, camposCondicion);
		}catch(NoResultException e){
			return null;
		}catch(Exception e){
			throw new DaoException();
		}

	}
	
	public List<Users> listaUsuariosFiltrados(String filtro)throws Exception{
		String sql="SELECT U FROM Users U WHERE U.userName LIKE :filtro AND U.userStatus=TRUE";
		Map<String, Object> camposCondicion=new HashMap<String, Object>();
		camposCondicion.put("filtro","%"+filtro+"%");		
		return findByCreateQuery(sql, camposCondicion);
	}
}

