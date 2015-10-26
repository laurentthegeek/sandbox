package controller;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import model.Doc;

@Named("workflow")
@SessionScoped
public class WorkflowController implements Serializable {

    @Inject
    FacesContext context;
    @Inject
    private Doc doc;

    public boolean getIsProcessReady() {
        return doc.getContents() != null;
    }
    
    public boolean getIsDownloadReady() {
        return doc.getContents() != null;
    }
}