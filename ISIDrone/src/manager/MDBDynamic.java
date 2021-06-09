/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author bndiaye
 */
public class MDBDynamic extends MDB {
    /***
     *
     * @throws SQLException
     */
    public static void connect(ConfigLocation configLocation) throws SQLException {

        try {
            HashMap<String, String> dbProperty = getPropValues(configLocation);
            Class.forName("com.mysql.cj.jdbc.Driver");
            String mysqlURL = "jdbc:mysql://" + dbProperty.get("DB_IP") + ":" + dbProperty.get("DB_PORT") + "/" + dbProperty.get("DB_NAME") + "?serverTimezone=UTC";
            connection = DriverManager.getConnection(mysqlURL, dbProperty.get("DB_USERNAME"), dbProperty.get("DB_PASSWORD"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(MDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return
     * @throws IOException
     */
    public static HashMap<String, String> getPropValues(ConfigLocation configLocation) throws IOException {
        InputStream inputStream = null;
        FileReader fileReader;
        HashMap<String, String> map = null;
        try {
            Properties prop = new Properties();
            switch (configLocation){
                case LOCAL:
                    inputStream = MDBDynamic.class.getResourceAsStream("/config.properties");
                    prop.load(inputStream);
                    break;
                case TOMCAT:
                    String fileName = System.getenv("ISIDRONE_CONFIG_PROPERTIES");
                    fileReader = new FileReader(fileName);
                    prop.load(fileReader);
                    break;
            }
            //now can use this input stream as usually, i.e. to load as properties

            //Recuperation des valeurss
            map = new HashMap<>();
            map.put("DB_NAME", prop.getProperty("DB_NAME"));
            map.put("DB_IP", prop.getProperty("DB_IP"));
            map.put("DB_PORT", prop.getProperty("DB_PORT"));
            map.put("DB_USERNAME", prop.getProperty("DB_USERNAME"));
            map.put("DB_PASSWORD", prop.getProperty("DB_PASSWORD"));
            assert inputStream != null;
            inputStream.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return map;
    }

}
