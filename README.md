# Popular Movies App Implementation

Most of us can relate to kicking back on the couch and enjoying a movie with friends and family. In this project, you’ll build an app to allow users to discover the most popular movies playing.

#### Project requirements Stage 1

- App is written solely in the Java Programming Language.

- Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

- UI contains an element (i.e a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

- UI contains a screen for displaying the details for a selected movie.

- Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

- App utilizes stable release versions of all libraries, Gradle, and Android Studio.

#### User Interface - Function

- When a user changes the sort criteria (“most popular and highest rated”) the main view gets updated correctly.

- When a movie poster thumbnail is selected, the movie details screen is launched.

#### Network API Implementation

- In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

#### General Project Guidelines

http://udacity.github.io/android-nanodegree-guidelines/core.html

NOTE: For Stage 1 of the Popular Movies App, it is okay if the app does not restore the data using onSaveInstanceState/onRestoreInstanceState
