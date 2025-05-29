package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private UserDTO user;
    private List<ProductDTO> products;
} 