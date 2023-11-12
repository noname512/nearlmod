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

public class PegasusHalo extends CustomRelic {

    public static final String ID = "nearlmod:PegasusHalo";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/cureup.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/cureup_p.png");
    public PegasusHalo() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractPlayer p = AbstractDungeon.player;
        addToBot(new AddTemporaryHPAction(p, p, floor(blockAmount)));
        return 0;
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic("nearlmod:CureUp");
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PegasusHalo();
    }
}
