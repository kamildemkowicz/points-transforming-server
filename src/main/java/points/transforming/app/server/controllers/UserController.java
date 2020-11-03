package points.transforming.app.server.controllers;

import javax.validation.Valid;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import points.transforming.app.server.configurations.security.jwt.JwtProvider;
import points.transforming.app.server.models.user.api.CreateUserRequest;
import points.transforming.app.server.models.user.api.LoginRequest;
import points.transforming.app.server.models.user.api.LoginResponse;
import points.transforming.app.server.models.user.api.ResponseMessage;
import points.transforming.app.server.services.user.UserService;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody final LoginRequest loginRequest) {

        final var authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final var jwt = jwtProvider.generateJwtToken(authentication);
        final var userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(LoginResponse.builder()
            .token(jwt)
            .username(userDetails.getUsername())
            .authorities(userDetails.getAuthorities())
            .build());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserRequest signUpRequest) {
        userService.createUser(signUpRequest);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}
