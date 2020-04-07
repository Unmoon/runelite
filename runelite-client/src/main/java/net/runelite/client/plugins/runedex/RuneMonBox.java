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

import com.google.common.base.Strings;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.util.Text;

class RuneMonBox extends JPanel
{
	private static final int ITEMS_PER_ROW = 5;
	private static final int TITLE_PADDING = 5;

	private final JPanel itemContainer = new JPanel();
	private final JPanel logTitle = new JPanel();
	private final JLabel subTitleLabel = new JLabel();
	@Getter(AccessLevel.PACKAGE)
	private final RuneMon runeMon;

	@Getter
	private final List<RuneMon> items = new ArrayList<>();


	RuneMonBox(
		final RuneMon runeMon)
	{
		this.runeMon = runeMon;

		setLayout(new BorderLayout(0, 1));
		setBorder(new EmptyBorder(5, 0, 0, 0));

		logTitle.setLayout(new BoxLayout(logTitle, BoxLayout.X_AXIS));
		logTitle.setBorder(new EmptyBorder(7, 7, 7, 7));
		logTitle.setBackground(ColorScheme.DARKER_GRAY_COLOR.darker());

		JLabel titleLabel = new JLabel();
		titleLabel.setText(runeMon.getName());
		titleLabel.setFont(FontManager.getRunescapeSmallFont());
		titleLabel.setForeground(Color.WHITE);
		// Set a size to make BoxLayout truncate the name
		titleLabel.setMinimumSize(new Dimension(1, titleLabel.getPreferredSize().height));

		subTitleLabel.setFont(FontManager.getRunescapeSmallFont());
		subTitleLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		subTitleLabel.setText(Integer.toString(runeMon.getId()));

		logTitle.add(Box.createRigidArea(new Dimension(TITLE_PADDING, 0)));
		logTitle.add(subTitleLabel);
		logTitle.add(Box.createHorizontalGlue());
		logTitle.add(titleLabel);
		logTitle.add(Box.createRigidArea(new Dimension(TITLE_PADDING, 0)));

		add(logTitle, BorderLayout.NORTH);
	}

	/**
	 * Checks if this box matches specified record
	 *
	 * @param record loot record
	 * @return true if match is made
	 */
	boolean matches(final RuneMon record)
	{
		return record.getId() == runeMon.getId();
	}

	/**
	 * Adds an record's data into a loot box.
	 */
	void addKill(final RuneMon record)
	{
		if (!matches(record))
		{
			throw new IllegalArgumentException(record.toString());
		}

		items.add(record);
	}

	void rebuild()
	{
		buildItems();
		validate();
		repaint();
	}

	void collapse()
	{
		if (!isCollapsed())
		{
			itemContainer.setVisible(false);
			applyDimmer(false, logTitle);
		}
	}

	void expand()
	{
		if (isCollapsed())
		{
			itemContainer.setVisible(true);
			applyDimmer(true, logTitle);
		}
	}

	boolean isCollapsed()
	{
		return !itemContainer.isVisible();
	}

	private void applyDimmer(boolean brighten, JPanel panel)
	{
		for (Component component : panel.getComponents())
		{
			Color color = component.getForeground();

			component.setForeground(brighten ? color.brighter() : color.darker());
		}
	}

	/**
	 * This method creates stacked items from the item list, calculates total price and then
	 * displays all the items in the UI.
	 */
	private void buildItems()
	{
		return;
//		List<RuneMon> items = this.items;
//
//		boolean isHidden = items.isEmpty();
//		setVisible(!isHidden);
//
//		if (isHidden)
//		{
//			return;
//		}
//
//		// Calculates how many rows need to be display to fit all items
//		final int rowSize = ((items.size() % ITEMS_PER_ROW == 0) ? 0 : 1) + items.size() / ITEMS_PER_ROW;
//
//		itemContainer.removeAll();
//		itemContainer.setLayout(new GridLayout(rowSize, ITEMS_PER_ROW, 1, 1));
//
//		for (int i = 0; i < rowSize * ITEMS_PER_ROW; i++)
//		{
//			final JPanel slotContainer = new JPanel();
//			slotContainer.setBackground(ColorScheme.DARKER_GRAY_COLOR);
//
//			if (i < items.size())
//			{
//				final RuneMon item = items.get(i);
//				final JLabel imageLabel = new JLabel();
//				imageLabel.setToolTipText(buildToolTip(item));
//				imageLabel.setVerticalAlignment(SwingConstants.CENTER);
//				imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
//
//				//AsyncBufferedImage itemImage = itemManager.getImage(item.getId(), item.getQuantity(), item.getQuantity() > 1);
//				//itemImage.addTo(imageLabel);
//
//				slotContainer.add(imageLabel);
//
//				// Create popup menu
//				final JPopupMenu popupMenu = new JPopupMenu();
//				popupMenu.setBorder(new EmptyBorder(5, 5, 5, 5));
//				slotContainer.setComponentPopupMenu(popupMenu);
//			}
//
//			itemContainer.add(slotContainer);
//		}
//
//		itemContainer.repaint();
	}

	private static String buildToolTip(RuneMon item)
	{
		return item.getName();
	}
}
