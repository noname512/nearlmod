package nearlmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class BloodBlade extends AbstractMonster {
    public static final String ID = "nearlmod:BloodBlade";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "images/monsters/bloodblade.png";

    public BloodBlade(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(23, 28), 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 18)
            damage.add(new DamageInfo(this, 10));
        else if (AbstractDungeon.ascensionLevel >= 2)
            damage.add(new DamageInfo(this, 9));
        else
            damage.add(new DamageInfo(this, 8));
        if (AbstractDungeon.ascensionLevel >= 7)
            setHp(28, 33);
        else
            setHp(23, 28);
    }

    @Override
    public void takeTurn() {
        if (this.nextMove == 2) {
            for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
                if (ms.id.equals("nearlmod:BloodKnight")) {
                    ms.heal(MathUtils.floor(ms.maxHealth * 0.1F), true);
                    addToTop(new ApplyPowerAction(ms, this, new StrengthPower(ms, 3)));
                    die(false);
                    return;
                }
        }
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        if (AbstractDungeon.ascensionLevel >= 17)
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (ms.id.equals("nearlmod:BloodKnight")) {
                setMove(MOVES[0], (byte) 2, Intent.UNKNOWN);
                return;
            }
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }
}
