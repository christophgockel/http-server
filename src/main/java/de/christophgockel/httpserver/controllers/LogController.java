package de.christophgockel.httpserver.controllers;

import de.christophgockel.httpserver.Logger;
import de.christophgockel.httpserver.StatusCode;
import de.christophgockel.httpserver.http.Request;
import de.christophgockel.httpserver.http.Response;
import de.christophgockel.httpserver.util.HtmlPage;

public class LogController extends Controller {
  @Override
  protected Response get(Request request) {
    Response response = new Response(StatusCode.OK);
    HtmlPage page = new HtmlPage("Log Entries");

    for (String entry : Logger.getEntries()) {
      page.addParagraph(entry);
    }

    response.setBody(page.getContent().getBytes());

    return response;
  }
}
