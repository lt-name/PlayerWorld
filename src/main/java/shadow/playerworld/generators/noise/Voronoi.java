package shadow.playerworld.generators.noise;

import java.util.Random;

public class Voronoi {
    private final float[][][][][] grid;
    private final Random r;
    private final int density;
    private final int size;
    private final int zsize;
    private final int dimensions;
    private final boolean is2D;
    private final DistanceMetric metric;
    private final int level;
    static final int[][] order3D = new int[][]{{0, 0, 0}, {1, 0, 0}, {0, 1, 0}, {0, 0, 1}, {-1, 0, 0}, {0, -1, 0}, {0, 0, -1}, {1, 1, 0}, {1, 0, 1}, {0, 1, 1}, {-1, 1, 0}, {-1, 0, 1}, {0, -1, 1}, {1, -1, 0}, {1, 0, -1}, {0, 1, -1}, {-1, -1, 0}, {-1, 0, -1}, {0, -1, -1}, {1, 1, 1}, {-1, 1, 1}, {1, -1, 1}, {1, 1, -1}, {-1, -1, 1}, {-1, 1, -1}, {1, -1, -1}, {-1, -1, -1}};
    static final int[][] order2D = new int[][]{{0, 0}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, 1}, {1, -1}, {-1, -1}};

    public Voronoi(int size, boolean is2D, long seed, int density, DistanceMetric metric, int level) {
        this.zsize = is2D ? 1 : size;
        this.dimensions = is2D ? 2 : 3;
        this.grid = new float[size][size][this.zsize][density][this.dimensions];
        this.r = new Random(seed);
        this.size = size;
        this.density = density;
        this.metric = metric;
        this.level = level;
        this.is2D = is2D;
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                for (int k = 0; k < this.zsize; ++k) {
                    for (int d = 0; d < density; ++d) {
                        for (int e = 0; e < this.dimensions; ++e) {
                            this.grid[i][j][k][d][e] = this.r.nextFloat();
                        }
                    }
                }
            }
        }
    }

    private float distance(float[] a, int[] offset, float[] b) {
        float[] m = new float[this.dimensions];
        for (int i = 0; i < this.dimensions; ++i) {
            m[i] = b[i] - (a[i] + (float)offset[i]);
        }
        float d = 0.0f;
        switch (this.metric) {
            case Linear: {
                for (int i = 0; i < this.dimensions; ++i) {
                    d += m[i] * m[i];
                }
                return (float)Math.sqrt(d);
            }
            case Squared: {
                for (int i = 0; i < this.dimensions; ++i) {
                    d += m[i] * m[i];
                }
                return d;
            }
            case Manhattan: {
                for (int i = 0; i < this.dimensions; ++i) {
                    d += Math.abs(m[i]);
                }
                return d;
            }
            case Chebyshev: {
                for (int i = 0; i < this.dimensions; ++i) {
                    d = Math.max(Math.abs(m[i]), d);
                }
                return d;
            }
            case Quadratic: {
                for (int i = 0; i < this.dimensions; ++i) {
                    for (int j = i; j < this.dimensions; ++j) {
                        d += m[i] * m[j];
                    }
                }
                return d;
            }
            case Wiggly: {
                for (int i = 0; i < this.dimensions; ++i) {
                    d = (float)((double)d + Math.pow(Math.abs(m[i]), 15.0));
                }
                return (float)Math.pow(d, 0.06666667014360428);
            }
        }
        return Float.POSITIVE_INFINITY;
    }

    public float get(float xin, float yin, float zin) {
        int i;
        if (this.is2D) {
            throw new UnsupportedOperationException("Cannot create 3D Voronoi basis when instantiated with is2D = true.");
        }
        int[] cell = new int[]{this.fastfloor(xin), this.fastfloor(yin), this.fastfloor(zin)};
        float[] pos = new float[]{xin - (float)cell[0], yin - (float)cell[1], zin - (float)cell[2]};
        int i2 = 0;
        while (i2 < 3) {
            int n = i2++;
            cell[n] = cell[n] % this.size;
        }
        float[] distances = new float[this.level];
        for (i = 0; i < this.level; ++i) {
            distances[i] = Float.MAX_VALUE;
        }
        for (i = 0; i < order3D.length; ++i) {
            boolean possible = true;
            float farDist = distances[this.level - 1];
            if (farDist < Float.MAX_VALUE) {
                for (int j = 0; j < 3; ++j) {
                    if (!(order3D[i][j] < 0 && farDist < pos[j]) && (order3D[i][j] <= 0 || !(farDist < 1.0f - pos[j]))) continue;
                    possible = false;
                    break;
                }
            }
            if (!possible) continue;
            int cx = (order3D[i][0] + cell[0]) % this.size;
            int cy = (order3D[i][1] + cell[1]) % this.size;
            int cz = (order3D[i][2] + cell[2]) % this.size;
            block4: for (int j = 0; j < this.density; ++j) {
                float d = this.distance(this.grid[cx][cy][cz][j], order3D[i], pos);
                for (int k = 0; k < this.level; ++k) {
                    if (!(d < distances[k])) continue;
                    for (int l = this.level - 1; l > k; --l) {
                        distances[l] = distances[l - 1];
                    }
                    distances[k] = d;
                    continue block4;
                }
            }
        }
        return distances[this.level - 1];
    }

    public float get(float xin, float yin) {
        int i;
        if (!this.is2D) {
            throw new UnsupportedOperationException("Cannot create 2D Voronoi basis when instantiated with is2D = false.");
        }
        int[] cell = new int[]{this.fastfloor(xin), this.fastfloor(yin)};
        float[] pos = new float[]{xin - (float)cell[0], yin - (float)cell[1]};
        int i2 = 0;
        while (i2 < 2) {
            int n = i2++;
            cell[n] = cell[n] % this.size;
        }
        float[] distances = new float[this.level];
        for (i = 0; i < this.level; ++i) {
            distances[i] = Float.MAX_VALUE;
        }
        for (i = 0; i < order2D.length; ++i) {
            boolean possible = true;
            float farDist = distances[this.level - 1];
            if (farDist < Float.MAX_VALUE) {
                for (int j = 0; j < this.dimensions; ++j) {
                    if (!(order2D[i][j] < 0 && farDist < pos[j]) && (order2D[i][j] <= 0 || !(farDist < 1.0f - pos[j]))) continue;
                    possible = false;
                    break;
                }
            }
            if (!possible) continue;
            int cx = (order2D[i][0] + cell[0] + this.size) % this.size;
            int cy = (order2D[i][1] + cell[1] + this.size) % this.size;
            block4: for (int j = 0; j < this.density; ++j) {
                float d = this.distance(this.grid[cx][cy][0][j], order2D[i], pos);
                for (int k = 0; k < this.level; ++k) {
                    if (!(d < distances[k])) continue;
                    for (int l = this.level - 1; l > k; --l) {
                        distances[l] = distances[l - 1];
                    }
                    distances[k] = d;
                    continue block4;
                }
            }
        }
        return distances[this.level - 1];
    }

    private int fastfloor(float x) {
        return x > 0.0f ? (int)x : (int)x - 1;
    }

    public static enum DistanceMetric {
        Linear,
        Squared,
        Manhattan,
        Quadratic,
        Chebyshev,
        Wiggly;

    }
}

