package org.example.spring.repository;

import java.util.ArrayList;
import java.util.List;
import org.example.spring.entities.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderRepository {
    public List<Order> findAll() {
        return new ArrayList<>();
    }

    public Order findById(Long id) {
        return new Order();
    }

    public void save(Order order) {

    }
}
