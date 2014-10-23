package de.fred4jupiter.liferay;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import com.liferay.faces.portal.context.LiferayFacesContext;

@RequestScoped
@ManagedBean(name = "configBean")
public class ConfigBean {

	public static final String PORTLET_PREF_KEY = "portletPref";

	private String portletPref;
	
	public ConfigBean() {
		portletPref = readPortletPreference(PORTLET_PREF_KEY, "<not found>");
	}

	private String readPortletPreference(String key, String defaultValue) {
		PortletPreferences preferences = getPortletPreferences();
		return preferences.getValue(key, defaultValue);
	}

	public void savePortletPreference() {
		PortletPreferences portletPreferences = LiferayFacesContext.getInstance().getPortletPreferences();
		try {
			portletPreferences.setValue(PORTLET_PREF_KEY, portletPref);
			portletPreferences.store();
		} catch (ReadOnlyException e) {
			throw new SavePortletPreferencesException(e.getMessage(), e);
		} catch (ValidatorException e) {
			throw new SavePortletPreferencesException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SavePortletPreferencesException(e.getMessage(), e);
		}
	}

	private PortletPreferences getPortletPreferences() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();

		return portletRequest.getPreferences();
	}

	public String getPortletPref() {
		return portletPref;
	}

	public void setPortletPref(String portletPref) {
		this.portletPref = portletPref;
	}
}
