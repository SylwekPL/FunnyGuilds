package net.dzikoysk.funnyguilds.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dzikoysk.funnyguilds.basic.Guild;
import net.dzikoysk.funnyguilds.basic.User;
import net.dzikoysk.funnyguilds.basic.util.GuildUtils;
import net.dzikoysk.funnyguilds.basic.util.RankManager;
import net.dzikoysk.funnyguilds.basic.util.UserUtils;
import net.dzikoysk.funnyguilds.command.util.Executor;
import net.dzikoysk.funnyguilds.data.Messages;
import net.dzikoysk.funnyguilds.util.StringUtils;

public class ExcInfo implements Executor {

	@Override
	public void execute(CommandSender s, String[] args) {
		Messages msg = Messages.getInstance();
		
		String tag = null;
		if(args.length > 0) tag = args[0];
		else if(s instanceof Player){
			User user = User.get((Player) s);
			if(user.hasGuild()) tag = user.getGuild().getTag();
		}
		
		if(tag == null || tag.isEmpty()){
			s.sendMessage(msg.getMessage("infoTag"));
			return;
		}
		
		if(!GuildUtils.tagExists(tag)){
			s.sendMessage(msg.getMessage("infoExists"));
			return;
		}
		
		Guild guild = GuildUtils.byTag(tag);
		if(guild == null){
			s.sendMessage(msg.getMessage("infoExists"));
			return;
		}
		DateFormat date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Date v = new Date(guild.getValidity());
		
		for(String m : msg.getList("infoList")){
			m = StringUtils.replace(m, "{GUILD}", guild.getName());
			m = StringUtils.replace(m, "{TAG}", guild.getTag());
			m = StringUtils.replace(m, "{OWNER}", guild.getOwner().getName());
			m = StringUtils.replace(m, "{MEMBERS}", StringUtils.toString(UserUtils.getNames(guild.getMembers()), true));
			m = StringUtils.replace(m, "{POINTS}", Integer.toString(guild.getRank().getPoints()));
			m = StringUtils.replace(m, "{KILLS}", Integer.toString(guild.getRank().getKills()));
			m = StringUtils.replace(m, "{DEATHS}", Integer.toString(guild.getRank().getDeaths()));
			m = StringUtils.replace(m, "{RANK}", Integer.toString(RankManager.getInstance().getPosition(guild)));
			m = StringUtils.replace(m, "{VALIDITY}", date.format(v));
			m = StringUtils.replace(m, "{LIVES}", Integer.toString(guild.getLives()));
			if(guild.getAllies().size() > 0)
				m = StringUtils.replace(m, "{ALLIES}", StringUtils.toString(GuildUtils.getNames(guild.getAllies()), true));
			else m = StringUtils.replace(m, "{ALLIES}", "Brak");
			s.sendMessage(m);
		}
		return;
		
	}

}
