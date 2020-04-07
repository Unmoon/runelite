/*
 * Copyright (c) 2018, Unmoon <https://github.com/Unmoon>
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

import com.google.gson.Gson;
import java.util.HashSet;
import java.util.Set;
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
	private static final Gson GSON = new Gson();

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	private NPC last_npc;

	private Set<RuneMon> runedex = new HashSet<>();

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
					if (getRuneMonById(last_npc.getId()) != null)
					{
						log.debug("RuneMon already in collection: {}", last_npc.getName());
						return;
					}

					RuneMon runemon = new RuneMon(
						last_npc,
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_NAME).getText(),
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_STATS).getText(),
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_AGGRESSIVE_STATS).getText(),
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_DEFENSIVE_STATS).getText(),
						client.getWidget(WidgetInfo.MONSTER_EXAMINE_OTHER_ATTRIBUTES).getText()
						);

					//log.debug("{}", GSON.toJson(runemon));
					runedex.add(runemon);
					log.debug("RuneMon added to collection: {}", runemon.getName());
					log.debug("RuneDex has the following RuneMon:");
					for (RuneMon i : runedex)
					{
						log.debug(i.getName());
					}
				});

			}
			catch (InterruptedException err)
			{
				log.warn("RuneDex widget loading interrupted!");
			}
		}
	}

	private RuneMon getRuneMonById(int id)
	{
		for (RuneMon i : runedex)
		{
			if (i.getId() == id) {
				return i;
			}
		}
		return null;
	}
}
