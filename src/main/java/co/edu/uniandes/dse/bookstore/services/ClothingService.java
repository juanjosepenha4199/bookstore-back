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

import co.edu.uniandes.dse.bookstore.entities.ClothingEntity;
import co.edu.uniandes.dse.bookstore.entities.BrandEntity;
import co.edu.uniandes.dse.bookstore.entities.DesignerEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.ErrorMessage;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.repositories.ClothingRepository;
import co.edu.uniandes.dse.bookstore.repositories.BrandRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClothingService {

	@Autowired
	ClothingRepository clothingRepository;

	@Autowired
	BrandRepository brandRepository;
	
	/**
	 * Guardar una nueva prenda
	 *
	 * @param clothingEntity La entidad de tipo prenda de la nueva prenda a persistir.
	 * @return La entidad luego de persistirla
	 * @throws IllegalOperationException Si el SKU es inválido o ya existe en la
	 *                                   persistencia o si la marca es inválida
	 */
	@Transactional
	public ClothingEntity createClothing(ClothingEntity clothingEntity) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de creación de la prenda");
		
		if (clothingEntity.getBrand() == null)
			throw new IllegalOperationException("Brand is not valid");
		
		Optional<BrandEntity> brandEntity = brandRepository.findById(clothingEntity.getBrand().getId());
		if (brandEntity.isEmpty())
			throw new IllegalOperationException("Brand is not valid");

		if (!validateSKU(clothingEntity.getSku()))
			throw new IllegalOperationException("SKU is not valid");

		if (!clothingRepository.findBySku(clothingEntity.getSku()).isEmpty())
			throw new IllegalOperationException("SKU already exists");

		clothingEntity.setBrand(brandEntity.get());
		log.info("Termina proceso de creación de la prenda");
		return clothingRepository.save(clothingEntity);
	}

	/**
	 * Devuelve todas las prendas que hay en la base de datos.
	 *
	 * @return Lista de entidades de tipo prenda.
	 */
	@Transactional
	public List<ClothingEntity> getClothingItems() {
		log.info("Inicia proceso de consultar todas las prendas");
		return clothingRepository.findAll();
	}

	/**
	 * Busca una prenda por ID
	 *
	 * @param clothingId El id de la prenda a buscar
	 * @return La prenda encontrada
	 * @throws EntityNotFoundException Si la prenda no se encuentra
	 */
	@Transactional
	public ClothingEntity getClothing(Long clothingId) throws EntityNotFoundException {
		log.info("Inicia proceso de consultar la prenda con id = {0}", clothingId);
		Optional<ClothingEntity> clothingEntity = clothingRepository.findById(clothingId);
		if (clothingEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CLOTHING_NOT_FOUND);
		log.info("Termina proceso de consultar la prenda con id = {0}", clothingId);
		return clothingEntity.get();
	}

	/**
	 * Actualizar una prenda por ID
	 *
	 * @param clothingId    El ID de la prenda a actualizar
	 * @param clothing La entidad de la prenda con los cambios deseados
	 * @return La entidad de la prenda luego de actualizarla
	 * @throws IllegalOperationException Si el SKU de la actualización es inválido
	 * @throws EntityNotFoundException Si la prenda no es encontrada
	 */
	@Transactional
	public ClothingEntity updateClothing(Long clothingId, ClothingEntity clothing)
			throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de actualizar la prenda con id = {0}", clothingId);
		Optional<ClothingEntity> clothingEntity = clothingRepository.findById(clothingId);
		if (clothingEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CLOTHING_NOT_FOUND);

		if (!validateSKU(clothing.getSku()))
			throw new IllegalOperationException("SKU is not valid");

		clothing.setId(clothingId);
		log.info("Termina proceso de actualizar la prenda con id = {0}", clothingId);
		return clothingRepository.save(clothing);
	}

	/**
	 * Eliminar una prenda por ID
	 *
	 * @param clothingId El ID de la prenda a eliminar
	 * @throws IllegalOperationException si la prenda tiene diseñadores asociados
	 * @throws EntityNotFoundException si la prenda no existe
	 */
	@Transactional
	public void deleteClothing(Long clothingId) throws EntityNotFoundException, IllegalOperationException {
		log.info("Inicia proceso de borrar la prenda con id = {0}", clothingId);
		Optional<ClothingEntity> clothingEntity = clothingRepository.findById(clothingId);
		if (clothingEntity.isEmpty())
			throw new EntityNotFoundException(ErrorMessage.CLOTHING_NOT_FOUND);

		List<DesignerEntity> designers = clothingEntity.get().getDesigners();

		if (!designers.isEmpty())
			throw new IllegalOperationException("Unable to delete clothing because it has associated designers");

		clothingRepository.deleteById(clothingId);
		log.info("Termina proceso de borrar la prenda con id = {0}", clothingId);
	}

	/**
	 * Verifica que el SKU no sea invalido.
	 *
	 * @param sku a verificar
	 * @return true si el SKU es valido.
	 */
	private boolean validateSKU(String sku) {
		return !(sku == null || sku.isEmpty());
	}
}
