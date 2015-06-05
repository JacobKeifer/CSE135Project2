package helpers;

//////////////////////////NEW//////////////////////////
//class for storing information on a sale
public class SalesHelper {
   private String stateName;
   private String productName;
   private float productTotal;
public SalesHelper(String stateName, String productName, float productTotal) {
	super();
	this.stateName = stateName;
	this.productName = productName;
	this.productTotal = productTotal;
}
public String getStateName() {
	return stateName;
}
public void setStateName(String stateName) {
	this.stateName = stateName;
}
public String getProductName() {
	return productName;
}
public void setProductName(String productName) {
	this.productName = productName;
}
public float getProductTotal() {
	return productTotal;
}
public void setProductTotal(float productTotal) {
	this.productTotal = productTotal;
}
   
   
}
