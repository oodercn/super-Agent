package net.ooder.tools;

import net.ooder.annotation.UserSpace;
import net.ooder.common.JDSException;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.annotation.ui.FileImgCssType;
import net.ooder.esd.annotation.ui.IconColorEnum;
import net.ooder.esd.dsm.aggregation.DomainInst;
import net.ooder.esd.engine.EUModule;
import net.ooder.esd.engine.EUPackage;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.enums.PackagePathType;
import net.ooder.esd.manager.plugins.api.APIFactory;
import net.ooder.esd.manager.plugins.api.node.APIPaths;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.FileVersion;
import net.ooder.vfs.Folder;
import net.ooder.web.APIConfig;

import java.util.ArrayList;
import java.util.List;

;

public class OODFile implements Comparable<OODFile> {

    String name;
    String id;
    Integer type;
    String location;
    String className;
    String packageName;
    String imageClass;
    String projectName;
    String caption;
    String path;
    Boolean group=false;
    public String iconColor;
    public String itemColor;
    public String fontColor;
    List<OODFile> sub;
    public Boolean initFold;


    public OODFile(ProjectVersion version) {
        this.projectName = version.getProject().getProjectName();
        this.name = projectName;
        this.location = version.getPath();
        this.id = location;
        this.iconColor = IconColorEnum.DARKBLUE.getName();
        this.imageClass = "ri-cube-line";
        this.type = 0;
        this.caption = name;
        initFold = true;
        path = "/";
    }

    public OODFile(EUPackage euPackage) {
        this.name = euPackage.getName();
        this.initFold = true;
        this.location = euPackage.getPath();
        String curProjectPath = euPackage.getProjectVersion().getPath();
        if (curProjectPath != null && !curProjectPath.equals("") && location.startsWith(curProjectPath)) {
            location = location.substring(curProjectPath.length());
        }
        this.className = euPackage.getPackageName();
        this.packageName = euPackage.getPackageName();
        this.sub = new ArrayList<>();

        PackagePathType pathType = PackagePathType.bpm;
        if (euPackage.getPackagePathType() != null) {
            pathType = euPackage.getPackagePathType();
        }

        this.iconColor = pathType.getApiType().getIconColorEnum().getName();
        this.id = euPackage.getId();
        this.imageClass = euPackage.getImageClass();
        this.type = 1;
        this.caption = euPackage.getDesc();
        this.projectName = euPackage.getProjectVersion().getProject().getProjectName();
    }

    public OODFile(EUModule module) {
        this.name = module.getName() + ".cls";
        this.initFold = true;
        this.location = module.getPath();
        String curProjectPath = module.getProjectVersion().getPath();
        if (curProjectPath != null && !curProjectPath.equals("") && location.startsWith(curProjectPath)) {
            location = location.substring(curProjectPath.length());
        }
        this.className = module.getClassName();
        this.id = module.getPath();
        this.imageClass = "ri-file-code-line";
        this.iconColor = IconColorEnum.YELLOW.getName();
        this.type = 1;
        this.caption = className;
        String title = module.getComponent().getTitle();
        if (title != null && !title.equals("")) {
            this.caption = caption + "(" + title + ")";
        }
        this.projectName = module.getProjectVersion().getProjectName();
    }

    public OODFile(FileVersion fileVersion, ProjectVersion version) {
        this.name = fileVersion.getFileName();
        this.location = fileVersion.getPath();
        if (version != null) {
            String curProjectPath = version.getPath();
            if (curProjectPath != null && !curProjectPath.equals("") && location.startsWith(curProjectPath)) {
                location = location.substring(curProjectPath.length());
            }
            this.projectName = version.getVersionName();
        }
        this.id = location;
        this.type = 1;
        this.caption = "#" + fileVersion.getIndex() + this.name;
    }


    public OODFile(DomainInst domainInst, ProjectVersion version) {
        UserSpace userSpace = domainInst.getUserSpace();
        String euPath = domainInst.getEuPackage().replace(".", "/") + "/";
        this.projectName = version.getProject().getProjectName();
        this.name = userSpace.getName() + "(" + userSpace.name() + ")";
        this.location = euPath;
        this.id = location;
        this.group=true;
        this.iconColor = IconColorEnum.DARKBLUE.getName();
        this.imageClass = userSpace.getImageClass();
        this.type = 0;
        this.caption = name;

        if (domainInst.getUserSpace().equals(UserSpace.VIEW)) {
            this.initFold = false;
        } else {
            this.initFold = true;
        }

    }

