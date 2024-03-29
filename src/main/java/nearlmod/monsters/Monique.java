package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.powers.DealCorrosionPower;

public class Monique extends AbstractMonster {
    public static final String ID = "nearlmod:Monique";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "resources/nearlmod/images/monsters/monique.png";
    private final int corrosionAmount;

    public Monique(float x, float y) {
        super(NAME, ID, 100, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8)
            setHp(110);
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.damage.add(new DamageInfo(this, 11));
            this.damage.add(new DamageInfo(this, 17));
        } else {
            this.damage.add(new DamageInfo(this, 10));
            this.damage.add(new DamageInfo(this, 15));
        }
        if (AbstractDungeon.ascensionLevel >= 18)
            corrosionAmount = 20;
        else
            corrosionAmount = 15;
        loadAnimation("resources/nearlmod/images/monsters/enemy_1182_flasrt_3/enemy_1182_flasrt_333.atlas", "resources/nearlmod/images/monsters/enemy_1182_flasrt_3/enemy_1182_flasrt_333.json", 1.5F);
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
        if (this.nextMove == 4) {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1)));
            setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
        } else {
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            if (this.nextMove == 3)
                setMove((byte)4, Intent.ATTACK, this.damage.get(1).base);
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
