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
import java.util.Set;
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

class RunedexPanel extends PluginPanel
{
	private final PluginErrorPanel errorPanel = new PluginErrorPanel();

	private final JPanel runemonContainer = new JPanel();

	private final JPanel titleContainer = new JPanel();

	private final List<Runemon> runedex = new ArrayList<>();
	private final List<RunemonBox> boxes = new ArrayList<>();

	private static final ImageIcon ICON;
	static final ImageIcon COMBAT_ICON;
	static final ImageIcon HITPOINTS_ICON;
	static final ImageIcon MAX_HIT_ICON;
	static final ImageIcon ATTACK_SPEED_ICON;

	static final ImageIcon ATTACK_ICON;
	static final ImageIcon STRENGTH_ICON;
	static final ImageIcon DEFENCE_ICON;
	static final ImageIcon MAGIC_ICON;
	static final ImageIcon MAGIC_STRENGTH_ICON;
	static final ImageIcon RANGED_ICON;
	static final ImageIcon RANGED_STRENGTH_ICON;

	static final ImageIcon STAB_ICON;
	static final ImageIcon SLASH_ICON;
	static final ImageIcon CRUSH_ICON;

	static final ImageIcon POISON_ICON;
	static final ImageIcon VENOM_ICON;
	static final ImageIcon DEMONBANE_ICON;
	static final ImageIcon DRAGONBANE_ICON;
	static final ImageIcon CANNON_ICON;
	static final ImageIcon SLAYER_ICON;
	static final ImageIcon SLAYER_BOSS_ICON;

	static final ImageIcon VAMPYRE_TIER_ONE_ICON;
	static final ImageIcon VAMPYRE_TIER_TWO_ICON;
	static final ImageIcon VAMPYRE_TIER_THREE_ICON;
	static
	{
		final BufferedImage expandedImg = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "icon.png");
		final BufferedImage combatIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Multicombat.png");
		final BufferedImage maxHitIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Damage_hitsplat.png");
		final BufferedImage attackSpeedIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Watch.png");

		final BufferedImage hitpointsIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Hitpoints_icon.png");
		final BufferedImage attackIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Attack_icon.png");
		final BufferedImage strengthIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Strength_icon.png");
		final BufferedImage defenceIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Defence_icon.png");
		final BufferedImage magicIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Magic_icon.png");
		final BufferedImage magicStrengthIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Magic_Damage_icon.png");
		final BufferedImage rangedIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Ranged_icon.png");
		final BufferedImage rangedStrengthIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Ranged_Strength_icon.png");

