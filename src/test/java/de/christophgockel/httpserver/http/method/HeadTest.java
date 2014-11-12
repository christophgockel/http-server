package de.christophgockel.httpserver.http.method;

import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class HeadTest {
  @Test
  public void returns200OK() {
    Method head = new Head();
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");
    assertThat(head.process(request), containsString("200 OK"));
  }
}
