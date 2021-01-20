package ec.gob.ambiente.sis.services;

import java.io.Serializable;

import javax.ejb.Stateless;

import org.apache.log4j.Logger;

import ec.gob.ambiente.sis.dao.AbstractFacade;
import ec.gob.ambiente.sis.model.QuestionsAnswers;

@Stateless
public class QuestionsAnswersFacade extends AbstractFacade<QuestionsAnswers, Integer> implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(QuestionsAnswersFacade.class);

	public QuestionsAnswersFacade() {
		super(QuestionsAnswers.class,Integer.class);
	}

}
