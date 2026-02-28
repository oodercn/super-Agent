package net.ooder.nexus.dto.personal;

import net.ooder.nexus.domain.personal.model.MediaPlatform;

import java.io.Serializable;
import java.util.List;

public class PlatformListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<MediaPlatform> platforms;

    public List<MediaPlatform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<MediaPlatform> platforms) {
        this.platforms = platforms;
    }
}
