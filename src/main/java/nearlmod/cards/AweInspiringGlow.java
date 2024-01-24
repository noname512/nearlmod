package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.cards.special.LightCard;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;

public class AweInspiringGlow extends AbstractNearlCard {
    public static final String ID = "nearlmod:AweInspiringGlow";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "images/cards/aweinspiringglow.png";
    private static final int COST = 0;
    private static final int LIGHT_AMT = 8;
    private static final int UPGRADE_PLUS_LIGHT = 3;

    public AweInspiringGlow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = LIGHT_AMT;
        cardsToPreview = new LightCard();
        isInnate = true;
        exhaust = true;
        tags.add(NearlTags.IS_GAIN_LIGHT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber)));
        addToBot(new MakeTempCardInHandAction(new LightCard(), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new AweInspiringGlow();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
        }
    }
}
