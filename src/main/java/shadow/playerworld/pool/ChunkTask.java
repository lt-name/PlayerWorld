package shadow.playerworld.pool;

import cn.nukkit.level.format.generic.BaseFullChunk;
import shadow.playerworld.generators.BaseGenerator;

public class ChunkTask implements BaseTask {
    int chunkX;
    int chunkZ;
    BaseGenerator bg;
    BaseFullChunk chunk;

    public ChunkTask(int chunkX, int chunkZ, BaseGenerator bg, BaseFullChunk chunk) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.bg = bg;
        this.chunk = chunk;
    }

    @Override
    public void run() {
        this.bg.generateTerrain(this.chunkX, this.chunkZ, this.chunk);
    }
}

