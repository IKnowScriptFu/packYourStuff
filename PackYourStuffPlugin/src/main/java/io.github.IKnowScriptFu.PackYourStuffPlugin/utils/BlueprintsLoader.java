package io.github.IKnowScriptFu.PackYourStuffPlugin.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Aemilius on 06/03/2016.
 */
public class BlueprintsLoader {
    JSONParser dataRetriever = new JSONParser();
    Path blueprintsPath;
    JSONObject blueprints;

    boolean loadingFailed;

    /**
     * Constructs the BlueprintsLoader, if something goes wrong sets loadingFailed to true, after this this the
     * loader cannot be used ever again. EVER.
     **/
    public BlueprintsLoader() {
        System.out.println();
        if(!(loadBlueprints()== null)){
            System.out.println("Failed");
            loadingFailed = true;
            releaseAllResources();
        }
    }

    /**
     * Release all resources used.
     */
    private void releaseAllResources() {
        dataRetriever = null;
        blueprintsPath = null;
        blueprints = null;
    }

    /**
     * Loads the json file containing the blueprints for the structures.
     * @return The JSONObject if everything went fine, null otherwise
     */
    public Exception loadBlueprints() {
        try {
            blueprintsPath = Paths.get(System.getProperty("user.dir")+"/Blueprints/blueprints.json");
            blueprints = (JSONObject) dataRetriever.parse(Files.newBufferedReader(blueprintsPath));
        } catch (ParseException e) {
            return e;
        } catch (IOException e) {
            return e;
        }
        return null;
    }

    /**
     @return The raw JSON file containing all the blueprints
     */
    public JSONObject getAllBlueprints(){
        return blueprints;
    }

    /**
     @return True if loading failed
     */
    public boolean loadingFailed(){
        return loadingFailed;
    }
}
