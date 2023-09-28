package com.ias.gsscore.network.response.home

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse


data class HomeResponse (
  @SerializedName("slider_images"              ) var sliderImages             : ArrayList<SliderImages>             = arrayListOf(),
  @SerializedName("latest_courses"             ) var latestCourses            : ArrayList<LatestCourses>            = arrayListOf(),
  @SerializedName("free_resources_categories"  ) var freeResourcesCategories  : ArrayList<FreeResourcesCategories>  = arrayListOf(),
  @SerializedName("current_affairs_categories" ) var currentAffairsCategories : ArrayList<FreeResourcesCategories> = arrayListOf(),
  @SerializedName("course_count"               ) var courseCount              : CourseCount?                        = CourseCount(),
  @SerializedName("study_material_list"        ) var studyMaterialList        : ArrayList<StudyMaterialList>        = arrayListOf()

):OodlesApiResponse()