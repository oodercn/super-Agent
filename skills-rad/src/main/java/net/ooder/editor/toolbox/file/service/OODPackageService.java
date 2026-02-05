package net.ooder.editor.toolbox.file.service;

import net.ooder.common.JDSException;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ResultModel;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.event.CustomFieldEvent;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ApiClassConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.*;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.util.page.TreePageUtil;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.web.APIConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/rad/file/")
@Controller
public class OODPackageService {

    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @RequestMapping("getEUPackages")
    @ResponseBody
    public TreeListResultModel<List<OODFileTree>> getEUPackages(String path, String packageName, String projectName) throws JDSException {
        List<Object> objs = new ArrayList<>();
        TreeListResultModel<List<OODFileTree>> result = new TreeListResultModel<>();
        try {
            Set<String> clsNames = new HashSet<>();
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            EUPackage euPackage = version.getEUPackage(packageName);
            Set<String> esdClassNameList = new HashSet<>();
            if (euPackage != null) {
                List<EUPackage> packages = euPackage.listChildren();
                for (EUPackage childPackage : packages) {
                    if (childPackage.getPackageName().startsWith(packageName) || packageName.startsWith(childPackage.getPackageName())) {
                        objs.add(childPackage);
                    }
                }


                Set<EUModule> moduleList = euPackage.listModules();
                for (EUModule module : moduleList) {
                    if (module.getRealClassName().equals(module.getClassName())) {
                        MethodConfig methodConfig = module.getComponent().getMethodAPIBean();
                        if (methodConfig != null && methodConfig.isModule()) {
                            esdClassNameList.add(methodConfig.getSourceClassName());
                        } else {
                            objs.add(module);
                            clsNames.add(module.getSimpleClassName() + ".cls");
                        }
                    } else {
                        objs.add(module);
                        clsNames.add(module.getSimpleClassName() + ".cls");
                    }
                }

            }
            List<FileInfo> fileInfos = euPackage.listFiles();
            for (FileInfo fileInfo : fileInfos) {

                if (!clsNames.contains(fileInfo.getName())) {
                    objs.add(fileInfo);
                }
            }

            List<APIConfig> allConfigs = APIFactory.getInstance(ESDFacrory.getAdminESDClient().getSpace()).getAPIConfigByPackageName(packageName);
            for (APIConfig apiConfig : allConfigs) {
                String className = apiConfig.getClassName();
                if (!esdClassNameList.contains(className)) {
                    ApiClassConfig apiClassConfig = DSMFactory.getInstance().getAggregationManager().getApiClassConfig(className);
                    if (apiClassConfig.getAllViewMethods().size() > 0) {
                        objs.add(apiConfig);
                    }
                }
            }

            for (String esdClassName : esdClassNameList) {
                ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(esdClassName, false);
                objs.add(esdClass);
            }
            result = TreePageUtil.getTreeList(objs, OODFileTree.class);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;

    }


    @RequestMapping(method = RequestMethod.POST, value = "uploadFile")
    @APIEventAnnotation(bindFieldEvent = CustomFieldEvent.UPLOAD)
    @CustomAnnotation(caption = "上传文件")
    public @ResponseBody
    ResultModel<OODFileTree> uploadFile(String domainId, String projectName, String packageName, @RequestParam("files") MultipartFile file) {
        ResultModel<OODFileTree> result = new ResultModel<OODFileTree>();
        try {
            ProjectVersion version = getClient().getProjectVersionByName(projectName);
            EUPackage euPackage = version.getEUPackage(packageName);
            Folder tfolder = euPackage.getFolder();
            try {
                FileInfo fileInfo = getClient().uploadFile(new MD5InputStream(file.getInputStream()), tfolder.getPath() + file.getOriginalFilename(), projectName);
                result.setData(new OODFileTree(euPackage, domainId, projectName));
            } catch (IOException e) {
                throw new JDSException(e);
            }
        } catch (JDSException e) {
            result = new ErrorResultModel();
            ((ErrorResultModel) result).setErrcode(JDSException.APPLICATIONNOTFOUNDERROR);
            ((ErrorResultModel) result).setErrdes(e.getMessage());
        }
        return result;
    }


    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }
}
