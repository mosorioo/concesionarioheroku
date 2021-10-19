package com.taller.concesionario.security.controller;

import com.taller.concesionario.dto.Mensaje;
import com.taller.concesionario.security.dto.JwtDto;
import com.taller.concesionario.security.dto.LoginUsuario;
import com.taller.concesionario.security.dto.NuevoUsuario;
import com.taller.concesionario.security.entity.Rol;
import com.taller.concesionario.security.entity.Usuario;
import com.taller.concesionario.security.enums.RolNombre;
import com.taller.concesionario.security.jwt.JwtProvider;
import com.taller.concesionario.security.service.RolService;
import com.taller.concesionario.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import antlr.StringUtils;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    RolService rolService;

    @Autowired
    JwtProvider jwtProvider;

    //Espera un json y lo convierte a tipo clase NuevoUsuario
    //url
    
    @PostMapping("/usuario")
    public ResponseEntity<?> nuevoUsuario(@Valid @RequestBody NuevoUsuario nuevoUsuario,
                                          BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(new Mensaje("Campos mal o email invalido"), HttpStatus.BAD_REQUEST);
        }
        if(usuarioService.existsByUsuario(nuevoUsuario.getNombreUsuario())){
            return new ResponseEntity<>(new Mensaje("Nombre de usuario existente"), HttpStatus.BAD_REQUEST);
        }
        if(usuarioService.existsByDni(nuevoUsuario.getDni())){
            return new ResponseEntity<>(new Mensaje("DNI existente"), HttpStatus.BAD_REQUEST);
        }
        if(usuarioService.existsByEmail(nuevoUsuario.getEmail())){
            return new ResponseEntity<>(new Mensaje("E-mail existente"), HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = new Usuario(nuevoUsuario.getNombreUsuario(), nuevoUsuario.getApellido(), nuevoUsuario.getNombre(), nuevoUsuario.getDni(), 
                nuevoUsuario.getEmail(), passwordEncoder.encode(nuevoUsuario.getPassword()));

        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
        usuario.setRoles(roles);

        usuarioService.save(usuario);

        return new ResponseEntity<>(new Mensaje("Usuario creado"), HttpStatus.CREATED); //Agregar: mostrar los datos del usuario creado
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Mensaje("Campos mal"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(),
                                loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        return new ResponseEntity<>(jwtDto, HttpStatus.OK);
    }

    // Actualizar usuario

    /*
    @PutMapping("/usuario/actualizar/{idUsuario}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable("idUsuario") int idUsuario, @RequestBody NuevoUsuario nuevoUsuario){

        if (!usuarioService.existsByIdUsuario(idUsuario))
        return new ResponseEntity(new Mensaje("No existe el usuario"), HttpStatus.NOT_FOUND);

        if (usuarioService.existsByUsuario(nuevoUsuario.getNombreUsuario())
                && usuarioService.getByUsuario(nuevoUsuario.getNombreUsuario()).get().getIdUsuario() != idUsuario)
            return new ResponseEntity(new Mensaje("El nombre del usuario ya existe"), HttpStatus.NOT_FOUND);

        if(StringUtils.isBlank(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"), HttpStatus.BAD_REQUEST);

        Usuario usuario = usuarioService.getUsuario(idUsuario).get();
        usuario.setUsuario(nuevoUsuario.getNombreUsuario());
        usuarioService.save(usuario);
        return new ResponseEntity(new Mensaje("Usuario actualizado"), HttpStatus.OK);
    }*/

    //Eliminar usuario
    /*
    @DeleteMapping("/usuario/borrar/{idUsuario}")
    public ResponseEntity<?> borrarUsuario(@PathVariable("idUsuario") int idUsuario){
        if (!usuarioService.existsByIdUsuario(idUsuario))
            return new ResponseEntity(new Mensaje("No existe el usuario"), HttpStatus.NOT_FOUND);
        usuarioService.deleteUsuario(idUsuario);
        return new ResponseEntity(new Mensaje("Usuario eliminado"), HttpStatus.OK);
    }*/
    

}
