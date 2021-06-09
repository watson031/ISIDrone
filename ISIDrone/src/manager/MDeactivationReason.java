package manager;


import entities.DeactivationReason;
import util.Hash;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MDeactivationReason {

    public static int add(DeactivationReason deactivationReason) {
        int code = -1;

        try {
            MDB.connect();
            String query = "INSERT INTO `isidrone`.`clients_deactivations_descriptions` (`id_client`, `id_deactivation_initiator`, `description`) VALUES (?, ?, ?)";

            PreparedStatement ps = MDB.getPS(query);

            ps.setInt(1, deactivationReason.getIdClient());
            ps.setInt(2, deactivationReason.getIdDeactivationInitiator());
            ps.setString(3, deactivationReason.getDescription());


            code = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            MDB.disconnect();
        }

        return code;
    }
}
