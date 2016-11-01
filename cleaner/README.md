# Clean baseball player records

Usage: run BaseballCleaner $pictureFolder $scrapedDataCsvFile $outFileName

Creates a tab-separated value file with following fields:

* fullname - Full name of the player
* nickname - Short name or nick name
* image - name of the image file
* team - Team. Players regularly change teams mid season, so they can have multiple teams. However, only one will be in here, so this is uncomplete.
* hasCollege - Player has visited college (0 or 1)
* college - name of the college
* birthYear - year of birth, -1 if missing
* birthMonth - month of birth, -1 if missing
* birthDay - day of birth, -1 if missing
* birthPlace - birthplace, empty if missing
* deathYear - year of death, -1 if missing
* deathMonth - month of death, -1 if missing
* deathDay - day of death, -1 if missing
* deathPlace - place of death, empty if missing
* height(cm) - height in cm
* weight(kg) - weight in kg
* position - position player played in
* debutyear - year of first play, -1 if missing
* debutmonth - month of first play, -1 if missing
* debutday - day of first play, -1 if missing
* lastgameyear - year of last game, -1 if missing
* lastgamemonth - month of last game, -1 if missing
* lastgameday - day of last game, -1 if missing
* source - URL the data was scraped from
