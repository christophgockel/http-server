package de.christophgockel.httpserver.http.method;

import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class OptionTest {
  @Test
  public void returnsAllowHeader() {
    Method options = new Option();
    Request request = RequestHelper.requestFor("OPTIONS * HTTP/1.1");

    String response = options.process(request);

    assertThat(response, containsString("200 OK"));
    assertThat(response, containsString("Allow:"));
  }
}
