package net.ooder.tools.action;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.web.RemoteConnectionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = {"/action/build/public/"})
@Aggregation(type=AggregationType.MENU,userSpace = UserSpace.SYS)
public class PublicAction {
    @RequestMapping(value = {"exportLocalServer"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 1, caption = "本地发布", imageClass = "ri-upload-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME})
    @ResponseBody
    public ResultModel<Boolean> exportLocalServer(String projectName, String serverId) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        if (projectName != null) {
            String finalLocalServerId = serverId;
            RemoteConnectionManager.getConntctionService(projectName).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ESDFacrory.getAdminESDClient().exportLocalServer(projectName, finalLocalServerId, chrome);
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return resultModel;
    }

    @RequestMapping(value = {"exportRemoteServer"}, method = {RequestMethod.POST})
    @CustomAnnotation(index = 2, caption = "远程发布", imageClass = "ri-cloud-upload-line")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME})
    @ResponseBody
    public ResultModel<Boolean> exportRemoteServer(String projectName, String serverId) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        String remoteServerId = serverId.toString();
        if (projectName != null) {
            RemoteConnectionManager.getConntctionService(projectName.toString()).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        ESDFacrory.getAdminESDClient().exportRemoteServer(projectName, remoteServerId, chrome);
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return resultModel;
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome  = JDSActionContext.getActionContext().Par(ChromeProxy.class);
        return chrome;
    }


}
