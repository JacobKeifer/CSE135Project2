package helpers;

public class StateWithSales {
  private String name;
  private String productName;
  private float totalSales;
  private float sTotal;
  private float pTotal;
  
  
  public StateWithSales(String name, String productName, float sales, float uTotal, float pTotal){
    this.name = name;
    this.productName = productName;
    this.totalSales = sales;
    this.sTotal = uTotal;
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
  
  public float getStateTotal(){
	    return this.sTotal;
	  }
  
  public float getProductTotal(){
	    return this.pTotal;
	  }
  
  public void setSales(float totalSales){
	this.totalSales = 0;
  }
}
