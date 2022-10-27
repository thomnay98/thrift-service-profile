/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftprofileserver;

import DAO.DAO;
import java.util.ArrayList;
import java.util.List;
import org.apache.thrift.TException;
import thriftprofile.InvalidOperation;
import thriftprofile.Profile;
import thriftprofile.ProfileService;

/**
 *
 * @author thom
 */
public class ThriftProfileHandler implements ProfileService.Iface{
    private DAO dao;
    
    public ThriftProfileHandler(){
        dao = new DAO();    
    }

    @Override
    public Profile getProfileById(int id) throws InvalidOperation, TException {
        Profile profile = new Profile();
        
        if(dao.getProfileById(id)!= null){
            return dao.getProfileById(id);
        }else return profile;
    }

    @Override
    public List<Profile> getMultiProfiles(List<Integer> ids) throws InvalidOperation, TException {
        List<Profile> profiles = new ArrayList<>();
        
        if(dao.getMultiProfiles(ids)!= null){
            return dao.getMultiProfiles(ids);
        }else return profiles;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addProfile(Profile profile) throws InvalidOperation, TException {
        dao.addProfile(profile);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteProfile(int id) throws InvalidOperation, TException {
        dao.deleteProfile(id);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
