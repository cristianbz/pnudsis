package ec.gob.ambiente.sis.converter;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import ec.gob.ambiente.sis.controller.SeguimientoSalvaguardaController;
import ec.gob.ambiente.sis.model.Catalogs;

@FacesConverter(value = "catalogsConverter", forClass = CatalogsConverter.class)
public class CatalogsConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		
//		if (value != null) {
//		
//			return (Catalogs) component.getAttributes().get(value);
//            
//        }
//		return null;
		 ValueExpression vex = ctx.getApplication().getExpressionFactory()
	                        .createValueExpression(ctx.getELContext(),
	                                "#{seguimientoSalvaguardaController}", SeguimientoSalvaguardaController.class);

		 SeguimientoSalvaguardaController seguimientoSalvaguardaController = (SeguimientoSalvaguardaController)vex.getValue(ctx.getELContext());
	        return seguimientoSalvaguardaController.getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().get(0);//  beers.getBeer(Integer.valueOf(beerId));
		
	}

	@Override
	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
		
		if (value != null && value instanceof Catalogs) {
			Catalogs catalogs = (Catalogs) value;

            if (catalogs.getCataText1() != null) {
                component.getAttributes().put(catalogs.getCataText1(), value);                
                return catalogs.getCataText1();
            }
        }
        return null;
	}

}
