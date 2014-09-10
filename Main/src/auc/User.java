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
public abstract class User {
    private int id;
    protected String name;
    protected String login;
    protected String password;
    
    public User(String name, String login, String password){
        this.name=name;
        this.login=login;
        this.password=password;
    }
    public boolean enter(String login, String password){
        if(login==this.login && password==this.password)return true;
        return false;
    }
    public String getName(){
        return name;
    }
    public String toString(){
        return ""+name+" "+login;
    }
    public String getLogin(){
        return login;
    }
    public String getPassword(){
        return password;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
