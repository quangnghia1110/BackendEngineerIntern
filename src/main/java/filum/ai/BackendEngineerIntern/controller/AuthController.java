package filum.ai.BackendEngineerIntern.controller;

import filum.ai.BackendEngineerIntern.model.payload.request.LoginRequest;
import filum.ai.BackendEngineerIntern.model.payload.request.RegisterRequest;
import filum.ai.BackendEngineerIntern.model.payload.response.DataResponse;
import filum.ai.BackendEngineerIntern.security.UserPrinciple;
import filum.ai.BackendEngineerIntern.service.AuthService;
import filum.ai.BackendEngineerIntern.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<DataResponse<String>> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok(
                DataResponse.<String>builder()
                        .status("success")
                        .message("User registered successfully")
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<DataResponse<String>> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(((UserPrinciple) authentication.getPrincipal()).getUsername());

        return ResponseEntity.ok(
                DataResponse.<String>builder()
                        .status("success")
                        .message("Login successful")
                        .data(token)
                        .build()
        );
    }
}
