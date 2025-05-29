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

import co.edu.uniandes.dse.bookstore.dto.ClothingDTO;
import co.edu.uniandes.dse.bookstore.dto.ClothingDetailDTO;
import co.edu.uniandes.dse.bookstore.entities.ClothingEntity;
import co.edu.uniandes.dse.bookstore.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.bookstore.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.bookstore.services.ClothingService;

/**
 * Clase que implementa el recurso "clothing".
 *
 * @author ISIS2603
 */
@RestController
@RequestMapping("/clothing")
public class ClothingController {

	@Autowired
	private ClothingService clothingService;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Busca y devuelve todas las prendas que existen en la aplicacion.
	 *
	 * @return JSONArray {@link ClothingDetailDTO} - Las prendas encontradas en la
	 *         aplicación. Si no hay ninguna retorna una lista vacía.
	 */
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<ClothingDetailDTO> findAll() {
		List<ClothingEntity> clothingItems = clothingService.getClothingItems();
		return modelMapper.map(clothingItems, new TypeToken<List<ClothingDetailDTO>>() {
		}.getType());
	}

	/**
	 * Busca la prenda con el id asociado recibido en la URL y la devuelve.
	 *
	 * @param id Identificador de la prenda que se esta buscando. Este debe ser una
	 *           cadena de dígitos.
	 * @return JSON {@link ClothingDetailDTO} - La prenda buscada
	 */
	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ClothingDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
		ClothingEntity clothingEntity = clothingService.getClothing(id);
		return modelMapper.map(clothingEntity, ClothingDetailDTO.class);
	}

	/**
	 * Crea una nueva prenda con la informacion que se recibe en el cuerpo de la
	 * petición y se regresa un objeto identico con un id auto-generado por la base
	 * de datos.
	 *
	 * @param clothingDTO {@link ClothingDTO} - La prenda que se desea guardar.
	 * @return JSON {@link ClothingDTO} - La prenda guardada con el atributo id
	 *         autogenerado.
	 */
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public ClothingDTO create(@RequestBody ClothingDTO clothingDTO) throws IllegalOperationException, EntityNotFoundException {
		ClothingEntity clothingEntity = clothingService.createClothing(modelMapper.map(clothingDTO, ClothingEntity.class));
		return modelMapper.map(clothingEntity, ClothingDTO.class);
	}

	/**
	 * Actualiza la prenda con el id recibido en la URL con la información que se
	 * recibe en el cuerpo de la petición.
	 *
	 * @param id Identificador de la prenda que se desea actualizar. Este debe ser
	 *           una cadena de dígitos.
	 * @param clothingDTO {@link ClothingDTO} La prenda que se desea guardar.
	 * @return JSON {@link ClothingDTO} - La prenda guardada.
	 */
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ClothingDTO update(@PathVariable Long id, @RequestBody ClothingDTO clothingDTO)
			throws EntityNotFoundException, IllegalOperationException {
		ClothingEntity clothingEntity = clothingService.updateClothing(id, modelMapper.map(clothingDTO, ClothingEntity.class));
		return modelMapper.map(clothingEntity, ClothingDTO.class);
	}

	/**
	 * Borra la prenda con el id asociado recibido en la URL.
	 *
	 * @param id Identificador de la prenda que se desea borrar. Este debe ser una
	 *           cadena de dígitos.
	 */
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
		clothingService.deleteClothing(id);
	}
}
