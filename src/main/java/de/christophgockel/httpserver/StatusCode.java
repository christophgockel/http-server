package de.christophgockel.httpserver;

public enum StatusCode {
  OK                    (200, "OK"),
  NO_CONTENT            (204, "No Content"),
  NOT_FOUND             (404, "Not Found"),
  PRECONDITION_FAILED   (412, "Precondition Failed"),
  INTERNAL_SERVER_ERROR (500, "Internal Server Error"),
  NOT_IMPLEMENTED       (501, "Not Implemented");

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
