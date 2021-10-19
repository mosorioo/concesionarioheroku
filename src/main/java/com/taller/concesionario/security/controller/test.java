package com.taller.concesionario.security.controller;

//import com.taller.concesionario.dto.Mensaje;
import com.taller.concesionario.security.repository.UsuarioRepository;
/*import com.taller.concesionario.security.dto.JwtDto;
import com.taller.concesionario.security.dto.LoginUsuario;
import com.taller.concesionario.security.dto.NuevoUsuario;
import com.taller.concesionario.security.entity.Rol;
import com.taller.concesionario.security.entity.Usuario;
import com.taller.concesionario.security.enums.RolNombre;
import com.taller.concesionario.security.jwt.JwtProvider;
import com.taller.concesionario.security.service.RolService;
import com.taller.concesionario.security.service.UsuarioService;*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
/*import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;*/
import org.springframework.web.bind.annotation.*;

/*import javax.validation.Valid;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;*/

@RestController
@CrossOrigin
public class test {

   @Autowired
   private UsuarioRepository usuarioRepository;

    @GetMapping("/me")
    public ResponseEntity<Object> login(UsernamePasswordAuthenticationToken user){

        return new ResponseEntity<>(usuarioRepository.findByNombreUsuario(user.getName()).get(), HttpStatus.OK);
    }
}
