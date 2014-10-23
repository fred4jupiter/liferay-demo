package de.fred4jupiter.liferay;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.log4j.Logger;

import com.liferay.faces.util.portal.WebKeys;
import com.liferay.mail.service.MailServiceUtil;
import com.liferay.portal.kernel.mail.MailMessage;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;

@RequestScoped
@ManagedBean(name = "viewBean")
public class ViewBean {

	private static final Logger LOG = Logger.getLogger(ViewBean.class);

	public String getPortletPref() {
		return getPortletPreferences().getValue(ConfigBean.PORTLET_PREF_KEY, "");
	}

	private PortletPreferences getPortletPreferences() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PortletRequest portletRequest = (PortletRequest) externalContext.getRequest();

		return portletRequest.getPreferences();
	}
	
	public String getPetName() {
		return (String) getCurrentLiferayUser().getExpandoBridge().getAttribute("petname");
	}

	/**
	 * Get currently logged in user.
	 * 
	 * @return
	 */
	private User getCurrentLiferayUser() {
		FacesContext context = FacesContext.getCurrentInstance();
		ThemeDisplay themeDisplay = (ThemeDisplay) context.getExternalContext().getRequestMap().get(WebKeys.THEME_DISPLAY);
		return themeDisplay.getUser();
	}

	public void sendEmail() {
		try {
			InternetAddress from = new InternetAddress("dummy@demo.de");

			MailMessage message = new MailMessage();
			message.setFrom(from);
			message.setTo(new InternetAddress("recipient@demo.de"));

			message.setSubject("Some subject");
			message.setBody("Some text");

			MailServiceUtil.sendEmail(message);
		} catch (AddressException e) {
			LOG.error(e.getMessage(), e);
		}
	}

}
