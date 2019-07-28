package xiaokai.is666.you.is.sb;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerGameModeChangeEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import xiaokai.tool.Belle;
import xiaokai.tool.Message;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class YouAreFool extends PluginBase implements Listener {
	public static final String MsgName = "Message.yml";
	public static final String ConfigName = "Config.yml";
	public String[] LoadFileName = { MsgName, ConfigName };
	public static YouAreFool mis;
	public static Config config;
	public Message Msg;
	public CheckSB sb;
	public static LinkedHashMap<String, SbByData> SBList = new LinkedHashMap<String, SbByData>();

	@Override
	public boolean onCommand(CommandSender player, Command command, String label, String[] a) {
		if (a == null || a.length < 1)
			return false;
		SbByData data;
		Player player2;
		PlayerInventory in;
		List<String> list;
		switch (a[0].toLowerCase()) {
		case "help":
		case "帮助":
			player.sendMessage(
					"/am help 获取插奸帮助\n/am help /am bc 设置是否启动op创造监测线程\n/am sc <玩家名> 添加或删除可以查看玩家背包的权限\n/am mc <玩家名> 添加或删除切换模式不清楚背包的玩家\n/am op <玩家名> 添加或删除玩家的OP权限\n/am ac <玩家名> 添加或删除可以切换模式的玩家名 \n/am 背包 help 查看背包相关的帮助");
			return true;
		case "bc":
			if (player.isPlayer()) {
				player.sendMessage("§4请在控制台执行此命令！");
				return true;
			}
			config.set("开启后台OP创造白名单检测", !config.getBoolean("开启后台OP创造白名单检测"));
			config.save();
			player.sendMessage("§6您已" + (config.getBoolean("开启后台OP创造白名单检测") ? "§e开启" : "§4关闭") + "§6后台OP创造白名单检测");
			if (config.getBoolean("开启后台OP创造白名单检测")) {
				if (sb != null && !sb.isInterrupted())
					sb.interrupt();
				sb = new CheckSB(this);
				sb.start();
				getLogger().info("§6已启动监测线程...");
			} else {
				if (sb != null && !sb.isInterrupted()) {
					sb.interrupt();
					getLogger().info("§4已停止启动监测线程...");
				}
			}
			return true;
		case "sc":
			if (player.isPlayer()) {
				player.sendMessage("§4请在控制台执行此命令！");
				return true;
			}
			if (a.length < 2) {
				player.sendMessage("§4请输入要添加或删除切换模式免清背包权限的玩家名");
				return true;
			}
			list = config.getList("可以查看背包的白名单") == null ? new ArrayList<String>() : config.getList("可以查看背包的白名单");
			if (list.contains(a[1])) {
				for (int i = 0; i < list.size(); i++)
					if (list.get(i).equals(a[1]))
						list.remove(i);
			} else
				list.add(a[1]);
			config.set("可以查看背包的白名单", list);
			config.save();
			player.sendMessage("§6您" + (list.contains(a[1]) ? "§e添加" : "§2删除") + "§6了§8" + a[1] + "§6的可查看背包权限");
			player2 = getPlayer(a[1]);
			if (player2 != null && player2.isOnline())
				player2.sendMessage(Msg.getMessage("被添加可以查看背包的白名单提示", new String[] { "{Player}" },
						new Object[] { player2.getName() }));
			return true;
		case "mc":
			if (player.isPlayer()) {
				player.sendMessage("§4请在控制台执行此命令！");
				return true;
			}
			if (a.length < 2) {
				player.sendMessage("§4请输入要添加或删除切换模式免清背包权限的玩家名");
				return true;
			}
			list = config.getList("切换模式清背包白名单") == null ? new ArrayList<String>() : config.getList("切换模式清背包白名单");
			if (list.contains(a[1])) {
				for (int i = 0; i < list.size(); i++)
					if (list.get(i).equals(a[1]))
						list.remove(i);
			} else
				list.add(a[1]);
			config.set("切换模式清背包白名单", list);
			config.save();
			player.sendMessage("§6您" + (list.contains(a[1]) ? "§e添加" : "§2删除") + "§6了§8" + a[1] + "§6的切换模式免清背包创造权限");
			player2 = getPlayer(a[1]);
			if (player2 != null && player2.isOnline())
				player2.sendMessage(Msg.getMessage("被添加切换模式清背包白名单提示", new String[] { "{Player}" },
						new Object[] { player2.getName() }));
			return true;
		case "op":
		case "管理":
			if (player.isPlayer()) {
				player.sendMessage("§4请在控制台执行此命令！");
				return true;
			}
			if (a.length < 2) {
				player.sendMessage("§4请输入要添加或删除OP权限的玩家名");
				return true;
			}
			list = config.getList("OP白名单") == null ? new ArrayList<String>() : config.getList("OP白名单");
			if (list.contains(a[1])) {
				for (int i = 0; i < list.size(); i++)
					if (list.get(i).equals(a[1]))
						list.remove(i);
			} else
				list.add(a[1]);
			config.set("OP白名单", list);
			config.save();
			player.sendMessage("§6您" + (list.contains(a[1]) ? "§e添加" : "§2删除") + "§6了§8" + a[1] + "§6的OP创造权限");
			player2 = getPlayer(a[1]);
			if (player2 != null && player2.isOnline())
				player2.sendMessage(
						Msg.getMessage("可以切换OP创造提示", new String[] { "{Player}" }, new Object[] { player2.getName() }));
			return true;
		case "ac":
		case "创造":
			if (player.isPlayer()) {
				player.sendMessage("§4请在控制台执行此命令！");
				return true;
			}
			if (a.length < 2) {
				player.sendMessage("§4请输入要添加或删除创造权限的玩家名");
				return true;
			}
			list = config.getList("创造白名单") == null ? new ArrayList<String>() : config.getList("创造白名单");
			if (list.contains(a[1])) {
				for (int i = 0; i < list.size(); i++)
					if (list.get(i).equals(a[1]))
						list.remove(i);
			} else
				list.add(a[1]);
			config.set("创造白名单", list);
			config.save();
			player.sendMessage("§6您" + (list.contains(a[1]) ? "§e添加" : "§2删除") + "§6了§8" + a[1] + "§6的创造权限");
			player2 = getPlayer(a[1]);
			if (player2 != null && player2.isOnline())
				player2.sendMessage(
						Msg.getMessage("可以切换创造提示", new String[] { "{Player}" }, new Object[] { player2.getName() }));
			return true;
		case "背包":
			if (a.length < 2) {
				player.sendMessage("§4/am 背包 help");
				return false;
			}
			switch (a[1].toLowerCase()) {
			case "帮助":
			case "help":
				if (!player.isPlayer() || Belle.isShopModeW(player.getName())) {
					player.sendMessage(
							"/am 背包 help 查看帮助\n/am 背包 清空 <玩家名> :清空一个玩家或者自己的背包\n/am 背包 设置 <玩家名> 将自己的背包内容设置为一个玩家的背包内容\n/am 背包 查看 <玩家名> 查看一个玩家的背包内容\n 其中除了查看，其他项均可不输入玩家名");
				} else
					player.sendMessage(Tool.getRand(1, 2) == 1 ? "§4就你这破权限很难让我给你办事啊..." : Msg.getMessage("权限不足"));
				return true;
			case "清空":
				if (!player.isPlayer()) {
					if (a.length < 3) {
						player.sendMessage("§4请输入咬设置背包的玩家的名字");
						return true;
					}
				}
				if (!player.isPlayer() || Belle.isShopModeW(player.getName())) {
					if (a.length < 3) {
						player2 = (Player) player;
					} else
						player2 = getPlayer(a[2]);
					if (player2 == null || !player2.isOnline()) {
						player.sendMessage("§4要设置查看的玩家不存在或不在线");
						return true;
					}
					in = player2.getInventory();
					Map<Integer, Item> Items = in.getContents();
					for (Integer i : Items.keySet())
						in.remove(Items.get(i));
					if (!player.getName().equals(player2.getName()))
						player2.sendMessage(Msg.getMessage("背包被清除提示", new String[] { "{Admin}", "{Player}" },
								new String[] { player.getName(), player2.getName() }));
					player.sendMessage(
							"§6您清空了" + (player.getName().equals(player2.getName()) ? "§5自己" : "§8" + player2.getName())
									+ "§6的背包");
				} else
					player.sendMessage(Msg.getMessage("权限不足"));
				return true;
			case "设置":
			case "set":
				if (!player.isPlayer()) {
					player.sendMessage("§4请在游戏内执行此命令");
					return true;
				}
				data = SBList.get(player.getName());
				if (Belle.isShopModeW(player.getName())) {
					if (a.length < 3 && ((data.player2 == null || !data.player2.isOnline()) && !data.isShopPlayerIn)) {
						player.sendMessage("§4请输入咬设置背包的玩家的名字");
						return true;
					}
					player2 = (!(data.player2 == null || !data.player2.isOnline())) ? data.player2 : getPlayer(a[2]);
					if (player2 == null || !player2.isOnline()) {
						player.sendMessage("§4要设置查看的玩家不存在或不在线");
						return true;
					}
					PlayerInventory inventory = ((Player) player).getInventory();
					player2.getInventory().setContents(inventory.getContents());
					player2.sendMessage(Msg.getMessage("背包被设置提醒", new String[] { "{Admin}", "{Player}" },
							new String[] { player.getName(), player2.getName() }));
					inventory.setContents(data.Items);
					data.isShopPlayerIn = false;
					data.Items = null;
					data.player2 = null;
					SBList.put(player.getName(), data);
					player.sendMessage("§6您已设置§4" + player2.getName() + "§6的背包");
				} else
					player.sendMessage(Msg.getMessage("权限不足"));
				return true;
			case "shop":
			case "查看":
				if (!player.isPlayer()) {
					player.sendMessage("§4请在游戏内执行此命令");
					return true;
				}
				data = SBList.get(player.getName());
				if (Belle.isShopModeW(player.getName())) {
					if (a.length < 3) {
						player.sendMessage("§4请输入咬查看的玩家的名字");
						return true;
					}
					player2 = getPlayer(a[2]);
					if (player2 == null || !player2.isOnline()) {
						player.sendMessage("§4该玩家不在线");
						return true;
					}
					in = ((Player) player).getInventory();
					data.Items = in.getContents();
					data.isShopPlayerIn = true;
					data.player2 = player2;
					SBList.put(player.getName(), data);
					in.setContents(player2.getInventory().getContents());
					player.sendMessage("§6已将§9" + player2.getName() + "§6的背包克隆");
					return true;
				} else
					player.sendMessage(Msg.getMessage("权限不足"));
				return true;
			default:
				player.sendMessage("§4/am 背包 help");
				return true;
			}
		default:
			return false;
		}
	}

	@Override
	public void onLoad() {
		getServer().getLogger().info(Tool.getColorFont(this.getName() + "正在加载！"));
		mis = this;
		super.onLoad();
	}

	@Override
	public void onEnable() {
		mis = this;
		(new Belle(this)).start();
		getServer().getPluginManager().registerEvents(this, this);
		config = new Config(new File(getDataFolder(), ConfigName), Config.YAML);
		Msg = new Message(this);
		sb = new CheckSB(this);
		if (config.getBoolean("开启后台OP创造白名单检测"))
			sb.start();
		super.onEnable();
		getServer().getLogger().info(Tool.getColorFont(this.getName() + "启动！作者：帅逼凯     QQ：2508543202"));
	}

	@EventHandler
	public void onCutMode(PlayerGameModeChangeEvent e) {
		if (e.isCancelled())
			return;
		Player player = e.getPlayer();
		if (!Belle.isGameW(player) && (e.getNewGamemode() != 0 && e.getNewGamemode() != 2)) {
			e.setCancelled();
			player.sendMessage(
					Msg.getMessage("切换模式被拒绝的提示", new String[] { "{Player}" }, new Object[] { player.getName() }));
			return;
		}
		if (!Belle.isCutModeW(player)) {
			player.sendMessage(
					Msg.getMessage("切换模式被清除背包的提示", new String[] { "{Player}" }, new Object[] { player.getName() }));
			PlayerInventory in = player.getInventory();
			Map<Integer, Item> Items = in.getContents();
			for (Integer i : Items.keySet()) {
				Item item = Items.get(i);
				if (item.getId() != 0)
					in.remove(item);
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		SBList.put(player.getName(), new SbByData(player));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		if (SBList.containsKey(player.getName()))
			SBList.remove(player.getName());
	}

	private Player getPlayer(String name) {
		Map<UUID, Player> Players = Server.getInstance().getOnlinePlayers();
		for (UUID uuid : Players.keySet()) {
			Player player = Players.get(uuid);
			if (player.getName().equals(name))
				return player;
		}
		return null;
	}
}
