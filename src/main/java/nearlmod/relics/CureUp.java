package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import static com.badlogic.gdx.math.MathUtils.floor;
import static java.lang.Math.min;

public class CureUp extends CustomRelic {

    public static final String ID = "nearlmod:CureUp";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/cureup.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/cureup_p.png");
    public static final int MAX_AMOUNT = 5;
    public CureUp() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atTurnStart() {
        this.counter = MAX_AMOUNT;
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        int block = floor(blockAmount);
        int exchange = min(block, this.counter);
        this.counter -= exchange;
        AbstractPlayer p = AbstractDungeon.player;
        if (exchange > 0) {
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new AddTemporaryHPAction(p, p, exchange));
        }
        return block - exchange;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new CureUp();
    }
}
