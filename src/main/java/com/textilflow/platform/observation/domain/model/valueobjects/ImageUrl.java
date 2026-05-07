package com.textilflow.platform.observation.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import lombok.Getter;

@Embeddable
@Getter
public class ImageUrl {
    @Column(columnDefinition = "TEXT")
    private String url;

    public ImageUrl() {}

    public ImageUrl(String url) {
        this.url = url;
    }

    public boolean isEmpty() {
        return url == null || url.trim().isEmpty();
    }
}
