package de.christophgockel.httpserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) {
    ServerSocket socket;
    Socket client;
    DataOutputStream out;

    try {
      socket = new ServerSocket(5000);

      while ((client = socket.accept()) != null) {
        out = new DataOutputStream(client.getOutputStream());

        out.writeBytes("HTTP/1.1 200 OK\r\n\r\n");

        out.close();
        client.close();
      }

      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
