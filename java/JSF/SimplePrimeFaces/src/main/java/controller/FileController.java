package controller;

import java.io.ByteArrayInputStream;
import javax.enterprise.context.RequestScoped;
import model.Doc;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Named("file")
@RequestScoped
public class FileController {

    @Inject
    FacesContext context;
    @Inject
    private Doc doc;
    private UploadedFile upFile;
    private StreamedContent downFile;

    public boolean getHasFile() {
        return doc.getContents() != null;
    }

    public UploadedFile getUpFile() {
        return upFile;
    }

    public void setUpFile(UploadedFile file) {
        this.upFile = file;
    }

    public StreamedContent getDownFile() {
        return downFile;
    }
    
    public void download() {
        downFile = new DefaultStreamedContent(new ByteArrayInputStream(doc.getContents()),
                doc.getContentType(), doc.getFileName());
    }

    public void upload() {
        doc.setContent(upFile.getFileName(), upFile.getContentType(), upFile.getContents());

        FacesMessage msg = new FacesMessage("Successful",
                String.format("File '%s' uploaded.", doc.getFileName()));
        context.addMessage(null, msg);
    }

    public void clear() {
        doc.clear();

        FacesMessage msg = new FacesMessage("Successful", "File cleared.");
        context.addMessage(null, msg);
    }
}
