package net.runelite.client.plugins.runedex;

import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import static net.runelite.api.Constants.CLIENT_TICK_LENGTH;
import static net.runelite.api.MenuAction.SPELL_CAST_ON_NPC;
import net.runelite.api.NPC;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import static net.runelite.api.widgets.WidgetID.MONSTER_EXAMINE_GROUP_ID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;


@Slf4j
@PluginDescriptor(
	name = "RuneDex",
	description = "Gotta catch 'em all!"
)
public class RuneDexPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	private NPC last_npc;

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuAction() == SPELL_CAST_ON_NPC)
		{
			last_npc = client.getCachedNPCs()[event.getId()];
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == MONSTER_EXAMINE_GROUP_ID)
		{
			try
			{
				Thread.sleep(CLIENT_TICK_LENGTH);
				clientThread.invokeLater(() ->
				{
					log.debug("Last NPC: {}", last_npc.getName());
					log.debug("{}", client.getWidget(WidgetInfo.MONSTER_EXAMINE_NAME).getText());
					log.debug("{}", client.getWidget(WidgetInfo.MONSTER_EXAMINE_STATS).getText());
					log.debug("{}", client.getWidget(WidgetInfo.MONSTER_EXAMINE_AGGRESSIVE_STATS).getText());
					log.debug("{}", client.getWidget(WidgetInfo.MONSTER_EXAMINE_DEFENSIVE_STATS).getText());
					log.debug("{}", client.getWidget(WidgetInfo.MONSTER_EXAMINE_OTHER_ATTRIBUTES).getText());
				});

			}
			catch (InterruptedException err)
			{
				log.warn("RuneDex widget loading interrupted!");
			}
		}
	}

}
