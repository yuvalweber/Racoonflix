package com.example.netflix.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MoviePlayerViewModel extends ViewModel {
	// This class is a ViewModel for the MoviePlayerActivity. It is responsible for fetching the video URL and saving the current position of the video.
    private final MutableLiveData<String> videoUrl = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPosition = new MutableLiveData<>(0);

    public LiveData<String> getVideoUrl() {
        return videoUrl;
    }

	// This method returns the current position of the video.
    public LiveData<Integer> getCurrentPosition() {
        return currentPosition;
    }

	// This method saves the current position of the video.
    public void savePosition(int position) {
        currentPosition.setValue(position);
    }

    public void fetchVideoUrl(String url) {
        // Assume repository fetches video URL
        videoUrl.setValue(url);
    }
}


