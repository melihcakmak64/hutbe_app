package com.example.hutbe.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hutbe.model.Hutbe
import com.example.hutbe.services.HutbeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HutbeViewModel : ViewModel() {

    // --- State holders ---
    private val _allHutbeler = MutableStateFlow<List<Hutbe>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _loading = MutableStateFlow(true)
    private val _error = MutableStateFlow<String?>(null)

    // --- State observers ---
    val searchQuery: StateFlow<String> = _searchQuery
    val loading: StateFlow<Boolean> = _loading
    val error: StateFlow<String?> = _error

    val filteredHutbeler: StateFlow<List<Hutbe>> = _searchQuery
        .combine(_allHutbeler) { query, list ->
            if (query.isBlank()) list
            else list.filter { it.Title.contains(query, ignoreCase = true) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        fetchHutbeler()
    }

    private fun fetchHutbeler() = viewModelScope.launch {
        _loading.value = true
        _error.value = null
        runCatching {
            HutbeRepository.fetchHutbeler()
        }.onSuccess {
            _allHutbeler.value = it
        }.onFailure {
            _error.value = "Veriler alınamadı: ${it.message}"
        }.also {
            _loading.value = false
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

}
