package de.christophgockel.httpserver.http.method;

import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class PutTest {
  @Test
  public void returns200OK() {
    Method put = new Put();
    Request request = RequestHelper.requestFor("GET / HTTP/1.1");
    assertThat(put.process(request), containsString("200 OK"));
  }
}
