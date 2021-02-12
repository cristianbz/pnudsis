package ec.gob.ambiente.sis.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ec.gob.ambiente.sis.model.Sectors;

@FacesConverter(value = "sectorsConverter", forClass = SectorsConverter.class)
public class SectorsConverter implements Converter {

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
			Sectors sectors = (Sectors) value;

            if (sectors.getSectName() != null) {
                component.getAttributes().put(sectors.getSectName(), value);
                return sectors.getSectName();
            }
        }
        return null;
	}

}
