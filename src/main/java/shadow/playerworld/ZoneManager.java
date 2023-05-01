package shadow.playerworld;

public class ZoneManager {
    public static int[] getNextZone(PlayerWorld pw) {
        int x = (Integer)pw.config.get("cx", (Object)48);
        int y = (Integer)pw.config.get("cy", (Object)48);
        int r = (Integer)pw.config.get("radius", (Object)48);
        if (x > 0 && x == y) {
            pw.config.set("cx", x -= 32);
            pw.config.set("cy", y);
            pw.config.set("radius", r);
            pw.config.save();
            return new int[]{x, y};
        }
        if (x < 0 && -x == y) {
            pw.config.set("cx", x);
            pw.config.set("cy", y -= 32);
            pw.config.set("radius", r);
            pw.config.save();
            return new int[]{x, y};
        }
        if (x < 0 && x == y) {
            pw.config.set("cx", (x += 32));
            pw.config.set("cy", y);
            pw.config.set("radius", r);
            pw.config.save();
            return new int[]{x, y};
        }
        if (x > 0 && -y == x) {
            pw.config.set("cx", x);
            pw.config.set("cy", y += 32);
            pw.config.set("radius", r);
            pw.config.save();
            return new int[]{x, y};
        }
        if (y == r) {
            pw.config.set("cx", (x -= 32));
            pw.config.set("cy", y);
            pw.config.set("radius", r);
            pw.config.save();
            return new int[]{x, y};
        }
        if (x == -r) {
            pw.config.set("cx", x);
            pw.config.set("cy", (y -= 32));
            pw.config.set("radius", r);
            pw.config.save();
            return new int[]{x, y};
        }
        if (y == -r) {
            pw.config.set("cx", (x += 32));
            pw.config.set("cy", y);
            pw.config.set("radius", r);
            pw.config.save();
            return new int[]{x, y};
        }
        if (x == r) {
            if ((y += 32) == r) {
                x += 32;
                y += 32;
                r += 32;
            }
            pw.config.set("cx", x);
            pw.config.set("cy", y);
            pw.config.set("radius", r);
            pw.config.save();
            return new int[]{x, y};
        }
        return null;
    }
}

