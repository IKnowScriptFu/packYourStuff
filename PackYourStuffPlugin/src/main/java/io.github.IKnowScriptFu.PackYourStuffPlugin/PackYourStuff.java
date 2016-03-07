package io.github.IKnowScriptFu.PackYourStuffPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Aemilius on 06/03/2016.
 */
public final class PackYourStuff extends JavaPlugin{


    @Override
    public void onEnable(){
        getLogger().info("Enabled plugin PackYourStuff");

    }

    @Override
    public void onDisable(){
        getLogger().info("Disabled plugin PackYourStuff");
    }

}
