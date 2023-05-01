package shadow.playerworld.pool;

import cn.nukkit.Player;
import shadow.playerworld.PlayerWorld;

public class ChunkTaskEnd
implements BaseTask {
    Player player;
    int[] center;

    public ChunkTaskEnd(Player player, int[] next) {
        this.player = player;
        this.center = next;
    }

    @Override
    public void run() {
        PlayerWorld.instance.world.getChunk(this.center[0], this.center[1]).setBlock(0, 0, 0, 20);
        this.player.sendMessage("您的一个小宇宙已经构建完成");
    }
}

