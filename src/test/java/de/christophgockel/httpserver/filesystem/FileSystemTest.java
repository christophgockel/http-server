package de.christophgockel.httpserver.filesystem;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FileSystemTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();

  @Before
  public void setup() {
  }

  @Test
  public void readsTheRootDirectory() throws IOException {
    FileSystem fs = new FileSystem(documentRoot.getRoot());
    List<File> listing = new ArrayList<>();

    listing.add(documentRoot.newFile("file_1.txt"));
    listing.add(documentRoot.newFile("file_2.txt"));
    listing.add(documentRoot.newFile("file_3.txt"));

    assertEquals(listing, fs.getFiles());
  }

  @Test
  public void knowsAllAvailableFiles() throws IOException {
    FileSystem fs = new FileSystem(documentRoot.getRoot());

    documentRoot.newFile("file_1.txt");
    documentRoot.newFile("file_2.txt");
    documentRoot.newFolder("sub");
    documentRoot.newFile("sub/file_3.txt");

    List<String> files = new ArrayList<>();
    files.add("file_1.txt");
    files.add("file_2.txt");
    files.add("sub/file_3.txt");

    assertEquals(files, fs.getAvailableFiles());
  }
}
