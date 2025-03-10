package org.contoso.whatsapp.data.repository.users

import org.contoso.whatsapp.data.models.ChatUser
import org.contoso.whatsapp.data.models.ChatUserCreationRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("users/search")
    suspend fun getUsersByName(@Query("name") name: String): List<ChatUser>
    
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): ChatUser

    @GET("users")
    suspend fun getAllUsers(): List<ChatUser>

    @POST("users")
    suspend fun createUser(@Body chatUserCreationRequest: ChatUserCreationRequest): ChatUser
}