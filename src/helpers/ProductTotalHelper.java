package helpers;

public class ProductTotalHelper {
  private String productName;
  private float totalSales;
  
  public ProductTotalHelper(String productName, float sales){
    this.productName = productName;
    this.totalSales = sales;
  }
  
  public String getProductName(){
    return this.productName;
  }
  
  public float getSales(){
    return this.totalSales;
  }
  
  public void setSales(float totalSales){
	this.totalSales = 0;
  }
}
