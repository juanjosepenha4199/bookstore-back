package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;

@Data
public class VariantDTO {
    private Long id;
    private String color;
    private String size;
    private Integer stock;
    private ProductDTO product;
} 