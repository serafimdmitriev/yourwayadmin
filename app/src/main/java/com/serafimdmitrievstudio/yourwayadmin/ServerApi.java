package com.serafimdmitrievstudio.yourwayadmin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


interface ServerApi {
    String ServerString = "YourWay/YourWayServer";

    @GET(ServerString)
    Call<ServerCheckAdminIdResponse> isExist(@Query("reason") String reason,
                                         @Query("idType") String idType,
                                         @Query("id") String id);

    @FormUrlEncoded
    @POST(ServerString)
    Call<ServerSimpleResponse> requestNewAdministrator(@Field("reason") String reason,
                                                       @Field("idType") String idType,
                                                       @Field("id") String id,
                                                       @Field("email") String email,
                                                       @Field("name") String name,
                                                       @Field("contacts") String contacts,
                                                       @Field("message") String message,
                                                       @Field("firebaseToken") String firebaseToken);

    @FormUrlEncoded
    @POST(ServerString)
    Call<ServerSimpleResponse> acceptNewAdministrator(@Field("reason") String reason,
                                                      @Field("requestId") int requestId,
                                                      @Field("accept") boolean accept,
                                                      @Field("accessLevel") short accessLevel);

    @FormUrlEncoded
    @POST(ServerString)
    Call<ServerSimpleResponse> acceptAction(@Field("reason") String reason,
                                            @Field("actionId") long actionId,
                                            @Field("segmentToAddStreetName") String segmentToAddStreetName,
                                            @Field("accept") boolean accept);

    @FormUrlEncoded
    @POST(ServerString)
    Call<ServerSimpleResponse> changeFirebaseToken(@Field("reason") String reason,
                                                                      @Field("oldToken") String oldToken,
                                                                      @Field("newToken") String newToken);

    @GET(ServerString)
    Call<List<ServerGetMapResponse>> getMap(@Query("reason") String reason);
}
