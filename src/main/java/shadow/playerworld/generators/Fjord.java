package shadow.playerworld.generators;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorGrass;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shadow.playerworld.noise.bukkit.PerlinOctaveGenerator;
import shadow.playerworld.noise.bukkit.SimplexOctaveGenerator;
import shadow.playerworld.PlayerWorld;

public class Fjord implements BaseGenerator {
    private long seed = -1L;
    private SimplexOctaveGenerator gen1 = null;
    private PerlinOctaveGenerator gen2 = null;
    private PerlinOctaveGenerator gen3 = null;
    private SimplexOctaveGenerator gen4 = null;
    private SimplexOctaveGenerator gen5 = null;
    private Random random;
    private NukkitRandom nrand;
    private List<Populator> populators;

    @Override
    public String getName() {
        return "\u5ce1\u6e7e\u4e16\u754c";
    }

    private void initNoise(long s) {
        if (s == this.seed) {
            return;
        }
        this.seed = s;
        this.random = new Random(s);
        this.gen1 = new SimplexOctaveGenerator(this.seed, 8);
        this.gen1.setScale(0.0078125);
        this.gen2 = new PerlinOctaveGenerator(this.seed, 5);
        this.gen2.setScale(0.015625);
        this.gen3 = new PerlinOctaveGenerator(this.seed, 5);
        this.gen3.setScale(0.03125);
        this.gen4 = new SimplexOctaveGenerator(this.seed, 8);
        this.gen4.setScale(0.041666666666666664);
        this.gen5 = new SimplexOctaveGenerator(this.seed, 4);
        this.gen5.setScale(0.125);
        this.nrand = new NukkitRandom(s);
    }

    public Fjord() {
        this.initNoise(this.seed + 1L);
        this.populators = new ArrayList<>();
        PopulatorTree populatorTree = new PopulatorTree(10);
        populatorTree.setBaseAmount(1);
        populatorTree.setRandomAmount(2);
        this.populators.add(populatorTree);
        PopulatorGrass populatorGrass = new PopulatorGrass();
        populatorGrass.setBaseAmount(50);
        populatorGrass.setRandomAmount(50);
        this.populators.add(populatorGrass);
    }

    public int xyzToByte(int x, int y, int z) {
        return (x * 16 + z) * 128 + y;
    }

    private int getSectionID(int y) {
        return y >> 4;
    }

    private void setBlock(BaseFullChunk result, int x, int y, int z, int block) {
        result.setBlock(x %= 16, y %= 256, z %= 16, block);
    }

    private int getBlock(BaseFullChunk result, int x, int y, int z) {
        return result.getBlockId(x, y, z);
    }

    @Override
    public void generateTerrain(int x, int z, BaseFullChunk result) {
        this.initNoise(PlayerWorld.seed);
        int worldMaxHeight = 256;
        for (int bZ = 0; bZ < 16; ++bZ) {
            for (int bX = 0; bX < 16; ++bX) {
                this.DoLayer_Bedrock(result, this.random, bX, bZ);
                int realX = bX + x * 16;
                int realZ = bZ + z * 16;
                double frequency = 0.15;
                double amplitude = 0.4;
                int multitude = 42;
                int sealevel = 64;
                double noiseValue = 0.0;
                double plainsValue = (this.gen1.noise((double)realX * 0.33, (double)realZ * -0.33, 0.1, 0.3) + 1.0) * 0.5;
                double mountainValue0 = this.gen1.noise((double)realX, (double)realZ, frequency, amplitude);
                noiseValue += mountainValue0 * (double)multitude + (double)sealevel;
                noiseValue += this.gen2.noise((double)realX, (double)realZ, (double)0.8f, 0.3f) * 16.0;
                noiseValue += this.gen3.noise((double)realX, (double)realZ, (double)1.7f, 0.45f) * 8.0;
                if (plainsValue <= 0.5) {
                    float lerpFactor = 1.0f;
                    if (plainsValue >= 0.35) {
                        lerpFactor = Util.LerpFactor(0.5f, 0.35f, (float)plainsValue);
                    }
                    noiseValue = (double)((float)sealevel * Util.Lerp(0.0f, 1.0f, lerpFactor)) + noiseValue * (double)Util.Lerp(1.0f, 0.1f, lerpFactor);
                }
                for (int bY = 1; (double)bY < noiseValue && bY < worldMaxHeight; ++bY) {
                    this.setBlock(result, bX, bY, bZ, 1);
                }
                int taigaBorder = 95;
                int actualSealevel = (int)((float)sealevel * 0.75f);
                this.DoLayer_Shore(result, this.random, bX, bZ, actualSealevel);
                this.DoLayer_SeaBed(result, this.random, bX, bZ, actualSealevel);
                this.DoLayer_Sea(result, this.random, bX, bZ, actualSealevel);
                this.DoLayer_GrassAndDirt(result, this.random, bX, bZ);
                this.DoLayer_Snow(result, this.random, bX, bZ, taigaBorder);
                result.setBiomeId(bX, bZ, EnumBiome.BIRCH_FOREST_HILLS_M.id);
            }
        }
        this.populators.forEach(populator -> populator.populate((ChunkManager)PlayerWorld.instance.world, x, z, this.nrand, (FullChunk)result));
    }

