package com.ultimate.bank.service;

import com.google.common.hash.Hashing;
import com.ultimate.bank.exception.BadCredentialsException;
import com.ultimate.bank.model.auth.LoginRequest;
import com.ultimate.bank.model.auth.LoginResponse;
import com.ultimate.bank.repository.UserRepository;
import com.ultimate.bank.util.HashUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtEncoder jwtEncoder,
                       BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) {

        var hashedCPF = HashUtil.hashCPF(request.CPF());

        var user = userRepository.findByCPF(hashedCPF);

        if (user.isEmpty() || !user.get().isLoginCorrect(request,
                passwordEncoder) ) {
            throw new BadCredentialsException("Invalid CPF or password.");
        }

        var now = Instant.now();
        var expiresIn = 300L;

        var claims = JwtClaimsSet.builder()
                .issuer("ultimate-bank")
                .subject(HashUtil.hashCPF(user.get().getCPF()))
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
