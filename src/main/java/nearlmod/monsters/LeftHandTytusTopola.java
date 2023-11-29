package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import nearlmod.actions.SummonOrbAction;
import nearlmod.cards.BraveTheDarkness;
import nearlmod.cards.NightScouringGleam;
import nearlmod.orbs.Blemishine;
import nearlmod.powers.DoubleBossPower;
import nearlmod.powers.LeftHandPower;
import nearlmod.powers.SuperWeakPower;

import java.util.Iterator;

public class LeftHandTytusTopola extends AbstractMonster {
    public static final String ID = "nearlmod:LeftHandTytusTopola";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/corruptknight.png";

    private int weakTimes = 3;

    public LeftHandTytusTopola(float x, float y) {
        super(NAME, ID, 55, 25.0F, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8)
            setHp(60);
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.damage.add(new DamageInfo(this, 11));
            weakTimes = 4;
        } else if (AbstractDungeon.ascensionLevel >= 3) {
            this.damage.add(new DamageInfo(this, 11));
        } else {
            this.damage.add(new DamageInfo(this, 9));
        }
    }


    @Override
    public void usePreBattleAction() {
        AbstractDungeon.getCurrRoom().cannotLose = true;
        addToBot(new ApplyPowerAction(this, this, new LeftHandPower(this)));
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (currentHealth <= 0 && !this.halfDead) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                this.halfDead = true;
            }

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

            this.addToTop(new ClearCardQueueAction());
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, "nearlmod:LeftHand"));
            this.setMove((byte)99, Intent.UNKNOWN);
            this.createIntent();
            this.applyPowers();
        }
    }
    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (this.nextMove == 99) {
            this.halfDead = false;
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
            AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
            if (AbstractDungeon.ascensionLevel >= 15) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this , new StrengthPower(this, 3)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this , new PainfulStabsPower(this)));
            }
            setMove((byte) 2, Intent.STRONG_DEBUFF);
        } else if (this.nextMove == 2) {
            addToBot(new ApplyPowerAction(p, this, new SuperWeakPower(p, weakTimes)));
            setMove((byte) 3, Intent.ATTACK, this.damage.get(0).base);
        } else {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            if ((this.nextMove != 4) && (this.nextMove != 1)) {
                setMove((byte) (this.nextMove + 1), Intent.ATTACK, this.damage.get(0).base);
            } else {
                setMove((byte) 2, Intent.STRONG_DEBUFF);
            }
        }
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.ascensionLevel < 18) {
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
        else {
            setMove((byte) 2, Intent.STRONG_DEBUFF);
        }
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }
}
