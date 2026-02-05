package net.ooder.tools;


import net.ooder.esd.engine.Project;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.engine.enums.ProjectDefAccess;
import net.ooder.esd.engine.enums.ProjectVersionStatus;

public class OODProject {

    String projectName;
    String name;
    String versionName;
    String id;
    String desc;

    String url;
    String location;
    String indexPage;


    String imageClass;
    Long createTime;
    Long updateTime;
    Integer version;
    String personName;
    ProjectDefAccess projectType;
    ProjectVersionStatus status;


    public OODProject(ProjectVersion version) {
        this.version = version.getVersion();
        this.versionName = version.getVersionName();
        this.projectName = version.getProject().getProjectName();
        this.location = version.getPath();


        this.status = version.getStatus();
        Project project = version.getProject();
        this.personName = project.getOwner() == null ? "" : project.getOwner().getName();
        this.imageClass = imageClass;
        this.url = project.getPublicServerUrl();
        this.indexPage = project.getConfig().getIndex();
        this.id = version.getVersionName();
        this.projectType = project.getProjectType();
        this.createTime = version.getRootFolder().getCreateTime();
        this.updateTime = version.getRootFolder().getUpdateTime();
        if (updateTime == null || updateTime < this.createTime) {
            updateTime = createTime;
        }
    }


    public OODProject(Project project) {
        this.desc = project.getDesc();
        this.projectType = project.getProjectType();
        this.projectName = project.getProjectName();
        this.name = projectName;
        this.indexPage = project.getConfig().getIndex();
        this.url = project.getPublicServerUrl();
        this.location = project.getPath();
        this.personName = project.getOwner() == null ? "" : project.getOwner().getName();
        this.imageClass = imageClass;
        this.id = project.getId();
        this.createTime = project.getRootfolder().getCreateTime();
        this.updateTime = project.getRootfolder().getUpdateTime();
        if (updateTime == null || updateTime < this.createTime) {
            updateTime = createTime;
        }

    }


    public String getIndexPage() {
        return indexPage;
    }

    public void setIndexPage(String indexPage) {
        this.indexPage = indexPage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ProjectDefAccess getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectDefAccess projectType) {
        this.projectType = projectType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public ProjectVersionStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectVersionStatus status) {
        this.status = status;
    }


    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }


    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }


    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}