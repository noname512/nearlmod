package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ReactivePower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.RegrowPower;
import nearlmod.powers.DealCorrosionPower;

public class PossessedVeteranJunkman extends AbstractMonster {
    public static final String ID = "nearlmod:PossessedVeteranJunkman";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "resources/nearlmod/images/monsters/roy.png";

    public int hpRestore = 4;

    public PossessedVeteranJunkman(float x, float y) {
        super(NAME, ID, 40, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(40);
            hpRestore = 5;
        }
        if (AbstractDungeon.ascensionLevel >= 17)
            this.damage.add(new DamageInfo(this, 11));
        else if (AbstractDungeon.ascensionLevel >= 2)
            this.damage.add(new DamageInfo(this, 10));
        else
            this.damage.add(new DamageInfo(this, 9));
        loadAnimation("resources/nearlmod/images/monsters/enemy_1183_mlasrt_3/enemy_1183_mlasrt_333.atlas", "resources/nearlmod/images/monsters/enemy_1183_mlasrt_3/enemy_1183_mlasrt_333.json", 1.5F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);
    }

    @Override
    public void usePreBattleAction() {
        addToTop(new ApplyPowerAction(this, this, new RegenPower(this, hpRestore)));
    }

    @Override
    public void takeTurn() {
        addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }
}
