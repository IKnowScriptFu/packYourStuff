package io.github.IKnowScriptFu.PackYourStuffPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Aemilius on 06/03/2016.
 */
public final class PackYourStuff extends JavaPlugin{

    @Override
    public void onEnable(){
        getLogger().info("Enabled plugin PackYourStuff");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            if(command.getName().equalsIgnoreCase("packit")){

            }
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
    }

}
