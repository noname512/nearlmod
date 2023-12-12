package nearlmod.monsters;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import nearlmod.powers.DealCorrosionPower;

public class Roy extends AbstractMonster {
    public static final String ID = "nearlmod:Roy";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    public static final String IMAGE = "images/monsters/roy.png";
    private boolean said;
    private final int corrosionAmount;

    public Roy(float x, float y) {
        super(NAME, ID, 100, 0, 0, 150.0F, 320.0F, IMAGE, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8)
            setHp(110);
        if (AbstractDungeon.ascensionLevel >= 3)
            this.damage.add(new DamageInfo(this, 19));
        else
            this.damage.add(new DamageInfo(this, 17));
        said = false;
        if (AbstractDungeon.ascensionLevel >= 18)
            corrosionAmount = 40;
        else
            corrosionAmount = 30;
        loadAnimation("images/monsters/enemy_1183_mlasrt_3/enemy_1183_mlasrt_333.atlas", "images/monsters/enemy_1183_mlasrt_3/enemy_1183_mlasrt_333.json", 1.5F);
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
        if (this.nextMove == 2) {
            this.state.setAnimation(0, "Attack", false);
            this.state.addAnimation(0, "Idle", true, 0);
            addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0)));
            addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true)));
        } else if (!said && AbstractDungeon.aiRng.random(0, 3) == 0) {
            AbstractMonster ms = AbstractDungeon.getMonsters().getMonster("nearlmod:Monique");
            if (ms != null && !ms.isDeadOrEscaped()) {
                addToBot(new TalkAction(ms, DIALOG[0], 0.3F, 3.0F));
                addToBot(new TalkAction(this, DIALOG[1], 0.3F, 3.0F));
                said = true;
                setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
                return;
            }
        }
        int num = 4;
        if (AbstractDungeon.ascensionLevel >= 17) {
            num = 3;
        }
        if (AbstractDungeon.aiRng.random(0, num) == 0)
            setMove((byte)2, Intent.ATTACK_DEBUFF, this.damage.get(0).base);
        else
            setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
    }

    @Override
    protected void getMove(int i) {
        setMove(MOVES[0], (byte)1, Intent.UNKNOWN);
    }
}
