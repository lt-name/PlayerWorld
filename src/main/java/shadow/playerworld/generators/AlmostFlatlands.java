package shadow.playerworld.generators;

import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorGrass;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import java.util.ArrayList;
import java.util.List;

import shadow.playerworld.noise.SimplexOctaveGenerator;
import shadow.playerworld.PlayerWorld;

public class AlmostFlatlands implements BaseGenerator {
    private static final int WORLD_HEIGHT = 64;
    private final NukkitRandom random;
    private List<Populator> populators = new ArrayList<>();

    public AlmostFlatlands() {
        PopulatorGrass p = new PopulatorGrass();
        p.setBaseAmount(50);
        p.setRandomAmount(50);
        this.populators.add((Populator)p);
        this.random = new NukkitRandom(PlayerWorld.seed);
    }

    private void setBlockAt(BaseFullChunk chunk, int x, int y, int z, int type) {
        chunk.setBlock(x, y, z, type);
    }

    @Override
    public String getName() {
        return "近平坦";
    }

    @Override
    public void generateTerrain(int chunkX, int chunkZ, BaseFullChunk chunk) {
        SimplexOctaveGenerator gen = new SimplexOctaveGenerator(PlayerWorld.NRAND, 8);
        gen.setScale(0.015625);
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int y;
                this.setBlockAt(chunk, x, 0, z, 7);
                int height = (int)(gen.noise(x + chunkX * 16, z + chunkZ * 16, 0.5, 0.5) / 0.75 + 64.0);
                for (y = 1; y < height - 4; ++y) {
                    this.setBlockAt(chunk, x, y, z, 1);
                }
                for (y = height - 4; y < height; ++y) {
                    this.setBlockAt(chunk, x, y, z, 3);
                }
                this.setBlockAt(chunk, x, height, z, 2);
                chunk.setBiomeId(x, z, EnumBiome.PLAINS.id);
            }
        }
        this.populators.forEach(populator -> populator.populate(PlayerWorld.instance.world, chunkX, chunkZ, this.random, chunk));
    }
}

