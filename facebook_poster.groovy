@Grab(group='io.github.http-builder-ng', module='http-builder-ng-core', version='1.0.4')
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')
import groovy.json.JsonSlurper
import static groovyx.net.http.HttpBuilder.configure
import groovy.json.JsonOutput
import java.util.Properties

// Function to convert ISO 8601 date to Unix timestamp
def toUnixTimestamp(dateString) {
    Date date = Date.parse("yyyy-MM-dd'T'HH:mm:ss'Z'", dateString)
    return date.time / 1000 as int
}

// Load properties from facebook-poster.properties file
def propertiesFilePath = 'facebook-poster.properties'
def properties = new Properties()
def propertiesFile = new File(propertiesFilePath)
if (!propertiesFile.exists()) {
    println "Properties file not found: $propertiesFilePath"
    System.exit(1)
}
properties.load(new FileInputStream(propertiesFile))

def accessToken = properties.getProperty('access_token')
def pageId = properties.getProperty('page_id')

if (!accessToken || !pageId) {
    println "Access token or page ID not found in properties file."
    System.exit(1)
}

// Get the path to the JSON file
println "Enter the path to the JSON file containing the posts:"
def jsonFilePath = System.console().readLine().trim()

// Read and parse the JSON file
def jsonFile = new File(jsonFilePath)
if (!jsonFile.exists()) {
    println "File not found: $jsonFilePath"
    System.exit(1)
}
def posts = new JsonSlurper().parse(jsonFile)

// Schedule each post
posts.each { post ->
    def title = post.title
    def content = post.content
    def link = post.link
    def publishTime = toUnixTimestamp(post.date)

    // The endpoint for publishing a post
    def url = "https://graph.facebook.com/v17.0/${pageId}/feed"

    // The parameters for the post
    def params = [
        message: "${title}\n\n${content}\n${link}",
        published: false,
        scheduled_publish_time: publishTime,
        access_token: accessToken
    ]

    // Make the request to schedule the post
    def response = configure {
        request.uri = url
        request.contentType = 'application/json'
        request.body = params
    }.post()

    // Print the response
    if (response.status == 200) {
        println "Post scheduled successfully for date: ${post.date}"
        println "Response:"
        println JsonOutput.prettyPrint(JsonOutput.toJson(response))
    } else {
        println "Failed to schedule post for date: ${post.date}"
        println "Response:"
        println JsonOutput.prettyPrint(JsonOutput.toJson(response))
    }
}
