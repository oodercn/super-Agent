package net.ooder.nexus.storage.vfs;

import java.io.*;
import java.util.*;

public class LocalVFSManager {
    private static LocalVFSManager instance;
    public static final String THREAD_LOCK = "Thread Lock";

    String tempRootPath;

    public static LocalVFSManager getInstance() {
        if (instance == null) {
            synchronized (THREAD_LOCK) {
                if (instance == null) {
                    instance = new LocalVFSManager();
                }
            }
        }
        return instance;
    }

    LocalVFSManager() {
        tempRootPath = "./data/cache";
        File tempFile = new File(tempRootPath);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }
    }

    public LocalFileObject getFileObjectByHash(String hash) {
        LocalFileObject fileObject = null;
        File hashFile = new File(tempRootPath, hash);
        if (hashFile != null && hashFile.exists()) {
            fileObject = new LocalFileObject(hashFile, hash);
        }
        return fileObject;
    }

    public Boolean delete(String hash) {
        FileObject fileObject = this.getFileObjectByHash(hash);
        File hashFile = new File(fileObject.getPath());
        if (hashFile.exists()) {
            hashFile.delete();
        }
        return true;
    }

    public InputStream downLoadByHash(String hash) {
        InputStream inputStream = null;
        FileObject fileObject = this.getFileObjectByHash(hash);
        File hashFile = new File(fileObject.getPath());
        if (hashFile.exists()) {
            try {
                inputStream = new FileInputStream(hashFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return inputStream;
    }

    public LocalFileObject createFileObject(InputStream inputStream, String hash) {
        LocalFileObject localFileObject = null;
        File temp = null;
        FileOutputStream out = null;
        FileInputStream in = null;

        File hashFile = null;
        try {
            temp = File.createTempFile("" + System.currentTimeMillis(), ".temp");
            out = new FileOutputStream(temp);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in = new FileInputStream(temp);
            localFileObject = getFileObjectByHash(hash);
            if (localFileObject == null || localFileObject.getLength() == 0) {
                File tempFile = new File(tempRootPath);
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                hashFile = new File(tempFile.getAbsolutePath(), hash);
                if (!hashFile.exists()) {
                    hashFile.createNewFile();
                }
                copyFile(temp, hashFile);
                localFileObject = new LocalFileObject(hashFile, hash);
            }
            temp.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return localFileObject;
    }

    private void copyFile(File source, File target) {
        try (InputStream in = new FileInputStream(source);
             OutputStream out = new FileOutputStream(target)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTempRootPath() {
        return tempRootPath;
    }

    public void setTempRootPath(String tempRootPath) {
        this.tempRootPath = tempRootPath;
    }

    public String formatPath(String path) {
        path = path.replace("\\", "/");
        return path;
    }

    public List<Folder> loadFolderList(String[] ids) {
        List<Folder> infoList = new ArrayList<>();
        for (String id : ids) {
            Folder folder = this.getFolderByID(id);
            infoList.add(folder);
        }
        return infoList;
    }

    public Folder getFolderByID(String folderId) {
        File file = new File(folderId);
        Folder folder = null;
        if (file.exists() && file.isDirectory()) {
            folder = new LocalFolder(file, file.getName(), "folder");
        }
        return folder;
    }

    public FileInfo getFileInfoByID(String fileId) {
        FileInfo fileInfo = null;
        File file = new File(fileId);
        if (file.exists()) {
            fileInfo = new LocalFile(file);
        }
        return fileInfo;
    }

    public List<FileVersion> loadVersionList(String... ids) {
        List<FileVersion> infoList = new ArrayList<>();
        for (String id : ids) {
            FileVersion fileVersion = this.getVersionById(id);
            infoList.add(fileVersion);
        }
        return infoList;
    }

    public FileVersion getVersionById(String versionId) {
        LocalFileVersion version = null;
        File file = new File(versionId);
        if (file.exists() && file.length() > 0) {
            try {
                FileInputStream inputStream = new FileInputStream(file);
                String hash = calculateHash(inputStream);
                LocalFileObject fileObject = this.getFileObjectByHash(hash);
                if (fileObject == null) {
                    fileObject = this.createFileObject(inputStream, hash);
                }
                version = new LocalFileVersion(file, hash);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return version;
    }

    private String calculateHash(InputStream inputStream) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                md.update(buffer, 0, bytesRead);
            }
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public List<FileInfo> loadFileList(String... ids) {
        List<FileInfo> infoList = new ArrayList<>();
        for (String id : ids) {
            FileInfo fileInfo = this.getFileInfoByID(id);
            infoList.add(fileInfo);
        }
        return infoList;
    }

    public Boolean updateFileVersionInfo(String fileVersionId, String hash) {
        return true;
    }

    public Set<String> getVersionByHash(String hash) {
        Set<String> strings = new HashSet<>();
        strings.add(hash);
        return strings;
    }

    public Folder getFolderByPath(String path) {
        Folder folder = null;
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        File file = new File(path);
        if (file.exists()) {
            folder = new LocalFolder(file, file.getName(), "folder");
        }
        return folder;
    }

    public FileInfo getFileInfoByPath(String path) {
        FileInfo localFile = null;
        File file = new File(path);
        if (file.exists()) {
            localFile = new LocalFile(file);
        }
        return localFile;
    }

    public Boolean copyFolder(String spath, String tpath) {
        Folder sfolder = this.getFolderByPath(spath);
        if (sfolder != null) {
            Folder tfolder = this.mkDir(tpath);
            List<FileInfo> childFileList = sfolder.getFileList();
            for (FileInfo eFileInfo : childFileList) {
                copyFile(eFileInfo, tfolder);
            }
            List<Folder> childFolderList = sfolder.getChildrenList();
            for (Folder childFolder : childFolderList) {
                Folder copyFolder = tfolder.createChildFolder(childFolder.getName(), tfolder.getPersonId());
                copyFolder.setDescription(childFolder.getDescription());
                copyChildFolder(childFolder, copyFolder);
            }
        }
        return true;
    }

    private void copyChildFolder(Folder pfolder, Folder pNewFolder) {
        if (pfolder == null || pNewFolder == null) {
            return;
        }
        List<FileInfo> childFileList = pfolder.getFileList();
        for (FileInfo eFileInfo : childFileList) {
            copyFile(eFileInfo, pNewFolder);
        }
        List<Folder> childFolderList = pfolder.getChildrenList();
        for (Folder childFolder : childFolderList) {
            Folder copyFolder = pNewFolder.createChildFolder(childFolder.getName(), pNewFolder.getPersonId());
            copyFolder.setDescription(childFolder.getDescription());
            copyChildFolder(childFolder, copyFolder);
        }
    }

    public Boolean cloneFolder(String spath, String tpath) {
        return copyFolder(spath, tpath);
    }

    public Boolean updateFileInfo(String path, String name, String description) {
        LocalFile fileInfo = (LocalFile) this.getFileInfoByPath(path);
        fileInfo.setUpdateTime(System.currentTimeMillis());
        fileInfo.setName(name);
        fileInfo.setDescription(description);
        return true;
    }

    public Boolean updateFolderInfo(String path, String name, String description, String type) {
        LocalFolder folder = (LocalFolder) this.getFolderByPath(path);
        folder.setName(name);
        folder.setDescription(description);
        folder.setFolderType(type);
        return true;
    }

    public Boolean updateFolderState(String path, String state) {
        LocalFolder folder = (LocalFolder) this.getFolderByPath(path);
        folder.setState(state);
        return true;
    }

    public Boolean copyFile(String spath, String tpath) {
        FileInfo fileInfo = this.getFileInfoByPath(spath);
        Folder tfolder = this.getFolderByPath(tpath);
        copyFile(fileInfo, tfolder);
        return true;
    }

    public String copyFile(FileInfo eFileInfo, Folder newFolder) {
        LocalFile copyFileInfo = null;
        Folder dbfolder = this.getFolderByID(newFolder.getID());
        copyFileInfo = (LocalFile) dbfolder.createFile(eFileInfo.getName(), eFileInfo.getPersonId());
        FileVersion version = eFileInfo.getCurrentVersion();
        if (version == null || version.getFileObject() == null) {
            return copyFileInfo.getID();
        }
        LocalFileVersion copyVersion = (LocalFileVersion) copyFileInfo.getCurrentVersion();
        if (copyVersion == null) {
            copyVersion = (LocalFileVersion) copyFileInfo.createFileVersion();
        }
        copyVersion.setFileObjectId(version.getFileObject().getID());
        copyVersion.setSourceId(version.getVersionID());
        copyVersion.setCreateTime(System.currentTimeMillis());
        copyVersion.setPersonId(newFolder.getPersonId());
        return copyFileInfo.getID();
    }

    public LocalFileVersion createFileVersion(LocalFile info) {
        File file = new File(info.getPath());
        LocalFileVersion fileVersion = null;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            String hash = calculateHash(inputStream);
            fileVersion = new LocalFileVersion(file, hash);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileVersion;
    }

    public FileInfo createFile2(String path, String name, String description) {
        File folderFile = new File(path);
        folderFile.mkdirs();
        File file = new File(folderFile.getAbsolutePath(), name);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileInfo fileInfo = this.getFileInfoByPath(file.getAbsolutePath());
        if (fileInfo == null) {
            fileInfo = new LocalFile(file);
        }
        return fileInfo;
    }

    public Folder mkDir2(String path, String description, String type) {
        Folder folder = this.getFolderByPath(path);
        if (folder == null) {
            File file = new File(path);
            file.mkdirs();
            folder = new LocalFolder(file, description, type);
        }
        return folder;
    }

    public FileVersion createFileVersion(String path, String filehash) {
        FileVersion version = this.getVersionById(path);
        File file = new File(path);
        File hashFile = new File(tempRootPath, filehash);
        try {
            copyFile(hashFile, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (version == null) {
            version = new LocalFileVersion(file, filehash);
        }
        return version;
    }

    public Boolean deleteFile(String[] fileIds) {
        for (String fileId : fileIds) {
            this.removeFileInfo(fileId);
        }
        return true;
    }

    public Boolean deleteFolder(String folderId) {
        LocalFolder folder = (LocalFolder) this.getFolderByID(folderId);
        File file = new File(folder.getPath());
        if (file.exists()) {
            deleteDirectory(file);
        }
        return true;
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    public Boolean removeFileInfo(String fileId) {
        LocalFile folder = (LocalFile) this.getFileInfoByID(fileId);
        if (folder != null) {
            File file = new File(folder.getPath());
            if (file.exists()) {
                file.delete();
            }
        }
        return true;
    }

    public List<FileInfo> getChiledFileList(String id) {
        LocalFolder folder = (LocalFolder) this.getFolderByID(id);
        Set<String> folderIdList = folder.getChildrenIdList();
        List<FileInfo> fileInfos = loadFileList(folderIdList.toArray(new String[]{}));
        Collections.sort(fileInfos, new Comparator<FileInfo>() {
            public int compare(FileInfo o1, FileInfo o2) {
                return o2.getCreateTime() > o1.getCreateTime() ? 0 : -1;
            }
        });
        return fileInfos;
    }

    public List<Folder> getChildrenFolderRecursivelyList(String id) {
        LocalFolder folder = (LocalFolder) this.getFolderByID(id);
        Set<String> recursiveChildIdList = new LinkedHashSet<>();
        recursiveChildIdList = folder.getChildIdsRecursivelyList(recursiveChildIdList, folder);
        List<Folder> allChildList = loadFolderList(recursiveChildIdList.toArray(new String[]{}));
        return allChildList;
    }

    public List<Folder> getChildrenFolderList(String id) {
        LocalFolder folder = (LocalFolder) this.getFolderByID(id);
        Set<String> folderIdList = folder.getChildrenIdList();
        List<Folder> folderList = loadFolderList(folderIdList.toArray(new String[]{}));
        return folderList;
    }

    public List<FileInfo> getChiledFileRecursivelyList(String id) {
        Set<String> fileInfoIds = new HashSet<>();
        List<Folder> allChildList = getChildrenFolderRecursivelyList(id);
        for (Folder folder : allChildList) {
            fileInfoIds.addAll(folder.getFileIdList());
        }
        List<FileInfo> fileInfos = this.loadFileList(fileInfoIds.toArray(new String[]{}));
        return fileInfos;
    }

    public Folder mkDir(String path) {
        return this.mkDir2(path, null, "folder");
    }

    public FileInfo createFile(String path, String name) {
        return this.createFile2(path, name, name);
    }
}
