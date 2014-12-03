package de.christophgockel.httpserver.http;

import java.io.IOException;

public class ServerSocket {
  private final java.net.ServerSocket socket;

  public ServerSocket(java.net.ServerSocket socket) {
    this.socket = socket;
  }
  public ClientSocket accept() throws IOException {
    return new ClientSocket(socket.accept());
  }

  public void close() throws IOException {
    socket.close();
  }
}
