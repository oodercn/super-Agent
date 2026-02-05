package net.ooder.editor.console.java.page;

import net.ooder.annotation.*;
import net.ooder.common.JDSException;
import net.ooder.common.logging.ChromeProxy;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.context.JDSActionContext;
import net.ooder.dsm.editor.action.UPLoadFile;
import net.ooder.dsm.editor.agg.JavaAggTree;
import net.ooder.dsm.editor.agg.menu.JavaAggImportMenu;
import net.ooder.dsm.editor.agg.menu.JavaAggNewMenu;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.MenuBarMenu;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomBeforInvoke;
import net.ooder.esd.annotation.event.CustomCallBack;
import net.ooder.esd.annotation.event.CustomOnExecueSuccess;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.menu.CustomMenuType;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.RequestPathEnum;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.CustomViewFactory;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.dsm.java.JavaPackage;
import net.ooder.esd.dsm.java.JavaPackageManager;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.tool.component.ModuleComponent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = {"/rad/file/java/context/page/"})
@MenuBarMenu(menuType = CustomMenuType.CONTEXTMENU, caption = "菜单")
@Aggregation(type = AggregationType.MENU, rootClass = JavaPagePackageMenu.class, userSpace = UserSpace.SYS)
public class JavaPagePackageMenu {

    @RequestMapping(method = RequestMethod.POST, value = "paste")
    @CustomAnnotation(imageClass = "ri-clipboard-line", index = 1, caption = "粘贴")
    @APIEventAnnotation(customRequestData = {RequestPathEnum.TREEVIEW, RequestPathEnum.STAGVAR, RequestPathEnum.CTX}, callback = CustomCallBack.TREERELOADNODE)
    public @ResponseBody
    TreeListResultModel<List<JavaAggTree>> paste(String sfilePath, String packageName, String domainId, String projectName) {
        TreeListResultModel<List<JavaAggTree>> result = new TreeListResultModel<List<JavaAggTree>>();
        try {

            DSMFactory dsmFactory = DSMFactory.getInstance();
            File desFile = new File(sfilePath);
            DomainInst domainInst = dsmFactory.getAggregationManager().getDomainInstById(domainId, projectName);
            JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(desFile, domainInst, null);
            DSMFactory.getInstance().getBuildFactory().copy(srcBean, packageName);
            result.setIds(Arrays.asList(new String[]{domainId + "|" + packageName}));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST, value = "reLoad")
    @CustomAnnotation(imageClass = "ri-refresh-line", index = 1, caption = "刷新")
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW, callback = CustomCallBack.TREERELOADNODE)
    public @ResponseBody
    TreeListResultModel<List<JavaAggTree>> reLoad(String packageName, String domainId, String filePath) {
        TreeListResultModel<List<JavaAggTree>> result = new TreeListResultModel<List<JavaAggTree>>();
        String id = domainId + "|" + packageName;
        result.setIds(Arrays.asList(new String[]{id}));
        return result;
    }


