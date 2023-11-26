package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import nearlmod.actions.SummonOrbAction;
import nearlmod.cards.BraveTheDarkness;
import nearlmod.cards.NightScouringGleam;
import nearlmod.orbs.Blemishine;
import nearlmod.powers.DoubleBossPower;

public class CorruptKnight extends AbstractMonster {
    public static final String ID = "nearlmod:CorruptKnight";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/corruptknight.png";
    public boolean isWhitheredDead = false;

    public CorruptKnight(float x, float y) {
        super(NAME, ID, 130, 25.0F, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9)
            setHp(145);
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.damage.add(new DamageInfo(this, 20));
            this.damage.add(new DamageInfo(this, 99));
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, 20));
            this.damage.add(new DamageInfo(this, 90));
        } else {
            this.damage.add(new DamageInfo(this, 18));
            this.damage.add(new DamageInfo(this, 90));
        }
    }


    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new DoubleBossPower(this)));
        AbstractPlayer p = AbstractDungeon.player;
        if (AbstractDungeon.ascensionLevel < 15) {
            addToBot(new ApplyPowerAction(p, this, new ArtifactPower(p, 2)));
        }
        else {
            addToBot(new ApplyPowerAction(p, this, new ArtifactPower(p, 1)));
        }
        addToBot(new SummonOrbAction(new Blemishine()));
        addToBot(new SummonOrbAction(new Blemishine()));
        AbstractCard card = new NightScouringGleam();
        card.upgrade();
        p.hand.addToHand(card);
        if (AbstractDungeon.ascensionLevel < 15) {
            card = card.makeCopy();
            card.upgrade();
            p.hand.addToHand(card);
        }
        card = new BraveTheDarkness();
        card.upgrade();
        card.rawDescription += " NL 保留 。";
        card.selfRetain = true;
        card.initializeDescription();
        p.hand.addToHand(card);
    }

    @Override
    public void takeTurn() {
        int attTimes = 1;
        if (isWhitheredDead) attTimes++;
        if (this.nextMove == 2) {
            for (int i = 0; i < attTimes; i++) {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
            }
            setMove((byte) 3, Intent.ATTACK, this.damage.get(0).base, attTimes, (attTimes > 1));
        } else {
            for (int i = 0; i < attTimes; i++) {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            }
            if ((this.nextMove != 7) && (this.nextMove != 1)) {
                setMove((byte) (this.nextMove + 1), Intent.ATTACK, this.damage.get(0).base, attTimes, (attTimes > 1));
            } else {
                setMove(MOVES[0], (byte) 2, Intent.ATTACK, this.damage.get(1).base, attTimes, (attTimes > 1));
            }
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
    }

    public void die() {
        super.die();
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            if (ms instanceof WitheredKnight && !ms.isDeadOrEscaped()) {
                ((WitheredKnight) ms).CorruptedDead();
                ms.getPower("nearlmod:DoubleBoss").onSpecificTrigger();
            }
    }

    public void WhitheredDead() {
        isWhitheredDead = true;
        if (this.nextMove == 2) {
            setMove(MOVES[0], (byte) 2, Intent.ATTACK, this.damage.get(1).base, 2, true);
            this.createIntent();
        }
        else {
            setMove(this.nextMove, Intent.ATTACK, this.damage.get(0).base, 2, true);
            this.createIntent();
        }
        // TODO: 让腐败骑士说点什么
    }
}
