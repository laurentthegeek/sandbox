package jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(eager=true)
@RequestScoped
public class MainController {
    
    public MainController() {
        System.err.println ("MainBean starting up!");
    }

    public String getText() {
        return "Here is some text!";
    }
}