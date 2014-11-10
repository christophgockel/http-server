package de.christophgockel.httpserver;

public enum RequestMethod {
  GET, PUT, POST, HEAD, DELETE, TRACE, CONNECT, OPTIONS;

  public static RequestMethod forValue(String value) {
    switch (value) {
      case "GET":
        return GET;
      case "POST":
        return POST;
      case "PUT":
        return PUT;
      case "HEAD":
        return HEAD;
      case "DELETE":
        return DELETE;
      case "TRACE":
        return TRACE;
      case "CONNECT":
        return CONNECT;
      case "OPTIONS":
        return OPTIONS;
      default:
        throw new UnknownMethod(value);
    }
  }

  public static class UnknownMethod extends RuntimeException {
    public UnknownMethod(String method) {
      super("Method '" + method + "' is not supported.");
    }
  }
}
