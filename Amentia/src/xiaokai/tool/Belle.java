package xiaokai.tool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.Utils;
import xiaokai.is666.you.is.sb.YouAreFool;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Belle {
	private YouAreFool kick;

	public Belle(YouAreFool kick) {
		this.kick = kick;
	}

	public void start() {
		File file = kick.getDataFolder();
		if (!file.exists())
			file.mkdirs();
		for (String FileName : kick.LoadFileName) {
			file = new File(kick.getDataFolder(), FileName);
			if (!file.exists())
				try {
					kick.getLogger().info("§6初始化资源：§c" + FileName);
					InputStream rand = this.getClass().getResourceAsStream("/resources/" + FileName);
					if (rand == null) {
						String QQName = "";
						try {
							QQName = Tool.doPost("http://tool.epicfx.cn/", "s=qs&qq=2508543202");
							if (QQName == null)
								QQName = "";
						} catch (Exception e) {
						}
						kick.getLogger().error("初始化资源包失败！可能是插件已经损坏或被人为修改！请联系作者！" + QQName + "QQ：2508543202 ");
					} else
						Utils.writeFile(file, rand);
				} catch (IOException e) {
					kick.getLogger().error("§4资源初始化失败！请检查！§f" + e.getMessage());
					kick.setEnabled(false);
				}
		}
		DumperOptions dumperOptions = new DumperOptions();
		dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		LinkedHashMap<String, Object> map;
		Yaml yaml = new Yaml(dumperOptions);
		String content;
		for (String FileNae : kick.LoadFileName)
			try {
				kick.getLogger().info("§6正在检查文件" + FileNae);
				content = Utils.readFile(this.getClass().getResourceAsStream("/resources/" + FileNae));
				map = new ConfigSection(yaml.loadAs(content, LinkedHashMap.class));
				Config config = new Config(new File(kick.getDataFolder(), FileNae), Config.YAML);
				Map<String, Object> cg = config.getAll();
				isMap(map, cg, config);
			} catch (IOException e) {
				kick.getLogger().info("§4在检查数据中遇到错误！请尝试删除该文件§9[§d" + FileNae + "§9]\n§f" + e.getMessage());
			}
	}

	public void isMap(Map<String, Object> map, Map<String, Object> cg, Config config) {
		for (String ike : map.keySet())
			if (!cg.containsKey(ike)) {
				cg.put(ike, map.get(ike));
				kick.getLogger().info("§6" + ike + "§4所属的数据错误！已回复默认");
				continue;
			} else if ((cg.get(ike) == null) && !(((cg.get(ike) instanceof Map) || (map.get(ike) instanceof Map))
					|| ((cg.get(ike) instanceof List) && (map.get(ike) instanceof List)
							|| ((cg.get(ike) instanceof String) && (map.get(ike) instanceof String)))
					|| ((map.get(ike) instanceof Integer) && (cg.get(ike) instanceof Integer))
					|| ((map.get(ike) instanceof Boolean) && (cg.get(ike) instanceof Boolean))
					|| ((map.get(ike) instanceof Float) && (cg.get(ike) instanceof Float)))) {
				cg.put(ike, map.get(ike));
				kick.getLogger().info("§6" + ike + "§4属性不匹配！已回复默认");
				continue;
			} else if (map.get(ike) instanceof Map)
				cg.put(ike, icMap((Map<String, Object>) map.get(ike), (Map<String, Object>) cg.get(ike)));
		config.setAll((LinkedHashMap<String, Object>) cg);
		config.save();
	}

	public Map<String, Object> icMap(Map<String, Object> map, Map<String, Object> cg) {
		for (String ike : map.keySet())
			if (!cg.containsKey(ike)) {
				cg.put(ike, map.get(ike));
				kick.getLogger().info("§6" + ike + "§4所属的数据错误！已回复默认");
				continue;
			} else if ((cg.get(ike) == null) && !(((cg.get(ike) instanceof Map) && (map.get(ike) instanceof Map))
					|| ((cg.get(ike) instanceof List) && (map.get(ike) instanceof List)
							|| ((cg.get(ike) instanceof String) && (map.get(ike) instanceof String))))) {
				cg.put(ike, map.get(ike));
				kick.getLogger().info("§6" + ike + "§4属性不匹配！已回复默认");
				continue;
			} else if (map.get(ike) instanceof Map)
				cg.put(ike, icMap((Map<String, Object>) map.get(ike), (Map<String, Object>) cg.get(ike)));
		return cg;
	}

	/**
	 * 判断是否是OP白名单
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isOPW(Player player) {
		return player.isPlayer() ? isOPW(player.getName()) : true;
	}

	/**
	 * 判断是否是OP白名单
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isOPW(String player) {
		List<String> list = YouAreFool.config.getList("OP白名单");
		return (isMain(player) || (list != null ? list : new ArrayList<String>()).contains(player));
	}

	/**
	 * 判断是否是可以自由切换模式
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isGameW(Player player) {
		return player.isPlayer() ? isGameW(player.getName()) : true;
	}

	/**
	 * 判断是否是可以自由切换模式
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isGameW(String player) {
		List<String> list = YouAreFool.config.getList("创造白名单");
		return (isMain(player) || (list != null ? list : new ArrayList<String>()).contains(player));
	}

	/**
	 * 判断是否是可以查看背包白名单
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isShopModeW(Player player) {
		return player.isPlayer() ? isShopModeW(player.getName()) : true;
	}

	/**
	 * 判断是否是可以查看背包白名单
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isShopModeW(String player) {
		List<String> list = YouAreFool.config.getList("可以查看背包的白名单");
		return (isMain(player) || (list != null ? list : new ArrayList<String>()).contains(player));
	}

	/**
	 * 判断是否是模式切换清背包白名单
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isCutModeW(Player player) {
		return player.isPlayer() ? isCutModeW(player.getName()) : true;
	}

	/**
	 * 判断是否是模式切换清背包白名单
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isCutModeW(String player) {
		List<String> list = YouAreFool.config.getList("切换模式清背包白名单");
		return (isMain(player) || (list != null ? list : new ArrayList<String>()).contains(player));
	}

	/**
	 * 查看一个玩家是不是妇猪
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isMain(Player player) {
		return player.isPlayer() ? isMain(player.getName()) : true;
	}

	/**
	 * 查看一个玩家是不是妇猪
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isMain(String player) {
		String SB = YouAreFool.config.getString("服主");
		return (SB != null && SB.equals(player));
	}
}
