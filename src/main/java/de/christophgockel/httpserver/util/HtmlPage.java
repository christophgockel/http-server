package de.christophgockel.httpserver.util;

import java.util.ArrayList;
import java.util.List;

public class HtmlPage {
  private final String title;
  private List<String> paragraphs;

  public HtmlPage(String title) {
    this.title = title;
    this.paragraphs = new ArrayList<>();
  }

  public String getContent() {
    String content = "<html><head><title>" + title + "</title></head><body>";

    for (String paragraph : paragraphs) {
      content += "<p>" + paragraph + "</p>";
    }

    content += "</body></html>";

    return content;
  }

  public void addParagraph(String content) {
    paragraphs.add(content);
  }

  public String createLink(String text, String target) {
    return "<a href=\"" + target + "\">" + text + "</a>";
  }
}
