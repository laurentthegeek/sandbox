package gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Application
        implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        final Label label = new Label("Hello GWT 2.0!");

        final Button button = new Button("Click me!", new ClickHandler(){

            private int count = 0;

            public void onClick(ClickEvent event) {
                label.setText("You've clicked " + ++count + "times");
            }
        });

        RootPanel.get().add(label);
        RootPanel.get().add(button);
    }
}
