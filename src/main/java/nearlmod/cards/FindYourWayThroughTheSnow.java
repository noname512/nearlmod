package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.actions.SummonFriendAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.cards.friendcards.ArtificialSnowfall;
import nearlmod.orbs.Aurora;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;
import nearlmod.powers.ColdPower;

public class FindYourWayThroughTheSnow extends AbstractNearlCard {
    public static final String ID = "nearlmod:FindYourWayThroughTheSnow";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/findyourwaythroughthesnow.png";
    private static final int COST = 1;
    private static final int COLD_CNT = 2;

    public FindYourWayThroughTheSnow() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.RARE, CardTarget.ENEMY);
        cardsToPreview = new ArtificialSnowfall();
        magicNumber = baseMagicNumber = COLD_CNT;
        tags.add(NearlTags.FRIEND_RELATED);
        tags.add(NearlTags.IS_SUMMON_CARD);
        belongFriend = Aurora.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Aurora.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(m, p, new ColdPower(m, magicNumber)));
        if (extraTriggered()) {
            AbstractFriendCard card = new ArtificialSnowfall();
            if (upgraded) card.upgrade();
            addToBot(new AddFriendCardToHandAction(card));
        }
        else {
            addToBot(new SummonFriendAction(new Aurora()));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FindYourWayThroughTheSnow();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            cardsToPreview.upgrade();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
