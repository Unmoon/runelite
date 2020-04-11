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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

class RuneDexPanel extends PluginPanel
{
	private static final int MAX_LOOT_BOXES = 500;

	// When there is no loot, display this
	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	// Handle loot boxes
	private final JPanel logsContainer = new JPanel();

	// Details and navigation
	private final JPanel actionsContainer = new JPanel();

	// Individual records for the individual kills this session
	private final List<RuneMon> sessionRecords = new ArrayList<>();
	private final List<RuneMonBox> boxes = new ArrayList<>();

	private final RuneDexPlugin plugin;
	//private final LootTrackerConfig config;

	private static final ImageIcon ICON;
	static
	{
		final BufferedImage expandedImg = ImageUtil.getResourceStreamFromClass(RuneDexPlugin.class, "icon.png");

		ICON = new ImageIcon(expandedImg);
	}

	RuneDexPanel(final RuneDexPlugin plugin)//, final LootTrackerConfig config)
	{
		this.plugin = plugin;

		setBorder(new EmptyBorder(6, 6, 6, 6));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		// Create layout panel for wrapping
		final JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		add(layoutPanel, BorderLayout.NORTH);

		actionsContainer.setLayout(new BorderLayout());
		actionsContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		actionsContainer.setPreferredSize(new Dimension(0, 30));
		actionsContainer.setBorder(new EmptyBorder(5, 5, 5, 10));
		actionsContainer.setVisible(false);

		JLabel titleText = new JLabel();
		titleText.setForeground(Color.WHITE);
		titleText.setText(" Runedex");

		JLabel titleIcon = new JLabel();
		titleIcon.setIcon(ICON);

		actionsContainer.add(titleText, BorderLayout.CENTER);
		actionsContainer.add(titleIcon, BorderLayout.WEST);

		// Create loot boxes wrapper
		logsContainer.setLayout(new BoxLayout(logsContainer, BoxLayout.Y_AXIS));
		layoutPanel.add(actionsContainer);
		layoutPanel.add(logsContainer);

		// Add error pane
		errorPanel.setContent("Runedex", "You haven't caught any!");
		add(errorPanel);
	}

	private boolean isAllCollapsed()
	{
		return boxes.stream()
			.filter(RuneMonBox::isCollapsed)
			.count() == boxes.size();
	}

	/**
	 * Adds a new entry to the plugin.
	 * Creates a subtitle, adds a new entry and then passes off to the render methods, that will decide
	 * how to display this new data.
	 */
	void add(final RuneMon item)
	{
		sessionRecords.add(item);

		RuneMonBox box = buildBox(item);
		box.rebuild();
		rebuild();
	}

	/**
	 * Rebuilds all the boxes from scratch using existing listed records, depending on the grouping mode.
	 */
	private void rebuild()
	{
		SwingUtil.fastRemoveAll(logsContainer);
		boxes.clear();
		sessionRecords.sort((c1, c2) -> c2.getId() - c1.getId());

		for (RuneMon r : sessionRecords)
		{
			buildBox(r);
		}

		boxes.forEach(RuneMonBox::rebuild);
		logsContainer.revalidate();
		logsContainer.repaint();
	}

	/**
	 * This method decides what to do with a new record, if a similar log exists, it will
	 * add its items to it, updating the log's overall price and kills. If not, a new log will be created
	 * to hold this entry's information.
	 */
	private RuneMonBox buildBox(RuneMon record)
	{
		// Show main view
		remove(errorPanel);
		actionsContainer.setVisible(true);

		// Create box
		final RuneMonBox box = new RuneMonBox(record);
		box.addKill(record);

		// Create popup menu
		final JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
		box.setComponentPopupMenu(popupMenu);

		// Create collapse event
		box.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					if (box.isCollapsed())
					{
						box.expand();
					}
					else
					{
						box.collapse();
					}
					//updateCollapseText();
				}
			}
		});

		// Add box to panel
		boxes.add(box);
		logsContainer.add(box, 0);

		if (boxes.size() > MAX_LOOT_BOXES)
		{
			logsContainer.remove(boxes.remove(0));
		}

		return box;
	}
}
