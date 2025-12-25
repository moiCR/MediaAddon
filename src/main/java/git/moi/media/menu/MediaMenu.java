package git.moi.media.menu;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MediaMenu {

    private final MediaPlugin plugin;

    public MediaMenu(MediaPlugin plugin){
        this.plugin = plugin;
    }

    public void selectionMenu(Player player){
        SGMenu menu =  plugin.getSpiGUI().create(ColorUtil.translate(plugin.getConfig().getString("MENU.SELECTION_MENU.TITLE")), plugin.getConfig().getInt("MENU.SELECTION_MENU.ROW_SIZE"));
        SGButton video = new SGButton(
                new ItemBuilder(Material.SKULL_ITEM, 1, 3).setName(plugin.getConfig().getString("MENU.SELECTION_MENU.HEADS.VIDEO.DISPLAYNAME")).build()
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
                new ItemBuilder(Material.SKULL_ITEM, 1, 3).setName(plugin.getConfig().getString("MENU.SELECTION_MENU.HEADS.STREAM.DISPLAYNAME")).build()
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

        setTexture(video.getIcon(),  plugin.getConfig().getString("MENU.SELECTION_MENU.HEADS.VIDEO.TEXTURE"));
        setTexture(stream.getIcon(), plugin.getConfig().getString("MENU.SELECTION_MENU.HEADS.STREAM.TEXTURE"));
        menu.setButton(12, video);
        menu.setButton(14, stream);

        player.openInventory(menu.getInventory());
    }

    public void confirmMenu(Player player){
        SGMenu menu = plugin.getSpiGUI().create(ColorUtil.translate(plugin.getConfig().getString("MENU.CONFIRM_MENU.TITLE").replace("{variant}", plugin.getMediaHandler().getVariant().get(player.getUniqueId()))), plugin.getConfig().getInt("MENU.CONFIRM_MENU.ROW_SIZE"));
        SGButton confirm = new SGButton(
            new ItemBuilder(Material.SKULL_ITEM, 1, 3).setName(ColorUtil.translate(plugin.getConfig().getString("MENU.CONFIRM_MENU.HEADS.CONFIRM.DISPLAYNAME"))).build()
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
                new ItemBuilder(Material.SKULL_ITEM, 1, 3).setName(ColorUtil.translate(plugin.getConfig().getString("MENU.CONFIRM_MENU.HEADS.CANCEL.DISPLAYNAME"))).build()
        ).withListener((InventoryClickEvent e)-> {
            Player clicker = (Player) e.getWhoClicked();
            plugin.getMediaHandler().getUrls().remove(clicker.getUniqueId());
            plugin.getMediaHandler().getVariant().remove(clicker.getUniqueId());

            clicker.closeInventory();
            clicker.sendMessage(ColorUtil.translate(plugin.getConfig().getString("MENU.CONFIRM_MENU.PROCESS_CANCELLED")));
            clicker.playSound(clicker.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
        });
        setTexture(confirm.getIcon(), plugin.getConfig().getString("MENU.CONFIRM_MENU.HEADS.CONFIRM.TEXTURE"));
        setTexture(cancel.getIcon(), plugin.getConfig().getString("MENU.CONFIRM_MENU.HEADS.CANCEL.TEXTURE"));

        menu.setButton(12, confirm);
        menu.setButton(14, cancel);
        player.openInventory(menu.getInventory());
    }




    public void setTexture(ItemStack head, String textureValue){
        SkullMeta iconMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        propertyMap.put("textures", new Property("texture", textureValue));

        try {
            Field field = iconMeta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(iconMeta, profile);
        }catch (Exception e){
            e.printStackTrace();
        }

        head.setItemMeta(iconMeta);
    }
}
