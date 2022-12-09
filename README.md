# billing

To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	    allprojects {
	    	repositories {
		   	...
		   	maven { url 'https://jitpack.io' }
	  	}
   	}
  
Step 2. Add the dependency

     	dependencies {
	        implementation 'com.github.gold-devoloper:billing:1.0.0'
   	}
  
Open the AndroidManifest.xml of your application and add this permission:

        <uses-permission android:name="com.android.vending.BILLING" />
	
	
For builds that use minSdkVersion lower than 24 it is very important to include the following in your app's build.gradle file:

     		  android {
         		  compileOptions {
 	 		  coreLibraryDesugaringEnabled true

  			  sourceCompatibility JavaVersion.VERSION_11
  			  targetCompatibility JavaVersion.VERSION_11
			  }
			}

		dependencies {
 			 coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:1.1.5'
		}


For builds that use minSdkVersion lower than 21 add the above and also this:

        android {
   	 defaultConfig {
         multiDexEnabled true
        }
       }
	
This step is required to enable support for some APIs on lower SDK versions that aren't available natively only starting from minSdkVersion 24.
	
Create an instance of BillingConnector class. Constructor will take 2 parameters:
Context
License key from Play Console

		billingConnector = new BillingConnector(this, "license_key")
                .setConsumableIds(consumableIds)
                .setNonConsumableIds(nonConsumableIds)
                .setSubscriptionIds(subscriptionIds)
                .autoAcknowledge()
                .autoConsume()
                .enableLogging()
                .connect();
		
Implement the listener to handle event results and errors:

                billingConnector.setBillingEventListener(new BillingEventListener() {
            @Override
            public void onProductsFetched(@NonNull List<ProductInfo> productDetails) {
                /*Provides a list with fetched products*/
            }

            @Override
            public void onPurchasedProductsFetched(@NonNull ProductType productType, @NonNull List<PurchaseInfo> purchases) {
                /*Provides a list with fetched purchased products*/
                
                /*
                 * This will be called even when no purchased products are returned by the API
                 * */
            }

            @Override
            public void onProductsPurchased(@NonNull List<PurchaseInfo> purchases) {
                /*Callback after a product is purchased*/
            }

            @Override
            public void onPurchaseAcknowledged(@NonNull PurchaseInfo purchase) {
                /*Callback after a purchase is acknowledged*/
                
                /*
                 * Grant user entitlement for NON-CONSUMABLE products and SUBSCRIPTIONS here
                 *
                 * Even though onProductsPurchased is triggered when a purchase is successfully made
                 * there might be a problem along the way with the payment and the purchase won't be acknowledged
                 *
                 * Google will refund users purchases that aren't acknowledged in 3 days
                 *
                 * To ensure that all valid purchases are acknowledged the library will automatically
                 * check and acknowledge all unacknowledged products at the startup
                 * */
            }

            @Override
            public void onPurchaseConsumed(@NonNull PurchaseInfo purchase) {
                /*Callback after a purchase is consumed*/
                
                /*
                 * Grant user entitlement for CONSUMABLE products here
                 *
                 * Even though onProductsPurchased is triggered when a purchase is successfully made
                 * there might be a problem along the way with the payment and the user will be able consume the product
                 * without actually paying
                 * */
            }

            @Override
            public void onBillingError(@NonNull BillingConnector billingConnector, @NonNull BillingResponse response) {
                /*Callback after an error occurs*/
                
                switch (response.getErrorType()) {
                    case CLIENT_NOT_READY:
                        //TODO - client is not ready yet
                        break;
                    case CLIENT_DISCONNECTED:
                        //TODO - client has disconnected
                        break;
                    case PRODUCT_NOT_EXIST:
                        //TODO - product does not exist
                        break;
                    case CONSUME_ERROR:
                        //TODO - error during consumption
                        break;
                    case CONSUME_WARNING:
                        /*
                         * This will be triggered when a consumable purchase has a PENDING state
                         * User entitlement must be granted when the state is PURCHASED
                         *
                         * PENDING transactions usually occur when users choose cash as their form of payment
                         *
                         * Here users can be informed that it may take a while until the purchase complete
                         * and to come back later to receive their purchase
                         * */
                        //TODO - warning during consumption
                        break;
                    case ACKNOWLEDGE_ERROR:
                        //TODO - error during acknowledgment
                        break;
                    case ACKNOWLEDGE_WARNING:
                        /*
                         * This will be triggered when a purchase can not be acknowledged because the state is PENDING
                         * A purchase can be acknowledged only when the state is PURCHASED
                         *
                         * PENDING transactions usually occur when users choose cash as their form of payment
                         *
                         * Here users can be informed that it may take a while until the purchase complete
                         * and to come back later to receive their purchase
                         * */
                        //TODO - warning during acknowledgment
                        break;
                    case FETCH_PURCHASED_PRODUCTS_ERROR:
                        //TODO - error occurred while querying purchased products
                        break;
                    case BILLING_ERROR:
                        //TODO - error occurred during initialization / querying product details
                        break;
                    case USER_CANCELED:
                        //TODO - user pressed back or canceled a dialog
                        break;
                    case SERVICE_UNAVAILABLE:
                        //TODO - network connection is down
                        break;
                    case BILLING_UNAVAILABLE:
                        //TODO - billing API version is not supported for the type requested
                        break;
                    case ITEM_UNAVAILABLE:
                        //TODO - requested product is not available for purchase
                        break;
                    case DEVELOPER_ERROR:
                        //TODO - invalid arguments provided to the API
                        break;
                    case ERROR:
                        //TODO - fatal error during the API action
                        break;
                    case ITEM_ALREADY_OWNED:
                        //TODO - failure to purchase since item is already owned
                        break;
                    case ITEM_NOT_OWNED:
                        //TODO - failure to consume since item is not owned
                        break;
                }
            }
        });
	

		Purchase a non-consumable/consumable product:
		billingConnector.purchase(this, "product_id");
		Purchase a subscription with a base plan:
		billingConnector.subscribe(this, "product_id");
		Purchase a subscription with multiple offers:
		billingConnector.subscribe(this, "product_id", 0);
		billingConnector.subscribe(this, "product_id", 1);
		Cancel a subscription:
		billingConnector.unsubscribe(this, "product_id");
Release instance
To avoid memory leaks don't forget to release the BillingConnector instance when it's no longer needed.
	@Override
   	 protected void onDestroy() {
    	    super.onDestroy();
     	   if (billingConnector != null) {
            billingConnector.release();
        }
   	 }
Kotlin
Kotlin is interoperable with Java and vice versa. This library works without any issues in Kotlin projects.

The sample app provides an example for Kotlin users.

Sample App
Go through the sample app to see a more advanced integration of the library.
