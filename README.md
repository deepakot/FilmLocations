# FilmLocations
Problem Statement: Create an app that shows on a map where movies have been filmed in San Francisco. The user should be able to filter the view using autocompletion search.

Solution: Now the main problem here is that the json gives us the repeated results only for the changed locations for a single movie. So I took a movieList to store the list of the different movies and a HashMap to store the places for a single movie. I took a recyclerview and filled the views with the moviesList and when the user clicks on a movie, the app takes the user to another activity in which we pass a list of the locations where this particular clicked movie has been filmed in San Fransisco. And with the Google Map API I display the locations on the Map. Stored the movieList in SharedPreferences to prevent every time first call to server from getting data. Currently I'm displaying data if present in SharedPreferences otherwise get through Network call.

Reasons Behind Technical Choices: 

1. RecyclerView to display list of items: The RecyclerView is much more powerful, flexible and a major enhancement over ListView. We have LayoutManager which supports LinearLayoutManager, StaggeredLayoutManager and GridLayoutManager. Using RecyclerView animating the views becomes much easier.  
2. SearchView.OnQueryTextListener: To implement searchview in the app and filter out the list as soon as user types the movie name.
3. Volley Library for network call: One of the many advantages of volley is that, you don’t need to write code for accessing network. All of this is managed by volley itself. Whenever a new network request is created, a background thread is spawned for network processing. Whose life-cycle is maintained by volley itself. With Volley you also get many more out of the box features like:
    Retry Mechanism
    Caching
    Multiple Request Types
        JsonObjectRequest
        JsonArrayRequest
        StringRequest
        ImageRequest
4. Google Maps: To display the shooting locations of the movies simply by passing the latitude, longitude. Also we can have StreetView or other views using that as per the requirements.
5. Gson: Gson is a library that is used to convert Java Objects(in my case List<Movie>) into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object.

Things I could've done if I were to spent more time on the project: I would've used more animations in the app. Could've added SplashActivity at the launch of the app. And could've added Overlay with user instructions on how to use the app. 
Also wanted to have a TabLayout on the Home page which was supposed to contain the Food Trucks problem solution in the second tab.

But could only get today for working on the app. Hope you like it. :)
