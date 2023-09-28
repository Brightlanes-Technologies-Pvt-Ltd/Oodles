package com.ias.gsscore.network

import com.ias.gsscore.network.notification.NotificationResponse
import com.ias.gsscore.network.request.LoginRegisterRequest
import com.ias.gsscore.network.request.TestSubmitRequest
import com.ias.gsscore.network.response.LoginResponse
import com.ias.gsscore.network.response.OodlesApiResponse
import com.ias.gsscore.network.response.address.AddressResponse
import com.ias.gsscore.network.response.cart.CartItemListResponse
import com.ias.gsscore.network.response.cart.CartItemResponse
import com.ias.gsscore.network.response.confirmorder.CourseCheckoutResponse
import com.ias.gsscore.network.response.confirmorder.PayUResponse
import com.ias.gsscore.network.response.confirmorder.ProductCheckoutResponse
import com.ias.gsscore.network.response.confirmorder.TransactionResponse
import com.ias.gsscore.network.response.coursedetails.CourseDetailResponse
import com.ias.gsscore.network.response.courses.CourseDetailsResponse
import com.ias.gsscore.network.response.courses.CourseListResponse
import com.ias.gsscore.network.response.freeresource.CurrentAffairDetailsResponse
import com.ias.gsscore.network.response.freeresource.CurrentAffairResponse
import com.ias.gsscore.network.response.freeresource.FreeResourceDetailsResponse
import com.ias.gsscore.network.response.freeresource.FreeResourceResponse
import com.ias.gsscore.network.response.google.GoogleLoginResponse
import com.ias.gsscore.network.response.home.HomeResponse
import com.ias.gsscore.network.response.myaccount.*
import com.ias.gsscore.network.response.studynotes.ProductDetailsResponse
import com.ias.gsscore.network.response.studynotes.ProductListResponse
import com.ias.gsscore.network.response.testresult.TestResultResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {
    //Authentication APi
    @POST("/api/auth/site_login")
    suspend fun loginApi(@Body loginRequest: LoginRegisterRequest): Response<LoginResponse>

    @POST("/api/auth/sendOtp")
    suspend fun sendOtp(@Body request: HashMap<String, String>): Response<LoginResponse>


    @POST("/api/auth/verifyOtp")
    suspend fun verifyOtp(@Body request: HashMap<String, String>): Response<LoginResponse>

    @POST("/api/auth/update_user_details")
    suspend fun updateUserDetails(@Body request: HashMap<String, String>): Response<LoginResponse>

    @POST("/api/auth/update_password")
    suspend fun updateNewPassword(@Body request: HashMap<String, String>): Response<LoginResponse>

    @POST("/api/auth/google_login")
    suspend fun googleLogin(@Body request: HashMap<String, String>): Response<GoogleLoginResponse>

    // MyAccount API

    @POST("/api/myaccount/my_packages")
    suspend fun myPackagesList(@Body request: HashMap<String, String>): Response<MyCourseListResponse>

    @POST("/api/myaccount/my_pt_test_list")
    suspend fun ptTestList(@Body request: HashMap<String, String>): Response<AllTestResponse>

    @POST("/api/myaccount/my_mains_test_list")
    suspend fun mainsTestList(@Body request: HashMap<String, String>): Response<AllTestResponse>

    @POST("/api/myaccount/my_ebook_list")
    suspend fun eBookList(@Body request: HashMap<String, String>): Response<AllTestResponse>

    @POST("/api/myaccount/my_video_list")
    suspend fun videoList(@Body request: HashMap<String, String>): Response<AllTestResponse>

    @POST("/api/myaccount/video_details")
    suspend fun videoDetails(@Body request: HashMap<String, String>): Response<VideoDetailsResponse>

    @POST("/api/myaccount/get_test_instructions")
    suspend fun getTestInstructions(@Body request: HashMap<String, String>): Response<OodlesApiResponse>

    @POST("/api/myaccount/start_test")
    suspend fun startTest(@Body request: HashMap<String, String>): Response<StartTestResponse>

    @POST("/api/myaccount/submit_prelims_test")
    suspend fun submitTest(@Body request: TestSubmitRequest): Response<OodlesApiResponse>

    @POST("/api/myaccount/prelims_leaderboard")
    suspend fun prelimsLeaderboard(@Body request: HashMap<String, String>): Response<TestResultResponse>

    @POST("/api/myaccount/submit_prelims_test_user_feedback")
    suspend fun prelimsUserFeedback(@Body request: HashMap<String, String>): Response<OodlesApiResponse>

    @POST("/api/myaccount/prelims_test_report")
    suspend fun prelimsTestReport(@Body request: HashMap<String, String>): Response<TestResultResponse>

    @POST("/api/myaccount/mains_test_topper_list")
    suspend fun mainsTestTopperList(@Body request: HashMap<String, String>): Response<TestResultResponse>

    @POST("/api/myaccount/mains_test_question_list")
    suspend fun mainsTestQuestionList(@Body request: HashMap<String, String>): Response<TestResultResponse>

    @POST("/api/myaccount/mains_question_video")
    suspend fun mainsQuestionVideo(@Body request: HashMap<String, String>): Response<TestResultResponse>


    // Cart API
    @POST("/api/cart/products_cartItems_list")
    suspend fun cartItemList(@Body request: HashMap<String, String>): Response<CartItemListResponse>

    @POST("/api/cart/add_to_cart")
    suspend fun addToCart(@Body request: HashMap<String, String>): Response<CartItemResponse>

    @POST("/api/cart/wish_list_items")
    suspend fun wishlistItemList(@Body request: HashMap<String, String>): Response<CartItemListResponse>

    @POST("/api/cart/remove_cart_item")
    suspend fun removeCartItem(@Body request: HashMap<String, String>): Response<CartItemResponse>

    @FormUrlEncoded
    @POST("/api/cart/remove_from_wishlist")
    suspend fun removeWishItem(
        @Field("user_id") user_id: String,
        @Field("product_id") product_id: String,
        @Field("mode") mode: String
    ): Response<CartItemResponse>

    @POST("/api/cart/add_into_wish_list")
    suspend fun addToWishlist(@Body request: HashMap<String, String>): Response<CartItemResponse>

    // Courses
    @POST("/api/courses/course_list")
    suspend fun courseList(@Body request: HashMap<String, String>): Response<CourseListResponse>

    @POST("/api/courses/course_details")
    suspend fun courseDetails(@Body request: HashMap<String, String>): Response<CourseDetailsResponse>

    @POST("/api/auth/enquiry_form_submit")
    suspend fun enquiryNow(@Body request: HashMap<String, String>): Response<OodlesApiResponse>

    // Address API
    @POST("/api/address/address_list")
    suspend fun addressList(@Body request: HashMap<String, String>): Response<AddressResponse>

    // Course Detail API
    @POST("/api/cart/courses_cartitems_list")
    suspend fun courseDetailList(@Body request: HashMap<String, String>): Response<CourseDetailResponse>

    @POST("/api/address/delete_address")
    suspend fun deleteAddress(@Body request: HashMap<String, String>): Response<AddressResponse>

    @POST("/api/address/add_address")
    suspend fun addAddress(@Body request: HashMap<String, String>): Response<AddressResponse>

    @POST("/api/address/address_details")
    suspend fun addressDetails(@Body request: HashMap<String, String>): Response<AddressResponse>

    @POST("/api/address/update_address")
    suspend fun updateAddress(@Body request: HashMap<String, String>): Response<AddressResponse>

    // Home
    @POST("/api/home/home")
    suspend fun homeDetails(@Body request: HashMap<String, String>): Response<HomeResponse>

    // Study Material
    @POST("/api/products/product_list")
    suspend fun productList(@Body request: HashMap<String, String>): Response<ProductListResponse>

    @POST("/api/products/product_details")
    suspend fun productDetails(@Body request: HashMap<String, String>): Response<ProductDetailsResponse>

    @POST("/api/cart/product_checkout")
    suspend fun produtCheckout(@Body request: HashMap<String, String>): Response<ProductCheckoutResponse>

    @POST("/api/cart/course_checkout")
    suspend fun courseCheckout(@Body request: HashMap<String, String>): Response<CourseCheckoutResponse>

    @POST("/api/payment_response/payumoney")
    suspend fun payuresponse(@Body request: HashMap<String, String>): Response<PayUResponse>

    @POST("/api/payment_response/transaction_response")
    suspend fun transresponse(@Body request: HashMap<String, String>): Response<TransactionResponse>

    @POST("/api/freeresources/free_resources")
    suspend fun freeResource(@Body request: HashMap<String, String>): Response<FreeResourceResponse>

    @POST("/api/currentaffairs/list")
    suspend fun currentAffair(@Body request: HashMap<String, String>): Response<CurrentAffairResponse>

    @POST("/api/freeresources/free_resources_details")
    suspend fun freeResourceDetails(@Body request: HashMap<String, String>): Response<FreeResourceDetailsResponse>

    @POST("/api/currentaffairs/details")
    suspend fun currentAffairDetails(@Body request: HashMap<String, String>): Response<CurrentAffairDetailsResponse>

    @POST("/api/myaccount/user_profile")
    suspend fun userProfile(@Body request: HashMap<String, String>): Response<ProfileResponse>

    @POST("/api/myaccount/update_user_profile")
    suspend fun updateUserProfile(@Body request: HashMap<String, String>): Response<OodlesApiResponse>

    @POST("/api/address/state_list")
    suspend fun stateList(@Body request: HashMap<String, String>): Response<StateListResponse>

    @POST("/api/auth/update_user_device_id")
    suspend fun fcm(@Body request: HashMap<String, String>): Response<OodlesApiResponse>

    @POST("/api/myaccount/get_user_notifications")
    suspend fun notification(@Body request: HashMap<String, String>): Response<NotificationResponse>

    @Multipart
    @Headers("ContentType: application/pdf")
    @POST("/api/managefiles/upload_mains_test_answer_copy")
    @JvmSuppressWildcards
    fun uploadPDF(@Part file: MultipartBody.Part, @PartMap params: Map<String, RequestBody>): Call<HashMap<String, Any>>

    @POST("https://conapi.iasscore.in")
    suspend fun getJWT(@Body request: HashMap<String, Any>): HashMap<String, String>?


    /*   @FormUrlEncoded
       @POST("/user/otherProfile")
       suspend fun getOtherUserProfile(
           @Header("x-token") xToken: String,
           @Field("userId") userId: Int,
       ): Response<LoginResponse>*/

/*    @Multipart
    @POST("/media/insert/imageAndThumbnail")
    suspend fun uploadImageAndThumbnail(
        @Header("x-token") xToken: String,
        @Part image: MultipartBody.Part,
        @Part thumbnail: MultipartBody.Part
    ): Response<ImageAndThumbnailResponse>*/


}