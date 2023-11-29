package nearlmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.powers.AttackUpPower;
import nearlmod.powers.DuelPower;
import nearlmod.powers.ExsanguinationPower;
import nearlmod.powers.RebornPower;

import java.util.Iterator;

public class BloodKnight extends AbstractMonster {
    public static final String ID = "nearlmod:BloodKnight";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "images/monsters/bloodknight.png";

    public int debuffTimes = 2;
    public int ExsanguinationTurn = 3;
    public int BladeStrength = 0;
    public int currentTurn = 0;
    public boolean isStage2 = false;

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new DuelPower(this, 25)));
        addToBot(new ApplyPowerAction(this, this, new RebornPower(this)));
        AbstractDungeon.getCurrRoom().cannotLose = true;
    }
    public BloodKnight(float x, float y) {
        super(NAME, ID, 300, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 19) {
            damage.add(new DamageInfo(this, 20));
            damage.add(new DamageInfo(this, 12));
            debuffTimes = 3;
        }
        else if (AbstractDungeon.ascensionLevel >= 4) {
            damage.add(new DamageInfo(this, 20));
            damage.add(new DamageInfo(this, 12));
        }
        else {
            damage.add(new DamageInfo(this, 18));
            damage.add(new DamageInfo(this, 12));
        }
        if (AbstractDungeon.ascensionLevel >= 15) {
            BladeStrength = 2;
        }
        if (AbstractDungeon.ascensionLevel >= 9)
            maxHealth = 340;
        currentTurn = 0;
        isStage2 = false;
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (currentHealth <= 0 && !this.halfDead) {

            Iterator s = this.powers.iterator();

            AbstractPower p;
            while(s.hasNext()) {
                p = (AbstractPower)s.next();
                p.onDeath();
            }

            s = AbstractDungeon.player.relics.iterator();

            while(s.hasNext()) {
                AbstractRelic r = (AbstractRelic)s.next();
                r.onMonsterDeath(this);
            }

            if (AbstractDungeon.getCurrRoom().cannotLose) {
                this.halfDead = true;
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, MathUtils.floor(this.maxHealth * 0.4F)));
            }
            this.addToTop(new ClearCardQueueAction());
            this.setMove((byte)50, Intent.UNKNOWN);
            this.createIntent();
            this.applyPowers();
        }
    }

    @Override
    public void takeTurn() {
        // TODO
        AbstractPlayer p = AbstractDungeon.player;
        if (this.nextMove == 1) {
            addToBot(new DamageAction(p, damage.get(0)));
        }
        else if (this.nextMove == 2) {
            addToBot(new DamageAction(p, damage.get(1)));
            addToBot(new ApplyPowerAction(p, this, new WeakPower(p, debuffTimes, true)));
            addToBot(new ApplyPowerAction(p, this, new VulnerablePower(p, debuffTimes, true)));
        }
        else if (this.nextMove == 3) {
            cleanDebuff();
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 3)));
        }
        else if (this.nextMove == 4) {
            cleanDebuff();
            // TODO：调位置！
            addToBot(new SpawnMonsterAction(new BloodBlade(0.0F, 0.0F, BladeStrength), true));
            if (AbstractDungeon.ascensionLevel >= 19) {
                addToBot(new SpawnMonsterAction(new BloodBlade(0.0F, 0.0F, BladeStrength), true));
            }
        }
        else if(this.nextMove == 99) {
            addToBot(new ApplyPowerAction(p, this, new ExsanguinationPower(p)));
        }
        else if (this.currentHealth == this.maxHealth) {
            this.halfDead = false;
            cleanDebuff();
            currentTurn = -1;
        }
        else if (this.nextMove == 55) {
            this.halfDead = false;
            cleanDebuff();
            currentTurn = -1;
            isStage2 = true;
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            addToBot(new RemoveSpecificPowerAction(this, this, "nearlmod:Reborn"));
            addToBot(new ApplyPowerAction(this, this, new DuelPower(this, 25)));
            addToBot(new ApplyPowerAction(this, this, new AttackUpPower(this, 50)));
        }
        else {
            // TODO: 这里也得调位置！
            addToBot(new SpawnMonsterAction(new BloodBlade(0.0F, 0.0F, BladeStrength), true));
            addToBot(new SpawnMonsterAction(new BloodBlade(0.0F, 0.0F, BladeStrength), true));
            if (AbstractDungeon.ascensionLevel >= 19) {
                addToBot(new SpawnMonsterAction(new BloodBlade(0.0F, 0.0F, BladeStrength), true));
            }
            setMove((byte)(this.nextMove+1), Intent.UNKNOWN);
            return;
        }
        currentTurn ++;
        if ((currentTurn == 2) && (!p.hasPower("nearlmod:Exsanguination"))) {
            setMove(MOVES[0], (byte)99, Intent.STRONG_DEBUFF);
        }
        else {
            for (;;) {
                byte next = (byte)AbstractDungeon.aiRng.random(0, 2);
                if ((next == this.nextMove) || ((next == 0) && (this.nextMove > 2))) {
                    continue;
                }
                if (next == 1) {
                    setMove(next, Intent.ATTACK, this.damage.get(0).base);
                }
                else if (next == 2) {
                    setMove(next, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
                }
                else if (next == 0) {
                    if (!isStage2) {
                        setMove((byte)3, Intent.BUFF);
                    }
                    else {
                        setMove((byte)4, Intent.BUFF);
                    }
                }
                break;
            }
        }
    }

    void cleanDebuff() {
        Iterator var2 = this.powers.iterator();

        while (var2.hasNext()) {
            AbstractPower pow = (AbstractPower) var2.next();
            if (pow != null && pow.type == AbstractPower.PowerType.DEBUFF) {
                addToBot(new RemoveSpecificPowerAction(this, this, pow));
            }
        }
    }
    @Override
    protected void getMove(int i) {
        byte next = (byte)AbstractDungeon.aiRng.random(0, 2);
        if (next == 1) {
            setMove(next, Intent.ATTACK, this.damage.get(0).base);
        }
        else if (next == 2) {
            setMove(next, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        }
        else if (next == 0) {
            setMove((byte)3, Intent.BUFF);
        }
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }
}
