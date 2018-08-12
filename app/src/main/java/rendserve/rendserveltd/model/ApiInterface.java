package rendserve.rendserveltd.model;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rendserve.rendserveltd.Pojo.BasePojo;
import rendserve.rendserveltd.Pojo.DailyReportPojo;
import rendserve.rendserveltd.Pojo.LoginPojo;
import rendserve.rendserveltd.Pojo.ProjectPojo;
import rendserve.rendserveltd.Pojo.UploadDocPojo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/api/mobile/Login")
    Call<LoginPojo> login(@Field("email") String email, @Field("password") String password, @Field("device_token") String device_token);

    @FormUrlEncoded
    @POST("/api/mobile/Signup")
    Call<LoginPojo> signup(@Field("full_name") String full_name, @Field("email") String email, @Field("password") String password, @Field("company") String company, @Field("company_size") String company_size, @Field("phone_number") String phone_number);

    @FormUrlEncoded
    @POST("/api/mobile/ForgotPassword")
    Call<LoginPojo> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("/api/mobile/GetProject")
    Call<ProjectPojo> getProject(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("/api/mobile/DeleteProject")
    Call<BasePojo> deleteProject(@Field("user_id") String user_id, @Field("project_id") String project_id);

    @Multipart
    @POST("/api/mobile/addProject")
    Call<LoginPojo> addProject(@Part("user_id") RequestBody user_id, @Part("project_name") RequestBody project_name,
                               @Part("start_date") RequestBody start_date, @Part("end_date") RequestBody end_date,
                               @Part("lats") RequestBody lats, @Part("longs") RequestBody longs,
                               @Part("address") RequestBody address, @Part("state") RequestBody state,
                               @Part("zipcode") RequestBody zipcode, @Part("emails") RequestBody emails,
                               @Part("constructor_name") RequestBody constructor_name,@Part("manager_name") RequestBody manager_name,
                               @Part MultipartBody.Part logo);

    @Multipart
    @POST("/api/mobile/editProject")
    Call<LoginPojo> editProject(@Part("user_id") RequestBody user_id,@Part("id") RequestBody id, @Part("project_name") RequestBody project_name,
                               @Part("start_date") RequestBody start_date, @Part("end_date") RequestBody end_date,
                               @Part("lats") RequestBody lats, @Part("longs") RequestBody longs,
                               @Part("address") RequestBody address, @Part("state") RequestBody state,
                               @Part("zipcode") RequestBody zipcode, @Part("emails") RequestBody emails,
                               @Part("constructor_name") RequestBody constructor_name,@Part("manager_name") RequestBody manager_name,
                               @Part MultipartBody.Part logo);

    @Multipart
    @POST("/api/mobile/editProject")
    Call<LoginPojo> editProject(@Part("user_id") RequestBody user_id,@Part("id") RequestBody id, @Part("project_name") RequestBody project_name,
                                @Part("start_date") RequestBody start_date, @Part("end_date") RequestBody end_date,
                                @Part("lats") RequestBody lats, @Part("longs") RequestBody longs,
                                @Part("address") RequestBody address, @Part("state") RequestBody state,
                                @Part("zipcode") RequestBody zipcode, @Part("emails") RequestBody emails,
                                @Part("constructor_name") RequestBody constructor_name,@Part("manager_name") RequestBody manager_name );
    /*@Multipart
    @POST("/api/mobile/addProject")
    Call<LoginPojo> addProjectWithotLogo(@Part("user_id") RequestBody user_id, @Part("project_name") RequestBody project_name,
                               @Part("start_date") RequestBody start_date, @Part("end_date") RequestBody end_date,
                               @Part("lats") RequestBody lats, @Part("longs") RequestBody longs,
                               @Part("address") RequestBody address, @Part("state") RequestBody state,
                               @Part("zipcode") RequestBody zipcode, @Part("emails") RequestBody emails);*/
    @FormUrlEncoded
    @POST("/api/mobile/generatePdf")
    Call<BasePojo> pdfProject(@Field("user_id") String user_id, @Field("project_id") String project_id
            , @Field("enddate") String enddate, @Field("state") String state
            , @Field("startdate") String startdate, @Field("address") String address
            , @Field("temperature") String temperature, @Field("weather") String weather, @Field("time_of_visit") String time_of_visit
            , @Field("humidity") String humidity, @Field("constructor_name") String constructor_name
            , @Field("manager_name") String manager_name, @Field("email") String email, @Field("data") String data);

    @Multipart
    @POST("/api/mobile/addDocument")
    Call<UploadDocPojo> addDocument(@Part MultipartBody.Part logo);

    @FormUrlEncoded
    @POST("/api/mobile/dailyReport")
    Call<DailyReportPojo> dailyReport(@Field("user_id") String user_id, @Field("project_id") String project_id);

    @FormUrlEncoded
    @POST("/api/mobile/DeleteReport")
    Call<BasePojo> deleteReport(@Field("user_id") String user_id, @Field("id") String id);

}