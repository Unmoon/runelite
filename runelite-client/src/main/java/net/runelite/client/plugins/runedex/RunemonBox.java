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
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

class RunemonBox extends JPanel
{
	private static final int TITLE_PADDING = 5;

	private final JPanel container = new JPanel();
	@Getter(AccessLevel.PACKAGE)
	private final Runemon runemon;

	RunemonBox(
		final Runemon runemon)
	{
		this.runemon = runemon;

		setLayout(new BorderLayout(0, 1));
		setBorder(new EmptyBorder(5, 0, 0, 0));

		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
		titlePanel.setBorder(new EmptyBorder(7, 7, 7, 7));
		titlePanel.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());

		JLabel IDLabel = new JLabel();
		IDLabel.setFont(FontManager.getRunescapeSmallFont());
		IDLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		IDLabel.setText("#" + runemon.getId());

		JLabel nameLabel = new JLabel();
		nameLabel.setText(runemon.getName());
		nameLabel.setFont(FontManager.getRunescapeSmallFont());
		nameLabel.setForeground(Color.WHITE);
		// Set a size to make BoxLayout truncate the name
		nameLabel.setMinimumSize(new Dimension(1, nameLabel.getPreferredSize().height));

		titlePanel.add(Box.createRigidArea(new Dimension(TITLE_PADDING, 0)));
		titlePanel.add(IDLabel);
		titlePanel.add(Box.createHorizontalGlue());
		titlePanel.add(nameLabel);
		titlePanel.add(Box.createRigidArea(new Dimension(TITLE_PADDING, 0)));

		container.setVisible(false);

