package net.dzikoysk.funnyguilds.util.element;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.dzikoysk.funnyguilds.basic.User;
import net.dzikoysk.funnyguilds.util.Parser;
import net.dzikoysk.funnyguilds.util.StringUtils;
import net.dzikoysk.funnyguilds.util.Ticking;

public class PlayerList {

	private final User user;
	private boolean init;
	private String[] ss;
	private String[] prefix;
	private String[] suffix;
	
	public PlayerList(User user){
		this.user = user;
		this.ss = PlayerListScheme.getScheme();
		this.prefix = new String[60];
		this.suffix = new String[60];
		user.setPlayerList(this);
	}

	private void split(){
		this.prefix = new String[60];
		this.suffix = new String[60];
		for(int i = 0; i < 60; i++){
			if(ss[i] == null || ss[i].isEmpty()) continue;
			String[] s = ss[i].split("(?<=\\G................)");
			if(s.length >= 2){
				prefix[i] = s[0];
				String color = ChatColor.getLastColors(prefix[i]);
				if(color == null || color.isEmpty()) color = "�f";
				String sx = color + s[1];
				if(sx.length() > 15) suffix[i] = sx.substring(0, 15);
				else suffix[i] = sx;
			}else if(s.length == 1){
				prefix[i] = s[0];
				suffix[i] = "";
			}
		}
	}
	
	private void update(){
		this.ss = PlayerListScheme.getScheme();
		Calendar now = Calendar.getInstance();
		int second = now.get(Calendar.SECOND);
		int minute = now.get(Calendar.MINUTE);
		for(int i : PlayerListScheme.getEdit()){
			String s = ss[i];
			if(s == null || s.isEmpty()) continue;
			if(second < 10) s = StringUtils.replace(s, "{SECOND}", '0' + Integer.toString(second));
			else s = StringUtils.replace(s, "{SECOND}", Integer.toString(second));
			if(minute < 10) s = StringUtils.replace(s, "{MINUTE}", '0' + Integer.toString(minute));
			else s = StringUtils.replace(s, "{MINUTE}", Integer.toString(minute));
			if(user.hasGuild()){
				s = StringUtils.replace(s, "{GUILD}", user.getGuild().getName());
				s = StringUtils.replace(s, "{TAG}", user.getGuild().getTag());
			}else{
				s = StringUtils.replace(s, "{GUILD}", "Brak");
				s = StringUtils.replace(s, "{TAG}", "Brak");
			}
			s = StringUtils.replace(s, "{ONLINE}", Integer.toString(Bukkit.getOnlinePlayers().length));
			s = StringUtils.replace(s, "{PLAYER}", user.getName());
			s = StringUtils.replace(s, "{TPS}", Double.toString(Ticking.getTPS()));
			s = StringUtils.replace(s, "{PING}", Integer.toString(user.getPing()));
			s = StringUtils.replace(s, "{POINTS}", Integer.toString(user.getRank().getPoints()));
			s = StringUtils.replace(s, "{KILLS}", Integer.toString(user.getRank().getKills()));
			s = StringUtils.replace(s, "{DEATHS}", Integer.toString(user.getRank().getDeaths()));
			s = StringUtils.replace(s, "{HOUR}", Integer.toString(now.get(Calendar.HOUR_OF_DAY)));
			String r = Parser.parseRank(s);
			if(r != null) s = r;
			ss[i] = s;
		}
	}
	
	public void send(){
		Player player = Bukkit.getPlayer(user.getName());
		if(player == null) return;
		update();
		split();
		PlayerListManager.send(player);
	}
	
	public void init(boolean init){
		this.init = init;
	}

	public String[] getPrefix(){
		return prefix.clone();
	}
	
	public String[] getSuffix(){
		return suffix.clone();
	}
	
	public boolean getInit(){
		return init;
	}
}