		final BufferedImage stabIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "White_dagger.png");
		final BufferedImage slashIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "White_scimitar.png");
		final BufferedImage crushIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "White_warhammer.png");

		final BufferedImage poisonIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Poison_hitsplat.png");
		final BufferedImage venomIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Venom_hitsplat.png");
		final BufferedImage demonbaneIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Greater_demon_icon.png");
		final BufferedImage dragonbaneIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Red_dragon_icon.png");
		final BufferedImage cannonIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Cannon_barrels.png");
		final BufferedImage slayerIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Slayer_icon.png");
		final BufferedImage slayerBossIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Like_a_boss.png");

		final BufferedImage vampyreTierOneIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Vampyre_Juvenile_chathead.png");
		final BufferedImage vampyreTierTwoIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Vampyre_Juvinate_chathead.png");
		final BufferedImage vampyreTierThreeIcon = ImageUtil.getResourceStreamFromClass(RunedexPlugin.class, "Vyrewatch_chathead.png");

		ICON = new ImageIcon(expandedImg);
		COMBAT_ICON = new ImageIcon(combatIcon);
		MAX_HIT_ICON = new ImageIcon(maxHitIcon);
		ATTACK_SPEED_ICON = new ImageIcon(attackSpeedIcon);

		HITPOINTS_ICON = new ImageIcon(hitpointsIcon);
		ATTACK_ICON = new ImageIcon(attackIcon);
		STRENGTH_ICON = new ImageIcon(strengthIcon);
		DEFENCE_ICON = new ImageIcon(defenceIcon);
		MAGIC_ICON = new ImageIcon(magicIcon);
		MAGIC_STRENGTH_ICON = new ImageIcon(magicStrengthIcon);
		RANGED_ICON = new ImageIcon(rangedIcon);
		RANGED_STRENGTH_ICON = new ImageIcon(rangedStrengthIcon);

		STAB_ICON = new ImageIcon(stabIcon);
		SLASH_ICON = new ImageIcon(slashIcon);
		CRUSH_ICON = new ImageIcon(crushIcon);

		POISON_ICON = new ImageIcon(poisonIcon);
		VENOM_ICON = new ImageIcon(venomIcon);
		DEMONBANE_ICON = new ImageIcon(demonbaneIcon);
		DRAGONBANE_ICON = new ImageIcon(dragonbaneIcon);
		CANNON_ICON = new ImageIcon(cannonIcon);
		SLAYER_ICON = new ImageIcon(slayerIcon);
		SLAYER_BOSS_ICON = new ImageIcon(slayerBossIcon);

		VAMPYRE_TIER_ONE_ICON = new ImageIcon(vampyreTierOneIcon);
		VAMPYRE_TIER_TWO_ICON = new ImageIcon(vampyreTierTwoIcon);
		VAMPYRE_TIER_THREE_ICON = new ImageIcon(vampyreTierThreeIcon);
	}

	RunedexPanel()
	{
		setBorder(new EmptyBorder(6, 6, 6, 6));
		setBackground(ColorScheme.DARK_GRAY_COLOR);
		setLayout(new BorderLayout());

		JLabel titleIcon = new JLabel();
		titleIcon.setIcon(ICON);

		JLabel titleText = new JLabel();
		titleText.setForeground(Color.WHITE);
		titleText.setText(" Runedex");

		titleContainer.setLayout(new BorderLayout());
		titleContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		titleContainer.setPreferredSize(new Dimension(0, 30));
		titleContainer.setBorder(new EmptyBorder(5, 5, 5, 10));
		titleContainer.setVisible(false);
		titleContainer.add(titleText, BorderLayout.CENTER);
		titleContainer.add(titleIcon, BorderLayout.WEST);

		runemonContainer.setLayout(new BoxLayout(runemonContainer, BoxLayout.Y_AXIS));

		final JPanel layoutPanel = new JPanel();
		layoutPanel.setLayout(new BoxLayout(layoutPanel, BoxLayout.Y_AXIS));
		layoutPanel.add(titleContainer);
		layoutPanel.add(runemonContainer);
		add(layoutPanel, BorderLayout.NORTH);

		// Add error pane
		errorPanel.setContent("Runedex", "Use Monster Examine on monsters<br>to add them to your Runedex!");
		add(errorPanel);
	}

	void add(final Runemon item)
	{
		runedex.add(item);
		buildBox(item);
		rebuild();
	}

	void rebuildFromSet(final Set<Runemon> runemonSet)
	{
		runedex.clear();
		runedex.addAll(runemonSet);
		rebuild();
	}

	private void rebuild()
	{
		SwingUtil.fastRemoveAll(runemonContainer);
		boxes.clear();
		// Sort by ID, smallest first
		runedex.sort((c1, c2) -> c2.getId() - c1.getId());

		if (runedex.isEmpty())
		{
			// Show error view
			add(errorPanel);
			titleContainer.setVisible(false);
		}
		else
		{
			// Show main view
			remove(errorPanel);
			titleContainer.setVisible(true);
		}

		for (Runemon r : runedex)
		{
			buildBox(r);
		}

		boxes.forEach(RunemonBox::rebuild);
		runemonContainer.revalidate();
		runemonContainer.repaint();
	}

	private void buildBox(Runemon runemon)
	{
		// Create box
		final RunemonBox box = new RunemonBox(runemon);

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
				}
			}
		});

		// Add box to panel
		boxes.add(box);
		runemonContainer.add(box, 0);
	}
}
