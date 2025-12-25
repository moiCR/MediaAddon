package git.moi.media.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ColorUtil {
    public final String BLUE = ChatColor.BLUE.toString();
    public final String AQUA = ChatColor.AQUA.toString();
    public final String YELLOW = ChatColor.YELLOW.toString();
    public final String RED = ChatColor.RED.toString();
    public final String GRAY = ChatColor.GRAY.toString();
    public final String GOLD = ChatColor.GOLD.toString();
    public final String GREEN = ChatColor.GREEN.toString();
    public final String WHITE = ChatColor.WHITE.toString();
    public final String BLACK = ChatColor.BLACK.toString();
    public final String BOLD = ChatColor.BOLD.toString();
    public final String ITALIC = ChatColor.ITALIC.toString();
    public final String UNDER_LINE = ChatColor.UNDERLINE.toString();
    public final String STRIKE_THROUGH = ChatColor.STRIKETHROUGH.toString();
    public final String RESET = ChatColor.RESET.toString();
    public final String MAGIC = ChatColor.MAGIC.toString();
    public final String DBLUE = ChatColor.DARK_BLUE.toString();
    public final String DAQUA = ChatColor.DARK_AQUA.toString();
    public final String DGRAY = ChatColor.DARK_GRAY.toString();
    public final String DGREEN = ChatColor.DARK_GREEN.toString();
    public final String DPURPLE = ChatColor.DARK_PURPLE.toString();
    public final String DRED = ChatColor.DARK_RED.toString();
    public final String PURPLE = ChatColor.DARK_PURPLE.toString();
    public final String PINK = ChatColor.LIGHT_PURPLE.toString();
    public final String MENU_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH +
            "------------------------";
    public final String CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH +
            "------------------------------------------------";
    public final String SMALL_CHAT_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH +
            "-----------------";
    public final String SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH +
            "----------------------";
    public final String VERTICAL_BAR = StringEscapeUtils.unescapeJava("\u2503");
    public final String HEART = ChatColor.DARK_RED + StringEscapeUtils.unescapeJava("\u2764");
    public final String LEFT_ARROW = StringEscapeUtils.unescapeJava("\u00ab");
    public final String RIGHT_ARROW = StringEscapeUtils.unescapeJava("\u00bb");

    public String translate(String in) {
        return ChatColor.translateAlternateColorCodes('&', in);
    }

    public List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList();
        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return toReturn;
    }

    public List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList();
        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }
        return toReturn;
    }

    public String strip(String in) {
        return ChatColor.stripColor(in);
    }

    public List<String> strip(List<String> lines) {
        List<String> toReturn = new ArrayList();
        for (String line : lines) {
            toReturn.add(ChatColor.stripColor(line));
        }
        return toReturn;
    }

    public List<String> strip(String[] lines) {
        List<String> toReturn = new ArrayList();
        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.stripColor(line));
            }
        }
        return toReturn;
    }

    public String format(String in, Object... args) {
        return String.format(translate(in), args);
    }

    public List<String> format(List<String> lines, Object... args) {
        List<String> toReturn = new ArrayList<>();
        for (String line : lines) {
            toReturn.add(String.format(translate(line), args));
        }
        return toReturn;
    }
    public void sendMessage(Player player, String text){
        player.sendMessage(translate(text));
    }

}
