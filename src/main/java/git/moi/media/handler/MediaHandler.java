package git.moi.media.handler;

import git.moi.media.MediaPlugin;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Getter
public class MediaHandler {

    private final Map<UUID, String> urls;
    private final MediaPlugin plugin;
    private final Map<UUID, String> variant;

    public MediaHandler(MediaPlugin plugin){
        this.plugin = plugin;
        this.urls = new HashMap<>();
        this.variant = new HashMap<>();
    }

}
