package shadow.playerworld.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.item.TeleportBook;

public class EventManager implements Listener {
    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        Item item = event.getItem();
        if (!TeleportBook.isTeleportBook(item)) {
            return;
        }
        event.setCancelled(true);
        String[] lores = item.getLore();
        String pos = lores[1];
        try {
            String[] loc = pos.split(",");
            int x = Integer.parseInt(loc[0]);
            int z = Integer.parseInt(loc[1]);
            int material = PlayerWorld.instance.world.getChunk(x, z).getBlockId(0, 0, 0);
            if (material != 20) {
                p.sendMessage("小宇宙未创建完成，请耐心等待");
                return;
            }
            x = x * 16 + 7;
            z = z * 16 + 7;
            int y = PlayerWorld.instance.world.getHighestBlockAt(x, z) + 2;
            Position pp = new Position((double)x, (double)y, (double)z, PlayerWorld.instance.world);
            p.teleport(pp);
            p.sendMessage("使用/pwback命令返回主世界");
        }
        catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
    }
}

