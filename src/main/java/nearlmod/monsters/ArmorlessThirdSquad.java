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
import nearlmod.powers.CorrosionDamagePower;
import nearlmod.powers.DealCorrosionPower;

public class ArmorlessThirdSquad extends AbstractMonster {
    public static final String ID = "nearlmod:ArmorlessThirdSquad";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "images/monsters/armorlessthirdsquad.png";
    private final int corrosionAmount;

    public ArmorlessThirdSquad(float x, float y, int level) {
        super(NAME, ID, 100, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.ELITE;
        int dmg1, dmg2;
        if (AbstractDungeon.ascensionLevel >= 18) { dmg1 = 10; dmg2 = 15; }
        else if (AbstractDungeon.ascensionLevel >= 3) { dmg1 = 9; dmg2 = 13; }
        else { dmg1 = 8; dmg2 = 12; }
        damage.add(new DamageInfo(this, MathUtils.floor(dmg1 * (1 + 0.1F * level))));
        damage.add(new DamageInfo(this, MathUtils.floor(dmg2 * (1 + 0.1F * level))));
        int hp;
        if (AbstractDungeon.ascensionLevel >= 8) hp = 110;
        else hp = 100;
        setHp(MathUtils.floor(hp * (1 + 0.1F * level)));
        if (AbstractDungeon.ascensionLevel >= 15)
            corrosionAmount = 20;
        else
            corrosionAmount = 15;
    }

    @Override
    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new DealCorrosionPower(this, corrosionAmount)));
    }

    @Override
    public void takeTurn() {
        DamageInfo info;
        if (nextMove == 4) info = damage.get(1);
        else info = damage.get(0);
        setMove((byte)(nextMove % 4 + 1), Intent.ATTACK_DEBUFF, info.base, 2, true);
        AbstractPlayer p = AbstractDungeon.player;
        for (int i = 1; i <= 2; i++) {
            addToBot(new DamageAction(p, info));
            addToBot(new ApplyPowerAction(p, this, new CorrosionDamagePower(p, info.output * corrosionAmount)));
        }
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK_DEBUFF, damage.get(0).base, 2, true);
    }
}
