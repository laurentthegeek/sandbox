package servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public final class MyContextListener implements ServletContextListener {

    private static ServletContext context;

    /**
     * Dummy methods that returns some shared ressource.
     * @return the shared resource.
     */
    public static ServletContext getServletContext() {
        assert context != null;
        if (context == null)
            throw new IllegalStateException();
        
        return context;
    }

    public void contextInitialized(ServletContextEvent event) {
        assert context == null;
        context = event.getServletContext();
        System.out.printf("MyContextListener.contextInitialized: custom initialization of '%s'\n",
                context.getContextPath());
    }

    public void contextDestroyed(ServletContextEvent event) {
        assert context != null;
        System.out.printf("MyContextListener.contextDestroyed: custom termination of '%s'\n",
                context.getContextPath());
        context = null;
    }
}