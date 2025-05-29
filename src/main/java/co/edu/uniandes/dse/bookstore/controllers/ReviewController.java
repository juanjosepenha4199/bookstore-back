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
package co.edu.uniandes.dse.bookstore.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.bookstore.dto.ReviewDTO;
import co.edu.uniandes.dse.bookstore.entities.ReviewEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.ReviewService;

/**
 * Clase que implementa el recurso "reviews".
 *
 * @author ISIS2603
 * @version 1.0
 */
@RestController
@RequestMapping("/clothing")
public class ReviewController {

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Crea una nueva reseña con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param clothingId El ID de la prenda de la cual se le agrega la reseña
	 * @param review {@link ReviewDTO} - La reseña que se desea guardar.
	 * @return JSON {@link ReviewDTO} - La reseña guardada con el atributo id
	 *         autogenerado.
	 */
	@PostMapping(value = "/{clothingId}/reviews")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ReviewDTO createReview(@PathVariable Long clothingId, @RequestBody ReviewDTO review)
			throws EntityNotFoundException {
		ReviewEntity reviewEnity = modelMapper.map(review, ReviewEntity.class);
		ReviewEntity newReview = reviewService.createReview(clothingId, reviewEnity);
		return modelMapper.map(newReview, ReviewDTO.class);
	}

	/**
	 * Busca y devuelve todas las reseñas que existen en una prenda.
	 *
	 * @param clothingId El ID de la prenda de la cual se buscan las reseñas
	 * @return JSONArray {@link ReviewDTO} - Las reseñas encontradas en la prenda. Si
	 *         no hay ninguna retorna una lista vacía.
	 */
	@GetMapping(value = "/{clothingId}/reviews")
	@ResponseStatus(code = HttpStatus.OK)
	public List<ReviewDTO> getReviews(@PathVariable Long clothingId) throws EntityNotFoundException {
		List<ReviewEntity> reviews = reviewService.getReviews(clothingId);
		return modelMapper.map(reviews, new TypeToken<List<ReviewDTO>>() {
		}.getType());
	}

	/**
	 * Busca y devuelve la reseña con el ID recibido en la URL, relativa a una prenda.
	 *
	 * @param clothingId El ID de la prenda de la cual se busca la reseña
	 * @param reviewId El ID de la reseña que se busca
	 * @return {@link ReviewDTO} - La reseña encontrada en la prenda.
	 */
	@GetMapping(value = "/{clothingId}/reviews/{reviewId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ReviewDTO getReview(@PathVariable Long clothingId, @PathVariable Long reviewId)
			throws EntityNotFoundException, IllegalOperationException {
		ReviewEntity reviewEntity = reviewService.getReview(clothingId, reviewId);
		return modelMapper.map(reviewEntity, ReviewDTO.class);
	}

	/**
	 * Actualiza la reseña con el ID recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param clothingId El ID de la prenda de la cual se actualiza la reseña
	 * @param reviewId El ID de la reseña que se desea actualizar
	 * @param review {@link ReviewDTO} - La reseña que se desea guardar.
	 * @return JSON {@link ReviewDTO} - La reseña actualizada.
	 */
	@PutMapping(value = "/{clothingId}/reviews/{reviewId}")
	@ResponseStatus(code = HttpStatus.OK)
	public ReviewDTO updateReview(@PathVariable Long clothingId, @PathVariable Long reviewId,
			@RequestBody ReviewDTO review) throws EntityNotFoundException, IllegalOperationException {
		ReviewEntity reviewEntity = modelMapper.map(review, ReviewEntity.class);
		ReviewEntity updatedReview = reviewService.updateReview(clothingId, reviewId, reviewEntity);
		return modelMapper.map(updatedReview, ReviewDTO.class);
	}

	/**
	 * Borra la reseña con el id asociado recibido en la URL.
	 *
	 * @param clothingId El ID de la prenda de la cual se va a eliminar la reseña.
	 * @param reviewId El ID de la reseña que se va a eliminar.
	 * @throws IllegalOperationException 
	 * @throws BusinessLogicException {@link BusinessLogicExceptionMapper} -
	 */
	@DeleteMapping(value = "/{clothingId}/reviews/{reviewId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void deleteReview(@PathVariable Long clothingId, @PathVariable Long reviewId)
			throws EntityNotFoundException, IllegalOperationException {
		reviewService.deleteReview(clothingId, reviewId);
	}
}
