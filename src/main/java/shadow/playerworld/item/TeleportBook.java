package shadow.playerworld.item;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookWritten;

public class TeleportBook {
    private static final String BOOK = "TELEPORT_BOOK";

    public static boolean isTeleportBook(Item item) {
        if (!(item instanceof ItemBookWritten)) {
            return false;
        }
        String[] lores = item.getLore();
        if (lores == null || lores.length == 0) {
            return false;
        }
        String book = lores[0];
        return book.equals(BOOK);
    }

    public static Item getTeleportBook(Player p, int chunkX, int chunkZ) {
        ItemBookWritten book = (ItemBookWritten)Item.get((int)387, (Integer)0, (int)1);
        String pos = chunkX + "," + chunkZ;
        book.setLore(BOOK, pos);
        String[] pages = new String[]{"阅读本书进入小宇宙，/pwback退出小宇宙"};
        book.writeBook(p.getName(), "传送之书", pages);
        return book;
    }
}

