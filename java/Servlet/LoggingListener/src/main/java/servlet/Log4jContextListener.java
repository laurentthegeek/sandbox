package servlet;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log4jContextListener implements ServletContextListener {

    private static final Logger log = Logger.getLogger(Log4jContextListener.class);
    public static final String LOG4J_CONFIG = "log4j.configuration";
    public static final String LOG4J_WATCH = "log4j.watch";
    public static final String ENV_BASE = "java:comp/env/";
    public static final String ENV_LOG4J_CONFIG = ENV_BASE + LOG4J_CONFIG;
    public static final String ENV_LOG4J_WATCH = ENV_BASE + LOG4J_WATCH;

    private void dumpJndi(Context ctx, String entry) {
        try {
            NamingEnumeration names = ctx.listBindings(entry);
            while (names.hasMore()) {
                Binding b = (Binding) names.next();
                System.out.printf("%s='%s' (%s)\n", b.getName(), b.getObject(), b.getClassName());
            }
        } catch (NamingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private <T> T lookupJndi(Context ctx, String entry) {
        try {
            return (T) ctx.lookup(entry);
        } catch (NamingException ex) {
            throw new RuntimeException(String.format("missing JDNI entry '%s'", entry), ex);
        } catch (ClassCastException ex) {
            throw new RuntimeException(String.format("invalid JDNI entry '%s'", entry), ex);
        }
    }

    private String getProperty(ServletContext ctx, String entry, String defaultValue) {
        String key = ctx.getInitParameter(entry);
        if (key == null) {
            if (defaultValue == null) {
                throw new RuntimeException(String.format("missing Servlet context-param '%s'", key));
            } else {
                return defaultValue;
            }
        }

        String value = System.getProperty(key);
        if (value == null) {
            if (defaultValue == null) {
                throw new RuntimeException(String.format("missing Java properties '%s'", key));
            } else {
                return defaultValue;
            }
        }

        return value;
    }

//    private URL getConfigUrl(Context ctx) {
//        String location = lookup(ctx, ENV_LOG4J_URL);
//        try {
//            return new URL(location);
//        } catch (MalformedURLException ex) {
//            throw new RuntimeException(String.format(
//                    "invalid JNDI entry '%s': could not parse '%s'", ENV_LOG4J_URL, location), ex);
//        }
//    }

    private void initLog4j(String filename, boolean watch) {
        BasicConfigurator.resetConfiguration();
        if (watch) {
            PropertyConfigurator.configureAndWatch(filename);
        } else {
            PropertyConfigurator.configure(filename);
        }
        log.info("Loaded log4j configuration from url: " + filename);
    }

    private void initFromJNDI(Context ctx) throws NamingException {
        dumpJndi(ctx, ENV_BASE);
        String config = lookupJndi(ctx, ENV_LOG4J_CONFIG);
        Boolean watch = lookupJndi(ctx, ENV_LOG4J_WATCH);
        initLog4j(config, watch);
    }

    private void initFromProperties(ServletContext ctx) {
        String config = getProperty(ctx, LOG4J_CONFIG, null);
        Boolean watch = Boolean.parseBoolean(getProperty(ctx, LOG4J_WATCH, "false"));
        initLog4j(config, watch);
    }

    public void contextInitialized(ServletContextEvent evt) {
        try {
            //initFromJNDI(new InitialContext());
            initFromProperties(evt.getServletContext());
        } catch (Throwable t) {
            log.error("could not initialize log4j", t);
            throw new RuntimeException(t);
        }
    }

    public void contextDestroyed(ServletContextEvent evt) {
    }
}
