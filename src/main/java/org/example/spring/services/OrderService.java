package org.example.spring.services;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.example.spring.entities.Order;
import org.example.spring.entities.OrderItems;
import org.example.spring.entities.User;
import org.example.spring.repository.JobRepository;
import org.example.spring.repository.MaxRepository;
import org.example.spring.repository.OrderItemRepository;
import org.example.spring.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    public final OrderRepository orderRepository;
    public final OrderItemRepository orderItemRepository;
    public final JobRepository jobRepository;
    public final MaxRepository maxRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    Logger logger = Logger.getLogger(getClass().getName());

    public Order rOrder(Long id) {
        logger.log(Level.ALL, "rOrder");
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
        logger.log(Level.ALL, "cOrder");
        if (State.isActive()) {
            return;
        }

        if (o.getOrderType().equals("ABHOLUNG")) {
            o.setPrice(o.getPrice() - 5);
        } else if (o.getOrderType().equals("NACHNAME")) {
            o.setPrice(o.getPrice() + 15);
        }

        if (o.getAddress().contains("email")) {
            //NOSONAR https://stackoverflow.com/questions/52756066/is-email-regex-pattern-suggested-by-w3c-spec-prone-to-redos-attacks
            var p = Pattern.compile("^.+@.+\\..+$");
            String[] parts = o.getAddress().split("xxxx");
            if (parts.length < 1) {
                logger.log(Level.ALL, "okay");
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
            importantOrder(o);
        }

        if (o.getPrice() % 3 == 0) {
            logger.log(Level.ALL, "Was soll das? ");
        }
    }

    private void importantOrder(Order o) {
        if (o.getOrderItems().size() > 5) {
            logger.log(Level.ALL, "debug");
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

    public String runJob() {
        logger.log(Level.ALL, "runJob");
        new JobRunner().run(jobRepository.getJobs());
        return "done";
    }

    public void uOrder(Long id, String neuerOrderType, String date) {
        logger.log(Level.ALL, "uOrder");
        if (LocalDate.parse(date).isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("nope");
        }

        List<Order> o = orderRepository.findAll();
        Order order = o.stream().filter(it -> Objects.equals(it.getId(), id)).findFirst().orElseThrow();
        order.setDescription(neuerOrderType);
        order.setOrderDate(date);
        orderRepository.save(order);
    }

    public int calcMax(Order o) {
        logger.log(Level.ALL, "calcMax");
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
        logger.log(Level.ALL, "canOrder");
        State.setActive(!Objects.equals(o.getDescription(), "noch nicht freigegeben"));
        return !Objects.equals(o.getDescription(), "noch nicht freigegeben");
    }

    public void dOrder(Long i_id) {
        logger.log(Level.ALL, "dOrder");
        List<Order> l_O = orderRepository.findAll();
        List<Order> theReturnValue = l_O.stream()
            .filter(theListValueOfOrders -> !Objects.equals(theListValueOfOrders.getId(), i_id))
            .toList();
        theReturnValue.forEach(orderRepository::save);
    }

    public User getUserData(String txt) {
        logger.log(Level.ALL, "dOrder");
        if (txt.contains("a")) {
            return new User();
        }
        byte[] array = new byte[7]; // length is bounded by 7
        secureRandom.nextBytes(array);
        String s = new String(array, StandardCharsets.UTF_8);
        logger.log(Level.ALL, txt);
        return getUserData(txt + s);
    }
}
