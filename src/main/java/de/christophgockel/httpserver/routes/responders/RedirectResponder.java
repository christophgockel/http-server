package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;

public class RedirectResponder extends BaseResponder {
  private static final String LOCALHOST = "localhost";
  private final int defaultPort;

  public RedirectResponder(int defaultPort) {
    this.defaultPort = defaultPort;
  }

  @Override
  protected boolean respondsTo(RequestMethod method, String path) {
    return method == RequestMethod.GET && path.equals("/redirect");
  }

  @Override
  protected Response respond(Request request) {
    Response response = new Response(StatusCode.FOUND);

    response.addHeader("Location", "http://" + getHostAndPort(request) + "/");

    return response;
  }

  private String getHostAndPort(Request request) {
    String host = getHost(request);

    if (!host.contains(":")) {
      host += ":" + defaultPort;
    }

    return host;
  }

  private String getHost(Request request) {
    String host = request.getHeaders().get("Host");

    if (host == null) {
      host = LOCALHOST;
    }

    return host;
  }
}
