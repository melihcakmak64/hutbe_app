package com.example.hutbe.controller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hutbe.model.Hutbe
import com.example.hutbe.services.HutbeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HutbeViewModel : ViewModel() {
    private val _allHutbeler = MutableStateFlow<List<Hutbe>>(emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _selectedHutbe = MutableStateFlow<Hutbe?>(null)
    val selectedHutbe: StateFlow<Hutbe?> = _selectedHutbe

    // Arama sorgusuna göre filtrelenmiş hutbeler
    val filteredHutbeler: StateFlow<List<Hutbe>> = combine(
        _allHutbeler,
        _searchQuery
    ) { hutbeler, query ->
        if (query.isBlank()) {
            hutbeler
        } else {
            hutbeler.filter { hutbe ->
                hutbe.Title.lowercase().contains(query.lowercase())
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        fetchHutbeler()
    }

    private fun fetchHutbeler() {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                _allHutbeler.value = HutbeRepository.fetchHutbeler()
            } catch (e: Exception) {
                _error.value = "Veriler alınamadı: ${e.message}"
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    fun selectHutbe(hutbe: Hutbe?) {
        _selectedHutbe.value = hutbe
    }

    fun refreshHutbeler() {
        fetchHutbeler()
    }
}