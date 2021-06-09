package manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Item;
import java.nio.charset.Charset;

public class MItem {

    public static ArrayList<Item> getItems(int category) {
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            MDB.connect();
            String query;
            PreparedStatement ps;
            ResultSet rs;

            if (category == 1) {
                query = "SELECT * FROM product";
                ps = MDB.getPS(query);
            } else {
                query = "SELECT * FROM product WHERE category = ?";
                ps = MDB.getPS(query);
                ps.setInt(1, category);
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(getItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return items;
    }

    public static Item getItemById(int id) {
        Item item = null;
        try {
            MDB.connect();
            String query = "SELECT * FROM product WHERE id = ?";

            PreparedStatement ps = MDB.getPS(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                item = getItemFromResultSet(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return item;
    }

    public static ArrayList<Item> getFeaturedItems() {
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            MDB.connect();
            String query;
            ResultSet rs;

            query = "SELECT * FROM product WHERE id IN (SELECT product FROM featured_product)";

            rs = MDB.execQuery(query);

            while (rs.next()) {
                items.add(getItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return items;
    }

    private static Item getItemFromResultSet(ResultSet rs) {

        Item item = new Item();

        try {
            item.setId(rs.getInt("id"));
            item.setCategory(rs.getInt("category"));
            item.setName(rs.getString("name"));
            item.setDescription(rs.getString("description"));
            item.setPrice(rs.getDouble("price"));
            item.setSerial(rs.getString("serialNumber"));
            item.setImage(rs.getString("imgName"));
            item.setStock(rs.getInt("stockQty"));
            item.setActive(rs.getInt("isActive"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static ArrayList<Item> getItemsBySearchedValues(String searchedValue) {
        ArrayList<Item> items = new ArrayList<Item>();
        try {
            MDB.connect();
            String query;
            ResultSet rs;

            query = "SELECT * FROM product where lower(name) like lower(?) or lower(description) like lower(?)";

            PreparedStatement ps = MDB.getPS(query);
            ps.setString(1, "%" + searchedValue + "%");
            ps.setString(2, "%" + searchedValue + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(getItemFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return items;
    }

    public static int modifyProductById(Item item) {
        int code = -1;

        try {
            MDB.connect();

            // Ajoute l'address a la BD
            String query = "UPDATE  isidrone.product set category = ? , name = ? , description = ?, price =? , serialNumber =? , stockQty =? ,  isActive = ? WHERE id = ?";

            PreparedStatement ps = MDB.getPS(query);

            ps.setInt(1, item.getCategory());
            ps.setString(2, new String(item.getName().getBytes(), Charset.forName("UTF-8")));
            ps.setString(3, new String(item.getDescription().getBytes(), Charset.forName("UTF-8")));
            ps.setDouble(4, item.getPrice());
            ps.setString(5, item.getSerial());
            ps.setInt(6, item.getStock());
            ps.setInt(7, item.isActive());
            ps.setInt(8, item.getId());
            code = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return code;
    }

    public static int addProduct(Item product) {
        int code = -1;

        try {
            MDB.connect();

            // Ajoute l'address a la BD
            String query = "INSERT INTO product (`category`, `name`, `description`, `price`, `serialNumber` "
                    + ", `imgName` , `stockQty` , `isActive`) ";
            query += " VALUES (?, ?, ?, ?, ? , ? , ? , ? )";

            PreparedStatement ps = MDB.getPS(query);

            ps.setInt(1, product.getCategory());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getPrice());
            ps.setString(5, product.getSerial());
            ps.setString(6, product.getImage());
            ps.setInt(7, product.getStock());
            ps.setInt(8, product.isActive());
            code = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return code;

    }

    public static int deleteItemById(int id) {
        Integer affectedRow = null;
        try {
            MDB.connect();
            String query = "Delete FROM product WHERE id = ?";

            PreparedStatement ps = MDB.getPS(query);
            ps.setInt(1, id);
            affectedRow = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return affectedRow;
    }
}
