package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;
import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private CartDTO cart;
    private List<OrderDTO> orders;
    private List<ReviewDTO> reviews;
} 