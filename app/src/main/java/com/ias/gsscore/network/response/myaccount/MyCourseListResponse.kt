package com.ias.gsscore.network.response.myaccount

import com.ias.gsscore.network.response.OodlesApiResponse
import com.ias.gsscore.network.response.home.LatestCourses


data class MyCourseListResponse (
    var latest_courses: ArrayList<LatestCourses>?,
    var package_list: List<PackageList>?,
    var package_logo:String="",
): OodlesApiResponse()