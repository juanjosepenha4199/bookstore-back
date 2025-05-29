package co.edu.uniandes.dse.bookstore.services;

import co.edu.uniandes.dse.bookstore.entities.UserEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserEntity> obtenerUsuarios() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserEntity obtenerUsuario(Long id) throws EntityNotFoundException {
        Optional<UserEntity> usuario = userRepository.findById(id);
        if (usuario.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        return usuario.get();
    }

    @Transactional
    public UserEntity crearUsuario(UserEntity usuario) {
        return userRepository.save(usuario);
    }

    @Transactional
    public UserEntity actualizarUsuario(Long id, UserEntity usuario) throws EntityNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        usuario.setId(id);
        return userRepository.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) throws EntityNotFoundException {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }
} 