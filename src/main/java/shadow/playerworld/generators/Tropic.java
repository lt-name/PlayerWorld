package shadow.playerworld.generators;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorMelon;
import cn.nukkit.level.generator.populator.impl.tree.JungleBigTreePopulator;
import cn.nukkit.level.generator.populator.impl.tree.JungleTreePopulator;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shadow.playerworld.noise.bukkit.SimplexNoiseGenerator;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.generators.noise.FMB_RMF;
import shadow.playerworld.generators.noise.Voronoi;

public class Tropic implements BaseGenerator {
    private long usedSeed = 1337L;
    private FMB_RMF n_p;
    private SimplexNoiseGenerator ground_nouise;
    private Voronoi cliffs;
    private Random random;
    private NukkitRandom nrand;
    private List<Populator> populators;

    public Tropic() {
        this.changeSeed(this.usedSeed + 1L);
        this.populators = new ArrayList<Populator>();
        JungleTreePopulator trees = new JungleTreePopulator();
        trees.setBaseAmount(10);
        this.populators.add((Populator)trees);
        JungleBigTreePopulator bigTrees = new JungleBigTreePopulator();
        bigTrees.setBaseAmount(6);
        this.populators.add((Populator)bigTrees);
        PopulatorMelon melon = new PopulatorMelon();
        melon.setBaseAmount(-65);
        melon.setRandomAmount(70);
        this.populators.add((Populator)melon);
    }

    @Override
    public void generateTerrain(int xchunk, int zchunk, BaseFullChunk result) {
        this.checkSeed(PlayerWorld.seed);
        int SEELEVEL = 60;
        int TIMBERLIMIT = 110;
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int y;
                int currheight = 50;
                currheight = Math.max(currheight, this.genGroundNoise(x, z, xchunk, zchunk) + 60);
                int DIRTHEIGHT = this.random.nextInt(5);
                boolean DO_GRASS = this.random.nextBoolean();
                for (y = 0; y <= currheight; ++y) {
                    if (y == currheight) {
                        if (currheight < 59) {
                            result.setBlock(x, y, z, 3);
                            continue;
                        }
                        if (y == 59 || y == 60 || y == 61) {
                            result.setBlock(x, y, z, 12);
                            continue;
                        }
                        if (y >= 110) {
                            if (DO_GRASS) {
                                result.setBlock(x, y, z, 2);
                                continue;
                            }
                            result.setBlock(x, y, z, 1);
                            continue;
                        }
                        result.setBlock(x, y, z, 2);
                        continue;
                    }
                    if (y >= 110) {
                        result.setBlock(x, y, z, 1);
                        continue;
                    }
                    if (currheight - y <= DIRTHEIGHT) {
                        result.setBlock(x, y, z, 3);
                        continue;
                    }
                    result.setBlock(x, y, z, 1);
                }
                if (currheight + 1 <= 60) {
                    for (y = currheight + 1; y <= 60; ++y) {
                        result.setBlock(x, y, z, 9);
                    }
                }
                result.setBlock(x, 0, z, 7);
                result.setBlock(x, 1, z, 7);
                if (this.random.nextBoolean()) {
                    result.setBlock(x, 2, z, 7);
                }
                result.setBiomeId(x, z, EnumBiome.JUNGLE.id);
            }
        }
        this.populators.forEach(populator -> populator.populate((ChunkManager)PlayerWorld.instance.world, xchunk, zchunk, this.nrand, (FullChunk)result));
    }

    private int genGroundNoise(int x, int z, int xchunk, int zchunk) {
        double x_calc = (double)(x + xchunk * 16 + 0x3FFFFFFF) * 0.003;
        double z_calc = (double)(z + zchunk * 16 + 0x3FFFFFFF) * 0.003;
        double temp = this.n_p.noise_FractionalBrownianMotion(x_calc, z_calc, 0.0, 6, 0.45f, 1.5f);
        double ground = this.ground_nouise.noise(x_calc, z_calc, 4, 0.25, 0.125) * 33.0;
        double cliff = this.cliffs.get((float)(x + xchunk * 16) / 250.0f, (float)(z + zchunk * 16) / 250.0f) * 120.0f;
        double noise = ground + Math.abs(this.n_p.noise_RidgedMultiFractal(x_calc, z_calc, 0.0, 4, 2.85f, 0.45f, 1.0f) + (double)0.05f * temp) * 55.0;
        return (int)Math.round(noise -= cliff);
    }

    private void changeSeed(long seed) {
        if (this.usedSeed == seed) {
            return;
        }
        this.usedSeed = seed;
        this.n_p = new FMB_RMF(seed);
        this.ground_nouise = new SimplexNoiseGenerator(seed);
        this.cliffs = new Voronoi(64, true, seed, 16, Voronoi.DistanceMetric.Squared, 4);
        this.random = new Random(seed);
        this.nrand = new NukkitRandom(seed);
    }

    private void checkSeed(Long worldSeed) {
        if (worldSeed != this.usedSeed) {
            this.usedSeed = worldSeed;
            this.changeSeed(worldSeed);
        }
    }

    @Override
    public String getName() {
        return "\u70ed\u5e26\u4e16\u754c";
    }
}

