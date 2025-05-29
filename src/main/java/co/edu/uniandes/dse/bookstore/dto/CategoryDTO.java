package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private List<ProductDTO> products;
} 