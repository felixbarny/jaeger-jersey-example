package jaeger.jersey;

import java.net.URI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

@Path("/")
@Produces(MediaType.TEXT_PLAIN)
public class Frontend {

  final JerseyClient client;

  Frontend(JerseyClient client) {
    this.client = client;
  }

  @GET
  public String callBackend() {
    return client.target(URI.create("http://localhost:9000/api")).request()
        .buildGet().invoke().readEntity(String.class);
  }

  public static void main(String[] args) {
    TraceGraph traceGraph = new TraceGraph("frontend");
    Frontend frontend = new Frontend(new JerseyClientBuilder()
        .register(traceGraph.clientFilter()).build());
    ResourceConfig rc = new ResourceConfig()
        .register(traceGraph.serverFilter())
        .register(frontend);

    GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:8081"), rc);
  }
}
