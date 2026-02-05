package net.ooder.tools;

import net.ooder.annotation.MethodChinaName;
import net.ooder.common.JDSException;
import net.ooder.common.database.metadata.MetadataFactory;
import net.ooder.common.database.metadata.TableInfo;
import net.ooder.common.util.StringUtility;
import net.ooder.config.ErrorListResultModel;
import net.ooder.config.ErrorResultModel;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.esd.dsm.DSMFactory;
import net.ooder.esd.dsm.gen.UIGenTools;
import net.ooder.esd.dsm.repository.database.proxy.DSMTableProxy;
import net.ooder.esd.engine.*;
import net.ooder.esd.engine.config.DataBaseConfig;
import net.ooder.esd.engine.enums.PackageType;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.manager.OODModuleFile;
import net.ooder.esd.manager.OODSyncFile;
import net.ooder.esd.manager.UIComponentNode;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.esd.manager.plugins.font.node.FontConfig;
import net.ooder.esd.manager.plugins.img.node.ImgConfig;
import net.ooder.esd.tool.component.APICallerComponent;
import net.ooder.esd.tool.component.Component;
import net.ooder.esd.tool.component.ModuleComponent;
import net.ooder.esd.tool.properties.APICallerProperties;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.VFSConstants;
import net.ooder.vfs.ct.CtVfsFactory;
import net.ooder.vfs.ct.CtVfsService;
import net.ooder.web.APIConfig;
import net.ooder.web.APIConfigFactory;
import net.ooder.web.RequestMethodBean;
import net.ooder.web.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Controller
@RequestMapping(value = {"/RAD/plugs/"})
@MethodChinaName(cname = "RAD工具应用")
public class PlugsService {


