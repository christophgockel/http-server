package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class OptionsResponderTest {
  private BaseResponder responder;

  @Before
  public void setup() {
    responder = new OptionsResponder();
  }

  @Test
  public void respondsWith200OK() throws IOException {
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    assertThat(responder.handle(request), containsString("200 OK"));
  }

  @Test
  public void responseContainsAllowedMethods() throws IOException {
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    assertThat(responder.handle(request), containsString("Allow:"));
  }
}
