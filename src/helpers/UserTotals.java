package helpers;

public class UserTotals {
  private String userName;
  private float totalSales;
  
  public UserTotals(String userName, float sales){
    this.userName = userName;
    this.totalSales = sales;
  }
  
  public String getUserName(){
    return this.userName;
  }
  
  public float getSales(){
    return this.totalSales;
  }
  
  public void setSales(float totalSales){
	this.totalSales = 0;
  }
}