    @MethodChinaName(cname = "从模板添加文件")
    @RequestMapping(value = {"addFromTpl"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<OODModule> addFromTpl(String className, String tempPath, String projectName) {
        ResultModel<OODModule> result = new ResultModel<OODModule>();
        OODModule oodModule = new OODModule();
        List<OODFile> modules = oodModule.getFiles();
        try {
            FileInfo tempFile = getVfsClient().getFileByPath(tempPath);
            String json = this.getVfsClient().readFileAsString(tempFile.getPath(), VFSConstants.Default_Encoding).toString();
            this.getClient().saveModuleAsJson(projectName, className, json);

        } catch (JDSException e) {
            e.printStackTrace();
        }

        if (tempPath == null || tempPath.equals("")) {
            tempPath = "template/cn/index.js";
        }

        return result;
    }

    @MethodChinaName(cname = "获取所有OOD类")
    @RequestMapping(value = {"getAllClass"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODFile>> getAllClass(String projectName) {
        List<OODFile> classList = new ArrayList<OODFile>();
        ListResultModel<List<OODFile>> result = new ListResultModel<List<OODFile>>();
        Set<EUModule> modules = null;
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            modules = projectVersion.getAllModule();
            for (EUModule module : modules) {
                if (module != null) {
                    classList.add(new OODFile(module));
                }
            }
            result.setData(classList);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODFile>> errorResult = new ErrorListResultModel<List<OODFile>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "获取所有OOD类")
    @RequestMapping(value = {"getAllClassComponents"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODFile>> getAllClassComponents( String projectName) {
        List<OODFile> classList = new ArrayList<OODFile>();
        ListResultModel<List<OODFile>> result = new ListResultModel<List<OODFile>>();
        Set<EUModule> modules = null;
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            modules = projectVersion.getAllModule();
            for (EUModule module : modules) {
                if (module != null) {
                    classList.add(new OODFile(module));
                }
            }
            result.setData(classList);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODFile>> errorResult = new ErrorListResultModel<List<OODFile>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "字体类配置")
    @RequestMapping(value = {"getSelFont"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<FontConfig>> getSelFont(String projectName) {
        ListResultModel<List<FontConfig>> result = new ListResultModel<List<FontConfig>>();
        List<FontConfig> fontConfigs = new ArrayList<FontConfig>();
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            fontConfigs = projectVersion.getProject().getFonts();
            result.setData(fontConfigs);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<FontConfig>> errorResult = new ErrorListResultModel<List<FontConfig>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @MethodChinaName(cname = "图片类配置")
    @RequestMapping(value = {"getSelImg"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<ImgConfig>> getSelImg( String projectName) {
        ListResultModel<List<ImgConfig>> result = new ListResultModel<List<ImgConfig>>();
        List<ImgConfig> imgConfigs = new ArrayList<ImgConfig>();
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            imgConfigs = projectVersion.getProject().getImgs();
            result.setData(imgConfigs);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<ImgConfig>> errorResult = new ErrorListResultModel<List<ImgConfig>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "图片类配置")
    @RequestMapping(value = {"getSelImgByPath"}, method = {RequestMethod.POST})
    public @ResponseBody
    ResultModel<ImgConfig> getSelImgByPath(String projectName, String path, String imgConfigId) {
        ResultModel<ImgConfig> result = new ResultModel<ImgConfig>();
        ImgConfig imgConfig = null;
        try {
            if (path != null && !path.equals("")) {
                getClient().reLoadImageConfig();
                imgConfig = getClient().buildImgConfig(projectName, path);
                result.setData(imgConfig);
            } else if (imgConfigId != null && !imgConfigId.equals("")) {
                imgConfig = getClient().getImgConfig(projectName, imgConfigId);
                result.setData(imgConfig);
            }
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<ImgConfig> errorResult = new ErrorResultModel<ImgConfig>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }

    @MethodChinaName(cname = "获取工程内图片")
    @RequestMapping(value = {"getInnerImg"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ResultModel<ImgConfig> getInnerImg( String projectName, String path) {
        ResultModel<ImgConfig> result = new ResultModel<ImgConfig>();

        try {
            getClient().reLoadImageConfig();
            ImgConfig imgConfig = getClient().buildImgConfig(projectName, path);
            result.setData(imgConfig);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorResultModel<ImgConfig> errorResult = new ErrorResultModel<ImgConfig>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }
        return result;
    }


    @RequestMapping(value = {"API"}, method = {RequestMethod.GET})
    public ResultModel<Boolean> api(HttpServletResponse response, String projectName) {
        InputStream stream = null;
        FileInfo fileInfo = null;
        try {
            fileInfo = this.getVfsClient().getFileByPath("root/RAD/API/index.html");
            if (fileInfo != null) {
                stream = fileInfo.getCurrentVersonInputStream();
            }
            OutputStream os = response.getOutputStream();
            // 循环写入输出流
            byte[] b = new byte[4096];
            int length;
            while ((length = stream.read(b)) > 0) {
                os.write(b, 0, length);

            }
            try {
                os.close();
                stream.close();
            } catch (final IOException ioe) {
            }

        } catch (JDSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultModel<>();

    }

    @MethodChinaName(cname = "获取扩展模块库")
    @RequestMapping(value = {"getExtModuleProjectTree"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODFile>> getExtModuleProjectTree(String projectName) {
        ListResultModel<List<OODFile>> result = new ListResultModel<List<OODFile>>();
        List<OODFile> oodFiles = new ArrayList<OODFile>();
        List<Folder> folders = new ArrayList<Folder>();
        try {
            List<Project> projects = this.getClient().getAllProject(ProjectDefAccess.Module);
            for (Project project : projects) {
                Set<EUModule> modules = null;
                ProjectVersion projectVersion = project.getActiveProjectVersion();
                try {
                    modules = projectVersion.getAllModule();
                } catch (JDSException e) {
                    e.printStackTrace();
                }

                if (modules != null && modules.size() > 0) {
                    OODFile oodFile = new OODFile(projectVersion);
                    oodFile.setCaption(projectVersion.getProject().getDesc() == null ? projectVersion.getProject().getProjectName() : projectVersion.getProject().getDesc());
                    oodFiles.add(oodFile);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODFile>> errorResult = new ErrorListResultModel<List<OODFile>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }
        result.setData(oodFiles);
        return result;
    }


    @MethodChinaName(cname = "获取扩展组件库")
    @RequestMapping(value = {"getExtComProjectTree"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODFile>> getExtComProjectTree( String projectName) {
        ListResultModel<List<OODFile>> result = new ListResultModel<List<OODFile>>();
        List<OODFile> oodFiles = new ArrayList<OODFile>();
        List<Folder> folders = new ArrayList<Folder>();
        try {
            List<Project> projects = this.getClient().getAllProject(ProjectDefAccess.Component);
            for (Project project : projects) {
                Set<EUModule> modules = null;
                ProjectVersion projectVersion = project.getActiveProjectVersion();
                try {
                    modules = projectVersion.getAllModule();
                } catch (JDSException e) {
                    e.printStackTrace();
                }

                if (modules != null && modules.size() > 0) {
                    OODFile oodFile = new OODFile(projectVersion);
                    oodFile.setCaption(projectVersion.getProject().getDesc() == null ? projectVersion.getProject().getProjectName() : projectVersion.getProject().getDesc());
                    oodFiles.add(oodFile);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODFile>> errorResult = new ErrorListResultModel<List<OODFile>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }
        result.setData(oodFiles);
        return result;
    }


    @MethodChinaName(cname = "获取所有工程")
    @RequestMapping(value = {"getAllProjectTree"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODFile>> getAllProjectTree(String projectName, String tpath) {
        ListResultModel<List<OODFile>> result = new ListResultModel<List<OODFile>>();
        List<OODFile> oodFiles = new ArrayList<OODFile>();
        List<Folder> folders = new ArrayList<Folder>();
        try {

            Project sproject = this.getClient().getProjectByName(projectName);
            ProjectDefAccess type = sproject.getProjectType();
//            if (tpath != null && tpath.startsWith("Module/")) {
//                type = ProjectDefAccess.Module;
//            }

            List<Project> projects = this.getClient().getAllProject(type);

            for (Project project : projects) {
                Set<EUModule> modules = null;
                ProjectVersion projectVersion = project.getActiveProjectVersion();
                try {
                    modules = projectVersion.getAllModule();
                } catch (JDSException e) {
                    e.printStackTrace();
                }

                if (modules != null && modules.size() > 0) {
                    OODFile oodFile = new OODFile(projectVersion);
                    oodFile.setCaption(projectVersion.getProject().getDesc() == null ? projectVersion.getProject().getProjectName() : projectVersion.getProject().getDesc());
                    oodFile.setSub(new ArrayList<>());
                    oodFiles.add(oodFile);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODFile>> errorResult = new ErrorListResultModel<List<OODFile>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }
        result.setData(oodFiles);
        return result;
    }


    @MethodChinaName(cname = "获取所有包")
    @RequestMapping(value = {"getAllClasses"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODSyncFile>> getAllClasses(String projectName, String className) {
        ListResultModel<List<OODSyncFile>> result = new ListResultModel<List<OODSyncFile>>();
        List<OODSyncFile> oodFiles = new ArrayList<OODSyncFile>();
        List<Folder> folders = new ArrayList<Folder>();
        Map<String, OODSyncFile> folderMap = new HashMap<String, OODSyncFile>();

        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            EUModule euModule = projectVersion.getModule(className);
            EUPackage euPackage = projectVersion.getEUPackage(euModule.getPackageName());
            List<EUModule> allModules = euPackage.listAllModule();
            List<String> modules = euModule.getComponent().getRequired();
            for (String moduleName : modules) {
                EUModule module = ESDFacrory.getAdminESDClient().getModule(moduleName, projectName);
                if (!allModules.contains(module)) {
                    allModules.add(module);
                }
            }

            for (EUModule module : allModules) {
                if (module != null && (className == null || !className.equals(module.getClassName()))) {
                    try {
                        Folder folder = getClient().getFileByPath(module.getPath(), projectName).getFolder();
                        OODSyncFile oodSyncFile = folderMap.get(folder.getPath());
                        if (oodSyncFile == null) {
                            oodSyncFile = new OODSyncFile(folder, projectVersion);
                            folderMap.put(folder.getPath(), oodSyncFile);
                            oodFiles.add(oodSyncFile);
                        }
                        OODModuleFile moduleFile = new OODModuleFile(module);
                        moduleFile.setType("");
                        moduleFile.setCaption(module.getName());
                        oodSyncFile.getSub().add(moduleFile);
                    } catch (JDSException e) {
                        e.printStackTrace();
                    }
                }
            }


            Collections.sort(oodFiles, new Comparator<OODSyncFile>() {
                public int compare(OODSyncFile o1, OODSyncFile o2) {
                    return o1.getClassName().compareTo(o2.getClassName());
                }
            });


            result.setData(oodFiles);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODSyncFile>> errorResult = new ErrorListResultModel<List<OODSyncFile>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }

        return result;
    }

    @MethodChinaName(cname = "获取所有包")
    @RequestMapping(value = {"getAllPackages"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<OODFile>> getAllPackages(String projectName, String type, String pattern) {
        ListResultModel<List<OODFile>> result = new ListResultModel<List<OODFile>>();
        List<OODFile> oodFiles = new ArrayList<OODFile>();
        List<Folder> folders = new ArrayList<Folder>();
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            List<EUPackage> packages = getClient().getAllPackage(projectName, PackageType.userdef);

            for (EUPackage euPackage : packages) {
                if (euPackage.findModules(type, pattern).size() > 0) {
                    oodFiles.add(new OODFile(euPackage));
                }
            }
            Collections.sort(oodFiles, new Comparator<OODFile>() {
                public int compare(OODFile o1, OODFile o2) {
                    return o1.getClassName().compareTo(o2.getClassName());
                }
            });
            result.setData(oodFiles);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<OODFile>> errorResult = new ErrorListResultModel<List<OODFile>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }

        return result;
    }


    @MethodChinaName(cname = "获取所有包")
    @RequestMapping(value = {"getAPIPackages"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<APIPaths>> getAPIPackages(String projectName) {
        ListResultModel<List<APIPaths>> result = new ListResultModel<List<APIPaths>>();
        List<APIPaths> oodFiles = new ArrayList<APIPaths>();
        List<Folder> folders = new ArrayList<Folder>();
        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            List<APIConfig> apiConfigs = getClient().getAPIConfigByProject(projectName);

            for (APIConfig apiConfig : apiConfigs) {
                oodFiles.add(new APIPaths(APIFactory.LoclHostName, apiConfig));
            }
            Collections.sort(oodFiles, new Comparator<APIPaths>() {
                public int compare(APIPaths o1, APIPaths o2) {
                    return o1.getPath().compareTo(o2.getPath());
                }
            });
            result.setData(oodFiles);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<APIPaths>> errorResult = new ErrorListResultModel<List<APIPaths>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }

        return result;
    }

    @MethodChinaName(cname = "指定包下组件")
    @RequestMapping(value = {"getAllComponentsByClass"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<Object>> getAllComponentsByClass(String projectName, String className, String type, String pattern, Boolean draggable) {
        if (draggable == null) {
            draggable = true;
        }
        ListResultModel<List<Object>> result = new ListResultModel<List<Object>>();
        List<Object> modules = new ArrayList<Object>();

        try {
            EUModule module = this.getClient().getModule(className, projectName);
            ModuleComponent component = module.getComponent();
            if (component != null) {
                for (Object subcomponent : component.findComponents(type, pattern)) {
                    modules.add(new UIComponentNode((Component) subcomponent, draggable));
                }
            }
            result.setData(modules);

        } catch (Exception e) {
            e.printStackTrace();
            ErrorListResultModel<List<Object>> errorResult = new ErrorListResultModel<List<Object>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }

        return result;
    }


    @MethodChinaName(cname = "指定包下组件")
    @RequestMapping(value = {"getAllComponentsByPath"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<Object>> getAllComponentsByPath(String projectName, String path, String className, String type, String pattern, Boolean draggable) {
        if (draggable == null) {
            draggable = true;
        }
        ListResultModel<List<Object>> result = new ListResultModel<List<Object>>();
        List<Object> modules = new ArrayList<Object>();

        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            Folder folder = getClient().getFolderByPath(path, projectName);
            if (folder == null) {
                folder = getClient().createFolder(path, projectName);

            }
            Set<EUModule> allModules = projectVersion.getAllModule();
            for (EUModule module : allModules) {
                if (module != null) {
                    Folder mfolder = getClient().getFileByPath(module.getPath(), projectName).getFolder();
                    if (mfolder.getPath().equals(folder.getPath())) {
                        ModuleComponent component = module.getComponent();
                        if (component != null && component.findComponents(type, pattern) != null && component.findComponents(type, pattern).size() > 0) {
                            UIComponentNode node = new UIComponentNode(component, draggable);
                            // node.setCaption(component.getDesc()==null ?component.getClassName());
                            modules.add(node);
                        }
                    }
                }
            }


            result.setData(modules);

        } catch (Exception e) {
            e.printStackTrace();
            ErrorListResultModel<List<Object>> errorResult = new ErrorListResultModel<List<Object>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }

        return result;
    }

    @MethodChinaName(cname = "检索关联动作")
    @RequestMapping(value = {"getComponents"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<Object>> getComponents(String projectName) {
        ListResultModel<List<Object>> result = new ListResultModel<List<Object>>();
        List<Object> modules = new ArrayList<Object>();

        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            Set<EUModule> allModules = projectVersion.getAllModule();
            for (EUModule module : allModules) {
                if (module != null) {
                    ModuleComponent component = module.getComponent();
                    if (component != null && component.getChildren() != null && component.getChildren().size() > 0) {
                        modules.add(new UIComponentNode(component, true));
                    }
                }
            }
            result.setData(modules);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<Object>> errorResult = new ErrorListResultModel<List<Object>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }

        return result;
    }

    @MethodChinaName(cname = "获取扩展应用定义")
    @RequestMapping(value = {"getExtModules"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<Object>> getExtModules( String projectName) {
        ListResultModel<List<Object>> result = new ListResultModel<List<Object>>();
        List<Object> modules = new ArrayList<Object>();

        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            Set<EUModule> allModules = projectVersion.getAllModule();
            for (EUModule module : allModules) {
                if (module != null) {
                    ModuleComponent component = module.getComponent();
                    if (component != null && component.getChildren() != null && component.getChildren().size() > 0) {
                        modules.add(new UIComponentNode(component, true));
                    }
                }
            }
            result.setData(modules);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<Object>> errorResult = new ErrorListResultModel<List<Object>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }

        return result;
    }


    @MethodChinaName(cname = "获取扩展组件定义")
    @RequestMapping(value = {"getExtComs"}, method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody
    ListResultModel<List<Object>> getExtComs(String projectName) {
        ListResultModel<List<Object>> result = new ListResultModel<List<Object>>();
        List<Object> modules = new ArrayList<Object>();

        try {
            ProjectVersion projectVersion = getClient().getProjectVersionByName(projectName);
            Set<EUModule> allModules = projectVersion.getAllModule();
            for (EUModule module : allModules) {
                if (module != null) {
                    ModuleComponent component = module.getComponent();
                    if (component != null && component.getChildren() != null && component.getChildren().size() > 0) {
                        modules.add(new UIComponentNode(component, true));
                    }
                }
            }
            result.setData(modules);
        } catch (JDSException e) {
            e.printStackTrace();
            ErrorListResultModel<List<Object>> errorResult = new ErrorListResultModel<List<Object>>();
            errorResult.setErrdes(e.getMessage());

            result = errorResult;
        }

        return result;
    }


    @MethodChinaName(cname = "获取所有数据库表")
    @RequestMapping(method = RequestMethod.POST, value = "fdt/GetAllTableByName")
    @ResponseBody
    public ListResultModel<List<TableInfo>> getAllTableByName(String simpleName, String projectName) {
        ListResultModel<List<TableInfo>> result = new ListResultModel<List<TableInfo>>();
        List<TableInfo> tables = new ArrayList<TableInfo>();
        try {
            ProjectVersion projectVersion = this.getClient().getProjectVersionByName(projectName);
            List<DataBaseConfig> configs = projectVersion.getProject().getConfig().getDbConfigs();

            for (DataBaseConfig config : configs) {
                try {
                    if (config.getConfigKey() != null && this.getClient().getDbFactory(config.getConfigKey()) != null) {
                        MetadataFactory factory = getClient().getDbFactory(config.getConfigKey());
                        List<TableInfo> alltables = factory.getTableInfos(simpleName);
                        List<String> tableNames = config.getTableName();
                        for (TableInfo table : alltables) {
                            if (tableNames == null || tableNames.contains(table.getName())) {
                                tables.add(table);
                            }
                        }
                        result = PageUtil.getDefaultPageList(tables);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        } catch (JDSException e) {
            ErrorListResultModel<List<TableInfo>> errorResult = new ErrorListResultModel<List<TableInfo>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }

        return result;
    }


    @MethodChinaName(cname = "获取数据库表组件")
    @RequestMapping(value = "GetTableComponents", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    ResultModel<List<UIComponentNode>> getTableComponents(String tableName, String configKey, String projectName, String repositoryId) {
        ResultModel<List<UIComponentNode>> result = new ResultModel<List<UIComponentNode>>();
        List<UIComponentNode> componentNodes = new ArrayList<UIComponentNode>();
        try {
            ProjectVersion version = this.getClient().getProjectVersionByName(projectName);
            UIGenTools tools = new UIGenTools(version, configKey);
            MetadataFactory metadataFactory = this.getClient().getDbFactory(configKey);
            DSMTableProxy proxy = DSMFactory.getInstance().getRepositoryManager().getTableProxyByName(tableName, repositoryId);
            EUModule formModule = tools.genTableFormModule(tableName, null, repositoryId);

            EUModule gridLayoutModule = tools.genTableGridModule(tableName, null, repositoryId);

            APIConfig config = APIConfigFactory.getInstance().getAPIConfig("net.ooder.fdt.server.service.DAOFromService");
            List<RequestMethodBean> methods = config.getMethods();

            for (RequestMethodBean methodBean : methods) {
                methodBean.setUrl(StringUtility.replace(methodBean.getUrl(), "{connfigKey}", configKey));
                methodBean.setUrl(StringUtility.replace(methodBean.getUrl(), "{className}", proxy.getClassName()));
                APICallerProperties properties = new APICallerProperties(methodBean);
                UIComponentNode apiComponent = new UIComponentNode(new APICallerComponent(methodBean.getName(), properties), true);
                componentNodes.add(apiComponent);
            }
            componentNodes.add(new UIComponentNode(formModule.getComponent(), true));
            componentNodes.add(new UIComponentNode(gridLayoutModule.getComponent(), true));
            result.setData(componentNodes);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorResultModel<List<UIComponentNode>> errorResult = new ErrorResultModel<List<UIComponentNode>>();
            errorResult.setErrdes(e.getMessage());
            result = errorResult;
        }

        return result;
    }

    public ESDClient getClient() throws JDSException {

        ESDClient client = ESDFacrory.getAdminESDClient();

        return client;
    }

    public CtVfsService getVfsClient() {

        CtVfsService vfsClient = CtVfsFactory.getCtVfsService();
        return vfsClient;
    }
}



