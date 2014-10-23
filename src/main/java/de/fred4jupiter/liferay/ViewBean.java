package de.fred4jupiter.liferay;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import com.liferay.faces.util.portal.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;

@RequestScoped
@ManagedBean(name = "viewBean")
public class ViewBean {

	public String getPortletPref() {
		return getPortletPreferences().getValue(ConfigBean.PORTLET_PREF_KEY, "");
	}
	
	private PortletPreferences getPortletPreferences() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();

		return portletRequest.getPreferences();
	}
	
	/**
	 * Get currently logged in user.
	 * 
	 * @return
	 */
	private User getCurrentLiferayUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		ThemeDisplay themeDisplay = (ThemeDisplay)context.getExternalContext().getRequestMap().get(WebKeys.THEME_DISPLAY);
		return themeDisplay.getUser();
	}

}
