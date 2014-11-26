package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class OptionsResponderTest {
  private OptionsResponder responder;

  @Before
  public void setup() {
    responder = new OptionsResponder();
  }

  @Test
  public void respondsToNoParticularResource() {
    assertTrue(responder.respondsTo(RequestMethod.OPTIONS, "/"));
  }

  @Test
  public void doesNotRespondToNonOptionsRequests() {
    assertFalse(responder.respondsTo(RequestMethod.GET, "/"));
  }

  @Test
  public void respondsWith200OK() throws IOException {
    Request request = RequestHelper.requestFor("OPTIONS * HTTP/1.1");

    assertEquals(StatusCode.OK, responder.respond(request).getStatus());
  }

  @Test
  public void responseContainsAllowedMethods() throws IOException {
    Request request = RequestHelper.requestFor("OPTIONS * HTTP/1.1");

    assertTrue(responder.respond(request).getHeaders().containsKey("Allow"));
  }
}
