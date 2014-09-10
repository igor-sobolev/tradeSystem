/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package auc;

/**
 *
 * @author Игорь
 */
public class Client extends User{
    public Client(String name, String login, String password){
        super(name,login, password);
    }
    public void takeResult(Request request){
        System.out.println("Request:"+request+" has been processed!");
    }
}
