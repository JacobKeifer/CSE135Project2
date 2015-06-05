package helpers;

/////////////////////////////NEW///////////////////////////
//class that stores information that is pulled from the 'combined' precomputed table
public class CombinedHelper {
   private String state;
   private String product;
   private float total;
   private float stateTotal;
   private float productTotal;
   private int id;
	   
public CombinedHelper(String state, float total,
		float stateTotal, float productTotal, int id, String product) {
	this.state = state;
	this.total = total;
	this.stateTotal = stateTotal;
	this.productTotal = productTotal;
	this.id = id;
	this.product = product;
}

public String getState() {
	return state;
}

public void setState(String state) {
	this.state = state;
}

public float getTotal() {
	return total;
}

public void setTotal(float total) {
	this.total = total;
}

public float getStateTotal() {
	return stateTotal;
}

public void setStateTotal(float stateTotal) {
	this.stateTotal = stateTotal;
}

public float getProductTotal() {
	return productTotal;
}

public void setProductTotal(float productTotal) {
	this.productTotal = productTotal;
}

public int getId(){
	return this.id;
}

public String getProduct(){
	return this.product;
}

}
