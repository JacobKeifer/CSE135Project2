package helpers;

public class StateWithSales {
  private String name;
  private float total;
  
  
  public StateWithSales(String name, float total){
    this.name = name;
    this.total = total;
  }
  
  public String getName(){
    return this.name;
  }
  
  public float getTotal(){
	  return this.total;
  }
}