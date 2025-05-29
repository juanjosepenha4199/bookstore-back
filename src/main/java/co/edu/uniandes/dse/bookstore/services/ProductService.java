package co.edu.uniandes.dse.bookstore.services;

import co.edu.uniandes.dse.bookstore.entities.ProductEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductEntity> obtenerProductos() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ProductEntity obtenerProducto(Long id) throws EntityNotFoundException {
        Optional<ProductEntity> producto = productRepository.findById(id);
        if (producto.isEmpty()) {
            throw new EntityNotFoundException("Producto no encontrado");
        }
        return producto.get();
    }

    @Transactional
    public ProductEntity crearProducto(ProductEntity producto) {
        return productRepository.save(producto);
    }

    @Transactional
    public ProductEntity actualizarProducto(Long id, ProductEntity producto) throws EntityNotFoundException {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado");
        }
        producto.setId(id);
        return productRepository.save(producto);
    }

    @Transactional
    public void eliminarProducto(Long id) throws EntityNotFoundException {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }
} 