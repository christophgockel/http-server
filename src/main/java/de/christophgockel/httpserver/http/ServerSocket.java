package de.christophgockel.httpserver.http;

import java.io.IOException;

public interface ServerSocket {
  public ClientSocket accept() throws IOException;
  public void close() throws IOException;
}
