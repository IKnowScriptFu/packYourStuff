package io.github.IKnowScriptFu.PackYourStuffPlugin;

import io.github.IKnowScriptFu.PackYourStuffPlugin.utils.Cardinals;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.*;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Aemilius on 13/03/2016.
 */
public class StructureBuilder  extends BukkitRunnable{
    private Cardinals facingDirection;
    private Location firstStone;
    private Building building;
    private MaterialData[][][] structure;
    private World world;
    private int nextX = 0, nextY = 0, nextZ = 0;
    private int X0, Y0, Z0;
    private int width, height, depth;
    private int angle;

    public StructureBuilder(Player player, Building building) {
        world = player.getWorld();
        firstStone = player.getLocation();
        X0 = firstStone.getBlockX();
        Y0 = firstStone.getBlockY();
        Z0 = firstStone.getBlockZ();
        facingDirection = Cardinals.vectorToCardinal(player.getLocation().getDirection());
        this.building = building;
        structure = building.structure;
        height = building.getHeight();
        depth =  building.getDepth();
        width = building.getWidth();
        angle = Cardinals.angleBetween(building.frontFace, facingDirection);
    }
    private Integer[][] adjacents = {{0, 0, 1}, {0, 1, 0}, {1, 0, 0}};
    private Block[] adjBlocks = new Block[3];

    @Override
    public void run() {
        Block toBeChanged = nextBlock();
        MaterialData replacement = structure[nextY][nextZ][nextX];

        if(replacement instanceof Directional){
            MaterialData surrounding;
            try {
            adjBlocks[0] = toBeChanged.getRelative(Cardinals.toBlockFace(Cardinals.rotateCardinal(facingDirection,90)));
            adjBlocks[1] = toBeChanged.getRelative(Cardinals.toBlockFace(facingDirection));
            adjBlocks[2] = toBeChanged.getRelative(BlockFace.UP);

                int i = 0;
                for (Integer[] adj : adjacents) {
                    surrounding = structure[nextY + adj[0]][nextZ + adj[1]][nextX + adj[2]];
                    replaceBlock(adjBlocks[i++], surrounding);
                }
            }catch (NullPointerException | IndexOutOfBoundsException ignored){}
        }

        replaceBlock(toBeChanged, replacement);

        try {
            do {
                if ((nextX == width - 1) && (nextY == height - 1) && (nextZ == depth - 1)) {
                    this.cancel();
                }

                nextX = (nextX + 1) % width;
                if (nextX == 0) {
                    nextZ = (nextZ + 1) % depth;
                    if (nextZ == 0) {
                        nextY++;
                    }
                }
            } while (skippable());
        }catch (IndexOutOfBoundsException | NullPointerException ignored ){}
    }

    private boolean skippable(){
        if (structure[nextY][nextZ][nextX].getItemType().equals(nextBlock().getType())) {
            if (structure[nextY][nextZ][nextX].getData() == (nextBlock().getData())) {
                return true;
            }
        }
        return false;
    }

    private void replaceBlock(Block toBeChanged, MaterialData replacement){
        toBeChanged.setType(replacement.getItemType());
        toBeChanged.setData(replacement.getData());

        if (toBeChanged.getState().getData() instanceof Directional){
            Directional dir = ((Directional) toBeChanged.getState().getData());
            BlockFace bf;
            if(toBeChanged.getState().getData() instanceof Stairs || toBeChanged.getState().getData() instanceof
                    Ladder) {
                bf = Cardinals.rotateBlockFace(dir.getFacing(), angle + 180);
            }else {
                bf = Cardinals.rotateBlockFace(dir.getFacing(), angle);
            }

            BlockState bs = toBeChanged.getState();
            if(bf != null) {
                ((Directional) bs.getData()).setFacingDirection(bf);
            }

            bs.update();
        }
    }

    private Block nextBlock(){
        switch (facingDirection){
            case NORTH:
                return world.getBlockAt(X0 + nextX, Y0 + nextY, Z0 - nextZ);
            case EAST:
                return world.getBlockAt(X0 + nextZ, Y0 + nextY, Z0 + nextX);
            case SOUTH:
                return world.getBlockAt(X0 - nextX, Y0 + nextY, Z0 + nextZ);
            case WEST:
                return world.getBlockAt(X0 - nextZ, Y0 + nextY, Z0 - nextX);
            default:
                return null;
        }
    }

}
