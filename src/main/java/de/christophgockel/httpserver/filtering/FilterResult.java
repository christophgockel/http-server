package de.christophgockel.httpserver.filtering;

import de.christophgockel.httpserver.http.Response;

public class FilterResult {
  private final boolean isValid;
  private final Response rejectionResponse;

  public static FilterResult valid() {
    return new FilterResult();
  }

  public static FilterResult invalidWithResponse(Response response) {
    return new FilterResult(response);
  }

  public boolean isValid() {
    return isValid;
  }

  public Response getRejectionResponse() {
    return rejectionResponse;
  }

  private FilterResult() {
    this.isValid = true;
    this.rejectionResponse = null;
  }

  private FilterResult(Response rejectionResponse) {
    this.isValid = false;
    this.rejectionResponse = rejectionResponse;
  }
}
