package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;
import java.util.List;

@Data
public class OperatorDTO {
    private Long id;
    private String name;
    private String email;
    private List<OrderDTO> orders;
    private List<ProductDTO> products;
} 