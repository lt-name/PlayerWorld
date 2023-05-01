package shadow.playerworld.gui;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBookWritten;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.utils.TextFormat;
import shadow.playerworld.generators.BaseGenerator;
import shadow.playerworld.generators.FakeGenerator;
import shadow.playerworld.pool.TaskPool;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.ZoneManager;
import shadow.playerworld.item.TeleportBook;
import shadow.playerworld.pool.ChunkTask;
import shadow.playerworld.pool.ChunkTaskEnd;

public class GuiHandler
implements Listener {
    @EventHandler
    public void formRespond(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        FormWindow window = event.getWindow();
        if (event.getResponse() == null) {
            return;
        }
        if (window instanceof FormWindowSimple) {
            BaseGenerator bg;
            String title = ((FormWindowSimple)event.getWindow()).getTitle();
            String button = ((FormResponseSimple)event.getResponse()).getClickedButton().getText();
            if (!event.wasClosed() && title.equals("构建小宇宙") && (bg = FakeGenerator.dict.get(button)) != null) {
                if (player.getGamemode() != 1 && player.getExperienceLevel() < 30) {
                    player.sendMessage(TextFormat.RED + String.format("你的经验不足，需要至少%d级的经验", PlayerWorld.instance.config.get("level", 30)));
                    return;
                }
                ItemBookWritten book = (ItemBookWritten)Item.get((int)387, (Integer)0, (int)1);
                book.writeBook("shadow_wind", "temp", new String[]{""});
                if (!player.getInventory().canAddItem((Item)book)) {
                    player.sendMessage(TextFormat.RED + "你需要一个空物品栏位");
                    return;
                }
                if (player.getGamemode() != 1) {
                    int exp = player.getExperience();
                    int level = player.getExperienceLevel() - (Integer)PlayerWorld.instance.config.get("level", (Object)30);
                    player.setExperience(exp, level);
                }
                int[] next = ZoneManager.getNextZone(PlayerWorld.instance);
                Item item = TeleportBook.getTeleportBook(player, next[0], next[1]);
                player.getInventory().addItem(item);
                int radius = 12;
                int minX = next[0] + radius / 2 - radius;
                int maxX = minX + radius;
                int minZ = next[1] + radius / 2 - radius;
                int maxZ = minZ + radius;
                for (int x = minX; x < maxX; ++x) {
                    for (int z = minZ; z < maxZ; ++z) {
                        BaseFullChunk chunk = PlayerWorld.instance.world.getChunk(x, z, true);
                        TaskPool.pool.add(new ChunkTask(x, z, bg, chunk));
                    }
                }
                TaskPool.pool.add(new ChunkTaskEnd(player, next));
                player.sendMessage("您的一个小宇宙已开始构建，这通常要花费3分钟");
            }
        }
    }
}

