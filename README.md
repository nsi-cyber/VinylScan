# VinlyScan

VinlyScan is an Android application that allows users to scan vinyl record barcodes and instantly access detailed information about the records, including artist, album, release year, and more.




<p align="center">

  <img src="https://github.com/user-attachments/assets/8839313a-b23b-47a3-aae8-7df212a9479d" width="200" />
  <img src="https://github.com/user-attachments/assets/7c720eb3-a017-4775-836e-07abdc915dcf" width="200" />
  <img src="https://github.com/user-attachments/assets/2f5b7ab3-22bf-4587-8948-3457c7fd3eb0" width="200" />
  
  <img src="https://github.com/user-attachments/assets/e3e465a1-b70c-4bef-9229-524ba0355af0" width="200" />
</p>

## Features

- **Barcode Scanning**: Quickly scan vinyl record barcodes to retrieve detailed information.
- **Detailed Record Information**: Access comprehensive details about each record, including artist, album, genre, release year, and tracklist.
- **User-Friendly Interface**: Navigate the app with ease, thanks to its intuitive and clean design.

## Technologies Used

### Android

- **Retrofit**: For getting vinly data from Discogs API.
- **ML Kit**: For getting barcode data.
- **Kotlin**: Used as the programming language for the app.
- **Android Jetpack**: Modern Android development components:
  - **MutableState**: For observable data holders.
  - **ViewModel**: For UI-related data that survives configuration changes.
  - **Navigation**: For handling navigation within the app.
- **Coroutines**: For asynchronous programming.
- **Kotlin Flow**: For managing asynchronous data streams.
- **Dagger-Hilt**: For dependency injection.
- **Glide**: For image loading and caching.
- **Lottie**: For beautiful animations and enhancing the user experience.


## Detailed Description

### Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern along with Clean Architecture principles, ensuring a separation of concerns and making the code more maintainable, scalable, and testable.

### Data Fetching

- **Coroutines and Flow**: Used to handle asynchronous operations and manage data streams.



