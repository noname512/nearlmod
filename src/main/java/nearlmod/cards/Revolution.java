package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.orbs.Penance;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.LightPower;

public class Revolution extends AbstractNearlCard {
    public static final String ID = "nearlmod:Revolution";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/revolution.png";
    private static final int COST = 2;
    private static final int LIGHT_AMT = 15;
    private static final int CARD_AMT = 2;
    private static final int UPGRADE_PLUS_LIGHT = 5;

    public Revolution() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.COMMON, CardTarget.SELF);
        magicNumber = baseMagicNumber = LIGHT_AMT;
        secondMagicNumber = baseSecondMagicNumber = CARD_AMT;
        cardsToPreview = new SwitchType();
        cardsToPreview.upgrade();
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Penance.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Penance.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new LightPower(p, magicNumber)));
        if (extraTriggered()) {
            AbstractCard c = new SwitchType();
            c.upgrade();
            c.exhaust = true;
            c.isEthereal = true;
            addToBot(new MakeTempCardInHandAction(c, secondMagicNumber));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Revolution();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_LIGHT);
        }
    }
}
