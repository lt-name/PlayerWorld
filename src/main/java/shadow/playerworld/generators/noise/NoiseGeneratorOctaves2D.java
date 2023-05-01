package shadow.playerworld.generators.noise;

import java.util.Random;

public class NoiseGeneratorOctaves2D {
    private final NoiseGenerator2D[] noiseGenerators;
    private final int octaves;

    public NoiseGeneratorOctaves2D(Random rand, int i) {
        this.octaves = i;
        this.noiseGenerators = new NoiseGenerator2D[i];
        for (int j = 0; j < i; ++j) {
            this.noiseGenerators[j] = new NoiseGenerator2D(rand);
        }
    }

    public double[] generateNoiseArray(double[] array, double x, double z, int xSize, int zSize, double gridX, double gridZ, double fq) {
        return this.generateNoiseArray(array, x, z, xSize, zSize, gridX, gridZ, fq, 0.5);
    }

    public double[] generateNoiseArray(double[] array, double xPos, double zPos, int xSize, int zSize, double gridX, double gridZ, double fq, double persistance) {
        gridX /= 1.5;
        gridZ /= 1.5;
        if (array == null || array.length < xSize * zSize) {
            array = new double[xSize * zSize];
        } else {
            for (int k = 0; k < array.length; ++k) {
                array[k] = 0.0;
            }
        }
        double amplitude = 1.0;
        double frequency = 1.0;
        for (int l = 0; l < this.octaves; ++l) {
            this.noiseGenerators[l].generateNoiseArray(array, xPos, zPos, xSize, zSize, gridX * frequency, gridZ * frequency, 0.55 / amplitude);
            frequency *= fq;
            amplitude *= persistance;
        }
        return array;
    }
}

