package org.example.spring;

import org.example.spring.entities.Order;
import org.example.spring.repository.JobRepository;
import org.example.spring.repository.MaxRepository;
import org.example.spring.repository.OrderItemRepository;
import org.example.spring.repository.OrderRepository;
import org.example.spring.services.OrderService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    /***
     */
    private static final int MAGIC_NUMBER = 100;

    private Application() {
    }

    /***
     * @param args
     */
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
        var orderService = new OrderService(
            new OrderRepository(), new OrderItemRepository(),
            new JobRepository(),
            new MaxRepository());

        orderService.getUserData("ssss");
        boolean b = orderService.canOrder(new Order());
        if (b) {
            orderService.canOrder(new Order());
        }
        int i = orderService.calcMax(new Order());
        if (i > MAGIC_NUMBER) {
            System.out.println("pro");
        }
        orderService.cOrder(new Order());
        orderService.dOrder(1L);
        orderService.uOrder(1L, "TEST", "2024-01-02");
        Order order = orderService.rOrder(1L);
        orderService.cOrder(order);
        System.out.println(orderService.runJob().contains("x"));
    }

}
