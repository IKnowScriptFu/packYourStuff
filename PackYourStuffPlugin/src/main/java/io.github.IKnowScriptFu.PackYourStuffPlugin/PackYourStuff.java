package io.github.IKnowScriptFu.PackYourStuffPlugin;

import io.github.IKnowScriptFu.PackYourStuffPlugin.utils.BlueprintsHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Aemilius on 06/03/2016.
 */
public final class PackYourStuff extends JavaPlugin{

    private PackingHandler packingHandler;
    private BlueprintsHandler blueprintsHandler;
    private List<Building> allBuildings = new LinkedList<>();

    @Override
    public void onEnable(){
        packingHandler = new PackingHandler(this);
        blueprintsHandler = new BlueprintsHandler(allBuildings);
        getLogger().info("Enabled plugin PackYourStuff");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("packit")){
                if(args.length == 0) {
                    packingHandler.startNewPacking((Player) sender);
                }else {
                    switch (args[0].toLowerCase()){
                        case "clone":
                            packingHandler.buildStructure((Player) sender);
                            break;
                        case "list":
                            allBuildings.forEach(building -> sender.sendMessage(building.toString()));
                            break;
                        case "save":
                            Building playerBuilding = packingHandler.getWhatsBuilding((Player) sender);
                            if(playerBuilding != null) {
                                try {
                                    if ((args[1] != null) && (args[2] != null)) {
                                        blueprintsHandler.saveBuilding(new Building(args[1], ((Player) sender).getName(), args[2],
                                                playerBuilding.structure, playerBuilding.frontFace));
                                    }
                                }catch (IndexOutOfBoundsException e){
                                    blueprintsHandler.saveBuilding(playerBuilding);
                                }
                            }else {
                                sender.sendMessage("You're not moving any building!");
                            }
                            break;
                        case "select":
                            if(args[1] != null){
                                try {
                                    packingHandler.setWhatIsBuilding((Player) sender, allBuildings.get(Integer
                                            .parseInt(args[1])));
                                }catch (IndexOutOfBoundsException e){
                                    sender.sendMessage("The structure you selected does not exist");
                                }
                            }else{
                                sender.sendMessage("Wrong number of arguments");
                            }
                            break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDisable(){
        getLogger().info("Disabled plugin PackYourStuff");
        freeMemory();
    }

    /**
     * Frees the resources used
     */
    private void freeMemory() {
        packingHandler = null;
    }

}