    @Split
    @CustomAnnotation(index = 2)
    @ResponseBody
    public ResultModel<Boolean> split2() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }

    @RequestMapping(value = "UploadFile")
    @APIEventAnnotation(autoRun = true)
    @DialogAnnotation(width = "450", height = "380", caption = "上传JAVA文件")
    @ModuleAnnotation
    @FormViewAnnotation
    @CustomAnnotation(caption = "上传", index = 3, imageClass = "ri-upload-line", tips = "上传")
    public @ResponseBody
    ResultModel<UPLoadFile> uploadFile(String domainId, String packageName) {
        ResultModel<UPLoadFile> resultModel = new ResultModel<UPLoadFile>();
        try {
            UPLoadFile upLoadFile = new UPLoadFile(domainId, packageName);
            resultModel.setData(upLoadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;
    }


    @RequestMapping(value = {"split"})
    @Split
    @CustomAnnotation(index = 4)
    @ResponseBody
    public ResultModel<Boolean> split6() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }


    @RequestMapping(method = RequestMethod.POST, value = "newApp")
    @CustomAnnotation(imageClass = "ri-add-line", index = 5)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "新建", imageClass = "ri-add-line", index = 5)
    public JavaAggNewMenu getJavaJarAction() {
        return new JavaAggNewMenu();
    }


    @RequestMapping(method = RequestMethod.POST, value = "importAgg")
    @CustomAnnotation(imageClass = "ri-add-line", index = 6)
    @MenuBarMenu(menuType = CustomMenuType.SUB, caption = "导入", imageClass = "ri-file-transfer-line", index = 5)
    public JavaAggImportMenu importAgg() {
        return new JavaAggImportMenu();
    }


    @Split
    @CustomAnnotation(index = 7)
    @ResponseBody
    public ResultModel<Boolean> split7() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }

    @MethodChinaName(cname = "编译")
    @CustomAnnotation(imageClass = "ri-refresh-line", index = 8)
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEGRIDROW, onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY, callback = CustomCallBack.TREERELOADNODE)
    public @ResponseBody
    TreeListResultModel<List<JavaAggTree>> dsmBuild(String packageName, String domainId, String projectName) {
        TreeListResultModel<List<JavaAggTree>> resultModel = new TreeListResultModel<List<JavaAggTree>>();
        ChromeProxy chrome = getCurrChromeDriver();
        try {
            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);
            JavaPackage javaPackage = JavaPackageManager.getInstance(projectName).getPackageByName(domainInst, packageName);
            List<JavaSrcBean> javaSrcBeans = javaPackage.listAllFile();
            DSMFactory.getInstance().dynViewCompile(javaSrcBeans, projectName, getCurrChromeDriver());
            DSMFactory.getInstance().reload();
            getCurrChromeDriver().printLog("编译完成！", true);
            resultModel.setIds(Arrays.asList(new String[]{packageName}));
        } catch (Exception e) {
            e.printStackTrace();
            resultModel = new ErrorListResultModel();
            chrome.printError(e.getMessage());
            ((ErrorListResultModel) resultModel).setErrdes(e.getMessage());
        }
        return resultModel;
    }


    @RequestMapping(value = {"javaBuild"}, method = {RequestMethod.POST})
    @APIEventAnnotation(customRequestData = {RequestPathEnum.TOPFORM, RequestPathEnum.CTX}, onExecuteSuccess = {CustomOnExecueSuccess.MESSAGE}
            , beforeInvoke = CustomBeforInvoke.BUSY)
    @CustomAnnotation(index = 9, caption = "混合编译", imageClass = "ri-exchange-line")
    @ResponseBody
    public ResultModel<Boolean> javaBuild(String domainId, String projectName, String packageName) {
        ResultModel resultModel = new ResultModel();
        ChromeProxy chrome = getCurrChromeDriver();
        try {

            DomainInst domainInst = DSMFactory.getInstance().getAggregationManager().getDomainInstById(domainId, projectName);

            JavaPackage javaPackage = JavaPackageManager.getInstance(projectName).getPackageByName(domainInst, packageName);
            List<JavaSrcBean> javaSrcBeans = javaPackage.listAllFile();

            List<MethodConfig> allViewConfigs = new ArrayList<>();
            for (JavaSrcBean javaSrcBean : javaSrcBeans) {
                ApiClassConfig esdClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(javaSrcBean.getClassName());
                List viewMethods = esdClassConfig.getAllMethods();
                allViewConfigs.addAll(viewMethods);
            }

            for (MethodConfig methodAPIBean : allViewConfigs) {
                if (methodAPIBean.isModule()) {
                    EUModule module = ESDFacrory.getAdminESDClient().getModule(methodAPIBean.getUrl(), domainInst.getProjectVersionName());
                    if (module != null) {
                        List<ModuleComponent> moduleComponents = module.getComponent().findComponents(ComponentType.MODULE, null);
                        for (ModuleComponent moduleComponent : moduleComponents) {
                            if (moduleComponent.getEuModule() != null) {
                                ESDFacrory.getAdminESDClient().delModule(moduleComponent.getEuModule());
                            }
                        }
                        ESDFacrory.getAdminESDClient().delModule(module);
                    }
                    CustomViewFactory.getInstance().buildView(methodAPIBean, domainInst.getProjectVersionName(), (Map<String, ?>) JDSActionContext.getActionContext().getContext(), false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            chrome.printError(e.getMessage());
            ((ErrorResultModel) resultModel).setErrdes(e.getMessage());
        }


        return resultModel;
    }

    @Split
    @CustomAnnotation(index = 10)
    @ResponseBody
    public ResultModel<Boolean> split8() {
        ResultModel<Boolean> result = new ResultModel<Boolean>();
        return result;
    }

    @MethodChinaName(cname = "删除")
    @RequestMapping(method = RequestMethod.POST, value = "delete")
    @CustomAnnotation(imageClass = "ri-delete-bin-line", index = 11)
    @APIEventAnnotation(customRequestData = RequestPathEnum.TREEVIEW, callback = CustomCallBack.TREERELOADNODE)
    public @ResponseBody
    TreeListResultModel<List<JavaAggTree>> delete(String domainId, String parentId, String filePath, String javaTempId, String projectName) {
        TreeListResultModel<List<JavaAggTree>> result = new TreeListResultModel<List<JavaAggTree>>();
        try {
            File desFile = new File(filePath);
            DSMFactory dsmFactory = DSMFactory.getInstance();
            DomainInst domainInst = dsmFactory.getAggregationManager().getDomainInstById(domainId, projectName);
            if (desFile.exists()) {
                if (desFile.isDirectory()) {
                    JavaPackage javaPackage = domainInst.getPackageByFile(desFile);
                    parentId = domainId + "|" + javaPackage.getParent().getPackageName();
                    JavaPackageManager.getInstance(projectName).deleteJavaPackage(javaPackage);
                } else {

                    JavaSrcBean srcBean = BuildFactory.getInstance().getTempManager().genJavaSrc(desFile, domainInst, javaTempId);
                    parentId = domainId + "|" + srcBean.getPackageName();
                    JavaPackageManager.getInstance(projectName).deleteJavaFile(srcBean);
                }
            }

            result.setIds(Arrays.asList(new String[]{parentId}));

        } catch (JDSException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ChromeProxy getCurrChromeDriver() {
        ChromeProxy chrome = JDSActionContext.getActionContext().Par("$currChromeDriver", ChromeProxy.class);
        return chrome;
    }


}
