package com.proyectoL.softgold.security;

import com.proyectoL.softgold.model.Usuario;
import com.proyectoL.softgold.repository.UsuarioDAO;
import com.proyectoL.softgold.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UsuarioDAO usuarioDAO;

        @Autowired
        private UsuarioService usuarioService;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                System.out.println("Buscando usuario con email: " + email);

                Usuario usuario = usuarioDAO.findByEmail(email)
                                .orElseThrow(() -> {
                                        System.out.println("Usuario no encontrado con email: " + email);
                                        return new UsernameNotFoundException("Usuario no encontrado");
                                });

                System.out.println("Usuario encontrado: " + usuario.getEmail());
                System.out.println("Contraseña almacenada: " + usuario.getPassword());
                System.out.println("Roles del usuario: " + usuario.getRoles());

                usuario.getRoles().forEach(rol -> {
                        System.out.println("Nombre del rol: [" + rol.getNombre() + "]");
                });

                // Aquí devolvemos un User con estados personalizados
                return new org.springframework.security.core.userdetails.User(
                                usuario.getEmail(),
                                usuario.getPassword(),
                                true, // enabled
                                true, // accountNonExpired
                                true, // credentialsNonExpired
                                usuarioService.isAccountNonLocked(usuario), // accountNonLocked
                                usuario.getRoles().stream()
                                                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                                                .collect(Collectors.toList()));
        }
}
