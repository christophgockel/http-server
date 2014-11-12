package de.christophgockel.httpserver.http.method;

import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class NotImplementedTest {
  @Test
  public void returns501NotImplemented() {
    Method notImplemented = new NotImplemented();
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");

    String response = notImplemented.process(request);

    assertThat(response, containsString("501 Not Implemented"));
  }
}
