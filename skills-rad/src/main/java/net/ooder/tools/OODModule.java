package net.ooder.tools;

import java.util.ArrayList;
import java.util.List;

public class OODModule {

    String conf = "{imageWidth:64,imageWidth:64}";

    boolean OK = true;

    String content;

    String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    List<OODFile> files = new ArrayList<OODFile>();

    public String getContent() {

        return content;
    }

    public boolean isOK() {
        return OK;
    }

    public void setOK(boolean OK) {
        this.OK = OK;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public OODModule() {
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public List<OODFile> getFiles() {
        return files;
    }

    public void setFiles(List<OODFile> files) {
        this.files = files;
    }
}
