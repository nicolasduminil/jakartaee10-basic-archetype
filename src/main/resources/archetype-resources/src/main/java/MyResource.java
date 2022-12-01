package $package;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.inject.*;

import org.eclipse.microprofile.config.inject.*;

@Path("myresource")
public class MyResource
{
  @Inject
  @ConfigProperty(name = "message")
  private String message;

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String getIt()
  {
    return message;
  }
}
