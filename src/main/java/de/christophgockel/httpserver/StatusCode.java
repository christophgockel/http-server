package de.christophgockel.httpserver;

public enum StatusCode {
  OK(200, "OK"),
  NOT_FOUND(404, "Not Found"),
  NOT_IMPLEMENTED(501, "Not Implemented");

  private int code;
  private String message;

  private StatusCode(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public int getCode() {
    return code;
  }
}
