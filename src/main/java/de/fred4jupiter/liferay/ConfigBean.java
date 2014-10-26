package de.fred4jupiter.liferay;

import java.io.IOException;

import javax.faces.application.FacesMessage;
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

	public String savePortletPreference() {
		PortletPreferences portletPreferences = LiferayFacesContext.getInstance().getPortletPreferences();
		try {
			portletPreferences.setValue(PORTLET_PREF_KEY, portletPref);
			portletPreferences.store();
			addInfoMessage("msg.saved.successfully");
		} catch (ReadOnlyException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (ValidatorException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return "edit";
	}

	private void addInfoMessage(String key, Object... args) {
		addMessage(FacesMessage.SEVERITY_INFO, key, args);
	}

	private void addMessage(FacesMessage.Severity severity, String key, Object... args) {
		String message = getMessage(key, args);
		FacesMessage facesMessage = new FacesMessage(severity, message, message);
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}

	private String getMessage(String key, Object... args) {
		return LiferayFacesContext.getInstance().getMessage(key, args);
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
