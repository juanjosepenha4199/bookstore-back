/*
MIT License

Copyright (c) 2021 Universidad de los Andes - ISIS2603

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.ReviewService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Reviews
 *
 * @author ISIS2603
 */
@DataJpaTest
@Transactional
@Import(ReviewService.class)
class ReviewServiceTest {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private TestEntityManager entityManager;

	private PodamFactory factory = new PodamFactoryImpl();

	private ProductEntity productEntity = new ProductEntity();
	private List<ReviewEntity> reviewList = new ArrayList<>();

	/**
	 * Configuración inicial de la prueba.
	 */
	@BeforeEach
	void setUp() {
		clearData();
		insertData();
	}

	/**
	 * Limpia las tablas que están implicadas en la prueba.
	 */
	private void clearData() {
		entityManager.getEntityManager().createQuery("delete from ReviewEntity").executeUpdate();
		entityManager.getEntityManager().createQuery("delete from ProductEntity").executeUpdate();
	}

	/**
	 * Inserta los datos iniciales para el correcto funcionamiento de las pruebas.
	 */
	private void insertData() {
		productEntity = factory.manufacturePojo(ProductEntity.class);
		entityManager.persist(productEntity);
		
		for (int i = 0; i < 3; i++) {
			ReviewEntity entity = factory.manufacturePojo(ReviewEntity.class);
			entity.setProduct(productEntity);
			entityManager.persist(entity);
			reviewList.add(entity);
		}
		
		productEntity.setReviews(reviewList);
	}

	/**
	 * Prueba para crear un Review.
	 */
	@Test
	void testCreateReview() throws EntityNotFoundException {
		ReviewEntity newEntity = factory.manufacturePojo(ReviewEntity.class);
				
		ReviewEntity result = reviewService.createReview(productEntity.getId(), newEntity);
		assertNotNull(result);
		ReviewEntity entity = entityManager.find(ReviewEntity.class, result.getId());
		assertEquals(newEntity.getId(), entity.getId());
	}

	/**
	 * Prueba para crear un Review con una prenda que no existe.
	 */
	@Test
	void testCreateReviewInvalidProduct() {
		assertThrows(EntityNotFoundException.class, () -> {
			ReviewEntity newEntity = factory.manufacturePojo(ReviewEntity.class);
			reviewService.createReview(0L, newEntity);
		});
	}

	/**
	 * Prueba para consultar la lista de Reviews.
	 */
	@Test
	void testGetReviews() throws EntityNotFoundException {
		List<ReviewEntity> list = reviewService.getReviews(productEntity.getId());
		assertEquals(reviewList.size(), list.size());
		for (ReviewEntity entity : list) {
			boolean found = false;
			for (ReviewEntity storedEntity : reviewList) {
				if (entity.getId().equals(storedEntity.getId())) {
					found = true;
				}
			}
			assertTrue(found);
		}
	}

	/**
	 * Prueba para consultar la lista de Reviews de una prenda que no existe.
	 */
	@Test
	void testGetReviewsInvalidProduct() {
		assertThrows(EntityNotFoundException.class, () -> {
			reviewService.getReviews(0L);
		});
	}

	/**
	 * Prueba para consultar un Review.
	 */
	@Test
	void testGetReview() throws EntityNotFoundException {
		ReviewEntity entity = reviewList.get(0);
		ReviewEntity resultEntity = reviewService.getReview(productEntity.getId(), entity.getId());
		assertNotNull(resultEntity);
		assertEquals(entity.getId(), resultEntity.getId());
	}

	/**
	 * Prueba para consultar un Review que no existe.
	 */
	@Test
	void testGetInvalidReview() {
		assertThrows(EntityNotFoundException.class, () -> {
			reviewService.getReview(productEntity.getId(), 0L);
		});
	}

	/**
	 * Prueba para actualizar un Review.
	 */
	@Test
	void testUpdateReview() throws EntityNotFoundException {
		ReviewEntity entity = reviewList.get(0);
		ReviewEntity pojoEntity = factory.manufacturePojo(ReviewEntity.class);

		pojoEntity.setId(entity.getId());
		reviewService.updateReview(productEntity.getId(), entity.getId(), pojoEntity);

		ReviewEntity resp = entityManager.find(ReviewEntity.class, entity.getId());
		assertEquals(pojoEntity.getId(), resp.getId());
	}

	/**
	 * Prueba para actualizar un Review que no existe.
	 */
	@Test
	void testUpdateInvalidReview() {
		assertThrows(EntityNotFoundException.class, () -> {
			ReviewEntity pojoEntity = factory.manufacturePojo(ReviewEntity.class);
			pojoEntity.setId(0L);
			reviewService.updateReview(productEntity.getId(), 0L, pojoEntity);
		});
	}

	/**
	 * Prueba para eliminar un Review.
	 */
	@Test
	void testDeleteReview() throws EntityNotFoundException, IllegalOperationException {
		ReviewEntity entity = reviewList.get(0);
		reviewService.deleteReview(productEntity.getId(), entity.getId());
		ReviewEntity deleted = entityManager.find(ReviewEntity.class, entity.getId());
		assertNull(deleted);
	}

	/**
	 * Prueba para eliminar un Review que no existe.
	 */
	@Test
	void testDeleteInvalidReview() {
		assertThrows(EntityNotFoundException.class, () -> {
			reviewService.deleteReview(productEntity.getId(), 0L);
		});
	}
}
