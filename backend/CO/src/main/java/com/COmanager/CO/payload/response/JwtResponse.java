package com.COmanager.CO.payload.response;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String username;
  private String email;
  private String mobile; // Add the "mobile" field
  private List<String> roles;

  public JwtResponse(String accessToken, Long id, String username, String email, String mobile, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.mobile = mobile; // Set the "mobile" field
    this.roles = roles;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getMobile() {
    return mobile; // Getter for the "mobile" field
  }

  public void setMobile(String mobile) {
    this.mobile = mobile; // Setter for the "mobile" field
  }

  public List<String> getRoles() {
    return roles;
  }
}
