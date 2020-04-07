/*
 * Copyright (c) 2020, Unmoon <https://github.com/Unmoon>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.runedex;

import java.awt.image.BufferedImage;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import static net.runelite.api.Constants.CLIENT_TICK_LENGTH;
import net.runelite.api.GameState;
import static net.runelite.api.MenuAction.SPELL_CAST_ON_NPC;
import net.runelite.api.NPC;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.WidgetLoaded;
import static net.runelite.api.widgets.WidgetID.MONSTER_EXAMINE_GROUP_ID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;


@Slf4j
@PluginDescriptor(
	name = "RuneDex",
	description = "Gotta catch 'em all!"
)
public class RuneDexPlugin extends Plugin
{
	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	private RuneDexPanel panel;
	private NavigationButton navButton;

	private RuneDexClient runeDexClient = new RuneDexClient();

	private NPC lastNpc;

	Set<RuneMon> runeDex = new HashSet<>();

	private String authenticationHash;

	@Override
	protected void startUp() throws Exception
	{
		panel = new RuneDexPanel(this);

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "icon.png");

		navButton = NavigationButton.builder()
			.tooltip("Runedex")
			.priority(6)
			.icon(icon)
			.panel(panel)
			.build();
		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN
			&& (authenticationHash == null || !authenticationHash.equals(client.getUsername())))
		{
			// TODO: Hash this or use real auth
			authenticationHash = client.getUsername();
			runeDex = runeDexClient.fetch(authenticationHash);
			SwingUtilities.invokeLater(() ->
			{
				for (RuneMon r : runeDex)
				{
					panel.add(r);
				}
			});
		}
	}

	@Subscribe
	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuAction() == SPELL_CAST_ON_NPC)
		{
			lastNpc = client.getCachedNPCs()[event.getId()];
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
					int lastNpcId = lastNpc.getId();
					String lastNpcName = lastNpc.getName();
					String monsterExamineName = client.getWidget(WidgetInfo.MONSTER_EXAMINE_NAME).getText();

					if (lastNpcName == null || !lastNpcName.equals(monsterExamineName))
					{
						log.warn("Last NPC name does not match Monster Examine name, bailing out!");
						return;
					}

					RuneMon runeMon = new RuneMon(
						lastNpcId,
						monsterExamineName,
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_STATS).getText(),
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_AGGRESSIVE_STATS).getText(),
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_DEFENSIVE_STATS).getText(),
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_OTHER_ATTRIBUTES).getText()
						);

					RuneMon oldRuneMon = getRuneMonById(lastNpcId);
					if (oldRuneMon != null)
					{
						if (oldRuneMon.equals(runeMon))
						{
							log.debug("RuneMon already in collection: {}", runeMon.getName());
							return;
						}
						runeDex.remove(oldRuneMon);
					}

					runeDex.add(runeMon);
					log.debug("RuneMon added to collection: {}", runeMon.getName());

					SwingUtilities.invokeLater(() ->
					{
						panel.add(runeMon);
					});
				});
			}
			catch (InterruptedException err)
			{
				log.warn("RuneDex widget loading interrupted!");
			}
		}
	}

	@Schedule(
		period = 10,
		unit = ChronoUnit.SECONDS,
		asynchronous = true
	)
	public void submit() {
		runeDexClient.submit(runeDex, authenticationHash);
	}

	private RuneMon getRuneMonById(int id)
	{
		for (RuneMon i : runeDex)
		{
			if (i.getId() == id) {
				return i;
			}
		}
		return null;
	}
}
