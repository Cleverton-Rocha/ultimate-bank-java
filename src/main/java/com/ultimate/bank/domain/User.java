package com.ultimate.bank.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ultimate.bank.model.auth.LoginRequest;
import com.ultimate.bank.model.user.CreateUserRequest;
import com.ultimate.bank.model.user.UpdateUserRequest;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Table(name = "users")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 11, nullable = false, unique = true)
    private String CPF;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Account account;

    public void createUser(CreateUserRequest request,
                           BCryptPasswordEncoder passwordEncoder,
                           String hashedCPF) {
        this.CPF = hashedCPF;
        this.name = request.name();
        this.email = request.email();
        this.password = passwordEncoder.encode(request.password());
    }

    public boolean isLoginCorrect(LoginRequest request,
                                  BCryptPasswordEncoder passwordEncoder) {
       return passwordEncoder.matches(request.password(), this.password);
    }

}
