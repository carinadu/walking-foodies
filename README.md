# Android application walking foodie
It is a android health app that allows people to manage their daily calorie input and output. For calorie input, people can enter their meals/snacks by typing or even by speaking. For calorie output, a step counting module is implemented to count the steps of the user and the calories burned can be calculated based on the steps and user's profile. The daily activity and history data is showed in well-designed charts.

## UI
The main page - showing the current steps the user made. The color of the circle has differnt meaning. When it turns red means the user should walk more.
Enter food page - user can enter by typing and speaking.
Daily activity panel - shows the calories input and output in user friendly charts.
User profile page.
User profile - weight gain and lose.
![main](images/main_walking.png)
![enter meals](images/enterMealLarge.png)
![charts](images/dailyActivity.png)
![profile](images/profile2.png)
![profile2](images/profile.png)

## Main work
I designed and implemented the database, daily activity panel,  profile panel and weight gain and lose.

# Wiki Search based on Hadoop
Wiki Search is user-friendly search engine using MapReduce framework. The search engine supports fast speed query by applying index compression and clever partition. Moreover, it can understand and parse query in conjunctive normal form and the results are well ranked by giving different scores for AND, OR and PHRASE operations. After the search results come up, all the keywords except those marked by NOT in the query will be highlighted in the documents in a dynamic ways.

## Architecture Desingn
![Architecture](images/architecture_wiki_search.png)

## UI
The entrance of the search engine-search bar:

![Search bar](images/ui_wiki_search.png)

The result page:

![results](images/result_wiki_search.png)

The detail page:

![more infor](images/more_info.png)

## Main Work
I implemented the corpus building, query partition, conjunctive norm form analysis and result ranking parts. 


