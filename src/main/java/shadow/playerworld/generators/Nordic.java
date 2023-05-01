package shadow.playerworld.generators;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorFlower;
import cn.nukkit.level.generator.populator.impl.PopulatorGrass;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shadow.playerworld.noise.bukkit.SimplexOctaveGenerator;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.generators.noise.Voronoi;

public class Nordic implements BaseGenerator {
    private SimplexOctaveGenerator genHighland;
    private SimplexOctaveGenerator genBase1;
    private SimplexOctaveGenerator genBase2;
    private SimplexOctaveGenerator genHills;
    private SimplexOctaveGenerator genGround;
    private Voronoi voronoiGenBase1;
    private Voronoi voronoiGenBase2;
    private Voronoi voronoiGenMountains;
    private NukkitRandom random;
    private long usedSeed = 1337L;
    private List<Populator> populators;

    public Nordic() {
        this.changeSeed(this.usedSeed);
        this.populators = new ArrayList<>();

        PopulatorTree populatorTree = new PopulatorTree(1);
        populatorTree.setBaseAmount(1);
        populatorTree.setRandomAmount(2);
        this.populators.add(populatorTree);

        PopulatorGrass populatorGrass = new PopulatorGrass();
        populatorGrass.setBaseAmount(50);
        populatorGrass.setRandomAmount(50);
        this.populators.add(populatorGrass);

        PopulatorFlower flower = new PopulatorFlower();
        flower.setBaseAmount(0);
        flower.setRandomAmount(2);
        flower.addType(38, 4);
        flower.addType(38, 5);
        flower.addType(38, 6);
        flower.addType(38, 7);
        flower.addType(38, 8);
        this.populators.add(flower);
    }

    private static void setMaterialAt(BaseFullChunk chunk_data, int x, int y, int z, int material) {
        chunk_data.setBlock(x, y, z, material);
    }

    private static int getMaterialAt(BaseFullChunk chunk_data, int x, int y, int z) {
        return chunk_data.getBlockId(x, y, z);
    }

