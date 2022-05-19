package ec.gob.ambiente.sis.converter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import ec.gob.ambiente.sis.model.Catalogs;

//@FacesConverter(value = "catalogsConverter", forClass = CatalogsConverter.class)
@ManagedBean(name="catalogsConverter")
@SessionScoped
public class CatalogsConverter implements Converter,Serializable {

//	@Override
//	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {		
//		 ValueExpression vex = ctx.getApplication().getExpressionFactory()
//	                        .createValueExpression(ctx.getELContext(),
//	                                "#{seguimientoSalvaguardaController}", SeguimientoSalvaguardaController.class);
//		 SeguimientoSalvaguardaController seguimientoSalvaguardaController = (SeguimientoSalvaguardaController)vex.getValue(ctx.getELContext());
//	        return seguimientoSalvaguardaController.getSeguimientoSalvaguardaBean().getCatalogoLeyesSeleccionado().get(0);
//		
//	}
//
//	@Override
//	public String getAsString(FacesContext ctx, UIComponent component, Object value) {
//		
//		if (value != null && value instanceof Catalogs) {
//			Catalogs catalogs = (Catalogs) value;
//
//            if (catalogs.getCataText1() != null) {
//                component.getAttributes().put(catalogs.getCataText1(), value);                
//                return catalogs.getCataText1();
//            }
//        }
//        return null;
//	}
	
	private static final long serialVersionUID = 1L;


	@Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        Catalogs ct=new Catalogs();
        try{
            if(!s.equals("null")){
            	Catalogs o=new Catalogs(Integer.valueOf(s));
            ct=o;
            }
          } catch (Exception e) {
        	  e.printStackTrace();
          }
          return ct;
      }


    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        String r = "";
        try {
            if (o instanceof Catalogs) {
            	Catalogs ct= (Catalogs)o;
                r = String.valueOf(ct.getCataId());
            }else if (o instanceof String) {
               r = (String) o;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return r;
        }

}
