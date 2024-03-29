package nearlmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;

public class UndertideGloompincer extends AbstractMonster {
    public static final String ID = "nearlmod:UndertideGloompincer";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "resources/nearlmod/images/monsters/undertidegloompincer.png";
    int blockNum = 6;

    public UndertideGloompincer(float x, float y, int level) {
        super(NAME, ID, 60, 0, 0, 150.0F, 220.0F, IMAGE, x, y);
        type = EnemyType.NORMAL;
        int dmg;
        if (AbstractDungeon.ascensionLevel >= 17) dmg = 12;
        else if (AbstractDungeon.ascensionLevel >= 2) dmg = 11;
        else dmg = 10;
        damage.add(new DamageInfo(this, MathUtils.floor(dmg * (1 + 0.1F * level))));
        int hp;
        if (AbstractDungeon.ascensionLevel >= 7) hp = 65;
        else hp = 60;
        setHp(MathUtils.floor(hp * (1 + 0.1F * level)));
        if (AbstractDungeon.ascensionLevel >= 7) blockNum = 8;
        blockNum = MathUtils.floor(blockNum * (1 + 0.1F * level));
        loadAnimation("resources/nearlmod/images/monsters/enemy_1178_dscorp_2/enemy_1178_dscorp_233.atlas", "resources/nearlmod/images/monsters/enemy_1178_dscorp_2/enemy_1178_dscorp_233.json", 2.0F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new ApplyPowerAction(this, this, new MetallicizePower(this, blockNum)));
        addToBot(new GainBlockAction(this, this, blockNum));
    }

    @Override
    public void takeTurn() {
        this.state.setAnimation(0, "Attack", false);
        this.state.addAnimation(0, "Idle", true, 0);
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }
}
