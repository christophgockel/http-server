package de.christophgockel.httpserver.util;

public class Arguments {

  private final String[] arguments;

  public Arguments(String[] args) {
    arguments = args;
  }

  public int getPort() {
    try {
      return Integer.parseInt(get("-p"));
    } catch (NumberFormatException e) {
      return 5000;
    }
  }

  public String getDocumentRoot() {
    return get("-d");
  }

  private String get(String parameter) {
    for (int i = 0; i < arguments.length; i++) {
      if (arguments[i].equals(parameter)) {
        return arguments[i + 1];
      }
    }
    return "";
  }
}
