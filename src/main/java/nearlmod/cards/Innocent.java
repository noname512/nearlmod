package nearlmod.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nearlmod.NLMOD;
import nearlmod.actions.AddFriendCardToHandAction;
import nearlmod.cards.friendcards.AbstractFriendCard;
import nearlmod.cards.friendcards.WreathedInThorns;
import nearlmod.orbs.Penance;
import nearlmod.patches.AbstractCardEnum;
import nearlmod.patches.NearlTags;

public class Innocent extends AbstractNearlCard {
    public static final String ID = "nearlmod:Innocent";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "resources/nearlmod/images/cards/innocent.png";
    private static final int COST = 2;
    private static final int BLOCK_AMT = 14;
    private static final int UPGRADE_PLUS_BLOCK = 4;

    public Innocent() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.NEARL_GOLD,
                CardRarity.UNCOMMON, CardTarget.SELF);
        block = baseBlock = BLOCK_AMT;
        cardsToPreview = new WreathedInThorns();
        tags.add(NearlTags.FRIEND_RELATED);
        belongFriend = Penance.ORB_ID;
    }

    @Override
    public boolean extraTriggered() {
        return NLMOD.checkOrb(Penance.ORB_ID);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
        if (extraTriggered()) {
            AbstractFriendCard c = new WreathedInThorns();
            addToBot(new AddFriendCardToHandAction(c, upgraded, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Innocent();
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
            cardsToPreview.upgrade();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
