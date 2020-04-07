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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class RuneMon
{
	private static final Pattern MONSTER_EXAMINE_PATTERN = Pattern.compile("(?:<br>)?([\\w\\s]+): *(-?\\d+|\\w+)(?:<br>)?");
	private static final Pattern OTHER_ATTRIBUTES_PATTERN = Pattern.compile("(?:<br>- )([\\d\\w \\.]+)");

	@Getter
	private String name;

	@Getter
	private int id;

	@Getter
	private int combatLevel;

	@Getter
	private int hitpointsLevel;

	@Getter
	private int attackLevel;

	@Getter
	private int defenceLevel;

	@Getter
	private int strengthLevel;

	@Getter
	private int magicLevel;

	@Getter
	private int rangedLevel;

	@Getter
	private int maxHit;

	@Getter
	private String attackStyle;

	@Getter
	private int attackSpeed;

	@Getter
	private int attackBonus;

	@Getter
	private int stabBonus;

	@Getter
	private int slashBonus;

	@Getter
	private int crushBonus;

	@Getter
	private int magicBonus;

	@Getter
	private int rangedBonus;

	@Getter
	private int strengthBonus;

	@Getter
	private int rangedStrengthBonus;

	@Getter
	private int magicStrengthBonus;

	@Getter
	private int stabDefence;

	@Getter
	private int slashDefence;

	@Getter
	private int crushDefence;

	@Getter
	private int magicDefence;

	@Getter
	private int rangedDefence;

	@Getter
	private boolean isSlayerTask;

	@Getter
	private boolean isSlayerBossTask;

	@Getter
	private boolean isPoisonImmune;

	@Getter
	private boolean isCannonImmune;

	@Getter
	private boolean isDemonbaneVulnerable;

	@Getter
	private boolean isDragonbaneVulnerable;

	@Getter
	private int vampyreTier;

	RuneMon(
		int id,
		String monsterExamineName,
		String monsterExamineStats,
		String monsterExamineAggressiveStats,
		String monsterExamineDefensiveStats,
		String monsterExamineOtherAttributes
	)
	{
		this.id = id;
		this.name = monsterExamineName;

		Matcher matcher = MONSTER_EXAMINE_PATTERN.matcher(monsterExamineStats);
		while (matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2);

			switch (key) {
				case "Combat level":
					this.combatLevel = Integer.parseInt(value);
					break;
				case "Hitpoints":
					this.hitpointsLevel = Integer.parseInt(value);
					break;
				case "Attack":
					this.attackLevel = Integer.parseInt(value);
					break;
				case "Defence":
					this.defenceLevel = Integer.parseInt(value);
					break;
				case "Strength":
					this.strengthLevel = Integer.parseInt(value);
					break;
				case "Magic":
					this.magicLevel = Integer.parseInt(value);
					break;
				case "Ranged":
					this.rangedLevel = Integer.parseInt(value);
					break;
				case "Max standard hit":
					this.maxHit = Integer.parseInt(value);
					break;
				case "Main attack style":
					this.attackStyle = value;
					break;
				default:
					log.warn("Unknown MONSTER_EXAMINE_STATS key '{}' with value '{}'", key, value);
			}
		}

		matcher = MONSTER_EXAMINE_PATTERN.matcher(monsterExamineAggressiveStats);
		while (matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2);

			switch (key) {
				case "Attack speed":
					this.attackSpeed = Integer.parseInt(value);
					break;
				case "Attack bonus":
					this.attackBonus = Integer.parseInt(value);
					break;
				case "Stab bonus":
					this.stabBonus = Integer.parseInt(value);
					break;
				case "Slash bonus":
					this.slashBonus = Integer.parseInt(value);
					break;
				case "Crush bonus":
					this.crushBonus = Integer.parseInt(value);
					break;
				case "Magic bonus":
					this.magicBonus = Integer.parseInt(value);
					break;
				case "Ranged bonus":
					this.rangedBonus = Integer.parseInt(value);
					break;
				case "Strength bonus":
					this.strengthBonus = Integer.parseInt(value);
					break;
				case "Range str bonus":
					this.rangedStrengthBonus = Integer.parseInt(value);
					break;
				case "Magic str bonus ":
					this.magicStrengthBonus = Integer.parseInt(value);
					break;
				default:
					log.warn("Unknown MONSTER_EXAMINE_AGGRESSIVE_STATS key '{}' with value '{}'", key, value);
			}
		}

		matcher = MONSTER_EXAMINE_PATTERN.matcher(monsterExamineDefensiveStats);
		while (matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2);

			switch (key) {
				case "Stab":
					this.stabDefence = Integer.parseInt(value);
					break;
				case "Slash":
					this.slashDefence = Integer.parseInt(value);
					break;
				case "Crush":
					this.crushDefence = Integer.parseInt(value);
					break;
				case "Magic":
					this.magicDefence = Integer.parseInt(value);
					break;
				case "Ranged":
					this.rangedDefence = Integer.parseInt(value);
					break;
				default:
					log.warn("Unknown MONSTER_EXAMINE_DEFENSIVE_STATS key '{}' with value '{}'", key, value);
			}
		}

		matcher = OTHER_ATTRIBUTES_PATTERN.matcher(monsterExamineOtherAttributes);
		while (matcher.find()) {
			String key = matcher.group(1);

			switch (key) {
				case "Is a slayer monster.":
					this.isSlayerTask = true;
					break;
				case "Is not a slayer monster.":
					this.isSlayerTask = false;
					break;
				case "Is a slayer boss monster.":
					this.isSlayerBossTask = true;
					break;
				case "Immune to poison.":
					this.isPoisonImmune = true;
					break;
				case "Can be poisoned.":
					this.isPoisonImmune = false;
					break;
				case "Immune to cannons.":
					this.isCannonImmune = true;
					break;
				case "Not immune to cannons.":
					this.isCannonImmune = false;
					break;
				case "Vulnerable to dragonbane.":
					this.isDragonbaneVulnerable = true;
					break;
				case "Vulnerable to demonbane.":
					this.isDemonbaneVulnerable = true;
					break;
				case "Is a tier 1 vampyre.":
					this.vampyreTier = 1;
					break;
				case "Is a tier 2 vampyre.":
					this.vampyreTier = 2;
					break;
				case "Is a tier 3 vampyre.":
					this.vampyreTier = 3;
					break;
				default:
					log.warn("Unknown MONSTER_EXAMINE_OTHER_ATTRIBUTES key '{}'", key);
			}
		}
	}

	boolean equals(RuneMon c)
	{
		return c.getName().equals(this.getName()) &&
			c.getId() == this.getId() &&
			c.getCombatLevel() == this.getCombatLevel() &&
			c.getHitpointsLevel() == this.getHitpointsLevel() &&
			c.getAttackLevel() == this.getAttackLevel() &&
			c.getDefenceLevel() == this.getDefenceLevel() &&
			c.getStrengthLevel() == this.getStrengthLevel() &&
			c.getMagicLevel() == this.getMagicLevel() &&
			c.getRangedLevel() == this.getRangedLevel() &&
			c.getMaxHit() == this.getMaxHit() &&
			c.getAttackStyle().equals(this.getAttackStyle()) &&
			c.getAttackSpeed() == this.getAttackSpeed() &&
			c.getAttackBonus() == this.getAttackBonus() &&
			c.getStabBonus() == this.getStabBonus() &&
			c.getSlashBonus() == this.getSlashBonus() &&
			c.getCrushBonus() == this.getCrushBonus() &&
			c.getMagicBonus() == this.getMagicBonus() &&
			c.getRangedBonus() == this.getRangedBonus() &&
			c.getStrengthBonus() == this.getStrengthBonus() &&
			c.getRangedStrengthBonus() == this.getRangedStrengthBonus() &&
			c.getMagicStrengthBonus() == this.getMagicStrengthBonus() &&
			c.getStabDefence() == this.getStabDefence() &&
			c.getSlashDefence() == this.getSlashDefence() &&
			c.getCrushDefence() == this.getCrushDefence() &&
			c.getMagicDefence() == this.getMagicDefence() &&
			c.getRangedDefence() == this.getRangedDefence() &&
			c.isSlayerTask() == this.isSlayerTask() &&
			c.isSlayerBossTask() == this.isSlayerBossTask() &&
			c.isPoisonImmune() == this.isPoisonImmune() &&
			c.isCannonImmune() == this.isCannonImmune() &&
			c.isDemonbaneVulnerable() == this.isDemonbaneVulnerable() &&
			c.isDragonbaneVulnerable() == this.isDragonbaneVulnerable() &&
			c.getVampyreTier() == this.getVampyreTier();
	}
}
