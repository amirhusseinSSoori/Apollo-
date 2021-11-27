package com.example.myapplication.ui.repositories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.myapplication.domain.model.NodeModel
import com.example.myapplication.domain.exception.ApolloResult

import com.example.myapplication.domain.useCase.allRepository.ShowAllRepositoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoryViewModel @Inject constructor(private val showAllReposirtoryUseCase: ShowAllRepositoryUseCase) : ViewModel() {
    val state = MutableStateFlow<ApolloResult<List<NodeModel>>>(ApolloResult.Empty())
    val _state = state.asStateFlow()
    fun showRepositoryList() {
        viewModelScope.launch {
            showAllReposirtoryUseCase.execute().collect {
                state.value = it
            }
        }
    }
}