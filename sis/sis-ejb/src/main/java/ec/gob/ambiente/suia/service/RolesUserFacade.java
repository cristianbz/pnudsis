package ec.gob.ambiente.suia.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import ec.gob.ambiente.sigma.model.User;
import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.suia.model.Role;
import ec.gob.ambiente.suia.model.RolesUser;
import ec.gob.ambiente.suia.model.Users;




@Stateless
@LocalBean
public class RolesUserFacade extends AbstractFacade <RolesUser, Integer> implements Serializable {

	private static final long serialVersionUID = 5978766190183771960L;

	public RolesUserFacade() {
		super(RolesUser.class, Integer.class);		
	}
	
	/**
	 * Buscar Roles por usuario
	 * @param userName
	 * @param roleName
	 * @return Devuelve null si no encuntra ningun registro
	 */
	public List<RolesUser> findByUserNameAndRoleName(String userName,String roleName)
	{
		try {
			TypedQuery<RolesUser> query = super.getEntityManager().createQuery(""
					+ "select o from RolesUser o where o.user.userStatus=true and o.user.userName = :userName and o.role.roleName like :role and o.rousStatus = true ", RolesUser.class);
			query.setParameter("userName", userName);
			query.setParameter("role", roleName);		
			List<RolesUser> result= (List<RolesUser>) query.getResultList();
			if(result.size()>0)
				return result;			
		} catch (NoResultException e) {
			return null;
		}
		
		return null;
	}
	
	/**
	 * Agregar un rol a un usuario
	 * @param user
	 * @param role
	 */
	public void save(Users user,Role role)
	{
		RolesUser ru=new RolesUser();
		ru.setRousStatus(true);
		ru.setUser(user);
		ru.setRole(role);		
		create(ru);
	}
	
	/**
	 * Buscar Usuarios por nombre de rol
	 * @param roleName
	 * @return Devuelve null si no encuntra ningun registro
	 */
	public List<User> findByRoleName(String roleName)
	{
		try {
			TypedQuery<User> query = super.getEntityManager().createQuery(""
					+ "select u from RolesUser o,User u where o.user.userId=u.userId and o.role.roleName like :role and o.rousStatus = true ", User.class);		
			query.setParameter("role", roleName);		
			List<User> result= (List<User>) query.getResultList();
			if(result.size()>0)
				return result;
		} catch (NoResultException e) {			
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	public User findUniqueByRoleName(String roleName)
	{
		List<User> listUsers=findByRoleName(roleName);
		if(listUsers!=null)
			return listUsers.get(0);
		return null;
	}
	
	/**
	 * Lista los roles de un usuario
	 * @param Usuario
	 * @return Devuelve null si no encuntra ningun registro
	 */
	@SuppressWarnings("unchecked")
    public List<RolesUser> listRoleByUser(Users usuario) throws Exception {
        List<RolesUser> result = null;
        try {
        	Query query = getEntityManager().createQuery(" SELECT ru FROM RolesUser ru, Role r WHERE ru.role.roleId=r.roleId and ru.users.userId = :userId");		
			query.setParameter("userId",usuario.getUserId());
			return (List<RolesUser>) query.getResultList();
        } catch (NoResultException e) {
        	e.printStackTrace();
			result = null;
		}
        return result;
    }

}