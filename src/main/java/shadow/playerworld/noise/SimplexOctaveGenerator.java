package shadow.playerworld.noise;

import cn.nukkit.math.NukkitRandom;
import shadow.playerworld.noise.bukkit.NoiseGenerator;

public class SimplexOctaveGenerator extends PerlinOctaveGenerator {
    public SimplexOctaveGenerator(NukkitRandom rand, int octaves, int sizeX, int sizeZ) {
        this(rand, octaves, sizeX, 1, sizeZ);
    }

    public SimplexOctaveGenerator(NukkitRandom rand, int octaves, int sizeX, int sizeY, int sizeZ) {
        super(SimplexOctaveGenerator.createOctaves(rand, octaves), rand, sizeX, sizeY, sizeZ);
    }

    public SimplexOctaveGenerator(NukkitRandom rand, int octaves) {
        this(rand, octaves, 0, 0, 0);
    }

    protected static NoiseGenerator[] createOctaves(NukkitRandom rand, int octaves) {
        NoiseGenerator[] result = new NoiseGenerator[octaves];
        for (int i = 0; i < octaves; ++i) {
            result[i] = new SimplexNoise(rand);
        }
        return result;
    }

    @Override
    public double[] getFractalBrownianMotion(double x, double y, double z, double lacunarity, double persistence) {
        for (int i = 0; i < this.noise.length; ++i) {
            this.noise[i] = 0.0;
        }
        double freq = 1.0;
        double amp = 1.0;
        for (NoiseGenerator octave : this.octaves) {
            this.noise = ((PerlinNoise)octave).getNoise(this.noise, x, y, z, this.sizeX, this.sizeY, this.sizeZ, this.xScale * freq, this.yScale * freq, this.zScale * freq, 0.55 / amp);
            freq *= lacunarity;
            amp *= persistence;
        }
        return this.noise;
    }
}

