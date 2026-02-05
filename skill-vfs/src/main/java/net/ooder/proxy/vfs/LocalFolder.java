package net.ooder.proxy.vfs;

import com.alibaba.fastjson.annotation.JSONField;
import net.ooder.common.FolderState;
import net.ooder.common.FolderType;
import net.ooder.common.JDSException;
import net.ooder.common.util.CnToSpell;
import net.ooder.common.util.StringUtility;
import net.ooder.config.JDSConfig;
import net.ooder.server.JDSServer;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtFolder;
import net.ooder.vfs.ct.CtVfsFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class LocalFolder implements Folder {

    private String ID;

    private String name;

    private String path = "";

    private Integer hit;

    private String parentId;

    private String personId;

    private FolderType folderType;

    private Long size;

    private Integer orderNum = 0;

    private FolderState state;

    private String systemCode;

    private String description;

    private Long createTime;

    public Integer index = 0;

    public Long updateTime;

    public Integer recycle = 0;

    // 文件
    private Set<String> fileIdList;

    // 子文件夹
    private Set<String> childIdList;


    @JSONField(serialize = false)
    File folder;

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }


    public LocalFolder(File folder, String description, FolderType type) {

        this.folder = folder;
        this.ID = StringUtility.replace(folder.getAbsolutePath(), "\\", "/");
        this.name = folder.getName();
        this.description = description;
        this.folderType = type;
        String rootPath = JDSConfig.Config.currServerHome().getAbsolutePath();
        rootPath = StringUtility.replace(rootPath, "\\", "/");
        this.path = StringUtility.replace(folder.getAbsolutePath(), "\\", "/");
        this.path = StringUtility.replace(path, rootPath, "") + "/";
        if (folder.getAbsoluteFile().getParentFile() != null) {
            this.parentId = StringUtility.replace(folder.getAbsoluteFile().getParentFile().getAbsolutePath(), "\\", "/");
        } else {
            try {
                this.parentId = StringUtility.replace(folder.getCanonicalFile().getParentFile().getAbsolutePath(), "\\", "/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.personId = JDSServer.getInstance().getAdminUser().getId();
        } catch (JDSException e) {
            e.printStackTrace();
        }
        this.createTime = folder.lastModified();
        this.updateTime = folder.lastModified();

        File files[] = folder.listFiles();
        if (files != null) {
            this.fileIdList = new HashSet<>();
            this.childIdList = new HashSet<>();
            for (File file : files) {
                if (file.isFile()) {
                    fileIdList.add(StringUtility.replace(file.getAbsolutePath(), "\\", "/"));
                } else {
                    childIdList.add(StringUtility.replace(file.getAbsolutePath(), "\\", "/"));
                }
            }
        }
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public Set<String> getFileIdList() {
        return fileIdList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public String getPersonId() {
        return personId;
    }

    @Override
    public void setName(String name) {
        this.name = name;

    }

    @Override
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    @Override
    public void setFolderType(FolderType type) {
        this.folderType = type;
    }

    @Override
    public FolderType getFolderType() {
        return folderType;
    }

    @Override
    public Long getFolderSize() {
        return size;
    }

    @Override
    public int getOrderNum() {
        return this.orderNum;
    }

    @Override
    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public int getRecycle() {

        return recycle;
    }

    @Override
    public void setState(FolderState state) {

        this.state = state;
    }

    @Override
    public FolderState getState() {
        return state;
    }

    @Override
    public long getCreateTime() {
        return this.createTime;
    }

    @Override
    public String getDescription() {
        return this.description == null ? this.name : this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;

    }

    @Override
    public String getSystemCode() {
        return this.systemCode;
    }

    @Override
    public void setSystemCode(String sysCode) {
        this.systemCode = sysCode;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getIndex() {

        return index;
    }

    @Override
    public Long getUpdateTime() {
        return updateTime;
    }

    @Override
    public int getCachedSize() {
        return 0;
    }

    @Override
    public void setHit(Integer hit) {
        this.hit = hit;

    }

    @Override
    public Integer getHit() {
        return hit;
    }

    @Override

    @JSONField(serialize = false)
    public Folder getParent() {
        Folder folder = null;
        try {
            folder = CtVfsFactory.getCtVfsService().getFolderById(this.parentId);
            if (folder != null) {
                folder = new CtFolder(folder);
            }

        } catch (JDSException e) {

            e.printStackTrace();
        }
        return folder;

    }


    @JSONField(serialize = false)
    public Set<String> getChildIdsRecursivelyList(Set<String> recursiveChildIdList, Folder folder) {
        if (!folder.getChildrenIdList().isEmpty()) {
            List<Folder> folderList = folder.getChildrenList();
            for (int i = 0; i < folderList.size(); i++) {
                recursiveChildIdList.add(folderList.get(i).getID());
                // 递归子文件夹
                recursiveChildIdList = getChildIdsRecursivelyList(recursiveChildIdList, folderList.get(i));
            }
        }
        return recursiveChildIdList;
    }


    @Override
    public Set<String> getChildrenIdList() {
        return this.childIdList;
    }

    @Override

    @JSONField(serialize = false)
    public List<Folder> getChildrenList() {
        List<Folder> folderList = new ArrayList<Folder>();
        Set<String> folderIdList = this.getChildrenIdList();
        try {
            folderList = CtVfsFactory.getCtVfsService().loadFolers(folderIdList);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        Collections.sort(folderList, new Comparator<Folder>() {
            public int compare(Folder o1, Folder o2) {
                return o2.getCreateTime() - o1.getCreateTime() > 0 ? 0 : -1;
            }
        });

        return folderList;
    }

    @Override

    @JSONField(serialize = false)
    public List<Folder> getChildrenRecursivelyList() {
        Set<String> recursiveChildIdList = new LinkedHashSet<>();
        recursiveChildIdList = getChildIdsRecursivelyList(recursiveChildIdList, this);
        List<Folder> allChildList = new ArrayList<Folder>();
        try {
            allChildList = CtVfsFactory.getCtVfsService().loadFolers(recursiveChildIdList);
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return allChildList;
    }

    @Override

    @JSONField(serialize = false)
    public List<FileInfo> getFileList() {
        List<FileInfo> fileList = new ArrayList<FileInfo>();
        Set<String> paths = new LinkedHashSet<>();
        Set<String> fileIdList = this.getFileIdList();
        try {
            List<FileInfo> remotefileList = CtVfsFactory.getCtVfsService().loadFiles(fileIdList);
            for (FileInfo fileInfo : remotefileList) {
                if (fileInfo != null && !paths.contains(fileInfo.getPath())) {
                    fileList.add(fileInfo);
                    paths.add(fileInfo.getPath());
                }
            }
            Collections.sort(fileList, new Comparator<FileInfo>() {
                public int compare(FileInfo o1, FileInfo o2) {
                    return o2.getCreateTime() - o1.getCreateTime() > 0 ? 0 : -1;
                }
            });


        } catch (JDSException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    @Override

    @JSONField(serialize = false)
    public List<FileInfo> getFileListRecursively() {
        Set<String> fileIds = getFileIdListRecursively();
        if (fileIds == null) {
            return new ArrayList<FileInfo>();
        }
        List<FileInfo> fileList = new ArrayList<FileInfo>();

        try {
            fileList = CtVfsFactory.getCtVfsService().loadFiles(fileIds);
        } catch (JDSException e) {
            e.printStackTrace();
        }

        return fileList;
    }


    @JSONField(serialize = false)
    private Set<String> getFileIdListRecursively() {
        Set<String> fileIdSet = new LinkedHashSet<String>();
        Set<String> fileList = getFileIdList();
        for (String fileId : fileList) {
            fileIdSet.add(fileId);
        }
        List<Folder> folderList = getChildrenRecursivelyList();
        for (int i = 0; i < folderList.size(); i++) {
            Folder folder = folderList.get(i);
            if (folder == null) {
                continue;
            }
            Set<String> cfileIdList = folder.getFileIdList();
            for (String cfileId : cfileIdList) {
                fileIdSet.add(cfileId);
            }
        }
        return fileIdSet;
    }


    @Override

    @JSONField(serialize = false)
    public FileInfo createFile(String name, String createPersonId) {
        return createFile(CnToSpell.getFullSpell(name), name, createPersonId);
    }

    @Override

    @JSONField(serialize = false)
    public FileInfo createFile(String name, String descrition, String createPersonId) {
        FileInfo fileInfo = null;
        try {
            fileInfo = CtVfsFactory.getCtVfsService().createFile(this.getPath(), name, descrition);
            this.fileIdList.add(fileInfo.getID());
        } catch (JDSException e) {

            e.printStackTrace();
        }
        return fileInfo;
    }

    @Override

    @JSONField(serialize = false)
    public Folder createChildFolder(String name, String createPersonId) {
        return createChildFolder(CnToSpell.getFullSpell(name), name, createPersonId);
    }

    @Override

    @JSONField(serialize = false)
    public Folder createChildFolder(String name, String descrition, String createPersonId) {
        Folder folder = null;
        name = CnToSpell.getFullSpell(name);
        try {
            List<Folder> childlist = this.getChildrenList();

            List<String> childNamelist = new ArrayList<String>();

            for (Folder cfolder : childlist) {
                childNamelist.add(cfolder.getName());
            }
            boolean hasName = false;
            folder = CtVfsFactory.getCtVfsService().mkDir(this.getPath() + name, descrition, FolderType.folder);
            this.childIdList.add(folder.getID());


        } catch (JDSException e) {

            e.printStackTrace();
        }
        return folder;

    }

}
