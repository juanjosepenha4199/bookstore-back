package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;

@Data
public class VideoDTO {
    private Long id;
    private String url;
    private String description;
    private ProductDTO product;
} 