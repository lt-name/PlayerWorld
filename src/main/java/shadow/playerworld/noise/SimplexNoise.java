package shadow.playerworld.noise;

import cn.nukkit.math.NukkitRandom;

public class SimplexNoise extends PerlinNoise {
    protected static final double SQRT_3 = 1.7320508075688772;
    protected static final double F2 = 0.3660254037844386;
    protected static final double G2 = 0.21132486540518713;
    protected static final double G22 = -0.5773502691896257;
    protected static final double F3 = 0.3333333333333333;
    protected static final double G3 = 0.16666666666666666;
    protected static final double G32 = 0.3333333333333333;
    protected static final double G33 = -0.5;
    private static final Grad[] grad3 = new Grad[]{new Grad(1.0, 1.0, 0.0), new Grad(-1.0, 1.0, 0.0), new Grad(1.0, -1.0, 0.0), new Grad(-1.0, -1.0, 0.0), new Grad(1.0, 0.0, 1.0), new Grad(-1.0, 0.0, 1.0), new Grad(1.0, 0.0, -1.0), new Grad(-1.0, 0.0, -1.0), new Grad(0.0, 1.0, 1.0), new Grad(0.0, -1.0, 1.0), new Grad(0.0, 1.0, -1.0), new Grad(0.0, -1.0, -1.0)};
    protected final int[] permMod12 = new int[512];

    public SimplexNoise(NukkitRandom rand) {
        super(rand);
        for (int i = 0; i < 512; ++i) {
            this.permMod12[i] = this.perm[i] % 12;
        }
    }

    public static int floor(double x) {
        return x > 0.0 ? (int)x : (int)x - 1;
    }

    protected static double dot(Grad g, double x, double y) {
        return g.x * x + g.y * y;
    }

    protected static double dot(Grad g, double x, double y, double z) {
        return g.x * x + g.y * y + g.z * z;
    }

    @Override
    protected double[] get2dNoise(double[] noise, double x, double z, int sizeX, int sizeY, double scaleX, double scaleY, double amplitude) {
        int index = 0;
        for (int i = 0; i < sizeY; ++i) {
            double zin = this.offsetY + (z + (double)i) * scaleY;
            for (int j = 0; j < sizeX; ++j) {
                double xin = this.offsetX + (x + (double)j) * scaleX;
                int n = index++;
                noise[n] = noise[n] + this.simplex2D(xin, zin) * amplitude;
            }
        }
        return noise;
    }

    @Override
    protected double[] get3dNoise(double[] noise, double x, double y, double z, int sizeX, int sizeY, int sizeZ, double scaleX, double scaleY, double scaleZ, double amplitude) {
        int index = 0;
        for (int i = 0; i < sizeZ; ++i) {
            double zin = this.offsetZ + (z + (double)i) * scaleZ;
            for (int j = 0; j < sizeX; ++j) {
                double xin = this.offsetX + (x + (double)j) * scaleX;
                for (int k = 0; k < sizeY; ++k) {
                    double yin = this.offsetY + (y + (double)k) * scaleY;
                    int n = index++;
                    noise[n] = noise[n] + this.simplex3D(xin, yin, zin) * amplitude;
                }
            }
        }
        return noise;
    }

    @Override
    public double noise(double xin, double yin) {
        return this.simplex2D(xin += this.offsetX, yin += this.offsetY);
    }

    @Override
    public double noise(double xin, double yin, double zin) {
        return this.simplex3D(xin += this.offsetX, yin += this.offsetY, zin += this.offsetZ);
    }

