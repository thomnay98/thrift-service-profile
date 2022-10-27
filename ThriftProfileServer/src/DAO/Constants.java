/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

/**
 *
 * @author thom
 */
public class Constants {
    static final String GET_ONE = "SELECT * FROM Profiles WHERE PfID = ?";
    
    static final String INSERT= "INSERT INTO Profiles (Name, Email, Address, Phone) VALUES (?,?,?,?)";

    static final String DELETE = "DELETE FROM Profiles WHERE PfID = ?";

    static final String URL = "jdbc:mysql://localhost:3306/ThriftProfileServiceDB";
    static final String USERNAME = "root";
    static final String PASSWORD = "102798Ut!";
}
