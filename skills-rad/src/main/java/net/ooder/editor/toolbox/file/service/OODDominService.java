package net.ooder.editor.toolbox.file.service;

import net.ooder.common.JDSException;
import net.ooder.config.TreeListResultModel;
import net.ooder.editor.toolbox.file.OODFileTree;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.esd.bean.MethodConfig;
import net.ooder.esd.custom.ESDClass;
import net.ooder.esd.dsm.BuildFactory;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.EUPackage;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.util.page.TreePageUtil;
import net.ooder.web.APIConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/rad/file/")
public class OODDominService {

    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.RELOADCHILD)
    @RequestMapping("getDomainInsts")
    @ResponseBody
    public TreeListResultModel<List<OODFileTree>> geDomainInsts(String packageName, String projectName) throws JDSException {
        if (projectName == null) {
            projectName = DSMFactory.getInstance().getDefaultProjectName();
        }
        List<Object> objs = new ArrayList<>();
        TreeListResultModel<List<OODFileTree>> result = new TreeListResultModel<>();
        try {
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

            List<APIConfig> allConfigs = APIFactory.getInstance(ESDFacrory.getAdminESDClient().getSpace()).getAPIConfigByPackageName(packageName);
            for (APIConfig apiConfig : allConfigs) {
                String className = apiConfig.getClassName();
                if (!esdClassNameList.contains(className)) {
                    objs.add(apiConfig);
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

}