    private double simplex2D(double xin, double yin) {
        double n2;
        double n1;
        double n0;
        int j1;
        int i1;
        double dy0;
        double y0;
        int j;
        double t;
        double s = (xin + yin) * 0.3660254037844386;
        int i = SimplexNoise.floor(xin + s);
        double dx0 = (double)i - (t = (double)(i + (j = SimplexNoise.floor(yin + s))) * 0.21132486540518713);
        double x0 = xin - dx0;
        if (x0 > (y0 = yin - (dy0 = (double)j - t))) {
            i1 = 1;
            j1 = 0;
        } else {
            i1 = 0;
            j1 = 1;
        }
        double x1 = x0 - (double)i1 + 0.21132486540518713;
        double y1 = y0 - (double)j1 + 0.21132486540518713;
        double x2 = x0 + -0.5773502691896257;
        double y2 = y0 + -0.5773502691896257;
        int ii = i & 0xFF;
        int jj = j & 0xFF;
        int gi0 = this.permMod12[ii + this.perm[jj]];
        int gi1 = this.permMod12[ii + i1 + this.perm[jj + j1]];
        int gi2 = this.permMod12[ii + 1 + this.perm[jj + 1]];
        double t0 = 0.5 - x0 * x0 - y0 * y0;
        if (t0 < 0.0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * SimplexNoise.dot(grad3[gi0], x0, y0);
        }
        double t1 = 0.5 - x1 * x1 - y1 * y1;
        if (t1 < 0.0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * SimplexNoise.dot(grad3[gi1], x1, y1);
        }
        double t2 = 0.5 - x2 * x2 - y2 * y2;
        if (t2 < 0.0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * SimplexNoise.dot(grad3[gi2], x2, y2);
        }
        return 70.0 * (n0 + n1 + n2);
    }

    private double simplex3D(double xin, double yin, double zin) {
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
        double s = (xin + yin + zin) * 0.3333333333333333;
        int i = SimplexNoise.floor(xin + s);
        int j = SimplexNoise.floor(yin + s);
        int k = SimplexNoise.floor(zin + s);
        double t = (double)(i + j + k) * 0.16666666666666666;
        double dx0 = (double)i - t;
        double dy0 = (double)j - t;
        double dz0 = (double)k - t;
        double x0 = xin - dx0;
        double y0 = yin - dy0;
        double z0 = zin - dz0;
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
        int ii = i & 0xFF;
        int jj = j & 0xFF;
        int kk = k & 0xFF;
        int gi0 = this.permMod12[ii + this.perm[jj + this.perm[kk]]];
        int gi1 = this.permMod12[ii + i1 + this.perm[jj + j1 + this.perm[kk + k1]]];
        int gi2 = this.permMod12[ii + i2 + this.perm[jj + j2 + this.perm[kk + k2]]];
        int gi3 = this.permMod12[ii + 1 + this.perm[jj + 1 + this.perm[kk + 1]]];
        double t0 = 0.5 - x0 * x0 - y0 * y0 - z0 * z0;
        if (t0 < 0.0) {
            n0 = 0.0;
        } else {
            t0 *= t0;
            n0 = t0 * t0 * SimplexNoise.dot(grad3[gi0], x0, y0, z0);
        }
        double t1 = 0.5 - x1 * x1 - y1 * y1 - z1 * z1;
        if (t1 < 0.0) {
            n1 = 0.0;
        } else {
            t1 *= t1;
            n1 = t1 * t1 * SimplexNoise.dot(grad3[gi1], x1, y1, z1);
        }
        double t2 = 0.5 - x2 * x2 - y2 * y2 - z2 * z2;
        if (t2 < 0.0) {
            n2 = 0.0;
        } else {
            t2 *= t2;
            n2 = t2 * t2 * SimplexNoise.dot(grad3[gi2], x2, y2, z2);
        }
        double x3 = x0 + -0.5;
        double y3 = y0 + -0.5;
        double z3 = z0 + -0.5;
        double t3 = 0.5 - x3 * x3 - y3 * y3 - z3 * z3;
        if (t3 < 0.0) {
            n3 = 0.0;
        } else {
            t3 *= t3;
            n3 = t3 * t3 * SimplexNoise.dot(grad3[gi3], x3, y3, z3);
        }
        return 32.0 * (n0 + n1 + n2 + n3);
    }

    private static class Grad {
        public double x;
        public double y;
        public double z;

        Grad(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}

