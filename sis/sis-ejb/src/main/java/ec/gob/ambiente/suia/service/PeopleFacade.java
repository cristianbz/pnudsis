package ec.gob.ambiente.suia.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ec.gob.ambiente.sigma.model.People;
import ec.gob.ambiente.sis.dao.AbstractFacade;




@Stateless
public class PeopleFacade extends AbstractFacade<People, Integer> implements Serializable {

	private static final long serialVersionUID = 1164242243166182670L;

	public PeopleFacade() {
		super(People.class, Integer.class);		
	}
	
	public People findByPin(String peopPin) {

		try{
			Query query = getEntityManager().createQuery("select o from People o where o.peopPin = :peopPin order by o.peopId desc").setParameter("peopPin",peopPin);
			List<People> peopleList= (List<People>) query.getResultList();
			if(!peopleList.isEmpty())
				return peopleList.get(0);
			else return null;
		}catch(NoResultException e)
		{
			People people= findByUserName(peopPin);
			if(people!=null)
				people.setPeopPin(peopPin);
			return people;
		}
		
		
	}
	
	private People findByUserName(String userName) {

		try{
			Query query = getEntityManager().createQuery("select o from People o,User u where o.peopId=u.people.peopId and u.userName = :userName").setParameter("userName",userName);
			return (People) query.getSingleResult();			
		}catch(NoResultException e)
		{
			return null;
		}
		
		
	}
	
	
	

	public boolean save(People people)
	{
		boolean result = false;
		try
		{
			/*if(people.getPeopId() == null)
			{
				if(people.getNationality()==null)
					people.setNationality(new Nationality(18));
				create(people);
			}
			else
			{
				edit(people);
			}*/
			
			result = true;
		}
		catch(Exception ex)
		{
			result = false;
		}
		return result;
	}
	
	
}