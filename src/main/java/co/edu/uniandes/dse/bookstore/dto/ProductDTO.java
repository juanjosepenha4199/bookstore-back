package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private List<PhotoDTO> photos;
    private List<VideoDTO> videos;
    private List<VariantDTO> variants;
    private CategoryDTO category;
    private List<ReviewDTO> reviews;
    private List<OrderDetailDTO> orderDetails;
} 