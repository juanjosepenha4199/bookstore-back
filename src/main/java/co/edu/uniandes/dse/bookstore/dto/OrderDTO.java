package co.edu.uniandes.dse.bookstore.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Long id;
    private Date orderDate;
    private String status;
    private UserDTO user;
    private List<OrderDetailDTO> orderDetails;
    private OperatorDTO operator;
} 