package net.ooder.proxy.vfs;

import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.annotation.MethodChinaName;
import net.ooder.common.ContextType;
import net.ooder.common.FolderState;
import net.ooder.common.FolderType;
import net.ooder.common.TokenType;
import net.ooder.config.ResultModel;
import net.ooder.vfs.FileInfo;
import net.ooder.vfs.FileVersion;
import net.ooder.vfs.Folder;
import net.ooder.vfs.service.VFSDiskService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@EsbBeanAnnotation(dataType = ContextType.Action, tokenType = TokenType.admin)
public class VFSDiskServiceLocal implements VFSDiskService {


    @MethodChinaName(cname = "创建文件夹")
    public @ResponseBody
    ResultModel<Folder> mkDir(String path) {
        ResultModel<Folder> resultModel = new ResultModel();
        Folder folder = getLocalService().mkDir(path);
        resultModel.setData(folder);
        return resultModel;
    }


    @MethodChinaName(cname = "创建文件")
    public @ResponseBody
    ResultModel<FileInfo> createFile(String path, String name) {
        ResultModel<FileInfo> resultModel = new ResultModel();
        FileInfo fileInfo = getLocalService().createFile(path, name);
        resultModel.setData(fileInfo);
        return resultModel;
    }


    @MethodChinaName(cname = "根据文件夹path获取文件")
    public @ResponseBody
    ResultModel<Folder> getFolderByPath(String path) {
        ResultModel<Folder> resultModel = new ResultModel<>();
        Folder folder = getLocalService().getFolderByPath(path);
        resultModel.setData(folder);
        return resultModel;
    }

    @RequestMapping(method = RequestMethod.POST, value = "GetFileInfoByPath")
    @MethodChinaName(cname = "根据逻辑地址获取文件信息")
    public @ResponseBody
    ResultModel<FileInfo> getFileInfoByPath(String path) {
        ResultModel<FileInfo> resultModel = new ResultModel<>();
        FileInfo fileInfo = getLocalService().getFileInfoByPath(path);
        resultModel.setData(fileInfo);
        return resultModel;
    }


    @MethodChinaName(cname = "删除文件")
    public @ResponseBody
    ResultModel<Boolean> delete(String path) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean del = getLocalService().delete(path);
        resultModel.setData(del);
        return resultModel;
    }

    @Override

    @MethodChinaName(cname = "COPY文件夹")
    public @ResponseBody
    ResultModel<Boolean> copyFolder(String spath, String tpaht) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean copyFolder = getLocalService().copyFolder(spath, tpaht);
        resultModel.setData(copyFolder);
        return resultModel;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "CloneFolder")
    @MethodChinaName(cname = "克隆文件夹")
    public @ResponseBody
    ResultModel<Boolean> cloneFolder(String spath, String tpaht) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean copyFolder = getLocalService().cloneFolder(spath, tpaht);
        resultModel.setData(copyFolder);
        return resultModel;
    }

    @Override

    @MethodChinaName(cname = "更新文件信息")
    public @ResponseBody
    ResultModel<Boolean> updateFileInfo(String path, String name, String descrition) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean copyFolder = getLocalService().updateFileInfo(path, name, descrition);
        resultModel.setData(copyFolder);
        return resultModel;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "updateFolderInfo")
    @MethodChinaName(cname = "更新文件夹信息")
    public @ResponseBody
    ResultModel<Boolean> updateFolderInfo(String path, String name, String descrition, FolderType type) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean copyFolder = getLocalService().updateFolderInfo(path, name, descrition, type);
        resultModel.setData(copyFolder);
        return resultModel;
    }

    @Override
    @ResponseBody
    public ResultModel<Boolean> updateFolderState(String path, FolderState state) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean copyFolder = getLocalService().updateFolderState(path, state);
        resultModel.setData(copyFolder);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "COPY文件信息")
    public @ResponseBody
    ResultModel<Boolean> copyFile(String path, String path2) {

        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean copyFolder = getLocalService().copyFile(path, path2);
        resultModel.setData(copyFolder);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "COPY文件信息并重命名")
    public @ResponseBody
    ResultModel<FileInfo> createFile2(String path, String name, String descrition) {
        ResultModel<FileInfo> resultModel = new ResultModel();
        FileInfo fileInfo = getLocalService().createFile2(path, name, descrition);
        resultModel.setData(fileInfo);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "创建文件夹")
    public @ResponseBody
    ResultModel<Folder> mkDir2(String path, String descrition, FolderType type) {
        ResultModel<Folder> resultModel = new ResultModel<>();
        Folder folder = getLocalService().mkDir2(path, descrition, type);
        resultModel.setData(folder);
        return resultModel;
    }

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "createFileVersion")
    @MethodChinaName(cname = "创建指定HASH版本")
    public @ResponseBody
    ResultModel<FileVersion> createFileVersion(String path, String filehash) {
        ResultModel<FileVersion> resultModel = new ResultModel<>();
        FileVersion version = getLocalService().createFileVersion(path, filehash);

        resultModel.setData(version);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "获取版本")
    public @ResponseBody
    ResultModel<FileVersion> getVersionByPath(String path) {
        ResultModel<FileVersion> resultModel = new ResultModel<>();
        FileVersion version = getLocalService().getVersionById(path);
        resultModel.setData(version);
        return resultModel;
    }

    public LocalVFSManager getLocalService() {
        return LocalVFSManager.getInstance();
    }

}
