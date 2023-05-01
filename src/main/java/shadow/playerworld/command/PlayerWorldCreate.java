package shadow.playerworld.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import java.util.Map;
import shadow.playerworld.PlayerWorld;
import shadow.playerworld.generators.BaseGenerator;
import shadow.playerworld.generators.FakeGenerator;

public class PlayerWorldCreate extends Command {
    public PlayerWorldCreate() {
        super("pw", "打开创建菜单", "/pw");
    }

    private static void showWf(Player p) {
        FormWindowSimple gui = new FormWindowSimple("构建小宇宙", String.format("构建只属于你自己的小宇宙（需花费%d级经验）", PlayerWorld.instance.config.get("level", 30)));
        for (Map.Entry<String, BaseGenerator> entry : FakeGenerator.dict.entrySet()) {
            gui.addButton(new ElementButton(entry.getKey()));
        }
        p.showFormWindow(gui);
    }

    public boolean execute(CommandSender sender, String string, String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("只有玩家才能使用此指令");
            return true;
        }
        PlayerWorldCreate.showWf((Player)sender);
        return true;
    }
}

