package org.example.spring.repository;

import java.util.ArrayList;
import java.util.List;
import org.example.spring.entities.OrderItems;
import org.springframework.stereotype.Service;

@Service
public class OrderItemRepository {
    public List<OrderItems> findAll() {
        return new ArrayList<>();
    }
}
