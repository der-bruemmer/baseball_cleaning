# Query Microsoft Emotion API

Tool to query Microsoft Emotion Smile API.

Parameters:
* apiKey: the Microsoft Emotion API key
* inFile: The CSV file containing player data created via the cleaner contained in this project
* inFolder: The folder containing the pictures to rate
* outFile: The CSV file results are written to

The tool will query 9000 pictures using the API, creating a CSV file containing the columns: 

filename,anger,contempt,disgust,fear,happiness,neutral,sadness,surprise

The free Emotion API is capped at 10000 requests per month, so projects with more pictures will have to be timed accordingly