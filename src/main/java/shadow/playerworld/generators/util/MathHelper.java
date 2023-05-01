package shadow.playerworld.generators.util;

public class MathHelper {
    public static final float PI = (float)Math.PI;
    private static float[] SIN_TABLE = new float[65536];

    public static float sin(float f) {
        return SIN_TABLE[(int)(f * 10430.38f) & 0xFFFF];
    }

    public static float cos(float f) {
        return SIN_TABLE[(int)(f * 10430.38f + 16384.0f) & 0xFFFF];
    }

    public static float sqrt(float f) {
        return (float)Math.sqrt(f);
    }

    public static float sqrt(double d) {
        return (float)Math.sqrt(d);
    }

    public static int floor(float f) {
        int i = (int)f;
        return f >= (float)i ? i : i - 1;
    }

    public static int floor(double d) {
        int i = (int)d;
        return d >= (double)i ? i : i - 1;
    }

    public static float abs(float f) {
        return f < 0.0f ? -f : f;
    }

    public static double clamp(double f, double f1, double f2) {
        return f < f1 ? f1 : (f > f2 ? f2 : f);
    }

    static {
        for (int i = 0; i < 65536; ++i) {
            MathHelper.SIN_TABLE[i] = (float)Math.sin((double)i * Math.PI * 2.0 / 65536.0);
        }
    }
}

