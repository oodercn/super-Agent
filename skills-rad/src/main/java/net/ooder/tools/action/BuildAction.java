package net.ooder.tools.action;

import net.ooder.annotation.Aggregation;
import net.ooder.annotation.AggregationType;
import net.ooder.annotation.Split;
import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.web.RemoteConnectionManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = {"/action/build/"})
@Aggregation(type = AggregationType.MENU,userSpace = UserSpace.SYS)
public class BuildAction {


    @RequestMapping(value = {"rebuildCustomModule"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME},   onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY)
    @CustomAnnotation(index = 0, caption = "编译", imageClass = "ri-currency-line")
    @ResponseBody
    public ResultModel<Boolean> rebuildCustomModule(String projectName) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            Map map = new HashMap();
            map.put("projectId", version.getProject().getId());
            DSMFactory.getInstance().compileProject(projectName);
            ESDFacrory.getAdminESDClient().buildCustomModule(projectName, null, null, map, chrome);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return resultModel;
    }

    @RequestMapping(value = {"javaBuild"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME},   onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY)
    @CustomAnnotation(index = 1, caption = "混合编译", imageClass = "ri-refresh-line")
    @ResponseBody
    public ResultModel<Boolean> javaBuild(String projectName, String packageName) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        try {

            if (packageName != null && !packageName.equals("")) {
                List names = Arrays.asList(new String[]{packageName});
                ESDFacrory.getAdminESDClient().delFile(names, projectName);
            }
            DSMFactory.getInstance().compileProject(projectName);
            ESDFacrory.getAdminESDClient().buildCustomModule(projectName, packageName, null, null, chrome);
            ESDFacrory.getInstance().reload();


        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) resultModel).setErrdes(e.getMessage());
        }
        return resultModel;
    }


    @RequestMapping(value = {"split2"})
    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split2() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }

    private void exportProject(String projectName, ChromeProxy chrome, boolean deploy, boolean download) {
        try {
            ESDFacrory.getAdminESDClient().exportProject(projectName, chrome, deploy, download);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            chrome.execScript("ood.free('export')");
        }

    }

    @RequestMapping(value = {"export"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME})
    @CustomAnnotation(index = 3, caption = "打包发布", imageClass = "ri-package-line")
    @ResponseBody
    public ResultModel<Boolean> export(String projectName) {
        ChromeProxy defaultChrome =  getCurrChromeDriver();
        if (projectName != null) {
            RemoteConnectionManager.getConntctionService(projectName).execute(new Runnable() {
                @Override
                public void run() {
                    exportProject(projectName, defaultChrome, false, false);
                }
            });
        }
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }


    @RequestMapping(value = {"download"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.SPA_PROJECTNAME})
    @CustomAnnotation(index = 3, caption = "鎵撳寘涓嬭浇", imageClass = "ri-download-line")
    @ResponseBody
    public ResultModel<Boolean> download(String projectName) {
        ChromeProxy defaultChrome =  getCurrChromeDriver();
        if (projectName != null) {
            RemoteConnectionManager.getConntctionService(projectName).execute(new Runnable() {
                @Override
                public void run() {
                    exportProject(projectName, defaultChrome, false, true);
                }
            });
        }
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }



    @Split
    @CustomAnnotation(index = 4)
    @ResponseBody
    public ResultModel<Boolean> split() {
        ResultModel resultModel = new ResultModel();
        return resultModel;
    }


    @RequestMapping(method = RequestMethod.POST, value = "Public")
    @CustomAnnotation(index = 5)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "鍙戝竷", imageClass = "ri-send-plane-line")
    public PublicAction getPublicAction() {
        return new PublicAction();
    }


    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome  = JDSActionContext.getActionContext().Par(ChromeProxy.class);
        return chrome;
    }



}
