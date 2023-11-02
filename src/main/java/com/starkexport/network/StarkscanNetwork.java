package com.starkexport.network;

import com.starkexport.model.pojo.Transaction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface StarkscanNetwork {
    @GET("/api/v0/transactions")
    Call<Transaction> retrieveTransaction (@Header("x-api-key") String apikey, @Query("from_block") int fromBlock, @Query("to_block") int toBlock, @Query("contract_address") String contractAddress, @Query("order_by") String orderBy, @Query("limit") int limit);

}
