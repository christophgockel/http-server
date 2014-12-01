package de.christophgockel.httpserver.filtering;

import de.christophgockel.httpserver.filtering.filters.Filter;
import de.christophgockel.httpserver.helper.RequestHelper;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import org.junit.Test;

import static de.christophgockel.httpserver.StatusCode.NOT_ALLOWED;
import static org.junit.Assert.*;

public class FilterChainTest {
  @Test
  public void filteringWithNoFiltersIsAValidRequest() {
    FilterChain chain = new FilterChain();

    FilterResult result = chain.filter(RequestHelper.requestFor("GET / HTTP/1.1"));

    assertTrue(result.isValid());
  }

  @Test
  public void isValidIfFiltersDeclareItAsValid() {
    FilterChain chain = new FilterChain();
    chain.addFilter(new ValidatingFilter());

    FilterResult result = chain.filter(RequestHelper.requestFor("GET / HTTP/1.1"));

    assertTrue(result.isValid());
  }

  @Test
  public void isNotValidIfFilterMarksRequestAsInvalid() {
    FilterChain chain = new FilterChain();
    chain.addFilter(new InvalidatingFilter());

    FilterResult result = chain.filter(RequestHelper.requestFor("GET / HTTP/1.1"));

    assertFalse(result.isValid());
  }

  @Test
  public void ifRequestIsInvalidARejectionResponseIsAvailable() {
    FilterChain chain = new FilterChain();
    chain.addFilter(new InvalidatingFilter());

    FilterResult result = chain.filter(RequestHelper.requestFor("GET / HTTP/1.1"));

    assertEquals(NOT_ALLOWED, result.getRejectionResponse().getStatus());
  }

  @Test
  public void filtersCanBeResourceSpecific() {
    FilterChain chain = new FilterChain();
    SpyFilter filter = new SpyFilter();
    chain.addFilter("/resource", filter);

    chain.filter(RequestHelper.requestFor("GET /resource HTTP/1.1"));

    assertTrue(filter.filterHasBeenCalled);
  }

  @Test
  public void specificFiltersWorkOnlyOnThePathSpecified() {
    FilterChain chain = new FilterChain();
    SpyFilter filter = new SpyFilter();
    chain.addFilter("/resource", filter);

    chain.filter(RequestHelper.requestFor("GET /some/other/path HTTP/1.1"));

    assertFalse(filter.filterHasBeenCalled);
  }

  private class ValidatingFilter extends Filter {
    @Override
    public boolean filter(Request request) {
      return true;
    }
  }

  private class InvalidatingFilter extends Filter {
    @Override
    public boolean filter(Request request) {
      return false;
    }

    @Override
    public Response getRejectionResponse() {
      return new Response(NOT_ALLOWED);
    }
  }

  private class SpyFilter extends Filter {
    public boolean filterHasBeenCalled = false;
    @Override
    public boolean filter(Request request) {
      filterHasBeenCalled = true;
      return true;
    }
  }
}
