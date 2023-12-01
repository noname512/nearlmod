package nearlmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class KnightShielder extends AbstractMonster {
    public static final String ID = "nearlmod:KnightShielder";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "images/monsters/knightshielder.png";
    private int blockAmount;

    public KnightShielder(float x, float y, int level) {
        super(NAME, ID, 160, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.ELITE;
        int dmg;
        if (AbstractDungeon.ascensionLevel >= 18) dmg = 9;
        else dmg = 8;
        damage.add(new DamageInfo(this, MathUtils.floor(dmg * (1 + 0.1F * level))));
        int hp;
        if (AbstractDungeon.ascensionLevel >= 8) hp = 180;
        else hp = 160;
        setHp(MathUtils.floor(hp * (1 + 0.1F * level)));
        if (AbstractDungeon.ascensionLevel >= 18) blockAmount = 20;
        else if (AbstractDungeon.ascensionLevel >= 3) blockAmount = 18;
        else blockAmount = 16;
        blockAmount = MathUtils.floor(blockAmount * (1 + 0.1F * level));
    }

    @Override
    public void takeTurn() {
        if (nextMove == 1) {
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        } else {
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
                if (!m.isDeadOrEscaped())
                    addToBot(new GainBlockAction(m, blockAmount));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.aiRng.randomBoolean())
            setMove((byte)1, Intent.ATTACK, damage.get(0).base);
        else
            setMove((byte)2, Intent.DEFEND);
    }
}
