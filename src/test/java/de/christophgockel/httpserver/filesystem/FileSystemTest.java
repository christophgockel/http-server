package de.christophgockel.httpserver.filesystem;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileSystemTest {
  @Rule
  public TemporaryFolder documentRoot = new TemporaryFolder();

  @Before
  public void setup() {
  }

  @Test
  public void knowsIfAPathIsAFile() throws IOException {
    FileSystem fs = new FileSystem(documentRoot.getRoot());

    documentRoot.newFile("file_1.txt");

    assertTrue(fs.isFile("file_1.txt"));
  }

  @Test
  public void knowsIfAPathIsADirectory() throws IOException {
    FileSystem fs = new FileSystem(documentRoot.getRoot());

    documentRoot.newFolder("sub");

    assertTrue(fs.isDirectory("sub"));
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

  @Test
  public void listsContentsOfSubdirectories() throws IOException {
    FileSystem fs = new FileSystem(documentRoot.getRoot());
    List<File> listing = new ArrayList<>();

    documentRoot.newFile("file.txt");
    documentRoot.newFolder("sub");
    listing.add(documentRoot.newFile("sub/file_1.txt"));
    listing.add(documentRoot.newFile("sub/file_2.txt"));

    assertEquals(listing, fs.getFiles("sub"));
  }

  @Test
  public void providesFileContents() throws IOException {
    FileSystem fs = new FileSystem(documentRoot.getRoot());

    File file = documentRoot.newFile("file.txt");
    FileWriter fw = new FileWriter(file);
    fw.write("something");
    fw.close();

    assertArrayEquals("something".getBytes(), fs.getFileContent("file.txt"));
  }

  @Test
  public void providesMimeTypeInformation() throws IOException {
    FileSystem fs = new FileSystem(documentRoot.getRoot());

    documentRoot.newFile("file.txt");

    assertEquals("text/plain", fs.getMimeType("file.txt"));
  }

  @Test
  public void writesFileContents() throws IOException {
    documentRoot.newFile("some_file.txt");
    FileSystem fs = new FileSystem(documentRoot.getRoot());

    fs.setFileContent("some_file.txt", "some new content".getBytes());

    assertArrayEquals("some new content".getBytes(), fs.getFileContent("some_file.txt"));
  }

  @Test
  public void providesSHA1HashOfAFile() throws IOException {
    FileSystem fs = new FileSystem(documentRoot.getRoot());

    File file = documentRoot.newFile("file.txt");
    FileWriter fw = new FileWriter(file);
    fw.write("default content\n");
    fw.close();

    assertEquals("60bb224c68b1ed765a0f84d910de58d0beea91c4", fs.getSHA1ForFile("file.txt"));
  }
}
