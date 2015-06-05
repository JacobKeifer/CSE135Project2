package helpers;

public class ProductTotalHelper {
  private String productName;
  private float total;
  
  public ProductTotalHelper(String productName, float total){
    this.productName = productName;
    this.total = total;
  }
  
  public String getProductName(){
    return this.productName;
  }
  
  public float getTotal(){
    return this.total;
  }
  
}
