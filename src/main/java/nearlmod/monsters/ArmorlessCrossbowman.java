package nearlmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

public class ArmorlessCrossbowman extends AbstractMonster {
    public static final String ID = "nearlmod:ArmorlessCrossbowman";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "images/monsters/armorlesscrossbowman.png";
    private final int frailAmount;

    public ArmorlessCrossbowman(float x, float y, int level) {
        super(NAME, ID, 60, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.ELITE;
        int dmg1, dmg2;
        if (AbstractDungeon.ascensionLevel >= 18) { dmg1 = 11; dmg2 = 13; }
        else if (AbstractDungeon.ascensionLevel >= 3) { dmg1 = 10; dmg2 = 13; }
        else { dmg1 = 9; dmg2 = 12; }
        damage.add(new DamageInfo(this, MathUtils.floor(dmg1 * (1 + 0.1F * level))));
        damage.add(new DamageInfo(this, MathUtils.floor(dmg2 * (1 + 0.1F * level))));
        int hp;
        if (AbstractDungeon.ascensionLevel >= 8) hp = 75;
        else hp = 60;
        setHp(MathUtils.floor(hp * (1 + 0.1F * level)));
        if (AbstractDungeon.ascensionLevel >= 18)
            frailAmount = 2;
        else
            frailAmount = 1;
    }

    @Override
    public void takeTurn() {
        AbstractPlayer p = AbstractDungeon.player;
        if (nextMove == 1) {
            addToBot(new DamageAction(p, damage.get(0)));
        } else {
            addToBot(new DamageAction(p, damage.get(1)));
            addToBot(new ApplyPowerAction(p, this, new FrailPower(p, frailAmount, true)));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.aiRng.random(0, 1) == 0) {
            setMove((byte) 1, Intent.ATTACK, damage.get(0).base);
        } else {
            setMove((byte) 2, Intent.ATTACK_DEBUFF, damage.get(1).base);
        }
    }
}
