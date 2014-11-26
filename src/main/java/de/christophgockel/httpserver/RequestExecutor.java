package de.christophgockel.httpserver;

import de.christophgockel.httpserver.http.ClientSocket;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.routes.Router;

import java.io.DataOutputStream;
import java.io.IOException;

public class RequestExecutor implements Runnable {
  private final ClientSocket socket;
  private final Router router;
  private DataOutputStream out;

  public RequestExecutor(ClientSocket socket, Router router) {
    this.socket = socket;
    this.router = router;
  }

  @Override
  public void run() {
    try {
      out = new DataOutputStream(socket.getOutputStream());

      Request request = new Request(socket.getInputStream());
      Logger.log(request);

      out.write(router.dispatch(request).getFullResponse());

      out.close();
      socket.close();
    } catch (Exception e) {
      sendError(e.getMessage());
    }
  }

  private void sendError(String message) {
    try {
      Response response = new Response(StatusCode.INTERNAL_SERVER_ERROR);
      response.setBody(message);
      out.write(response.getFullResponse());
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }
}
