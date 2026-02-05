package net.ooder.web;

import net.ooder.annotation.EsbBeanAnnotation;
import net.ooder.common.JDSConstants;
import net.ooder.common.JDSException;
import net.ooder.common.expression.function.AbstractFunction;
import net.ooder.common.logging.Log;
import net.ooder.common.logging.LogFactory;
import net.ooder.context.JDSActionContext;
import net.ooder.esd.engine.ESDClient;
import net.ooder.esd.engine.ESDFacrory;
import net.ooder.esd.engine.ProjectVersion;
import net.ooder.esd.manager.plugins.font.node.FontConfig;
import net.ooder.vfs.FileInfo;

import java.util.ArrayList;
import java.util.List;

@EsbBeanAnnotation(expressionArr = "CssFills()", id = "CssFills")
public class CssFills extends AbstractFunction {
    private static final Log logger = LogFactory.getLog(JDSConstants.CONFIG_KEY, CssFills.class);

    public CssFills() {

    }

    public List<FileInfo> perform() {
        String projectName = (String) JDSActionContext.getActionContext().getParams("projectName");
        List<FileInfo> cssFiles = new ArrayList<FileInfo>();
        ProjectVersion version = null;
        try {
            version = getClient().getProjectVersionByName(projectName);
            List<FontConfig> fontNodes = version.getProject().getFonts();
            for (FontConfig fontNode : fontNodes) {
                for (String cssFilePath : fontNode.getListCachePath()) {
                    FileInfo cssFile = getClient().getFileByPath(cssFilePath);
                    if (cssFile.getName().endsWith(".css") && cssFile.getCurrentVersion() != null && cssFile.getCurrentVersion().getLength() > 0) {
                        if (!cssFiles.contains(cssFile)) {
                            cssFiles.add(cssFile);
                        }
                    }
                }
            }
        } catch (JDSException e) {
            e.printStackTrace();
        }
        return cssFiles;
    }


    ESDClient getClient() throws JDSException {
        ESDClient client = ESDFacrory.getAdminESDClient();
        return client;
    }
}
