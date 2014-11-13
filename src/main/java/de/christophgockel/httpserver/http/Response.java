package de.christophgockel.httpserver.http;

import de.christophgockel.httpserver.StatusCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response {
  private final Map<String, String> headers;
  private StatusCode status;
  private byte[] body;

  public Response() {
    this(StatusCode.OK);
  }

  public Response(StatusCode status) {
    this.status = status;
    this.headers = new HashMap<>();
    this.body = new byte[0];
;  }

  public StatusCode getStatus() {
    return status;
  }

  public void addHeader(String name, String value) {
    headers.put(name, value);
  }

  public Map<String,String> getHeaders() {
    return headers;
  }

  public void setBody(byte[] bodyContent) {
    body = bodyContent;
  }

  public byte[] getBody() {
    return body;
  }

  public void setBody(String bodyContent) {
    body = bodyContent.getBytes();
  }

  public byte[] getFullResponse() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    out.write(statusLine());
    out.write(headerLines());
    out.write(lineSeparator());
    out.write(body);

    out.close();

    return out.toByteArray();
  }

  private byte[] statusLine() {
    return ("HTTP/1.1 " + status.getCode() + " " + status.getMessage() + "\r\n").getBytes();
  }

  private byte[] headerLines() {
    String headerLines = "";

    for (Map.Entry<String, String> entry : headers.entrySet()) {
      headerLines += entry.getKey() + ": " + entry.getValue() + "\r\n";
    }

    return headerLines.getBytes();
  }

  private byte[] lineSeparator() {
    return "\r\n".getBytes();
  }
}
