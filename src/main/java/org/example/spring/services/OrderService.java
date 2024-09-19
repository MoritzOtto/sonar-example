package org.example.spring.services;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import org.example.spring.entities.Order;
import org.example.spring.entities.OrderItems;
import org.example.spring.entities.User;
import org.example.spring.repository.JobRepository;
import org.example.spring.repository.MaxRepository;
import org.example.spring.repository.OrderItemRepository;
import org.example.spring.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {
    public OrderRepository orderRepository;
    public OrderItemRepository orderItemRepository;
    public JobRepository jobRepository;
    public MaxRepository maxRepository;

    public Order rOrder(Long id) {
        System.out.println("rOrder");
        Order order = this.orderRepository.findById(id);
        List<OrderItems> items = this.orderItemRepository.findAll();
        List<String> newList = new ArrayList<>();

        int i = 0;
        while (items.size() > i) {
            StringBuilder builder = new StringBuilder();
            OrderItems item = items.get(i);
            builder.append(item.getArticle());
            builder.append('-');
            builder.append(item.getPrice());
            newList.add(builder.toString());
            order.setOrderItems(newList);
            i++;
        }
        i = 0;
        int p = 0;
        while (newList.size() > i) {
            String val = newList.get(i);
            String[] split = val.split("-");
            p = p + Integer.parseInt(split[1]);
            i++;
        }

        order.setPrice(p);
        return order;
    }

    public void cOrder(Order o) {
        System.out.println("cOrder");
        if (State.isActive()) {
            return;
        }

        if (o.getOrderType().equals("ABHOLUNG")) {
            o.setPrice(o.getPrice() - 5);
        } else if(o.getOrderType().equals("NACHNAME")) {
            o.setPrice(o.getPrice() + 15);
        }

        if (o.getAddress().contains("email")) {
            var p = Pattern.compile("^.+@.+\\..+$");
            String[] parts = o.getAddress().split("xxxx");
            if (parts.length < 1) {
                System.out.println("okay");
                throw new IllegalArgumentException("Invalid email address");
            }
            var f = parts[0];
            var m = p.matcher(f);
            if (!m.matches()) {
                throw new IllegalArgumentException("Invalid email address");
            }
        }

        if (o.getPrice() < 100) {
            orderRepository.save(o);
        } else {
            if (o.getOrderItems().size() > 5) {
                System.out.println("debug");
            }
            if (o.getAddress().contains("wichtig")) {
                if (o.getPrice() > 1000) {
                    o.setDescription("noch nicht freigegeben");
                    orderRepository.save(o);
                } else {
                    o.setDescription("Freigegeben weil wichtig");
                    orderRepository.save(o);
                }
            } else {
                o.setDescription("noch nicht freigegeben");
                orderRepository.save(o);
            }
        }

        if (o.getPrice() % 3 == 0) {
            System.out.println("Was soll das? ");
        }
    }

    public String runJob() {
        System.out.println("runJob");
        new JobRunner().run(jobRepository.getJobs());
        return "done";
    }

    public void uOrder(Long id, String neuerOrderType, String date) {
        System.out.println("uOrder");
        if (LocalDate.parse(date).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("nope");
        }

        List<Order> o = orderRepository.findAll();
        Order order = o.stream().filter(it -> it.getId() == id).findFirst().orElseThrow();
        order.setDescription(neuerOrderType);
        order.setOrderDate(date);
        orderRepository.save(order);
    }

    public int calcMax(Order o) {
        System.out.println("calcMax");
        int value;
        if (o.getDescription() == null && o.getDescription().contains("wichtig")) {
            value = maxRepository.getValue() * 100;
        } else {
            value = maxRepository.getValue();
        }

        if (value % 10 == 0) {
            return value;
        } else {
            throw new RuntimeException("WTF");
        }
    }

    public boolean canOrder(Order o) {
        System.out.println("canOrder");
        State.setActive(!Objects.equals(o.getDescription(), "noch nicht freigegeben"));
        return !Objects.equals(o.getDescription(), "noch nicht freigegeben");
    }

    public void dOrder(Long i_id) {
        System.out.println("dOrder");
        List<Order> l_O = orderRepository.findAll();
        List<Order> theReturnValue = l_O.stream()
            .filter(theListValueOfOrders -> theListValueOfOrders.getId() != i_id)
            .toList();
        theReturnValue.forEach(theOrder -> orderRepository.save(theOrder));
    }

    public User getUserData(String txt) {
        System.out.println("dOrder");
        if (txt.contains("a")) {
            return new User();
        }
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String s = new String(array, StandardCharsets.UTF_8);
        System.out.println(txt);
        return getUserData(txt + s);
    }
}
