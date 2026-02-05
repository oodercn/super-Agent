package net.ooder.editor.toolbox.file.service;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.editor.toolbox.file.UPLoadOODFile;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.ModuleAnnotation;
import net.ooder.esd.annotation.event.CustomTabsEvent;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.annotation.field.DialogAnnotation;
import net.ooder.esd.annotation.view.FormViewAnnotation;
import net.ooder.esd.annotation.view.TreeViewAnnotation;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUPackage;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.util.page.TreePageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/rad/file/")
@Controller
public class OODProjectService {

    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD, autoRun = true, bindTabsEvent = CustomTabsEvent.TABEDITOR)
    @RequestMapping("ProjectTrees")
    @TreeViewAnnotation
    @ModuleAnnotation(cache = false)
    @ResponseBody
    public TreeListResultModel<List<OODFileTree>> getProjectTrees(String projectName) {
        ProjectDefAccess[] sysDef = new ProjectDefAccess[]{ProjectDefAccess.admin, ProjectDefAccess.sys, ProjectDefAccess.DSM};
        List<Object> dataList = new ArrayList<>();
        List<DomainInst> domainInsts = new ArrayList<>();
        try {
            if (projectName == null) {
                projectName = DSMFactory.getInstance().getDefaultProjectName();
            }
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            ProjectDefAccess projectDefAccess = version.getProject().getProjectType();
            if (Arrays.asList(sysDef).contains(projectDefAccess)) {
                domainInsts = DSMFactory.getInstance().findDomainInst(projectName, UserSpace.SYS);
                PackageType[] packageTypes = projectDefAccess.getPackageTypes();
                List<EUPackage> packages = ESDFacrory.getAdminESDClient().getTopPackages(version.getVersionName());
                for (EUPackage euPackage : packages) {
                    PackagePathType packagePathType = euPackage.getPackagePathType();
                    if (packagePathType != null && packagePathType.getApiType() != null) {
                        if (Arrays.asList(packageTypes).contains(packagePathType.getApiType())) {
                            if (euPackage.getFolder().getFileList().size() > 0 || euPackage.getFolder().getChildrenList().size() > 0) {
                                dataList.add(euPackage);
                            }
                        } else if (Arrays.asList(packageTypes).contains(packagePathType)) {
                            dataList.add(euPackage);
                        }
                    }
                }
            } else {
                domainInsts = DSMFactory.getInstance().getAllUserDomainInst(projectName);
            }

            for (DomainInst domainInst : domainInsts) {
                dataList.add(domainInst);
            }


            EUPackage euPackage = ESDFacrory.getAdminESDClient().getPackageByPath(version.getVersionName(), "");
            if (euPackage != null) {
                List<EUPackage> childPackageList = euPackage.listChildren();
                for (EUPackage childPackage : childPackageList) {
                    boolean isSys = false;
                    for (UserSpace userSpace : UserSpace.values()) {
                        if (childPackage.getName().equals(userSpace.getType().toLowerCase())) {
                            isSys = true;
                        }
                    }
                    if (!isSys) {
                        dataList.add(childPackage);
                    }
                }
            }

        } catch (JDSException e) {
            e.printStackTrace();
        }
        return TreePageUtil.getTreeList(dataList, OODFileTree.class);

    }


    @RequestMapping(value = "UploadFile")
    @APIEventAnnotation(autoRun = true)
    @DialogAnnotation(width = "450", height = "380", caption = "上传文件")
    @ModuleAnnotation
    @FormViewAnnotation
    @CustomAnnotation(caption = "上传", index = 31, imageClass = "ri-file-line", tips = "上传")
    public @ResponseBody
    ResultModel<UPLoadOODFile> uploadFile(String domainId, String packageName) {
        ResultModel<UPLoadOODFile> resultModel = new ResultModel<UPLoadOODFile>();
        try {
            UPLoadOODFile upLoadFile = new UPLoadOODFile(domainId, packageName);
            resultModel.setData(upLoadFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultModel;
    }


    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }
}
