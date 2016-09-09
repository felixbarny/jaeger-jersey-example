package jaeger.jersey;

import java.net.URI;
import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

@Path("/api")
@Produces(MediaType.TEXT_PLAIN)
public class Backend {

  @GET
  public String printDate() {
    return new Date().toString();
  }

  public static void main(String[] args) {
    TraceGraph traceGraph = new TraceGraph("backend");
    ResourceConfig rc = new ResourceConfig()
        .register(traceGraph.serverFilter())
        .register(Backend.class);

    GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:9000"), rc);
  }
}
