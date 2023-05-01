package shadow.playerworld;

import cn.nukkit.level.Level;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import java.util.Random;

import shadow.playerworld.command.PlayerWorldCreate;
import shadow.playerworld.command.PlayerWorldTest;
import shadow.playerworld.event.EventManager;
import shadow.playerworld.generators.FakeGenerator;
import shadow.playerworld.gui.GuiHandler;
import shadow.playerworld.pool.TaskPool;

public class PlayerWorld extends PluginBase {
    public Config config;
    public static PlayerWorld instance;
    public Level world;
    public static final String WORLD_NAME = "player_world";
    public static final Random RAND = new Random();
    public static final NukkitRandom NRAND = new NukkitRandom();
    public static long seed = -1L;

    public void onEnable() {
        instance = this;
        seed = RAND.nextLong();
        this.config = new Config(this.getDataFolder() + "/worlds.yml", Config.YAML);
        if (!this.config.exists("level")) {
            this.config.set("level", 30);
            this.config.set("cx", 48);
            this.config.set("cy", 48);
            this.config.set("radius", 48);
            this.config.save();
        }
        Generator.addGenerator(FakeGenerator.class, "shadow/playerworld", 1);
        if (!this.getServer().isLevelGenerated(WORLD_NAME)) {
            this.getLogger().info("这是您第一次使用本插件，正在创建主玩家世界（所有宇宙都将位于此世界）");
            this.getServer().generateLevel(WORLD_NAME, seed, Generator.getGenerator("shadow/playerworld"));
        }
        if (!this.getServer().isLevelLoaded(WORLD_NAME)) {
            this.getServer().loadLevel(WORLD_NAME);
        }
        this.world = this.getServer().getLevelByName(WORLD_NAME);
        RAND.setSeed(this.world.getSeed());
        NRAND.setSeed(this.world.getSeed());
        TaskPool.registerTask(this);
        this.getServer().getPluginManager().registerEvents(new GuiHandler(), this);
        this.getServer().getPluginManager().registerEvents(new EventManager(), this);
        this.getServer().getCommandMap().register("pw", new PlayerWorldCreate());
        this.getServer().getCommandMap().register("pwt", new PlayerWorldTest());
    }

    public void onDisable() {
        TaskPool.runAll();
    }

}

