package io.github.IKnowScriptFu.PackYourStuffPlugin;

import io.github.IKnowScriptFu.PackYourStuffPlugin.utils.Cardinals;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 Created by Aemilius on 09/03/2016.
 */
public class PackingHandler implements Listener{
    private Map<Player, Integer> playersPacking = new HashMap<>();
    private Map<Player, Location[]> movingStructures = new HashMap<>();
    private Map<Player, Building> savedStructures = new HashMap<>();

    private PackingConversation packingConversation;
    private Plugin packPlugin;


    public PackingHandler(Plugin plugin) {
        packPlugin = plugin;
        packingConversation = new PackingConversation(packPlugin, this::beginPackingProcedure);
        packPlugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void playerPacking(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(playersPacking.keySet().contains(player)) {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.GOLD_PICKAXE) && event.getAction()
                    .equals(Action.RIGHT_CLICK_BLOCK)) {
                Integer moveState = playersPacking.get(player);
                if (moveState == 0) {
                    movingStructures.put(player, new Location[2]);
                    playersPacking.put(player, 1);
                    Block firstBlock = event.getClickedBlock();
                    movingStructures.get(player)[0] = firstBlock.getLocation();
                } else if (moveState == 1) {
                    Block secondBlock = event.getClickedBlock();
                    if (!secondBlock.getLocation().equals(movingStructures.get(player)[0])) {
                        removePlayer(player);
                        movingStructures.get(player)[1] = secondBlock.getLocation();
                        Conversation conversation = packingConversation.buildConversation(player);
                        conversation.begin();
                    }
                }
            }
        }
    }

    private Void beginPackingProcedure(Player player){
        double width;
        double height;
        double depth;

        Location firstBlock =  movingStructures.get(player)[0];
        Location secondBlock = movingStructures.get(player)[1];

        double minX = Math.min(firstBlock.getX(), secondBlock.getX());
        double minY = Math.min(firstBlock.getY(), secondBlock.getY());
        double minZ = Math.min(firstBlock.getZ(), secondBlock.getZ());

        width = Math.max(firstBlock.getX(), secondBlock.getX()) - minX;
        height = Math.max(firstBlock.getY(), secondBlock.getY()) - minY;
        depth = Math.max(firstBlock.getZ(), secondBlock.getZ()) - minZ;

        Cardinals facingDirection = Cardinals.vectorToCardinal(player.getLocation().getDirection());
        MaterialData[][][] structure;
        World world = player.getWorld();

        if( facingDirection  == Cardinals.WEST || facingDirection == Cardinals.EAST){
                double temp = depth;
                depth = width;
                width = temp;
        }

        structure = new MaterialData[(int)(height)+1][(int)(depth)+1][(int)(width)+1];
        Block b;

        for(double y = 0; y<=(int)(height); y++){
            for(double z = 0; z<=(int)(depth); z++){
                for(double x = 0; x<=(int)(width); x++){
                    switch (facingDirection){
                        case NORTH:
                            b = world.getBlockAt((int) (minX+x), (int) (minY+y), (int) (minZ+depth-z));
                            break;
                        case EAST:
                            b = world.getBlockAt((int) (minX+z), (int) (minY+y), (int) (minZ+x));
                            break;
                        case SOUTH:
                            b = world.getBlockAt((int) (minX+width-x), (int) (minY+y), (int) (minZ+z));
                            break;
                        case WEST:
                            b = world.getBlockAt((int) (minX+depth-z), (int) (minY+y), (int) (minZ+width-x));
                            break;
                        default:
                            return null;
                    }
                    structure[(int) y][(int)z][(int) x] = b.getState().getData();
                }
            }
        }

        setWhatIsBuilding(player, new Building(String.valueOf(new Random().nextLong()), player.getName(), "N/A", structure,
                facingDirection));
        return null;
    }

    public void setWhatIsBuilding(Player player, Building building){
        savedStructures.put(player, building);
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
        if(movingStructures.keySet().contains(player)){
            movingStructures.remove(player);
        }
    }

    public void buildStructure(Player player) {
        StructureBuilder structureBuilder = new StructureBuilder(player, savedStructures.get(player));
        structureBuilder.runTaskTimer(packPlugin, 60, 1);
    }

    public Building getWhatsBuilding(Player player){
        try {
            return savedStructures.get(player);
        }catch (NullPointerException e){ return null; }
    }


}
