package ec.gob.ambiente.sis.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ec.gob.ambiente.sis.model.Catalogs;
import ec.gob.ambiente.sis.model.Sectors;

@FacesConverter(value = "catalogsConverter", forClass = CatalogsConverter.class)
public class CatalogsConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null) {
            return component.getAttributes().get(value);
        }
		return null;
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		if (value != null && value instanceof Sectors) {
			Catalogs catalogs = (Catalogs) value;

            if (catalogs.getCataText1() != null) {
                component.getAttributes().put(catalogs.getCataText1(), value);
                return catalogs.getCataText1();
            }
        }
        return null;
	}

}
