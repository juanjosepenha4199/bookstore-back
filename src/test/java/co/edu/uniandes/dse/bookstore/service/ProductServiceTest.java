package co.edu.uniandes.dse.bookstore.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.uniandes.dse.bookstore.entities.ProductEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.services.ProductService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ProductService.class)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ProductEntity> productList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ProductEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ProductEntity product = factory.manufacturePojo(ProductEntity.class);
            entityManager.persist(product);
            productList.add(product);
        }
    }

    @Test
    void testCrearProducto() {
        ProductEntity newProduct = factory.manufacturePojo(ProductEntity.class);
        ProductEntity result = productService.crearProducto(newProduct);
        assertNotNull(result);
        ProductEntity entity = entityManager.find(ProductEntity.class, result.getId());
        assertEquals(newProduct.getName(), entity.getName());
        assertEquals(newProduct.getDescription(), entity.getDescription());
        assertEquals(newProduct.getPrice(), entity.getPrice());
    }

    @Test
    void testObtenerProductos() {
        List<ProductEntity> list = productService.obtenerProductos();
        assertEquals(productList.size(), list.size());
        for (ProductEntity entity : list) {
            boolean found = false;
            for (ProductEntity storedEntity : productList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testObtenerProducto() throws EntityNotFoundException {
        ProductEntity entity = productList.get(0);
        ProductEntity resultEntity = productService.obtenerProducto(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getName(), resultEntity.getName());
        assertEquals(entity.getDescription(), resultEntity.getDescription());
        assertEquals(entity.getPrice(), resultEntity.getPrice());
    }

    @Test
    void testObtenerProductoInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            productService.obtenerProducto(0L);
        });
    }

    @Test
    void testActualizarProducto() throws EntityNotFoundException {
        ProductEntity entity = productList.get(0);
        ProductEntity pojoEntity = factory.manufacturePojo(ProductEntity.class);
        pojoEntity.setId(entity.getId());
        productService.actualizarProducto(entity.getId(), pojoEntity);
        ProductEntity resp = entityManager.find(ProductEntity.class, entity.getId());
        assertEquals(pojoEntity.getName(), resp.getName());
        assertEquals(pojoEntity.getDescription(), resp.getDescription());
        assertEquals(pojoEntity.getPrice(), resp.getPrice());
    }

    @Test
    void testActualizarProductoInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            ProductEntity pojoEntity = factory.manufacturePojo(ProductEntity.class);
            pojoEntity.setId(0L);
            productService.actualizarProducto(0L, pojoEntity);
        });
    }

    @Test
    void testEliminarProducto() throws EntityNotFoundException {
        ProductEntity entity = productList.get(1);
        productService.eliminarProducto(entity.getId());
        ProductEntity deleted = entityManager.find(ProductEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testEliminarProductoInvalido() {
        assertThrows(EntityNotFoundException.class, () -> {
            productService.eliminarProducto(0L);
        });
    }
} 