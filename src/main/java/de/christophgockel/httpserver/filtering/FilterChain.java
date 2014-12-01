package de.christophgockel.httpserver.filtering;

import de.christophgockel.httpserver.filtering.filters.Filter;
import de.christophgockel.httpserver.http.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterChain {
  List<Filter> generalFilters;
  Map<String, Filter> specificFilters;

  public FilterChain() {
    generalFilters = new ArrayList<>();
    specificFilters = new HashMap<>();
  }

  public FilterResult filter(Request request) {
    for (Filter filter : getFilters(request)) {
      if (!filter.filter(request)) {
        return FilterResult.invalidWithResponse(filter.getRejectionResponse());
      }
    }

    return FilterResult.valid();
  }

  private List<Filter> getFilters(Request request) {
    List<Filter> allFilters = new ArrayList<>();

    if (specificFilterIsAvailable(request)) {
      allFilters.add(specificFilterFor(request));
    }

    allFilters.addAll(generalFilters);

    return allFilters;
  }

  private Filter specificFilterFor(Request request) {
    return specificFilters.get(request.getURI());
  }

  private boolean specificFilterIsAvailable(Request request) {
    return specificFilters.containsKey(request.getURI());
  }

  public void addFilter(Filter filter) {
    generalFilters.add(filter);
  }

  public void addFilter(String path, Filter filter) {
    specificFilters.put(path, filter);
  }
}
