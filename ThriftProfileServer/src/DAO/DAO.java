/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import static DAO.Constants.URL;
import static DAO.Constants.USERNAME;
import static DAO.Constants.PASSWORD;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;   
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import thriftprofile.Profile;
/**
 *
 * @author thom
 */
public class DAO {
    
    private Driver driver;
    private Connection connection;
    protected Hashtable<Integer, Profile> hash_table = new Hashtable<>();
    
    public DAO() {
        try{
//            driver = new com.mysql.jdbc.Driver();
//            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Profile getProfileById(int id){
        
        Profile profile = new Profile();
        System.out.println("hash: " + hash_table.size());
        boolean flag = false;
        
        if(hash_table.size()>0){
            for (Map.Entry<Integer, Profile> entry : hash_table.entrySet()){
                if(entry.getKey() == id){
                    profile = entry.getValue();
                    flag = true;
                    break;
                }else{
                    flag = false;
                }
            } 
        }
        
        if(flag == false){
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

                if(profile.getId() != 0 ){
                    hash_table.put(profile.getId(), profile);
                }

                statement.execute();
                statement.close();
                
                System.out.println("Get from DB");
                
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        
        return profile;
    }
    
    public List<Profile> getMultiProfiles(List<Integer> ids){
        List<Profile> profiles = new ArrayList<>();
        
        String query = createQuery(ids.size());
        
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = null;

            int parameterIndex = 1;
            
//            for (Iterator < Integer > iterator = ids.iterator(); iterator.hasNext();) {
//                Integer id = iterator.next();
//                statement.setInt(parameterIndex++, id);
//            }
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
        }
    }
    
    public void deleteProfile(int id){
        try{
            PreparedStatement statement = connection.prepareStatement(Constants.DELETE);
            statement.setInt(1, id);
            statement.execute();
            statement.close();
            
            if(hash_table.size()>0){
                for (Map.Entry<Integer, Profile> entry : hash_table.entrySet()){
                    if(entry.getKey() == id){
                        hash_table.remove(id);
                        break;
                    }
                }
            }
            
            System.out.println("Xóa profile thành công!");
        }catch (SQLException e){
            e.printStackTrace();
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
