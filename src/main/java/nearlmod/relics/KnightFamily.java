package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class KnightFamily extends CustomRelic {

    public static final String ID = "nearlmod:KnightFamily";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/knightfamily.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/knightfamily_p.png");
    private int status; // 1-未触发 2-触发中 3-已触发
    public KnightFamily() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStartPreDraw() {
        counter = 1;
        status = 1;
    }

    @Override
    public void onPlayerEndTurn() {
        if (status == 2) status = 3;
    }

    @Override
    public int onLoseHpLast(int damageAmount) {
        if (status == 2) {
            flash();
            return 0;
        }
        AbstractPlayer p = AbstractDungeon.player;
        if (status == 3 || damageAmount < p.currentHealth) return damageAmount;
        counter = 0;
        status = 2;
        flash();
        p.decreaseMaxHealth(MathUtils.ceil(p.maxHealth * 0.6F));
        p.heal(p.maxHealth);
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, 3)));
        return 0;
    }

    @Override
    public int compareTo(AbstractRelic arg0) {
        if (arg0 instanceof KnightFamily)
            return 0;
        else
            return 999;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KnightFamily();
    }
}
