package git.moi.media.listener;

import git.moi.media.MediaPlugin;
import git.moi.media.utils.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class ChatListener implements Listener {

    private final MediaPlugin plugin;

    public ChatListener(MediaPlugin plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        String message = e.getMessage();

        boolean url_checker = false;
        if(!player.hasMetadata("newURL")){
            return;
        }

        e.setCancelled(true);
        if (message.equals("CANCEL")){
            player.removeMetadata("newURL", plugin);
            plugin.getMediaMenu().selectionMenu(player);
            plugin.getMediaHandler().getVariant().remove(player.getUniqueId());
            return;
        }

        for (String startWith : plugin.getConfig().getStringList("URL_CHECKER.URLS")){
            if (message.startsWith(startWith)){
                url_checker = true;
                break;
            }
        }

        if (!url_checker){
            player.sendMessage(ColorUtil.translate(plugin.getConfig().getString("URL_CHECKER.ERROR")));
            return;
        }

        player.removeMetadata("newURL", plugin);
        plugin.getMediaMenu().confirmMenu(player);
        plugin.getMediaHandler().getUrls().put(player.getUniqueId(), message);
    }
}
