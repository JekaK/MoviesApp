# MoviesApp

Find movies to be added to your personal favorites lists, search for movies by the title.
Review trending, popular and top rated movies.

<i>Note: work in progress.</i>

The TMDb movie database API: https://www.themoviedb.org/documentation/api

Please, obtain your own API key in order to work with the source code.

<h3> Active branches: </h3>

<ul><b> main </b> - contains stable implementation of main functionality.</ul>

<h3> Architecture approach </h3>

<img src="https://miro.medium.com/max/1400/1*ohFythvIKvgVUy_08dF4Ag.png" width="50%">

In my case I'm using MVI based architecture with Jetpack Compose in Single Activity App and powered by Orbit MVI with Clean Architecture on board.

<h3> Main libraries used: </h3>

<b> Retrofit2 </b> - A type-safe HTTP client for Android and Java<br>
<b> Hilt </b> - as dependency injection library based on Dagger2<br>
<b> Kotlin Coroutines & Flow </b> - for execution of asynchronous tasks<br>
<b> Room </b> - for work with SQLite database<br>
<b> Gson </b> - for parsing JSON responses from TMDB<br>
<b> Paging </b> - for loading data in pages and showing it in a Recycler View<br>
<b> Lifecycle </b> - for performing actions in response to a change in the lifecycle status of activities and fragments<br>
<b> Accompanist libs </b> - for supporting different features such as Flow Layout that not presented in official Google Compose, but provided by Google as separate lib<br>
<b> Lanscapist Coil </b> - Coil base library for loading images<br>
<b> Orbit MVI </b> - for using features of MVI such as reducers, state handling and side effects dispatching<br>

---

<p align="center">
  <img src="https://github.com/JekaK/MoviesApp/blob/main/images/Pixel%20True%20Mockup.png" width="100%">
  <img src="https://github.com/JekaK/MoviesApp/blob/main/images/Pixel%20True%20Mockup%20(1).png" width="100%">
</p>

<p align="center">
  <img src="https://github.com/JekaK/MoviesApp/blob/main/images/Pixel%20True%20Mockup%20(2).png" width="100%">
  <img src="https://github.com/JekaK/MoviesApp/blob/main/images/Pixel%20True%20Mockup%20(3).png" width="100%">
</p>

---

![](https://github.com/JekaK/MoviesApp/blob/main/appm.gif)
