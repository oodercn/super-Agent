package net.ooder.editor.toolbox.file;

import net.ooder.editor.toolbox.file.service.OODPackageService;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.field.FileUploadAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.StretchType;

@FormAnnotation(stretchHeight = StretchType.last)
public class ImportProject {

    @CustomAnnotation(pid = true, hidden = true)
    String domainId;

    @FileUploadAnnotation(bindClass = OODPackageService.class)
    @FieldAnnotation(componentType = ComponentType.FILEUPLOAD, haslabel = false, colSpan = -1, required = true)
    @CustomAnnotation(caption = "上传文件")
    String thumbnailFile;

    public ImportProject() {

    }

    public ImportProject(String domainId, String packageName) {
        this.domainId = domainId;

    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }


    public String getThumbnailFile() {
        return thumbnailFile;
    }

    public void setThumbnailFile(String thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }
}