		add(titlePanel, BorderLayout.NORTH);
		add(container, BorderLayout.CENTER);
	}

	void rebuild()
	{
		buildItems();
		validate();
		repaint();
	}

	void collapse()
	{
		container.setVisible(false);
	}

	void expand()
	{
		container.setVisible(true);
	}

	boolean isCollapsed()
	{
		return !container.isVisible();
	}

	private void buildItems()
	{
		container.removeAll();

		JPanel slotContainer = new JPanel();
		slotContainer.setLayout(new GridLayout(1, 4, 1, 1));
		slotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		slotContainer.add(createStatsLabel("Combat level", RunedexPanel.COMBAT_ICON, Integer.toString(runemon.getCombatLevel())));
		slotContainer.add(createStatsLabel("Attack speed", RunedexPanel.ATTACK_SPEED_ICON, Integer.toString(runemon.getAttackSpeed())));
		slotContainer.add(createStatsLabel("Max standard hit", RunedexPanel.MAX_HIT_ICON, Integer.toString(runemon.getMaxHit())));
		switch (runemon.getAttackStyle())
		{
			case "Stab":
				slotContainer.add(createStatsLabel("Main attack style", RunedexPanel.STAB_ICON, runemon.getAttackStyle()));
				break;
			case "Slash":
				slotContainer.add(createStatsLabel("Main attack style", RunedexPanel.SLASH_ICON, runemon.getAttackStyle()));
				break;
			case "Crush":
				slotContainer.add(createStatsLabel("Main attack style", RunedexPanel.CRUSH_ICON, runemon.getAttackStyle()));
				break;
			default:
				slotContainer.add(createStatsLabel("Main attack style", RunedexPanel.ATTACK_ICON, runemon.getAttackStyle()));
				break;
		}
		container.add(createTitle("General"));
		container.add(slotContainer);

		slotContainer = new JPanel();
		slotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		slotContainer.add(createStatsLabel("Hitpoints", RunedexPanel.HITPOINTS_ICON, Integer.toString(runemon.getHitpointsLevel())));
		slotContainer.add(createStatsLabel("Attack", RunedexPanel.ATTACK_ICON, Integer.toString(runemon.getAttackLevel())));
		slotContainer.add(createStatsLabel("Strength", RunedexPanel.STRENGTH_ICON, Integer.toString(runemon.getStrengthLevel())));
		slotContainer.add(createStatsLabel("Defence", RunedexPanel.DEFENCE_ICON, Integer.toString(runemon.getDefenceLevel())));
		slotContainer.add(createStatsLabel("Magic", RunedexPanel.MAGIC_ICON, Integer.toString(runemon.getMagicLevel())));
		slotContainer.add(createStatsLabel("Ranged", RunedexPanel.RANGED_ICON, Integer.toString(runemon.getRangedLevel())));
		slotContainer.setLayout(new GridLayout(1, slotContainer.getComponentCount(), 1, 1));
		container.add(createTitle("Combat stats"));
		container.add(slotContainer);


		slotContainer = new JPanel();
		slotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		switch (runemon.getAttackStyle())
		{
			case "Stab":
				slotContainer.add(createStatsLabel("Stab attack bonus", RunedexPanel.STAB_ICON,  (runemon.getAttackBonus() < 0 ? "" : "+") + runemon.getAttackBonus()));
				break;
			case "Slash":
				slotContainer.add(createStatsLabel("Slash attack bonus", RunedexPanel.SLASH_ICON, (runemon.getAttackBonus() < 0 ? "" : "+") + runemon.getAttackBonus()));
				break;
			case "Crush":
				slotContainer.add(createStatsLabel("Crush attack bonus", RunedexPanel.CRUSH_ICON, (runemon.getAttackBonus() < 0 ? "" : "+") + runemon.getAttackBonus()));
				break;
			default:
				slotContainer.add(createStatsLabel("Attack bonus", RunedexPanel.ATTACK_ICON, (runemon.getAttackBonus() < 0 ? "" : "+") + runemon.getAttackBonus()));
				break;
		}
		slotContainer.add(createStatsLabel("Strength bonus", RunedexPanel.STRENGTH_ICON,  (runemon.getStrengthBonus() < 0 ? "" : "+") + runemon.getStrengthBonus()));
		slotContainer.add(createStatsLabel("Magic bonus", RunedexPanel.MAGIC_ICON, (runemon.getMagicBonus() < 0 ? "" : "+") + runemon.getMagicBonus()));
		slotContainer.add(createStatsLabel("Magic strength bonus", RunedexPanel.MAGIC_STRENGTH_ICON,  (runemon.getMagicStrengthBonus() < 0 ? "" : "+") + runemon.getMagicStrengthBonus()));
		slotContainer.add(createStatsLabel("Ranged bonus", RunedexPanel.RANGED_ICON,  (runemon.getRangedBonus() < 0 ? "" : "+") + runemon.getRangedBonus()));
		slotContainer.add(createStatsLabel("Ranged strength bonus", RunedexPanel.RANGED_STRENGTH_ICON,  (runemon.getRangedStrengthBonus() < 0 ? "" : "+") + runemon.getRangedStrengthBonus()));
		slotContainer.setLayout(new GridLayout(1, slotContainer.getComponentCount(), 1, 1));
		container.add(createTitle("Offensive bonuses"));
		container.add(slotContainer);


		slotContainer = new JPanel();
		slotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		slotContainer.add(createStatsLabel("Stab defence bonus", RunedexPanel.STAB_ICON, (runemon.getStabDefence() < 0 ? "" : "+") + runemon.getStabDefence()));
		slotContainer.add(createStatsLabel("Slash defence bonus", RunedexPanel.SLASH_ICON,  (runemon.getSlashDefence() < 0 ? "" : "+") + runemon.getSlashDefence()));
		slotContainer.add(createStatsLabel("Crush defence bonus", RunedexPanel.CRUSH_ICON,  (runemon.getCrushDefence() < 0 ? "" : "+") + runemon.getCrushDefence()));
		slotContainer.add(createStatsLabel("Magic defence bonus", RunedexPanel.MAGIC_ICON,  (runemon.getMagicDefence() < 0 ? "" : "+") + runemon.getMagicDefence()));
		slotContainer.add(createStatsLabel("Ranged defence bonus", RunedexPanel.RANGED_ICON,  (runemon.getRangedDefence() < 0 ? "" : "+") + runemon.getRangedDefence()));

		slotContainer.setLayout(new GridLayout(1, slotContainer.getComponentCount(), 1, 1));
		container.add(createTitle("Defensive bonuses"));
		container.add(slotContainer);

		slotContainer = new JPanel();
		slotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		if (runemon.isSlayerTask()) slotContainer.add(createStatsLabel("Slayer task", RunedexPanel.SLAYER_ICON, ""));
		if (runemon.isSlayerBossTask()) slotContainer.add(createStatsLabel("Slayer boss task", RunedexPanel.SLAYER_BOSS_ICON, ""));
		if (runemon.isPoisonImmune()) slotContainer.add(createStatsLabel("Immune to poison", RunedexPanel.POISON_ICON, ""));
		if (runemon.isCannonImmune()) slotContainer.add(createStatsLabel("Immune to cannon", RunedexPanel.CANNON_ICON, ""));
		if (runemon.isDemonbaneVulnerable()) slotContainer.add(createStatsLabel("Vulnerable to demonbane weapons", RunedexPanel.DEMONBANE_ICON, ""));
		if (runemon.isDragonbaneVulnerable()) slotContainer.add(createStatsLabel("Vulnerable to dragonbane weapons", RunedexPanel.DRAGONBANE_ICON, ""));
		switch (runemon.getVampyreTier())
		{
			case 1:
				slotContainer.add(createStatsLabel("Tier 1 vampyre", RunedexPanel.VAMPYRE_TIER_ONE_ICON, ""));
				break;
			case 2:
				slotContainer.add(createStatsLabel("Tier 2 vampyre", RunedexPanel.VAMPYRE_TIER_TWO_ICON, ""));
				break;
			case 3:
				slotContainer.add(createStatsLabel("Tier 3 vampyre", RunedexPanel.VAMPYRE_TIER_THREE_ICON, ""));
				break;
		}
		if (slotContainer.getComponentCount() > 0)
		{
			slotContainer.setLayout(new GridLayout(1, slotContainer.getComponentCount(), 1, 1));
			container.add(createTitle("Other"));
			container.add(slotContainer);
		}

		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.repaint();
	}

	JLabel createStatsLabel(String tooltip, ImageIcon icon, String text)
	{
		JLabel label = new JLabel();
		label.setVerticalTextPosition(JLabel.BOTTOM);
		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setToolTipText(tooltip);
		label.setIcon(icon);
		label.setText(text);
		label.setFont(FontManager.getRunescapeSmallFont());
		return label;
	}

	JPanel createTitle(String text)
	{
		JLabel label = new JLabel();
		label.setText(text);
		label.setFont(FontManager.getRunescapeSmallFont());
		label.setForeground(Color.WHITE);
		label.setVerticalTextPosition(JLabel.CENTER);
		label.setHorizontalTextPosition(JLabel.CENTER);

		JPanel title = new JPanel();
		title.setBorder(new EmptyBorder(0, 7, 0, 7));
		title.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());
		title.add(label);
		return title;
	}
}
