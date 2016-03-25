package io.github.IKnowScriptFu.PackYourStuffPlugin;

import io.github.IKnowScriptFu.PackYourStuffPlugin.utils.Cardinals;
import org.bukkit.block.Block;
import org.bukkit.material.MaterialData;

/**
 * Created by Aemilius on 16/03/2016.
 */
public class Building {
    public final String name;
    public final String author;
    public final String type;
    public final MaterialData[][][] structure;
    public final Cardinals frontFace;

    public Building(String name, String author, String type, MaterialData[][][] structure, Cardinals frontFace) {
        this.name = name;
        this.author = author;
        this.type = type;
        this.structure = structure;
        this.frontFace = frontFace;
    }

    public Building(MaterialData[][][] structure, Cardinals frontFace){
        this("N/A", "N/A", "N/A", structure, frontFace);
    }

    public int getHeight(){
        return structure.length;
    }

    public int getDepth(){
        return structure[0].length;
    }

    public int getWidth(){
        return structure[0][0].length;
    }

    @Override
    public String toString(){
        return "\nName: "+name+"\nAuthor: "+author+"\nType: "+type+"\nSize:"+"\n    Width: "+structure[0][0]
                .length+"\n    Depth: "+
                structure[0].length+"\n    Height: "+structure.length;
    }
}
