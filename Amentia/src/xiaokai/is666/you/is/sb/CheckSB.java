package xiaokai.is666.you.is.sb;

import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import xiaokai.tool.Belle;

/**
 * @author Winfxk
 */
public class CheckSB extends Thread {
	private YouAreFool mis;

	public CheckSB(YouAreFool mis) {
		this.mis = mis;
	}

	@Override
	public void run() {
		while (true) {
			if (Thread.currentThread().isInterrupted())
				break;
			int Time = YouAreFool.config.getInt("检查间隔");
			try {
				sleep(((Time < 1) ? 1 : Time) * 1000);
				Map<UUID, Player> Players = Server.getInstance().getOnlinePlayers();
				for (UUID uuid : Players.keySet()) {
					Player player = Players.get(uuid);
					if (player.isOnline()) {
						if (!Belle.isOPW(player) && player.isOp()) {
							player.setOp(false);
							player.sendMessage(mis.Msg.getMessage("设置OP被拒绝提示", new String[] { "{Player}" },
									new Object[] { player.getName() }));
						}
						if (!Belle.isGameW(player) && (player.getGamemode() != 0 && player.getGamemode() != 2)) {
							player.sendMessage(mis.Msg.getMessage("切换模式被拒绝的提示", new String[] { "{Player}" },
									new Object[] { player.getName() }));
							player.setGamemode(0);
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}
}
