/**
 * 
 */
var submitButton = document.getElementById("submit");
submitButton.onsubmit = function(){
	console.log("hello");
	var name = document.getElementById("name");
	var role = document.getElementById("role");
	var age = document.getElementById("age");
	var state = document.getElementById("state");
    if(name.value === ""){
    	document.getElementById("nameError").style.visibility = "visible";
    	return false;
    }
    else{
    	document.getElementById("nameError").style.visibility = "hidden";
    }
    
    if(role.value === ""){
    	document.getElementById("roleError").style.visibility = "visible";
    	return false;
    }
    else{
    	document.getElementById("roleError").style.visibility = "hidden";
    }
    
    if(age.value === ""){
    	document.getElementById("ageError").style.visibility = "visible";
    	return false;
    }
    else{
    	document.getElementById("ageError").style.visibility = "hidden";
    }
    
    if(state.value === ""){
    	document.getElementById("stateError").style.visibility = "visible";
    	return false;
    }
    else{
    	document.getElementById("stateError").style.visibility = "hidden";
    }
}