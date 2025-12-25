package git.moi.media;

import git.moi.media.commands.MediaCommand;
import git.moi.media.handler.MediaHandler;
import git.moi.media.listener.ChatListener;
import git.moi.media.menu.MediaMenus;
import git.moi.media.utils.ColorUtil;
import git.moi.media.utils.spigui.SpiGUI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


@Getter
public final class MediaPlugin extends JavaPlugin {
    public SpiGUI spiGUI;
    private MediaHandler mediaHandler;
    private MediaMenus mediaMenus;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.spiGUI = new SpiGUI(this);
        this.mediaHandler = new MediaHandler(this);
        this.getCommand("media").setExecutor(new MediaCommand(this));

        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&4========&c==================&4========"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&4&l&nMedia Addon"));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(""));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&c&l♥ &cAuthor: " + String.join(", ", getDescription().getAuthors())));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&c&l♥ &cVersion: &f"+ getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate(""));
        Bukkit.getConsoleSender().sendMessage(ColorUtil.translate("&4========&c==================&4========"));
    }

    @Override
    public void onDisable() {

    }





}
