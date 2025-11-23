package src;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.FragmentConfiguration;

public class Main {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        WebAppContext ctx = new WebAppContext();
        ctx.setResourceBase("webapp");
        ctx.setContextPath("/");

        ctx.setConfigurations(new Configuration[] {
                new AnnotationConfiguration(),
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new MetaInfConfiguration(),
                new FragmentConfiguration()
        });

        ctx.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
                ".*/classes/.*|.*spring-web.*");

        ctx.getServerClassMatcher().exclude(
                "org.eclipse.jetty.servlet.listener.IntrospectorCleaner",
                "org.eclipse.jetty.servlet.DefaultServlet",
                "org.eclipse.jetty.servlet.NoJspServlet");

        ctx.setParentLoaderPriority(true);

        server.setHandler(ctx);
        server.start();
        server.join();
    }
}
