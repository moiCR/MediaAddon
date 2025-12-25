package git.moi.media.menu;

import git.moi.media.MediaPlugin;
import git.moi.media.utils.ColorUtil;
import git.moi.media.utils.Cooldown;
import git.moi.media.utils.ItemBuilder;
import git.moi.media.utils.spigui.buttons.SGButton;
import git.moi.media.utils.spigui.menu.SGMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.concurrent.TimeUnit;

public class MediaMenus {

    private final MediaPlugin plugin;

    public MediaMenus(MediaPlugin plugin){
        this.plugin = plugin;
    }

    public void selectionMenu(Player player){
        SGMenu menu =  plugin.getSpiGUI().create(ColorUtil.translate(plugin.getConfig().getString("MENU.SELECTION_MENU.TITLE")), plugin.getConfig().getInt("MENU.SELECTION_MENU.ROW_SIZE"));
        SGButton video = new SGButton(
                new ItemBuilder(Material.SKULL_ITEM, 1, 3)
                        .setName(plugin.getConfig().getString("MENU.SELECTION_MENU.HEADS.VIDEO.DISPLAYNAME"))
                        .setHeadTexture(plugin.getConfig().getString("MENU.SELECTION_MENU.HEADS.VIDEO.TEXTURE"))
                        .build()
        ).withListener((InventoryClickEvent e)->{
            Player clicker = (Player) e.getWhoClicked();
            clicker.closeInventory();
            clicker.setMetadata("newURL", new FixedMetadataValue(plugin, null));

            for (String typeMessage : plugin.getConfig().getStringList("TYPE_MESSAGE.VIDEO")){
                clicker.sendMessage(ColorUtil.translate(typeMessage));
            }

            clicker.playSound(clicker.getLocation(), Sound.NOTE_PLING, 1f, 1f);
            plugin.getMediaHandler().getVariant().put(e.getWhoClicked().getUniqueId(), "Video");
        });

        SGButton stream = new SGButton(
                new ItemBuilder(Material.SKULL_ITEM, 1, 3)
                        .setName(plugin.getConfig().getString("MENU.SELECTION_MENU.HEADS.STREAM.DISPLAYNAME"))
                        .setHeadTexture(plugin.getConfig().getString("MENU.SELECTION_MENU.HEADS.STREAM.TEXTURE"))
                        .build()
        ).withListener((InventoryClickEvent e)->{
            Player clicker = (Player) e.getWhoClicked();
            clicker.closeInventory();
            clicker.setMetadata("newURL", new FixedMetadataValue(plugin, null));

            for (String typeMessage : plugin.getConfig().getStringList("TYPE_MESSAGE.STREAM")){
                clicker.sendMessage(ColorUtil.translate(typeMessage));
            }

            clicker.playSound(clicker.getLocation(), Sound.NOTE_PLING, 1f, 1f);
            plugin.getMediaHandler().getVariant().put(e.getWhoClicked().getUniqueId(), "Stream");
        });

        menu.setButton(12, video);
        menu.setButton(14, stream);
        player.openInventory(menu.getInventory());
    }

    public void confirmMenu(Player player){
        SGMenu menu = plugin.getSpiGUI().create(ColorUtil.translate(plugin.getConfig().getString("MENU.CONFIRM_MENU.TITLE").replace("{variant}", plugin.getMediaHandler().getVariant().get(player.getUniqueId()))), plugin.getConfig().getInt("MENU.CONFIRM_MENU.ROW_SIZE"));
        SGButton confirm = new SGButton(
            new ItemBuilder(Material.SKULL_ITEM, 1, 3)
                    .setName(ColorUtil.translate(plugin.getConfig().getString("MENU.CONFIRM_MENU.HEADS.CONFIRM.DISPLAYNAME")))
                    .setHeadTexture(plugin.getConfig().getString("MENU.CONFIRM_MENU.HEADS.CONFIRM.TEXTURE"))
                    .build()
        ).withListener((InventoryClickEvent e)->{
            Player clicker = (Player) e.getWhoClicked();
            String url = plugin.getMediaHandler().getUrls().remove(clicker.getUniqueId());
            String name = clicker.getName();
            String variant = plugin.getMediaHandler().getVariant().remove(clicker.getUniqueId());
            clicker.closeInventory();

            for (String broadcastMessage : plugin.getConfig().getStringList("BROADCAST.MESSAGE")){
                Bukkit.broadcastMessage(ColorUtil.translate(broadcastMessage.replace("{player}", name).replace("{url}", url).replace("{variant}", variant)));
            }

            clicker.playSound(clicker.getLocation(), Sound.VILLAGER_YES, 1f, 1f);
            Cooldown.setCooldown("command_cooldown", clicker, TimeUnit.SECONDS.toMillis(plugin.getConfig().getInt("COMMAND.COOLDOWN_TIME")));
        });

        SGButton cancel = new SGButton(
                new ItemBuilder(Material.SKULL_ITEM, 1, 3)
                        .setName(ColorUtil.translate(plugin.getConfig().getString("MENU.CONFIRM_MENU.HEADS.CANCEL.DISPLAYNAME")))
                        .setHeadTexture(plugin.getConfig().getString("MENU.CONFIRM_MENU.HEADS.CANCEL.TEXTURE"))
                        .build()
        ).withListener((InventoryClickEvent e)-> {
            Player clicker = (Player) e.getWhoClicked();
            plugin.getMediaHandler().getUrls().remove(clicker.getUniqueId());
            plugin.getMediaHandler().getVariant().remove(clicker.getUniqueId());

            clicker.closeInventory();
            clicker.sendMessage(ColorUtil.translate(plugin.getConfig().getString("MENU.CONFIRM_MENU.PROCESS_CANCELLED")));
            clicker.playSound(clicker.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
        });

        menu.setButton(12, confirm);
        menu.setButton(14, cancel);
        player.openInventory(menu.getInventory());
    }



}
