package nearlmod.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class BloodBlade extends AbstractMonster {
    public static final String ID = "nearlmod:BloodBlade";
    public static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String IMAGE = "images/monsters/bloodblade.png";
    private float absorbTimer;
    private boolean isAbsorbing;
    private float vx;
    private float vy;

    public BloodBlade(float x, float y) {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(23, 28), 0, 0, 100.0F, 170.0F, IMAGE, x, y);
        type = EnemyType.NORMAL;
        if (AbstractDungeon.ascensionLevel >= 17)
            damage.add(new DamageInfo(this, 10));
        else if (AbstractDungeon.ascensionLevel >= 2)
            damage.add(new DamageInfo(this, 9));
        else
            damage.add(new DamageInfo(this, 8));
        if (AbstractDungeon.ascensionLevel >= 7)
            setHp(28, 33);
        else
            setHp(23, 28);
        isAbsorbing = false;
        loadAnimation("images/monsters/enemy_1186_bldbld/enemy_1186_bldbld.atlas", "images/monsters/enemy_1186_bldbld/enemy_1186_bldbld37.json", 2.0F);
        this.flipHorizontal = true;
        this.stateData.setMix("Idle", "Die", 0.1F);
        this.state.setAnimation(0, "Idle", true);

    }

    public BloodBlade(float x, float y, int str) {
        this(x,y);
        if (str != 0) {
            addToBot(new ApplyPowerAction(this,this, new StrengthPower(this, str)));
        }
    }

    @Override
    public void takeTurn() {
        if (this.nextMove == 2) {
            for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
                if (ms.id.equals("nearlmod:BloodKnight")) {
                    if (AbstractDungeon.ascensionLevel < 17) {
                        AbstractDungeon.actionManager.addToBottom(new HealAction(ms, this, MathUtils.floor(ms.maxHealth * 0.1F)));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new HealAction(ms, this, MathUtils.floor(ms.maxHealth * 0.15F)));
                    }
                    isAbsorbing = true;
                    absorbTimer = 1.5F;
                    vx = (ms.drawX - drawX) / (absorbTimer / Gdx.graphics.getDeltaTime());
                    vy = (ms.drawY - drawY) / (absorbTimer / Gdx.graphics.getDeltaTime());
                    if (ms.drawX > drawX) flipHorizontal = !flipHorizontal;
                    hideHealthBar();
                    addToTop(new ApplyPowerAction(ms, this, new StrengthPower(ms, 3)));
                    return;
                }
        }
        this.state.setAnimation(0, "Attack", false);
        this.state.addAnimation(0, "Idle", true, 0);
        addToBot(new DamageAction(AbstractDungeon.player, damage.get(0)));
        if (AbstractDungeon.ascensionLevel >= 17)
            addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, 1)));
        for (AbstractMonster ms : AbstractDungeon.getCurrRoom().monsters.monsters)
            if (ms.id.equals("nearlmod:BloodKnight")) {
                setMove(MOVES[0], (byte) 2, Intent.UNKNOWN);
                return;
            }
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)1, Intent.ATTACK, damage.get(0).base);
    }

    @Override
    public void updateAnimations() {
        super.updateAnimations();
        if (isAbsorbing) {
            if (absorbTimer != 0.0F) {
                absorbTimer -= Gdx.graphics.getDeltaTime();
                drawX += vx;
                drawY += vy;
            }
            if (absorbTimer <= 0.0F) {
                currentHealth = 0;
                isAbsorbing = false;
                die();
            }
        }
    }
}
