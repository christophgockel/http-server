package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RedirectResponderTest {
  @Test
  public void respondsToGetOfSlashRedirect() {
    RedirectResponder responder = new RedirectResponder(1234);

    assertTrue(responder.respondsTo(RequestMethod.GET, "/redirect"));
  }

  @Test
  public void respondsWith302RedirectToRoot() {
    String content = "GET /redirect HTTP/1.1\r\n" +
                     "Host: www.somehost.com\r\n\r\n";
    Request request = RequestHelper.requestFor(content);

    RedirectResponder responder = new RedirectResponder(1234);
    Response response = responder.respond(request);

    assertEquals(StatusCode.FOUND, response.getStatus());
    assertEquals("http://www.somehost.com:1234/", response.getHeaders().get("Location"));
  }

  @Test
  public void redirectsWithPortFromRequest() {
    String content = "GET /redirect HTTP/1.1\r\n" +
                     "Host: www.somehost.com:8080\r\n\r\n";
    Request request = RequestHelper.requestFor(content);

    RedirectResponder responder = new RedirectResponder(1234);
    Response response = responder.respond(request);

    assertEquals(StatusCode.FOUND, response.getStatus());
    assertEquals("http://www.somehost.com:8080/", response.getHeaders().get("Location"));
  }

  @Test
  public void redirectsToLocalhostWhenNoHostIsGivenInRequest() {
    String content = "GET /redirect HTTP/1.0\r\n";
    Request request = RequestHelper.requestFor(content);

    RedirectResponder responder = new RedirectResponder(1234);
    Response response = responder.respond(request);

    assertEquals(StatusCode.FOUND, response.getStatus());
    assertEquals("http://localhost:1234/", response.getHeaders().get("Location"));
  }
}
