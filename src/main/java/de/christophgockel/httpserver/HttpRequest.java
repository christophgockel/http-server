package de.christophgockel.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HttpRequest {
  private RequestMethod       method;
  private String              uri;
  private Map<String, String> headers;
  private String              body;

  public HttpRequest(InputStream input) {
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
    String requestLine;
    String headers = "";
    String body = "";

    try {
      requestLine = reader.readLine().trim();
      parseRequestLine(requestLine);

      Scanner scanner = new Scanner(reader);
      scanner.useDelimiter("\r\n");

      while (scanner.hasNext()) {
        String line = scanner.next();
        if (line.matches("^.*?:.*?$")) {
          headers += line + "\n";
        } else {
          body += line + "\n";
        }
      }

      this.headers = parseHeaders(headers);
      this.body = body.trim();
    } catch (NullPointerException | NoSuchElementException | IOException e) {
      throw new MalformedException("Invalid request", e);
    }
  }

  private void parseRequestLine(String requestLine) {
    try {
      String[] parts = requestLine.split("[ ]");

      method = RequestMethod.forValue(parts[0]);
      uri = parts[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new MalformedException("Invalid request: '" + requestLine + "'", e);
    }
  }

  private Map<String, String> parseHeaders(String headersContent) {
    Map<String, String> headers = new HashMap<>();
    Scanner scanner = new Scanner(headersContent.trim());
    scanner.useDelimiter(":|\n");

    try {
      while (scanner.hasNext()) {
        String header = scanner.next().trim();
        String content = scanner.next().trim();

        headers.put(header, content);
      }
    } catch (NoSuchElementException e) {
      throw new MalformedException("Malformed request headers: '" + headersContent + "'", e);
    }

    return headers;
  }

  public static class MalformedException extends RuntimeException {
    public MalformedException(String message, Throwable previous) {
      super(message, previous);
    }
  }
}
