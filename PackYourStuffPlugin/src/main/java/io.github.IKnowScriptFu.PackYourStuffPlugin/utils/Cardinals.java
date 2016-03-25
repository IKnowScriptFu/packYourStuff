package io.github.IKnowScriptFu.PackYourStuffPlugin.utils;

import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

/**
 * Created by Aemilius on 16/03/2016.
 */
public enum Cardinals {
    NORTH (0),
    EAST (90),
    SOUTH (180),
    WEST (270);

    public final int angle;

    Cardinals(int i) {
        angle = i;
    }

    public static Cardinals vectorToCardinal(Vector entityVector){
        double x = entityVector.getX();
        double z = entityVector.getZ();

        if ( Math.abs(z) >= Math.abs(x) ){
            if ( z < 0 ){
                return Cardinals.NORTH;
            }else {
                return Cardinals.SOUTH;
            }
        } else {
            if (x < 0){
                return Cardinals.WEST;
            }
            return Cardinals.EAST;
        }
    }

    public static int angleBetween(Cardinals first, Cardinals second){
        return second.angle - first.angle;
    }

    public static Cardinals rotateCardinal( Cardinals cardinal, int angle){
        int newAngle = (cardinal.angle + (angle%360) + 360)%360;
        switch (newAngle){
            case 0:
                return NORTH;
            case 90:
                return EAST;
            case 180:
                return SOUTH;
            case 270:
                return WEST;
        }
        return null;
    }

    public static BlockFace rotateBlockFace(BlockFace blockFace, int angle){
        Cardinals bf = Cardinals.toCardinal(blockFace);
        if(bf != null) {
            bf = rotateCardinal(bf, angle);
            return toBlockFace(bf);
        }
        return null;
    }

    public static BlockFace toBlockFace( Cardinals cardinal){
        switch (cardinal){
            case NORTH:
                return BlockFace.NORTH;
            case EAST:
                return BlockFace.EAST;
            case SOUTH:
                return BlockFace.SOUTH;
            case WEST:
                return BlockFace.WEST;
        }
        return null;
    }

    public static Cardinals toCardinal(BlockFace blockface){
        switch (blockface){
            case NORTH:
                return NORTH;
            case EAST:
                return EAST;
            case SOUTH:
                return SOUTH;
            case WEST:
                return WEST;
        }
        return null;
    }

    public static Cardinals parseCardinalName(String name){
        switch (name.toLowerCase()){
            case "north":
                return NORTH;
            case "east":
                return EAST;
            case "south":
                return SOUTH;
            case "west":
                return WEST;
            default:
                return null;
        }
    }
}
