/**
@autor proamazonia [Christian Báez]  29 sep. 2021

**/
package ec.gob.ambiente.sis.converter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import ec.gob.ambiente.sigma.model.CatalogType;
import ec.gob.ambiente.sis.model.CatalogsType;

@ManagedBean(name="catalogTypeConverter")
@SessionScoped
public class CatalogTypeConverter implements Converter,Serializable {

	private static final long serialVersionUID = 1L;


	@Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        CatalogsType ct=new CatalogsType();
        try{
            if(!s.equals("null")){
            	CatalogsType o=new CatalogsType(Integer.valueOf(s));
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
            if (o instanceof CatalogType) {
            	CatalogType ct= (CatalogType)o;
                r = String.valueOf(ct.getCatyId());
            }else if (o instanceof String) {
               r = (String) o;
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return r;
        }
}