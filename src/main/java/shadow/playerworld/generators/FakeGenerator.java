package shadow.playerworld.generators;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FakeGenerator extends Generator {
    public static final Map<String, BaseGenerator> dict = new LinkedHashMap<String, BaseGenerator>();
    protected ChunkManager level;
    protected NukkitRandom nukkitRandom;
    protected final List<Populator> generationPopulators = new ArrayList<Populator>();
    protected final List<Populator> populators = new ArrayList<Populator>();

    public FakeGenerator(Map options) {
    }

    public int getId() {
        return 1;
    }

    public void init(ChunkManager cm, NukkitRandom nr) {
        this.level = cm;
        this.nukkitRandom = nr;
    }

    public void generateChunk(int chunkX, int chunkZ) {
        BaseFullChunk chunkData = this.level.getChunk(chunkX, chunkZ);
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                chunkData.setBiomeId(x, z, EnumBiome.PLAINS.id);
            }
        }
        this.generationPopulators.forEach(populator -> populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, (FullChunk)chunkData));
    }

    public void populateChunk(int chunkX, int chunkZ) {
        BaseFullChunk chunkData = this.level.getChunk(chunkX, chunkZ);
        this.populators.forEach(populator -> populator.populate(this.level, chunkX, chunkZ, this.nukkitRandom, (FullChunk)chunkData));
    }

    public Map<String, Object> getSettings() {
        return Collections.emptyMap();
    }

    public String getName() {
        return "normal";
    }

    public Vector3 getSpawn() {
        return new Vector3(0.5, 256.0, 0.5);
    }

    public ChunkManager getChunkManager() {
        return this.level;
    }

    static {
        BaseGenerator bg = new VanillaLike();
        dict.put(bg.getName(), bg);
        bg = new AlmostFlatlands();
        dict.put(bg.getName(), bg);
        bg = new Fjord();
        dict.put(bg.getName(), bg);
        bg = new Hoth();
        dict.put(bg.getName(), bg);
        bg = new Island();
        dict.put(bg.getName(), bg);
        bg = new Tatooine();
        dict.put(bg.getName(), bg);
        bg = new Nordic();
        dict.put(bg.getName(), bg);
        bg = new Tropic();
        dict.put(bg.getName(), bg);
    }
}

