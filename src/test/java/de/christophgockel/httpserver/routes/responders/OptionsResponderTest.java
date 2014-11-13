package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionsResponderTest {
  private BaseResponder responder;

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

    assertContains(responder.respond(request), "200 OK");
  }

  @Test
  public void responseContainsAllowedMethods() throws IOException {
    Request request = RequestHelper.requestFor("OPTIONS * HTTP/1.1");

    assertContains(responder.respond(request), "Allow:");
  }

  private void assertContains(byte[] actual, String expected) {
    assertThat(new String(actual), containsString(expected));
  }
}
