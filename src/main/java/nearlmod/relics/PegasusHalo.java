package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PegasusHalo extends CustomRelic {

    public static final String ID = "nearlmod:PegasusHalo";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("images/relics/pegasushalo.png");
    public static final Texture IMG_OUTLINE = new Texture("images/relics/pegasushalo_p.png");
    public PegasusHalo() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.BOSS, LandingSound.FLAT);
        this.counter = 0;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        flash();
        AbstractPlayer p = AbstractDungeon.player;
        int sumBlock = this.counter + MathUtils.floor(blockAmount);
        addToBot(new AddTemporaryHPAction(p, p, sumBlock / 4));
        this.counter = sumBlock % 4;
        return MathUtils.floor(blockAmount);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(CureUp.ID);
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(CureUp.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); i++)
                if (AbstractDungeon.player.relics.get(i).relicId.equals(CureUp.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
        } else {
            super.obtain();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PegasusHalo();
    }
}
