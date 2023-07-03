package com.example.photogallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryViewModel"

class PhotoGalleryViewModel : ViewModel() {
    private val photoRepository = PhotoRepository()
    private val preferencesRepository = PreferencesRepository.get()

    private val _galleryItems: MutableStateFlow<List<GalleryItem>> = MutableStateFlow(emptyList())
    val galleryItem: StateFlow<List<GalleryItem>>
        get() = _galleryItems.asStateFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.storedQuery.collectLatest { storedQuery ->
                try {
                    val items = photoRepository.searchPhotos(storedQuery)
                    _galleryItems.value = items
                } catch (ex: Exception) {
                    Log.d(TAG, "Failed to fetch gallery items", ex)
                }
            }
        }
    }

    fun setQuery(query: String) {
        viewModelScope.launch {preferencesRepository.setStoredQuery(query)}
    }

    private suspend fun fetchGalleryItems(query: String): List<GalleryItem> {
        return if (query.isNotEmpty()) {
            photoRepository.searchPhotos(query)
        } else {
            photoRepository.fetchPhotos()
        }
    }
}