package br.com.sismedicina.gestor.auth;

import br.com.sismedicina.gestor.auth.model.ERole;
import br.com.sismedicina.gestor.auth.model.Role;
import br.com.sismedicina.gestor.auth.model.User;
import br.com.sismedicina.gestor.auth.repositorio.RoleRepositorio;
import br.com.sismedicina.gestor.auth.repositorio.UserRepositorio;
import br.com.sismedicina.gestor.auth.request.LoginRequest;
import br.com.sismedicina.gestor.auth.request.SignupRequest;
import br.com.sismedicina.gestor.auth.response.JwtResponse;
import br.com.sismedicina.gestor.chat.response.MessageResponse;
import br.com.sismedicina.gestor.security.jwt.JwtUtils;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import br.com.sismedicina.gestor.tecnico.model.Tecnico;
import br.com.sismedicina.gestor.tecnico.repositorio.TecnicoRepositorio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TecnicoRepositorio tecnicoRepositorio;

    @Autowired
    UserRepositorio userRepositorio;

    @Autowired
    RoleRepositorio roleRepositorio;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Long tecnicoId = tecnicoRepositorio.findByUserId(userDetails.getId()).map(Tecnico::getId).orElse(null);

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getCadastroCompleto(),
                roles,tecnicoId));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepositorio.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username já está em uso!"));
        }

        if (userRepositorio.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email já está em uso!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));
        user.setNome(signUpRequest.getNome());
        user.setTelefone(signUpRequest.getTelefone());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepositorio.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role não encontrada."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepositorio.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role não encontrada."));
                        roles.add(adminRole);
                        user.setCadastroCompleto(true);

                        break;
                    case "tecnico":
                        Role modRole = roleRepositorio.findByName(ERole.ROLE_TECNICO)
                                .orElseThrow(() -> new RuntimeException("Error: Role não encontrada."));
                        roles.add(modRole);
                        user.setCadastroCompleto(false);

                        break;
                    default:
                        Role userRole = roleRepositorio.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role não encontrada."));
                        roles.add(userRole);
                        user.setCadastroCompleto(true);
                }
            });
        }

        user.setRoles(roles);
        userRepositorio.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuário registrado com sucesso!"));
    }
}
