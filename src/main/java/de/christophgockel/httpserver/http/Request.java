package de.christophgockel.httpserver.http;

import de.christophgockel.httpserver.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public class Request {
  private RequestMethod method;
  private String              uri;
  private Map<String, String> parameters;
  private Map<String, String> headers;
  private String              body;

  Request() {
    this.uri = "";
    headers = new HashMap<>();
    parameters = new HashMap<>();
    body = "";
  }

  public RequestMethod getMethod() {
    return method;
  }

  public void setMethod(RequestMethod method) {
    this.method = method;
  }

  public String getURI() {
    return uri;
  }

  public void setURI(String uri) {
    this.uri = uri;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = new HashMap<>(headers);
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public boolean hasParameters() {
    return parameters.size() > 0;
  }

  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, String> parameters) {
    this.parameters = new HashMap<>(parameters);
  }
}
