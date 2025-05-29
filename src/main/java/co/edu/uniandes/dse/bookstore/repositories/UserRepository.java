package co.edu.uniandes.dse.bookstore.repositories;

import co.edu.uniandes.dse.bookstore.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
} 