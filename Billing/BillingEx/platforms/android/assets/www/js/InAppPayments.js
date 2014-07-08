
	function successHandler (result) {
		var strResult = "";
        if(typeof result === 'object') {
            strResult = JSON.stringify(result);
        } else {
            strResult = result;
        }
        alert("SUCCESS: \r\n"+strResult );
	}
			
	function errorHandler (error) {
	    alert("ERROR: \r\n"+error );
	}
	
	function initSuccessHandler (result) {
		var strResult = "";
        if(typeof result === 'object') {
            strResult = JSON.stringify(result);
        } else {
            strResult = result;
        }
        alert("Init SUCCESS: \r\n"+strResult );
        
        inappbilling.mapProducts(successHandler, errorHandler, ["1023608","gas","1023609","infinite_gas"])
        
	}

    // Click on init button
	function init(){
		// Initialize the billing plugin
		inappbilling.init(initSuccessHandler, errorHandler, {showLog:true});
	}

	// Click on purchase button
	function buy(){
		// make the purchase
		inappbilling.buy(successHandler, errorHandler,"gas");
	}
			
	// Click on ownedProducts button
	function ownedProducts(){
		// Initialize the billing plugin
		inappbilling.getPurchases(successHandler, errorHandler);
	}

    // Click on Consume purchase button
    function consumePurchase(){
		inappbilling.consumePurchase(successHandler, errorHandler, "gas");
	}

    // Click on subscribe button
    function subscribe(){
		// make the purchase
        inappbilling.subscribe(successHandler, errorHandler,"infinite_gas2");
	}
            
	// Click on Query Details button
	function getDetails(){
		// Query the store for the product details
		inappbilling.getProductDetails(successHandler, errorHandler, ["gas","infinite_gas"]);
	}
			
	// Click on Get Available Products button
	function getAvailable(){
		// Get the products available for purchase.
		inappbilling.getAvailableProducts(successHandler, errorHandler);
	}		