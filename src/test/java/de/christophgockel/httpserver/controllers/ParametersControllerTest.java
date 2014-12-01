package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class ParametersControllerTest {
  private ParametersController controller;

  @Before
  public void setup() {
    controller = new ParametersController();
  }

  @Test
  public void returnsParametersInTheBody() throws IOException {
    String content = "GET /parameters?someparam HTTP/1.1\r\n";
    Response response = controller.dispatch(RequestHelper.requestFor(content));

    assertContains(response, "someparam");
  }

  @Test
  public void decodesURIParameterCodes() throws IOException {
    String content = "GET /parameters?var=val1%20val2 HTTP/1.1\r\n";
    Response response = controller.dispatch(RequestHelper.requestFor(content));

    assertContains(response, "var = val1 val2");
  }

  @Test
  public void parsesMultipleParameters() throws IOException {
    String content = "GET /parameters?var1=val1&var2=val2 HTTP/1.1\r\n";
    Response response = controller.dispatch(RequestHelper.requestFor(content));

    assertContains(response, "var1 = val1");
    assertContains(response, "var2 = val2");
  }

  @Test
  public void displaysMessageWhenNoParametersGiven() throws IOException {
    String content = "GET /parameters HTTP/1.1\r\n";
    Response response = controller.dispatch(RequestHelper.requestFor(content));

    assertContains(response, "No Parameters");
  }

  private void assertContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), containsString(expected));
  }
}
