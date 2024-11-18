package nearlmod.monsters;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.combat.DeckPoofEffect;
import nearlmod.actions.EndBattleAction;
import nearlmod.actions.RemoveLastFriendAction;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.friendcards.Sanctuary;
import nearlmod.cards.special.BlockCard;
import nearlmod.characters.Nearl;
import nearlmod.orbs.AbstractFriend;
import nearlmod.orbs.Horn;
import nearlmod.orbs.Penance;
import nearlmod.powers.*;

public class Mandragora extends AbstractMonster {
    public static final String ID = "nearlmod:Mandragora";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "resources/nearlmod/images/monsters/lastkheshig.png";

    private boolean talked;
    public final int gazeNum;

    public Mandragora(float x, float y) {
        super(NAME, ID, 80, 10.0F, 0, 170.0F, 320.0F, IMAGE, x, y);
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
        talked = false;
        loadAnimation("resources/nearlmod/images/monsters/enemy_1185_nmekgt_3/enemy_1185_nmekgt_333.atlas", "resources/nearlmod/images/monsters/enemy_1185_nmekgt_3/enemy_1185_nmekgt_333.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
        addToBot(new ApplyPowerAction(this, this, new RebornPower(this)));
        addToBot(new SummonFriendAction(new Horn()));
        if (AbstractDungeon.ascensionLevel < 15) {
            addToBot(new SummonFriendAction(new Horn()));
        }
    }
    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.nextMove == 99) {
            this.halfDead = false;
            addToBot(new RemoveSpecificPowerAction(this, this, "nearlmod:Reborn"));
            addToBot(new HealAction(this, this, this.maxHealth));
            addToBot(new CanLoseAction());
            if (AbstractDungeon.ascensionLevel >= 15) {
                addToBot(new ApplyPowerAction(this, this , new BeatOfDeathPower(this, 1)));
            }
        } else if (this.nextMove <= 2) {
            addToBot(new DamageAction(p, this.damage.get(0)));
        } else if (this.nextMove == 3) {
            addToBot(new DamageAction(p, this.damage.get(0)));
            addToBot(new DamageAction(p, this.damage.get(0)));
            addToBot(new ApplyPowerAction(p, this, new GazedPower(p, gazeNum)));
        }
        else if (this.nextMove == 4) {
            addToBot(new DamageAction(p, this.damage.get(0)));
            addToBot(new DamageAction(p, this.damage.get(0)));
            addToBot(new RemoveSpecificPowerAction(p, this, GazedPower.POWER_ID));
        }

        if ((nextMove == 99) || (nextMove == 1)) {
            setMove((byte) 2, Intent.ATTACK, this.damage.get(0).base);
        }
        else if (nextMove == 2) {
            setMove((byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
        }
        else if (nextMove == 3) {
            setMove(MOVES[0], (byte) 4, Intent.ATTACK, this.damage.get(0).base, 2, true);
        }
        else if (nextMove == 4) {
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
        } else {
            this.state.setAnimation(0, "Skill_2", false);
            this.state.addAnimation(0, "Idle", true, 0);
        }
    }
    @Override
    protected void getMove(int i) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
    }
}
