# CheckMeOut

CheckMeOut is a a supermarket self checkout app that also doubles as a financial tracker. It allows one to scan goods add them to a cart and Checkout.

## Author
- [Ruth Mwangi](https://github.com/Ruth-Mwangi)

## Features
* Scanning of items
* Adding items to cart

## Upcoming Features
* Calculating total of Items
* Checkout
* Adding Checkout data to the Expense feature
* Fix issue of scanning same item multiple times
* Maintain data in recyclerview when switching of view occurs and reset recyclerview after checkout
* drawing a rectangle around the barcode when detected

## Getting Started

* Clone the repo
* Open the terminal and navigate to appropriate folder
* type git clone followed by the link you just copied
* add appropriate dependencies
* open the file and run the MainActivity.java class
* Click on the start shopping link
* Scan items
* If Scan image is unsuccessful [click here see Api Documentation](https://checkmeout-api.herokuapp.com/)<br />

<img src="images/crayons.jpg" width="200"> 
<img src="images/crisps.jpg" width="200">
<img src="images/Screenshot_20200608-070331_CheckMeOut.jpg" width="200"><br />
* Click on the basket icon when you are done <br />
<img src="images/expenses.jpg" width="200"><br />
* Ensure all items scanned are on the list then proceed to click on the checkout button
* You can view all expenses by clicking on the expenses option in the menu <br />
<img src="images/Screenshot_20200608-070525_CheckMeOut.jpg" width="200">

 
### Prerequisites

* Android Studio
* Jdk

## Technologies Used
* Java 
* The libraries below were useful in the development of the application <br />
 ![image](https://user-images.githubusercontent.com/22973263/83426797-63572200-a438-11ea-806d-e374c8386669.png)
### Testing
For testing i used the following libraries
* androidTestImplementation 'androidx.test.ext:junit:1.1.1'
* androidTestImplementation 'androidx.test:rules:1.2.0'
* androidTestImplementation 'androidx.test:runner:1.2.0'
* androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
* testImplementation 'org.robolectric:robolectric:4.3.1'
### Google vision
* implementation 'com.google.android.gms:play-services-vision:20.0.0'
### Firebase
* implementation 'com.google.firebase:firebase-analytics:17.4.2'
* implementation 'com.firebaseui:firebase-ui-database:3.3.1'
* implementation 'com.google.firebase:firebase-core:17.4.2'
* implementation 'com.google.firebase:firebase-database:19.3.0'
* implementation 'com.google.firebase:firebase-auth:19.3.1'
### Api
* implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
* implementation 'com.squareup.okhttp3:okhttp:3.12.0'

 
 
 
## Apis Used
* [Mobile vision api](https://developers.google.com/vision/android/barcodes-overview) for the scanning of barcodes.
* [checkmeout-api](https://checkmeout-api.herokuapp.com/) used to retrieve items using barcodes


## Support and contact details
If you come across any problems you can reach me at rwmwwangi96@gmail.com or use these resources:
* [Mobile vision api](https://developers.google.com/vision/android/barcodes-overview)



