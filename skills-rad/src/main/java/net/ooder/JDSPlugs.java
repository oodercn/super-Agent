package net.ooder;

import net.ooder.client.JDSSessionFactory;
import net.ooder.common.JDSException;
import net.ooder.config.JDSConfig;
import net.ooder.config.UserBean;
import net.ooder.context.JDSActionContext;
import net.ooder.engine.JDSSessionHandle;
import net.ooder.esb.util.EsbFactory;
import net.ooder.jds.core.User;
import net.ooder.server.JDSServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component("JDSPlugs")
public class JDSPlugs implements
        ApplicationListener<ContextRefreshedEvent> {
    public static final String PRODUCT_NAME = "ESD EditorTools";

    public static final String PRODUCT_VERSION = "V1.0b";

    public static final String PRODUCT_COPYRIGHT = "Copyright(c)2004 - 2026 ooder.net, All Rights Reserved";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent ev) {
        if (ev.getApplicationContext().getParent() == null) {
            System.out.println("************************************************");
            try {
                System.out.println("-------- JDSHome=" + JDSConfig.getServerHome() + " Initialization ---------");
                System.out.println("************************************************");
                System.out.println("----- 欢迎使用 JDS ESD 代理服务器");
                System.out.println(PRODUCT_NAME + " - " + PRODUCT_VERSION);
                System.out.println(PRODUCT_COPYRIGHT);
                System.out.println("************************************************");
                System.out.println("-------- ESDTools Initialization ---------");
                System.out.println("************************************************");
                System.out.println("- Start Connect Server - " + UserBean.getInstance().getServerUrl() + "*");
                System.out.println("- Connent JDSServer UserName    [" + UserBean.getInstance().getUsername() + "]*");
                login(UserBean.getInstance());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void login(UserBean bean) throws JDSException {
        User user = new User();
        user.setSessionId(UserBean.getInstance().getUsername());
        user.setAccount(UserBean.getInstance().getUsername());
        user.setName(UserBean.getInstance().getUsername());
        user.setId(UserBean.getInstance().getUsername());
        JDSServer.getClusterClient().getUDPClient().setUser(user);
        JDSSessionFactory factory = new JDSSessionFactory(JDSActionContext.getActionContext());
        JDSSessionHandle handle = factory.createSessionHandle();
        JDSServer.getInstance().newJDSClientService(handle, JDSConfig.getConfigName());
        EsbFactory.initBus();
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://127.0.0.1:" + JDSConfig.getValue("server.port").trim() + "/OOD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
