package nearlmod.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.powers.AttackUpPower;

public class ArmorlessAssassin extends AbstractMonster {
    public static final String ID = "nearlmod:ArmorlessAssassin";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "resources/nearlmod/images/monsters/armorlessassassin.png";
    private final int attackUpAmount;

    public ArmorlessAssassin(float x, float y, int level) {
        super(NAME, ID, 180, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        type = EnemyType.ELITE;
        int dmgVal;
        if (AbstractDungeon.ascensionLevel >= 3) dmgVal = 18;
        else dmgVal = 16;
        damage.add(new DamageInfo(this, MathUtils.floor(dmgVal * (1 + 0.1F * level))));
        int hp;
        if (AbstractDungeon.ascensionLevel >= 8) hp = 200;
        else hp = 180;
        setHp(MathUtils.floor(hp * (1 + 0.1F * level)));
        if (AbstractDungeon.ascensionLevel >= 18)
            attackUpAmount = 80;
        else
            attackUpAmount = 50;
        loadAnimation("resources/nearlmod/images/monsters/enemy_1180_aruass_2/enemy_1180_aruass_2.atlas", "resources/nearlmod/images/monsters/enemy_1180_aruass_2/enemy_1180_aruass_237.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void takeTurn() {
        if (nextMove == 1) {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
            addToBot(new RemoveSpecificPowerAction(this, this, AttackUpPower.POWER_ID));
        } else {
            addToBot(new ApplyPowerAction(this, this, new AttackUpPower(this, attackUpAmount)));
        }
        getMove(0);
    }

    @Override
    protected void getMove(int i) {
        if (AbstractDungeon.aiRng.random(0, 2) == 0) {
            setMove((byte) 1, Intent.ATTACK, damage.get(0).base);
        } else {
            setMove((byte) 2, Intent.BUFF);
        }
    }
}
