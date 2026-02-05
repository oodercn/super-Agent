package net.ooder.editor.console.java;

import net.ooder.common.util.IOUtility;
import net.ooder.dsm.editor.action.BaseEditorTools;
import net.ooder.esd.annotation.CustomAnnotation;
import net.ooder.esd.annotation.FieldAnnotation;
import net.ooder.esd.annotation.FormAnnotation;
import net.ooder.esd.annotation.field.JavaEditorAnnotation;
import net.ooder.esd.annotation.field.ToolBarMenu;
import net.ooder.esd.annotation.ui.ComponentType;
import net.ooder.esd.annotation.ui.HAlignType;
import net.ooder.esd.annotation.ui.StretchType;
import net.ooder.esd.dsm.enums.DSMType;
import net.ooder.esd.dsm.java.JavaSrcBean;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Java源代码编辑器类
 * 负责Java源代码的编辑和展示功能
 */
@ToolBarMenu(hAlign = HAlignType.left, menuClasses = BaseEditorTools.class)
@FormAnnotation(stretchHeight = StretchType.last)
public class JavaSourceEditor {

    @CustomAnnotation(uid = true, hidden = true)
    String filePath;

    @CustomAnnotation(pid = true, hidden = true)
    String dsmId;

    @CustomAnnotation(pid = true, hidden = true)
    DSMType dsmType;

    @CustomAnnotation(pid = true, hidden = true)
    String javaTempId;

    @CustomAnnotation(pid = true, hidden = true)
    String methodName;

    @CustomAnnotation(pid = true, hidden = true)
    String sourceClassName;

    @JavaEditorAnnotation
    @FieldAnnotation(required = true, haslabel = false, colSpan = -1, componentType = ComponentType.JAVAEDITOR)
    @CustomAnnotation()
    String content;

    /**
     * 空构造方法
     */
    public JavaSourceEditor() {

    }

    /**
     * 带JavaSrcBean参数的构造方法
     * @param javaSrcBean Java源代码文件对象
     */
    public JavaSourceEditor(JavaSrcBean javaSrcBean) {

        try {
            FileInputStream javaFileStream = new FileInputStream(javaSrcBean.getFile());
            this.dsmId = javaSrcBean.getDsmId();
            this.dsmType = javaSrcBean.getDsmType();
            this.javaTempId = javaSrcBean.getJavaTempId();
            this.methodName = javaSrcBean.getMethodName();
            this.sourceClassName = javaSrcBean.getSourceClassName();
            this.filePath = javaSrcBean.getFile().getAbsolutePath();
            this.content = IOUtility.toString(javaFileStream, "utf-8");
            IOUtility.shutdownStream(javaFileStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSourceClassName() {
        return sourceClassName;
    }

    public void setSourceClassName(String sourceClassName) {
        this.sourceClassName = sourceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDsmId() {
        return dsmId;
    }

    public void setDsmId(String dsmId) {
        this.dsmId = dsmId;
    }

    public DSMType getDsmType() {
        return dsmType;
    }

    public void setDsmType(DSMType dsmType) {
        this.dsmType = dsmType;
    }

    public String getJavaTempId() {
        return javaTempId;
    }

    public void setJavaTempId(String javaTempId) {
        this.javaTempId = javaTempId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
