package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

public class Monique extends AbstractMonster {
    public static final String ID = "nearlmod:Monique";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/monique.png";

    public Monique(float x, float y) {
        super(NAME, ID, 100, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7)
            setHp(110);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.damage.add(new DamageInfo(this, 11));
            this.damage.add(new DamageInfo(this, 17));
        } else {
            this.damage.add(new DamageInfo(this, 10));
            this.damage.add(new DamageInfo(this, 15));
        }
        this.flipHorizontal = true;
    }

    @Override
    public void takeTurn() {
        if (this.nextMove == 4) {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true)));
            setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
        } else {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            if (this.nextMove == 3)
                setMove((byte)4, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
            else
                setMove((byte)(this.nextMove + 1), Intent.ATTACK, this.damage.get(0).base);
        }
    }

    @Override
    public void die() {
        for (AbstractMonster ms : AbstractDungeon.getMonsters().monsters)
            if (ms instanceof Roy && !ms.isDeadOrEscaped()) {
                addToBot(new EscapeAction(ms));
            }
        super.die();
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
    }
}
