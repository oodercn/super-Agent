package net.ooder.web.vfs;

import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.annotation.MethodChinaName;
import net.ooder.common.ContextType;
import net.ooder.common.TokenType;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.vfs.*;
import net.ooder.vfs.service.VFSClientService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

@EsbBeanAnnotation(dataType = ContextType.Action, tokenType = TokenType.admin)
public class VFSClientServiceLocal implements VFSClientService {


    @Override
    @MethodChinaName(cname = "获取文件夹")
    public @ResponseBody
    ResultModel<Folder> getFolderByID(String folderId) {
        ResultModel<Folder> resultModel = new ResultModel<>();
        Folder folder = this.getLocalService().getFolderByID(folderId);
        resultModel.setData(folder);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "获取文件")
    public @ResponseBody
    ResultModel<FileInfo> getFileInfoByID(String fileId) {
        ResultModel<FileInfo> resultModel = new ResultModel<>();
        FileInfo fileInfo = this.getLocalService().getFileInfoByID(fileId);
        resultModel.setData(fileInfo);
        return resultModel;
    }


    @Override
    @MethodChinaName(cname = "获取文件副本")
    public @ResponseBody
    ResultModel<FileCopy> getFileCopyById(String id) {
        return new ResultModel<>();
    }

    @Override
    @MethodChinaName(cname = "获取文件版本")
    public @ResponseBody
    ResultModel<FileVersion> getVersionById(String versionId) {
        ResultModel<FileVersion> resultModel = new ResultModel<>();
        FileVersion version=getLocalService().getVersionById(versionId);
        resultModel.setData(version);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "删除文件")
    public @ResponseBody
    ResultModel<Boolean> deleteFile(@RequestBody String[] fileIds) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean del = getLocalService().deleteFile(fileIds);
        resultModel.setData(del);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "删除文件夹")
    public @ResponseBody
    ResultModel<Boolean> deleteFolder(String folderId) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean del = getLocalService().deleteFolder(folderId);
        resultModel.setData(del);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "复制视图")
    public @ResponseBody
    ResultModel<Boolean> copyView(@RequestBody List<FileView> views, @RequestBody FileVersion newVersion) {
        return new ResultModel<>();
    }

    @Override
    @MethodChinaName(cname = "获取回收站文件", display = false)
    public @ResponseBody
    ListResultModel<List<FileInfo>> getPersonDeletedFile(String userId) {
        return new ListResultModel<List<FileInfo>>();
    }

    @Override
    @MethodChinaName(cname = "获取个人回收站文件", display = false)
    public @ResponseBody
    ListResultModel<List<Folder>> getPersonDeletedFolder(String userId) {
        return new ListResultModel<List<Folder>>();
    }

    @Override
    @MethodChinaName(cname = "获取已删除文件", display = false)
    public @ResponseBody
    ResultModel<FileInfo> getDeletedFile(String fileId) {
        return new ResultModel<>();
    }

    @Override
    @MethodChinaName(cname = "获取已删除文件夹", display = false)
    public @ResponseBody
    ResultModel<Folder> getDeletedFolder(String folderId) {
        return new ResultModel<>();
    }


    @Override
    @MethodChinaName(cname = "删除文件", display = false)
    public @ResponseBody
    ResultModel<Boolean> removeFileInfo(String fileId) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean del = getLocalService().removeFileInfo(fileId);
        resultModel.setData(del);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "获取文件链接")
    public @ResponseBody
    ResultModel<FileLink> getFileLinkByID(String linkId) {
        return new ResultModel<>();
    }

    @Override
    @MethodChinaName(cname = "获取文件列表")
    public @ResponseBody
    ListResultModel<List<FileInfo>> getChiledFileList(String id) {
        ListResultModel<List<FileInfo>> resultModel = new ListResultModel<>();
        List<FileInfo> list = getLocalService().getChiledFileList(id);
        resultModel.setData(list);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "递归子文件夹")
    public @ResponseBody
    ListResultModel<List<Folder>> getChildrenFolderRecursivelyList(String id) {
        ListResultModel<List<Folder>> resultModel = new ListResultModel<>();
        List<Folder> list = getLocalService().getChildrenFolderRecursivelyList(id);
        resultModel.setData(list);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "递归文件")
    public @ResponseBody
    ListResultModel<List<FileInfo>> getChiledFileRecursivelyList(String id) {
        ListResultModel<List<FileInfo>> resultModel = new ListResultModel<>();
        List<FileInfo> list = getLocalService().getChiledFileRecursivelyList(id);
        resultModel.setData(list);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "获取子文件夹")
    public @ResponseBody
    ListResultModel<List<Folder>> getChildrenFolderList(String id) {
        ListResultModel<List<Folder>> resultModel = new ListResultModel<>();
        List<Folder> list = getLocalService().getChildrenFolderList(id);
        resultModel.setData(list);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "批量装载文件夹", display = false)
    public @ResponseBody
    ListResultModel<List<Folder>> loadFolderList(@RequestBody String[] ids) {
        ListResultModel<List<Folder>> resultModel = new ListResultModel<>();
        List<Folder> list = getLocalService().loadFolderList(ids);
        resultModel.setData(list);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "批量装载文件", display = false)
    public @ResponseBody
    ListResultModel<List<FileInfo>> loadFileList(@RequestBody String[] ids) {
        ListResultModel<List<FileInfo>> resultModel = new ListResultModel<>();
        List<FileInfo> infoList=this.getLocalService().loadFileList(ids);
        resultModel.setData(infoList);
        return resultModel;
    }


    @Override
    @MethodChinaName(cname = "批量装载文件版本", display = false)
    public @ResponseBody
    ListResultModel<List<FileVersion>> loadVersionList(@RequestBody String[] ids) {
        ListResultModel<List<FileVersion>> resultModel=new ListResultModel<List<FileVersion>>();
        List<FileVersion> fileVersions=this.getLocalService().loadVersionList(ids);
        resultModel.setData(fileVersions);
        return resultModel;
    }


    @Override
    @MethodChinaName(cname = "获取视图")
    public @ResponseBody
    ResultModel<FileView> getFileViewByID(String fileViewId) {
       return new ResultModel<>();
    }


    @Override
    @MethodChinaName(cname = "创建视图")
    public @ResponseBody
    ResultModel<FileView> createViewByVersionId(String versionId, String objectId, Integer fileIndex) {
        return new ResultModel<>();
    }


    @Override
    @MethodChinaName(cname = "更新版本信息")
    public @ResponseBody
    ResultModel<Boolean> updateFileVersionInfo(String fileVersionId, String hash) {
        ResultModel<Boolean> resultModel = new ResultModel<>();
        Boolean update = getLocalService().updateFileVersionInfo(fileVersionId,hash);
        resultModel.setData(update);
        return resultModel;
    }

    @Override
    @MethodChinaName(cname = "更新视图信息")
    public @ResponseBody
    ResultModel<Boolean> updateFileViewInfo(@RequestBody FileView view) {

        return new ResultModel<>();
    }


    @Override
    @MethodChinaName(cname = "批量装载视图信息", display = false)
    public @ResponseBody
    ListResultModel<List<FileView>> loadFileViewList(@RequestBody String[] ids) {
        return new ListResultModel<>();
    }


    //
    @Override
    @MethodChinaName(cname = "根据HASH查询版本", display = false)
    public @ResponseBody
    ListResultModel<Set<String>> getVersionByHash(String hash) {
        ListResultModel<Set<String>> resultModel=new ListResultModel<>();
        Set<String> stringSet = getLocalService().getVersionByHash(hash);
        resultModel.setData(stringSet);
        return resultModel;
    }


    public LocalVFSManager getLocalService() {
        return LocalVFSManager.getInstance();
    }


}
