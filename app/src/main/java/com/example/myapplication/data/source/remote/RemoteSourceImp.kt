package com.example.myapplication.data.source.remote

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.example.myapplication.R
import com.example.myapplication.data.network.errorHandle.ApolloResult
import com.example.myapplication.data.network.errorHandle.DataSourceException
import com.example.myapplication.data.source.local.LocalSource
import example.myapplication.GetListQuery
import javax.inject.Inject

class RemoteSourceImp @Inject constructor(val network: ApolloClient) : RemoteSource {
    override suspend fun getListRepFromNetwork(owner: String): ApolloResult<GetListQuery.Data?>  {
        return try {
            val result = network.query(GetListQuery(owner)).await()
            if (result.hasErrors()) {
                ApolloResult.Error(DataSourceException.Server(result.errors?.first()))
            } else {
                ApolloResult.Success(result.data)
            }
        } catch (e: Exception) {
            ApolloResult.Error(DataSourceException.Unexpected(R.string.app_name))
        }
    }
}