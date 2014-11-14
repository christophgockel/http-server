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
    BaseResponder responder = new RedirectResponder(1234);

    assertTrue(responder.respondsTo(RequestMethod.GET, "/redirect"));
  }

  @Test
  public void respondsWith302RedirectToRoot() {
    String content = "GET /redirect HTTP/1.1";
    Request request = RequestHelper.requestFor(content);

    BaseResponder responder = new RedirectResponder(1234);
    Response response = responder.respond(request);

    assertEquals(StatusCode.FOUND, response.getStatus());
    assertEquals("http://localhost:1234/", response.getHeaders().get("Location"));
  }
}
