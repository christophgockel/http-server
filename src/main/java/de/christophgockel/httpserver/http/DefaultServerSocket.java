package de.christophgockel.httpserver.http;

import java.io.IOException;

public class DefaultServerSocket implements ServerSocket {
  private final java.net.ServerSocket socket;

  public DefaultServerSocket(java.net.ServerSocket socket) {
    this.socket = socket;
  }
  @Override
  public ClientSocket accept() throws IOException {
    return new DefaultClientSocket(socket.accept());
  }

  @Override
  public void close() throws IOException {
    socket.close();
  }
}
