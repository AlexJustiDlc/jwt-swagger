package com.justiniano.api.security.controller;

import com.justiniano.api.security.jwt.JwtProvider;
import com.justiniano.api.security.model.Rol;
import com.justiniano.api.security.model.Usuario;
import com.justiniano.api.security.service.UsuarioService;
import com.justiniano.api.security.util.JwtDto;
import com.justiniano.api.security.util.LoginDto;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

@CrossOrigin
@RestController
@RequestMapping("/auth")
@Api
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> create(@RequestBody Usuario usuario){
        if (usuarioService.existsByUsername(usuario.getUsername()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El Nombre de Usuario \""+usuario.getUsername()+"\" ya existe.");
        if (usuarioService.existsByCorreo(usuario.getCorreo()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El Correo \""+usuario.getCorreo()+"\" ya existe.");

        /* AGREGAR ROL "USER" */
        Set<Rol> rol = new HashSet<>();
        Rol rolU = new Rol();
        rolU.setId(2);
        rol.add(rolU);
        usuario.setRoles(rol);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.create(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> Login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        JwtDto jwtDto = new JwtDto(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(jwtDto);
    }

    @ApiIgnore
    @PostMapping("/refresh")
    public ResponseEntity<JwtDto> refresh(@RequestBody JwtDto jwtDto) throws ParseException {
        String token = jwtProvider.refreshToken(jwtDto);
        JwtDto jwt = new JwtDto(token);
        return ResponseEntity.status(HttpStatus.OK).body(jwt);
    }

    //@ApiIgnore
    //@GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.getAll());
    }
}
