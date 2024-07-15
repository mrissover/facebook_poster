# Facebook Post Scheduler Script

This Groovy script schedules posts on a Facebook page using the Facebook Graph API. It reads the access token and page ID from a properties file and the post details from a JSON file. The script schedules each post at the specified date and time.

## How It Works

1. **Load Properties**: The script reads the access token and page ID from a properties file named `facebook-poster.properties`.
2. **Read JSON File**: The script prompts the user for the path to a JSON file containing the posts to be scheduled.
3. **Parse JSON File**: The script parses the JSON file to extract the details of each post.
4. **Schedule Posts**: For each post, the script converts the specified date and time to a Unix timestamp and schedules the post using the Facebook Graph API.
5. **Handle Responses**: The script prints the response from the Facebook API for each scheduled post, indicating whether the scheduling was successful or not.

## Input Files

### Properties File

The properties file, named `facebook-poster.properties`, should be placed in the same directory as the script. It contains the access token and page ID required to interact with the Facebook Graph API.

**Example `facebook-poster.properties`**:
```properties
access_token=YOUR_PAGE_ACCESS_TOKEN
page_id=YOUR_PAGE_ID
