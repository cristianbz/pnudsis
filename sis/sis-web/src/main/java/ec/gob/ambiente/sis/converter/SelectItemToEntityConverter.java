/**
@autor proamazonia [Christian Báez]  2 feb. 2023

**/
package ec.gob.ambiente.sis.converter;

import java.util.List;
import java.util.function.Predicate;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
@FacesConverter(value = "SelectItemToEntityConverter")
public class SelectItemToEntityConverter implements Converter{
	 @Override
	    public Object getAsObject(FacesContext ctx, UIComponent comp, String value) {
	        Object o = null;
	        if (!(value == null || value.isEmpty())) {
	            o = this.getSelectedItemAsEntity(comp, value);
	        }
	        return o;
	    }

	    @Override
	    public String getAsString(FacesContext ctx, UIComponent comp, Object value) {
	        String s = "";
	        if (value != null) {
	            s = ((ABaseEntity) value).getIdentifier().toString();
	        }
	        return s;
	    }

	    private ABaseEntity getSelectedItemAsEntity(UIComponent comp, String value) {
	        ABaseEntity item = null;

	        List<ABaseEntity> selectItems = null;
	        for (UIComponent uic : comp.getChildren()) {
	            if (uic instanceof UISelectItems) {
	                Long itemId = Long.valueOf(value);
	                selectItems = (List<ABaseEntity>) ((UISelectItems) uic).getValue();

	                if (itemId != null && selectItems != null && !selectItems.isEmpty()) {
	                    Predicate<ABaseEntity> predicate = i -> i.getIdentifier().equals(itemId);
	                    item = selectItems.stream().filter(predicate).findFirst().orElse(null);
	                }
	            }
	        }

	        return item;
	    }
}

