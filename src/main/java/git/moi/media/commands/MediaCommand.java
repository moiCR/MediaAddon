package git.moi.media.commands;

import git.moi.media.MediaPlugin;
import git.moi.media.utils.ColorUtil;
import git.moi.media.utils.Cooldown;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MediaCommand implements CommandExecutor {

    private final MediaPlugin plugin;

    public MediaCommand(MediaPlugin plugin){
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(ColorUtil.translate("&cThis command is only executable in game."));
            return true;
        }

        if (!sender.hasPermission("media.use")){
            sender.sendMessage(ColorUtil.translate(plugin.getConfig().getString("COMMAND.NO_PERMISSIONS")));
            return true;
        }

        Player player = (Player) sender;
        if (Cooldown.hasCooldown("command_cooldown", player)){
            player.sendMessage(ColorUtil.translate(plugin.getConfig().getString("COMMAND.COOLDOWN").replace("{cooldown}", Cooldown.getCooldown("command_cooldown", player))));
            return true;
        }

        plugin.getMediaMenus().selectionMenu(player);

        return false;
    }
}
