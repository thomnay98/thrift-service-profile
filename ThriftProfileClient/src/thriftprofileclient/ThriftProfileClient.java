/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftprofileclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import thriftprofile.Profile;
import thriftprofile.ProfileService;

/**
 *
 * @author thom
 */
public class ThriftProfileClient {
    private ProfileService.Client client;

    public ThriftProfileClient(){
        TTransport transport;
        try {
            transport = new TSocket("localhost", 7911);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            client = new ProfileService.Client(protocol);

        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
    
    public void getProfileById() throws TException {
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Nhap id can tim: ");
        
        Profile profile = client.getProfileById(Integer.parseInt(scan.nextLine()));
        
        System.out.println("Profile cần tìm");
        if(profile.getId() != 0){
            System.out.println("ID: " + profile.getId()+ ", Ten: " + profile.getName()
                            + ", Email: " + profile.getEmail() + ", DC: " 
                            + profile.getAddress() + ", SDT: "+ profile.getPhone());
            
        }else{
            System.out.println("Khong tim thay profile theo id tren! ");
        }
        
    }
    
    public void getMultiProfiles() throws TException {
        List<Profile> profiles = new ArrayList<>();
        
        ArrayList<Integer> listIds = new ArrayList<>();
        
        Scanner scan = new Scanner(System.in);
        
        System.out.println("So luong Profile can xem: ");
        int SL = scan.nextInt();
        
        for (int i=0; i<SL; i++){
            System.out.println("Nhap id thu "+ (i+1) +": ");
            listIds.add(scan.nextInt());
        }
        
        profiles = client.getMultiProfiles(listIds);
        
        
        if(profiles.size() >= 1){
            if(profiles.size() < SL){
                System.out.println("Chi tim thay "+ profiles.size()+" profile: ");
            }else{
                System.out.println("DS profile can tim: ");
            }
            for (int i = 0; i< profiles.size(); i++){
                System.out.println(profiles.get(i));
            }
        }else{
            System.out.println("Không tìm thấy profile nao");
        }
        
    }
    
    public void InsertProfile() throws TException {
        Profile profile = new Profile();
        
        Scanner scan = new Scanner(System.in);
        System.out.println("Nhap Ten: ");
        profile.setName(scan.nextLine());
        //String Name = scan.nextLine();
        System.out.println("Nhap Email: ");
        profile.setEmail(scan.nextLine());
        //String Email = scan.nextLine();
        System.out.println("Nhap DC: ");
        profile.setAddress(scan.nextLine());
        //String Address = scan.nextLine();
        System.out.println("Nhap sdt: ");
        profile.setPhone(Integer.parseInt(scan.nextLine()));
        //String Phone = scan.nextLine();
        
        client.addProfile(profile);
        
        System.out.println("Thêm Pf thành công!");
    }
    
    public void DeleteProfile() throws TException {
        Scanner scan = new Scanner(System.in);
        
        System.out.println("Nhap ID can xoa: ");
        client.deleteProfile(scan.nextInt());
        
        System.out.println("Xoa Pf thành công!");
    }
}
