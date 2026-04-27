package cn.nukkit.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityFallingBlock;
import cn.nukkit.event.block.BlockFallEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.cloudburstmc.nbt.NbtType;

import java.util.Arrays;


/**
 * @author rcsuperman (Nukkit Project)
 */
public abstract class BlockFallable extends BlockSolid {

    public BlockFallable(BlockState blockstate) {
        super(blockstate);
    }

    @Override
    public int onUpdate(int type) {
        Block down = this.down();
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if ((down.isAir() || down instanceof BlockFire || down instanceof BlockLiquid ||
                    (down instanceof BlockBubbleColumn && down.getLevelBlockAtLayer(1) instanceof BlockLiquid))) {
                BlockFallEvent event = new BlockFallEvent(this);
                this.level.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return type;
                }
                drop(NbtMap.EMPTY);
                return type;
            }
        }
        return 0;
    }

    public void drop(NbtMap customNbt) {
        this.level.setBlock(this, Block.get(Block.AIR), true, true);
        EntityFallingBlock fall = createFallingEntity(customNbt);

        fall.spawnToAll();
    }

    protected EntityFallingBlock createFallingEntity(NbtMap customNbt) {
      final NbtMapBuilder builder = Entity.getDefaultNBT(new Vector3(this.x + 0.5, this.y, this.z + 0.5)).toBuilder()
                .putCompound("Block", this.blockstate.getBlockStateTag());

        builder.putAll(customNbt);

        EntityFallingBlock fall = (EntityFallingBlock) Entity.createEntity(Entity.FALLING_BLOCK, this.getLevel().getChunk((int) this.x >> 4, (int) this.z >> 4), builder.build());

        if (fall != null) {
            fall.spawnToAll();
        }

        return fall;
    }

    public Item toFallingItem() {
        return this.toItem();
    }
}
