package nearlmod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.*;
import nearlmod.orbs.*;
import nearlmod.patches.AbstractCardEnum;

public class UnitedAsOne extends AbstractNearlCard {
    public static final String ID = "nearlmod:UnitedAsOne";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/unitedasone.png";
    private static final int COST = 0;

    public UnitedAsOne() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.SELF);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!Viviana.uniqueUsed && NLMOD.checkOrb(Viviana.ORB_ID))
            addToBot(new AddFriendCardToHandAction(new FlameShadow(), upgraded));
        if (!Blemishine.uniqueUsed && NLMOD.checkOrb(Blemishine.ORB_ID))
            addToBot(new AddFriendCardToHandAction(new DivineAvatar(), upgraded));
        if (!Nightingale.uniqueUsed && NLMOD.checkOrb(Nightingale.ORB_ID))
            addToBot(new AddFriendCardToHandAction(new WhiteFiendProtection(), upgraded));
        if (!Shining.uniqueUsed && NLMOD.checkOrb(Shining.ORB_ID))
            addToBot(new AddFriendCardToHandAction(new BlackFiendProtection(), upgraded));
        if (!Whislash.uniqueUsed && NLMOD.checkOrb(Whislash.ORB_ID))
            addToBot(new AddFriendCardToHandAction(new Rebuke(), upgraded));
    }

    @Override
    public AbstractCard makeCopy() {
        return new UnitedAsOne();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
