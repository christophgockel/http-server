package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.Logger;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class LogControllerTest {
  private LogController controller;

  @Before
  public void setup() {
    controller = new LogController();
  }

  @Test
  public void showsLogEntries() throws IOException {
    Request request = RequestHelper.requestFor("GET /logs HTTP/1.1");

    Request logRequest = RequestHelper.requestFor("GET /something HTTP/1.1");
    Logger.log(logRequest);

    Response response = controller.dispatch(request);
    assertContains(response, "GET /something HTTP/1.1");
  }

  private void assertContains(Response response, String expected) throws IOException {
    assertThat(new String(response.getFullResponse()), containsString(expected));
  }
}
