/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import static DAO.Constants.URL;
import static DAO.Constants.USERNAME;
import static DAO.Constants.PASSWORD;
import java.io.IOException;
import java.net.InetSocketAddress;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;   
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import net.spy.memcached.MemcachedClient;
import thriftprofile.Profile;
/**
 *
 * @author thom
 */
public class DAO { 

    private Connection connection;
    protected Hashtable<Integer, Profile> hash_table = new Hashtable<>();
    protected MemcachedClient mcc = null;
    
    
    public DAO() {
        try{
//            driver = new com.mysql.jdbc.Driver();
//            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void printSqlException(SQLException exception) {
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.err.println("SQL exception!");
        System.err.println("SQL state: " + exception.getSQLState() + "; Error code: " + exception.getErrorCode());
        System.err.println("Message: " + exception.getMessage());
        System.err.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    
    public Profile getProfileById(int id){
        
        Profile profile = new Profile();
        
        // caching with memcached
        
        try{
            mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1",11211));
            
            if(mcc.get(String.valueOf(id))!= null){
                
                profile = (Profile) mcc.get(String.valueOf(id));
                System.out.println("Get from Memcached");
                
            }else{
                try{
                    PreparedStatement statement = connection.prepareStatement(Constants.GET_ONE);
                    statement.setInt(1, id);
                    ResultSet rs = statement.executeQuery();

                    while(rs.next()){
                        profile = new Profile();
                        profile.setId(rs.getInt("PfID"));
                        profile.setName(rs.getString("Name"));
                        profile.setEmail(rs.getString("Email"));
                        profile.setAddress(rs.getString("Address"));
                        profile.setPhone(rs.getInt("Phone"));
                    }
                    
                    System.out.println("Database: " + profile);

                    if(profile.getId() != 0 ){
                        mcc.set(String.valueOf(profile.getId()), 2000, profile);
                    }

                    statement.execute();
                    statement.close();

                    System.out.println("Get from DB");

                }catch(SQLException e){
                    printSqlException(e);
                }
            }
        }catch (IOException ix){
            ix.printStackTrace();
        }
        
        //caching with hash_table
        
//        if(hash_table.get(id) != null){
//            profile = hash_table.get(id);
//        } else {
//            try{
//                PreparedStatement statement = connection.prepareStatement(Constants.GET_ONE);
//                statement.setInt(1, id);
//                ResultSet rs = statement.executeQuery();
//
//                while(rs.next()){
//                    profile = new Profile();
//                    profile.setId(rs.getInt("PfID"));
//                    profile.setName(rs.getString("Name"));
//                    profile.setEmail(rs.getString("Email"));
//                    profile.setAddress(rs.getString("Address"));
//                    profile.setPhone(rs.getInt("Phone"));
//                }
//
//                if(profile.getId() != 0 ){
//                    hash_table.put(profile.getId(), profile);
//                }
//
//                statement.execute();
//                statement.close();
//                
//                System.out.println("Get from DB");
//                
//            }catch(SQLException e){
//                printSqlException(e);
//            }
//        }
        
        return profile;
    }
    
    public List<Profile> getMultiProfiles(List<Integer> ids){
        List<Profile> profiles = new ArrayList<>();
        
        String query = createQuery(ids.size());
        
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = null;

            int parameterIndex = 1;
            
            for (Integer id : ids) {
                statement.setInt(parameterIndex++, id);
            }
            
            rs = statement.executeQuery();
                
            while(rs.next()){
                Profile profile = new Profile();
                profile.setId(rs.getInt("PfID"));
                profile.setName(rs.getString("Name"));
                profile.setEmail(rs.getString("Email"));
                profile.setAddress(rs.getString("Address"));
                profile.setPhone(rs.getInt("Phone"));

                profiles.add(profile);
            }
            
        }catch (SQLException e){
            e.printStackTrace();
            printSqlException(e);
        }
        return profiles;
    }
    
    public void addProfile(Profile profile) {
        try{
            PreparedStatement statement = connection.prepareStatement(Constants.INSERT);
            statement.setString(1, profile.getName());
            statement.setString(2, profile.getEmail());
            statement.setString(3, profile.getAddress());
            statement.setInt(4, profile.getPhone());
            statement.execute();
            statement.close();
            
            System.out.println("Thêm profile thành công!");
        }catch(SQLException e){
            e.printStackTrace();
            printSqlException(e);
        }
    }
    
    public void deleteProfile(int id){
        try{
            PreparedStatement statement = connection.prepareStatement(Constants.DELETE);
            statement.setInt(1, id);
            statement.execute();
            statement.close();
            
            if(hash_table.get(id) != null){
                hash_table.remove(id);
            }
            
            if(mcc.get(String.valueOf(id))!= null){
                mcc.delete(String.valueOf(id));
            }
            
            System.out.println("Xóa profile thành công!");
        }catch (SQLException e){
            e.printStackTrace();
            printSqlException(e);
        }   
    }
    
    private static String createQuery(int length) {
        String query = "SELECT * FROM Profiles WHERE PfID IN (";
        StringBuilder queryBuilder = new StringBuilder(query);
        for (int i = 0; i < length; i++) {
            queryBuilder.append(" ?");
            if (i != length - 1)
                queryBuilder.append(",");
        }
        queryBuilder.append(")");
        return queryBuilder.toString();
    }
}
