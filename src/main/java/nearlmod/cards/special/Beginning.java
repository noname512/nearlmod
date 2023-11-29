package nearlmod.cards.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.actions.SummonOrbAction;
import nearlmod.cards.AbstractNearlCard;
import nearlmod.orbs.Blemishine;
import nearlmod.patches.NearlTags;
import nearlmod.relics.LateLight;

public class Beginning extends AbstractNearlCard {
    public static final String ID = "nearlmod:Beginning";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/majestylight.png";
    private static final int COST = 1;
    private static final int UPGRADE_COST = 0;

    public Beginning() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.POWER, CardColor.COLORLESS,
                CardRarity.SPECIAL, CardTarget.SELF);
        tags.add(NearlTags.IS_SUMMON_CARD);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!p.hasRelic(LateLight.ID))
            addToBot(new SummonOrbAction(new Blemishine()));
        else
            p.getRelic(LateLight.ID).flash();
    }

    @Override
    public AbstractCard makeCopy() {
        return new Beginning();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADE_COST);
        }
    }
}
