package org.example.spring.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.example.spring.entities.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderRepository {
    Logger logger = Logger.getLogger(getClass().getName());

    public List<Order> findAll() {
        return new ArrayList<>();
    }

    public Order findById(Long id) {
        Order order = new Order();
        order.setId(id);
        return order;
    }

    public void save(Order order) {
        logger.log(Level.ALL, order.toString());
    }
}
