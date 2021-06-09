/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;


import entities.Provinces;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author rdossant
 */
public class MProvinces {
    
     public static ArrayList<Provinces> getProvinces() {
        ArrayList<Provinces> provinces = new ArrayList<Provinces>();

        try {
            MDB.connect();
            String query = "SELECT * FROM provinces";
            ResultSet rs = MDB.execQuery(query);
            while (rs.next()) {
                provinces.add(new Provinces(rs.getInt(1), rs.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return provinces;
    }
     
     public static Provinces getProvinceById(int id) {
        Provinces province = null;
        try {
            MDB.connect();
            String query = "SELECT *  FROM provinces WHERE id = ?";
            PreparedStatement ps = MDB.getPS(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                province = new Provinces(rs.getInt(1), rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return province;
    }
}
