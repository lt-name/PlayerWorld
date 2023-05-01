package shadow.playerworld.generators;

import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.generic.BaseFullChunk;
import java.util.Random;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.generators.noise.NoiseGenerator;

public class Hoth implements BaseGenerator {
    private NoiseGenerator noiseGenerator = null;

    @Override
    public String getName() {
        return "\u51b0\u5c01\u4e16\u754c";
    }

    private static int getBedrockMaterial(Random localRand, int limit) {
        return Hoth.getBedrockMaterial(localRand, limit, 1);
    }

    private static int getBedrockMaterial(Random localRand, int limit, int material) {
        int ran = localRand.nextInt() & 0xFF;
        if (ran > limit) {
            return material;
        }
        return 7;
    }

    @Override
    public void generateTerrain(int chunkx, int chunkz, BaseFullChunk chunk) {
        if (this.noiseGenerator == null) {
            this.noiseGenerator = new NoiseGenerator(PlayerWorld.seed);
        }
        boolean smooth_snow = true;
        int surfaceOffset = 0;
        Random localRand = new Random(chunkx * chunkz);
        byte[][] snowcover = new byte[16][16];
        int vsegs = 16;
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                double water;
                int watery;
                double dowater;
                double lava;
                int lavay;
                double dolava;
                int i;
                int rx = chunkx * 16 + x;
                int rz = chunkz * 16 + z;
                float factor = 1.0f;
                int y = 0;
                HothUtils.setPos(chunk, x, y, z, 7);
                HothUtils.setPos(chunk, x, y + 1, z, Hoth.getBedrockMaterial(localRand, 230));
                HothUtils.setPos(chunk, x, y + 2, z, Hoth.getBedrockMaterial(localRand, 179));
                HothUtils.setPos(chunk, x, y + 3, z, Hoth.getBedrockMaterial(localRand, 128));
                HothUtils.setPos(chunk, x, y + 4, z, Hoth.getBedrockMaterial(localRand, 76));
                HothUtils.setPos(chunk, x, y + 5, z, Hoth.getBedrockMaterial(localRand, 51));
                for (y = 6; y < 27 + surfaceOffset; ++y) {
                    HothUtils.setPos(chunk, x, y, z, 1);
                }
                double stone = this.noiseGenerator.noise(rx, rz, 8, 16.0) * 3.0;
                for (int i2 = 0; i2 < (int)stone; ++i2) {
                    HothUtils.setPos(chunk, x, y, z, 1);
                    ++y;
                }
                double dirt = this.noiseGenerator.noise(rx, rz, 8, 11.0) * 5.0;
                for (int i3 = 2; i3 < (int)dirt; ++i3) {
                    HothUtils.setPos(chunk, x, y, z, 3);
                    ++y;
                }
                double gravel = this.noiseGenerator.noise(rx, rz, 7, 16.0) * 5.0;
                for (int i4 = 2; i4 < (int)gravel; ++i4) {
                    HothUtils.setPos(chunk, x, y, z, 13);
                    ++y;
                }
                double sandstone = this.noiseGenerator.noise(rx, rz, 8, 23.0) * 4.0;
                for (int i5 = 1; i5 < (int)sandstone; ++i5) {
                    HothUtils.setPos(chunk, x, y, z, 24);
                    ++y;
                }
                double sand = 1.0 + this.noiseGenerator.noise(rx, rz, 8, 43.0) * 4.0;
                for (int i6 = 0; i6 < (int)sand; ++i6) {
                    HothUtils.setPos(chunk, x, y, z, 12);
                    ++y;
                }
                double clay = 1.0 + this.noiseGenerator.noise(rx, rz, 3, 9.0) * 5.0;
                for (int i7 = 3; i7 < (int)clay; ++i7) {
                    if (i7 == 3) {
                        HothUtils.setPos(chunk, x, y, z, 172);
                    } else {
                        HothUtils.setPos(chunk, x, y, z, 82);
                    }
                    ++y;
                }
                while (y < 34 + surfaceOffset) {
                    HothUtils.setPos(chunk, x, y, z, 79);
                    ++y;
                }
                double icel = this.noiseGenerator.noise(rx, rz, 3, 68.0) * 8.0;
                for (int i8 = 3; i8 < (int)icel; ++i8) {
                    HothUtils.setPos(chunk, x, y, z, 79);
                    ++y;
                }
                double iceh = this.noiseGenerator.noise(rx, rz, 3, 7.0) * 15.0;
                double ice = (double)factor * (this.noiseGenerator.noise(rx, rz, 4, 63.0) * 2.0 + this.noiseGenerator.noise(rx, rz, 10, 12.0)) * 2.5;
                int icey = surfaceOffset + 64 + (int)ice;
                double dicey = (double)(surfaceOffset + 64) + ice;
                while ((double)y < (double)icey - iceh) {
                    HothUtils.setPos(chunk, x, y, z, 174);
                    ++y;
                }
                while (y < icey) {
                    HothUtils.setPos(chunk, x, y, z, 79);
                    ++y;
                }
                double domountain = this.noiseGenerator.noise(rx, rz, 4, 236.0) * 20.0;
                double mfactor = 0.0;
                if (domountain > 18.0) {
                    mfactor = 1.0;
                } else if (domountain > 16.0) {
                    mfactor = (domountain - 16.0) * 0.5;
                }
                if (mfactor > 0.0) {
                    double mountain = this.noiseGenerator.noise(rx, rz, 4, 27.0) * 84.0;
                    mountain += this.noiseGenerator.noise(rx, rz, 8, 3.0) * 5.0;
                    for (i = 0; i < (int)(mountain * mfactor); ++i) {
                        HothUtils.setPos(chunk, x, i + 26 + surfaceOffset, z, 1);
                        if (i + 26 + surfaceOffset <= y) continue;
                        y = i + 26 + surfaceOffset;
                    }
                }
                double snowblocks = 1.0 + this.noiseGenerator.noise(rx, rz, 8, 76.0) * 2.0;
                for (i = 0; i < (int)(snowblocks + (dicey - (double)((int)dicey))); ++i) {
                    HothUtils.setPos(chunk, x, y, z, 80);
                    ++y;
                }
                double dval = snowblocks + dicey;
                snowcover[z][x] = (byte)(8.0 * (dval - (double)((int)dval)));
                HothUtils.setPos(chunk, x, y, z, 78);
                if (smooth_snow) {
                    byte data = snowcover[z][x];
                    chunk.setBlockData(x, y, z, (int)data);
                }
                if ((dolava = this.noiseGenerator.noise(rx, rz, 4, 71.0) * 10.0) > 7.0 && (lavay = (int)(lava = this.noiseGenerator.noise(rx, rz, 4, 7.0) * 21.0) - 18) > -2) {
                    int start = 8 - (2 + lavay) / 2;
                    for (int i9 = -1; i9 < lavay; ++i9) {
                        if (start + i9 > 6) {
                            HothUtils.setPos(chunk, x, start + i9, z, 0);
                            continue;
                        }
                        HothUtils.setPos(chunk, x, start + i9, z, 11);
                    }
                }
                if ((dowater = this.noiseGenerator.noise(rx, rz, 4, 91.0) * 10.0) > 7.0 && (watery = (int)(water = this.noiseGenerator.noise(rx, rz, 4, 8.0) * 21.0) - 18) > -2) {
                    int start = 23 - (2 + watery) / 2;
                    for (int i10 = -1; i10 < watery; ++i10) {
                        if (start + i10 > 21) {
                            HothUtils.setPos(chunk, x, start + i10, z, 0);
                            continue;
                        }
                        HothUtils.setPos(chunk, x, start + i10, z, 9);
                    }
                }
                chunk.setBiomeId(x, z, EnumBiome.ICE_PLAINS_SPIKES.id);
            }
        }
    }

    private static class HothUtils {
        private HothUtils() {
        }

        public static void setPos(BaseFullChunk chunk, int x, int y, int z, int material) {
            chunk.setBlock(x, y, z, material);
        }
    }
}

