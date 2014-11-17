package de.christophgockel.httpserver.util;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthenticationTest {
  @Test
  public void knowsWhenCredentialsDontMatch() {
    Authentication auth = new Authentication("name", "password");
    String credentials = base64EncodedCredentials("othername", "otherpassword");

    assertFalse(auth.isAuthenticated(credentials));
  }

  @Test
  public void checksThatCredentialsMatch() {
    Authentication auth = new Authentication("myname", "mypassword");
    String credentials = base64EncodedCredentials("myname", "mypassword");

    assertTrue(auth.isAuthenticated(credentials));
  }

  private String base64EncodedCredentials(String username, String password) {
    return Base64.encodeBase64String((username + ":" + password).getBytes());
  }
}
