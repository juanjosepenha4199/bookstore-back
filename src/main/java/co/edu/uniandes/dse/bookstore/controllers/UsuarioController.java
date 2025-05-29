package co.edu.uniandes.dse.bookstore.controllers;

import co.edu.uniandes.dse.bookstore.dto.UserDTO;
import co.edu.uniandes.dse.bookstore.entities.UserEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> obtenerUsuarios() {
        return userService.obtenerUsuarios().stream()
                .map(usuario -> modelMapper.map(usuario, UserDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO obtenerUsuario(@PathVariable Long id) throws EntityNotFoundException {
        UserEntity usuario = userService.obtenerUsuario(id);
        return modelMapper.map(usuario, UserDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO crearUsuario(@RequestBody UserDTO userDTO) {
        UserEntity usuario = modelMapper.map(userDTO, UserEntity.class);
        UserEntity creado = userService.crearUsuario(usuario);
        return modelMapper.map(creado, UserDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO actualizarUsuario(@PathVariable Long id, @RequestBody UserDTO userDTO) throws EntityNotFoundException {
        UserEntity usuario = modelMapper.map(userDTO, UserEntity.class);
        UserEntity actualizado = userService.actualizarUsuario(id, usuario);
        return modelMapper.map(actualizado, UserDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarUsuario(@PathVariable Long id) throws EntityNotFoundException {
        userService.eliminarUsuario(id);
    }
} 