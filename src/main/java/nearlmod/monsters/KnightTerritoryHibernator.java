package nearlmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;

public class KnightTerritoryHibernator extends AbstractMonster {
    public static final String ID = "nearlmod:KnightTerritoryHibernator";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "images/monsters/knightterritoryhibernator.png";
    private boolean asleep;

    public KnightTerritoryHibernator(float x, float y, int level) {
        super(NAME, ID, 180, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.ELITE;
        int dmg;
        if (AbstractDungeon.ascensionLevel >= 18) dmg = 18;
        else if (AbstractDungeon.ascensionLevel >= 3) dmg = 17;
        else dmg = 16;
        damage.add(new DamageInfo(this, MathUtils.floor(dmg * (1 + 0.1F * level))));
        int hp;
        if (AbstractDungeon.ascensionLevel >= 7) hp = 200;
        else hp = 180;
        setHp(MathUtils.floor(hp * (1 + 0.1F * level)));
        asleep = true;
    }

    @Override
    public void usePreBattleAction() {
    }

    @Override
    public void takeTurn() {
        if (nextMove == 1)
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        // 真的不考虑写一个 if 没有队友 && 自己还睡着 then 直接获取奖励吗（x）
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        if (asleep)
            setMove((byte)2, Intent.SLEEP);
        else
            setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (asleep && currentHealth < maxHealth) {
            asleep = false;
            setMove((byte)3, Intent.STUN);
            createIntent();
        }
    }
}
