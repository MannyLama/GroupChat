package pl.mirotcz.groupchat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mirotcz.groupchat.Group;
import pl.mirotcz.groupchat.GroupChat;
import pl.mirotcz.groupchat.Invitations;
import pl.mirotcz.groupchat.Messages;
import pl.mirotcz.groupchat.Messenger;
import pl.mirotcz.groupchat.Players;

public class InviteCommand {

    public InviteCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player pl = (Player) sender;
            if (GroupChat.getPermissions().hasPermission(pl, "groupchat.guildOwner")) {
                if (Players.getPlayerCurrentGroup(pl.getUniqueId()) != null) {
                    Group senderGroup = Players.getPlayerCurrentGroup(pl.getUniqueId());
                    if (senderGroup.getOwner().equals(pl.getUniqueId())) {
                        if (args.length > 1) {
                            if (Bukkit.getPlayer(args[1]) != null) {
                                if (!senderGroup.isBanned(Bukkit.getPlayer(args[1]))) {
                                    if (!senderGroup.isMember(Bukkit.getPlayer(args[1]))) {
                                        Invitations.addInvitation(Bukkit.getPlayer(args[1]), senderGroup.getID());
                                        Messenger.send(Bukkit.getPlayer(args[1]), Messages.INFO_INVITATION_GET.replaceAll("<group>", senderGroup.getName()));
                                        Messenger.send(sender, Messages.INFO_PLAYER_INVITED.replaceAll("<player>", Bukkit.getPlayer(args[1]).getName()));
                                    } else {
                                        Messenger.send(sender, Messages.INFO_PLAYER_ALREADY_MEMBER);
                                    }
                                } else {
                                    Messenger.send(sender, Messages.INFO_PLAYER_IS_BANNED.replaceAll("<player>", Bukkit.getPlayer(args[1]).getName()));
                                }
                            } else {
                                Messenger.send(sender, Messages.INFO_PLAYER_NOT_ONLINE);
                            }
                        }  else Messenger.send(sender, "You didn't specified a player.");
                    } else {
                        Messenger.send(sender, Messages.INFO_YOU_NOT_OWNER);
                    }

                } else {
                    Messenger.send(sender, Messages.INFO_YOU_NOT_MEMBER);
                }
            } else {
                Messenger.send(pl, Messages.INFO_NO_PERMISSION);
            }
        } else {
            Messenger.send(sender, Messages.INFO_YOU_NOT_PLAYER);
        }
    }
}