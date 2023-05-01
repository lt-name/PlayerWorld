package shadow.playerworld.generators.vanilla;

import java.util.Random;
import shadow.playerworld.generators.noise.NoiseGeneratorOctaves2D;

public class WorldChunkManagerOld {
    public double[] temperatures;
    public double[] rain;
    public double[] c;
    public BetaBiome[] dx;
    private final NoiseGeneratorOctaves2D noise1;
    private final NoiseGeneratorOctaves2D noise2;
    private final NoiseGeneratorOctaves2D noise3;

    public WorldChunkManagerOld(long seed) {
        this.noise1 = new NoiseGeneratorOctaves2D(new Random(seed * 9871L), 4);
        this.noise2 = new NoiseGeneratorOctaves2D(new Random(seed * 39811L), 4);
        this.noise3 = new NoiseGeneratorOctaves2D(new Random(seed * 543321L), 2);
    }

    public BetaBiome getBiome(int i, int j) {
        return this.getBiomeData(i, j, 1, 1)[0];
    }

    public double[] getTemperatures(double[] ad, int i, int j, int k, int l) {
        if (ad == null || ad.length < k * l) {
            ad = new double[k * l];
        }
        ad = this.noise1.generateNoiseArray(ad, i, j, k, l, 0.025, 0.025, 0.25);
        this.c = this.noise3.generateNoiseArray(this.c, i, j, k, l, 0.25, 0.25, 0.5882352941176471);
        int i1 = 0;
        for (int j1 = 0; j1 < k; ++j1) {
            for (int k1 = 0; k1 < l; ++k1) {
                double d = this.c[i1] * 1.1 + 0.5;
                double d1 = 0.01;
                double d2 = 1.0 - d1;
                double d3 = (ad[i1] * 0.15 + 0.7) * d2 + d * d1;
                if ((d3 = 1.0 - (1.0 - d3) * (1.0 - d3)) < 0.0) {
                    d3 = 1.0E-4;
                }
                if (d3 > 1.0) {
                    d3 = 1.0;
                }
                ad[i1] = d3;
                ++i1;
            }
        }
        this.temperatures = ad;
        return ad;
    }

    public BetaBiome[] getBiomeBlock(BetaBiome[] biomes, int x, int z, int xSize, int zSize) {
        if (biomes == null || biomes.length < xSize * zSize) {
            biomes = new BetaBiome[xSize * zSize];
        }
        this.temperatures = this.noise1.generateNoiseArray(this.temperatures, x, z, xSize, xSize, 0.025f, 0.025f, 0.25);
        this.rain = this.noise2.generateNoiseArray(this.rain, x, z, xSize, xSize, 0.05f, 0.05f, 0.3333333333333333);
        this.c = this.noise3.generateNoiseArray(this.c, x, z, xSize, xSize, 0.25, 0.25, 0.5882352941176471);
        int index = 0;
        for (int blockX = 0; blockX < xSize; ++blockX) {
            for (int blockZ = 0; blockZ < zSize; ++blockZ) {
                double d = this.c[index] * 1.1 + 0.5;
                double d1 = 0.01;
                double d2 = 1.0 - d1;
                double temp = (this.temperatures[index] * 0.15 + 0.7) * d2 + d * d1;
                d1 = 0.002;
                d2 = 1.0 - d1;
                double humid = (this.rain[index] * 0.15 + 0.5) * d2 + d * d1;
                if ((temp = 1.0 - (1.0 - temp) * (1.0 - temp)) < 0.0) {
                    temp = 0.0;
                }
                if (humid < 0.0) {
                    humid = 0.0;
                }
                if (temp > 1.0) {
                    temp = 1.0;
                }
                if (humid > 1.0) {
                    humid = 1.0;
                }
                this.temperatures[index] = temp;
                this.rain[index] = humid;
                biomes[index++] = BiomeOld.getBiomeFromLookup(temp, humid);
            }
        }
        return biomes;
    }

    public BetaBiome[] getBiomes(BetaBiome[] biomes, int x, int z, int xSize, int zSize) {
        if (biomes == null || biomes.length < xSize * zSize) {
            biomes = new BetaBiome[xSize * zSize];
        }
        this.temperatures = this.noise1.generateNoiseArray(this.temperatures, x, z, xSize, xSize, 0.025, 0.025, 0.25);
        this.rain = this.noise2.generateNoiseArray(this.rain, x, z, xSize, xSize, 0.05, 0.05, 0.3333333333333333);
        this.c = this.noise3.generateNoiseArray(this.c, x, z, xSize, xSize, 0.25, 0.25, 0.5882352941176471);
        int index = 0;
        for (int blockX = 0; blockX < xSize; ++blockX) {
            for (int blockZ = 0; blockZ < zSize; ++blockZ) {
                double d = this.c[index] * 1.1 + 0.5;
                double d1 = 0.01;
                double d2 = 1.0 - d1;
                double temp = (this.temperatures[index] * 0.15 + 0.7) * d2 + d * d1;
                d1 = 0.002;
                d2 = 1.0 - d1;
                double humid = (this.rain[index] * 0.15 + 0.5) * d2 + d * d1;
                if ((temp = 1.0 - (1.0 - temp) * (1.0 - temp)) < 0.0) {
                    temp = 0.0;
                }
                if (humid < 0.0) {
                    humid = 0.0;
                }
                if (temp > 1.0) {
                    temp = 1.0;
                }
                if (humid > 1.0) {
                    humid = 1.0;
                }
                this.temperatures[index] = temp;
                this.rain[index] = humid;
                biomes[index++] = BiomeOld.getBiomeFromLookup(temp, humid);
            }
        }
        return biomes;
    }

    private BetaBiome[] getBiomeData(int i, int j, int k, int l) {
        this.dx = this.getBiomes(this.dx, i, j, k, l);
        return this.dx;
    }
}

