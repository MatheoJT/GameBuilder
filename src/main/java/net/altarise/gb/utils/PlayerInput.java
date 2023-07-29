package net.altarise.gb.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class PlayerInput {
    public static final HashMap<Player, PlayerInput> playerInputs = new HashMap<>();

    public Consumer<SignHandler> signConsumer;
    public Consumer<ChatHandler> chatConsumer;
    public String defaultMessage;
    private final Player player;

    private static Field channelField;

    static {
        Arrays.stream(NetworkManager.class.getDeclaredFields()).filter(field -> field.getType().isAssignableFrom(Channel.class)).findFirst().ifPresent(field -> channelField = field);
    }

    public static void register(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new ChatHandler(), plugin);
    }




    /**
     * @param player target
     * @param defaultMessage default message in menu (use \n for new line), for Anvil, it will change the left item name (input line)
     */

    public PlayerInput(Player player, String defaultMessage) {
        this.defaultMessage = defaultMessage;
        this.player = player;
        playerInputs.put(player, this);
    }


    public void sign(Consumer<SignHandler> consumer) {
        this.signConsumer = consumer;
        List<String> lines = Arrays.asList(defaultMessage.split("\n"));
        Location signLocation = new Location(player.getWorld(), 0, 0, 0);
        org.bukkit.Material blockType = signLocation.getBlock().getType();
        BlockPosition blockPosition = new BlockPosition(signLocation.getX(), signLocation.getY(), signLocation.getZ());
        PacketPlayOutBlockChange blockChange = new PacketPlayOutBlockChange(((CraftPlayer) player).getHandle().getWorld(), blockPosition);
        blockChange.block = CraftMagicNumbers.getBlock(org.bukkit.Material.WALL_SIGN).fromLegacyData(0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(blockChange);
        PacketPlayOutUpdateSign updateSign = new PacketPlayOutUpdateSign(((CraftPlayer) player).getHandle().getWorld(), blockPosition, new IChatBaseComponent[]{ IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + lines.get(0) + "\"}"), IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + lines.get(1) + "\"}"), IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + lines.get(2) + "\"}"), IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + lines.get(3) + "\"}") });
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(updateSign);
        PacketPlayOutOpenSignEditor packet2 = new PacketPlayOutOpenSignEditor(blockPosition);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet2);
        Bukkit.getWorld(player.getWorld().getName()).getBlockAt(signLocation).setType(blockType);
        injectNetty(player);
    }


    public void chat(Consumer<ChatHandler> consumer) {
        this.chatConsumer = consumer;
        List<String> lines = Arrays.asList(defaultMessage.split("\n"));
        lines.forEach(player::sendMessage);
    }






    @SuppressWarnings("rawtypes")
    public void injectNetty(final Player player) {
        try {
            Channel channel = (Channel) channelField.get(((CraftPlayer) player).getHandle().playerConnection.networkManager);
            if (channel != null) {
                channel.pipeline().addAfter("decoder", "player_input", new MessageToMessageDecoder<Packet>() {
                    @Override
                    protected void decode(ChannelHandlerContext chc, Packet packet, List<Object> out) {
                        if (packet instanceof PacketPlayInUpdateSign) {
                            PacketPlayInUpdateSign usePacket = (PacketPlayInUpdateSign) packet;
                            Bukkit.getPluginManager().callEvent(new SignHandler(player, usePacket.b()));
                        }


                        out.add(packet);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void ejectNetty(Player player) {
        try {
            Channel channel = (Channel) channelField.get(((CraftPlayer) player).getHandle().playerConnection.networkManager);
            if (channel != null && channel.pipeline().get("player_input") != null) channel.pipeline().remove("player_input");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class SignHandler extends PlayerEvent  {
        public final HandlerList handlers = new HandlerList();
        public final IChatBaseComponent[] lines;
        public SignHandler(Player player, IChatBaseComponent[] lines) {
            super(player);
            this.lines = lines;
            playerInputs.get(player).signConsumer.accept(this);
            ejectNetty(player);
            playerInputs.remove(player);
        }


        @Override
        public HandlerList getHandlers() {
            return handlers;
        }
    }


    public static class ChatHandler implements Listener {

        public String message;

        @EventHandler
        public void onChat(AsyncPlayerChatEvent event) {
            if(playerInputs.containsKey(event.getPlayer())) {
                event.setCancelled(true);
                message = event.getMessage();
                playerInputs.get(event.getPlayer()).chatConsumer.accept(this);
                playerInputs.remove(event.getPlayer());
            }
        }

    }







}


















