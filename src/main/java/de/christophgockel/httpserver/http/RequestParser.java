package de.christophgockel.httpserver.http;

import de.christophgockel.httpserver.RequestMethod;

import java.io.*;
import java.net.URLDecoder;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RequestParser {
  private Request request;
  private Map<String, String> headers;

  public Request parse(InputStream input) {
    request = new Request();
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));

    try {
      parseRequestLine(reader);
      parseHeaders(reader);
      parseBody(reader);
    } catch (IOException e) {
      throw new MalformedException("Invalid request", e);
    }

    return request;
  }

  private void parseRequestLine(BufferedReader reader) throws IOException {
    String requestLine = reader.readLine();

    try {
      String[] parts = requestLine.trim().split("[ ]");

      request.setMethod(RequestMethod.forValue(parts[0]));

      parseURI(parts[1]);
    } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
      throw new MalformedException("Invalid request: '" + requestLine + "'", e);
    }
  }

  private void parseURI(String uri) {
    if (uri.contains("?")) {
      String[] parts = uri.split("\\?");
      request.setURI(parts[0]);
      parseParameters(parts[1]);
    } else {
      request.setURI(uri);
    }
  }

  private void parseParameters(String queryString) {
    Scanner definitionScanner = new Scanner(queryString);
    definitionScanner.useDelimiter("&");
    Map<String, String> parameters = new HashMap<>();

    while (definitionScanner.hasNext()) {
      String definition = definitionScanner.next();

      String name;
      String value;

      if (definition.contains("=")) {
        String[] parts = definition.split("=");
        name = parts[0];
        value = decode(parts[1]);
      } else {
        name = definition;
        value = "";
      }

      parameters.put(name, value);
    }

    request.setParameters(parameters);
  }

  private String decode(String value) {
    try {
      return URLDecoder.decode(value, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      return "";
    }
  }

  private void parseHeaders(BufferedReader reader) throws IOException {
    headers = new HashMap<>();
    String line = reader.readLine();
    KeyValuePair header;

    while (isValidLine(line)) {
      header = parseHeaderLine(line);

      headers.put(header.getKey(), header.getValue());
      line = reader.readLine();
    }

    request.setHeaders(headers);
  }

  private boolean isValidLine(String line) {
    return line != null && !line.trim().equals("");
  }

  private KeyValuePair parseHeaderLine(String headerLine) {
    try {
      String[] parts = headerLine.split(":", 2);

      String header  = parts[0].trim();
      String content = parts[1].trim();

      return new KeyValuePair(header, content);
    } catch (ArrayIndexOutOfBoundsException e) {
      throw new MalformedException("Malformed request header: '" + headerLine + "'", e);
    }
  }

  private void parseBody(BufferedReader reader) throws IOException {
    String body = "";

    if (hasContentLength()) {
      CharBuffer buffer = CharBuffer.allocate(getContentLength());
      reader.read(buffer);

      for (char s : buffer.array()) {
        body += s;
      }

      if (!body.equals("") && !body.substring(body.length() - 1).equals("\n")) {
        body += "\n";
      }
    }

    request.setBody(body);
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

  private class KeyValuePair {
    private final String key;
    private final String value;

    KeyValuePair(String key, String value) {
      this.key = key;
      this.value = value;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }
  }
}
