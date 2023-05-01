package shadow.playerworld.pool;

import cn.nukkit.plugin.Plugin;
import java.util.LinkedList;
import java.util.List;
import shadow.playerworld.PlayerWorld;

public class TaskPool {
    public static List<BaseTask> pool = new LinkedList<BaseTask>();

    public static void runOnce() {
        if (pool.isEmpty()) {
            return;
        }
        BaseTask task = pool.get(0);
        task.run();
        pool.remove(0);
    }

    public static void registerTask(PlayerWorld plugin) {
        plugin.getServer().getScheduler().scheduleRepeatingTask(plugin, TaskPool::runOnce, 1);
    }

    public static void runAll() {
        while (!pool.isEmpty()) {
            TaskPool.runOnce();
        }
    }
}

