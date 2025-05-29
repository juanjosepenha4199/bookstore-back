package co.edu.uniandes.dse.bookstore.repositories;

import co.edu.uniandes.dse.bookstore.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
} 