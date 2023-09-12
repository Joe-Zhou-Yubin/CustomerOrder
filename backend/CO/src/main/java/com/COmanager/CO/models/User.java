package com.COmanager.CO.models;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import java.util.Random;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", 
uniqueConstraints = { 
  @UniqueConstraint(columnNames = "username"),
  @UniqueConstraint(columnNames = "email") 
})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userId;
	
	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;
	
	@NotBlank
	private String mobile;

	@NotBlank
	@Size(max = 120)
	private String password;

	@ManyToMany(fetch = FetchType.LAZY)
	  @JoinTable(  name = "user_roles", 
	        joinColumns = @JoinColumn(name = "user_userId"), 
	        inverseJoinColumns = @JoinColumn(name = "role_id"))
	  private Set<Role> roles = new HashSet<>();

    public User() {
        
    }

    public User(String username, String email, String password, String mobile) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.userId = generateUnique8CharString();
      }
    
 // Generate a unique 8-character alphanumeric string for userId using UUID
    private String generateUnique8CharString() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replace("-", "").substring(0, 8);
        return uuidStr;
    }
    
    
      public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getId() {
        return id;
      }

      public void setId(Long id) {
        this.id = id;
      }

      public String getUsername() {
        return username;
      }

      public void setUsername(String username) {
        this.username = username;
      }

      public String getEmail() {
        return email;
      }

      public void setEmail(String email) {
        this.email = email;
      }

      public String getPassword() {
        return password;
      }

      public void setPassword(String password) {
        this.password = password;
      }

      public Set<Role> getRoles() {
        return roles;
      }

      public void setRoles(Set<Role> roles) {
        this.roles = roles;
      }

      @Override
      public String toString() {
          return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", mobile=" + mobile + ", roles=" + roles + "]";
      }
      
}