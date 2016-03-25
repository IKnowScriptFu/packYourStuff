package io.github.IKnowScriptFu.PackYourStuffPlugin.utils;

import io.github.IKnowScriptFu.PackYourStuffPlugin.Building;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Aemilius on 06/03/2016.
 */
public class BlueprintsHandler {
    private JSONParser dataRetriever = new JSONParser();
    private Path blueprintsPath;
    private JSONObject blueprints;
    private List<Building> buildings;
    private boolean loadingFailed;

    /**
     * Constructs the BlueprintsHandler, if something goes wrong sets loadingFailed to true, after this this the
     * loader cannot be used ever again. EVER.
     *
     * @param buildings*/
    public BlueprintsHandler(List<Building> buildings) {
        this.buildings = buildings;
        Exception e = loadBlueprints();
        if(e != null) {
            System.out.println(e.toString());
            if (e instanceof ParseException) {
                blueprints = new JSONObject();
            } else {
                loadingFailed = true;
                releaseAllResources();
            }
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
    private Exception loadBlueprints() {
        try {
            blueprintsPath = Paths.get(System.getProperty("user.dir")+"/plugins/Blueprints/blueprints.json");
            blueprints = (JSONObject) dataRetriever.parse(Files.newBufferedReader(blueprintsPath));
            parseBlueprints(blueprints, buildings);
        } catch (ParseException e) {
            return e;
        } catch (IOException e) {
            return e;
        }
        return null;
    }

    private void parseBlueprints(JSONObject blueprints, List<Building> buildings) {
        blueprints.keySet().forEach(key->{
            JSONObject nextBuilding = (JSONObject) blueprints.get(key);
            String name, author, type;
            Cardinals frontFace;
            JSONArray structure;
            Long[] size = new Long[3];

            name = (String) key;
            author = (String) nextBuilding.get("author");
            type = (String) nextBuilding.get("type");
            frontFace = Cardinals.parseCardinalName((String) nextBuilding.get("frontface"));
            JSONArray sizesArr = (JSONArray) nextBuilding.get("size");
            for(int i = 0; i < 3; i++){
                size[i] = (Long) sizesArr.get(i);
            }
            structure = (JSONArray) nextBuilding.get("structure");

            buildings.add(new Building(name, author, type, parseStructure(structure, size), frontFace));
        });
    }

    /**
     @return The raw JSON file containing all the blueprints
     */
    public JSONObject getAllBlueprints(){
        return blueprints;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    /**
     @return True if loading failed
     */
    public boolean loadingFailed(){
        return loadingFailed;
    }

    public Set<String> getAllNames(){
        return blueprints.keySet();
    }

    public void writeRandomStuff(){

    }

    private MaterialData[][][] parseStructure(JSONArray structure, Long[] size){
        int height = Math.toIntExact(size[0]);
        int depth = Math.toIntExact(size[1]);
        int width = Math.toIntExact(size[2]);

        MaterialData[][][] structureData = new MaterialData[height][depth][width];
        for(int h = 0; h < height; h++){
            for (int d = 0; d < depth; d++){
                for (int w = 0; w < width; w++){
                    int next = h*(depth*width)+d*(width)+w;
                    JSONArray buildingBlock = (JSONArray) structure.get(next);
                    Material material = Material.matchMaterial((String) buildingBlock.get(0));
                    byte data = ((Long) buildingBlock.get(1)).byteValue();
                    structureData[h][d][w] = new MaterialData(material, data);
                }
            }
        }
        return  structureData;
    }

    public void saveBuilding(Building building){
        buildings.add(building);
        JSONObject newBuilding = new JSONObject();
        newBuilding.put("author", building.author);
        newBuilding.put("type", building.type);
        newBuilding.put("frontface", building.frontFace.toString());

        JSONArray structure = new JSONArray();
        JSONArray element = new JSONArray();
        for(MaterialData[][] level: building.structure){
            for (MaterialData[] slice: level) {
                for( MaterialData buildingBlock: slice){
                    element.add(buildingBlock.getItemType().toString());
                    element.add(buildingBlock.getData());
                    structure.add(element.clone());
                    element.clear();
                }
            }
        }

        JSONArray size = new JSONArray();
        size.addAll(Arrays.asList(building.getHeight(), building.getDepth(), building.getWidth()));
        newBuilding.put("size", size);
        newBuilding.put("structure", structure);

        blueprints.put(building.name, newBuilding);

        try {
            Writer writer = Files.newBufferedWriter(blueprintsPath, StandardOpenOption.WRITE);
            blueprints.writeJSONString(writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
