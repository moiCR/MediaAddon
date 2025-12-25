package git.moi.media.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import lombok.Getter;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class ItemBuilder {
    private final ItemStack itemStack;
    @Getter
    private final ItemMeta itemMeta;
    private Enchantment glow;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(material, 1, 0);
    }

    public ItemBuilder(Material material, int amount) {
        this(material, amount, 0);
    }

    public ItemBuilder(Material material, int amount, int durability) {
        this.itemStack = new ItemStack(material, amount, (short)durability);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public ItemBuilder setData(int durability) {
        this.itemStack.setDurability((short)durability);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder addAmount(int amount) {
        this.itemStack.setAmount(this.itemStack.getAmount() + amount);
        return this;
    }

    public ItemBuilder setName(String name) {
        this.itemMeta.setDisplayName(ColorUtil.translate(name));
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            ((SkullMeta)this.itemMeta).setOwner(owner);
        }
        catch (ClassCastException classCastException) {

        }
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        this.itemMeta.setLore(ColorUtil.translate(lore));
        return this;
    }
    public ItemBuilder setUnbrekeable(boolean bool){
        this.itemMeta.spigot().setUnbreakable(bool);
        return this;
    }
    public ItemBuilder setArmorColor(Color color) {
        if (this.itemMeta instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) this.itemMeta).setColor(color);
        return this;
    }

    public ItemBuilder setLore(String ... lore) {
        this.itemMeta.setLore(ColorUtil.translate(Arrays.asList(lore)));
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        try {
            this.itemMeta.getLore().add(ColorUtil.translate(line));
        }
        catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        return this;
    }

    public ItemBuilder addStoredEnchantment(Enchantment enchantment, int level) {
        try {
            ((EnchantmentStorageMeta)this.itemMeta).addStoredEnchant(enchantment, level, false);
        }
        catch (ClassCastException classCastException) {
            // empty catch block
        }
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment enchantment, int level) {
        this.itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder setHeadTexture(String textureValue){
        if (!(this.itemMeta instanceof SkullMeta) || itemStack.getType() != Material.SKULL_ITEM || itemStack.getDurability() != 3) {
            return this;
        }

        SkullMeta iconMeta = (SkullMeta) itemMeta;
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

        return this;
    }

    public ItemStack build() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
