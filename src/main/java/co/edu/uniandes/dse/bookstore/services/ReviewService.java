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

package co.edu.uniandes.dse.bookstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.bookstore.entities.ProductEntity;
import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.ErrorMessage;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.ProductRepository;
import co.edu.uniandes.dse.bookstore.repositories.ReviewRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase que implementa la conexion con la persistencia para la entidad de
 * Reseña(Review).
 *
 * @author ISIS2603
 */
@Slf4j
@Service
public class ReviewService {

	@Autowired
	ReviewRepository reviewRepository;

	@Autowired
	ProductRepository productRepository;
	
	
	/**
	 * Se encarga de crear un Review en la base de datos.
	 *
	 * @param reviewEntity Objeto de ReviewEntity con los datos nuevos
	 * @param productId   id del Producto el cual sera padre del nuevo Review.
	 * @return Objeto de ReviewEntity con los datos nuevos y su ID.
	 * @throws EntityNotFoundException si el producto no existe.
	 *
	 */
	@Transactional
	public ReviewEntity createReview(Long productId, ReviewEntity reviewEntity) throws EntityNotFoundException {
		log.info("Inicia proceso de crear review");
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if (productEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CLOTHING_NOT_FOUND);

		reviewEntity.setProduct(productEntity.get());

		log.info("Termina proceso de creación del review");
		return reviewRepository.save(reviewEntity);
	}

	/**
	 * Obtiene la lista de los registros de Review que pertenecen a un Producto.
	 *
	 * @param productId id del Producto el cual es padre de los Reviews.
	 * @return Colección de objetos de ReviewEntity.
	 */

	@Transactional
	public List<ReviewEntity> getReviews(Long productId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar los reviews asociados al producto con id = {0}", productId);
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if (productEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CLOTHING_NOT_FOUND);

		log.info("Termina proceso de consultar los reviews asociados al producto con id = {0}", productId);
		return productEntity.get().getReviews();
	}

	/**
	 * Obtiene los datos de una instancia de Review a partir de su ID. La existencia
	 * del elemento padre Producto se debe garantizar.
	 *
	 * @param productId   El id del Producto buscado
	 * @param reviewId     Identificador de la Reseña a consultar
	 * @return Instancia de ReviewEntity con los datos del Review consultado.
	 *
	 */
	@Transactional
	public ReviewEntity getReview(Long productId, Long reviewId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar el review con id = {0} del producto con id = " + productId,
				reviewId);
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if (productEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CLOTHING_NOT_FOUND);

		Optional<ReviewEntity> reviewEntity = reviewRepository.findById(reviewId);
		if (reviewEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.REVIEW_NOT_FOUND);

		log.info("Termina proceso de consultar el review con id = {0} del producto con id = " + productId,
				reviewId);
		return reviewEntity.get();
	}

	/**
	 * Actualiza la información de una instancia de Review.
	 *
	 * @param reviewEntity Instancia de ReviewEntity con los nuevos datos.
	 * @param productId   id del Producto el cual sera padre del Review actualizado.
	 * @param reviewId     id de la review que será actualizada.
	 * @return Instancia de ReviewEntity con los datos actualizados.
	 *
	 */
	@Transactional
	public ReviewEntity updateReview(Long productId, Long reviewId, ReviewEntity review) throws EntityNotFoundException {
		log.info("Inicia proceso de actualizar el review con id = {0} del producto con id = " + productId,
				reviewId);
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if (productEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CLOTHING_NOT_FOUND);

		Optional<ReviewEntity> reviewEntity = reviewRepository.findById(reviewId);
		if (reviewEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.REVIEW_NOT_FOUND);

		review.setId(reviewId);
		review.setProduct(productEntity.get());
		log.info("Termina proceso de actualizar el review con id = {0} del producto con id = " + productId,
				reviewId);
		return reviewRepository.save(review);
	}

	/**
	 * Elimina una instancia de Review de la base de datos.
	 *
	 * @param reviewId     Identificador de la instancia a eliminar.
	 * @param productId   id del Producto el cual es padre del Review.
	 * @throws EntityNotFoundException Si la reseña no esta asociada al producto.
	 * @throws IllegalOperationException 
	 *
	 */
	@Transactional
	public void deleteReview(Long productId, Long reviewId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar el review con id = {0} del producto con id = " + productId,
				reviewId);
		Optional<ProductEntity> productEntity = productRepository.findById(productId);
		if (productEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CLOTHING_NOT_FOUND);

		Optional<ReviewEntity> reviewEntity = reviewRepository.findById(reviewId);
		if (reviewEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.REVIEW_NOT_FOUND);
		
		if(!reviewEntity.get().getProduct().getId().equals(productId))
			throw new IllegalOperationException(ErrorMessage.REVIEW_NOT_ASSOCIATED_TO_CLOTHING);
		
		reviewRepository.deleteById(reviewId);
		log.info("Termina proceso de borrar el review con id = {0} del producto con id = " + productId,
				reviewId);
	}
}
