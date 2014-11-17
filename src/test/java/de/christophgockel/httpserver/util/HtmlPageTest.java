package de.christophgockel.httpserver.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

public class HtmlPageTest {
  @Test
  public void hasATitle() {
    HtmlPage page = new HtmlPage("the title");

    assertThat(page.getContent(), containsString("<title>the title</title>"));
  }

  @Test
  public void canHaveParagraphs() {
    HtmlPage page = new HtmlPage("the title");
    page.addParagraph("a paragraph");
    page.addParagraph("another paragraph");

    assertThat(page.getContent(), containsString("<p>a paragraph</p>"));
    assertThat(page.getContent(), containsString("<p>another paragraph</p>"));
  }

  @Test
  public void canCreateLinks() {
    HtmlPage page = new HtmlPage("");

    assertThat(page.createLink("text", "target"), is("<a href=\"target\">text</a>"));
  }

  @Test
  public void rendersAWholeHtmlPage() {
    HtmlPage page = new HtmlPage("page");
    page.addParagraph("some text...");
    String content = page.getContent();

    assertThat(content, containsString("<html>"));
    assertThat(content, containsString("<head>"));
    assertThat(content, containsString("<title>page</title"));
    assertThat(content, containsString("</head>"));
    assertThat(content, containsString("<body>"));
    assertThat(content, containsString("<p>some text...</p>"));
    assertThat(content, containsString("</body>"));
    assertThat(content, containsString("</html>"));
  }
}
