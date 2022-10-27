/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftprofileserver;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import thriftprofile.ProfileService;

/**
 *
 * @author thom
 */
public class ThriftProfileServer {

    public void start(){
        try {
            TServerSocket serverTransport = new TServerSocket(7911);
            
            ThriftProfileHandler handler = new ThriftProfileHandler();
 
            ProfileService.Processor<ProfileService.Iface> processor = new ProfileService.Processor<ProfileService.Iface>(handler);
 
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
            
            System.out.println("Starting server on port 7911 ...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        ThriftProfileServer server = new ThriftProfileServer();
        server.start();
    }
}