    @Override
    public void generateTerrain(int chunkx, int chunkz, BaseFullChunk result) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int currheight = 29;
                currheight = Math.max(currheight, this.genBase(x, z, chunkx, chunkz, this.genBase1, this.voronoiGenBase1));
                currheight = Math.max(currheight, this.genBase(x, z, chunkx, chunkz, this.genBase2, this.voronoiGenBase2));
                currheight = Math.max(currheight, this.genGround(x, z, chunkx, chunkz, this.genGround));
                currheight = Math.max(currheight, this.genHills(x, z, chunkx, chunkz, currheight, this.genHills));
                currheight = Math.max(currheight, this.genMountains(x, z, chunkx, chunkz, currheight, this.voronoiGenMountains));
                currheight = Math.max(currheight, this.genHighlands(x, z, chunkx, chunkz, currheight, this.genHighland));
                this.applyHeightMap(x, z, result, currheight);
                this.genFloor(x, z, result);
                this.genTopLayer(x, z, result, currheight);
                result.setBiomeId(x, z, EnumBiome.TAIGA_M.id);
            }
        }
        this.populators.forEach(populator -> populator.populate((ChunkManager)PlayerWorld.instance.world, chunkx, chunkz, this.random, (FullChunk)result));
    }

    private void applyHeightMap(int x, int z, BaseFullChunk chunk_data, int currheight) {
        for (int y = 0; y <= currheight; ++y) {
            Nordic.setMaterialAt(chunk_data, x, y, z, 1);
        }
    }

    private void genFloor(int x, int z, BaseFullChunk chunk_data) {
        for (int y = 0; y < 5; ++y) {
            if (y < 3) {
                Nordic.setMaterialAt(chunk_data, x, y, z, 7);
                continue;
            }
            int rnd = new Random().nextInt(100);
            if (rnd < 40) {
                Nordic.setMaterialAt(chunk_data, x, y, z, 7);
                continue;
            }
            Nordic.setMaterialAt(chunk_data, x, y, z, 1);
        }
    }

    private int genHighlands(int x, int z, int xChunk, int zChunk, int current_height, SimplexOctaveGenerator gen) {
        if (current_height < 50) {
            return 0;
        }
        double noise = gen.noise((double)((float)(x + xChunk * 16) / 250.0f), (double)((float)(z + zChunk * 16) / 250.0f), 0.6, 0.6) * 25.0;
        return (int)(34.0 + noise);
    }

    private int genHills(int x, int z, int xChunk, int zChunk, int current_height, SimplexOctaveGenerator gen) {
        double noise = gen.noise((double)((float)(x + xChunk * 16) / 250.0f), (double)((float)(z + zChunk * 16) / 250.0f), 0.6, 0.6) * 10.0;
        return (int)((double)(current_height - 2) + noise);
    }

    private int genGround(int x, int z, int xChunk, int zChunk, SimplexOctaveGenerator gen) {
        gen.setScale(0.0078125);
        double noise = gen.noise((double)(x + xChunk * 16), (double)(z + zChunk * 16), 0.01, 0.5) * 20.0;
        return (int)(34.0 + noise);
    }

    private int genMountains(int x, int z, int xChunk, int zChunk, int current_height, Voronoi noisegen) {
        double noise = noisegen.get((float)(x + xChunk * 16) / 250.0f, (float)(z + zChunk * 16) / 250.0f) * 100.0f;
        int limit = (int)((double)current_height + noise);
        if (limit < 30) {
            return 0;
        }
        return limit;
    }

    private int genBase(int x, int z, int xChunk, int zChunk, SimplexOctaveGenerator gen, Voronoi noisegen) {
        double noise_raw2;
        double noise_raw1 = gen.noise((double)((float)(x + xChunk * 16) / 1200.0f), (double)((float)(z + zChunk * 16) / 1200.0f), 0.5, 0.5) * 600.0;
        double noise = noise_raw1 * 0.5 + (noise_raw2 = (double)(noisegen.get((float)(x + xChunk * 16) / 800.0f, (float)(z + zChunk * 16) / 800.0f) * 500.0f)) * 0.5;
        double limit = 29.0 + noise;
        if (limit > 55.0) {
            limit = 55.0;
        }
        return (int)limit;
    }

    private void genTopLayer(int x, int z, BaseFullChunk chunk_data, int height) {
        boolean grass = true;
        Random rnd = new Random();
        if (height > 80) {
            return;
        }
        if (height <= 77 || rnd.nextBoolean()) {
            int soil_depth = rnd.nextInt(4);
            if (grass) {
                Nordic.setMaterialAt(chunk_data, x, height, z, 2);
            } else {
                Nordic.setMaterialAt(chunk_data, x, height, z, 3);
            }
            for (int y = height - 1; y >= height - soil_depth; --y) {
                Nordic.setMaterialAt(chunk_data, x, y, z, 3);
            }
        }
    }

    public final void changeSeed(long seed) {
        this.random = new NukkitRandom(seed);
        this.genHighland = new SimplexOctaveGenerator(this.random, 16);
        this.genBase1 = new SimplexOctaveGenerator(this.random, 16);
        this.genBase2 = new SimplexOctaveGenerator(this.random, 16);
        this.genHills = new SimplexOctaveGenerator(this.random, 4);
        this.genGround = new SimplexOctaveGenerator(this.random, 16);
        this.voronoiGenBase1 = new Voronoi(64, true, seed, 16, Voronoi.DistanceMetric.Squared, 4);
        this.voronoiGenBase2 = new Voronoi(64, true, seed, 16, Voronoi.DistanceMetric.Quadratic, 4);
        this.voronoiGenMountains = new Voronoi(64, true, seed, 16, Voronoi.DistanceMetric.Squared, 4);
    }

    private void checkSeed(long worldSeed) {
        if (worldSeed != this.usedSeed) {
            this.changeSeed(worldSeed);
            this.usedSeed = worldSeed;
        }
    }

    @Override
    public String getName() {
        return "\u5317\u6b27\u4e16\u754c";
    }
}

