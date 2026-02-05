package net.ooder.editor.toolbox.file.service;

import net.ooder.common.JDSException;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.engine.*;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.util.page.TreePageUtil;
import net.ooder.web.APIConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequestMapping("/rad/file/")
@Controller
public class OODFolderService {

    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @RequestMapping("getFolders")
    @ResponseBody
    public TreeListResultModel<List<OODFileTree>> getFolders(String path, String packageName, String projectName) throws JDSException {
        List<Object> objs = new ArrayList<>();
        TreeListResultModel<List<OODFileTree>> result = new TreeListResultModel<>();
        try {
            ProjectVersion version = ESDFacrory.getAdminESDClient().getProjectVersionByName(projectName);
            EUPackage euPackage = version.getEUPackage(packageName);

            List<String> esdClassNameList = new ArrayList<>();
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
                        if (methodConfig != null && methodConfig.isModule() && !esdClassNameList.contains(methodConfig.getSourceClassName())) {
                            esdClassNameList.add(methodConfig.getSourceClassName());
                        } else {
                            objs.add(module);
                        }
                    } else {
                        objs.add(module);
                    }
                }

            }

            List<String> apiClassNames = new ArrayList<>();
            List<APIConfig> allConfigs = APIFactory.getInstance(ESDFacrory.getAdminESDClient().getSpace()).getAPIConfigByPackageName(packageName);
            for (APIConfig apiConfig : allConfigs) {
                String className = apiConfig.getClassName();
                if (!esdClassNameList.contains(className)) {
                    objs.add(apiConfig);
                } else {
                    apiClassNames.add(className);
                }
            }

            for (String esdClassName : apiClassNames) {
                ESDClass esdClass = BuildFactory.getInstance().getClassManager().getAggEntityByName(esdClassName, false);
                objs.add(esdClass);
            }

            result = TreePageUtil.getTreeList(objs, OODFileTree.class);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return result;

    }

    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }
}
