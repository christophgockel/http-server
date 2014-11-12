package de.christophgockel.httpserver.http;

import de.christophgockel.httpserver.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

public class Request {
  private RequestMethod method;
  private String              uri;
  private Map<String, String> headers;
  private String              body;

  public Request(InputStream input) {
    headers = new HashMap<>();
    body = "";
    parseRequest(new BufferedReader(new InputStreamReader(input)));
  }

  public RequestMethod getMethod() {
    return method;
  }

  public String getURI() {
    return uri;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public String getBody() {
    return body;
  }

  private void parseRequest(BufferedReader reader) {
    try {
      parseRequestLine(reader);
      parseHeaders(reader);
      parseBody(reader);
    } catch (IOException e) {
      throw new MalformedException("Invalid request", e);
    }
  }

  private void parseRequestLine(BufferedReader reader) throws IOException {
    String requestLine = reader.readLine();

    try {
      String[] parts = requestLine.trim().split("[ ]");

      method = RequestMethod.forValue(parts[0]);
      uri = parts[1];
    } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
      throw new MalformedException("Invalid request: '" + requestLine + "'", e);
    }
  }

  private void parseHeaders(BufferedReader reader) throws IOException {
    String line = reader.readLine();

    while (isValidLine(line)) {
      parseHeaderLine(line);
      line = reader.readLine();
    }
  }

  private boolean isValidLine(String line) {
    return line != null && !line.trim().equals("");
  }

  private void parseHeaderLine(String headerLine) {
    try {
      String[] parts = headerLine.split(":", 2);

      String header  = parts[0].trim();
      String content = parts[1].trim();

      headers.put(header, content);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new MalformedException("Malformed request header: '" + headerLine + "'", e);
    }
  }

  private void parseBody(BufferedReader reader) throws IOException {
    if (hasContentLength()) {
      body = "";
      CharBuffer buffer = CharBuffer.allocate(getContentLength());
      reader.read(buffer);

      for (char s : buffer.array()) {
        body += s;
      }
    }
  }

  private boolean hasContentLength() {
    return headers.containsKey("Content-Length");
  }

  private int getContentLength() {
    return Integer.parseInt(headers.get("Content-Length"));
  }

  public static class MalformedException extends RuntimeException {
    public MalformedException(String message, Throwable previous) {
      super(message, previous);
    }
  }
}