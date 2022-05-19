/**
@autor proamazonia [Christian Báez]  28 jul. 2021

**/
package ec.gob.ambiente.sis.converter;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ec.gob.ambiente.sis.bean.AdministracionBean;
import ec.gob.ambiente.sis.controller.AdministracionController;
import ec.gob.ambiente.sis.controller.SeguimientoSalvaguardaController;
import ec.gob.ambiente.sis.model.Sectors;
import ec.gob.ambiente.suia.model.Users;

@FacesConverter(value = "UsuarioConverter")
public class UsuarioConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null) {
			 ValueExpression vex = ctx.getApplication().getExpressionFactory()
                     .createValueExpression(ctx.getELContext(),
                             "#{administracionBean}", AdministracionBean.class);
			AdministracionBean adm = (AdministracionBean)vex.getValue(ctx.getELContext());
			Users usuario=new Users();
			for (Users u : adm.getListaUsuarios()) {
				if(u.getUserName().equals(value)){
					usuario = u;
					break;
				}
					
			}
            return usuario;
        }
		return null;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		if (value != null && value instanceof Sectors) {
			Users usuario = (Users) value;

            if (usuario.getUserName() != null) {
                component.getAttributes().put(usuario.getUserName(), value);
                return usuario.getUserName();
            }
        }
        return null;
	}

}

