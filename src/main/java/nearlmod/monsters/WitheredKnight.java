package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import nearlmod.powers.DoubleBossPower;

public class WitheredKnight extends AbstractMonster {
    public static final String ID = "nearlmod:WitheredKnight";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/witheredknight.png";

    public boolean isCorruptedDead = false;
    public int SkillTimes = 3;

    public WitheredKnight(float x, float y) {
        super(NAME, ID, 130, 25.0F, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9)
            setHp(145);
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.damage.add(new DamageInfo(this, 10));
            this.damage.add(new DamageInfo(this, 16));
            SkillTimes = 4;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, 9));
            this.damage.add(new DamageInfo(this, 14));
        } else {
            this.damage.add(new DamageInfo(this, 8));
            this.damage.add(new DamageInfo(this, 12));
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractMonster ms = AbstractDungeon.getMonsters().getMonster("nearlmod:CorruptKnight");
        if (ms != null)
            addToBot(new TalkAction(ms, DIALOG[1], 0.3F, 3.0F));
        addToBot(new TalkAction(this, DIALOG[0], 0.3F, 3.0F));
        addToBot(new ApplyPowerAction(this, this, new DoubleBossPower(this)));
    }

    @Override
    public void takeTurn() {
        addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true)));
        int attTimes = 1;
        if (isCorruptedDead) attTimes++;
        if (this.nextMove == 2) {
            for (int i = 0; i < SkillTimes * attTimes; i++) {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
            }
            setMove((byte) 3, Intent.ATTACK_DEBUFF, this.damage.get(0).base, attTimes, (attTimes > 1));
        } else {
            for (int i = 0; i < attTimes; i++) {
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            }
            if ((this.nextMove != 7) && (this.nextMove != 1)) {
                setMove((byte) (this.nextMove + 1), Intent.ATTACK_DEBUFF, this.damage.get(0).base, attTimes, (attTimes > 1));
            } else {
                setMove(MOVES[0], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(1).base, SkillTimes * attTimes, true);
            }
        }
    }

    public void die() {
        super.die();
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            if (ms instanceof CorruptKnight && !ms.isDeadOrEscaped()) {
                ms.getPower("nearlmod:DoubleBoss").onSpecificTrigger();
                ((CorruptKnight) ms).WhitheredDead();
            }
    }

    public void CorruptedDead() {
        isCorruptedDead = true;
        addToBot(new TalkAction(this, DIALOG[2], 0.3F, 3.0F));
        if (this.nextMove == 2) {
            setMove(MOVES[0], (byte) 2, Intent.ATTACK_DEBUFF, this.damage.get(1).base, SkillTimes * 2, true);
            this.createIntent();
        }
        else {
            setMove(this.nextMove, Intent.ATTACK_DEBUFF, this.damage.get(0).base, 2, true);
            this.createIntent();
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte) 1, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
    }
}
