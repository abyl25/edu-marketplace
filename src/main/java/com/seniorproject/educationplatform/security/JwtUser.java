package com.seniorproject.educationplatform.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seniorproject.educationplatform.models.Cart;
import com.seniorproject.educationplatform.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public class JwtUser implements UserDetails {
//    private final User user;
    private final Long id;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final boolean enabled;
//    private final Date lastPasswordResetDate;
    private final Collection<? extends GrantedAuthority> authorities;
    @JsonIgnore
    private final Cart cart;

//    public JwtUser(User user) {
//        this.user = user;
//    }

    public JwtUser(Long id, String userName, String firstName, String lastName, String email, String password, Collection<? extends GrantedAuthority> authorities, boolean enabled, Cart cart) {
//        this.user = user; // User user,
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.cart = cart;
//        this.lastPasswordResetDate = lastPasswordResetDate;
    }

//    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getFirstname() {
        return firstName;
    }

    public String getLastname() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Cart getCart() {
        return cart;
    }

//    @JsonIgnore
//    public Date getLastPasswordResetDate() {
//        return lastPasswordResetDate;
//    }

    @Override
    public String toString() {
        return "JwtUser{" + "id=" + id + ", userName='" + userName + '\'' + ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", password='" + password + '\'' +
                ", enabled=" + enabled + ", authorities=" + authorities + '}';
    }
}
