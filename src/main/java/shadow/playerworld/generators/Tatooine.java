package shadow.playerworld.generators;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorCactus;
import cn.nukkit.level.generator.populator.impl.PopulatorDeadBush;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.generators.noise.NoiseGenerator;

public class Tatooine implements BaseGenerator {
    private NoiseGenerator noiseGenerator = null;
    private NukkitRandom random = new NukkitRandom(PlayerWorld.seed);
    private List<Populator> populators = new ArrayList<Populator>();

    public Tatooine() {
        PopulatorCactus cactus = new PopulatorCactus();
        cactus.setBaseAmount(2);
        this.populators.add((Populator)cactus);
        PopulatorDeadBush deadbush = new PopulatorDeadBush();
        deadbush.setBaseAmount(2);
        this.populators.add((Populator)deadbush);
    }

    private static int getBedrockMaterial(Random localRand, int limit) {
        return Tatooine.getBedrockMaterial(localRand, limit, 1);
    }

    private static int getBedrockMaterial(Random localRand, int limit, int material) {
        int ran = localRand.nextInt() & 0xFF;
        if (ran > limit) {
            return material;
        }
        return 7;
    }

    @Override
    public String getName() {
        return "\u6c99\u6f20\u4e16\u754c";
    }

    @Override
    public void generateTerrain(int chunkx, int chunkz, BaseFullChunk chunk) {
        int mAIR = 0;
        int mLAVA = 10;
        if (this.noiseGenerator == null) {
            this.noiseGenerator = new NoiseGenerator(PlayerWorld.seed);
        }
        int surfaceOffset = 0;
        Random localRand = new Random(chunkx * chunkz);
        for (int z = 0; z < 16; ++z) {
            for (int x = 0; x < 16; ++x) {
                double water;
                int watery;
                double dowater;
                double lava;
                int lavay;
                int i;
                int rx = chunkx * 16 + x;
                int rz = chunkz * 16 + z;
                float factor = 1.0f;
                int y = 0;
                HothUtils.setPos(chunk, x, y, z, 7);
                HothUtils.setPos(chunk, x, y + 1, z, Tatooine.getBedrockMaterial(localRand, 230));
                HothUtils.setPos(chunk, x, y + 2, z, Tatooine.getBedrockMaterial(localRand, 179));
                HothUtils.setPos(chunk, x, y + 3, z, Tatooine.getBedrockMaterial(localRand, 128));
                HothUtils.setPos(chunk, x, y + 4, z, Tatooine.getBedrockMaterial(localRand, 76));
                HothUtils.setPos(chunk, x, y + 5, z, Tatooine.getBedrockMaterial(localRand, 51));
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
                double clay = 1.0 + this.noiseGenerator.noise(rx, rz, 3, 9.0) * 5.0;
                for (int i5 = 3; i5 < (int)clay; ++i5) {
                    if (i5 == 3) {
                        HothUtils.setPos(chunk, x, y, z, 172);
                    } else {
                        HothUtils.setPos(chunk, x, y, z, 82);
                    }
                    ++y;
                }
                double iceh = this.noiseGenerator.noise(rx, rz, 3, 7.0) * 15.0;
                double ice = (double)factor * (this.noiseGenerator.noise(rx, rz, 4, 63.0) * 2.0 + this.noiseGenerator.noise(rx, rz, 10, 12.0)) * 2.5;
                int icey = surfaceOffset + 64 + (int)ice;
                double dicey = (double)(surfaceOffset + 64) + ice;
                while ((double)y < (double)icey - iceh) {
                    HothUtils.setPos(chunk, x, y, z, 24);
                    ++y;
                }
                while (y < icey) {
                    HothUtils.setPos(chunk, x, y, z, 12);
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
                    HothUtils.setPos(chunk, x, y, z, 12);
                    ++y;
                }
                double a = this.noiseGenerator.noise(rx, rz, 2, 60.0) * 8.0;
                for (int i6 = 4; i6 < 128 + surfaceOffset; ++i6) {
                    int old;
                    double d = this.noiseGenerator.noise(rx, i6, rz, 4, 8.0) * 16.0;
                    if (i6 > 96 + surfaceOffset) {
                        a += 8.0 * (((double)(i6 + surfaceOffset) - 32.0) / 32.0);
                    }
                    if (!(d > a + 10.0) || (old = chunk.getBlockId(x, i6, z)) != 1 && old != 24 && old != 12) continue;
                    if (i6 < 12) {
                        HothUtils.setPos(chunk, x, i6, z, mLAVA);
                        continue;
                    }
                    HothUtils.setPos(chunk, x, i6, z, mAIR);
                }
                double dolava = this.noiseGenerator.noise(rx, rz, 4, 71.0) * 10.0;
                if (dolava > 7.0 && (lavay = (int)(lava = this.noiseGenerator.noise(rx, rz, 4, 7.0) * 21.0) - 18) > -2) {
                    int start = 8 - (2 + lavay) / 2;
                    for (int i7 = -1; i7 < lavay; ++i7) {
                        if (start + i7 > 6) {
                            HothUtils.setPos(chunk, x, start + i7, z, 0);
                            continue;
                        }
                        HothUtils.setPos(chunk, x, start + i7, z, 11);
                    }
                }
                if ((dowater = this.noiseGenerator.noise(rx, rz, 4, 91.0) * 10.0) > 7.0 && (watery = (int)(water = this.noiseGenerator.noise(rx, rz, 4, 8.0) * 21.0) - 18) > -2) {
                    int start = 23 - (2 + watery) / 2;
                    for (int i8 = -1; i8 < watery; ++i8) {
                        if (start + i8 > 21) {
                            HothUtils.setPos(chunk, x, start + i8, z, 0);
                            continue;
                        }
                        HothUtils.setPos(chunk, x, start + i8, z, 9);
                    }
                }
                chunk.setBiomeId(x, z, EnumBiome.DESERT_HILLS.id);
            }
        }
        HothUtils.replaceTop(chunk, 24, 1, 12, 256);
        this.populators.forEach(populator -> populator.populate((ChunkManager)PlayerWorld.instance.world, chunkx, chunkz, this.random, (FullChunk)chunk));
    }

    private static class HothUtils {
        private HothUtils() {
        }

        public static void setPos(BaseFullChunk chunk, int x, int y, int z, int material) {
            chunk.setBlock(x, y, z, material);
        }

        public static void replaceTop(BaseFullChunk chunk, int from1, int from2, int to, int maxy) {
            for (int x = 0; x < 16; ++x) {
                for (int z = 0; z < 16; ++z) {
                    int old;
                    int y = HothUtils.getMaxY(chunk, x, z, maxy);
                    if (y <= 0 || (old = chunk.getBlockId(x, y, z)) != from1 && old != from2) continue;
                    HothUtils.setPos(chunk, x, y, z, to);
                }
            }
        }

        private static int getMaxY(BaseFullChunk chunk, int x, int z, int maxy) {
            for (int i = maxy - 1; i > 0; --i) {
                int type = HothUtils.getRawPos(chunk, x, i, z);
                if (type == 0) continue;
                return i;
            }
            return 0;
        }

        private static int getRawPos(BaseFullChunk chunk, int x, int y, int z) {
            return chunk.getBlockId(x, y, z);
        }
    }
}

