package shadow.playerworld.generators.noise;

import shadow.playerworld.noise.bukkit.PerlinNoiseGenerator;

public class NoiseGenerator {
    private PerlinNoiseGenerator generator;

    public NoiseGenerator(long seed) {
        this.generator = new PerlinNoiseGenerator(seed);
    }

    public double noise(int x, int y, int z, int octaves, double zoom) {
        if (octaves < 1) {
            octaves = 1;
        }
        if (octaves > 10) {
            octaves = 10;
        }
        double noise = this.generator.noise((double)x / zoom, (double)y / zoom, (double)z / zoom, octaves, 1.0, 1.0, true);
        if ((noise = (noise + 1.0) / 2.0) < 0.0) {
            return 0.0;
        }
        if (noise >= 1.0) {
            return 0.9999998807907104;
        }
        return noise;
    }

    public double noise(int x, int z, int octaves, double zoom) {
        double offset = 0.0;
        double div = 1.0;
        if (octaves < 1) {
            octaves = 1;
        }
        if (octaves > 10) {
            octaves = 10;
        }
        switch (octaves) {
            case 1: {
                offset = 0.5517346858978271;
                div = 1.089094877243042;
                break;
            }
            case 2: {
                offset = 0.6375992894172668;
                div = 1.292115569114685;
                break;
            }
            case 3: {
                offset = 0.6195292472839355;
                div = 1.232893466949463;
                break;
            }
            case 4: {
                offset = 0.5321022272109985;
                div = 1.043905258178711;
                break;
            }
            case 5: {
                offset = 0.40902603444590824;
                div = 0.9168765227000764;
                break;
            }
            case 6: {
                offset = 0.3374881789256107;
                div = 0.7683966215673046;
                break;
            }
            case 7: {
                offset = 0.2879264178925265;
                div = 0.6592998496889363;
                break;
            }
            case 8: {
                offset = 0.2509012622339434;
                div = 0.5769489851948282;
                break;
            }
            case 9: {
                offset = 0.22211701434645162;
                div = 0.5128490426570946;
                break;
            }
            case 10: {
                offset = 0.19909079554807718;
                div = 0.46156463362380296;
            }
        }
        double noise = this.generator.noise((double)x / zoom, (double)z / zoom, octaves, 1.0, 1.0, true);
        noise = offset + noise;
        noise /= div;
        if (noise < 0.0) {
            return 0.0;
        }
        if (noise >= 1.0) {
            return 0.9999998807907104;
        }
        return noise;
    }
}

