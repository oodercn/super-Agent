package net.ooder.web.vfs;

import net.ooder.common.FolderState;
import net.ooder.common.FolderType;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.common.util.FileUtility;
import net.ooder.common.util.IOUtility;
import net.ooder.common.util.StringUtility;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.org.conf.OrgConstants;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.FileObject;
import net.ooder.vfs.FileVersion;
import net.ooder.vfs.Folder;
import net.ooder.vfs.ct.CtVfsFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

public class LocalVFSManager {

    private static LocalVFSManager instance;
    public static final String THREAD_LOCK = "Thread Lock";
    private static final Log log = LogFactory.getLog(OrgConstants.CONFIG_KEY.getType(), LocalVFSManager.class);

    ProjectVersion projectVersion;

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
        tempRootPath = CtVfsFactory.getLocalCachePath();
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
        return null;
    }

    ;

    public MD5InputStream downLoadByHash(String hash) {
        MD5InputStream inputStream = null;
        FileObject fileObject = this.getFileObjectByHash(hash);
        File hashFile = new File(fileObject.getPath());
        if (hashFile.exists()) {
            try {
                inputStream = new MD5InputStream(new FileInputStream(hashFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return inputStream;
    }


    public LocalFileObject createFileObject(MD5InputStream md5InputStream) {
        LocalFileObject localFileObject = null;
        String hash = "";
        File temp = null;
        FileOutputStream out = null;
        FileInputStream in = null;

        File hashFile = null;
        try {
            temp = File.createTempFile("" + System.currentTimeMillis(), ".temp");
            out = new FileOutputStream(temp);
            in = new FileInputStream(temp);
            IOUtils.copy(md5InputStream, out);
            hash = DigestUtils.md5Hex(in);
            localFileObject = getFileObjectByHash(hash);
            if (localFileObject == null || localFileObject.getLength() == 0) {
                File tempFile = new File(CtVfsFactory.getLocalCachePath());
                if (!tempFile.exists()) {
                    tempFile.mkdirs();
                }
                hashFile = new File(tempFile.getAbsolutePath(), hash);
                if (!hashFile.exists()) {
                    hashFile.createNewFile();
                }
                IOUtility.copy(temp, hashFile);
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

    public void downLoad() {


    }


    public String getTempRootPath() {
        return tempRootPath;
    }

    public void setTempRootPath(String tempRootPath) {
        this.tempRootPath = tempRootPath;
    }

    public ProjectVersion getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(ProjectVersion projectVersion) {
        this.projectVersion = projectVersion;
    }


    public String formartPath(String path) {
        path = StringUtility.replace(path, "\\", "/");
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
            if (file.getParent().endsWith(ESDFacrory.defaultSpace)) {
                folder = new LocalFolder(file, file.getName(), FolderType.space);
            } else {
                folder = new LocalFolder(file, file.getName(), FolderType.folder);
            }
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
                MD5InputStream md5InputStream = new MD5InputStream(new FileInputStream(file));
                LocalFileObject fileObject = this.createFileObject(md5InputStream);
                version = new LocalFileVersion(file, fileObject.getHash());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
        return version;
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
            if (path.endsWith(ESDFacrory.defaultSpace + "/")) {
                folder = new LocalFolder(file, file.getName(), FolderType.space);
            } else {
                folder = new LocalFolder(file, file.getName(), FolderType.folder);
            }
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

    public Boolean copyFolder(String spath, String tpaht) {

        Folder sfolder = this.getFolderByPath(spath);
        if (sfolder != null) {
            Folder tfolder = mkDir(spath);
            Map map = new HashMap();
            List<FileInfo> childFileList = ((Folder) sfolder).getFileList();
            for (FileInfo eFileInfo : childFileList) {
                copyFile(eFileInfo, tfolder);
            }
            // 拷贝文件夹
            List<Folder> childFolderList = ((Folder) sfolder).getChildrenList();
            for (Folder childFolder : childFolderList) {
                Folder copyFolder = ((Folder) tfolder).createChildFolder(childFolder.getName(), tfolder.getPersonId());
                copyFolder.setDescription(childFolder.getDescription());
                // 递归子文件夹
                copyChildFolder(childFolder, copyFolder);
            }
        }
        return true;
    }


    private void copyChildFolder(Folder pfolder, Folder pNewFolder) {
        Map map = new HashMap();
        if (pfolder == null || pNewFolder == null) {
            return;
        }
        List<FileInfo> childFileList = ((Folder) pfolder).getFileList();
        for (FileInfo eFileInfo : childFileList) {
            copyFile(eFileInfo, pNewFolder);
        }
        // 拷贝文件夹
        List<Folder> childFolderList = ((Folder) pfolder).getChildrenList();
        for (Folder childFolder : childFolderList) {
            Folder copyFolder = ((Folder) pNewFolder).createChildFolder(childFolder.getName(), pNewFolder.getPersonId());
            copyFolder.setDescription(childFolder.getDescription());
            // 递归子文件夹
            copyChildFolder(childFolder, copyFolder);
        }

    }

    public Boolean cloneFolder(String spath, String tpaht) {
        return copyFolder(spath, tpaht);

    }

    public Boolean updateFileInfo(String path, String name, String descrition) {
        LocalFile fileInfo = (LocalFile) this.getFileInfoByPath(path);
        fileInfo.setUpdateTime(System.currentTimeMillis());
        fileInfo.setName(name);
        fileInfo.setDescrition(descrition);
        return true;
    }

    public Boolean updateFolderInfo(String path, String name, String description, FolderType type) {
        LocalFolder folder = (LocalFolder) this.getFolderByPath(path);
        folder.setName(name);
        folder.setDescription(description);
        folder.setFolderType(type);
        return true;
    }

    public Boolean updateFolderState(String path, FolderState state) {
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
        Map map = new HashMap();
        LocalFile copyFileInfo = null;

        Folder dbfolder = this.getFolderByID(newFolder.getID());
        copyFileInfo = (LocalFile) dbfolder.createFile(eFileInfo.getName(), eFileInfo.getPersonId());
        // 拷贝版本
        FileVersion version = (FileVersion) eFileInfo.getCurrentVersion();
        if (version == null || version.getFileObject() == null) {
            return copyFileInfo.getID();
        }

        LocalFileVersion copyVerson = (LocalFileVersion) copyFileInfo.getCurrentVersion();
        if (copyVerson == null) {
            copyVerson = (LocalFileVersion) copyFileInfo.createFileVersion();
        }

        copyVerson.setFileObjectId(version.getFileObject().getID());
        copyVerson.setSourceId(version.getVersionID());
        copyVerson.setCreateTime(System.currentTimeMillis());
        copyVerson.setPersonId(newFolder.getPersonId());
        return copyFileInfo.getID();
    }

    public LocalFileVersion createFileVersion(LocalFile info) {
        File file = new File(info.getPath());
        LocalFileVersion fileVersion = null;
        MD5InputStream md5InputStream = null;
        try {
            md5InputStream = new MD5InputStream(new FileInputStream(file));
            fileVersion = new LocalFileVersion(file, md5InputStream.getHashString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileVersion;
    }

    public FileInfo createFile2(String path, String name, String descrition) {
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

    public Folder mkDir2(String path, String descrition, FolderType type) {
        Folder folder = this.getFolderByPath(path);
        if (folder == null) {
            File file = new File(path);
            file.mkdirs();
            folder = new LocalFolder(file, descrition, type);
        }
        return folder;
    }

    public FileVersion createFileVersion(String path, String filehash) {
        FileVersion version = this.getVersionById(path);
        File file = new File(path);
        File hashFile = new File(CtVfsFactory.getLocalCachePath(), filehash);

        try {
            IOUtility.copy(hashFile, file);
        } catch (IOException e) {
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
            try {
                FileUtility.deleteDirectory(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // file.delete();
        }
        return true;
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
                return o2.getCreateTime() - o1.getCreateTime() > 0 ? 0 : -1;
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
        return this.mkDir2(path, null, FolderType.folder);
    }

    public FileInfo createFile(String path, String name) {
        return this.createFile2(path, name, name);
    }

}
