/*******************************************************************************
 * Copyright (c) 2015 Stichting Yona Foundation
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v.2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at https://mozilla.org/MPL/2.0/.
 *******************************************************************************/
package nu.yona.server

import groovy.json.*
import nu.yona.server.test.AnalysisService
import spock.lang.Shared
import spock.lang.Specification

class RelevantCategoriesTest extends Specification
{
	@Shared
	def AnalysisService analysisService = new AnalysisService()

	def 'Get relevant categories'()
	{
		given:

		when:
		def response = analysisService.getRelevantCategories()

		then:
		response.status == 200
		response.responseData.categories.size() == 4
		response.responseData.categories.contains("Gambling")
		response.responseData.categories.contains("lotto")
		response.responseData.categories.contains("news/media")
		response.responseData.categories.contains("newsgroups/forums")
	}
}
