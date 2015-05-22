package helpers;

public class CategoriesDisplay {
  private String name;
  private String id;
  
  
  public CategoriesDisplay(String name, String id){
    this.name = name;
    this.id = id;
  }
  
  public String getName(){
    return this.name;
  }
  
  public String getId(){
    return this.id;
  }
  
}