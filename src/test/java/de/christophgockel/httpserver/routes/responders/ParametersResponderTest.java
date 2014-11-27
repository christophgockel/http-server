package de.christophgockel.httpserver.routes.responders;

import de.christophgockel.httpserver.RequestMethod;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;

public class ParametersResponderTest {
  private ParametersResponder responder;

  @Before
  public void setup() {
    responder = new ParametersResponder();
  }

  @Test
  public void respondsToGetRequestsOnSpecificRoute() {
    assertTrue(responder.respondsTo(RequestMethod.GET, "/parameters"));
  }

  @Test
  public void putsParametersIntoTheBody() throws IOException {
    String content = "GET /parameters?someparam HTTP/1.1\r\n";
    Response response = responder.respond(RequestHelper.requestFor(content));

    assertContains(response, "someparam");
  }

  @Test
  public void decodesURIParameterCodes() throws IOException {
    String content = "GET /parameters?var=val1%20val2 HTTP/1.1\r\n";
    Response response = responder.respond(RequestHelper.requestFor(content));

    assertContains(response, "var = val1 val2");
  }

  @Test
  public void parsesMultipleParameters() throws IOException {
    String content = "GET /parameters?var1=val1&var2=val2 HTTP/1.1\r\n";
    Response response = responder.respond(RequestHelper.requestFor(content));

    assertContains(response, "var1 = val1");
    assertContains(response, "var2 = val2");
  }

  @Test
  public void displaysMessageWhenNoParametersGiven() throws IOException {
    String content = "GET /parameters HTTP/1.1\r\n";
    Response response = responder.respond(RequestHelper.requestFor(content));

    assertContains(response, "No Parameters");
  }

  private void assertContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), containsString(expected));
  }
}
