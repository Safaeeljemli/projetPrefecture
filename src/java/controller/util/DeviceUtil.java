package controller.util;
/*<dependency>
    <groupId>net.sf.uadetector</groupId>
    <artifactId>uadetector-core</artifactId>
    <version>0.9.22</version>
</dependency>*/


import bean.Device;
import bean.User;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import net.sf.uadetector.ReadableUserAgent;
import net.sf.uadetector.UserAgentStringParser;
import net.sf.uadetector.service.UADetectorServiceFactory;

public class DeviceUtil {

    private static final DeviceUtil instance = new DeviceUtil();

    public static DeviceUtil getInstance() {
        return instance;
    }

    private DeviceUtil() {
    }

    private static ReadableUserAgent getUserAgent() {
        HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
        ReadableUserAgent agent = parser.parse(httpServletRequest.getHeader("User-Agent"));
        return agent;
    }

     public static Device getDevice(User user) {
        ReadableUserAgent ag = getUserAgent();
        Device device = new Device();
        device.setBrowser(ag.getFamily().getName());
        device.setOperatingSystem(ag.getOperatingSystem().getName());
        device.setDeviceCategorie(ag.getDeviceCategory().getName());
        device.setUser(user);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        String userAgent = externalContext.getRequestHeaderMap().get("User-Agent");
        return device;
    }

}
