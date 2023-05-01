package shadow.playerworld.generators;

import cn.nukkit.level.format.generic.BaseFullChunk;
import java.util.Random;

import shadow.playerworld.generators.util.EnumGummy;
import shadow.playerworld.generators.util.MathHelper;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.generators.noise.NoiseGeneratorOctaves3D;
import shadow.playerworld.generators.noise.NoiseGeneratorPerlin;

public class CandyWorld implements BaseGenerator {
    public static final int ROCK = 172;
    public static final int OCEAN_BLOCK = 9;
    private Random rand;
    private NoiseGeneratorOctaves3D minLimitPerlinNoise;
    private NoiseGeneratorOctaves3D maxLimitPerlinNoise;
    private NoiseGeneratorOctaves3D mainPerlinNoise;
    private NoiseGeneratorPerlin surfaceNoise;
    public NoiseGeneratorOctaves3D scaleNoise;
    public NoiseGeneratorOctaves3D depthNoise;
    public NoiseGeneratorOctaves3D forestNoise;
    private double[] heightMap;
    private float[] biomeWeights;
    private double[] depthBuffer = new double[256];
    double[] mainNoiseRegion;
    double[] minLimitRegion;
    double[] maxLimitRegion;
    double[] depthRegion;

    public CandyWorld() {
        long seed = PlayerWorld.seed;
        this.rand = new Random(seed);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves3D(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves3D(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves3D(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves3D(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves3D(this.rand, 16);
        this.forestNoise = new NoiseGeneratorOctaves3D(this.rand, 8);
        this.heightMap = new double[825];
        this.biomeWeights = new float[25];
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                float f;
                this.biomeWeights[i + 2 + (j + 2) * 5] = f = 10.0f / MathHelper.sqrt((float)(i * i + j * j) + 0.2f);
            }
        }
    }

    public void setBlocksInChunk(int x, int z, BaseFullChunk primer) {
        generateHeightmap(x * 4, z * 4);
        for (int i = 0; i < 4; i++) {
            int j = i * 5;
            int k = (i + 1) * 5;
            for (int l = 0; l < 4; l++) {
                int i1 = (j + l) * 33;
                int j1 = (j + l + 1) * 33;
                int k1 = (k + l) * 33;
                int l1 = (k + l + 1) * 33;
                for (int i2 = 0; i2 < 32; i2++) {
                    double d1 = this.heightMap[i1 + i2];
                    double d2 = this.heightMap[j1 + i2];
                    double d3 = this.heightMap[k1 + i2];
                    double d4 = this.heightMap[l1 + i2];
                    double d5 = (this.heightMap[i1 + i2 + 1] - d1) * 0.125D;
                    double d6 = (this.heightMap[j1 + i2 + 1] - d2) * 0.125D;
                    double d7 = (this.heightMap[k1 + i2 + 1] - d3) * 0.125D;
                    double d8 = (this.heightMap[l1 + i2 + 1] - d4) * 0.125D;
                    for (int j2 = 0; j2 < 8; j2++) {
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.25D;
                        double d13 = (d4 - d2) * 0.25D;
                        for (int k2 = 0; k2 < 4; k2++) {
                            double d16 = (d11 - d10) * 0.25D;
                            double lvt_45_1_ = d10 - d16;
                            for (int l2 = 0; l2 < 4; l2++) {
                                if ((lvt_45_1_ += d16) > 0.0D) {
                                    primer.setBlock(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, 172);
                                } else if (i2 * 8 + j2 < 64) {
                                    primer.setBlock(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, 9);
                                }
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

    public void replaceBiomeBlocks(int x, int z, BaseFullChunk primer) {
        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, x * 16, z * 16, 16, 16, 0.0625, 0.0625, 1.0);
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                this.generateCustomTerrain(this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
            }
        }
    }

    public void generateCustomTerrain(Random rand, BaseFullChunk chunkPrimerIn, int x, int z, double noiseVal) {
        int seaLevel = 64;
        int j = -1;
        int fillerToPlace = (int)(noiseVal / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        EnumGummy gummy_color = EnumGummy.getGummyForGeneration(noiseVal);
        int[] topState = { 172, gummy_color.getMaterial() };
        int[] fillerState = { 172, gummy_color.getMaterial() };
        int chunkZ = x & 0xF;
        int chunkX = z & 0xF;
        for (int currentY = 255; currentY >= 0; currentY--) {
            if (currentY <= rand.nextInt(5)) {
                chunkPrimerIn.setBlock(chunkX, currentY, chunkZ, 7);
            } else {
                int iblockstate2 = chunkPrimerIn.getBlockId(chunkX, currentY, chunkZ);
                if (iblockstate2 == 0) {
                    j = -1;
                } else if (iblockstate2 == 1 || iblockstate2 == 172) {
                    if (j == -1) {
                        if (currentY >= seaLevel - 4 && currentY <= seaLevel + 1) {
                            topState = new int[] { 172, gummy_color.getMaterial() };
                            fillerState = new int[] { 172, gummy_color.getMaterial() };
                        }
                        if (currentY < seaLevel && (topState == null || topState[0] == 0))
                            topState = new int[] { 9, 0 };
                        j = fillerToPlace + rand.nextInt(8) + 4;
                        if (currentY >= seaLevel - 1) {
                            chunkPrimerIn.setBlock(chunkX, currentY, chunkZ, topState[0]);
                            if (topState[1] != 0)
                                chunkPrimerIn.setBlockData(chunkX, currentY, chunkZ, topState[1]);
                        } else if (currentY < seaLevel - 7 - fillerToPlace) {
                            chunkPrimerIn.setBlock(chunkX, currentY, chunkZ, 13);
                        } else {
                            chunkPrimerIn.setBlock(chunkX, currentY, chunkZ, fillerState[0]);
                            if (fillerState[1] != 0)
                                chunkPrimerIn.setBlockData(chunkX, currentY, chunkZ, fillerState[1]);
                        }
                    } else if (j > 0) {
                        j--;
                        chunkPrimerIn.setBlock(chunkX, currentY, chunkZ, fillerState[0]);
                        if (fillerState[1] != 0)
                            chunkPrimerIn.setBlockData(chunkX, currentY, chunkZ, fillerState[1]);
                    }
                }
            }
        }
    }

    @Override
    public void generateTerrain(int x, int z, BaseFullChunk chunkprimer) {
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        this.setBlocksInChunk(x, z, chunkprimer);
        this.replaceBiomeBlocks(x, z, chunkprimer);
    }

    private void generateHeightmap(int x, int z) {
        this.depthRegion = this.depthNoise.generateNoiseArray(this.depthRegion, x, z, 5, 5, 200.0, 200.0, 0.5);
        float f = 684.412f;
        float f1 = 684.412f;
        this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseArray(this.mainNoiseRegion, x, 0.0, z, 5, 33, 5, (double)f / 80.0, (double)f1 / 160.0, (double)f / 80.0);
        this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseArray(this.minLimitRegion, x, 0.0, z, 5, 33, 5, f, f1, f);
        this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseArray(this.maxLimitRegion, x, 0.0, z, 5, 33, 5, f, f1, f);
        int i = 0;
        int j = 0;
        for (int k = 0; k < 5; ++k) {
            for (int l = 0; l < 5; ++l) {
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                for (int j1 = -2; j1 <= 2; ++j1) {
                    for (int k1 = -2; k1 <= 2; ++k1) {
                        float f5 = 0.125f;
                        float f6 = 0.01f;
                        float f7 = this.biomeWeights[j1 + 2 + (k1 + 2) * 5] / (f5 + 2.0f);
                        f2 += f6 * f7;
                        f3 += f5 * f7;
                        f4 += f7;
                    }
                }
                f2 /= f4;
                f3 /= f4;
                f2 = f2 * 0.9f + 0.1f;
                f3 = (f3 * 4.0f - 1.0f) / 8.0f;
                double d7 = this.depthRegion[j] / 8000.0;
                if (d7 < 0.0) {
                    d7 = -d7 * 0.3;
                }
                if ((d7 = d7 * 3.0 - 2.0) < 0.0) {
                    if ((d7 /= 2.0) < -1.0) {
                        d7 = -1.0;
                    }
                    d7 /= 1.4;
                    d7 /= 2.0;
                } else {
                    if (d7 > 1.0) {
                        d7 = 1.0;
                    }
                    d7 /= 8.0;
                }
                ++j;
                double d8 = f3;
                double d9 = f2;
                d8 += d7 * 0.2;
                d8 = d8 * 8.5 / 8.0;
                double d0 = 8.5 + d8 * 4.0;
                for (int l1 = 0; l1 < 33; ++l1) {
                    double d1 = ((double)l1 - d0) * 12.0 * 128.0 / 256.0 / d9;
                    if (d1 < 0.0) {
                        d1 *= 4.0;
                    }
                    double d2 = this.minLimitRegion[i] / 512.0;
                    double d3 = this.maxLimitRegion[i] / 512.0;
                    double d4 = (this.mainNoiseRegion[i] / 10.0 + 1.0) / 2.0;
                    double d5 = MathHelper.clamp(d2, d3, d4) - d1;
                    if (l1 > 29) {
                        double d6 = (float)(l1 - 29) / 3.0f;
                        d5 = d5 * (1.0 - d6) + -10.0 * d6;
                    }
                    this.heightMap[i] = d5;
                    ++i;
                }
            }
        }
    }

    @Override
    public String getName() {
        return "\u7cd6\u679c\u4e16\u754c";
    }
}

