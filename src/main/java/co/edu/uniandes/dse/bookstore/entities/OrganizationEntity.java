package co.edu.uniandes.dse.bookstore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

/**
 * Clase que representa una organizacion en la persistencia
 *
 * @author 
 */

@Data
@Entity
public class OrganizationEntity extends BaseEntity {

	enum TIPO_ORGANIZACION {
		PRIVADA, PUBLICA, FUNDACION
	}

	private String name;
	private TIPO_ORGANIZACION tipo;

	@PodamExclude
	@OneToOne(mappedBy = "organization", fetch = FetchType.LAZY)
	private PrizeEntity prize;
}
