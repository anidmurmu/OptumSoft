Architecture
1) MVVM
2) Clean Architecture(module: data, domain and ui)
   data -> responsible for dealing with data
   domain -> responsible for core operations
   ui -> responsible for dealing with ui
   
data flows from data layer to domain layer and to ui layer and vice-versa

Components use
1) MPAndroidChart for drawing chart
2) Data binding
3) Coroutines for threading
4) Retrofit for api call
5) Socket IO for subscribing to event
6) Hilt for dependency injection


Functionality Done
1) On selecting any sensor, graph will be shown(by default selected sensor in first one)
2) Graph updates when it is update, deleted or initialized
3) Menu to select "recent" and "minute" scale

Functionality Left out
1) Showing Deviation
2) Showing multiple graphs

Known Issues
1) App crashes sometimes because of threading

Branch to be used is : development
