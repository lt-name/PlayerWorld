package shadow.playerworld.generators;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.level.generator.populator.impl.PopulatorFlower;
import cn.nukkit.level.generator.populator.impl.PopulatorGrass;
import cn.nukkit.level.generator.populator.type.Populator;
import cn.nukkit.math.NukkitRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shadow.playerworld.noise.bukkit.PerlinOctaveGenerator;
import shadow.playerworld.noise.bukkit.SimplexOctaveGenerator;
import shadow.playerworld.PlayerWorld;

public class Island implements BaseGenerator {
    private long seed = -1L;
    private SimplexOctaveGenerator bg;
    private SimplexOctaveGenerator g;
    private PerlinOctaveGenerator e;
    private PerlinOctaveGenerator c;
    private SimplexOctaveGenerator d;
    private Random rand;
    private NukkitRandom nrand;
    private List<Populator> populators;

    private void init(long s) {
        if (s == this.seed) {
            return;
        }
        this.seed = s;
        this.nrand = new NukkitRandom(this.seed);
        this.rand = new Random(this.seed);
        this.bg = new SimplexOctaveGenerator(this.seed, 18);
        this.bg.setScale(0.0078125);
        this.g = new SimplexOctaveGenerator(this.seed, 36);
        this.g.setScale(0.015625);
        this.e = new PerlinOctaveGenerator(this.seed, 32);
        this.e.setScale(0.03125);
        this.c = new PerlinOctaveGenerator(this.seed, 38);
        this.c.setScale(0.015625);
        this.d = new SimplexOctaveGenerator(this.seed, 18);
        this.d.setScale(0.0078125);
    }

    public Island() {
        this.init(this.seed + 1L);
        this.populators = new ArrayList<Populator>();
        PopulatorGrass p = new PopulatorGrass();
        p.setBaseAmount(50);
        p.setRandomAmount(50);
        this.populators.add((Populator)p);
        PopulatorFlower flower = new PopulatorFlower();
        flower.setBaseAmount(10);
        flower.addType(37, 0);
        flower.addType(38, 0);
        flower.addType(38, 2);
        flower.addType(38, 3);
        flower.addType(38, 4);
        flower.addType(38, 5);
        flower.addType(38, 6);
        flower.addType(38, 7);
        flower.addType(38, 8);
        flower.addType(175, 1);
        flower.addType(175, 4);
        flower.addType(175, 5);
        this.populators.add((Populator)flower);
    }

    @Override
    public void generateTerrain(int ChunkX, int ChunkZ, BaseFullChunk chunk) {
        this.init(PlayerWorld.seed);
        int max = 255;
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                int thisblock;
                int y2;
                int rx = x + ChunkX * 16;
                int rz = z + ChunkZ * 16;
                int h = (int)(this.bg.noise((double)rx, (double)rz, 0.75, 0.75) * 38.0 + 58.0);
                int bh = (int)(this.g.noise((double)rx, (double)rz, 0.5, 0.5) * 24.0 + 56.0);
                int ch = (int)(this.c.noise((double)rx, (double)rz, 0.75, 0.25) + this.g.noise((double)rx, (double)rz, 1.25, 0.75) + this.bg.noise((double)rx, (double)rz, 0.25, 0.5) * 18.0 + 20.0);
                int eg = (int)(this.e.noise((double)rx, (double)rz, 0.5, 0.5) + this.g.noise((double)rx, (double)rz, 1.25, 0.75) * 38.0 + 64.0);
                int ds = (int)(this.d.noise((double)rx, (double)rz, 0.25, 0.75) + this.c.noise((double)rx, (double)rz, 0.75, 0.25) + this.g.noise((double)rx, (double)rz, 1.25, 0.75) + this.bg.noise((double)rx, (double)rz, 0.25, 0.5) * 32.0 + 25.0);
                for (int y = 2; y < max; ++y) {
                    if (y < h || y < bh || y < eg) {
                        chunk.setBlock(x, y, z, 1);
                    }
                    if (y > ch * -1 + 95 || y > ds * -1 + 101) {
                        chunk.setBlock(x, y, z, 0);
                    }
                    if (y <= 70) continue;
                    int it = 0;
                    int maxX = Math.max(x + 1, x - 1);
                    int maxY = Math.max(y + 1, y - 1);
                    int maxZ = Math.max(z + 1, z - 1);
                    int minX = Math.max(x + 1, x - 1);
                    int minY = Math.max(y + 1, y - 1);
                    int minZ = Math.max(z + 1, z - 1);
                    for (int i = 0; i <= Math.abs(maxX - minX); ++i) {
                        for (int ii = 0; ii <= Math.abs(maxZ - minZ); ++ii) {
                            for (int iii = 0; iii <= Math.abs(maxY - minY); ++iii) {
                                if (minX + i > 15 || minY + iii > 255 || minZ + ii > 15) {
                                    ++it;
                                    continue;
                                }
                                int material = chunk.getBlockId(minX + i, minY + iii, minZ + ii);
                                if (material != 0 && material != 9 && material != 8) continue;
                                ++it;
                            }
                        }
                    }
                    if (it <= 5) continue;
                    chunk.setBlock(x, y, z, 0);
                }
                int b2 = 2;
                int b3 = 3;
                for (y2 = 74; y2 < max; ++y2) {
                    thisblock = chunk.getBlockId(x, y2, z);
                    int blockabove = chunk.getBlockId(x, y2 + 1, z);
                    if (thisblock == 0 || blockabove != 0) continue;
                    chunk.setBlock(x, y2, z, b2);
                    int y3 = this.rand.nextInt(5) + 1;
                    int y4 = this.rand.nextInt(5) + 1;
                    if (y2 >= 128) continue;
                    if (chunk.getBlockId(x, y2 - y3, z) != 0) {
                        chunk.setBlock(x, y2 - y3, z, b3);
                    }
                    if (chunk.getBlockId(x, y2 - y4, z) == 0) continue;
                    chunk.setBlock(x, y2 - y4, z, b3);
                }
                for (y2 = 1; y2 < 75; ++y2) {
                    thisblock = chunk.getBlockId(x, y2, z);
                    if (thisblock != 0) continue;
                    chunk.setBlock(x, y2, z, 9);
                    int blockbelow = chunk.getBlockId(x, y2 - 1, z);
                    if (blockbelow != 2 && blockbelow != 1) continue;
                    chunk.setBlock(x, y2 - 1, z, 13);
                }
                chunk.setBlock(x, 1, z, 7);
                chunk.setBlock(x, 0, z, 7);
                chunk.setBiomeId(x, z, EnumBiome.OCEAN.id);
            }
        }
        this.populators.forEach(populator -> populator.populate((ChunkManager)PlayerWorld.instance.world, ChunkX, ChunkZ, this.nrand, (FullChunk)chunk));
    }

    @Override
    public String getName() {
        return "\u5c9b\u5c7f\u4e16\u754c";
    }
}

