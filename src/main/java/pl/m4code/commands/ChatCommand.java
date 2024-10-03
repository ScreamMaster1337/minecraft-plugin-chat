package pl.m4code.commands;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.utils.TextUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatCommand extends CommandAPI implements Listener {
    private static final String CHAT_PERMISSION = "m4code.chat";
    private static final String RELOAD_PERMISSION = "m4code.reload";
    private static final String ALLOW_PERMISSION = "m4code.allow";
    private static final String VIP_PERMISSION = "m4code.vip";
    private boolean chatEnabled = true;
    private boolean vipOnly = false;
    private int chatCooldown = 0;
    private final Map<UUID, Long> lastMessageTime = new HashMap<>();

    public ChatCommand() {
        super(
                "chat",
                "",
                "",
                "/chat <reload|on|off|cooldown|vip|clear>",
                List.of("")
        );
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        loadChatState();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(CHAT_PERMISSION)) {
            TextUtil.sendMessage(sender, getConfigMessage("messages.no_permission"));
            return;
        }

        if (args.length == 0) {
            TextUtil.sendMessage(sender, getConfigMessage("messages.usage"));
            return;
        }

        String adminName = sender instanceof Player ? ((Player) sender).getName() : "Console";
        FileConfiguration config = Main.getInstance().getConfig();

        switch (args[0].toLowerCase()) {
            case "reload":
                if (sender.hasPermission(RELOAD_PERMISSION)) {
                    Main.getInstance().reloadConfig();
                    TextUtil.sendMessage(sender, getConfigMessage("messages.reload"));
                } else {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.no_permission"));
                }
                break;
            case "on":
                chatEnabled = true;
                vipOnly = false;
                saveChatState();
                Bukkit.broadcastMessage(TextUtil.fixColor(getConfigMessage("messages.chat_enabled").replace("%admin%", adminName)));
                break;
            case "off":
                chatEnabled = false;
                saveChatState();
                Bukkit.broadcastMessage(TextUtil.fixColor(getConfigMessage("messages.chat_disabled_by_admin").replace("%admin%", adminName)));
                break;
            case "cooldown":
                if (args.length < 2) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.usage_cooldown"));
                    return;
                }
                try {
                    chatCooldown = parseTime(args[1]);
                    saveChatState();
                    TextUtil.sendMessage(sender, getConfigMessage("messages.cooldown_set").replace("%time%", String.valueOf(chatCooldown)));
                } catch (IllegalArgumentException e) {
                    TextUtil.sendMessage(sender, getConfigMessage("messages.invalid_time_format"));
                }
                break;
            case "vip":
                chatEnabled = true;
                vipOnly = true;
                saveChatState();
                Bukkit.broadcastMessage(TextUtil.fixColor(getConfigMessage("messages.chat_vip").replace("%admin%", adminName)));
                break;
            case "clear":
                for (int i = 0; i < 400; i++) {
                    Bukkit.broadcastMessage("");
                }
                Bukkit.broadcastMessage(TextUtil.fixColor(getConfigMessage("messages.chat_cleared").replace("%admin%", adminName)));
                break;
            default:
                TextUtil.sendMessage(sender, getConfigMessage("messages.unknown_subcommand"));
                break;
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (!chatEnabled && !player.hasPermission(ALLOW_PERMISSION)) {
            TextUtil.sendMessage(player, getConfigMessage("messages.chat_disabled"));
            event.setCancelled(true);
            return;
        }

        if (vipOnly && !player.hasPermission(ALLOW_PERMISSION) && !player.hasPermission(VIP_PERMISSION)) {
            TextUtil.sendMessage(player, getConfigMessage("messages.chat_vip_only"));
            event.setCancelled(true);
            return;
        }

        if (chatCooldown > 0 && !player.hasPermission(ALLOW_PERMISSION)) {
            long currentTime = System.currentTimeMillis();
            long lastTime = lastMessageTime.getOrDefault(playerId, 0L);
            if (currentTime - lastTime < chatCooldown * 1000) {
                int remainingTime = (int) ((chatCooldown * 1000 - (currentTime - lastTime)) / 1000);
                TextUtil.sendMessage(player, getConfigMessage("messages.cooldown_wait").replace("%time%", String.valueOf(remainingTime)));
                event.setCancelled(true);
                return;
            }
            lastMessageTime.put(playerId, currentTime);
        }
    }

    private void saveChatState() {
        FileConfiguration config = Main.getInstance().getConfig();
        config.set("chat.enabled", chatEnabled);
        config.set("chat.vipOnly", vipOnly);
        config.set("chat.cooldown", chatCooldown);
        Main.getInstance().saveConfig();
    }

    private void loadChatState() {
        FileConfiguration config = Main.getInstance().getConfig();
        chatEnabled = config.getBoolean("chat.enabled", true);
        vipOnly = config.getBoolean("chat.vipOnly", false);
        chatCooldown = config.getInt("chat.cooldown", 5); // Default cooldown time
    }

    private int parseTime(String time) {
        Pattern pattern = Pattern.compile("^(\\d+)([smhd])$");
        Matcher matcher = pattern.matcher(time);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Nieprawidłowy format czasu");
        }

        int value = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);

        switch (unit) {
            case "s":
                return value;
            case "m":
                return value * 60;
            case "h":
                return value * 3600;
            case "d":
                return value * 86400;
            default:
                throw new IllegalArgumentException("Nieprawidłowa jednostka czasu");
        }
    }

    private String getConfigMessage(String path) {
        FileConfiguration config = Main.getInstance().getConfig();
        String message = config.getString(path);
        return message != null ? message : "Missing message for " + path;
    }

    @Override
    public List<String> tab(@NonNull Player player, @NotNull @NonNull String[] args) {
        return null;
    }
}