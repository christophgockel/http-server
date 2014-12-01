package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.Logger;
import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LogResponderTest {
  private LogResponder responder;

  @Before
  public void setup() {
    responder = new LogResponder();
  }

  @Test
  public void respondsToLogsPath() {
    assertTrue(responder.respondsTo(RequestMethod.GET, "/logs"));
    assertTrue(responder.respondsTo(RequestMethod.PUT, "/logs"));
    assertTrue(responder.respondsTo(RequestMethod.HEAD, "/logs"));
  }

  @Test
  public void simpleGETasksForAuthentication() throws IOException {
    String content = "GET /logs HTTP/1.1\r\n";
    Request request = RequestHelper.requestFor(content);
    Response response = responder.respond(request);

    assertEquals(StatusCode.UNAUTHORIZED, response.getStatus());
    assertTrue(response.getHeaders().containsKey("WWW-Authenticate"));
    assertContains(response, "Authentication required");
  }

  @Test
  public void validatesCredentials() {
    String content = createAuthenticatedLogRequest();
    Request request = RequestHelper.requestFor(content);

    Response response = responder.respond(request);
    assertEquals(StatusCode.OK, response.getStatus());
  }

  @Test
  public void showsLogEntriesWhenAuthenticated() throws IOException {
    String content = createAuthenticatedLogRequest();
    Request request = RequestHelper.requestFor(content);
    Request logRequest = RequestHelper.requestFor("GET /something HTTP/1.1");
    Logger.log(logRequest);

    Response response = responder.respond(request);
    assertContains(response, "GET /something HTTP/1.1");
  }

  private String createAuthenticatedLogRequest() {
    String encodedCredentials = Base64.encodeBase64String(("admin:hunter2").getBytes());
    return "GET /logs HTTP/1.1\r\n" +
           "Authorization: Basic " + encodedCredentials + "\r\n\r\n";
  }

  private void assertContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), containsString(expected));
  }
}
