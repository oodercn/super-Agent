package net.ooder.editor.console.java;


import net.ooder.esd.dsm.java.JavaPackage;

public class SourcePackage {

    JavaPackage javaPackage;

    String domainId;

    String currentClassName;

    public SourcePackage(JavaPackage javaPackage, String currentClassName, String domainId) {
        this.javaPackage = javaPackage;
        this.currentClassName = currentClassName;
        this.domainId = domainId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getCurrentClassName() {
        return currentClassName;
    }

    public void setCurrentClassName(String currentClassName) {
        this.currentClassName = currentClassName;
    }

    public JavaPackage getJavaPackage() {
        return javaPackage;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof SourcePackage) {
            return ((SourcePackage) obj).getJavaPackage().equals(javaPackage);
        }
        return super.equals(obj);
    }
}
