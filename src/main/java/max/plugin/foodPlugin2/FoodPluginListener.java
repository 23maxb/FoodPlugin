package max.plugin.foodPlugin2;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class JoinEvent implements org.bukkit.event.Listener {
    public static final String resourcePackURL = "temp";

    @EventHandler
    public void giveResourcePack(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setResourcePack(resourcePackURL);

    }

    @EventHandler
    public void resourcePackResult(PlayerResourcePackStatusEvent event) {
        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            Player player = event.getPlayer();
            player.sendMessage("Your client did not accept the resource pack. If you want to download it later, relog or use this link: ");
            player.sendMessage(resourcePackURL);
        }
    }

}
