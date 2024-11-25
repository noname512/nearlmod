package nearlmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import nearlmod.actions.PureDamageAllEnemiesAction;
import nearlmod.patches.CurseRelicPatch;

public class AnmasLove extends CustomRelic {

    public static final String ID = "nearlmod:AnmasLove";
    public static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final Texture IMG = new Texture("resources/nearlmod/images/relics/revenge.png");
    public static final Texture IMG_OUTLINE = new Texture("resources/nearlmod/images/relics/revenge_p.png");
    public int triggerIf = 2;
    public AnmasLove() {
        super(ID, IMG, IMG_OUTLINE, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            addToBot(new GainEnergyAction(1));
        }
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            addToBot(new DrawCardAction(1));
        }
    }

    @Override
    public void atTurnStart() {
        if (AbstractDungeon.relicRng.random(0, 99) <= triggerIf) {
            this.flash();
            addToBot(new PureDamageAllEnemiesAction(AbstractDungeon.player, 10, "Anma", AbstractGameAction.AttackEffect.NONE, DamageInfo.DamageType.THORNS));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AnmasLove();
    }
}
