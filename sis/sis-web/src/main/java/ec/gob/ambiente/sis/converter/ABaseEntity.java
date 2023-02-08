/**
@autor proamazonia [Christian Báez]  2 feb. 2023

**/
package ec.gob.ambiente.sis.converter;

import java.io.Serializable;

public abstract class ABaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public abstract Long getIdentifier();
}