    public OODFile(Folder folder, ProjectVersion version) {
        initFold = true;
        imageClass = "ri-folder-line";
        path = location;
        this.name = folder.getDescription() == null ? folder.getName() : folder.getDescription();

        String subpath = StringUtility.replace(folder.getPath(), version.getPath(), "/");
        try {
            APIPaths apiPaths = APIFactory.getInstance().getAPIPaths(subpath);
            if (apiPaths != null) {
                for (APIConfig config : apiPaths.getApiConfigs()) {
                    if (config.getChinaName() != null) {
                        this.name = folder.getName() + "(" + config.getDesc() + ")";
                        this.imageClass = apiPaths.getImageClass();
                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }


        this.location = folder.getPath();
        this.id = location;
        this.className = StringUtility.replace(location, version.getPath(), "");
        this.className = StringUtility.replace(this.className, "/", ".");
        if (className.endsWith(".")) {
            className = className.substring(0, className.length() - 1);
        }
        if (location.endsWith(".")) {
            className = className.substring(1, className.length());
        }

        this.iconColor = IconColorEnum.CYAN.getName();
        PackagePathType packagePathType = PackagePathType.equalsPath(subpath);
        if (packagePathType != null && packagePathType.getApiType() != null) {
            this.name = packagePathType.getDesc() + "(" + className + ")";
            this.imageClass = packagePathType.getImageClass();
            this.iconColor = packagePathType.getApiType().getIconColorEnum().getName();
            this.setInitFold(true);
            if (packagePathType.equals(PackagePathType.App)) {
                List<Folder> folders = folder.getChildrenList();
                List<FileInfo> infos = folder.getFileList();
                this.setInitFold(false);
                this.sub = new ArrayList<>();
                for (Folder cfolder : folders) {
                    this.sub.add(new OODFile(cfolder, version));
                }
                for (FileInfo fileInfo : infos) {
                    if (!fileInfo.getName().endsWith(".cls")) {
                        this.sub.add(new OODFile(fileInfo, version));
                    } else {
                        EUModule module = version.getModule(fileInfo.getPath());
                        if (module != null) {
                            this.sub.add(new OODFile(module));
                        }
                    }
                }
            }
        } else {
            if (!this.name.endsWith("(" + className + ")")) {
                this.name = this.name + "(" + className + ")";
            }

        }
        this.type = 0;
        this.caption = this.name;
        //模板文件没有版本
        if (version != null) {
            String curProjectPath = version.getPath();
            if (curProjectPath != null && !curProjectPath.equals("") && location.startsWith(curProjectPath)) {
                location = location.substring(curProjectPath.length());
            }
            this.projectName = version.getVersionName();
        }

        String rootPath = version.getRootFolder().getPath() + version.getProject().getProjectName() + "/";
        if (folder.getPath().equals(rootPath)) {
            imageClass = "ri-sitemap-line";
            this.initFold = false;
        }

    }


    public OODFile(FileInfo fileInfo, ProjectVersion version) {
        this.name = fileInfo.getDescription() == null ? fileInfo.getName() : fileInfo.getDescription();
        this.location = fileInfo.getPath();
        int index = name.lastIndexOf(".");
        String mimeType = null;
        if (index > 0) {
            String fileType = name.substring(index + 1).toLowerCase();
            this.imageClass = FileImgCssType.fromType(fileType).getImageClass();
        } else {
            this.imageClass = FileImgCssType.unkown.getImageClass();
        }
        this.iconColor = IconColorEnum.LIGHTPURPLE.getName();
        this.id = location;
        this.type = 1;
        this.caption = this.name;
        if (version != null) {
            String curProjectPath = version.getPath();
            if (curProjectPath != null && !curProjectPath.equals("") && location.startsWith(curProjectPath)) {
                location = location.substring(curProjectPath.length());
            }
            this.projectName = version.getVersionName();
        }
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public Boolean getInitFold() {
        return initFold;
    }

    public void setInitFold(Boolean initFold) {
        this.initFold = initFold;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<OODFile> getSub() {
        return sub;
    }

    public void setSub(List<OODFile> sub) {
        this.sub = sub;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageClass() {
        return imageClass;
    }

    public void setImageClass(String imageClass) {
        this.imageClass = imageClass;
    }

    @Override
    public int compareTo(OODFile o) {
        if (className != null && o.getClassName() != null) {
            return className.compareTo(o.getClassName());
        } else if (caption != null && o.getCaption() != null) {
            return caption.compareTo(o.getCaption());
        } else if (location != null && o.getLocation() != null) {
            return location.compareTo(o.getLocation());
        }

        return id.compareTo(o.getId());
    }

    public Boolean getGroup() {
        return group;
    }

    public void setGroup(Boolean group) {
        this.group = group;
    }
}