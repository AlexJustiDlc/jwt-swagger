package com.justiniano.api.security.service;

import com.justiniano.api.security.model.Usuario;
import com.justiniano.api.security.model.UsuarioPrincipal;
import com.justiniano.api.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAll(){
        return repository.findAll();
    }

    public Usuario getByUsername(String username){
        return repository.findByUsername(username);
    }

    public boolean existsByUsername(String username){
        return repository.existsByUsername(username);
    }

    public boolean existsByCorreo(String correo){
        return repository.existsByCorreo(correo);
    }

    public String create(Usuario usuario){
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        repository.save(usuario);
        return "Usuario \""+usuario.getUsername()+"\" Creado.";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = getByUsername(username);
        return UsuarioPrincipal.build(usuario);
    }
}
