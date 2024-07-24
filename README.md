# WhereThereIs4Cast
==================

A play project that shows the weather forecast. This is the
repository: https://github.com/SelloN/WhereThereIs4Cast

**WhereThereIs4Cast** is a fully functional Android app built entirely with Kotlin and Jetpack Compose. It
follows Android design and development best practices using MVVM.

* Integration with a remote data API requiring authentication (access-token, bearer-token, API-key).
    - The authentication done on the project was only using an API-key. As requested, 3 screens were
      created, the MainScreen Screen, Search and Favourite Screen for offline saving of locations.
* Application with one mandatory screen and up to 2 optional screens.
    - The app has only three screens
* Some of the aspects of the UI components have to be data-driven. These can be background color,
  background image, font color, etc.
    - Background images change according to the weather conditions as specified in the requirements
      documents
    - Main screen images (icons) changes on the list as these are taken from the API calls
* The application must render at least one local or remote image asset.
    - All local images used on the app are the ones that came with the project.
    - All local images transition according to weather conditions from the API
* Implementation of layers: presentation, domain, data.
    - Presentation Layer - I have placed all screens under the screen package.
    - Data Layer - I have placed database operations under the data package with respective models
      in the model package. Repositories are also placed in their package.
    - Domain - All API calls are in the network package, ViewModels are with their screens packages
      since one ViewModel exists for each screen. Repositories as well.
    - Following dependency injection best practices, the repository takes data sources as
      dependencies in its constructor. I followed the android best practices on this
      page: https://developer.android.com/topic/architecture/domain-layer#:~:text=The%20domain%20layer%20is%20an,is%20reused%20by%20multiple%20ViewModels.
    - The MVVM guide of the app was followed from this
      article: https://medium.com/@shakeabdulrahman/mastering-android-development-clean-architecture-mvvm-and-offline-functionality-with-android-717271dc45ab
    - The MVVM architecture was used because it offers enough data abstraction for an app of this
      size. It is also the most flexible to changing it to the latest architecture, Clean
      Architecture. The only reason I don’t call this approach a Clean Architecture is because I
      did’t want to use the Use-Cases strategy.
* Navigation: Top app bar has a drop down menu for seeing Favourite items. You can save a favourite
  location by clicking on the top left corner.

* An optional feature leveraging local persistence.
    - Done with favorites for offline storage
    - Room was used

The following are the third party
libraries used. The others are all from the google framework (jetbrains included).

Project organization

* Version control repository
    - Github: https://github.com/SelloN/WhereThereIs4Cast
* Branching strategies
    - We make the changes from a feature branch (feature/noted-improvements) and merge into the
      master branch (main in this case)
* File and folder structure follows the MVVM architecture
* Naming conventions and coding, commenting style are done in Jetpack Compose coding and naming
  style

- All conventions were applied according to Jetpack Compose and Kotlin coding standards.

Third party libs

*retrofit = {group = "com.squareup.retrofit2", name="retrofit", version.ref = "retrofit"}
gson = {group = "com.google.code.gson", name="gson", version.ref = "gson"}
converter-gson = {group = "com.squareup.retrofit2", name="converter-gson", version.ref = "retrofit"}
coil-compose = { module = "io.coil-kt:coil-compose", version.ref = "coilCompose" }