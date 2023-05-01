package shadow.playerworld.generators;

import cn.nukkit.level.format.generic.BaseFullChunk;

public interface BaseGenerator {
    public String getName();

    public void generateTerrain(int var1, int var2, BaseFullChunk var3);
}

