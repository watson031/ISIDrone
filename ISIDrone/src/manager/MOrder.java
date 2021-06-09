package manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.Cart;
import entities.Item;
import entities.ItemCart;
import entities.Order;
import entities.User;

public class MOrder {

    public static int add(User user, Cart cart) {

        int orderId = 0;

        try {
            // TODO Faire une transaction
            MDB.connect();

            // Partie 1
            // Creer une commande et récupere le ID
            String query = "INSERT INTO `order` (`user_id`, `date`) VALUES (?, now())";

            PreparedStatement ps = MDB.getPS(query, 1);

            ps.setInt(1, user.getId());

            ps.executeUpdate();

            ResultSet generatedKeys = ps.getGeneratedKeys();

            if (generatedKeys.next()) {
                orderId = generatedKeys.getInt(1);
            }

            // Partie 2
            // Ajoute tout les items de la commande dans la table order_info
            for (ItemCart itemC : cart.getCart().values()) {

                query = "INSERT INTO `order_info` (`order_id`, `product_id`, `qty`, `price`) VALUES (?, ?, ?, ?)";
                ps = MDB.getPS(query);

                ps.setInt(1, orderId);
                ps.setInt(2, itemC.getId());
                ps.setInt(3, itemC.getQty());
                ps.setDouble(4, itemC.getPrice());

                ps.executeUpdate();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return orderId;

    }

    public static List<Order> getAllOrdersByUserId(int userId) {

        List<Order> orderList = new ArrayList<Order>();

        try {

            MDB.connect();

            String query = "SELECT `order`.id, `order`.date,"
                    + " `order_info`.order_id, `order_info`.product_id, `order_info`.qty, `order`.isShipped, `order_info`.price "
                    + "FROM `order` INNER JOIN `order_info` ON `order`.id = `order_info`.order_id WHERE `order`.user_id = ?;";

            PreparedStatement ps = MDB.getPS(query);

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            int orderId = 0;

            Order order = new Order();

            // Une ligne = un item avec qte
            while (rs.next()) {

                // Si nouvel commande, creer une nouvelle commande
                if (orderId != rs.getInt("id")) {

                    // Ecraser le orderId de condition
                    orderId = rs.getInt("id");

                    // Nouvelle commande
                    order = new Order();

                    order.setId(orderId);
                    order.setUserId(userId);
                    order.setDate(rs.getString("order.date"));
                    order.setIsShipped(rs.getInt(6) == 1);

                    // Ajouter la commande a la liste	
                    orderList.add(order);
                }

                // Recupérer l'item suivant
                int itemId = rs.getInt("order_info.product_id");

                // Recuperer l'item complet de la BD
                Item item = MItem.getItemById(itemId);

                // Ecraser le prix et ajouter la quantité
                ItemCart itemC = new ItemCart(item);
                itemC.setQty(rs.getInt("order_info.qty"));
                itemC.setPrice(rs.getDouble("order_info.price"));

                // Ajouter l'itemPanier au panier
                order.addItem(itemC.getSerial(), itemC);
            }

            //orderList.add(order);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return orderList;
    }

    public static ArrayList<Order> getAll() {
        ArrayList<Order> orders = new ArrayList<Order>();
        try {

            MDB.connect();

            String query = "SELECT  * from `order`";
            ResultSet rs = MDB.execQuery(query);
            while (rs.next()) {
                orders.add(new Order(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getBoolean(4)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return orders;
    }

    public static Integer deleteOrderById(int idOrder) {
        Integer affectedRow = null;
        try {
            MDB.connect();
            String query1 = "Delete FROM  isidrone.order_info WHERE isidrone.order_info.order_id = ?";
            String query2 = "Delete FROM `isidrone`.`order` WHERE id = ?";

            PreparedStatement ps = MDB.getPS(query1);

            ps.setInt(1, idOrder);
            affectedRow = ps.executeUpdate();

            if (affectedRow > 0) {
                ps = MDB.getPS(query2);
                ps.setInt(1, idOrder);
                affectedRow = ps.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return affectedRow;
    }

    public static Integer shipOrderById(int idOrder, int isShipped) {
        Integer affectedRow = null;
        try {
            MDB.connect();
            String query1 = "UPDATE `order` SET isShipped =" + isShipped + " WHERE id = ?";

            PreparedStatement ps = MDB.getPS(query1);

            ps.setInt(1, idOrder);
            affectedRow = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return affectedRow;
    }

}
