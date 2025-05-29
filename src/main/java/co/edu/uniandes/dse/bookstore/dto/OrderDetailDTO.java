package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;

@Data
public class OrderDetailDTO {
    private Long id;
    private OrderDTO order;
    private ProductDTO product;
    private Integer quantity;
    private Double price;
} 