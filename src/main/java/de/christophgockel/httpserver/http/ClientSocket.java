package de.christophgockel.httpserver.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientSocket {
  private final Socket socket;

  public ClientSocket(Socket socket) {
    this.socket = socket;
  }

  public InputStream getInputStream() throws IOException {
    return socket.getInputStream();
  }

  public OutputStream getOutputStream() throws IOException {
    return socket.getOutputStream();
  }

  public void close() throws IOException {
    socket.close();
  }
}
