package shadow.playerworld.generators.util;

import java.util.Random;

public enum EnumGummy {
    RED(0, "red", 16729392),
    ORANGE(1, "orange", 16751439),
    YELLOW(2, "yellow", 16770403),
    WHITE(3, "white", 16776880),
    GREEN(4, "green", 8446507);

    public static final EnumGummy[] META_LOOKUP;
    public static final EnumGummy[] WORLDGEN_SEQUENCE;
    private final int meta;
    private final String name;
    private final int color;
    private final float[] colorComponentValues;

    static {
        META_LOOKUP = new EnumGummy[EnumGummy.values().length];
        WORLDGEN_SEQUENCE = new EnumGummy[]{RED, ORANGE, YELLOW, GREEN, GREEN, YELLOW, WHITE, YELLOW, ORANGE, RED};
        for (EnumGummy enumgummy : values()) {
            META_LOOKUP[enumgummy.getMetadata()] = enumgummy;
        }
    }

    public int getMaterial() {
        if ("red".equals(this.name)) {
            return 14;
        }
        if ("orange".equals(this.name)) {
            return 1;
        }
        if ("yellow".equals(this.name)) {
            return 4;
        }
        if ("white".equals(this.name)) {
            return 0;
        }
        return 5;
    }

    private EnumGummy(int meta, String name, int color) {
        this.meta = meta;
        this.name = name;
        this.color = color;
        int i = (color & 0xFF0000) >> 16;
        int j = (color & 0xFF00) >> 8;
        int k = color & 0xFF;
        this.colorComponentValues = new float[]{(float)i / 255.0f, (float)j / 255.0f, (float)k / 255.0f};
    }

    public static EnumGummy byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }
        return META_LOOKUP[meta];
    }

    public static EnumGummy getGummyForGeneration(double noise) {
        int i = (int)(noise * 1.6) % WORLDGEN_SEQUENCE.length;
        if (i < 0) {
            i += WORLDGEN_SEQUENCE.length;
        }
        return WORLDGEN_SEQUENCE[i];
    }

    public static EnumGummy random(Random rand) {
        return EnumGummy.byMetadata(rand.nextInt(META_LOOKUP.length));
    }

    public int getMetadata() {
        return this.meta;
    }

    public int getColor() {
        return this.color;
    }

    public float[] getColorComponentValues() {
        return this.colorComponentValues;
    }

    public String getName() {
        return this.name;
    }
}

