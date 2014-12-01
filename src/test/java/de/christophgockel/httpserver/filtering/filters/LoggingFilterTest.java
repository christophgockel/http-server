package de.christophgockel.httpserver.filtering.filters;

import de.christophgockel.httpserver.Logger;
import de.christophgockel.httpserver.helper.RequestHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class LoggingFilterTest {
  private LoggingFilter filter;

  @Before
  public void setup() {
    Logger.clear();
    filter = new LoggingFilter();
  }

  @Test
  public void doesNotDenyTheRequest() {
    assertTrue(filter.filter(RequestHelper.requestFor("GET / HTTP/1.1")));
  }

  @Test
  public void logsEveryRequest() {
    filter.filter(RequestHelper.requestFor("GET / HTTP/1.1"));

    assertEquals("GET / HTTP/1.1", Logger.getEntries().get(0));
  }
}
