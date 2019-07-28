package xiaokai.is666.you.is.sb;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.item.Item;

/**
 * @author Winfxk
 */
public class SbByData {
	public Player player;
	/**
	 * 是否处于查看玩家背包的状态
	 */
	public boolean isShopPlayerIn = false;
	/**
	 * 背包里面的内容
	 */
	public Map<Integer, Item> Items;
	/**
	 * 查看的背包玩家
	 */
	public Player player2;

	public SbByData(Player player) {
		this.player = player;
	}
}
