/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import entities.Address;
import entities.User;
import util.Hash;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author bndiaye
 */
public class MUser {

    public static ArrayList<User> getAll() {
        ArrayList<User> users = new ArrayList<>();
        try {

            MDB.connect();

            String query = "SELECT  * from user where userType = 0";
            ResultSet rs = MDB.execQuery(query);
            while (rs.next()) {
                users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return users;
    }

    public static ArrayList<User> getAllPlusAdmins() {
        ArrayList<User> users = new ArrayList<>();
        try {

            MDB.connect();

            String query = "SELECT  * from user";
            ResultSet rs = MDB.execQuery(query);
            while (rs.next()) {
                users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return users;
    }

    public static ArrayList<User> getAllClients() {
        ArrayList<User> users = new ArrayList<>();
        try {

            MDB.connect();
            ResultSet rs;
            String query = "SELECT id, lastName, firstName,email, userStatus from user where userType = 0";
            PreparedStatement ps = MDB.getPS(query);

            rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return users;
    }

    public static ArrayList<User> getAllClients(String searchedValue) {
        ArrayList<User> users = new ArrayList<>();
        try {

            MDB.connect();
            ResultSet rs;
            String query = "SELECT lastName, firstName,email from user where (lower(firstName) like lower(?) or lower(lastName) "
                    + "like lower(?) or lower(email) like lower(?)) and userType = 0";
            PreparedStatement ps = MDB.getPS(query);
            ps.setString(1, "%" + searchedValue + "%");
            ps.setString(2, "%" + searchedValue + "%");
            ps.setString(3, "%" + searchedValue + "%");

            rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getString(1), rs.getString(2), rs.getString(3)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return users;
    }

    public static Integer isUserActif(int idUser,int idDeactivatorIniator, String statut,String description) {
        Integer affectedRow = null;
        try {
            MDB.connect();

            //Ajouter dans la table deactivation_..
            if(statut.equals("DISACTIVATED")){
                String query1 = "INSERT INTO isidrone.clients_deactivations_descriptions (id_client, id_deactivation_initiator, description) VALUES (?, ?, ?)";
                PreparedStatement ps = MDB.getPS(query1);
                ps.setInt(1, idUser);
                ps.setInt(2, idDeactivatorIniator);
                ps.setString(3, description==null?"":description);
                affectedRow = ps.executeUpdate();

            }
            if (statut.equals("ACTIVATED")){
                String query1 = "DELETE FROM  isidrone.clients_deactivations_descriptions WHERE id_client =?";
                PreparedStatement ps = MDB.getPS(query1);
                ps.setInt(1, idUser);
                affectedRow = ps.executeUpdate();
            }
            String query1 = "UPDATE `user` SET userStatus = ? WHERE id = ?";

            PreparedStatement ps = MDB.getPS(query1);

            ps.setString(1, statut);
            ps.setInt(2, idUser);
            affectedRow = ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return affectedRow;
    }

    public static User ClientById(int clientId) {
        User user = null;
        try {

            MDB.connect();
            ResultSet rs;
            String query = "SELECT * from user inner join address on address.id = ship_address_id   where userType = 0 and user.id = ?";
            PreparedStatement ps = MDB.getPS(query);
            ps.setInt(1, clientId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Address address = new Address(rs.getInt(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getString(15), rs.getString(16));
                user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), address, rs.getInt(7), rs.getString(8));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }
        return user;
    }
    public static int isExistEmail(User user){
        int isExist = -1;

        try {
            MDB.connect();

            String query = "SELECT id FROM user WHERE email = ? and id != ?";
            PreparedStatement ps = MDB.getPS(query);

            ps.setString(1, user.getEmail());
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();

            isExist = (rs.next() ? 0 : 1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            MDB.disconnect();
        }

        return isExist;
    }
    
    
    public static int editClient(User user) {
        int code = isExistEmail(user);
        if(code == 1){
            try {
                MDB.connect();
                // Ajoute l'address a la BD
                String query = "UPDATE  `user` set `lastName` = ? , `firstName` = ? , `email` = ?,  `userStatus` = ?  WHERE id =? ";
                PreparedStatement ps = MDB.getPS(query);

                ps.setString(1, user.getLastName());
                ps.setString(2, user.getFirstName());
                ps.setString(3, user.getEmail());
                ps.setString(4, user.getUserStatus());
                ps.setInt(5, user.getId());
                code = ps.executeUpdate();
                if (code > 0){
                    String query2 = "UPDATE  `address` set `no` = ? , `appt` = ? , `street` = ?, `zip`=?, `city`=?, `state`=?, `country`=? WHERE id =? ";
                    ps = MDB.getPS(query2);

                    ps.setString(1, user.getShipAddress().getNo());
                    ps.setString(2, user.getShipAddress().getAppt());
                    ps.setString(3, user.getShipAddress().getStreet());
                    ps.setString(4, user.getShipAddress().getZip());
                    ps.setString(5, user.getShipAddress().getCity());
                    ps.setString(6, user.getShipAddress().getState());
                    ps.setString(7, user.getShipAddress().getCountry());
                    ps.setInt(8, user.getShipAddress().getId());
                    code = ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                MDB.disconnect();
            }
        }

        return code;
    }
    
    
     public static int editClientPassword(User user) {
        int code = isExistEmail(user);
        if(code == 1){
            try {
                MDB.connect();
                // Ajoute l'address a la BD
                String query = "UPDATE  `user` set `password` = ? WHERE id =? ";
                PreparedStatement ps = MDB.getPS(query);
                //
                ps.setString(1, Hash.SHA1(user.getPassword()));
                ps.setInt(2, user.getId());
                code = ps.executeUpdate();
              
            } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            } finally {
                MDB.disconnect();
            }
        }
        return code;
    }
     
     
}
