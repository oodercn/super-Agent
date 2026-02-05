package net.ooder.editor.toolbox.file;

import net.ooder.editor.toolbox.file.service.OODPackageService;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.field.FileUploadAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.StretchType;

@FormAnnotation(stretchHeight = StretchType.last)
public class UPLoadOODFile {

    @CustomAnnotation(pid = true, hidden = true)
    String domainId;

    @CustomAnnotation(pid = true, hidden = true)
    String packageName;

    @FileUploadAnnotation(bindClass = OODPackageService.class)
    @FieldAnnotation(componentType = ComponentType.FILEUPLOAD, haslabel = false, colSpan = -1, required = true)
    @CustomAnnotation(caption = "上传文件")
    String thumbnailFile;

    public UPLoadOODFile() {

    }

    public UPLoadOODFile(String domainId, String packageName) {
        this.domainId = domainId;
        this.packageName = packageName;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getThumbnailFile() {
        return thumbnailFile;
    }

    public void setThumbnailFile(String thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }
}
