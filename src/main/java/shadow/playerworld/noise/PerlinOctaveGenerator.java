package shadow.playerworld.noise;

import cn.nukkit.math.NukkitRandom;
import shadow.playerworld.noise.bukkit.OctaveGenerator;
import shadow.playerworld.noise.bukkit.NoiseGenerator;

public class PerlinOctaveGenerator extends OctaveGenerator {
    protected final int sizeX;
    protected final int sizeY;
    protected final int sizeZ;
    protected double[] noise;

    public PerlinOctaveGenerator(NukkitRandom rand, int octaves, int sizeX, int sizeZ) {
        this(rand, octaves, sizeX, 1, sizeZ);
    }

    public PerlinOctaveGenerator(NukkitRandom rand, int octaves, int sizeX, int sizeY, int sizeZ) {
        this(PerlinOctaveGenerator.createOctaves(rand, octaves), rand, sizeX, sizeY, sizeZ);
    }

    public PerlinOctaveGenerator(NoiseGenerator[] octaves, NukkitRandom rand, int sizeX, int sizeY, int sizeZ) {
        super(octaves);
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.noise = new double[sizeX * sizeY * sizeZ];
    }

    protected static NoiseGenerator[] createOctaves(NukkitRandom rand, int octaves) {
        NoiseGenerator[] result = new NoiseGenerator[octaves];
        for (int i = 0; i < octaves; ++i) {
            result[i] = new PerlinNoise(rand);
        }
        return result;
    }

    protected static long floor(double x) {
        return x >= 0.0 ? (long)x : (long)x - 1L;
    }

    public double[] getFractalBrownianMotion(double x, double z, double lacunarity, double persistence) {
        return this.getFractalBrownianMotion(x, 0.0, z, lacunarity, persistence);
    }

    public double[] getFractalBrownianMotion(double x, double y, double z, double lacunarity, double persistence) {
        for (int i = 0; i < this.noise.length; ++i) {
            this.noise[i] = 0.0;
        }
        double freq = 1.0;
        double amp = 1.0;
        x *= this.xScale;
        y *= this.yScale;
        z *= this.zScale;
        for (NoiseGenerator octave : this.octaves) {
            double dx = x * freq;
            double dz = z * freq;
            long lx = PerlinOctaveGenerator.floor(dx);
            long lz = PerlinOctaveGenerator.floor(dz);
            dx -= (double)lx;
            dz -= (double)lz;
            double dy = y * freq;
            this.noise = ((PerlinNoise)octave).getNoise(this.noise, dx += (double)(lx %= 0x1000000L), dy, dz += (double)(lz %= 0x1000000L), this.sizeX, this.sizeY, this.sizeZ, this.xScale * freq, this.yScale * freq, this.zScale * freq, amp);
            freq *= lacunarity;
            amp *= persistence;
        }
        return this.noise;
    }

    public int getSizeX() {
        return this.sizeX;
    }

    public int getSizeY() {
        return this.sizeY;
    }

    public int getSizeZ() {
        return this.sizeZ;
    }
}

