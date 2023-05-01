package shadow.playerworld.noise.bukkit;

import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.Level;
import cn.nukkit.math.NukkitRandom;

public class SimplexNoiseGenerator
extends PerlinNoiseGenerator {
    protected static final double SQRT_3 = Math.sqrt(3.0);
    protected static final double SQRT_5 = Math.sqrt(5.0);
    protected static final double F2 = 0.5 * (SQRT_3 - 1.0);
    protected static final double G2 = (3.0 - SQRT_3) / 6.0;
    protected static final double G22 = G2 * 2.0 - 1.0;
    protected static final double F3 = 0.3333333333333333;
    protected static final double G3 = 0.16666666666666666;
    protected static final double F4 = (SQRT_5 - 1.0) / 4.0;
    protected static final double G4 = (5.0 - SQRT_5) / 20.0;
    protected static final double G42 = G4 * 2.0;
    protected static final double G43 = G4 * 3.0;
    protected static final double G44 = G4 * 4.0 - 1.0;
    protected static final int[][] grad4 = new int[][]{{0, 1, 1, 1}, {0, 1, 1, -1}, {0, 1, -1, 1}, {0, 1, -1, -1}, {0, -1, 1, 1}, {0, -1, 1, -1}, {0, -1, -1, 1}, {0, -1, -1, -1}, {1, 0, 1, 1}, {1, 0, 1, -1}, {1, 0, -1, 1}, {1, 0, -1, -1}, {-1, 0, 1, 1}, {-1, 0, 1, -1}, {-1, 0, -1, 1}, {-1, 0, -1, -1}, {1, 1, 0, 1}, {1, 1, 0, -1}, {1, -1, 0, 1}, {1, -1, 0, -1}, {-1, 1, 0, 1}, {-1, 1, 0, -1}, {-1, -1, 0, 1}, {-1, -1, 0, -1}, {1, 1, 1, 0}, {1, 1, -1, 0}, {1, -1, 1, 0}, {1, -1, -1, 0}, {-1, 1, 1, 0}, {-1, 1, -1, 0}, {-1, -1, 1, 0}, {-1, -1, -1, 0}};
    protected static final int[][] simplex = new int[][]{{0, 1, 2, 3}, {0, 1, 3, 2}, {0, 0, 0, 0}, {0, 2, 3, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 2, 3, 0}, {0, 2, 1, 3}, {0, 0, 0, 0}, {0, 3, 1, 2}, {0, 3, 2, 1}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 3, 2, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {1, 2, 0, 3}, {0, 0, 0, 0}, {1, 3, 0, 2}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {2, 3, 0, 1}, {2, 3, 1, 0}, {1, 0, 2, 3}, {1, 0, 3, 2}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {2, 0, 3, 1}, {0, 0, 0, 0}, {2, 1, 3, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {2, 0, 1, 3}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 0, 1, 2}, {3, 0, 2, 1}, {0, 0, 0, 0}, {3, 1, 2, 0}, {2, 1, 0, 3}, {0, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}, {3, 1, 0, 2}, {0, 0, 0, 0}, {3, 2, 0, 1}, {3, 2, 1, 0}};
    protected double offsetW;
    private static final SimplexNoiseGenerator instance = new SimplexNoiseGenerator();

    protected SimplexNoiseGenerator() {
    }

    public SimplexNoiseGenerator(Level world) {
        this(new NukkitRandom(world.getSeed()));
    }

    public SimplexNoiseGenerator(ChunkManager level) {
        this(new NukkitRandom(level.getSeed()));
    }

    public SimplexNoiseGenerator(long seed) {
        this(new NukkitRandom(seed));
    }

    public SimplexNoiseGenerator(NukkitRandom rand) {
        super(rand);
        this.offsetW = rand.nextDouble() * 256.0;
    }

    protected static double dot(int[] g, double x, double y) {
        return (double)g[0] * x + (double)g[1] * y;
    }

    protected static double dot(int[] g, double x, double y, double z) {
        return (double)g[0] * x + (double)g[1] * y + (double)g[2] * z;
    }

    protected static double dot(int[] g, double x, double y, double z, double w) {
        return (double)g[0] * x + (double)g[1] * y + (double)g[2] * z + (double)g[3] * w;
    }

    public static double getNoise(double xin) {
        return instance.noise(xin);
    }

    public static double getNoise(double xin, double yin) {
        return instance.noise(xin, yin);
    }

    public static double getNoise(double xin, double yin, double zin) {
        return instance.noise(xin, yin, zin);
    }

    public static double getNoise(double x, double y, double z, double w) {
        return instance.noise(x, y, z, w);
    }

    @Override
    public double noise(double xin, double yin, double zin) {
        double n3;
        double n2;
        double n1;
        double n0;
        int k2;
        int j2;
        int i2;
        int k1;
        int j1;
        int i1;
        double s = ((xin += this.offsetX) + (yin += this.offsetY) + (zin += this.offsetZ)) * 0.3333333333333333;
        int i = floor(xin + s);
        int j = floor(yin + s);
        int k = floor(zin + s);
        double t = (double)(i + j + k) * 0.16666666666666666;
        double X0 = (double)i - t;
        double Y0 = (double)j - t;
        double Z0 = (double)k - t;
        double x0 = xin - X0;
        double y0 = yin - Y0;
        double z0 = zin - Z0;
        if (x0 >= y0) {
            if (y0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 1;
                k2 = 0;
            } else if (x0 >= z0) {
                i1 = 1;
                j1 = 0;
                k1 = 0;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            } else {
                i1 = 0;
                j1 = 0;
                k1 = 1;
                i2 = 1;
                j2 = 0;
                k2 = 1;
            }
        } else if (y0 < z0) {
            i1 = 0;
            j1 = 0;
            k1 = 1;
            i2 = 0;
            j2 = 1;
            k2 = 1;
        } else if (x0 < z0) {
            i1 = 0;
            j1 = 1;
            k1 = 0;
            i2 = 0;
            j2 = 1;
            k2 = 1;
        } else {
            i1 = 0;
            j1 = 1;
            k1 = 0;
            i2 = 1;
            j2 = 1;
            k2 = 0;
        }
        double x1 = x0 - (double)i1 + 0.16666666666666666;
        double y1 = y0 - (double)j1 + 0.16666666666666666;
        double z1 = z0 - (double)k1 + 0.16666666666666666;
        double x2 = x0 - (double)i2 + 0.3333333333333333;
        double y2 = y0 - (double)j2 + 0.3333333333333333;
        double z2 = z0 - (double)k2 + 0.3333333333333333;
        double x3 = x0 - 1.0 + 0.5;
        double y3 = y0 - 1.0 + 0.5;
        double z3 = z0 - 1.0 + 0.5;
        int ii = i & 0xFF;
        int jj = j & 0xFF;
        int kk = k & 0xFF;
        int gi0 = this.perm[ii + this.perm[jj + this.perm[kk]]] % 12;
        int gi1 = this.perm[ii + i1 + this.perm[jj + j1 + this.perm[kk + k1]]] % 12;
        int gi2 = this.perm[ii + i2 + this.perm[jj + j2 + this.perm[kk + k2]]] % 12;
        int gi3 = this.perm[ii + 1 + this.perm[jj + 1 + this.perm[kk + 1]]] % 12;
        double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0;
        if (t0 < 0.0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * SimplexNoiseGenerator.dot(grad3[gi0], x0, y0, z0);
        }
        double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1;
        if (t1 < 0.0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * SimplexNoiseGenerator.dot(grad3[gi1], x1, y1, z1);
        }
        double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2;
        if (t2 < 0.0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * SimplexNoiseGenerator.dot(grad3[gi2], x2, y2, z2);
        }
        double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3;
        if (t3 < 0.0) {
            n3 = 0.0;
        } else {
            t3 *= t3;
            n3 = t3 * t3 * SimplexNoiseGenerator.dot(grad3[gi3], x3, y3, z3);
        }
        return 32.0 * (n0 + n1 + n2 + n3);
    }

    @Override
    public double noise(double xin, double yin) {
        double n2;
        double n1;
        double n0;
        int j1;
        int i1;
        double Y0;
        double y0;
        int j;
        double t;
        double s;
        int i;
        double X0;
        double x0;
        if ((x0 = (xin += this.offsetX) - (X0 = (double)(i = floor(xin + (s = (xin + (yin += this.offsetY)) * F2))) - (t = (double)(i + (j = floor(yin + s))) * G2))) > (y0 = yin - (Y0 = (double)j - t))) {
            i1 = 1;
            j1 = 0;
        } else {
            i1 = 0;
            j1 = 1;
        }
        double x1 = x0 - (double)i1 + G2;
        double y1 = y0 - (double)j1 + G2;
        double x2 = x0 + G22;
        double y2 = y0 + G22;
        int ii = i & 0xFF;
        int jj = j & 0xFF;
        int gi0 = this.perm[ii + this.perm[jj]] % 12;
        int gi1 = this.perm[ii + i1 + this.perm[jj + j1]] % 12;
        int gi2 = this.perm[ii + 1 + this.perm[jj + 1]] % 12;
        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if (t0 < 0.0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * SimplexNoiseGenerator.dot(grad3[gi0], x0, y0);
        }
        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if (t1 < 0.0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * SimplexNoiseGenerator.dot(grad3[gi1], x1, y1);
        }
        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0.0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * SimplexNoiseGenerator.dot(grad3[gi2], x2, y2);
        }
        return 70.0 * (n0 + n1 + n2);
    }

    public double noise(double x, double y, double z, double w) {
        double n4;
        double n3;
        double n2;
        double n1;
        double n0;
        double s = ((x += this.offsetX) + (y += this.offsetY) + (z += this.offsetZ) + (w += this.offsetW)) * F4;
        int i = floor(x + s);
        int j = floor(y + s);
        int k = floor(z + s);
        int l = floor(w + s);
        double t = (double)(i + j + k + l) * G4;
        double X0 = (double)i - t;
        double Y0 = (double)j - t;
        double Z0 = (double)k - t;
        double W0 = (double)l - t;
        double x0 = x - X0;
        double y0 = y - Y0;
        double z0 = z - Z0;
        double w0 = w - W0;
        int c1 = x0 > y0 ? 32 : 0;
        int c2 = x0 > z0 ? 16 : 0;
        int c3 = y0 > z0 ? 8 : 0;
        int c4 = x0 > w0 ? 4 : 0;
        int c5 = y0 > w0 ? 2 : 0;
        int c6 = z0 > w0 ? 1 : 0;
        int c = c1 + c2 + c3 + c4 + c5 + c6;
        int i1 = simplex[c][0] >= 3 ? 1 : 0;
        int j1 = simplex[c][1] >= 3 ? 1 : 0;
        int k1 = simplex[c][2] >= 3 ? 1 : 0;
        int l1 = simplex[c][3] >= 3 ? 1 : 0;
        int i2 = simplex[c][0] >= 2 ? 1 : 0;
        int j2 = simplex[c][1] >= 2 ? 1 : 0;
        int k2 = simplex[c][2] >= 2 ? 1 : 0;
        int l2 = simplex[c][3] >= 2 ? 1 : 0;
        int i3 = simplex[c][0] >= 1 ? 1 : 0;
        int j3 = simplex[c][1] >= 1 ? 1 : 0;
        int k3 = simplex[c][2] >= 1 ? 1 : 0;
        int l3 = simplex[c][3] >= 1 ? 1 : 0;
        double x1 = x0 - (double)i1 + G4;
        double y1 = y0 - (double)j1 + G4;
        double z1 = z0 - (double)k1 + G4;
        double w1 = w0 - (double)l1 + G4;
        double x2 = x0 - (double)i2 + G42;
        double y2 = y0 - (double)j2 + G42;
        double z2 = z0 - (double)k2 + G42;
        double w2 = w0 - (double)l2 + G42;
        double x3 = x0 - (double)i3 + G43;
        double y3 = y0 - (double)j3 + G43;
        double z3 = z0 - (double)k3 + G43;
        double w3 = w0 - (double)l3 + G43;
        double x4 = x0 + G44;
        double y4 = y0 + G44;
        double z4 = z0 + G44;
        double w4 = w0 + G44;
        int ii = i & 0xFF;
        int jj = j & 0xFF;
        int kk = k & 0xFF;
        int ll = l & 0xFF;
        int gi0 = this.perm[ii + this.perm[jj + this.perm[kk + this.perm[ll]]]] % 32;
        int gi1 = this.perm[ii + i1 + this.perm[jj + j1 + this.perm[kk + k1 + this.perm[ll + l1]]]] % 32;
        int gi2 = this.perm[ii + i2 + this.perm[jj + j2 + this.perm[kk + k2 + this.perm[ll + l2]]]] % 32;
        int gi3 = this.perm[ii + i3 + this.perm[jj + j3 + this.perm[kk + k3 + this.perm[ll + l3]]]] % 32;
        int gi4 = this.perm[ii + 1 + this.perm[jj + 1 + this.perm[kk + 1 + this.perm[ll + 1]]]] % 32;
        double t0 = 0.6 - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
        if (t0 < 0.0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * SimplexNoiseGenerator.dot(grad4[gi0], x0, y0, z0, w0);
        }
        double t1 = 0.6 - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
        if (t1 < 0.0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * SimplexNoiseGenerator.dot(grad4[gi1], x1, y1, z1, w1);
        }
        double t2 = 0.6 - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
        if (t2 < 0.0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * SimplexNoiseGenerator.dot(grad4[gi2], x2, y2, z2, w2);
        }
        double t3 = 0.6 - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
        if (t3 < 0.0) {
            n3 = 0.0;
        } else {
            t3 *= t3;
            n3 = t3 * t3 * SimplexNoiseGenerator.dot(grad4[gi3], x3, y3, z3, w3);
        }
        double t4 = 0.6 - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
        if (t4 < 0.0) {
            n4 = 0.0;
        } else {
            t4 *= t4;
            n4 = t4 * t4 * SimplexNoiseGenerator.dot(grad4[gi4], x4, y4, z4, w4);
        }
        return 27.0 * (n0 + n1 + n2 + n3 + n4);
    }

    public static SimplexNoiseGenerator getInstance() {
        return instance;
    }
}

