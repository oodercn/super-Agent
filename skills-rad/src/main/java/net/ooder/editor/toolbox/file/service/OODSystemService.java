package net.ooder.editor.toolbox.file.service;

import net.ooder.cluster.ServerNode;
import net.ooder.config.TreeListResultModel;
import net.ooder.esd.annotation.event.CustomTreeEvent;
import net.ooder.esd.annotation.field.APIEventAnnotation;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/rad/file/")
public class OODSystemService {

    @APIEventAnnotation(bindTreeEvent = CustomTreeEvent.TREERELOAD)
    @RequestMapping("getServerNodes")
    public TreeListResultModel<List<ServerNode>> getServerNodes() {
        return null;
    }

}
