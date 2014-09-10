/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package auc;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 *
 * @author Игорь
 */
public class TradeSystem {
    private List<User> users = new ArrayList<User>();
    private List<Request> workerRequests = new ArrayList<Request>();
    private List<Request> employerRequests = new ArrayList<Request>();
    private User currentUser;
    
    public void addUser(String name, String login, String password, String repeation, int type){
        if(findUser(login,password)!=null){
            if(type==1)
                users.add(new Admin(name,login,password));
            if(type==0)
                users.add(new Client(name,login,password));
        }
    }
    public User findUser(String login, String password){
        for(User u: users){
            if(u.getLogin().equals(login)){                
                if(u.getPassword().equals(password)){
                    return u;
                }
                else System.out.println("password error");
                return null;
            }
        }
        System.out.println("no such user");
        return null;
    }
    public void save(){
        String user = "admin";//Логин пользователя
          String password = "";//Пароль пользователя
          String url = "jdbc:mysql://localhost:3306/auction";//URL адрес
          Connection c = null;//Соединение с БД
          try{
               c = DriverManager.getConnection(url, user, password);//Установка соединения с БД
               Statement st = c.createStatement();//Готовим запроc
               ResultSet rs = null;               
               st.executeUpdate("DELETE FROM users");
               
               for(User u: users){
                   String name = u.getName();
                   String login = u.getLogin();
                   String pass = u.getPassword();
                   if(u instanceof Client){
                       st.execute("INSERT INTO `auction`.`users` (`name`, `login`, `password`, `admin_level`)"+
                                                        "VALUES('"+name+"', '"+login+"', '"+pass+"', 0)");
                       System.out.println("#lod: client "+u+" saved");
                   }  
                   if(u instanceof Admin){
                       st.execute("INSERT INTO `auction`.`users` (`name`, `login`, `password`, `admin_level`)"+
                                                        "VALUES('"+name+"', '"+login+"', '"+pass+"', 1)");
                       System.out.println("#lod: admin "+u+" saved");
                   } 
               }
               for(Request wr: workerRequests){
                   String name = wr.getProduct();
                   int count = wr.getCount();
                   int price = wr.getPrice();
                   Client cl = wr.getRequester();
                   int id=-1;
                   rs = st.executeQuery("select * from users");
                   while(rs.next()){
                       if(cl.getName().equals(rs.getString("name")) && cl.getLogin().equals(rs.getString("login")) && cl.getPassword().equals(rs.getString("password")))
                           id=rs.getInt("id");
                   }
                   st.execute("INSERT INTO requests (`product_name`, `price`,`count`,`type`, `user_id`)" +
                                         "VALUES ('"+name+"', '"+price+"', '"+count+"', 1, '"+id+"')");
                   System.out.println("#lod: Request "+wr+" saved");
               }
          } catch(Exception e){
               e.printStackTrace();
          }
          finally{
               //Обязательно необходимо закрыть соединение
               try {
                    if(c != null)
                    c.close();
               } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
               }
          }
    }
    public void load(){
          String user = "admin";//Логин пользователя
          String password = "";//Пароль пользователя
          String url = "jdbc:mysql://localhost:3306/auction";//URL адрес
          Connection c = null;//Соединение с БД
          try{
               c = DriverManager.getConnection(url, user, password);//Установка соединения с БД
               Statement st = c.createStatement();//Готовим запроc
               ResultSet rs = st.executeQuery("select * from users");//Выполняем запрос к БД, результат в переменной rs
               while(rs.next()){
                    if(rs.getInt("admin_level")==0){
                        String pass = rs.getString("password");
                        String name = rs.getString("name");
                        String login = rs.getString("login");
                        Client cl = new Client(name,login,pass);
                        cl.setId(rs.getInt("id"));
                        users.add(cl);
                        System.out.println("#log: "+cl+" was added as client.");                        
                    }
                    if(rs.getInt("admin_level")==1){
                        String pass = rs.getString("password");
                        String name = rs.getString("name");
                        String login = rs.getString("login");
                        Admin ad = new Admin(name,login,pass);
                        users.add(ad);
                        System.out.println("#log: "+ad+" was added as admin.");                        
                    }
               }
               rs = st.executeQuery("select * from requests");
               while(rs.next()){
                   if(rs.getInt("type")==0){
                       String product = rs.getString("product_name");
                       int count = rs.getInt("count");
                       int price = rs.getInt("price");
                       int user_id = rs.getInt("user_id");
                       for(User u: users){
                           if(u instanceof Client){
                               if(u.getId()==user_id){
                                   Request req = new Request((Client)u,product,price,count,0);
                                   employerRequests.add(req);
                                   System.out.println("#log: "+req + " was added");
                               }
                           }
                       }
                   }
                   if(rs.getInt("type")==1){
                       String product = rs.getString("product_name");
                       int count = rs.getInt("count");
                       int price = rs.getInt("price");
                       int user_id = rs.getInt("user_id");
                       User currentUser = null;
                       for(User u: users){
                           if(u instanceof Client){
                               if(u.getId()==user_id){
                                   Request req = new Request((Client)u,product,price,count,1);
                                   workerRequests.add(req);
                                   System.out.println("#log: "+req+" was added");
                               }
                           }
                       }
                   }
               }
          } catch(Exception e){
               e.printStackTrace();
          }
          finally{
               //Обязательно необходимо закрыть соединение
               try {
                    if(c != null)
                    c.close();
               } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
               }
          }
    }
    public void processRequests(){
        for(Request wr: workerRequests){
            int maxPrice=wr.getPrice();
            Client winner = null;
            Client saller = wr.getRequester();
            Request r = null;
            Request worker = null;
            for(Request er:employerRequests){
                if(wr.getProduct().equals(er.getProduct()) && wr.getCount()==er.getCount() && er.getPrice()>maxPrice){
                    winner = er.getRequester();
                    maxPrice = er.getPrice();
                    r = er;
                    worker = wr;
                }
            }
            if(r!=null){
                winner.takeResult(r);
                saller.takeResult(r);
            }
        }            
    }
    public static void main(String[] args){
          TradeSystem ts = new TradeSystem();
          //ts.load();
          ts.processRequests();
          //ts.save();
    }
}
