package de.christophgockel.httpserver.http;

import java.io.*;

public class StubClientSocket extends ClientSocket {
  private final InputStream inputStream;
  private final OutputStream outputStream;

  public StubClientSocket(String inputContent) {
    super(null);
    inputStream = new ByteArrayInputStream(inputContent.getBytes());
    outputStream = new ByteArrayOutputStream();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return inputStream;
  }

  @Override
  public OutputStream getOutputStream() throws IOException {
    return outputStream;
  }

  @Override
  public void close() throws IOException {
  }

  public String getContent() {
    return outputStream.toString();
  }
}