    private int findHighestBlockY(BaseFullChunk result, int bX, int bZ) {
        int worldMaxHeight = 256;
        for (int bY = worldMaxHeight - 1; bY > 0; --bY) {
            int blockType = this.getBlock(result, bX, bY, bZ);
            if (blockType == 0) continue;
            return bY;
        }
        return 0;
    }

    private void DoLayer_Bedrock(BaseFullChunk result, Random random, int bX, int bZ) {
        this.setBlock(result, bX, 0, bZ, 7);
        for (int bY = 1; bY < 5; ++bY) {
            float randomFloat = random.nextFloat();
            if (!((double)randomFloat < 0.5)) continue;
            this.setBlock(result, bX, bY, bZ, 7);
        }
    }

    private void DoLayer_Shore(BaseFullChunk result, Random random, int bX, int bZ, int sealevel) {
        int worldMaxHeight = 256;
        int shoreMinLimitDry = 2;
        int shoreMaxLimitDry = 4;
        int shoreLimitMinWet = 5;
        int shoreLimitMaxWet = 9;
        int shoreMinDepth = 2;
        int shoreMaxDepth = 5;
        int highestBlockY = this.findHighestBlockY(result, bX, bZ);
        int shoreLimitWet = shoreLimitMinWet + (int)(random.nextFloat() * (float)(shoreLimitMaxWet - shoreLimitMinWet));
        int shoreLimitDry = shoreMinLimitDry + (int)(random.nextFloat() * (float)(shoreMaxLimitDry - shoreMinLimitDry));
        if (highestBlockY > sealevel - shoreLimitWet && highestBlockY < sealevel + shoreLimitDry) {
            int shoreDepth = shoreMinDepth + (int)(random.nextFloat() * (float)(shoreMaxDepth - shoreMinDepth));
            for (int bY = highestBlockY; bY > highestBlockY - shoreDepth && bY > 0; --bY) {
                this.setBlock(result, bX, bY, bZ, 13);
            }
        }
    }

    private void DoLayer_SeaBed(BaseFullChunk result, Random random, int bX, int bZ, int sealevel) {
        int highestBlockY = this.findHighestBlockY(result, bX, bZ);
        if (highestBlockY <= sealevel) {
            for (int i = highestBlockY; i > highestBlockY - 5 && i > 1; --i) {
                int currBlockMat = this.getBlock(result, bX, i, bZ);
                if (currBlockMat == 0 || currBlockMat == 13) continue;
                double nextGaussian = random.nextGaussian();
                int mat = nextGaussian > -0.2 && nextGaussian < 0.2 ? 82 : 3;
                this.setBlock(result, bX, i, bZ, mat);
            }
        }
    }

    private void DoLayer_Sea(BaseFullChunk result, Random random, int bX, int bZ, int sealevel) {
        int worldMaxHeight = 256;
        int highestBlockY = this.findHighestBlockY(result, bX, bZ);
        for (int bY = highestBlockY + 1; bY < sealevel && bY < worldMaxHeight; ++bY) {
            if (this.getBlock(result, bX, bY, bZ) != 0) continue;
            this.setBlock(result, bX, bY, bZ, 9);
        }
    }

    private void DoLayer_GrassAndDirt(BaseFullChunk result, Random random, int bX, int bZ) {
        int highestBlockY = this.findHighestBlockY(result, bX, bZ);
        if (highestBlockY < 1) {
            return;
        }
        int highestBlockType = this.getBlock(result, bX, highestBlockY - 1, bZ);
        if (highestBlockType != 1) {
            return;
        }
        int dirtMinDepth = 3;
        int dirtMaxDepth = 8;
        int dirtDepth = dirtMinDepth + (int)(random.nextFloat() * (float)(dirtMaxDepth - dirtMinDepth));
        for (int bY = highestBlockY; bY > highestBlockY - dirtDepth && bY > 0; --bY) {
            if (this.getBlock(result, bX, bY, bZ) == 0) continue;
            this.setBlock(result, bX, bY, bZ, 3);
        }
        this.setBlock(result, bX, highestBlockY, bZ, 2);
    }

    private void DoLayer_Snow(BaseFullChunk result, Random random, int bX, int bZ, int taigaBorder) {
        int highestBlockY = this.findHighestBlockY(result, bX, bZ);
        int snowMinTransitionHeight = -2;
        int snowMaxTransitionHeight = 2;
        int snowTransitionHeight = snowMinTransitionHeight + (int)(random.nextGaussian() * (double)(snowMaxTransitionHeight - snowMinTransitionHeight));
        if (highestBlockY < (taigaBorder += snowTransitionHeight)) {
            return;
        }
        int blockType = this.getBlock(result, bX, highestBlockY, bZ);
        if (blockType == 9 || blockType == 11 || blockType == 0) {
            return;
        }
        if (highestBlockY + 1 < 256) {
            this.setBlock(result, bX, highestBlockY + 1, bZ, 78);
        }
    }

    private static class Util {
        private Util() {
        }

        public static float Lerp(float _From, float _To, float _F) {
            return _From + (_To - _From) * _F;
        }

        public static double Lerp(double _From, double _To, double _F) {
            return _From + (_To - _From) * _F;
        }

        public static float LerpFactor(float _Min, float _Max, float _Value) {
            return (_Value - _Min) / (_Max - _Min);
        }
    }
}

