package com.taller.concesionario.security.service;

import com.taller.concesionario.security.entity.Usuario;
import com.taller.concesionario.security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Optional<Usuario> getByUsuario(String nombreUsuario){
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    }

    public Boolean existsByUsuario(String nombreUsuario){
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    public Boolean existsByEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Boolean existsByDni(long dni){
        return usuarioRepository.existsByDni(dni);
    }

    public void save(Usuario usuario){
        usuarioRepository.save(usuario);
    }

    //Otros mas

    public List<Usuario> listaUsuario(){
        return  usuarioRepository.findAll();
    }


    public Optional<Usuario> getUsuario(int idUsuario){
        return  usuarioRepository.findById(idUsuario);
    }

    /*
    //Delete 

    public void deleteUsuario(int idUsuario){
        UsuarioRepository.deleteById(idUsuario);
    }

    public boolean existsByIdUsuario(int idUsuario){
        return UsuarioRepository.existsById(idUsuario);
    }
*/


}
