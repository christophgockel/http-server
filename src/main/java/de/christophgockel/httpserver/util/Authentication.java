package de.christophgockel.httpserver.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.Charset;

public class Authentication {
  private final String username;
  private final String password;

  public Authentication(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public boolean isAuthenticated(String base64EncodedCredentials) {
    String usernameAndPassword = getDecodedCredentials(base64EncodedCredentials);
    String[] credentialParts = splitCredentials(usernameAndPassword);

    return credentialsAreValid(credentialParts[0], credentialParts[1]);
  }

  private String[] splitCredentials(String usernameAndPassword) {
    if (usernameAndPassword.contains(":")) {
      return usernameAndPassword.split("\\:");
    }

    return new String[] {"", ""};
  }

  private String getDecodedCredentials(String base64EncodedCredentials) {
    return new String(Base64.decodeBase64(base64EncodedCredentials.getBytes()), Charset.defaultCharset());
  }


  private boolean credentialsAreValid(String username, String password) {
    return username.equals(this.username) && password.equals(this.password);
  }

}
