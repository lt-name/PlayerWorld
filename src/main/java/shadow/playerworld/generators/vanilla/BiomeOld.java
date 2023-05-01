package shadow.playerworld.generators.vanilla;

public class BiomeOld {
    private static final BetaBiome[] biomeLookupTable = new BetaBiome[4096];

    public static void generateBiomeLookup() {
        for (int i = 0; i < 64; ++i) {
            for (int j = 0; j < 64; ++j) {
                BiomeOld.biomeLookupTable[i + j * 64] = BiomeOld.getBiome((float)i / 63.0f, (float)j / 63.0f);
            }
        }
    }

    public static int top(BetaBiome biome) {
        if (biome.equals(BetaBiome.DESERT)) {
            return 12;
        }
        return 2;
    }

    public static int filler(BetaBiome biome) {
        if (biome.equals(BetaBiome.DESERT)) {
            return 12;
        }
        return 3;
    }

    public static BetaBiome getBiomeFromLookup(double d, double d1) {
        int i = (int)(d * 63.0);
        int j = (int)(d1 * 63.0);
        return biomeLookupTable[i + j * 64];
    }

    private static BetaBiome getBiome(float f, float f1) {
        return f < 0.1f ? BetaBiome.TUNDRA : (f1 < 0.2f ? (f < 0.5f ? BetaBiome.TUNDRA : (f < 0.95f ? BetaBiome.SAVANNA : BetaBiome.DESERT)) : (f1 > 0.5f && f < 0.7f ? BetaBiome.SWAMPLAND : (f < 0.5f ? BetaBiome.TAIGA : (f < 0.97f ? (f1 < 0.35f ? BetaBiome.SHRUBLAND : BetaBiome.FOREST) : (f1 < 0.45f ? BetaBiome.PLAINS : ((f1 *= f) < 0.9f ? BetaBiome.SEASONAL_FOREST : BetaBiome.RAINFOREST))))));
    }
}

