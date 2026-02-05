package net.ooder.editor;


import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.field.ModuleRefFieldAnnotation;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.StretchType;
import net.ooder.esd.dsm.java.JavaSrcBean;
import net.ooder.esd.tool.component.ModuleComponent;

@FormAnnotation(stretchHeight = StretchType.last)
public class OODDesignerForm {

    @ModuleRefFieldAnnotation(src = "RAD.OODDesigner")
    @FieldAnnotation(haslabel = false, colSpan = -1, componentType = ComponentType.MODULE)
    @CustomAnnotation()
    ModuleComponent moduleComponent;

    public OODDesignerForm() {

    }

    public OODDesignerForm(JavaSrcBean javaSrcBean) {

    }

    public ModuleComponent getModuleComponent() {
        return moduleComponent;
    }

    public void setModuleComponent(ModuleComponent moduleComponent) {
        this.moduleComponent = moduleComponent;
    }
}
