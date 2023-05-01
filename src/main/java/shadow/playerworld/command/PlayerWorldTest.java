package shadow.playerworld.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import shadow.playerworld.PlayerWorld;

public class PlayerWorldTest extends Command {
    public PlayerWorldTest() {
        super("pwback", "返回主世界", "/pwback");
    }

    public boolean execute(CommandSender sender, String string, String[] strings) {
        if (sender instanceof Player) {
            Player p = (Player)sender;
            Level level = PlayerWorld.instance.getServer().getLevelByName("world");
            p.teleport(level.getSafeSpawn());
        }
        return true;
    }

    public boolean execute1(CommandSender sender, String string, String[] strings) {
        Player player = (Player)sender;
        Position pos = new Position(768.0, 96.0, 256.0, PlayerWorld.instance.world);
        player.teleport(pos);
        return true;
    }
}

