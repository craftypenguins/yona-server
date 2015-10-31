package nu.yona.server

import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseException
import spock.lang.Ignore
import spock.lang.IgnoreRest
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll
import groovy.json.*

class UserTest extends Specification {

	def baseURL = "http://localhost:8080"
	def YonaServer yonaServer = new YonaServer(baseURL)
	def timestamp = yonaServer.getTimeStamp()
	def userCreationJSON = """{
				"firstName":"John",
				"lastName":"Doe ${timestamp}",
				"nickName":"JD ${timestamp}",
				"emailAddress":"john${timestamp}@hotmail.com",
				"mobileNumber":"+${timestamp}",
				"devices":[
					"Galaxy mini"
				],
				"goals":[
					"gambling"
				]}"""
	def password = "John Doe"

	def 'Create John Doe'(){
		given:

		when:
			def response = yonaServer.addUser(userCreationJSON, password)

		then:
			response.status == 201
			testUser(response.responseData, true)

		cleanup:
			yonaServer.deleteUser(yonaServer.stripQueryString(response.responseData._links.self.href), password)
	}

	def 'Get John Doe with private data'(){
		given:
			def userURL = yonaServer.stripQueryString(yonaServer.addUser(userCreationJSON, password).responseData._links.self.href);

		when:
			def response = yonaServer.getUser(userURL, true, password)

		then:
			response.status == 200
			testUser(response.responseData, true)

		cleanup:
			yonaServer.deleteUser(userURL, password)
	}

	def 'Try to get John Doe\'s private data with a bad password'(){
		given:
			def userURL = yonaServer.stripQueryString(yonaServer.addUser(userCreationJSON, password).responseData._links.self.href);

		when:
			def response = yonaServer.getUser(userURL, true, "nonsense")

		then:
			HttpResponseException e = thrown()
			e.statusCode == 400

		cleanup:
			yonaServer.deleteUser(userURL, password)
	}

	def 'Get John Doe without private data'(){
		given:
			def userURL = yonaServer.stripQueryString(yonaServer.addUser(userCreationJSON, password).responseData._links.self.href);

		when:
			def response = yonaServer.getUser(userURL, false)

		then:
			response.status == 200
			testUser(response.responseData, false)

		cleanup:
			yonaServer.deleteUser(userURL, password)
	}

	def 'Delete John Doe'(){
		given:
			def userURL = yonaServer.stripQueryString(yonaServer.addUser(userCreationJSON, password).responseData._links.self.href);

		when:
			def response = yonaServer.deleteUser(userURL, password)

		then:
			response.status == 200
			verifyUserDoesNotExist(userURL)
	}

	void testUser(responseData, includePrivateData)
	{
		assert responseData.firstName == "John"
		assert responseData.lastName == "Doe ${timestamp}"
		assert responseData.emailAddress == "john${timestamp}@hotmail.com"
		assert responseData.mobileNumber == "+${timestamp}"
		if (includePrivateData) {
			assert responseData.nickName == "JD ${timestamp}"
			assert responseData.devices.size() == 1
			assert responseData.devices[0] == "Galaxy mini"
			assert responseData.goals.size() == 1
			assert responseData.goals[0] == "gambling"
		} else {
			assert responseData.nickName == null
			assert responseData.devices == null
			assert responseData.goals == null
		}
	}

	void verifyUserDoesNotExist(userURL)
	{
		try {
			def response = yonaServer.getUser(userURL, false)
			assert false;
		} catch (HttpResponseException e) {
			assert e.statusCode == 404
		}
	}
}