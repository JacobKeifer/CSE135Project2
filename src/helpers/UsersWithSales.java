package helpers;

public class UsersWithSales {
  private String name;
  private String productName;
  private float totalSales;
  private float uTotal;
  private float pTotal;
  
  
  public UsersWithSales(String name, String productName, float sales, float uTotal, float pTotal){
    this.name = name;
    this.productName = productName;
    this.totalSales = sales;
    this.uTotal = uTotal;
    this.pTotal = pTotal;
  }
  
  public String getName(){
    return this.name;
  }
  
  public String getProductName(){
    return this.productName;
  }
  
  public float getSales(){
    return this.totalSales;
  }
  
  public float getUserTotal(){
	    return this.uTotal;
	  }
  
  public float getProductTotal(){
	    return this.pTotal;
	  }
  
  public void setSales(float totalSales){
	this.totalSales = 0;
  }
}
