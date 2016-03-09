package io.github.IKnowScriptFu.PackYourStuffPlugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 Created by Aemilius on 09/03/2016.
 */
public class PackingHandler implements Listener{


    Map<Player, Integer> playersPacking = new HashMap<>();
    Map<Player, Location[]> movingStructures = new HashMap<>();

    PackingConversation packingConversation;
    Plugin packPlugin;


    public PackingHandler(Plugin plugin) {
        packPlugin = plugin;
        packingConversation = new PackingConversation(packPlugin, this::beginPackingProcedure);
        packPlugin.getServer().getPluginManager().registerEvents(this, packPlugin);
    }

    @EventHandler
    public void playerPacking(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(player.getInventory().getItemInMainHand().getType().equals(Material.GOLD_PICKAXE) && event.getAction()
                .equals(Action.RIGHT_CLICK_BLOCK)){
            Integer moveState = playersPacking.get(player);
            if(moveState == 0){
                movingStructures.put(player, new Location[2]);
                playersPacking.put(player, 1);
                Block firstBlock = event.getClickedBlock();
                movingStructures.get(player)[0] = firstBlock.getLocation();
            }else if(moveState == 1){
                removePlayer(player);
                Block firstBlock = event.getClickedBlock();
                movingStructures.get(player)[1] = firstBlock.getLocation();
                Conversation conversation = packingConversation.buildConversation(player);
                conversation.begin();

            }
        }
    }

    private Void beginPackingProcedure(Player player){
        return null;
    }

    @EventHandler
    public void playerLogout(PlayerQuitEvent event){
        Player player = event.getPlayer();
        removePlayer(player);
    }

    /**
     * Removes the player from those currently packing
     * @param player
     */
    private void removePlayer(Player player){
        if(playersPacking.containsKey(player)){
            playersPacking.remove(player);
        }
    }

    /**
     * Adds the player to the map of those who are currently packing. A new player is added with moveState 0 which
     * means he's not yet decided which structure to transfer
     * @param player the player who has begun packing
     */
    public void startNewPacking(Player player){
        playersPacking.put(player, 0);
    }
}
