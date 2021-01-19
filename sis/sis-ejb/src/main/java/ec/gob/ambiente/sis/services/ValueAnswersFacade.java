package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.ValueAnswers;

@Stateless
public class ValueAnswersFacade extends AbstractFacade<ValueAnswers, Integer> implements Serializable {


	private static final long serialVersionUID = 1L;

	public ValueAnswersFacade() {
		super(ValueAnswers.class,Integer.class);
	}

}
