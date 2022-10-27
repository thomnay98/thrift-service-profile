/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thriftprofileclient;

import java.util.Scanner;
import org.apache.thrift.TException;

/**
 *
 * @author thom
 */
public class Main {
    public static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) throws TException {
        String choose = null;
        boolean exit = false;
        ThriftProfileClient tClient = new ThriftProfileClient();
 
        // show menu
        showMenu();
        while (true) {
            choose = scanner.nextLine();
            switch (choose) {
            case "1":
                tClient.getProfileById();
                System.out.println("---------------------------");
                break;
            case "2":
                tClient.getMultiProfiles();
                System.out.println("---------------------------");
                break;
            case "3":
                tClient.InsertProfile();
                System.out.println("---------------------------");
                break;
            case "4":
                tClient.DeleteProfile();
                System.out.println("---------------------------");
                break;
            case "0":
                System.out.println("exited!");
                exit = true;
                break;
            default:
                System.out.println("Lỗi! Vui lòng chọn hành động bên dưới");
                break;
            }
            if (exit) {
                break;
            }
            // show menu
            showMenu();
        }
    }
 
    /**
     * create menu
     */
    public static void showMenu() {
        System.out.println("-----------MENU------------");
        System.out.println("1. Tim Profile theo ID.");
        System.out.println("2. Tim DS profile.");
        System.out.println("3. Them profile.");
        System.out.println("4. Xoa profile.");
        System.out.println("0. Thoát.");
        System.out.println("---------------------------");
        System.out.print("Chọn hành động: ");
    }
}
