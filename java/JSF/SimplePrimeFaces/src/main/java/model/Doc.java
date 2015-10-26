package model;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class Doc implements Serializable {

    private String fileName;
    private String contentType;
    private byte[] contents;

    public String getContentType() {
        return contentType;
    }

    public byte[] getContents() {
        return contents;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLength() {
        return contents.length;
    }

    public void setContent(String fileName, String contentType, byte[] contents) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.contents = contents;
    }

    public void clear() {
        this.fileName = null;
        this.contentType = null;
        this.contents = null;
    }
}
