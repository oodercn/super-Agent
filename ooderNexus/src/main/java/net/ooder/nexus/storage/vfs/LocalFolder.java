package net.ooder.nexus.storage.vfs;

import java.io.File;
import java.util.*;

public class LocalFolder implements Folder {
    private String ID;
    private String name;
    private String path;
    private String personId;
    private String description;
    private String parentId;
    private String oldFolderId;
    private String folderType;
    private Long createTime;
    private Long updateTime;
    private Set<String> childrenIdList;
    private Set<String> fileIdList;
    private String state;

    public LocalFolder(File file, String name, String folderType) {
        this.ID = file.getAbsolutePath();
        this.ID = this.ID.replace("\\", "/");
        this.name = name;
        this.path = file.getAbsolutePath().replace("\\", "/");
        this.personId = "system";
        this.folderType = folderType;
        this.description = name;
        this.createTime = file.lastModified();
        this.updateTime = file.lastModified();
        this.childrenIdList = new HashSet<>();
        this.fileIdList = new HashSet<>();
        loadChildren();
    }

    private void loadChildren() {
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    childrenIdList.add(file.getAbsolutePath().replace("\\", "/"));
                } else {
                    fileIdList.add(file.getAbsolutePath().replace("\\", "/"));
                }
            }
        }
    }

    @Override
    public String getID() {
        return ID;
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
    public String getPersonId() {
        return personId;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getParentId() {
        return parentId;
    }

    @Override
    public String getOldFolderId() {
        return oldFolderId;
    }

    @Override
    public String getFolderType() {
        return folderType;
    }

    @Override
    public Long getCreateTime() {
        return createTime;
    }

    @Override
    public Long getUpdateTime() {
        return updateTime;
    }

    @Override
    public Set<String> getChildrenIdList() {
        return childrenIdList;
    }

    @Override
    public Set<String> getFileIdList() {
        return fileIdList;
    }

    @Override
    public List<Folder> getChildrenList() {
        List<Folder> folders = new ArrayList<>();
        for (String childId : childrenIdList) {
            File file = new File(childId);
            if (file.exists() && file.isDirectory()) {
                folders.add(new LocalFolder(file, file.getName(), folderType));
            }
        }
        return folders;
    }

    @Override
    public List<FileInfo> getFileList() {
        List<FileInfo> files = new ArrayList<>();
        for (String fileId : fileIdList) {
            File file = new File(fileId);
            if (file.exists() && !file.isDirectory()) {
                files.add(new LocalFile(file));
            }
        }
        return files;
    }

    @Override
    public Folder createChildFolder(String name, String personId) {
        File childFile = new File(path, name);
        if (!childFile.exists()) {
            childFile.mkdirs();
        }
        return new LocalFolder(childFile, name, folderType);
    }

    @Override
    public FileInfo createFile(String name, String personId) {
        File file = new File(path, name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new LocalFile(file);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }

    public Set<String> getChildIdsRecursivelyList(Set<String> recursiveChildIdList, Folder folder) {
        if (folder == null) {
            return recursiveChildIdList;
        }
        Set<String> children = folder.getChildrenIdList();
        recursiveChildIdList.addAll(children);
        for (String childId : children) {
            LocalVFSManager manager = LocalVFSManager.getInstance();
            Folder childFolder = manager.getFolderByID(childId);
            getChildIdsRecursivelyList(recursiveChildIdList, childFolder);
        }
        return recursiveChildIdList;
    }
}
