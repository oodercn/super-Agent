package net.ooder.editor.toolbox.file.service;

import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import net.ooder.vfs.FileVersion;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/rad/file/")
public class OODFileVersionService {

    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREERELOAD)
    @RequestMapping("getFileVersions")
    public TreeListResultModel<List<FileVersion>> getFileVersions() {
        return null;
    }

}
