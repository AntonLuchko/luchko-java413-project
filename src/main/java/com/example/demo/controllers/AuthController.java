package com.example.demo.controllers;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;


@Controller
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/vhod")
public String vhod(){
        return "/auth/auth-panel";
}

@PostMapping("register")
public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest){

            AuthResponse response=userService.register(registerRequest);
            String role= jwtService.extractRoles(response.getToken()).stream().findFirst().orElse("ROLE_USER");
            String email= jwtService.extractEmail(response.getToken());
            return ResponseEntity.ok(
                    new AuthResponse(
                            response.getToken(),
                            role,
                            email
                    )
            );


}

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = userService.authenticate(request);
        return ResponseEntity.ok(response);
    }



}
