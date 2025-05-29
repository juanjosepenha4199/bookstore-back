package co.edu.uniandes.dse.bookstore.controllers;

import co.edu.uniandes.dse.bookstore.dto.ProductDTO;
import co.edu.uniandes.dse.bookstore.entities.ProductEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> obtenerProductos() {
        return productService.obtenerProductos().stream()
                .map(producto -> modelMapper.map(producto, ProductDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO obtenerProducto(@PathVariable Long id) throws EntityNotFoundException {
        ProductEntity producto = productService.obtenerProducto(id);
        return modelMapper.map(producto, ProductDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO crearProducto(@RequestBody ProductDTO productDTO) {
        ProductEntity producto = modelMapper.map(productDTO, ProductEntity.class);
        ProductEntity creado = productService.crearProducto(producto);
        return modelMapper.map(creado, ProductDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO actualizarProducto(@PathVariable Long id, @RequestBody ProductDTO productDTO) throws EntityNotFoundException {
        ProductEntity producto = modelMapper.map(productDTO, ProductEntity.class);
        ProductEntity actualizado = productService.actualizarProducto(id, producto);
        return modelMapper.map(actualizado, ProductDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarProducto(@PathVariable Long id) throws EntityNotFoundException {
        productService.eliminarProducto(id);
    }
} 