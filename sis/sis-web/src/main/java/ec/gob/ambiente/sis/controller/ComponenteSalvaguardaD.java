/**
@autor proamazonia [Christian Báez]  12 mar. 2021

**/
package ec.gob.ambiente.sis.controller;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import ec.gob.ambiente.sis.bean.SalvaguardaDBean;
import lombok.Getter;

@Dependent
public class ComponenteSalvaguardaD implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	@Getter
	private SalvaguardaDBean salvaguardaDBean;
}

