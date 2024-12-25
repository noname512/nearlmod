package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.actions.SummonFriendAction;
import nearlmod.orbs.Horn;
import nearlmod.powers.*;

public class Mandragora extends AbstractMonster {
    public static final String ID = "nearlmod:Mandragora";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public final int gazeNum;
    public boolean isStage2;

    public Mandragora(float x, float y) {
        super(NAME, ID, 80, 10.0F, 0, 170.0F, 320.0F, null, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9)
            setHp(85);
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.damage.add(new DamageInfo(this, 10));
            gazeNum = 4;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, 10));
            gazeNum = 3;
        } else {
            this.damage.add(new DamageInfo(this, 9));
            gazeNum = 3;
        }
        loadAnimation("resources/nearlmod/images/monsters/enemy_1523_mandra/enemy_1523_mandra33.atlas", "resources/nearlmod/images/monsters/enemy_1523_mandra/enemy_1523_mandra33.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("C1_Idle", "Reborn", 0.1F);
        this.stateData.setMix("Reborn", "C2_Idle_1", 0.1F);
        this.stateData.setMix("C2_Idle_1", "C2_Die_1", 0.1F);
        this.state.setAnimation(0, "C1_Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
        addToBot(new ApplyPowerAction(this, this, new RebornPower(this)));
        addToBot(new SummonFriendAction(new Horn()));
        if (AbstractDungeon.ascensionLevel < 15) {
            addToBot(new SummonFriendAction(new Horn()));
        }
        isStage2 = false;
        addToBot(new TalkAction(this, DIALOG[0], 3.0F, 3.0F));
        addToBot(new TalkAction(this, DIALOG[1], 0.3F, 3.0F));
    }
    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.nextMove == 99) {
            state.setAnimation(0, "Reborn_End", false);
            state.addAnimation(0, "C2_Idle_1", true, 0);
            this.halfDead = false;
            addToBot(new RemoveSpecificPowerAction(this, this, "nearlmod:Reborn"));
            addToBot(new HealAction(this, this, this.maxHealth));
            addToBot(new CanLoseAction());
            if (AbstractDungeon.ascensionLevel >= 15) {
                addToBot(new ApplyPowerAction(this, this , new BeatOfDeathPower(this, 1)));
            }
            isStage2 = true;
        } else if (this.nextMove <= 2) {
            if (!isStage2) {
                state.setAnimation(0, "C1_Attack", false);
                state.addAnimation(0, "C1_Idle", true, 0);
            } else {
                state.setAnimation(0, "C2_Attack_1", false);
                state.addAnimation(0, "C2_Idle_1", true, 0);
            }
            addToBot(new DamageAction(p, this.damage.get(0)));
        } else if (this.nextMove == 3) {
            if (!isStage2) {
                state.setAnimation(0, "C1_Skill_1_Begin", false);
                state.addAnimation(0, "C1_Skill_1_Loop", true, 0);
            } else {
                state.setAnimation(0, "C2_Skill_1_Begin", false);
                state.addAnimation(0, "C2_Skill_1_Loop", true, 0);
            }
            addToBot(new DamageAction(p, this.damage.get(0)));
            addToBot(new DamageAction(p, this.damage.get(0)));
            addToBot(new ApplyPowerAction(p, this, new GazedPower(p, gazeNum)));
        }
        else if (this.nextMove == 4) {
            if (!isStage2) {
                state.setAnimation(0, "C1_Skill_1_End", false);
                state.addAnimation(0, "C1_Idle", true, 0);
            } else {
                state.setAnimation(0, "C2_Skill_1_End", false);
                state.addAnimation(0, "C2_Idle_1", true, 0);
            }
            addToBot(new DamageAction(p, this.damage.get(0)));
            addToBot(new DamageAction(p, this.damage.get(0)));
            addToBot(new RemoveSpecificPowerAction(p, this, GazedPower.POWER_ID));
        }

        if ((nextMove == 99) || (nextMove == 1)) {
            setMove((byte) 2, Intent.ATTACK, this.damage.get(0).base);
        } else if (nextMove == 2) {
            setMove((byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
        } else if (nextMove == 3) {
            setMove(MOVES[0], (byte) 4, Intent.ATTACK, this.damage.get(0).base, 2, true);
        } else if (nextMove == 4) {
            setMove(MOVES[0], (byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (currentHealth <= 0 && !this.halfDead) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                this.halfDead = true;
                for (AbstractPower p : powers)
                    p.onDeath();
                for (AbstractRelic r : AbstractDungeon.player.relics)
                    r.onMonsterDeath(this);
            }

            state.setAnimation(0, "Reborn_Begin", false);
            state.addAnimation(0, "Reborn_Loop", true, 0);
            addToTop(new ClearCardQueueAction());
            setMove((byte)99, Intent.UNKNOWN);
            createIntent();
            applyPowers();
        }
    }
    @Override
    public void die() {
        addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, this, GazedPower.POWER_ID));
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }
    @Override
    protected void getMove(int i) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
    }
}
