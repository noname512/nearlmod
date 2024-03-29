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
import nearlmod.powers.DealCorrosionPower;

public class ArmorlessCleanupSquad extends AbstractMonster {
    public static final String ID = "nearlmod:ArmorlessCleanupSquad";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "resources/nearlmod/images/monsters/armorlesscleanupsquad.png";
    private final int corrosionAmount;

    public ArmorlessCleanupSquad(float x, float y, int level) {
        super(NAME, ID, 80, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.ELITE;
        int dmg1, dmg2;
        if (AbstractDungeon.ascensionLevel >= 18) { dmg1 = 18; dmg2 = 27; }
        else if (AbstractDungeon.ascensionLevel >= 3) { dmg1 = 16; dmg2 = 24; }
        else { dmg1 = 14; dmg2 = 21; }
        damage.add(new DamageInfo(this, MathUtils.floor(dmg1 * (1 + 0.1F * level))));
        damage.add(new DamageInfo(this, MathUtils.floor(dmg2 * (1 + 0.1F * level))));
        int hp;
        if (AbstractDungeon.ascensionLevel >= 8) hp = 90;
        else hp = 100;
        setHp(MathUtils.floor(hp * (1 + 0.1F * level)));
        if (AbstractDungeon.ascensionLevel >= 15)
            corrosionAmount = 20;
        else
            corrosionAmount = 15;
        loadAnimation("resources/nearlmod/images/monsters/enemy_1183_mlasrt_2/enemy_1183_mlasrt_233.atlas", "resources/nearlmod/images/monsters/enemy_1183_mlasrt_2/enemy_1183_mlasrt_233.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new DealCorrosionPower(this, corrosionAmount)));
    }

    @Override
    public void takeTurn() {
        this.state.setAnimation(0, "Attack", false);
        this.state.addAnimation(0, "Idle", true, 0);
        DamageInfo info;
        if (nextMove == 4) info = damage.get(1);
        else info = damage.get(0);
        setMove((byte)(nextMove % 4 + 1), Intent.ATTACK_DEBUFF, info.base);
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new DamageAction(p, info));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK_DEBUFF, damage.get(0).base);
    }
}
