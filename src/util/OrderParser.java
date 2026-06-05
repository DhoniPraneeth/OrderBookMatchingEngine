package util;
import model.Order;
import model.OrderSide;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class OrderParser {

    public static List<Order> parse(
            String fileName)
            throws Exception {

        List<Order> orders =
                new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(
                             new FileReader(
                                     fileName))) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] tokens =
                        line.split("\\s+");

                String trader =
                        tokens[0];

                OrderSide side =
                        OrderSide.valueOf(
                                tokens[1]);

                double price =
                        Double.parseDouble(
                                tokens[2]);

                int quantity =
                        Integer.parseInt(
                                tokens[3]);

                orders.add(
                        new Order(
                                trader,
                                side,
                                price,
                                quantity));
            }
        }

        return orders;
    }
}