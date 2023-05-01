package shadow.playerworld.generators.vanilla;

import cn.nukkit.level.biome.EnumBiome;
import java.util.Random;
import shadow.playerworld.generators.noise.NoiseGeneratorOctaves3D;

public class BetaBiome {
    public static final BetaBiome RAINFOREST = new BetaBiome("Rainforest", EnumBiome.JUNGLE.id);
    public static final BetaBiome SWAMPLAND = new BetaBiome("Swampland", EnumBiome.SWAMP.id);
    public static final BetaBiome SEASONAL_FOREST = new BetaBiome("Seasonal forest", EnumBiome.FOREST.id);
    public static final BetaBiome FOREST = new BetaBiome("Forest", EnumBiome.FOREST.id);
    public static final BetaBiome SAVANNA = new BetaBiome("Savanna", EnumBiome.SAVANNA.id);
    public static final BetaBiome SHRUBLAND = new BetaBiome("Shrubland", EnumBiome.PLAINS.id);
    public static final BetaBiome TAIGA = new BetaBiome("Taiga", EnumBiome.TAIGA.id);
    public static final BetaBiome DESERT = new BetaBiome("Desert", EnumBiome.DESERT.id);
    public static final BetaBiome PLAINS = new BetaBiome("Plains", EnumBiome.PLAINS.id);
    public static final BetaBiome ICE_DESERT = new BetaBiome("Ice desert", EnumBiome.COLD_BEACH.id);
    public static final BetaBiome TUNDRA = new BetaBiome("Tundra", EnumBiome.ICE_PLAINS.id);
    private final int nukkitBiome;
    private final String name;

    BetaBiome(String name, int biome) {
        this.name = name;
        this.nukkitBiome = biome;
    }

    public int getBiome() {
        return this.nukkitBiome;
    }

    public String getName() {
        return this.name;
    }

    public int getTreesPerChunk(Random random, NoiseGeneratorOctaves3D noiseGen, int chunkX, int chunkZ) {
        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;
        int treesRand = (int)((noiseGen.generateNoise((double)x * 0.5, (double)z * 0.5) / 8.0 + random.nextDouble() * 4.0 + 4.0) / 3.0);
        int trees = 0;
        if (random.nextInt(10) == 0) {
            ++trees;
        }
        if (this == FOREST) {
            trees += treesRand + 5;
        }
        if (this == RAINFOREST) {
            trees += treesRand + 5;
        }
        if (this == SEASONAL_FOREST) {
            trees += treesRand + 2;
        }
        if (this == TAIGA) {
            trees += treesRand + 5;
        }
        if (this == DESERT) {
            trees -= 20;
        }
        if (this == TUNDRA) {
            trees -= 20;
        }
        if (this == PLAINS) {
            trees -= 20;
        }
        return trees;
    }

    public int getCactusForBiome() {
        int k16 = 0;
        if (this.equals(DESERT)) {
            k16 += 10;
        }
        return k16;
    }

    public int getDeadBushPerChunk() {
        int byte1 = 0;
        if (this.equals(DESERT)) {
            byte1 = 2;
        }
        return byte1;
    }

    public int getGrassPerChunk() {
        int byte1 = 0;
        if (this.equals(FOREST)) {
            byte1 = 2;
        }
        if (this.equals(RAINFOREST)) {
            byte1 = 10;
        }
        if (this.equals(SEASONAL_FOREST)) {
            byte1 = 2;
        }
        if (this.equals(TAIGA)) {
            byte1 = 1;
        }
        if (this.equals(PLAINS)) {
            byte1 = 10;
        }
        return byte1;
    }

    public int getFlowersPerChunk() {
        int flowers = 0;
        if (this.equals(FOREST)) {
            flowers = 2;
        }
        if (this.equals(SEASONAL_FOREST)) {
            flowers = 4;
        }
        if (this.equals(TAIGA)) {
            flowers = 2;
        }
        if (this.equals(PLAINS)) {
            flowers = 3;
        }
        return flowers;
    }
}

