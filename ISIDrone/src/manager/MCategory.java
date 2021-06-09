package manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entities.Category;
import entities.Item;
import java.nio.charset.Charset;
import java.sql.Types;

public class MCategory {

    public static ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();

        try {
            MDB.connect();
            String query = "SELECT * FROM category";
            ResultSet rs = MDB.execQuery(query);
            while (rs.next()) {
                categories.add(new Category(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return categories;
    }

    public static ArrayList<Category> getClientCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();
        PreparedStatement ps = null;

        try {
            MDB.connect();
            String query = "SELECT * FROM category where isActive = ?";

            ps = MDB.getPS(query);
            ps.setInt(1, 1);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                categories.add(new Category(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(4), rs.getInt(5)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return categories;
    }

    public static Category getCategorieById(int id) {
        Category cat = null;
        try {
            MDB.connect();
            String query = "SELECT *  FROM category WHERE id = ?";
            PreparedStatement ps = MDB.getPS(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int asctif = rs.getInt(5);
                cat = new Category(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(5), rs.getInt(4));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return cat;
    }

    public static int isExist(int category) {
        int isExist;
        try {
            MDB.connect();
            String query = "SELECT 'exist' FROM category WHERE id = ?";
            PreparedStatement ps = MDB.getPS(query);

            ps.setInt(1, category);
            ResultSet rs = ps.executeQuery();

            isExist = (rs.next() ? 0 : 1);
        } catch (Exception e) {
            isExist = -1;
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return isExist;
    }

    public static ArrayList<Category> getCategoriesByOrder() {
        ArrayList<Category> categories = new ArrayList<Category>();

        try {
            MDB.connect();
            String query = "SELECT * FROM category A ORDER BY A.order";
            ResultSet rs = MDB.execQuery(query);
            while (rs.next()) {
                categories.add(new Category(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getBoolean(5), rs.getInt(4)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return categories;
    }

    public static Integer deleteCategoryById(int idCategory) {
        Integer affectedRow = null;
        try {
            MDB.connect();
            String query = "Delete FROM category WHERE id = ?";
            PreparedStatement ps = MDB.getPS(query);
            ps.setInt(1, idCategory);
            affectedRow = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return affectedRow;
    }

    public static int modifyCategory(Category category) {
        int code = -1;

        try {
            MDB.connect();

            String description = new String(category.getDescription().getBytes(), Charset.forName("UTF-8"));
            String query = "UPDATE  isidrone.category set name = ?, description = ?, isidrone.category.order =? , isActive =? WHERE id = ?";

            PreparedStatement ps = MDB.getPS(query);

            ps.setString(1, new String(category.getName().getBytes(), Charset.forName("UTF-8")));
            if (description.trim().equals("")) {
                ps.setNull(2, Types.NULL);
            } else {
                ps.setString(2, description);
            }

            ps.setInt(3, category.getOrder());
            ps.setBoolean(4, category.getIsActive());
            ps.setInt(5, category.getId());
            code = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return code;
    }

    public static int isExistByName(String name) {
        int isExist = 0;
        try {
            MDB.connect();
            String query = "SELECT * FROM category WHERE lower(name) = lower(?)";
            PreparedStatement ps = MDB.getPS(query);

            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();

            //isExist = (rs.first() ? 0 : 1);
            while (rs.next()) {
                isExist = 1;
            }
        } catch (SQLException e) {
            isExist = 0;
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return isExist;
    }

    public static int addCategory(Category category) {
        int code = -1;
        //
        try {
            MDB.connect();
            String description = new String(category.getDescription().getBytes(), Charset.forName("UTF-8"));
            // Ajoute l'address a la BD
            String query = "INSERT INTO category ( `name`, `description`, `order` , `isActive`) ";
            query += " VALUES (?, ?, ?, ? )";

            PreparedStatement ps = MDB.getPS(query);

            ps.setString(1, new String(category.getName().getBytes(), Charset.forName("UTF-8")));
            if (description.trim().equals("")) {
                ps.setNull(2, Types.NULL);
            } else {
                ps.setString(2, description);
            }

            ps.setInt(3, category.getOrder());
            ps.setBoolean(4, category.getIsActive());
            code = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return code;
    }

}
