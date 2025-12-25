package git.moi.media.listener;

import git.moi.media.MediaPlugin;
import git.moi.media.utils.ColorUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.regex.Pattern;

@Getter
public class ChatListener implements Listener {

    private final MediaPlugin plugin;
    private final Pattern pattern;

    public ChatListener(MediaPlugin plugin){
        this.plugin = plugin;
        this.pattern = Pattern.compile(
                "^(https?://)?(www\\\\.)?(youtube\\\\.com|twitch\\\\.tv|tiktok\\\\.com).*$",
                Pattern.CASE_INSENSITIVE);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        String message = e.getMessage();

        if(!player.hasMetadata("newURL")){
            return;
        }

        e.setCancelled(true);
        if (message.equals("CANCEL")){
            player.removeMetadata("newURL", plugin);
            getPlugin().getMediaMenus().selectionMenu(player);
            getPlugin().getMediaHandler().getVariant().remove(player.getUniqueId());
            return;
        }

        if (!getPattern().matcher(message).find()){
            player.sendMessage(ColorUtil.translate(plugin.getConfig().getString("URL_CHECKER.ERROR")));
            return;
        }

        player.removeMetadata("newURL", plugin);
        getPlugin().getMediaMenus().confirmMenu(player);
        getPlugin().getMediaHandler().getUrls().put(player.getUniqueId(), message);
    }
}
