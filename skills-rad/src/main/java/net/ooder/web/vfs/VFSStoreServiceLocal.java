package net.ooder.web.vfs;

import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.annotation.MethodChinaName;
import net.ooder.common.ContextType;
import net.ooder.common.TokenType;
import net.ooder.common.md5.MD5InputStream;
import net.ooder.config.ListResultModel;
import net.ooder.config.ResultModel;
import net.ooder.vfs.FileObject;
import net.ooder.vfs.adapter.FileAdapter;
import net.ooder.vfs.service.VFSStoreService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@EsbBeanAnnotation(dataType = ContextType.Action, tokenType = TokenType.admin)
public class VFSStoreServiceLocal implements VFSStoreService {


    @Override
    @MethodChinaName(cname = "获取HASH映射")
    public @ResponseBody
    ResultModel<FileObject> getFileObjectByHash(String hash) {
        ResultModel<FileObject> fileObjectResultModel = new ResultModel<>();
        FileObject fileObject = getLocalService().getFileObjectByHash(hash);
        fileObjectResultModel.setData(fileObject);
        return fileObjectResultModel;
    }

    @Override
    @MethodChinaName(cname = "增量写入")
    public @ResponseBody
    ResultModel<Integer> writeLine(String fileObjectId, String json) {
        return new ResultModel<Integer>();
    }


    @Override
    public ResultModel<List<String>> readLine(String fileObjectId, List<Integer> lines) {
        ResultModel<List<String>> resultModel = new ResultModel<>();
        return resultModel;
    }


    @Override
    @MethodChinaName(cname = "下载")
    public ResultModel<InputStream> downLoadByHash(String hash) {
        ResultModel<InputStream> resultModel = new ResultModel<>();
        MD5InputStream md5InputStream = this.getLocalService().downLoadByHash(hash);
        if (md5InputStream != null) {
            resultModel.setData(md5InputStream);
        }
        return resultModel;
    }


    @Override
    @MethodChinaName(cname = "文件适配器")
    public @ResponseBody
    ResultModel<FileAdapter> getFileAdapter() {

        return new ResultModel<>();
    }

    @Override
    @MethodChinaName(cname = "删除HASH")
    public @ResponseBody
    ResultModel<Boolean> deleteFileObject(String ID) {
        this.getLocalService().delete(ID);
        return new ResultModel<>();
    }

    @Override
    @MethodChinaName(cname = "更新HASH信息")
    public @ResponseBody
    ResultModel<Boolean> updateFileObject(@RequestBody FileObject fileObject) {
        return new ResultModel<>();
    }

    @Override
    @MethodChinaName(cname = "获取文件信息")
    public @ResponseBody
    ResultModel<FileObject> getFileObjectByID(String id) {
        return this.getFileObjectByHash(id);
    }

    @Override
    @MethodChinaName(cname = "创建文件对象")
    public @ResponseBody
    ResultModel<FileObject> createFileObject(@RequestParam("file") MultipartFile file) {
        ResultModel<FileObject> resultModel = new ResultModel<>();
        try {
            MD5InputStream md5InputStream = new MD5InputStream(file.getInputStream());
            FileObject fileObject = this.getLocalService().createFileObject(md5InputStream);
            resultModel.setData(fileObject);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return resultModel;

    }


    @Override
    @MethodChinaName(cname = "装载文件信息")
    public @ResponseBody
    ListResultModel<List<FileObject>> loadFileObjectList(@RequestBody String[] ids) {
        ListResultModel<List<FileObject>> resultModel = new ListResultModel<List<FileObject>>();
        List<FileObject> fileObjects = new ArrayList<>();
        for (String hash : ids) {
            FileObject fileObject = getLocalService().getFileObjectByHash(hash);
            fileObjects.add(fileObject);
        }
        resultModel.setData(fileObjects);

        return resultModel;
    }

    public LocalVFSManager getLocalService() {
        return LocalVFSManager.getInstance();
    }

}
