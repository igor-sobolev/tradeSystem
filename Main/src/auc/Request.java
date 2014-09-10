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
public class Request {
    private Client requester;
    private String product;
    private int price;
    private int count;
    private int type;
    
    public Request(Client requester, String product, int price, int count, int type){
        this.requester=requester;
        this.product=product;
        this.price=price;
        this.count=count;
        this.type=type;
    }
    public Client getRequester(){
        return requester;
    }
    public String getProduct(){
        return product;
    }
    public int getPrice(){
        return price;
    }
    public int getCount(){
        return count;
    }
    public int getType(){
        return type;
    }
    public void setCount(int ncount){
        count = ncount;
    }
    public String toString(){
        return "type:"+type+" product:"+product+" price:"+price+" count:"+count+" requester:"+requester;
    }
}
