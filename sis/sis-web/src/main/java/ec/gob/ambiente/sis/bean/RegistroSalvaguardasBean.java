/**
@autor proamazonia [Christian Báez]  3 ago. 2021

**/
package ec.gob.ambiente.sis.bean;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import lombok.Getter;
import lombok.Setter;

@Named
@ViewScoped
public class RegistroSalvaguardasBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Getter
	@Setter
	private boolean esRegistroSalvaguardas;

}

