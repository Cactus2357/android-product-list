package com.example.product.webservice;

import com.example.product.entity.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IPostAPI {
    @GET("/posts")
    Call<List<Post>> getPosts();
    @GET("/posts")
    Call<List<Post>> getPosts(@Query("userId") int userId);
    @GET("/posts/{id}")
    Call<Post> getPostsById(@Path("id") int id);
    @POST("/posts")
    Call<Post> createPost(@Body Post post);
}
