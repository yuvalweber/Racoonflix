package com.example.netflix.viewmodels;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MoviePlayerViewModel extends ViewModel {
    private final MutableLiveData<String> videoUrl = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentPosition = new MutableLiveData<>(0);

    public LiveData<String> getVideoUrl() {
        return videoUrl;
    }

    public LiveData<Integer> getCurrentPosition() {
        return currentPosition;
    }

    public void savePosition(int position) {
        currentPosition.setValue(position);
    }

    public void fetchVideoUrl(String url) {
        // Assume repository fetches video URL
        videoUrl.setValue(url);
    }
}


