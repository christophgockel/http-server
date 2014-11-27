package de.christophgockel.httpserver.http;

import java.io.IOException;

public class StubServerSocket implements ServerSocket {
  private boolean closed;
  private boolean alreadyAccepted;
  private final StubSocket clientSocket;

  public StubServerSocket(String clientContent) {
    clientSocket = new StubSocket(clientContent);
    closed = false;
    alreadyAccepted = false;
  }

  @Override
  public ClientSocket accept() throws IOException {
    if (alreadyAccepted) {
      return null;
    }

    alreadyAccepted = true;

    return clientSocket;
  }

  @Override
  public void close() throws IOException {
    closed = true;
  }

  public boolean isClosed() {
    return closed;
  }
}
