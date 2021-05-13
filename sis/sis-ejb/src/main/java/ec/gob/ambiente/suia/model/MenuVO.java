/**
@autor proamazonia [Christian Báez]  5 may. 2021

**/
package ec.gob.ambiente.suia.model;

import java.io.Serializable;

public class MenuVO implements Serializable{

	private static final long serialVersionUID = 3004111257549476329L;
	
	private Integer idMenu; 
    private String labelMenu;
    private String actionMenu;    
    private String urlMenu;
    private Integer idMenuParent;   
    private Integer idStatusUsuario;   
    private Integer idUser;   
    private Boolean endNode;   
    private Integer order;   
    private String icon;

    public MenuVO(Integer idMenu, String labelMenu, String actionMenu, String urlMenu, Integer idMenuParent, Integer idUser, Boolean endNode, Integer order, String icon) {
    	this.idMenu = idMenu; 
    	this.labelMenu = labelMenu;
    	this.actionMenu = actionMenu;    
    	this.urlMenu = urlMenu;
    	this.idMenuParent = idMenuParent;
    	this.idUser = idUser;   
    	this.endNode = endNode;   
    	this.order = order;   
    	this.icon = icon;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMenu != null ? idMenu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MenuVO)) {
            return false;
        }
        MenuVO other = (MenuVO) object;
        if ((this.idMenu == null && other.idMenu != null) || (this.idMenu != null && !this.idMenu.equals(other.idMenu))) {
            return false;
        }
        return true;
    }

	public Integer getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}

	public String getLabelMenu() {
		return labelMenu;
	}

	public void setLabelMenu(String labelMenu) {
		this.labelMenu = labelMenu;
	}

	public String getActionMenu() {
		return actionMenu;
	}

	public void setActionMenu(String actionMenu) {
		this.actionMenu = actionMenu;
	}

	public String getUrlMenu() {
		return urlMenu;
	}

	public void setUrlMenu(String urlMenu) {
		this.urlMenu = urlMenu;
	}

	public Integer getIdMenuParent() {
		return idMenuParent;
	}

	public void setIdMenuParent(Integer idMenuParent) {
		this.idMenuParent = idMenuParent;
	}

	public Integer getIdStatusUsuario() {
		return idStatusUsuario;
	}

	public void setIdStatusUsuario(Integer idStatusUsuario) {
		this.idStatusUsuario = idStatusUsuario;
	}

	public Integer getIdUser() {
		return idUser;
	}

	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}

	public Boolean getEndNode() {
		return endNode;
	}

	public void setEndNode(Boolean endNode) {
		this.endNode = endNode;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
