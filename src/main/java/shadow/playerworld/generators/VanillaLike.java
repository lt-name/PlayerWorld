package shadow.playerworld.generators;

import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorGrass;
import cn.nukkit.level.generator.populator.impl.PopulatorTree;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.generators.noise.NoiseGeneratorOctaves3D;
import shadow.playerworld.generators.vanilla.WorldChunkManagerOld;

public class VanillaLike implements BaseGenerator {
    private Random rand;
    private NukkitRandom nrand;
    private NoiseGeneratorOctaves3D noiseGen1;
    private NoiseGeneratorOctaves3D noiseGen2;
    private NoiseGeneratorOctaves3D noiseGen3;
    private NoiseGeneratorOctaves3D noiseGen4;
    private NoiseGeneratorOctaves3D noiseGen5;
    private NoiseGeneratorOctaves3D noiseGen6;
    private NoiseGeneratorOctaves3D noiseGen7;
    private double[] noise;
    private double[] sandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] stoneNoise = new double[256];
    private double[] noise3;
    private double[] noise1;
    private double[] noise2;
    private double[] noise6;
    private double[] noise7;
    private List<Populator> populators = new ArrayList<>();
    public WorldChunkManagerOld wcm = null;

    public VanillaLike() {
        PopulatorTree populatorTree = new PopulatorTree(0);
        populatorTree.setBaseAmount(1);
        populatorTree.setRandomAmount(2);
        this.populators.add(populatorTree);

        PopulatorGrass populatorGrass = new PopulatorGrass();
        populatorGrass.setBaseAmount(50);
        populatorGrass.setRandomAmount(50);
        this.populators.add(populatorGrass);
    }

    public void initC(long seed) {
        this.rand = new Random(seed);
        this.nrand = new NukkitRandom(seed);
        this.noiseGen1 = new NoiseGeneratorOctaves3D(this.rand, 16, true);
        this.noiseGen2 = new NoiseGeneratorOctaves3D(this.rand, 16, true);
        this.noiseGen3 = new NoiseGeneratorOctaves3D(this.rand, 8, true);
        this.noiseGen4 = new NoiseGeneratorOctaves3D(this.rand, 4, true);
        this.noiseGen5 = new NoiseGeneratorOctaves3D(this.rand, 4, true);
        this.noiseGen6 = new NoiseGeneratorOctaves3D(this.rand, 10, true);
        this.noiseGen7 = new NoiseGeneratorOctaves3D(this.rand, 16, true);
    }

    public void initRand(int chunkX, int chunkZ) {
        this.rand.setSeed((long)chunkX * 341873128712L + (long)chunkZ * 132897987541L);
    }

    @Override
    public void generateTerrain(int x, int z, BaseFullChunk terrain) {
        if (this.wcm == null) {
            long seed = PlayerWorld.seed;
            this.initC(seed);
            this.wcm = new WorldChunkManagerOld(seed);
        }
        this.initRand(x, z);
        this.wcm.getBiomeBlock(null, x * 16, z * 16, 16, 16);
        this.generateTerrainOld(x, z, terrain);
        this.replaceBlocksForBiome(x, z, terrain);
        for (int bx = 0; bx < 16; ++bx) {
            for (int bz = 0; bz < 16; ++bz) {
                terrain.setBiomeId(bx, bz, EnumBiome.FOREST.id);
            }
        }
        this.populators.forEach(populator -> populator.populate(PlayerWorld.instance.world, x, z, this.nrand, terrain));
    }

    public void generateTerrainOld(int x, int z, BaseFullChunk terrain) {
        double[] temperatures = this.wcm.temperatures;
        int byte0 = 4;
        int oceanHeight = 64;
        int k = byte0 + 1;
        int b2 = 17;
        int l = byte0 + 1;
        this.noise = this.initNoiseField(this.noise, x * byte0, 0, z * byte0, k, b2, l);
        for (int xPiece = 0; xPiece < byte0; ++xPiece) {
            for (int zPiece = 0; zPiece < byte0; ++zPiece) {
                for (int yPiece = 0; yPiece < 16; ++yPiece) {
                    double d = 0.125;
                    double d1 = this.noise[((xPiece) * l + (zPiece)) * b2 + (yPiece)];
                    double d2 = this.noise[((xPiece) * l + (zPiece + 1)) * b2 + (yPiece)];
                    double d3 = this.noise[((xPiece + 1) * l + (zPiece)) * b2 + (yPiece)];
                    double d4 = this.noise[((xPiece + 1) * l + (zPiece + 1)) * b2 + (yPiece)];
                    double d5 = (this.noise[((xPiece) * l + (zPiece)) * b2 + (yPiece + 1)] - d1) * d;
                    double d6 = (this.noise[((xPiece) * l + (zPiece + 1)) * b2 + (yPiece + 1)] - d2) * d;
                    double d7 = (this.noise[((xPiece + 1) * l + (zPiece)) * b2 + (yPiece + 1)] - d3) * d;
                    double d8 = (this.noise[((xPiece + 1) * l + (zPiece + 1)) * b2 + (yPiece + 1)] - d4) * d;
                    for (int l1 = 0; l1 < 8; ++l1) {
                        double d9 = 0.25;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for (int i2 = 0; i2 < 4; ++i2) {
                            int xLoc = i2 + xPiece * 4;
                            int yLoc = yPiece * 8 + l1;
                            int zLoc = zPiece * 4;
                            double d14 = 0.25;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for (int k2 = 0; k2 < 4; ++k2) {
                                double d17 = temperatures[(xPiece * 4 + i2) * 16 + (zPiece * 4 + k2)];
                                int block = 0;
                                if (yPiece * 8 + l1 < oceanHeight) {
                                    block = d17 < 0.5 && yPiece * 8 + l1 >= oceanHeight - 1 ? 79 : 9;
                                }
                                if (d15 > 0.0) {
                                    block = 1;
                                }
                                terrain.setBlock(xLoc, yLoc, zLoc, block);
                                ++zLoc;
                                d15 += d16;
                            }
                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void replaceBlocksForBiome(int xPos, int zPos, BaseFullChunk terrain) {
        int oceanHeight = 64;
        double d = 0.03125;
        this.sandNoise = this.noiseGen4.generateNoiseArray(this.sandNoise, xPos * 16, zPos * 16, 0.0, 16, 16, 1, d, d, 1.0);
        this.gravelNoise = this.noiseGen4.generateNoiseArray(this.gravelNoise, xPos * 16, 109.0134, zPos * 16, 16, 1, 16, d, 1.0, d);
        this.stoneNoise = this.noiseGen5.generateNoiseArray(this.stoneNoise, xPos * 16, zPos * 16, 0.0, 16, 16, 1, d * 2.0, d * 2.0, d * 2.0);
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                boolean sand = this.sandNoise[x + z * 16] + this.rand.nextDouble() * 0.2 > 0.0;
                boolean gravel = this.gravelNoise[x + z * 16] + this.rand.nextDouble() * 0.2 > 3.0;
                int depth = (int)(this.stoneNoise[x + z * 16] / 3.0 + 3.0 + this.rand.nextDouble() * 0.25);
                int prevDepth = -1;
                int topBlock = 2;
                int fillerBlock = 3;
                for (int y = 127; y >= 0; --y) {
                    if (y <= this.rand.nextInt(5)) {
                        terrain.setBlock(z, y, x, 7);
                        continue;
                    }
                    int block = terrain.getBlockId(z, y, x);
                    if (block == 0) {
                        prevDepth = -1;
                        continue;
                    }
                    if (block != 1) continue;
                    if (prevDepth == -1) {
                        if (depth <= 0) {
                            topBlock = 0;
                            fillerBlock = 1;
                        } else if (y >= oceanHeight - 4 && y <= oceanHeight + 1) {
                            topBlock = 2;
                            fillerBlock = 3;
                            if (gravel) {
                                topBlock = 0;
                                fillerBlock = 13;
                            }
                            if (sand) {
                                topBlock = 12;
                                fillerBlock = 12;
                            }
                        }
                        if (y < oceanHeight && topBlock == 0) {
                            topBlock = 9;
                        }
                        prevDepth = depth;
                        if (y >= oceanHeight - 1) {
                            terrain.setBlock(z, y, x, topBlock);
                            continue;
                        }
                        terrain.setBlock(z, y, x, fillerBlock);
                        continue;
                    }
                    if (prevDepth <= 0) continue;
                    terrain.setBlock(z, y, x, fillerBlock);
                    if (--prevDepth != 0 || fillerBlock != 12) continue;
                    prevDepth = this.rand.nextInt(4);
                    fillerBlock = 24;
                }
            }
        }
    }

    private double[] initNoiseField(double[] array, int xPos, int yPos, int zPos, int xSize, int ySize, int zSize) {
        if (array == null) {
            array = new double[xSize * ySize * zSize];
        }
        double d0 = 684.412;
        double d1 = 684.412;
        double[] temp = this.wcm.temperatures;
        double[] rain = this.wcm.rain;
        this.noise6 = this.noiseGen6.generateNoiseArray(this.noise6, xPos, zPos, xSize, zSize, 1.121, 1.121, 0.5);
        this.noise7 = this.noiseGen7.generateNoiseArray(this.noise7, xPos, zPos, xSize, zSize, 200.0, 200.0, 0.5);
        this.noise3 = this.noiseGen3.generateNoiseArray(this.noise3, xPos, yPos, zPos, xSize, ySize, zSize, d0 / 80.0, d1 / 160.0, d0 / 80.0);
        this.noise1 = this.noiseGen1.generateNoiseArray(this.noise1, xPos, yPos, zPos, xSize, ySize, zSize, d0, d1, d0);
        this.noise2 = this.noiseGen2.generateNoiseArray(this.noise2, xPos, yPos, zPos, xSize, ySize, zSize, d0, d1, d0);
        int k1 = 0;
        int l1 = 0;
        int i2 = 16 / xSize;
        for (int x = 0; x < xSize; ++x) {
            int k2 = x * i2 + i2 / 2;
            for (int z = 0; z < zSize; ++z) {
                double d6;
                int i3 = z * i2 + i2 / 2;
                double d2 = temp[k2 * 16 + i3];
                double d3 = rain[k2 * 16 + i3] * d2;
                double d4 = 1.0 - d3;
                d4 *= d4;
                d4 *= d4;
                d4 = 1.0 - d4;
                double d5 = (this.noise6[l1] + 256.0) / 512.0;
                if ((d5 *= d4) > 1.0) {
                    d5 = 1.0;
                }
                if ((d6 = this.noise7[l1] / 8000.0) < 0.0) {
                    d6 = -d6 * 0.3;
                }
                if ((d6 = d6 * 3.0 - 2.0) < 0.0) {
                    if ((d6 /= 2.0) < -1.0) {
                        d6 = -1.0;
                    }
                    d6 /= 1.4;
                    d6 /= 2.0;
                    d5 = 0.0;
                } else {
                    if (d6 > 1.0) {
                        d6 = 1.0;
                    }
                    d6 /= 8.0;
                }
                if (d5 < 0.0) {
                    d5 = 0.0;
                }
                d5 += 0.5;
                d6 = d6 * (double)ySize / 16.0;
                double d7 = (double)ySize / 2.0 + d6 * 4.0;
                ++l1;
                for (int y = 0; y < ySize; ++y) {
                    double d8;
                    double d9 = ((double)y - d7) * 12.0 / d5;
                    if (d9 < 0.0) {
                        d9 *= 4.0;
                    }
                    double d10 = this.noise1[k1] / 512.0;
                    double d11 = this.noise2[k1] / 512.0;
                    double d12 = (this.noise3[k1] / 10.0 + 1.0) / 2.0;
                    d8 = d12 < 0.0 ? d10 : (d12 > 1.0 ? d11 : d10 + (d11 - d10) * d12);
                    d8 -= d9;
                    if (y > ySize - 4) {
                        double d13 = (float)(y - (ySize - 4)) / 3.0f;
                        d8 = d8 * (1.0 - d13) + -10.0 * d13;
                    }
                    array[k1] = d8;
                    ++k1;
                }
            }
        }
        return array;
    }

    @Override
    public String getName() {
        return "原版风格";
    }
}

