package pl.mirotcz.groupchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import pl.mirotcz.groupchat.commands.BanCommand;
import pl.mirotcz.groupchat.commands.CreateCommand;
import pl.mirotcz.groupchat.commands.DeleteCommand;
import pl.mirotcz.groupchat.commands.HelpCommand;
import pl.mirotcz.groupchat.commands.InfoCommand;
import pl.mirotcz.groupchat.commands.InviteCommand;
import pl.mirotcz.groupchat.commands.JoinCommand;
import pl.mirotcz.groupchat.commands.KickCommand;
import pl.mirotcz.groupchat.commands.LeaveCommand;
import pl.mirotcz.groupchat.commands.ListCommand;
import pl.mirotcz.groupchat.commands.MyGroupsCommand;
import pl.mirotcz.groupchat.commands.ReloadCommand;
import pl.mirotcz.groupchat.commands.SayCommand;
import pl.mirotcz.groupchat.commands.SetOwnerCommand;
import pl.mirotcz.groupchat.commands.SetTypeCommand;
import pl.mirotcz.groupchat.commands.ShowCommand;
import pl.mirotcz.groupchat.commands.SwitchCommand;
import pl.mirotcz.groupchat.commands.UnBanCommand;

public class CommandListener implements Listener, CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("groupchat")) {
			if(GroupChat.getPermissions().hasPermission(sender, "groupchat.user")) {
				if(args.length == 0) {
					new HelpCommand(sender);
				}
				else {
					switch(args[0].toLowerCase()) {
						case "create":
							new CreateCommand(sender, args);
							break;
						case "delete":
							new DeleteCommand(sender, args);
							break;
						case "say":
							new SayCommand(sender, args);
							break;
						case "invite":
							new InviteCommand(sender, args);
							break;
						case "join":
							new JoinCommand(sender, args);
							break;
						case "leave":
							new LeaveCommand(sender, args);
							break;
						case "kick":
							new KickCommand(sender, args);
							break;
						case "info":
							new InfoCommand(sender, args);
							break;
						case "reload":
							new ReloadCommand(sender, args);
							break;
						case "ban":
							new BanCommand(sender, args);
							break;
						case "unban":
							new UnBanCommand(sender, args);
							break;
						case "list":
							new ListCommand(sender, args);
							break;
						case "switch":
							new SwitchCommand(sender, args);
							break;
						case "show":
							new ShowCommand(sender, args);
							break;
						case "setowner":
							new SetOwnerCommand(sender, args);
							break;
						case "settype":
							new SetTypeCommand(sender, args);
							break;
						case "mygroups":
							new MyGroupsCommand(sender);
							break;
						default:
							new HelpCommand(sender);
					}
				}
			}
		}
		return true;
	}
}